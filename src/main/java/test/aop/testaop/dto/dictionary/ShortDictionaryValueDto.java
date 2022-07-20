package test.aop.testaop.dto.dictionary;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import test.aop.testaop.dto.BaseDictionaryTypeAndValueDto;

@Getter
@SuperBuilder
public class ShortDictionaryValueDto extends BaseDictionaryTypeAndValueDto {

    private String dictionaryTypeCode;

    private String parentCode;

}
