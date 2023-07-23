package com.salgu.salgupayment.payment.exception;

import com.salgu.salgupayment.util.exception.CustomException;
import com.salgu.salgupayment.util.response.ResponseCode;
import lombok.Getter;

@Getter
public class PaymentException extends CustomException {

	private ResponseCode responseCodeEnum = null;

	public PaymentException(String message) {
		super(message);
	}

	public PaymentException(ResponseCode code) {
		super(code.getMessage());
		this.responseCodeEnum = code;
	}

	public PaymentException(ResponseCode code, String message) {
		super(message);
		this.responseCodeEnum = code;
	}
}