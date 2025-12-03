package com.bookstore.repository;

import com.bookstore.model.Buku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BukuRepository extends JpaRepository<Buku, Long> {

    // Cek ISBN unik
    boolean existsByIsbn(String isbn);

    // Optional: cari by ISBN
    Optional<Buku> findByIsbn(String isbn);

    // Search by keyword in multiple fields
    @Query("SELECT b FROM Buku b WHERE " +
            "LOWER(b.judul) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.penulis) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.penerbit) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.kategori) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Buku> findByKeyword(@Param("keyword") String keyword);
}