package ca.mcgill.ecse321.SportPlus.dto;

public class ClientPaymentResponseDto {

    private boolean hasPaymentMethod;

    @SuppressWarnings("unused")
    public ClientPaymentResponseDto() {
    }

    public ClientPaymentResponseDto(boolean hasPaymentMethod) {
        this.hasPaymentMethod = hasPaymentMethod;
    }

    public boolean getHasPaymentMethod() {
        return hasPaymentMethod;
    }

    public void setHasPaymentMethod(boolean hasPaymentMethod) {
        this.hasPaymentMethod = hasPaymentMethod;
    }
    
}
