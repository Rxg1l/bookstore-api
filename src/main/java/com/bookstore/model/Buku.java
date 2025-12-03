package com.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "buku")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Buku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String judul;

    @Column(nullable = false, length = 100)
    private String penulis;

    @Column(nullable = false, length = 100)
    private String penerbit;

    @Column(name = "tahun_terbit", nullable = false)
    private Integer tahunTerbit;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String deskripsi;

    @Column(name = "jumlah_halaman", nullable = false)
    private Integer jumlahHalaman;

    @Column(nullable = false, length = 50)
    private String kategori;

    @Column(nullable = false)
    private Double harga;

    @Column(nullable = false)
    private Integer stok;
}