package ca.mcgill.ecse321.SportPlus.dto;

public class ClientPaymentResponseDto {

    private boolean hasPaymentMethod;

    @SuppressWarnings("unused")
    public ClientPaymentResponseDto() {
    }

    // Constructor
    public ClientPaymentResponseDto(boolean hasPaymentMethod) {
        this.hasPaymentMethod = hasPaymentMethod;
    }

    // Getter
    public boolean getHasPaymentMethod() {
        return hasPaymentMethod;
    }

    // Setter
    public void setHasPaymentMethod(boolean hasPaymentMethod) {
        this.hasPaymentMethod = hasPaymentMethod;
    }

}
