// utils/validation.js

export const validateBookForm = (formData) => {
  const errors = {};

  // Judul validation
  if (!formData.judul || formData.judul.trim() === "") {
    errors.judul = "Judul buku harus diisi";
  } else if (formData.judul.length < 2) {
    errors.judul = "Judul buku minimal 2 karakter";
  }

  // Penulis validation
  if (!formData.penulis || formData.penulis.trim() === "") {
    errors.penulis = "Penulis harus diisi";
  }

  // Penerbit validation
  if (!formData.penerbit || formData.penerbit.trim() === "") {
    errors.penerbit = "Penerbit harus diisi";
  }

  // Tahun Terbit validation
  if (!formData.tahunTerbit) {
    errors.tahunTerbit = "Tahun terbit harus diisi";
  } else {
    const year = parseInt(formData.tahunTerbit);
    const currentYear = new Date().getFullYear();
    if (year < 1900 || year > currentYear + 1) {
      errors.tahunTerbit = `Tahun terbit harus antara 1900 dan ${
        currentYear + 1
      }`;
    }
  }

  // ISBN validation
  if (!formData.isbn || formData.isbn.trim() === "") {
    errors.isbn = "ISBN harus diisi";
  }

  // Deskripsi validation
  if (!formData.deskripsi || formData.deskripsi.trim() === "") {
    errors.deskripsi = "Deskripsi harus diisi";
  } else if (formData.deskripsi.length < 10) {
    errors.deskripsi = "Deskripsi minimal 10 karakter";
  }

  // Jumlah Halaman validation
  if (!formData.jumlahHalaman) {
    errors.jumlahHalaman = "Jumlah halaman harus diisi";
  } else if (parseInt(formData.jumlahHalaman) < 1) {
    errors.jumlahHalaman = "Jumlah halaman minimal 1";
  }

  // Kategori validation
  if (!formData.kategori || formData.kategori.trim() === "") {
    errors.kategori = "Kategori harus dipilih";
  }

  // Harga validation
  if (!formData.harga) {
    errors.harga = "Harga harus diisi";
  } else if (parseFloat(formData.harga) < 0) {
    errors.harga = "Harga tidak boleh negatif";
  }

  // Stok validation
  if (formData.stok && parseInt(formData.stok) < 0) {
    errors.stok = "Stok tidak boleh negatif";
  }

  return errors;
};

export const formatValidationError = (error) => {
  if (typeof error === "string") {
    return error;
  }

  if (error.message) {
    return error.message;
  }

  if (error.response && error.response.data) {
    if (typeof error.response.data === "string") {
      return error.response.data;
    }
    if (error.response.data.message) {
      return error.response.data.message;
    }
    if (error.response.data.errors) {
      return error.response.data.errors.join(", ");
    }
  }

  return "Terjadi kesalahan yang tidak diketahui. Silakan coba lagi.";
};

export const formatCurrency = (amount) => {
  return new Intl.NumberFormat("id-ID", {
    style: "currency",
    currency: "IDR",
    minimumFractionDigits: 0,
  }).format(amount);
};
