package com.bookstore.service.impl;

import com.bookstore.dto.BukuDTO;
import com.bookstore.model.Buku;
import com.bookstore.repository.BukuRepository;
import com.bookstore.service.BukuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BukuServiceImpl implements BukuService {

    private final BukuRepository bukuRepository;

    @Override
    public List<BukuDTO> getAllBuku() {
        return bukuRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BukuDTO> getAllBukuPaginated(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable parameter must not be null");
        }
        return bukuRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    public BukuDTO getBukuById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        Buku buku = bukuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Buku dengan ID " + id + " tidak ditemukan"));
        return convertToDTO(buku);
    }

    @Override
    public BukuDTO createBuku(BukuDTO bukuDTO) {
        // Cek ISBN unik
        if (bukuRepository.existsByIsbn(bukuDTO.getIsbn())) {
            throw new RuntimeException("ISBN " + bukuDTO.getIsbn() + " sudah digunakan");
        }

        Buku buku = convertToEntity(bukuDTO);
        if (buku == null) {
            throw new IllegalArgumentException("Buku entity must not be null");
        }
        Buku savedBuku = bukuRepository.save(buku);
        return convertToDTO(savedBuku);
    }

    @Override
    public BukuDTO updateBuku(Long id, BukuDTO bukuDTO) {
        Objects.requireNonNull(id, "id must not be null");
        Buku existingBuku = bukuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Buku dengan ID " + id + " tidak ditemukan"));

        // Cek ISBN unik untuk buku lain
        if (!existingBuku.getIsbn().equals(bukuDTO.getIsbn()) &&
                bukuRepository.existsByIsbn(bukuDTO.getIsbn())) {
            throw new RuntimeException("ISBN " + bukuDTO.getIsbn() + " sudah digunakan oleh buku lain");
        }

        existingBuku.setJudul(bukuDTO.getJudul());
        existingBuku.setPenulis(bukuDTO.getPenulis());
        existingBuku.setPenerbit(bukuDTO.getPenerbit());
        existingBuku.setTahunTerbit(bukuDTO.getTahunTerbit());
        existingBuku.setIsbn(bukuDTO.getIsbn());
        existingBuku.setDeskripsi(bukuDTO.getDeskripsi());
        existingBuku.setJumlahHalaman(bukuDTO.getJumlahHalaman());
        existingBuku.setKategori(bukuDTO.getKategori());
        existingBuku.setHarga(bukuDTO.getHarga());
        existingBuku.setStok(bukuDTO.getStok());

        Buku updatedBuku = bukuRepository.save(existingBuku);
        return convertToDTO(updatedBuku);
    }

    @Override
    public void deleteBuku(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!bukuRepository.existsById(id)) {
            throw new RuntimeException("Buku dengan ID " + id + " tidak ditemukan");
        }
        bukuRepository.deleteById(id);
    }

    @Override
    public List<BukuDTO> searchBuku(String keyword) {
        if (keyword == null) {
            return List.of();
        }
        return bukuRepository.findByKeyword(keyword.toLowerCase()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ===== HELPER METHODS =====

    private BukuDTO convertToDTO(Buku buku) {
        BukuDTO dto = new BukuDTO();
        dto.setId(buku.getId());
        dto.setJudul(buku.getJudul());
        dto.setPenulis(buku.getPenulis());
        dto.setPenerbit(buku.getPenerbit());
        dto.setTahunTerbit(buku.getTahunTerbit());
        dto.setIsbn(buku.getIsbn());
        dto.setDeskripsi(buku.getDeskripsi());
        dto.setJumlahHalaman(buku.getJumlahHalaman());
        dto.setKategori(buku.getKategori());
        dto.setHarga(buku.getHarga());
        dto.setStok(buku.getStok());
        return dto;
    }

    private Buku convertToEntity(BukuDTO dto) {
        Buku buku = new Buku();
        buku.setJudul(dto.getJudul());
        buku.setPenulis(dto.getPenulis());
        buku.setPenerbit(dto.getPenerbit());
        buku.setTahunTerbit(dto.getTahunTerbit());
        buku.setIsbn(dto.getIsbn());
        buku.setDeskripsi(dto.getDeskripsi());
        buku.setJumlahHalaman(dto.getJumlahHalaman());
        buku.setKategori(dto.getKategori());
        buku.setHarga(dto.getHarga());
        buku.setStok(dto.getStok());
        return buku;
    }
}