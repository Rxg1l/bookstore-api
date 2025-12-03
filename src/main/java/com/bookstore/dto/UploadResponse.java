package com.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {
    private String message;
    private int totalProcessed;
    private int successCount;
    private int errorCount;
    private List<String> errors;
    private String fileType;
}
