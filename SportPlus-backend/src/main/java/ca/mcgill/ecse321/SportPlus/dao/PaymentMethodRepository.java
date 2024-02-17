package ca.mcgill.ecse321.SportPlus.dao;

import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.PaymentMethod;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Integer> {

    /**
     * Find PpaymentMethods by card number
     * 
     * @param cardNumber
     * @return PaymentMethod
     */
    PaymentMethod findByCardNumber(String cardNumber);

    /**
     * Find all PpaymentMethods for a client
     * 
     * @param Client
     * @return List<PaymentMethod>
     */

    List<PaymentMethod> findByClient(Client client);


}