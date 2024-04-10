package ca.mcgill.ecse321.SportPlus.dto;

import java.sql.Date;

public class PaymentMethodRequestDto {

    private String cardNumber;
    private Date expDate;
    private String cvc;
    private String cardHolderName;
    private int clientId;


    // Constructors
    public PaymentMethodRequestDto() {
    }

    public PaymentMethodRequestDto(String cardNumber, Date expDate, String cvc, String cardHolderName, int clientId) {
        this.cardNumber = cardNumber;
        this.expDate = expDate;
        this.cvc = cvc;
        this.cardHolderName = cardHolderName;
        this.clientId = clientId;
    }

    // Getter and Setter cardNumber
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    // Getter and setter expiry date
    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    // Getter and setter cvc 
    public String getCvc() {
        return cvc;
    }

    public void setCvC(String cvc) {
        this.cvc = cvc;
    }

    // Getter and setter name
    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    // Getter and setter client 
    public int getClientId() {
        return this.clientId;
    }

    public void setClient(int clientId) {
        this.clientId = clientId;
    }

}
