package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import ca.mcgill.ecse321.SportPlus.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.PaymentMethod;

@SpringBootTest
public class PaymentMethodRepositoryTests {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        paymentMethodRepository.deleteAll();
        clientRepository.deleteAll();

    }

    // findByCardNumber

    @Test
    @Transactional
    public void testFindByCardNumber() {

        // Create a client
        Client client = new Client("test@email.com", "John", "123321", "Doe", 0);
        clientRepository.save(client);
        // Given a PaymentMethod entity with a specific card number
        String cardNumber = "1234567890123456";
        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, null, "123", "John Doe", 123, client);
        // Use the repository to save the PaymentMethod
        paymentMethodRepository.save(paymentMethod);

        // When findByCardNumber is executed
        PaymentMethod foundPaymentMethod = paymentMethodRepository.findByCardNumber(cardNumber);

        // Then the found PaymentMethod should match the saved one
        assertThat(foundPaymentMethod).isNotNull();
        assertThat(foundPaymentMethod.getCardNumber()).isEqualTo(cardNumber);
        assertEquals(cardNumber, foundPaymentMethod.getCardNumber());
        assertEquals("123", foundPaymentMethod.getCvc());
        assertEquals("John Doe", foundPaymentMethod.getCardHolderName());
        assertEquals(client, foundPaymentMethod.getClient());
        assertEquals(123, foundPaymentMethod.getCardId());

    }

}
