package com.learnSpringBoot.dreamShops.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class APIResponse {
    private String message;
    private Object data;

    public APIResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
