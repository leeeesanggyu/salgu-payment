package com.salgu.salgupayment.util.response;

/**
 * author by kang_dae_won
 */
public enum ResponseCodeEnum {
    SUCCESS(0,"success"),
    FAILED(-1,"failed"),
    ;

    private final int code;
    private final String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
