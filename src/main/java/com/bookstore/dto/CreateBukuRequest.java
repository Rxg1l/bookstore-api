package com.bookstore.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBukuRequest {

    @NotBlank(message = "Judul tidak boleh kosong")
    @Size(min = 2, max = 200, message = "Judul harus 2-200 karakter")
    private String judul;

    @NotBlank(message = "Penulis tidak boleh kosong")
    @Size(min = 2, max = 100, message = "Penulis harus 2-100 karakter")
    private String penulis;

    @NotBlank(message = "Penerbit tidak boleh kosong")
    @Size(min = 2, max = 100, message = "Penerbit harus 2-100 karakter")
    private String penerbit;

    @NotNull(message = "Tahun terbit tidak boleh kosong")
    @Min(value = 1900, message = "Tahun terbit minimal 1900")
    @Max(value = 2100, message = "Tahun terbit maksimal 2100")
    private Integer tahunTerbit;

    @NotBlank(message = "ISBN tidak boleh kosong")
    @Pattern(regexp = "^[0-9-]+$", message = "ISBN hanya boleh mengandung angka dan tanda hubung")
    private String isbn;

    @NotBlank(message = "Deskripsi tidak boleh kosong")
    @Size(min = 10, max = 2000, message = "Deskripsi harus 10-2000 karakter")
    private String deskripsi;

    @NotNull(message = "Jumlah halaman tidak boleh kosong")
    @Min(value = 1, message = "Jumlah halaman minimal 1")
    @Max(value = 10000, message = "Jumlah halaman maksimal 10000")
    private Integer jumlahHalaman;

    @NotBlank(message = "Kategori tidak boleh kosong")
    private String kategori;

    @NotNull(message = "Harga tidak boleh kosong")
    @DecimalMin(value = "0.0", inclusive = false, message = "Harga harus lebih dari 0")
    private Double harga;

    @NotNull(message = "Stok tidak boleh kosong")
    @Min(value = 0, message = "Stok tidak boleh negatif")
    private Integer stok;
}