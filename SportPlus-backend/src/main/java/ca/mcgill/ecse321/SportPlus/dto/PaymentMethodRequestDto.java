package ca.mcgill.ecse321.SportPlus.dto;

import java.sql.Date;
import ca.mcgill.ecse321.SportPlus.model.Client;

public class PaymentMethodRequestDto {

    private String cardNumber;
    private Date expDate;
    private String cvc;
    private String cardHolderName;
    private Client client;

    public PaymentMethodRequestDto() {
    }

    public PaymentMethodRequestDto(String cardNumber, Date expDate, String cvc, String cardHolderName, Client client) {
        this.cardNumber = cardNumber;
        this.expDate = expDate;
        this.cvc = cvc;
        this.cardHolderName = cardHolderName;
        this.client = client;
    }    

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvC(String cvc) {
        this.cvc = cvc;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
