package com.bookstore.service;

import com.bookstore.dto.BukuDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BukuService {

    // GET ALL - tanpa pagination
    List<BukuDTO> getAllBuku();

    // GET ALL - dengan pagination (return Page)
    Page<BukuDTO> getAllBukuPaginated(Pageable pageable);

    // GET BY ID
    BukuDTO getBukuById(Long id);

    // CREATE - hanya perlu method dengan BukuDTO saja
    BukuDTO createBuku(BukuDTO bukuDTO);

    // UPDATE
    BukuDTO updateBuku(Long id, BukuDTO bukuDTO);

    // DELETE
    void deleteBuku(Long id);

    // SEARCH
    List<BukuDTO> searchBuku(String keyword);
}