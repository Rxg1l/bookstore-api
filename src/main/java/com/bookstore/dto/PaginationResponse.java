// package com.bookstore.dto;

// import lombok.Data;
// import lombok.EqualsAndHashCode;
// import java.util.List;

// @Data
// @EqualsAndHashCode(callSuper = true)
// public class PaginationResponse<T> extends CustomResponse<List<T>> {
// private int currentPage;
// private int totalPages;
// private long totalItems;
// private int itemsPerPage;
// private boolean hasNext;
// private boolean hasPrevious;

// // Constructor untuk success
// public PaginationResponse(List<T> content, int currentPage, int totalPages,
// long totalItems, int itemsPerPage,
// boolean hasNext, boolean hasPrevious) {
// super(true, "Data berhasil diambil", content, null);
// this.currentPage = currentPage;
// this.totalPages = totalPages;
// this.totalItems = totalItems;
// this.itemsPerPage = itemsPerPage;
// this.hasNext = hasNext;
// this.hasPrevious = hasPrevious;
// }

// // Default constructor
// public PaginationResponse() {
// super();
// }

// // Static helper method untuk success
// public static <T> PaginationResponse<T> of(List<T> content, int currentPage,
// int totalPages,
// long totalItems, int itemsPerPage,
// boolean hasNext, boolean hasPrevious) {
// return new PaginationResponse<>(content, currentPage, totalPages, totalItems,
// itemsPerPage, hasNext, hasPrevious);
// }

// // Method untuk create error response
// public static <T> PaginationResponse<T> createError(String errorMessage) {
// PaginationResponse<T> response = new PaginationResponse<>();
// response.setSuccess(false);
// response.setError(errorMessage);
// response.setData(List.of());
// response.setCurrentPage(0);
// response.setTotalPages(0);
// response.setTotalItems(0);
// response.setItemsPerPage(0);
// response.setHasNext(false);
// response.setHasPrevious(false);
// return response;
// }
// }