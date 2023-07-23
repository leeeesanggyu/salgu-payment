package com.salgu.salgupayment.payment.controller;

import com.salgu.salgupayment.payment.dto.PaymentDto;
import com.salgu.salgupayment.payment.service.PaymentService;
import com.salgu.salgupayment.util.response.ResponseWithData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/purchase")
    public ResponseWithData purchase(
            @RequestBody PaymentDto.Purchase req
    ) {
        paymentService.purchase(req);
        return ResponseWithData.success("결제 완료");
    }
}
