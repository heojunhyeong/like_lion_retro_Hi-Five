package com.team.playmatebackend.global.response;

public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
}