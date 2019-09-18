package com.tct.smart_lock.domain;

import lombok.Data;

@Data
public class Result<T> {
    private String msg;
    private int code;
    private T data;
}
