package test.aop.testaop.dto.dictionary;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import test.aop.testaop.dto.BaseDictionaryTypeAndValueDto;

@Getter
@SuperBuilder
@NoArgsConstructor
public class AddDictionaryValueDto extends BaseDictionaryTypeAndValueDto {

    @NotBlank(message = "Не может быть пустым или состоять из пробелов")
    private String dictionaryTypeCode;

    private String parentCode;

}
