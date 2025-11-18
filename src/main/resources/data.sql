-- Insert sample data buku
INSERT INTO buku (judul, penulis, penerbit, tahun_terbit, isbn, deskripsi, jumlah_halaman, kategori, harga, stok, created_at, updated_at) VALUES
('Laskar Pelangi', 'Andrea Hirata', 'Bentang Pustaka', 2005, '978-979-1227-78-5', 'Novel tentang perjuangan sekelompok anak miskin di Belitung', 529, 'Fiksi', 85000.00, 50, NOW(), NOW()),
('Bumi Manusia', 'Pramoedya Ananta Toer', 'Hasta Mitra', 1980, '978-979-9731-23-7', 'Novel sejarah tentang masa kolonial Belanda', 535, 'Fiksi Sejarah', 95000.00, 30, NOW(), NOW()),
('Filosofi Teras', 'Henry Manampiring', 'Kompas', 2018, '978-602-412-518-9', 'Buku tentang filosofi stoisisme untuk kehidupan modern', 346, 'Filsafat', 99000.00, 100, NOW(), NOW()),
('Dilan 1990', 'Pidi Baiq', 'Pastel Books', 2014, '978-602-7888-69-7', 'Kisah cinta masa SMA di Bandung tahun 1990', 332, 'Romantis', 65000.00, 75, NOW(), NOW()),
('Negeri 5 Menara', 'Ahmad Fuadi', 'Gramedia Pustaka Utama', 2009, '978-979-22-4861-7', 'Kisah inspiratif tentang pelajar di pesantren', 423, 'Inspiratif', 88000.00, 40, NOW(), NOW());