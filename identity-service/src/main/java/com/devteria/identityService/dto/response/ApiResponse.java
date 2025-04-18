package com.devteria.identityService.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@JsonInclude(JsonInclude.Include.NON_NULL) // auto sezial lize nhung field bi null
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// contain all field normalize code
public class ApiResponse<T> {
    @Builder.Default
    int code = 200;

    String message;
    T result;
}
