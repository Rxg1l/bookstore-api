package com.bookstore.service;

import com.bookstore.dto.BukuDTO;
import com.bookstore.dto.CreateBukuRequest;
import com.bookstore.model.Buku;
import com.bookstore.repository.BukuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BukuService {
    
    private final BukuRepository bukuRepository;
    
    // Convert Entity to DTO
    private BukuDTO convertToDTO(Buku buku) {
        return new BukuDTO(
            buku.getId(),
            buku.getJudul(),
            buku.getPenulis(),
            buku.getPenerbit(),
            buku.getTahunTerbit(),
            buku.getIsbn(),
            buku.getDeskripsi(),
            buku.getJumlahHalaman(),
            buku.getKategori(),
            buku.getHarga(),
            buku.getStok()
        );
    }
    
    // Convert DTO to Entity
    private Buku convertToEntity(CreateBukuRequest request) {
        Buku buku = new Buku();
        buku.setJudul(request.getJudul());
        buku.setPenulis(request.getPenulis());
        buku.setPenerbit(request.getPenerbit());
        buku.setTahunTerbit(request.getTahunTerbit());
        buku.setIsbn(request.getIsbn());
        buku.setDeskripsi(request.getDeskripsi());
        buku.setJumlahHalaman(request.getJumlahHalaman());
        buku.setKategori(request.getKategori());
        buku.setHarga(request.getHarga());
        buku.setStok(request.getStok());
        return buku;
    }
    
    public List<BukuDTO> getAllBuku() {
        return bukuRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public BukuDTO getBukuById(Long id) {
        Buku buku = bukuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Buku tidak ditemukan dengan id: " + id));
        return convertToDTO(buku);
    }
    
    public BukuDTO createBuku(CreateBukuRequest request) {
        Buku buku = convertToEntity(request);
        Buku savedBuku = bukuRepository.save(buku);
        return convertToDTO(savedBuku);
    }
    
    public BukuDTO updateBuku(Long id, CreateBukuRequest request) {
        Buku existingBuku = bukuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Buku tidak ditemukan dengan id: " + id));
        
        existingBuku.setJudul(request.getJudul());
        existingBuku.setPenulis(request.getPenulis());
        existingBuku.setPenerbit(request.getPenerbit());
        existingBuku.setTahunTerbit(request.getTahunTerbit());
        existingBuku.setIsbn(request.getIsbn());
        existingBuku.setDeskripsi(request.getDeskripsi());
        existingBuku.setJumlahHalaman(request.getJumlahHalaman());
        existingBuku.setKategori(request.getKategori());
        existingBuku.setHarga(request.getHarga());
        existingBuku.setStok(request.getStok());
        
        Buku updatedBuku = bukuRepository.save(existingBuku);
        return convertToDTO(updatedBuku);
    }
    
    public void deleteBuku(Long id) {
        if (!bukuRepository.existsById(id)) {
            throw new RuntimeException("Buku tidak ditemukan dengan id: " + id);
        }
        bukuRepository.deleteById(id);
    }
    
    public List<BukuDTO> searchByJudul(String judul) {
        return bukuRepository.findByJudulContainingIgnoreCase(judul)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BukuDTO> searchByPenulis(String penulis) {
        return bukuRepository.findByPenulisContainingIgnoreCase(penulis)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BukuDTO> searchByKategori(String kategori) {
        return bukuRepository.findByKategoriContainingIgnoreCase(kategori)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BukuDTO> searchByKeyword(String keyword) {
        return bukuRepository.searchByKeyword(keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BukuDTO> getBukuByTahunTerbit(Integer tahunTerbit) {
        return bukuRepository.findByTahunTerbit(tahunTerbit)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BukuDTO> getBukuTersedia() {
        return bukuRepository.findByStokGreaterThan(0)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}