package test.aop.testaop.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    public ResponseEntity<ApiResponse> ok(Object in) {
        return ResponseEntity.ok(ApiResponse.builder()
                .httpStatus(HttpStatus.OK)
                .data(in)
                .build());
    }

    protected <T> ResponseEntity<T> ok() {
        return ResponseEntity.ok().build();
    }
}
