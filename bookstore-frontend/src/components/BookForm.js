import React, { useState, useEffect } from "react";
import {
  Container,
  TextField,
  Button,
  Typography,
  Paper,
  Grid,
  MenuItem,
  Box,
  Alert,
  Snackbar,
} from "@mui/material";
import { useParams, useNavigate } from "react-router-dom";
import { bookService } from "../services/bookService";
import { validateBookForm, formatValidationError } from "../utils/validation";

const BookForm = ({ onSave }) => {
  const { id } = useParams();
  const navigate = useNavigate();
  const bookId = id ? parseInt(id) : undefined;

  const [formData, setFormData] = useState({
    judul: "",
    penulis: "",
    penerbit: "",
    tahunTerbit: new Date().getFullYear(),
    isbn: "",
    deskripsi: "",
    jumlahHalaman: "",
    kategori: "",
    harga: "",
    stok: "",
  });

  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: "",
    severity: "success",
  });

  const categories = [
    "Fiksi",
    "Non-Fiksi",
    "Sains",
    "Teknologi",
    "Sejarah",
    "Biografi",
    "Fantasi",
    "Romance",
    "Misteri",
    "Komik",
    "Pendidikan",
    "Bisnis",
  ];

  useEffect(() => {
    if (bookId) {
      loadBook();
    }
  }, [bookId]);

  const loadBook = async () => {
    try {
      setIsLoading(true);
      const book = await bookService.getBookById(bookId);
      setFormData({
        judul: book.judul || "",
        penulis: book.penulis || "",
        penerbit: book.penerbit || "",
        tahunTerbit: book.tahunTerbit || new Date().getFullYear(),
        isbn: book.isbn || "",
        deskripsi: book.deskripsi || "",
        jumlahHalaman: book.jumlahHalaman || "",
        kategori: book.kategori || "",
        harga: book.harga || "",
        stok: book.stok || "",
      });
    } catch (error) {
      console.error("Error loading book:", error);
      showSnackbar("Error loading book: " + error.message, "error");
    } finally {
      setIsLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));

    // Clear error when user starts typing
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: "" }));
    }
  };

  const showSnackbar = (message, severity = "success") => {
    setSnackbar({ open: true, message, severity });
  };

  const handleCloseSnackbar = () => {
    setSnackbar({ ...snackbar, open: false });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Client-side validation
    const validationErrors = validateBookForm(formData);
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      showSnackbar("Harap perbaiki error pada form sebelum submit", "error");
      return;
    }

    setIsLoading(true);
    try {
      // Prepare data for API
      const apiData = {
        ...formData,
        tahunTerbit: parseInt(formData.tahunTerbit),
        jumlahHalaman: parseInt(formData.jumlahHalaman),
        harga: parseFloat(formData.harga),
        stok: parseInt(formData.stok),
      };

      let result;
      if (bookId) {
        result = await bookService.updateBook(bookId, apiData);
        showSnackbar("Buku berhasil diupdate!", "success");
      } else {
        result = await bookService.createBook(apiData);
        showSnackbar("Buku berhasil dibuat!", "success");
      }

      console.log("Save result:", result);

      if (onSave) {
        onSave();
      }

      // Redirect to book list after short delay
      setTimeout(() => navigate("/"), 1500);
    } catch (error) {
      console.error("Error saving book:", error);

      // Format error message properly
      let errorMessage = "Terjadi kesalahan saat menyimpan buku";
      if (error.message) {
        errorMessage = error.message;
      }

      showSnackbar(errorMessage, "error");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Container maxWidth="md" sx={{ mt: 4 }}>
      <Paper elevation={3} sx={{ p: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          {bookId ? "Edit Buku" : "Tambah Buku Baru"}
        </Typography>

        <Box component="form" onSubmit={handleSubmit} noValidate>
          <Grid container spacing={3}>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                label="Judul Buku"
                name="judul"
                value={formData.judul}
                onChange={handleChange}
                error={!!errors.judul}
                helperText={errors.judul}
                disabled={isLoading}
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                label="Penulis"
                name="penulis"
                value={formData.penulis}
                onChange={handleChange}
                error={!!errors.penulis}
                helperText={errors.penulis}
                disabled={isLoading}
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                label="Penerbit"
                name="penerbit"
                value={formData.penerbit}
                onChange={handleChange}
                error={!!errors.penerbit}
                helperText={errors.penerbit}
                disabled={isLoading}
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                label="Tahun Terbit"
                name="tahunTerbit"
                type="number"
                value={formData.tahunTerbit}
                onChange={handleChange}
                error={!!errors.tahunTerbit}
                helperText={errors.tahunTerbit}
                inputProps={{ min: 1900, max: new Date().getFullYear() + 1 }}
                disabled={isLoading}
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                label="ISBN"
                name="isbn"
                value={formData.isbn}
                onChange={handleChange}
                error={!!errors.isbn}
                helperText={errors.isbn}
                disabled={isLoading}
              />
            </Grid>

            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                multiline
                rows={3}
                label="Deskripsi"
                name="deskripsi"
                value={formData.deskripsi}
                onChange={handleChange}
                error={!!errors.deskripsi}
                helperText={errors.deskripsi}
                disabled={isLoading}
              />
            </Grid>

            <Grid item xs={12} sm={4}>
              <TextField
                required
                fullWidth
                label="Jumlah Halaman"
                name="jumlahHalaman"
                type="number"
                value={formData.jumlahHalaman}
                onChange={handleChange}
                error={!!errors.jumlahHalaman}
                helperText={errors.jumlahHalaman}
                inputProps={{ min: 1 }}
                disabled={isLoading}
              />
            </Grid>

            <Grid item xs={12} sm={4}>
              <TextField
                required
                fullWidth
                select
                label="Kategori"
                name="kategori"
                value={formData.kategori}
                onChange={handleChange}
                error={!!errors.kategori}
                helperText={errors.kategori}
                disabled={isLoading}
              >
                {categories.map((category) => (
                  <MenuItem key={category} value={category}>
                    {category}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>

            <Grid item xs={12} sm={4}>
              <TextField
                required
                fullWidth
                label="Harga (IDR)"
                name="harga"
                type="number"
                value={formData.harga}
                onChange={handleChange}
                error={!!errors.harga}
                helperText={errors.harga}
                inputProps={{ min: 0, step: 1000 }}
                disabled={isLoading}
              />
            </Grid>

            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Stok"
                name="stok"
                type="number"
                value={formData.stok}
                onChange={handleChange}
                error={!!errors.stok}
                helperText={errors.stok}
                inputProps={{ min: 0 }}
                disabled={isLoading}
              />
            </Grid>

            <Grid item xs={12}>
              <Box sx={{ display: "flex", gap: 2, justifyContent: "flex-end" }}>
                <Button
                  type="button"
                  variant="outlined"
                  onClick={() => navigate("/")}
                  disabled={isLoading}
                >
                  Batal
                </Button>
                <Button type="submit" variant="contained" disabled={isLoading}>
                  {isLoading
                    ? "Menyimpan..."
                    : bookId
                    ? "Update Buku"
                    : "Tambah Buku"}
                </Button>
              </Box>
            </Grid>
          </Grid>
        </Box>
      </Paper>

      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
      >
        <Alert
          onClose={handleCloseSnackbar}
          severity={snackbar.severity}
          sx={{ width: "100%" }}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default BookForm;
