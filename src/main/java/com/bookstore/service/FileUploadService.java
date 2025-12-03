package com.bookstore.service;

import com.bookstore.dto.BukuDTO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final BukuService bukuService;

    public List<BukuDTO> processExcelFile(MultipartFile file) throws IOException {
        List<BukuDTO> bukuList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                try {
                    BukuDTO bukuDTO = createBukuDTOFromRow(row);

                    // Skip jika judul kosong (baris kosong di Excel)
                    if (bukuDTO.getJudul() == null || bukuDTO.getJudul().trim().isEmpty()) {
                        continue;
                    }

                    BukuDTO savedBuku = bukuService.createBuku(bukuDTO);
                    bukuList.add(savedBuku);
                } catch (Exception e) {
                    System.err.println("Error processing row " + row.getRowNum() + ": " + e.getMessage());
                    // Continue ke row berikutnya meski ada error
                }
            }
        }

        return bukuList;
    }

    private BukuDTO createBukuDTOFromRow(Row row) {
        BukuDTO dto = new BukuDTO();

        // Judul - kolom 0
        dto.setJudul(getCellValueAsString(row.getCell(0)));

        // Penulis - kolom 1
        dto.setPenulis(getCellValueAsString(row.getCell(1)));

        // Penerbit - kolom 2
        dto.setPenerbit(getCellValueAsString(row.getCell(2)));

        // Tahun Terbit - kolom 3
        dto.setTahunTerbit(parseIntegerCell(row.getCell(3), 2024));

        // ISBN - kolom 4
        dto.setIsbn(getCellValueAsString(row.getCell(4)));

        // Deskripsi - kolom 5
        dto.setDeskripsi(getCellValueAsString(row.getCell(5)));

        // Jumlah Halaman - kolom 6
        dto.setJumlahHalaman(parseIntegerCell(row.getCell(6), 100));

        // Kategori - kolom 7
        dto.setKategori(getCellValueAsString(row.getCell(7)));

        // Harga - kolom 8
        dto.setHarga(parseDoubleCell(row.getCell(8), 0.0));

        // Stok - kolom 9
        dto.setStok(parseIntegerCell(row.getCell(9), 0));

        return dto;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Remove .0 from integer values
                    double value = cell.getNumericCellValue();
                    if (value == Math.floor(value)) {
                        return String.valueOf((int) value);
                    } else {
                        return String.valueOf(value);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return "";
        }
    }

    private Integer parseIntegerCell(Cell cell, Integer defaultValue) {
        if (cell == null) {
            return defaultValue;
        }

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                return Integer.parseInt(cell.getStringCellValue().trim());
            }
        } catch (Exception e) {
            // Ignore and return default
        }

        return defaultValue;
    }

    private Double parseDoubleCell(Cell cell, Double defaultValue) {
        if (cell == null) {
            return defaultValue;
        }

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                return Double.parseDouble(cell.getStringCellValue().trim());
            }
        } catch (Exception e) {
            // Ignore and return default
        }

        return defaultValue;
    }
}