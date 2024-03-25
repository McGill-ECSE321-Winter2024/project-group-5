package ca.mcgill.ecse321.SportPlus.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.SportPlus.model.PaymentMethod;
import ca.mcgill.ecse321.SportPlus.service.utilities.HelperMethods;

@Service
public class PaymentMethodService {

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Autowired
    ClientRepository clientRepository;

    // -----------Wrappers-----------//

    @Transactional
    public PaymentMethod getPaymentMethod(String cardNumber) {
        // Input validation
        if (cardNumber == null || cardNumber.trim().length() == 0) {
            throw new IllegalArgumentException("Card Number is invalid!");
        }
        // Find payment method by card
        PaymentMethod paymentMethod = paymentMethodRepository.findByCardNumber(cardNumber);

        // Validate if exists
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method does not exist!");
        }
        // Return it
        return paymentMethod;
    }

    @Transactional
    public List<PaymentMethod> getPaymentMethods(Client client) {

        // Input validation
        if (client == null || clientRepository.findByEmail(client.getEmail()) == null) {
            throw new IllegalArgumentException("Client is invalid!");
        }

        // get all th epayment methods of the client
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByClient(client);

        // Validate if exists
        if (paymentMethods.size() == 0) {
            throw new IllegalArgumentException("Payment method does not exist!");
        }

        // return the payment methods
        return paymentMethods;
    }

    @Transactional
    public void deletePaymentMethod(String cardNumber) {

        // Input validation
        if (cardNumber == null || cardNumber.trim().length() == 0) {
            throw new IllegalArgumentException("Card Number is invalid!");
        }
        // Find the paymentmeyhod with the card number
        PaymentMethod paymentMethod = paymentMethodRepository.findByCardNumber(cardNumber);
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method does not exist!");
        }

        // Delete the payment method
        paymentMethodRepository.deleteByCardNumber(cardNumber);
    }

    @Transactional
    public void deletePaymentMethods(Client client) {

        // Input validation
        if (client == null || clientRepository.findByEmail(client.getEmail()) == null) {
            throw new IllegalArgumentException("Client is invalid!");
        }

        // Find the payment methods for that client
        List<PaymentMethod> paymentMethod = paymentMethodRepository.findByClient(client);
        if (paymentMethod.size() == 0) {
            throw new IllegalArgumentException("Payment method does not exist!");
        }

        /// Delete the payment methods
        paymentMethodRepository.deleteByClient(client);
    }

    // ------------EndWrappers----------//

    @Transactional
    public PaymentMethod createPaymentMethod(String cardNumber, Date expDate, String cvc, String cardHolderName,
            Client assignedClient) {

        // Input validation
        if (cardNumber == null || cardNumber.trim().length() == 0) {
            throw new IllegalArgumentException("Card Number is invalid!");
        }
        if (expDate == null || HelperMethods.hasDatePassed(expDate)) {
            throw new IllegalArgumentException("Date is invalid!");
        }
        if (cvc == null || cvc.trim().length() == 0) {
            throw new IllegalArgumentException("CVC is invalid!");
        }
        if (cardHolderName == null || cardHolderName.trim().length() == 0) {
            throw new IllegalArgumentException("Cardholder name cannot be empty!");
        }
        if (assignedClient == null || clientRepository.findByEmail(assignedClient.getEmail()) == null) {
            throw new IllegalArgumentException("Client is invalid!");
        }

        // Create and save a new payment method
        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, expDate, cvc, cardHolderName, 0, assignedClient);
        paymentMethodRepository.save(paymentMethod);
        return paymentMethod;
    }

    @Transactional
    public boolean hasPaymentMethod(Client client) {

        // Input validation
        if (client == null || clientRepository.findByEmail(client.getEmail()) == null) {
            throw new IllegalArgumentException("Client is invalid!");
        }

        // Return true of the size is bigger than 0.
        return paymentMethodRepository.findByClient(client).size() != 0;
    }

}
