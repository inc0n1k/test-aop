package test.aop.testaop.dto.dictionary;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public class AllValuesPageRequestDto extends AllValuesRequestDto {

    @PositiveOrZero
    private int page = 0;

    @Positive
    private int size = 10;

    private Sort.Direction direction = Sort.Direction.ASC;

    private String sort = "id";

}
