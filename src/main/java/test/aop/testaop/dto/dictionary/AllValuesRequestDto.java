package test.aop.testaop.dto.dictionary;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import test.aop.testaop.dto.constant.DisplayType;
import test.aop.testaop.dto.constant.RequestType;

import java.util.Map;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllValuesRequestDto {

    @NotBlank(message = "Код обязательно должен быть")
    private String typeCode;

    private String searchByName;

    private String searchByCode;

    private Map<String, Object> parameters;

    private DisplayType displayType = DisplayType.SHORT;

    private RequestType requestType = RequestType.EQUALS;

}