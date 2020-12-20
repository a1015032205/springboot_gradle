package com.springboot.md.exception;

/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2020/9/27 下午2:39
 * @Description 自定义异常
 */
public class ApiException extends RuntimeException {

    public static ApiException create(int code) {
        throw new ApiException(code);
    }

    public static ApiException create(String message) {
        throw new ApiException(message);
    }

    public static ApiException create(int code, String message) {
        throw new ApiException(code, message);
    }

    public static ApiException create(int code, String message, Exception exception) {
        throw new ApiException(code, message, exception);
    }

    public static ApiException create(int code, String message, Object data) {
        throw new ApiException(code, message, data);
    }

    private int code = 400;

    private Object data;

    private ApiException(int code) {
        this.code = code;
    }

    private ApiException(String message) {
        super(message);
    }

    private ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    private ApiException(String message, Exception ex) {
        super(message, ex);
    }

    private ApiException(int code, String message, Exception ex) {
        super(message, ex);
        this.code = code;
    }

    private ApiException(int code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
