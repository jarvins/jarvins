package com.jarvins.entity.response;

import com.jarvins.entity.Response;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class SuccessResponse<T> implements Response, Serializable {

    private int code;
    private T data;

    public static <T> Response success(T result){
        return SuccessResponse.<T>builder().code(200).data(result).build();
    }
}
