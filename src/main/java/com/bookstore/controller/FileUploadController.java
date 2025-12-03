package com.bookstore.controller;

import com.bookstore.dto.BukuDTO;
import com.bookstore.dto.CustomResponse;
import com.bookstore.dto.ResponseUtil;
import com.bookstore.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/excel")
    public ResponseEntity<CustomResponse<List<BukuDTO>>> uploadExcelFile(
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseUtil.error("File tidak boleh kosong");
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null ||
                    (!originalFilename.endsWith(".xlsx") && !originalFilename.endsWith(".xls"))) {
                return ResponseUtil.error("Format file harus Excel (.xlsx atau .xls)");
            }

            List<BukuDTO> bukuList = fileUploadService.processExcelFile(file);

            return ResponseUtil.success(
                    String.format("Berhasil mengupload %d buku dari file", bukuList.size()),
                    bukuList);

        } catch (Exception e) {
            return ResponseUtil.error("Gagal mengupload file: " + e.getMessage());
        }
    }
}