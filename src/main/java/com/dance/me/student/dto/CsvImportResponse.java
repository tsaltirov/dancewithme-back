package com.dance.me.student.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CsvImportResponse {
    private int total;
    private int created;
    private int failed;
    private List<CsvRowError> errors;
}
