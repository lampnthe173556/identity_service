package com.devteria.identityService.exception;

import com.devteria.identityService.dto.response.ApiResponse;
import com.devteria.identityService.enums.ErrorCode;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;
import java.util.Objects;


@Slf4j
@ControllerAdvice
//class nay chiu trach nhiem handling exception
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";
    //define exception ow day

    //handler, khi xh runtime exception no se chay vao day xu ly
    @ExceptionHandler(value = RuntimeException.class)
    //return response entity
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException runtimeException) {
        //khai bao parameter vao method, spring inject exception vao parameter
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(505);
        apiResponse.setMessage(runtimeException.getMessage());
        //tra ve ten loi
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingAppException(RuntimeException exception) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException appException) {
        ErrorCode errorCode = appException.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    ResponseEntity<String> handlingMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
//        //getFiledError => lay duoc ten field bi loi
//        return ResponseEntity.badRequest().body(methodArgumentNotValidException.getFieldError().getDefaultMessage()
//                + " At filed:" +
//                methodArgumentNotValidException.getFieldError().getField());
//    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException
            (MethodArgumentNotValidException methodArgumentNotValidException) {
        String enumKey = methodArgumentNotValidException.getFieldError().getDefaultMessage();
        Map<String, Object> attributes = null;
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintViolation = methodArgumentNotValidException.getBindingResult().
                    getAllErrors().get(0).unwrap(ConstraintViolation.class);
            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());


        } catch (IllegalArgumentException exception) {

        }


        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(Objects.nonNull(attributes) ? mapAttribute(errorCode.getMessage(), attributes) : errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getStatusCode()).
                body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        //format chuan java replace 1 chuoi dung ngoac nhon
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
