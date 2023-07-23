package com.salgu.salgupayment.payment.responsecode;

import com.salgu.salgupayment.util.response.ResponseCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentResponseCodeEnum implements ResponseCode {
    PRODUCT_ERROR(-1, "상품 가져오던 중 오류"),
    ;

    private final int code;
    private final String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
