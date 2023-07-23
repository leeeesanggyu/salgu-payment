package com.salgu.salgupayment.payment.service;

import com.salgu.salgupayment.payment.dto.PaymentDto;
import com.salgu.salgupayment.payment.exception.PaymentException;
import com.salgu.salgupayment.payment.responsecode.PaymentResponseCodeEnum;
import com.salgu.salgupayment.util.response.ResponseWithData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${service.api}")
    private String API_URL;

    private final RestTemplate restTemplate;

    public Boolean purchase(PaymentDto.Purchase dto) {
        ResponseWithData getProductResult = null;
        try {
            getProductResult = restTemplate.getForObject(
                    "http://" + API_URL + "/product/" + dto.getProductId(),
                    ResponseWithData.class
            );
            log.info("{}/user/verification => {}", API_URL, getProductResult);
        } catch (Exception e) {
            throw new PaymentException(PaymentResponseCodeEnum.PRODUCT_ERROR);
        }

        if (getProductResult.getResponse().getOutput() != 0) {
            throw new PaymentException(PaymentResponseCodeEnum.PRODUCT_ERROR);
        }

        return true;
    }
}
