package test.aop.testaop.dto.dictionary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import test.aop.testaop.dto.BaseDictionaryTypeAndValueDto;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FullDictionaryTypeDto extends BaseDictionaryTypeAndValueDto {

    List<ShortDictionaryValueDto> values;
}
