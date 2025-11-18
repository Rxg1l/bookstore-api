package com.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object untuk Buku")
public class BukuDTO {
    
    @Schema(description = "ID buku", example = "1")
    private Long id;
    
    @Schema(description = "Judul buku", example = "Laskar Pelangi", required = true)
    private String judul;
    
    @Schema(description = "Penulis buku", example = "Andrea Hirata", required = true)
    private String penulis;
    
    @Schema(description = "Penerbit buku", example = "Bentang Pustaka")
    private String penerbit;
    
    @Schema(description = "Tahun terbit", example = "2005")
    private Integer tahunTerbit;
    
    @Schema(description = "ISBN buku", example = "978-979-1227-78-5")
    private String isbn;
    
    @Schema(description = "Deskripsi buku", example = "Novel tentang perjuangan anak-anak di Belitung")
    private String deskripsi;
    
    @Schema(description = "Jumlah halaman", example = "529")
    private Integer jumlahHalaman;
    
    @Schema(description = "Kategori buku", example = "Fiksi")
    private String kategori;
    
    @Schema(description = "Harga buku", example = "85000.0", required = true)
    private Double harga;
    
    @Schema(description = "Stok buku", example = "50", required = true)
    private Integer stok;
}