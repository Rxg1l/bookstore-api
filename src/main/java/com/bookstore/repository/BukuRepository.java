package com.bookstore.repository;

import com.bookstore.model.Buku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BukuRepository extends JpaRepository<Buku, Long> {
    
    // Mencari buku berdasarkan judul (case insensitive)
    List<Buku> findByJudulContainingIgnoreCase(String judul);
    
    // Mencari buku berdasarkan penulis
    List<Buku> findByPenulisContainingIgnoreCase(String penulis);
    
    // Mencari buku berdasarkan kategori
    List<Buku> findByKategoriContainingIgnoreCase(String kategori);
    
    // Mencari buku berdasarkan tahun terbit
    List<Buku> findByTahunTerbit(Integer tahunTerbit);
    
    // Mencari buku dengan stok tersedia
    List<Buku> findByStokGreaterThan(Integer stok);
    
    // Custom query untuk pencarian advanced
    @Query("SELECT b FROM Buku b WHERE " +
           "LOWER(b.judul) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.penulis) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.kategori) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Buku> searchByKeyword(@Param("keyword") String keyword);
}