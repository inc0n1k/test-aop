package test.aop.testaop.utils;

import lombok.*;
import org.springframework.http.HttpStatus;
import test.aop.testaop.utils.mess.LocalizedText;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private HttpStatus httpStatus;
//    private LocalizedText message;
    private List<LocalizedText> errors;
    private Object data;

}