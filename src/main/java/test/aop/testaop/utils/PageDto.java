package test.aop.testaop.utils;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageDto<T> {

    private int totalPages;
    private long totalElements;
    private int size;
    private int page;
    private List<T> data;

    public PageDto(List<T> data, Page page) {
        this.data = data;
        this.size = data.size();
        this.page = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }

    public PageDto(List<T> data, int size, int page, int totalPages, long totalElements) {
        this.data = data;
        this.size = size;
        this.page = page;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

}
