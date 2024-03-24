package ca.mcgill.ecse321.SportPlus.Service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.SportPlus.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.SportPlus.model.PaymentMethod;
import ca.mcgill.ecse321.SportPlus.service.PaymentMethodService;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.model.Client;

@ExtendWith(MockitoExtension.class)
public class TestPaymentMethodService {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private PaymentMethodService paymentMethodService;

    private static final String PAYMENTMETHOD_CARDNUMBER = "1234567812345678";
    private static final Date PAYMENTMETHOD_EXPDATE = new Date(1830297600000L);
    private static final String PAYMENTMETHOD_CVC = "111";
    private static final String PAYMENTMETHOD_CARDHOLDERNAME = "John Doe";
    private static final int PAYMENTMETHOD_CARDID = 1;

    private static final String CLIENT_EMAIL = "johndoe@email.com";
    private static final Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
    private static final Client WITHOUTPAYMENTMETHOD_CLIENT = new Client("nopaymentmethod@email.com", "John", "Pass123",
            "Doe", 3);

    @BeforeEach
    public void setMockOutput() {
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(paymentMethod);
        lenient().when(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER)).thenReturn(paymentMethod);
        lenient().when(paymentMethodRepository.findByClient(PAYMENTMETHOD_CLIENT)).thenReturn(paymentMethods);
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(clientRepository.findByEmail(CLIENT_EMAIL)).thenReturn(PAYMENTMETHOD_CLIENT);
        lenient().when(clientRepository.findByEmail("nopaymentmethod@email.com"))
                .thenReturn(WITHOUTPAYMENTMETHOD_CLIENT);
    }

    @Test
    public void testCreatePaymentMethod() {
        PaymentMethod paymentMethod = paymentMethodService.createPaymentMethod(PAYMENTMETHOD_CARDNUMBER,
                PAYMENTMETHOD_EXPDATE, PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CLIENT);

        assertNotNull(paymentMethod);
        assertEquals(paymentMethod.getCardHolderName(), PAYMENTMETHOD_CARDHOLDERNAME);
        assertEquals(paymentMethod.getCardNumber(), PAYMENTMETHOD_CARDNUMBER);
        assertEquals(paymentMethod.getCvc(), PAYMENTMETHOD_CVC);
        assertEquals(paymentMethod.getClient(), PAYMENTMETHOD_CLIENT);
        assertEquals(paymentMethod.getExpDate(), PAYMENTMETHOD_EXPDATE);

        verify(paymentMethodRepository, times(1)).save(paymentMethod);
    }

    @Test
    public void testDeletePaymentMethod() {
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);

        paymentMethodService.deletePaymentMethod(paymentMethod.getCardNumber());

        verify(paymentMethodRepository, times(1)).deleteByCardNumber(PAYMENTMETHOD_CARDNUMBER);
    }

    @Test
    public void testDeletePaymentMethodsByClient() {
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);

        paymentMethodService.deletePaymentMethods(paymentMethod.getClient());

        verify(paymentMethodRepository, times(1)).deleteByClient(PAYMENTMETHOD_CLIENT);
    }

    @Test
    public void tetGetPaymentMethod() {
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethod(PAYMENTMETHOD_CARDNUMBER);

        assertNotNull(paymentMethod);
        assertEquals(paymentMethod.getCardHolderName(), PAYMENTMETHOD_CARDHOLDERNAME);
        assertEquals(paymentMethod.getCardId(), PAYMENTMETHOD_CARDID);
        assertEquals(paymentMethod.getCardNumber(), PAYMENTMETHOD_CARDNUMBER);
        assertEquals(paymentMethod.getCvc(), PAYMENTMETHOD_CVC);
        assertEquals(paymentMethod.getClient(), PAYMENTMETHOD_CLIENT);
        assertEquals(paymentMethod.getExpDate(), PAYMENTMETHOD_EXPDATE);
    }

    @Test
    public void testGetInvalidPaymentMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentMethodService.getPaymentMethod("0987654321098765");
        });
    }

    @Test
    public void tetGetPaymentMethodsByClient() {
        List<PaymentMethod> paymentMethods = paymentMethodService.getPaymentMethods(PAYMENTMETHOD_CLIENT);
        for (PaymentMethod paymentMethod : paymentMethods) {
            assertNotNull(paymentMethod);
            assertEquals(paymentMethod.getCardHolderName(), PAYMENTMETHOD_CARDHOLDERNAME);
            assertEquals(paymentMethod.getCardId(), PAYMENTMETHOD_CARDID);
            assertEquals(paymentMethod.getCardNumber(), PAYMENTMETHOD_CARDNUMBER);
            assertEquals(paymentMethod.getCvc(), PAYMENTMETHOD_CVC);
            assertEquals(paymentMethod.getClient(), PAYMENTMETHOD_CLIENT);
            assertEquals(paymentMethod.getExpDate(), PAYMENTMETHOD_EXPDATE);
        }
    }

    @Test
    public void testHasPaymentMethod() {
        assertTrue(paymentMethodService.hasPaymentMethod(PAYMENTMETHOD_CLIENT));
    }

    @Test
    public void testDoesNotHavePaymentMethod() {
        assertFalse(paymentMethodService.hasPaymentMethod(WITHOUTPAYMENTMETHOD_CLIENT));
    }

}
