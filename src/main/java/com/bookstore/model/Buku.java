package com.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "buku")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Buku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Biarkan seperti ini untuk MySQL
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String judul;
    
    @Column(nullable = false, length = 100)
    private String penulis;
    
    @Column(length = 100)
    private String penerbit;
    
    @Column(name = "tahun_terbit")
    private Integer tahunTerbit;
    
    @Column(length = 20)
    private String isbn;
    
    @Column(columnDefinition = "TEXT")
    private String deskripsi;
    
    @Column(name = "jumlah_halaman")
    private Integer jumlahHalaman;
    
    @Column(length = 50)
    private String kategori;
    
    @Column
    private Double harga;
    
    @Column
    private Integer stok;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}