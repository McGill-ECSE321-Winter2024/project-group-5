package ca.mcgill.ecse321.SportPlus.dto;

import java.sql.Date;
import ca.mcgill.ecse321.SportPlus.model.Client;

public class PaymentMethodRequestDto {

    private String cardNumber;
    private Date expDate;
    private String cvc;
    private String cardHolderName;
    private Client client;


    // Constructors
    public PaymentMethodRequestDto() {
    }

    public PaymentMethodRequestDto(String cardNumber, Date expDate, String cvc, String cardHolderName, Client client) {
        this.cardNumber = cardNumber;
        this.expDate = expDate;
        this.cvc = cvc;
        this.cardHolderName = cardHolderName;
        this.client = client;
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
    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
