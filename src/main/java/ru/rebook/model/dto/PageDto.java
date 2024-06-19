package ru.rebook.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PageDto<T> {
    private List<T> contents;
    private long totalElements;
    private int totalPages;
}
