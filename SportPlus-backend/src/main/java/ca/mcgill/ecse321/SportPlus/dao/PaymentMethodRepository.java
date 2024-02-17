package ca.mcgill.ecse321.SportPlus.dao;

import ca.mcgill.ecse321.SportPlus.model.PaymentMethod;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Integer> {
    // Find a payment method by card number
    PaymentMethod findByCardNumber(String cardNumber);

    // Additional custom methods can be added here
}