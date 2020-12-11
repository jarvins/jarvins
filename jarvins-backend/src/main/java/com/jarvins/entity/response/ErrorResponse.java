package com.jarvins.entity.response;

import com.jarvins.entity.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class  ErrorResponse implements Response, Serializable {

    private int code;
    private String message;

    public static Response error(ErrorEnum err) {
        return new ErrorResponse(err.getCode(), err.getMessage());
    }
}
