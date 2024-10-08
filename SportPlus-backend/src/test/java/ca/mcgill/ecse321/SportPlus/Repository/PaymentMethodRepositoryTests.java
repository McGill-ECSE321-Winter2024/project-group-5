package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.LoginRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.PaymentMethod;

@SpringBootTest
public class PaymentMethodRepositoryTests {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private ClassTypeRepository classTypeRepository;

    @Autowired
    private SpecificClassRepository specificClassRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        loginRepository.deleteAll();
        registrationRepository.deleteAll();
        specificClassRepository.deleteAll();
        classTypeRepository.deleteAll();
        instructorRepository.deleteAll();
        paymentMethodRepository.deleteAll();
        clientRepository.deleteAll();
        ownerRepository.deleteAll();
    }

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
        assertTrue(foundPaymentMethod.getCardId() > 0);
    }

    @Test
    public void testFindByClient() {
        // Create a client
        Client client = new Client("test@email.com", "John", "123321", "Doe", 0);
        clientRepository.save(client);

        // Given two PaymentMethods entity with a specific client with different cardIds
        String cardNumber1 = "1234567890123456";
        String cardNumber2 = "1234567890123453";
        PaymentMethod paymentMethod1 = new PaymentMethod(cardNumber1, null, "123", "John Doe", 123, client);
        PaymentMethod paymentMethod2 = new PaymentMethod(cardNumber2, null, "321", "John Doe", 321, client);

        // Use the repository to save the PaymentMethod
        paymentMethodRepository.save(paymentMethod1);
        paymentMethodRepository.save(paymentMethod2);

        // When findByCardNumber is executed
        List<PaymentMethod> foundPaymentMethods = paymentMethodRepository.findByClient(client);
        assertEquals(2, foundPaymentMethods.size());
        assertThat(foundPaymentMethods).contains(paymentMethod1, paymentMethod2);

    }

    @Test
    @Transactional
    public void testDeleteByCardNumber() {
        // Create a client
        Client client = new Client("test@email.com", "John", "123321", "Doe", 0);
        clientRepository.save(client);

        // Given a PaymentMethod entity with a specific card number
        String cardNumber = "1234567890123456";
        String cardNumber2 = "1111111111111111";
        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, null, "123", "John Doe", 123, client);
        PaymentMethod paymentMethod2 = new PaymentMethod(cardNumber2, null, "123", "John Doe", 321, client);

        // Use the repository to save the PaymentMethod
        paymentMethodRepository.save(paymentMethod);
        paymentMethodRepository.save(paymentMethod2);

        // When deletes only the 1 cardnumber
        paymentMethodRepository.deleteByCardNumber(cardNumber);

        // CardNumber Should not exist
        assertThat(paymentMethodRepository.findByCardNumber(cardNumber)).isNull();

        // Second cardNumber2 should still be there
        PaymentMethod foundPaymentMethod = paymentMethodRepository.findByCardNumber(cardNumber2);
        assertEquals(cardNumber2, foundPaymentMethod.getCardNumber());
    }

    @Test
    @Transactional
    public void testDeleteByClient() {
        // Create 2 clients, one will be save other deleted
        Client client = new Client("test@email.com", "John", "123321", "Doe", 0);
        clientRepository.save(client);

        Client client2 = new Client("delete@email.com", "Paul", "123", "Test", 0);
        clientRepository.save(client2);

        // Given a PaymentMethod entity with a specific card number
        String cardNumber = "1234567890123456";
        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, null, "123", "John Doe", 123, client);
        PaymentMethod paymentMethodToBeDeleted = new PaymentMethod(cardNumber, null, "123", "Paul Test", 321, client2);

        // Use the repository to save the PaymentMethod
        paymentMethodRepository.save(paymentMethod);
        paymentMethodRepository.save(paymentMethodToBeDeleted);

        // When the second client gets deleted
        paymentMethodRepository.deleteByClient(client2);

        // Then, client2 Should not be there
        assertThat(paymentMethodRepository.findByClient(client2)).isEmpty();

        // Second client2 should still be there
        List<PaymentMethod> foundPaymentMethods = paymentMethodRepository.findByClient(client);
        assertEquals(1, foundPaymentMethods.size());
        assertThat(foundPaymentMethods).contains(paymentMethod);
    }

}
