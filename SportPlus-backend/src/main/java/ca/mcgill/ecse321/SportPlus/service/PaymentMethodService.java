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
        if (cardNumber == null || cardNumber.trim().length() == 0) {
            throw new IllegalArgumentException("Card Number is invalid!");
        }
        PaymentMethod paymentMethod = paymentMethodRepository.findByCardNumber(cardNumber);
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method does not exist!");
        }
        return paymentMethod;
    }

    @Transactional
    public List<PaymentMethod> getPaymentMethods(Client client) {
        if (client == null || clientRepository.findByEmail(client.getEmail()) == null) {
            throw new IllegalArgumentException("Client is invalid!");
        }
        List<PaymentMethod> paymentMethod = paymentMethodRepository.findByClient(client);
        if (paymentMethod.size() == 0) {
            throw new IllegalArgumentException("Payment method does not exist!");
        }
        return paymentMethod;
    }

    @Transactional
    public void deletePaymentMethod(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().length() == 0) {
            throw new IllegalArgumentException("Card Number is invalid!");
        }
        PaymentMethod paymentMethod = paymentMethodRepository.findByCardNumber(cardNumber);
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method does not exist!");
        }
        paymentMethodRepository.deleteByCardNumber(cardNumber);
    }

    @Transactional
    public void deletePaymentMethods(Client client) {
        if (client == null || clientRepository.findByEmail(client.getEmail()) == null) {
            throw new IllegalArgumentException("Client is invalid!");
        }
        List<PaymentMethod> paymentMethod = paymentMethodRepository.findByClient(client);
        if (paymentMethod.size() == 0) {
            throw new IllegalArgumentException("Payment method does not exist!");
        }
        paymentMethodRepository.deleteByClient(client);
    }

    // ------------EndWrappers----------//

    @Transactional
    public PaymentMethod createPaymentMethod(String cardNumber, Date expDate, String cvc, String cardHolderName,
            Client assignedClient) {
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
        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, expDate, cvc, cardHolderName, 0, assignedClient);
        paymentMethodRepository.save(paymentMethod);
        return paymentMethod;
    }

    @Transactional
    public boolean hasPaymentMethod(Client client) {
        if (client == null || clientRepository.findByEmail(client.getEmail()) == null) {
            throw new IllegalArgumentException("Client is invalid!");
        }
        return paymentMethodRepository.findByClient(client).size() != 0;
    }

}
