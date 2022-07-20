package test.aop.testaop.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import test.aop.testaop.utils.ApiResponse;
import test.aop.testaop.utils.mess.LocalizedText;
import test.aop.testaop.utils.mess.MessageUtils;

import java.util.ArrayList;
import java.util.Arrays;

@ControllerAdvice
class ErrorHandlingControllerAdvice {

    private final MessageUtils messageUtils;

    ErrorHandlingControllerAdvice(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiResponse> onConstraintValidationException(
            ConstraintViolationException e) {
        var list = new ArrayList<LocalizedText>();
        for (var violation : e.getConstraintViolations()) {
            list.add(addFieldName(messageUtils.getMessage(violation.getMessage()), violation.getPropertyPath().toString()));
        }
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .errors(list)
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        var list = new ArrayList<LocalizedText>();
        for (var fieldError : e.getBindingResult().getFieldErrors()) {
            list.add(addFieldName(messageUtils.getMessage(fieldError.getDefaultMessage()), fieldError.getField()));
        }
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .errors(list)
                        .build()
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<ApiResponse> onHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        var list = new ArrayList<LocalizedText>();
        list.add(new LocalizedText(
                "Method used: " + e.getMethod() + ", Support methods: " + Arrays.toString(e.getSupportedMethods()),
                "Method used: " + e.getMethod() + ", Support methods: " + Arrays.toString(e.getSupportedMethods()),
                "Method used: " + e.getMethod() + ", Support methods: " + Arrays.toString(e.getSupportedMethods())
        ));
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                        .errors(list)
                        .build()
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<ApiResponse> onMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        var list = new ArrayList<LocalizedText>();
        list.add(new LocalizedText(
                "Missed parameter: " + e.getParameterName() + ", parametr type: " + e.getParameterType(),
                "Missed parameter: " + e.getParameterName() + ", parametr type: " + e.getParameterType(),
                "Missed parameter: " + e.getParameterName() + ", parametr type: " + e.getParameterType()
        ));
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .errors(list)
                        .build()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse> onHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        var list = new ArrayList<LocalizedText>();
        list.add(new LocalizedText(
                e.getMessage(),
                e.getMessage(),
                e.getMessage()
        ));
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .errors(list)
                        .build()
        );
    }

    @ExceptionHandler(VoidException.class)
    ResponseEntity<ApiResponse> onVoidException(VoidException e) {
        var list = new ArrayList<LocalizedText>();
        list.add(messageUtils.getMessage(e.getMessage()));
        if (e.isInternal()) {
            return ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                            .errors(list)
                            .build()
            );
        } else {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .errors(list)
                            .build()
            );
        }
    }

    private LocalizedText addFieldName(LocalizedText lt, String name) {
        lt.setRu(name + " : " + lt.getRu());
        lt.setKk(name + " : " + lt.getKk());
        lt.setEn(name + " : " + lt.getEn());
        return lt;
    }

}