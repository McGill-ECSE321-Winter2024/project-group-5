package ca.mcgill.ecse321.SportPlus.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.dto.PaymentMethodResponseDto;
import ca.mcgill.ecse321.SportPlus.dto.PaymentMethodRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.ClientPaymentResponseDto;
import ca.mcgill.ecse321.SportPlus.dto.PaymentMethodListDto;
import ca.mcgill.ecse321.SportPlus.model.PaymentMethod;
import ca.mcgill.ecse321.SportPlus.service.ClientService;
import ca.mcgill.ecse321.SportPlus.service.PaymentMethodService;

@CrossOrigin(origins = "*")
@RestController
public class PaymentMethodRestController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private ClientService clientService;

    @GetMapping(value = { "/paymentMethod/getByCardNumber/{cardNumber}", "/paymentMethod/getByCardNumber/{cardNumber}/" })
    public PaymentMethodResponseDto findPaymentMethodByCardNumber(@PathVariable("cardNumber") String theCardNumber) {
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethod(theCardNumber);
        return new PaymentMethodResponseDto(paymentMethod);
    }

    @GetMapping(value = { "paymentMethod/getByClient/{clientId}", "paymentMethod/getByClient/{clientId}/" })
    public PaymentMethodListDto findPaymentMethodsByClient(@PathVariable("clientId") int theClientId) {
        Client client = clientService.getClient(theClientId);
        List<PaymentMethodResponseDto> paymentMethods = new ArrayList<>();
        for (PaymentMethod paymentResponseDto : paymentMethodService.getPaymentMethods(client)) {
            paymentMethods.add(new PaymentMethodResponseDto(paymentResponseDto));
        }
        return new PaymentMethodListDto(paymentMethods);
    }

    @DeleteMapping(value = { "paymentMethod/deleteByCardNumber/{cardNumber}", "paymentMethod/deleteByCardNumber/{cardNumber}/" })
    public void deletePaymentMethodByCardNumber(@PathVariable("cardNumber") String theCardNumber) {
        paymentMethodService.deletePaymentMethod(theCardNumber);
    }

    @DeleteMapping(value = { "paymentMethod/deleteByClient/{clientId}", "paymentMethod/deleteByClient/{clientId}/" })
    public void deletePaymentMethodsByClient(@PathVariable("clientId") int theClientId) {
        Client client = clientService.getClient(theClientId);
        paymentMethodService.deletePaymentMethods(client);
    }

    @PostMapping(value = { "/paymentMethod/create", "/paymentMethod/create/" })
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentMethodResponseDto createPaymentMethod(@RequestBody PaymentMethodRequestDto paymentMethod) {
        PaymentMethod createdPaymentMethod = paymentMethodService.createPaymentMethod(paymentMethod.getCardNumber(), paymentMethod.getExDate(), paymentMethod.getCvc(), paymentMethod.getCardHolderName(), paymentMethod.getClient());
        return new PaymentMethodResponseDto(createdPaymentMethod);
    }

    @GetMapping(value = { "clients/hasPaymentMethod/{clientEmail}", "clients/hasPaymentMethod/{clientEmail}/" })
    public ClientPaymentResponseDto hasPaymentMethod(@PathVariable("clientEmail") String theEmail) {
        Client client = clientService.getClient(theEmail);
        boolean hasPaymentMethod = paymentMethodService.hasPaymentMethod(client);
        return new ClientPaymentResponseDto(hasPaymentMethod);
    }

}
