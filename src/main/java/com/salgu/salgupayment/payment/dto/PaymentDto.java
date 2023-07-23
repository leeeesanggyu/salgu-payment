package com.salgu.salgupayment.payment.dto;

import lombok.Data;

public class PaymentDto {

    @Data
    public static class Purchase {
        private String productId;
        private String userId;
    }
}
