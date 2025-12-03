package com.bookstore.dto;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseUtil {

    private ResponseUtil() {
        // Private constructor to prevent instantiation
    }

    // Helper methods for ResponseEntity

    public static <T> ResponseEntity<CustomResponse<T>> createResponse(
            HttpStatus status, String message, T data) {
        return ResponseEntity
                .status(status.value())
                .body(CustomResponse.<T>builder()
                        .status(status.value())
                        .message(message)
                        .data(data)
                        .build());
    }

    public static <T> ResponseEntity<CustomResponse<T>> success(String message, T data) {
        return createResponse(HttpStatus.OK, message, data);
    }

    public static <T> ResponseEntity<CustomResponse<T>> success(T data) {
        return success("success", data);
    }

    public static <T> ResponseEntity<CustomResponse<T>> created(String message, T data) {
        return createResponse(HttpStatus.CREATED, message, data);
    }

    public static <T> ResponseEntity<CustomResponse<T>> created(T data) {
        return created("created", data);
    }

    public static <T> ResponseEntity<CustomResponse<T>> noContent() {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT.value())
                .body(CustomResponse.<T>noContent());
    }

    // Paginated response helpers

    public static <T> ResponseEntity<CustomResponse<T>> paginated(
            String message, T data, Integer totalPage, Long totalAllData) {
        return ResponseEntity
                .ok(CustomResponse.<T>paginated(message, data, totalPage, totalAllData));
    }

    public static <T> ResponseEntity<CustomResponse<List<T>>> paginated(
            Page<T> page, String message) {
        return ResponseEntity
                .ok(CustomResponse.<List<T>>paginated(
                        message,
                        page.getContent(),
                        page.getTotalPages(),
                        page.getTotalElements()));
    }

    public static <T> ResponseEntity<CustomResponse<List<T>>> paginated(Page<T> page) {
        return paginated(page, "success");
    }

    // Error response helpers

    public static <T> ResponseEntity<CustomResponse<T>> error(
            HttpStatus status, String message) {
        return ResponseEntity
                .status(status.value())
                .body(CustomResponse.<T>error(status, message));
    }

    public static <T> ResponseEntity<CustomResponse<T>> error(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static <T> ResponseEntity<CustomResponse<T>> badRequest(String message) {
        return error(HttpStatus.BAD_REQUEST, message);
    }

    public static <T> ResponseEntity<CustomResponse<T>> notFound(String message) {
        return error(HttpStatus.NOT_FOUND, message);
    }

    public static <T> ResponseEntity<CustomResponse<T>> internalServerError(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    // Success response without data
    public static ResponseEntity<CustomResponse<Void>> success(String message) {
        return ResponseEntity
                .ok(CustomResponse.<Void>builder()
                        .status(HttpStatus.OK.value())
                        .message(message)
                        .data(null)
                        .build());
    }
}