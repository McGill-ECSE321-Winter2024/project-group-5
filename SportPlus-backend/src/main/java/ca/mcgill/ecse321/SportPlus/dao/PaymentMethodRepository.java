package ca.mcgill.ecse321.SportPlus.dao;

import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.PaymentMethod;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, String> {
    /**
     * Find payment method by client
     * @param client
     * @return Payment Method
     */
    List<PaymentMethod> findByClient(Client client);

    /**\
     * Delete Payment method with id
     * @param Id
     */
    void deleteByPaymentId(Integer Id);
}
