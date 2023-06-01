package com.study.jpa.chap05_practice.dto;

import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponseDTO<T> {

    private int startPage;
    private int endPage;
    private int currentPage;

    private boolean prev;
    private boolean next;

    private int totalCount;

    // 한 페이지에 배치할 페이지 수 ( 1 ~ 10 // 11 ~ 20 )
    private static final int PAGE_COUNT = 10;

    public PageResponseDTO(Page<T> pageData) {
        this.totalCount = (int) pageData.getTotalElements();
        this.currentPage = pageData.getPageable().getPageNumber() + 1; // 현재 페이지 정보
        this.endPage = (int) (Math.ceil((double) currentPage / PAGE_COUNT) * PAGE_COUNT); // 마지막 페이지
        this.startPage = endPage - PAGE_COUNT + 1; // 처음 페이지

        int realEnd = pageData.getTotalPages(); // 총 페이지 수

        if (realEnd < this.endPage) this.endPage = realEnd;

        this.prev = startPage > 1;
        this.next = endPage < realEnd;
    }
}
