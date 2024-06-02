package com.group.commitapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {
    private T data;
    private String message;
    private int status;

    public Result(T data) {
        this.data = data;
        this.message = "success";
        this.status = 200; // OK
    }
    public Result() {
        this.message = "success";
        this.status = 200; // OK
    }
}