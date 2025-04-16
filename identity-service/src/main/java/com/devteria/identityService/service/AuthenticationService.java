package com.devteria.identityService.service;

import com.devteria.identityService.dto.request.AuthenticationRequest;
import com.devteria.identityService.dto.request.IntrospectRequest;
import com.devteria.identityService.dto.request.LogoutRequest;
import com.devteria.identityService.dto.response.AuthenticationResponse;
import com.devteria.identityService.dto.response.IntrospectResponse;
import com.devteria.identityService.entities.InvalidatedToken;
import com.devteria.identityService.entities.User;
import com.devteria.identityService.exception.AppException;
import com.devteria.identityService.enums.ErrorCode;
import com.devteria.identityService.repositories.InvalidatedRepository;
import com.devteria.identityService.repositories.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    InvalidatedRepository invalidatedRepository;
    @NonFinal//no inject constructor
    @Value(("${jwt.signerKey}"))//annotation doc bien tu file yaml
    protected String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }


        return IntrospectResponse.
                builder().
                valid(isValid).
                build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findUserByUsername(request.getUsername()).
                orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(user);
        return AuthenticationResponse.
                builder().
                token(token).
                authenticated(true).
                build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());
        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
        invalidatedRepository.save(invalidatedToken);
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);
        if (!(verified && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (invalidatedRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }


        return signedJWT;
    }

    private String generateToken(User user) {
        //header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        //body
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().
                subject(user.getUsername())
                .issuer("devteria.com")//dinh danh token do ai issuer
                .issueTime(new Date())//thoi gian tao ra token
                .expirationTime(
                        new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                        ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))//thoi gian het token
                .build();
        //payload => convert JWTClaimSet to payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        //jwsObject
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            //sign signature
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    //phai lay nhu nay de theo chuan authorization thi spring security se tu lay role
    //trong scope de phan quyen
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permissions -> {
                        stringJoiner.add(permissions.getName());
                    });
                }
            });
        }
        return stringJoiner.toString();
    }
}
