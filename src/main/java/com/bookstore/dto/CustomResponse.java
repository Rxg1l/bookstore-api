package com.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse<T> {

    private Integer status;
    private String message;
    private T data;

    // Optional fields for pagination
    private Integer totalPage;
    private Long totalAllData;

    // Static factory methods for success responses

    public static <T> CustomResponse<T> success(String message, T data) {
        return CustomResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> CustomResponse<T> success(T data) {
        return success("success", data);
    }

    public static <T> CustomResponse<T> created(String message, T data) {
        return CustomResponse.<T>builder()
                .status(HttpStatus.CREATED.value())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> CustomResponse<T> created(T data) {
        return created("created", data);
    }

    public static <T> CustomResponse<T> noContent() {
        return CustomResponse.<T>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("no content")
                .data(null)
                .build();
    }

    // Static factory methods for paginated responses

    public static <T> CustomResponse<T> paginated(String message, T data,
            Integer totalPage, Long totalAllData) {
        return CustomResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .totalPage(totalPage)
                .totalAllData(totalAllData)
                .build();
    }

    public static <T> CustomResponse<T> paginated(T data, Integer totalPage, Long totalAllData) {
        return paginated("success", data, totalPage, totalAllData);
    }

    // Static factory methods for error responses

    public static <T> CustomResponse<T> error(Integer status, String message) {
        return CustomResponse.<T>builder()
                .status(status)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> CustomResponse<T> error(HttpStatus status, String message) {
        return error(status.value(), message);
    }

    public static <T> CustomResponse<T> error(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static <T> CustomResponse<T> badRequest(String message) {
        return error(HttpStatus.BAD_REQUEST, message);
    }

    public static <T> CustomResponse<T> notFound(String message) {
        return error(HttpStatus.NOT_FOUND, message);
    }

    public static <T> CustomResponse<T> internalServerError(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    // Helper method to check if response is successful
    public boolean isSuccess() {
        return status != null && status >= 200 && status < 300;
    }
}