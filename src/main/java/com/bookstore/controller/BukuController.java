package com.bookstore.controller;

import com.bookstore.dto.BukuDTO;
import com.bookstore.dto.CreateBukuRequest;
import com.bookstore.service.BukuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buku")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Book Management", description = "API untuk manajemen data buku")
public class BukuController {
    
    private final BukuService bukuService;

    @Operation(summary = "Get semua buku", description = "Mendapatkan daftar semua buku yang tersedia")
    @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan data buku")
    @GetMapping
    public ResponseEntity<List<BukuDTO>> getAllBuku() {
        List<BukuDTO> bukuList = bukuService.getAllBuku();
        return ResponseEntity.ok(bukuList);
    }

    @Operation(summary = "Get buku by ID", description = "Mendapatkan detail buku berdasarkan ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Buku ditemukan"),
        @ApiResponse(responseCode = "404", description = "Buku tidak ditemukan", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BukuDTO> getBukuById(
            @Parameter(description = "ID buku yang ingin dicari") @PathVariable Long id) {
        BukuDTO buku = bukuService.getBukuById(id);
        return ResponseEntity.ok(buku);
    }

    @Operation(summary = "Tambah buku baru", description = "Menambahkan buku baru ke dalam sistem")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Buku berhasil dibuat"),
        @ApiResponse(responseCode = "400", description = "Data input tidak valid", content = @Content)
    })
    @PostMapping
    public ResponseEntity<BukuDTO> createBuku(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Data buku yang akan dibuat",
                required = true,
                content = @Content(schema = @Schema(implementation = CreateBukuRequest.class))
            )
            @RequestBody CreateBukuRequest request) {
        BukuDTO createdBuku = bukuService.createBuku(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBuku);
    }

    @Operation(summary = "Update buku", description = "Memperbarui data buku berdasarkan ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Buku berhasil diupdate"),
        @ApiResponse(responseCode = "404", description = "Buku tidak ditemukan", content = @Content)
    })

    @PutMapping("/{id}")
    public ResponseEntity<BukuDTO> updateBuku(
            @Parameter(description = "ID buku yang akan diupdate") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Data buku yang akan diupdate",
                required = true,
                content = @Content(schema = @Schema(implementation = CreateBukuRequest.class))
            )
            @RequestBody CreateBukuRequest request) {
        BukuDTO updatedBuku = bukuService.updateBuku(id, request);
        return ResponseEntity.ok(updatedBuku);
    }

    @Operation(summary = "Hapus buku", description = "Menghapus buku berdasarkan ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Buku berhasil dihapus"),
        @ApiResponse(responseCode = "404", description = "Buku tidak ditemukan", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuku(
            @Parameter(description = "ID buku yang akan dihapus") @PathVariable Long id) {
        bukuService.deleteBuku(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cari buku by judul", description = "Mencari buku berdasarkan kata kunci dalam judul")
    @ApiResponse(responseCode = "200", description = "Hasil pencarian")
    @GetMapping("/search/judul")
    public ResponseEntity<List<BukuDTO>> searchByJudul(
            @Parameter(description = "Kata kunci pencarian judul") @RequestParam String judul) {
        List<BukuDTO> bukuList = bukuService.searchByJudul(judul);
        return ResponseEntity.ok(bukuList);
    }

    @Operation(summary = "Cari buku by penulis", description = "Mencari buku berdasarkan nama penulis")
    @ApiResponse(responseCode = "200", description = "Hasil pencarian")
    @GetMapping("/search/penulis")
    public ResponseEntity<List<BukuDTO>> searchByPenulis(
            @Parameter(description = "Nama penulis") @RequestParam String penulis) {
        List<BukuDTO> bukuList = bukuService.searchByPenulis(penulis);
        return ResponseEntity.ok(bukuList);
    }

    @Operation(summary = "Cari buku by kategori", description = "Mencari buku berdasarkan kategori")
    @ApiResponse(responseCode = "200", description = "Hasil pencarian")
    @GetMapping("/search/kategori")
    public ResponseEntity<List<BukuDTO>> searchByKategori(
            @Parameter(description = "Kategori buku") @RequestParam String kategori) {
        List<BukuDTO> bukuList = bukuService.searchByKategori(kategori);
        return ResponseEntity.ok(bukuList);
    }

    @Operation(summary = "Cari buku by keyword", description = "Mencari buku berdasarkan kata kunci di judul, penulis, atau kategori")
    @ApiResponse(responseCode = "200", description = "Hasil pencarian")
    @GetMapping("/search")
    public ResponseEntity<List<BukuDTO>> searchByKeyword(
            @Parameter(description = "Kata kunci pencarian") @RequestParam String keyword) {
        List<BukuDTO> bukuList = bukuService.searchByKeyword(keyword);
        return ResponseEntity.ok(bukuList);
    }
}