package ca.mcgill.ecse321.SportPlus.dto;

import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.PaymentMethod;

public class PaymentMethodResponseDto {

    private int cardId;
    private String cardNumber;
    private String cardHolderName;
    private Client client;

    // Constructors
    @SuppressWarnings("unused")
    public PaymentMethodResponseDto() {
    }

    public PaymentMethodResponseDto(PaymentMethod paymentMethod) {
        this.cardId = paymentMethod.getCardId();
        this.cardNumber = paymentMethod.getCardNumber();
        this.cardHolderName = paymentMethod.getCardHolderName();
        this.client = paymentMethod.getClient();
    }

    // Getter and setter cardID
    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    // Getter and Setter Card number
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }   

    // Getter and setter name
    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    // Getter and setter client 
    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
