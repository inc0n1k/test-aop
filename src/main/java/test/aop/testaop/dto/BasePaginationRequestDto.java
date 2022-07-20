package test.aop.testaop.dto;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@MappedSuperclass
public abstract class BasePaginationRequestDto {

    private int page = 0;

    private int size = 10;

    private Sort.Direction direction = Sort.Direction.ASC;

    private String sort = "id";

}