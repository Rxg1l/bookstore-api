import axios from "axios";

const API_BASE_URL = "http://localhost:8083";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000,
});

// Mock data untuk fallback
const getMockBooks = () => {
  try {
    const saved = localStorage.getItem("mockBooks");
    if (saved) {
      return JSON.parse(saved);
    }
  } catch (error) {
    console.warn("Error loading from localStorage:", error);
  }

  return [
    {
      id: 1,
      judul: "Laskar Pelangi",
      penulis: "Andrea Hirata",
      penerbit: "Bentang Pustaka",
      tahunTerbit: 2005,
      isbn: "978-979-1227-78-5",
      deskripsi: "Novel tentang perjuangan sekelompok anak miskin di Belitung",
      jumlahHalaman: 529,
      kategori: "Fiksi",
      harga: 85000,
      stok: 50,
    },
    {
      id: 2,
      judul: "Bumi Manusia",
      penulis: "Pramoedya Ananta Toer",
      penerbit: "Lentera Dipantara",
      tahunTerbit: 1980,
      isbn: "978-979-97312-3-6",
      deskripsi: "Novel sejarah tentang pergerakan nasional Indonesia",
      jumlahHalaman: 535,
      kategori: "Fiksi Sejarah",
      harga: 95000,
      stok: 25,
    },
  ];
};

// Save mock data
const saveMockData = (books) => {
  try {
    localStorage.setItem("mockBooks", JSON.stringify(books));
  } catch (error) {
    console.warn("Error saving to localStorage:", error);
  }
};

export const bookService = {
  // Method untuk pagination
  getAllBukuPaginated: async (page = 0, size = 10, sort = "id,asc") => {
    try {
      console.log(
        `Fetching paginated books: page=${page}, size=${size}, sort=${sort}`
      );

      // Validasi parameter
      const validPage = Math.max(0, parseInt(page) || 0);
      const validSize = Math.max(1, Math.min(100, parseInt(size) || 10));
      const validSort = sort || "id,asc";

      // Build query parameters
      const params = new URLSearchParams({
        page: validPage,
        size: validSize,
        sort: validSort,
      });

      const url = `/api/buku/paginated?${params.toString()}`;
      console.log("Request URL:", url);

      // GUNAKAN VARIABLE BERBEDA untuk menghindari redeclaration
      const apiResponse = await api.get(url);
      console.log("Pagination response:", apiResponse.data);

      // Handle various response formats
      let booksData = [];
      let totalPage = 1;
      let totalAllData = 0;

      // Format 1: CustomResponse format
      if (apiResponse.data && apiResponse.data.data) {
        booksData = apiResponse.data.data;
        totalPage = apiResponse.data.totalPage || 1;
        totalAllData = apiResponse.data.totalAllData || booksData.length;
      }
      // Format 2: Spring Page format
      else if (apiResponse.data && apiResponse.data.content) {
        booksData = apiResponse.data.content;
        totalPage = apiResponse.data.totalPages || 1;
        totalAllData = apiResponse.data.totalElements || booksData.length;
      }
      // Format 3: Direct array
      else if (Array.isArray(apiResponse.data)) {
        booksData = apiResponse.data;
        totalPage = 1;
        totalAllData = apiResponse.data.length;
      }
      // Format 4: Direct object
      else if (apiResponse.data && typeof apiResponse.data === "object") {
        booksData = [apiResponse.data];
        totalPage = 1;
        totalAllData = 1;
      }

      return {
        data: booksData,
        totalPage: totalPage,
        totalAllData: totalAllData,
      };
    } catch (error) {
      console.error("Error fetching paginated books:", error);

      // Fallback to mock data with pagination
      const allBooks = getMockBooks();
      const start = page * size;
      const end = start + size;
      const paginatedBooks = allBooks.slice(start, end);

      return {
        data: paginatedBooks,
        totalPage: Math.ceil(allBooks.length / size),
        totalAllData: allBooks.length,
      };
    }
  },

  // Method tanpa pagination (untuk backup)
  getAllBuku: async () => {
    try {
      // GUNAKAN VARIABLE BERBEDA
      const apiResponse = await api.get("/api/buku");
      console.log("Non-paginated response:", apiResponse.data);

      let booksData = apiResponse.data;

      // Handle CustomResponse format
      if (booksData && booksData.data) {
        booksData = booksData.data;
      }

      if (typeof booksData === "object" && !Array.isArray(booksData)) {
        booksData = [booksData];
      }

      return Array.isArray(booksData) ? booksData : [];
    } catch (error) {
      console.warn("Backend error, menggunakan mock data:", error.message);
      return getMockBooks();
    }
  },

  getBookById: async (id) => {
    try {
      // GUNAKAN VARIABLE BERBEDA
      const apiResponse = await api.get(`/api/buku/${id}`);
      return apiResponse.data;
    } catch (error) {
      console.warn("Backend error, mencari di mock data");
      const mockBooks = getMockBooks();
      return mockBooks.find((book) => book.id === id) || mockBooks[0];
    }
  },

  createBook: async (bookData) => {
    try {
      // GUNAKAN VARIABLE BERBEDA
      const apiResponse = await api.post("/api/buku", bookData);

      // Simpan ke mock data untuk consistency
      const mockBooks = getMockBooks();
      const newBook = { ...apiResponse.data, id: Date.now() };
      mockBooks.push(newBook);
      saveMockData(mockBooks);

      return apiResponse.data;
    } catch (error) {
      console.warn("Backend error, menambahkan ke mock data");

      const mockBooks = getMockBooks();
      const newBook = { ...bookData, id: Date.now() };
      mockBooks.push(newBook);
      saveMockData(mockBooks);

      return newBook;
    }
  },

  updateBook: async (id, bookData) => {
    try {
      // GUNAKAN VARIABLE BERBEDA
      const apiResponse = await api.put(`/api/buku/${id}`, bookData);

      // Update mock data
      const mockBooks = getMockBooks();
      const index = mockBooks.findIndex((book) => book.id === id);
      if (index !== -1) {
        mockBooks[index] = { ...bookData, id };
        saveMockData(mockBooks);
      }

      return apiResponse.data;
    } catch (error) {
      console.warn("Backend error, update di mock data");

      const mockBooks = getMockBooks();
      const index = mockBooks.findIndex((book) => book.id === id);
      if (index !== -1) {
        mockBooks[index] = { ...bookData, id };
        saveMockData(mockBooks);
        return mockBooks[index];
      }

      return bookData;
    }
  },

  deleteBook: async (id) => {
    try {
      await api.delete(`/api/buku/${id}`);

      // Hapus dari mock data
      const mockBooks = getMockBooks();
      const newBooks = mockBooks.filter((book) => book.id !== id);
      saveMockData(newBooks);

      return {
        success: true,
        message: "Buku berhasil dihapus",
        deletedId: id,
      };
    } catch (error) {
      console.warn("Backend error, hapus dari mock data");

      const mockBooks = getMockBooks();
      const newBooks = mockBooks.filter((book) => book.id !== id);
      saveMockData(newBooks);

      return {
        success: true,
        message: "Buku dihapus dari data lokal",
        deletedId: id,
      };
    }
  },

  searchBuku: async (keyword) => {
    try {
      // GUNAKAN VARIABLE BERBEDA
      const apiResponse = await api.get(
        `/api/buku/search?keyword=${encodeURIComponent(keyword)}`
      );
      return apiResponse.data;
    } catch (error) {
      console.warn("Backend error, search di mock data");
      const mockBooks = getMockBooks();
      return mockBooks.filter(
        (book) =>
          book.judul?.toLowerCase().includes(keyword.toLowerCase()) ||
          book.penulis?.toLowerCase().includes(keyword.toLowerCase()) ||
          book.kategori?.toLowerCase().includes(keyword.toLowerCase())
      );
    }
  },
};
