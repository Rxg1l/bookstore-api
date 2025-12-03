import React, { useState, useEffect } from "react";
import {
  Container,
  Typography,
  Grid,
  Card,
  CardContent,
  CardActions,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  TextField,
  Box,
  AppBar,
  Toolbar,
  Chip,
  CircularProgress,
  Alert,
  Snackbar,
  Pagination,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
} from "@mui/material";
import { Delete, Edit, Search, Add, Inventory } from "@mui/icons-material";
import { useNavigate } from "react-router-dom";
import { bookService } from "../services/bookService";
import { formatCurrency } from "../utils/validation";

const BookList = () => {
  // State declarations
  const [books, setBooks] = useState([]);
  const [filteredBooks, setFilteredBooks] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [selectedBooks, setSelectedBooks] = useState([]);
  const [bulkDeleteDialog, setBulkDeleteDialog] = useState(false);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: "",
    severity: "success",
  });
  const [deleteDialog, setDeleteDialog] = useState({
    open: false,
    bookId: null,
    bookTitle: "",
  });

  // Pagination state
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [sortField, setSortField] = useState("id");
  const [sortDirection, setSortDirection] = useState("asc");

  const navigate = useNavigate();

  // Load books dengan pagination
  useEffect(() => {
    loadBooks();
  }, [page, size, sortField, sortDirection]);

  // Filter untuk search
  useEffect(() => {
    if (searchTerm) {
      const filtered = books.filter(
        (book) =>
          book.judul?.toLowerCase().includes(searchTerm.toLowerCase()) ||
          book.penulis?.toLowerCase().includes(searchTerm.toLowerCase()) ||
          book.kategori?.toLowerCase().includes(searchTerm.toLowerCase()) ||
          book.penerbit?.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setFilteredBooks(filtered);
    } else {
      setFilteredBooks(books);
    }
  }, [searchTerm, books]);

  const loadBooks = async () => {
    try {
      setLoading(true);
      setError("");

      console.log(
        `Loading books with pagination: page=${page}, size=${size}, sort=${sortField},${sortDirection}`
      );

      // Gunakan method getAllBukuPaginated
      const response = await bookService.getAllBukuPaginated(
        page,
        size,
        `${sortField},${sortDirection}`
      );

      console.log("Pagination response:", response);

      // Extract data dari response
      const booksData = response.data || [];
      const totalPagesData = response.totalPage || 1;
      const totalElementsData = response.totalAllData || 0;

      setBooks(booksData);
      setFilteredBooks(booksData);
      setTotalPages(totalPagesData);
      setTotalElements(totalElementsData);

      console.log(
        `Loaded ${booksData.length} books, total ${totalElementsData} books, ${totalPagesData} pages`
      );
    } catch (error) {
      console.error("Error loading books:", error);
      setError("Gagal memuat data buku: " + error.message);
      setBooks([]);
      setFilteredBooks([]);
      setTotalPages(0);
      setTotalElements(0);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
  };

  // PERBAIKAN: Pastikan semua fungsi punya parameter yang benar
  const handleDelete = async () => {
    const bookId = deleteDialog.bookId;
    const bookTitle = deleteDialog.bookTitle;

    try {
      // Optimistic update
      const updatedBooks = books.filter((book) => book.id !== bookId);
      setBooks(updatedBooks);
      setFilteredBooks(updatedBooks);

      // Tutup dialog
      setDeleteDialog({ open: false, bookId: null, bookTitle: "" });

      // Hapus dari server
      const result = await bookService.deleteBook(bookId);

      if (result.success) {
        showSnackbar(`Buku "${bookTitle}" berhasil dihapus!`, "success");
      } else {
        showSnackbar(result.message || "Buku gagal dihapus", "warning");
      }

      // Refresh data
      loadBooks();
    } catch (error) {
      console.error("Error in handleDelete:", error);
      showSnackbar("Terjadi kesalahan: " + error.message, "error");
      loadBooks();
    }
  };

  const handleBulkDelete = async () => {
    try {
      // Optimistic update
      const updatedBooks = books.filter(
        (book) => !selectedBooks.includes(book.id)
      );
      setBooks(updatedBooks);
      setFilteredBooks(updatedBooks);

      // Delete from server
      for (const bookId of selectedBooks) {
        await bookService.deleteBook(bookId);
      }

      setSelectedBooks([]);
      setBulkDeleteDialog(false);
      showSnackbar(`${selectedBooks.length} buku berhasil dihapus!`, "success");

      // Refresh data
      loadBooks();
    } catch (error) {
      console.error("Error in bulk delete:", error);
      showSnackbar("Gagal menghapus buku: " + error.message, "error");
      loadBooks();
    }
  };

  // PERBAIKAN: Fungsi dengan parameter yang jelas
  const openDeleteDialog = (bookId, bookTitle) => {
    setDeleteDialog({ open: true, bookId, bookTitle });
  };

  const closeDeleteDialog = () => {
    setDeleteDialog({ open: false, bookId: null, bookTitle: "" });
  };

  // PERBAIKAN: Parameter bookId
  const handleSelectBook = (bookId) => {
    setSelectedBooks((prev) =>
      prev.includes(bookId)
        ? prev.filter((id) => id !== bookId) // id di sini adalah parameter callback
        : [...prev, bookId]
    );
  };

  // PERBAIKAN: Parameter bookId
  const handleEdit = (bookId) => {
    navigate(`/edit/${bookId}`);
  };

  const handleAddBook = () => {
    navigate("/add");
  };

  const showSnackbar = (message, severity = "success") => {
    setSnackbar({ open: true, message, severity });
  };

  const closeSnackbar = () => {
    setSnackbar({ ...snackbar, open: false });
  };

  // Pagination handlers
  const handlePageChange = (event, value) => {
    setPage(value - 1);
  };

  const handleSizeChange = (event) => {
    setSize(event.target.value);
    setPage(0);
  };

  const handleSortChange = (field) => {
    if (sortField === field) {
      setSortDirection(sortDirection === "asc" ? "desc" : "asc");
    } else {
      setSortField(field);
      setSortDirection("asc");
    }
    setPage(0);
  };

  const getStockColor = (stok) => {
    if (stok === 0) return "error";
    if (stok < 10) return "warning";
    return "success";
  };

  const getStockText = (stok) => {
    if (stok === 0) return "Stok Habis";
    if (stok < 10) return "Stok Menipis";
    return "Tersedia";
  };

  if (loading && books.length === 0) {
    return (
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="80vh"
      >
        <CircularProgress />
        <Typography variant="h6" sx={{ ml: 2 }}>
          Memuat data buku...
        </Typography>
      </Box>
    );
  }

  const booksToRender = Array.isArray(filteredBooks) ? filteredBooks : [];

  return (
    <>
      <AppBar position="static" elevation={2}>
        <Toolbar>
          <Inventory sx={{ mr: 2 }} />
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Manajemen Toko Buku
          </Typography>
          <Button
            color="inherit"
            startIcon={<Add />}
            onClick={handleAddBook}
            variant="outlined"
            sx={{ borderColor: "white" }}
          >
            Tambah Buku
          </Button>
        </Toolbar>
      </AppBar>

      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        {error && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {error}
          </Alert>
        )}

        {/* Search dan Pagination Controls */}
        <Box
          sx={{
            mb: 4,
            p: 3,
            bgcolor: "background.paper",
            borderRadius: 2,
            boxShadow: 1,
          }}
        >
          <TextField
            fullWidth
            variant="outlined"
            placeholder="Cari berdasarkan judul, penulis, kategori, atau penerbit..."
            value={searchTerm}
            onChange={handleSearch}
            InputProps={{
              startAdornment: (
                <Search sx={{ mr: 1, color: "text.secondary" }} />
              ),
            }}
            sx={{ mb: 2 }}
          />

          <Box
            display="flex"
            justifyContent="space-between"
            alignItems="center"
            sx={{ mb: 2 }}
          >
            <Box display="flex" alignItems="center" gap={2}>
              <FormControl size="small" sx={{ minWidth: 120 }}>
                <InputLabel>Items per page</InputLabel>
                <Select
                  value={size}
                  label="Items per page"
                  onChange={handleSizeChange}
                >
                  <MenuItem value={5}>5</MenuItem>
                  <MenuItem value={10}>10</MenuItem>
                  <MenuItem value={20}>20</MenuItem>
                  <MenuItem value={50}>50</MenuItem>
                </Select>
              </FormControl>

              <Box display="flex" gap={1}>
                <Button
                  size="small"
                  variant={sortField === "judul" ? "contained" : "outlined"}
                  onClick={() => handleSortChange("judul")}
                >
                  Judul{" "}
                  {sortField === "judul" &&
                    (sortDirection === "asc" ? "↑" : "↓")}
                </Button>
                <Button
                  size="small"
                  variant={sortField === "harga" ? "contained" : "outlined"}
                  onClick={() => handleSortChange("harga")}
                >
                  Harga{" "}
                  {sortField === "harga" &&
                    (sortDirection === "asc" ? "↑" : "↓")}
                </Button>
              </Box>
            </Box>

            <Typography variant="body2" color="text.secondary">
              Halaman {page + 1} dari {totalPages} - Total {totalElements} buku
            </Typography>
          </Box>

          {totalPages > 1 && (
            <Box display="flex" justifyContent="center">
              <Pagination
                count={totalPages}
                page={page + 1}
                onChange={handlePageChange}
                color="primary"
              />
            </Box>
          )}
        </Box>

        {/* Books Grid */}
        {booksToRender.length === 0 ? (
          <Box textAlign="center" py={8}>
            <Typography variant="h6" color="text.secondary" gutterBottom>
              {books.length === 0
                ? "Belum ada data buku"
                : "Buku tidak ditemukan"}
            </Typography>
            {books.length === 0 && (
              <Button
                variant="contained"
                startIcon={<Add />}
                onClick={handleAddBook}
              >
                Tambah Buku Pertama
              </Button>
            )}
          </Box>
        ) : (
          <>
            <Grid container spacing={3}>
              {booksToRender.map((book) => (
                <Grid item key={book.id} xs={12} sm={6} md={4}>
                  <Card
                    sx={{
                      height: "100%",
                      display: "flex",
                      flexDirection: "column",
                      "&:hover": { boxShadow: 4 },
                    }}
                  >
                    <CardContent sx={{ flexGrow: 1 }}>
                      <Typography
                        gutterBottom
                        variant="h6"
                        component="h2"
                        sx={{
                          fontWeight: "bold",
                          height: "64px",
                          overflow: "hidden",
                        }}
                      >
                        {book.judul || "Judul tidak tersedia"}
                      </Typography>
                      <Typography
                        color="textSecondary"
                        gutterBottom
                        sx={{ fontStyle: "italic" }}
                      >
                        oleh {book.penulis || "Penulis tidak tersedia"}
                      </Typography>
                      <Typography
                        variant="body2"
                        color="textSecondary"
                        paragraph
                        sx={{ height: "60px", overflow: "hidden" }}
                      >
                        {book.deskripsi || "Deskripsi tidak tersedia"}
                      </Typography>
                      <Box sx={{ mb: 2 }}>
                        <Typography variant="body2" gutterBottom>
                          <strong>Penerbit:</strong>{" "}
                          {book.penerbit || "Tidak tersedia"}
                        </Typography>
                        <Typography variant="body2" gutterBottom>
                          <strong>Tahun:</strong>{" "}
                          {book.tahunTerbit || "Tidak tersedia"}
                        </Typography>
                        <Typography variant="body2" gutterBottom>
                          <strong>Kategori:</strong>{" "}
                          {book.kategori || "Tidak tersedia"}
                        </Typography>
                        <Typography variant="body2" gutterBottom>
                          <strong>Jumlah Halaman:</strong>{" "}
                          {book.jumlahHalaman || "Tidak tersedia"}
                        </Typography>
                      </Box>
                      <Typography
                        variant="h6"
                        color="primary"
                        sx={{ mb: 1, fontWeight: "bold" }}
                      >
                        {formatCurrency(book.harga || 0)}
                      </Typography>
                      <Chip
                        label={`${getStockText(book.stok || 0)} (${
                          book.stok || 0
                        })`}
                        color={getStockColor(book.stok || 0)}
                        size="small"
                      />
                    </CardContent>
                    <CardActions
                      sx={{ pt: 0, justifyContent: "space-between" }}
                    >
                      {/* PERBAIKAN: Gunakan book.id sebagai parameter */}
                      <Button
                        size="small"
                        startIcon={<Edit />}
                        onClick={() => handleEdit(book.id)}
                        variant="outlined"
                      >
                        Edit
                      </Button>
                      <Button
                        size="small"
                        color="error"
                        startIcon={<Delete />}
                        onClick={() => openDeleteDialog(book.id, book.judul)}
                        variant="outlined"
                      >
                        Hapus
                      </Button>
                    </CardActions>
                  </Card>
                </Grid>
              ))}
            </Grid>

            {totalPages > 1 && (
              <Box display="flex" justifyContent="center" sx={{ mt: 4 }}>
                <Pagination
                  count={totalPages}
                  page={page + 1}
                  onChange={handlePageChange}
                  color="primary"
                  size="large"
                />
              </Box>
            )}
          </>
        )}

        {/* Dialogs dan Snackbar */}
        <Dialog
          open={deleteDialog.open}
          onClose={closeDeleteDialog}
          maxWidth="sm"
          fullWidth
        >
          <DialogTitle>Konfirmasi Hapus Buku</DialogTitle>
          <DialogContent>
            <DialogContentText>
              Apakah Anda yakin ingin menghapus buku{" "}
              <strong>"{deleteDialog.bookTitle}"</strong>?
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={closeDeleteDialog}>Batal</Button>
            <Button
              onClick={handleDelete}
              color="error"
              variant="contained"
              startIcon={<Delete />}
            >
              Hapus
            </Button>
          </DialogActions>
        </Dialog>

        <Snackbar
          open={snackbar.open}
          autoHideDuration={6000}
          onClose={closeSnackbar}
        >
          <Alert onClose={closeSnackbar} severity={snackbar.severity}>
            {snackbar.message}
          </Alert>
        </Snackbar>
      </Container>
    </>
  );
};

export default BookList;
