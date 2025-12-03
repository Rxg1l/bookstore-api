package com.bookstore.controller;

import com.bookstore.dto.BukuDTO;
import com.bookstore.dto.CustomResponse;
import com.bookstore.dto.ResponseUtil;
import com.bookstore.service.BukuService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/buku")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class BukuController {

    private final BukuService bukuService;

    // GET ALL - Tanpa pagination
    @GetMapping
    public ResponseEntity<CustomResponse<List<BukuDTO>>> getAllBuku() {
        List<BukuDTO> semuaBuku = bukuService.getAllBuku();
        return ResponseUtil.success("Data buku berhasil diambil", semuaBuku);
    }

    // GET ALL - Dengan pagination
    @GetMapping("/paginated")
    public ResponseEntity<CustomResponse<List<BukuDTO>>> getAllBukuPaginated(
            Pageable pageable) {
        Page<BukuDTO> bukuPage = bukuService.getAllBukuPaginated(pageable);
        return ResponseUtil.paginated(bukuPage, "Data buku berhasil diambil");
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<BukuDTO>> getBukuById(@PathVariable Long id) {
        try {
            BukuDTO buku = bukuService.getBukuById(id);
            return ResponseUtil.success("Buku berhasil ditemukan", buku);
        } catch (Exception e) {
            return ResponseUtil.notFound(e.getMessage());
        }
    }

    // CREATE
    @PostMapping
    public ResponseEntity<CustomResponse<BukuDTO>> createBuku(@RequestPart("buku") String bukuJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BukuDTO bukuDTO = objectMapper.readValue(bukuJson, BukuDTO.class);

            BukuDTO bukuBaru = bukuService.createBuku(bukuDTO);
            return ResponseUtil.created("Buku berhasil ditambahkan", bukuBaru);
        } catch (Exception e) {
            return ResponseUtil.error("Gagal menambahkan buku: " + e.getMessage());
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<BukuDTO>> updateBuku(
            @PathVariable Long id,
            @Valid @RequestBody BukuDTO bukuDTO) {
        try {
            BukuDTO bukuUpdated = bukuService.updateBuku(id, bukuDTO);
            return ResponseUtil.success("Buku berhasil diupdate", bukuUpdated);
        } catch (Exception e) {
            return ResponseUtil.error("Gagal mengupdate buku: " + e.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Void>> deleteBuku(@PathVariable Long id) {
        try {
            bukuService.deleteBuku(id);
            return ResponseUtil.success("Buku berhasil dihapus");
        } catch (Exception e) {
            return ResponseUtil.error("Gagal menghapus buku: " + e.getMessage());
        }
    }

    // SEARCH
    @GetMapping("/search")
    public ResponseEntity<CustomResponse<List<BukuDTO>>> searchBuku(
            @RequestParam String keyword) {
        try {
            List<BukuDTO> hasilPencarian = bukuService.searchBuku(keyword);
            return ResponseUtil.success(
                    "Hasil pencarian berhasil didapatkan (jika data kosong berarti sudah dihapus admin)",
                    hasilPencarian);
        } catch (Exception e) {
            return ResponseUtil.error("Gagal mencari buku: " + e.getMessage());
        }
    }
}

// ✅ PAGINATION ENDPOINT - FIXED
// @Operation(summary = "Get semua buku dengan pagination")
// @GetMapping("/paginated")
// public ResponseEntity<PaginationResponse<BukuDTO>> getAllBukuPagination(
// @RequestParam(defaultValue = "0") int page,
// @RequestParam(defaultValue = "10") int size,
// @RequestParam(defaultValue = "id") String sortBy,
// @RequestParam(defaultValue = "asc") String sortDir) {
// try {
// PaginationResponse<BukuDTO> response = bukuService.getAllBukuPagination(page,
// size, sortBy, sortDir);
// return ResponseEntity.ok(response);
// } catch (Exception e) {
// PaginationResponse<BukuDTO> errorResponse = PaginationResponse
// .createError("Gagal mengambil data: " + e.getMessage());
// return ResponseEntity.badRequest().body(errorResponse);
// }
// }

// @Operation(summary = "Search buku dengan pagination")
// @GetMapping("/search/pagination")
// public ResponseEntity<PaginationResponse<BukuDTO>> searchByKeywordPagination(
// @RequestParam String keyword,
// @RequestParam(defaultValue = "0") int page,
// @RequestParam(defaultValue = "10") int size) {
// try {
// PaginationResponse<BukuDTO> response =
// bukuService.searchByKeywordPagination(keyword, page, size);
// return ResponseEntity.ok(response);
// } catch (Exception e) {
// PaginationResponse<BukuDTO> errorResponse = PaginationResponse
// .createError("Gagal mencari data: " + e.getMessage());
// return ResponseEntity.badRequest().body(errorResponse);
// }
// }

// ✅ REGULAR ENDPOINTS
// @Operation(summary = "Get semua buku")
// @GetMapping
// public ResponseEntity<CustomResponse<List<BukuDTO>>> getAllBuku() {
// try {
// List<BukuDTO> bukuList = bukuService.getAllBuku();
// return ResponseEntity.ok(CustomResponse.success("Data buku berhasil diambil",
// bukuList));
// } catch (Exception e) {
// return ResponseEntity.badRequest()
// .body(CustomResponse.error("Gagal mengambil data buku: " + e.getMessage()));
// }
// }

// @Operation(summary = "Get buku by ID")
// @GetMapping("/{id}")
// public ResponseEntity<CustomResponse<BukuDTO>> getBukuById(@PathVariable Long
// id) {
// try {
// BukuDTO buku = bukuService.getBukuById(id);
// return ResponseEntity.ok(CustomResponse.success("Buku ditemukan", buku));
// } catch (Exception e) {
// return ResponseEntity.badRequest().body(CustomResponse.error("Buku tidak
// ditemukan: " + e.getMessage()));
// }
// }

// @Operation(summary = "Tambah buku baru (JSON)")
// @PostMapping
// public ResponseEntity<CustomResponse<BukuDTO>> createBuku(@RequestBody
// CreateBukuRequest request) {
// try {
// BukuDTO createdBuku = bukuService.createBuku(request);
// return ResponseEntity.ok(CustomResponse.success("Buku berhasil dibuat",
// createdBuku));
// } catch (Exception e) {
// return ResponseEntity.badRequest().body(CustomResponse.error("Gagal membuat
// buku: " + e.getMessage()));
// }
// }

// @Operation(summary = "Tambah buku via form-data")
// @PostMapping("/form")
// public ResponseEntity<CustomResponse<BukuDTO>> createBukuViaForm(
// @RequestParam String judul,
// @RequestParam String penulis,
// @RequestParam(required = false) String penerbit,
// @RequestParam(required = false) Integer tahunTerbit,
// @RequestParam(required = false) String isbn,
// @RequestParam(required = false) String deskripsi,
// @RequestParam(required = false) Integer jumlahHalaman,
// @RequestParam(required = false) String kategori,
// @RequestParam Double harga,
// @RequestParam(required = false) Integer stok) {

// try {
// CreateBukuRequest request = new CreateBukuRequest();
// request.setJudul(judul);
// request.setPenulis(penulis);
// request.setPenerbit(penerbit);
// request.setTahunTerbit(tahunTerbit);
// request.setIsbn(isbn);
// request.setDeskripsi(deskripsi);
// request.setJumlahHalaman(jumlahHalaman);
// request.setKategori(kategori);
// request.setHarga(harga);
// request.setStok(stok != null ? stok : 0);

// BukuDTO createdBuku = bukuService.createBuku(request);
// return ResponseEntity.ok(CustomResponse.success("Buku berhasil dibuat via
// form", createdBuku));
// } catch (Exception e) {
// return ResponseEntity.badRequest().body(CustomResponse.error("Gagal membuat
// buku: " + e.getMessage()));
// }
// }
// }