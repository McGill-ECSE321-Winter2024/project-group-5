package ca.mcgill.ecse321.SportPlus.service;

import ca.mcgill.ecse321.SportPlus.service.utilities.HelperMethods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.model.Client;
import jakarta.transaction.Transactional;


@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    //-----------Wrappers-----------//

    @Transactional
	public Client getClient(String email) {
		Client client = clientRepository.findByEmail(email);
		return client;
	}

    @Transactional
    public Client getClient(Integer accountId){
        Client client = clientRepository.findByAccountId(accountId);
        return client;

    }
    @Transactional
    public void deleteClient(String email){
        clientRepository.deleteByEmail(email);
    }

    @Transactional
    public  List<Client> getAllClients(){
        return clientRepository.findAll();
    }
    //------------EndWrappers----------//

    @Transactional
	public String createClient(String email, String password) {
		Client client = new Client();
        String mess1 = HelperMethods.ClientEmailCheck(email);
        String mess2 = HelperMethods.PasswordCheck(password);
        if(!mess1.isEmpty()){return mess1;}
        if(!mess2.isEmpty()){return mess2;}
		client.setEmail(email);
        client.setPassword(password);
		clientRepository.save(client);
		return ""; //TODO
	}

    @Transactional
    public String updateClientFirstName(String email, String firstName){
        Client client = getClient(email);
        client.setFirstName(firstName);
        return ""; //TODO
    }

    @Transactional
    public String updateClientLastName(String email, String LastName){
        Client client = getClient(email);
        client.setLastName(LastName);
        return ""; //TODO
    }
    @Transactional
    public String updateClientPassword(String email, String password){
        Client client = getClient(email);
        String message = HelperMethods.PasswordCheck(password);
        if(message.isEmpty()){
            client.setPassword(password);
        }
        return message; 
    }

    
}
