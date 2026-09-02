package com.scrum.auth.dto;

import java.time.LocalDateTime;

public class ErrorResponse {
    private int code;
    private String msg;
    private LocalDateTime ts;

    public ErrorResponse() {}

    public ErrorResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.ts = LocalDateTime.now();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LocalDateTime getTs() {
        return ts;
    }

    public void setTs(LocalDateTime ts) {
        this.ts = ts;
    }
}
