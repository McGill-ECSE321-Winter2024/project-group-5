package ca.mcgill.ecse321.SportPlus.dto;

import java.util.List;

public class PaymentMethodListDto {

    List<PaymentMethodResponseDto> paymentMethods;

    // Constructors
    public PaymentMethodListDto() {
    }

    public PaymentMethodListDto(List<PaymentMethodResponseDto> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    // Getter
    public List<PaymentMethodResponseDto> getPaymentMethods() {
        return paymentMethods;
    }

    // Setter
    public void setPaymentMethods(List<PaymentMethodResponseDto> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

}
