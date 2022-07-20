package test.aop.testaop.dto.dictionary;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import test.aop.testaop.dto.BaseDictionaryTypeAndValueDto;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class ShortTreeValueDto extends BaseDictionaryTypeAndValueDto {

    private String dictionaryTypeCode;

    private String parentCode;

    private List<ShortTreeValueDto> child;

}