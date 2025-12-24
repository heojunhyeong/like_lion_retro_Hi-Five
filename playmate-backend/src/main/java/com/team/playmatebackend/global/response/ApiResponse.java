package com.team.playmatebackend.global.response;

public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;

    // 기본 생성자
    public ApiResponse() {}

    // 전체 생성자
    public ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    // 정적 팩토리 메서드
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    // getter, setter 생략 가능하면 lombok @Getter @Setter 사용 가능
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}