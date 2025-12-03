// package com.bookstore.exception;

// import com.bookstore.dto.CustomResponse;
// import com.bookstore.dto.ResponseUtil;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.http.converter.HttpMessageNotReadableException;
// import org.springframework.web.HttpMediaTypeNotSupportedException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice
// public class GlobalExceptionHandler {

// // FIX: @ExceptionHandler menerima class, bukan array
// @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
// public ResponseEntity<CustomResponse<Void>> handleMediaTypeNotSupported() {
// return ResponseUtil.error(
// HttpStatus.BAD_REQUEST,
// "Media type tidak didukung. Gunakan 'application/json'."
// );
// }

// @ExceptionHandler(HttpMessageNotReadableException.class)
// public ResponseEntity<CustomResponse<Void>> handleMessageNotReadable() {
// return ResponseUtil.error(
// HttpStatus.BAD_REQUEST,
// "Format JSON tidak valid. Periksa data yang dikirim."
// );
// }

// @ExceptionHandler(Exception.class)
// public ResponseEntity<CustomResponse<Void>> handleGenericException(Exception
// ex) {
// ex.printStackTrace();
// return ResponseUtil.error(
// HttpStatus.INTERNAL_SERVER_ERROR,
// "Terjadi kesalahan server: " + ex.getMessage()
// );
// }
// }