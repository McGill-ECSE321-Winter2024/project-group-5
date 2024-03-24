package ca.mcgill.ecse321.SportPlus.dto;

import java.util.List;

public class PaymentMethodListDto {

    List<PaymentMethodResponseDto> paymentMethods;

    public PaymentMethodListDto() {
    }

    public PaymentMethodListDto(List<PaymentMethodResponseDto> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public List<PaymentMethodResponseDto> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethodResponseDto> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

}
