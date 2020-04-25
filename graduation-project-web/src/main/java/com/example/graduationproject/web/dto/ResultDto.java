package com.example.graduationproject.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Zzwen
 * @date 2020/4/17 15:21
 */
@Data
@AllArgsConstructor
public class ResultDto<T> {
    private int code;
    private String message;
    private T data;
}
