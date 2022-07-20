package test.aop.testaop.dto.dictionary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import test.aop.testaop.dto.BaseDictionaryTypeAndValueDto;

@Getter
@SuperBuilder
@NoArgsConstructor
public class UpdateDictionaryTypeDto extends BaseDictionaryTypeAndValueDto {

    private String newTypeCode;

}