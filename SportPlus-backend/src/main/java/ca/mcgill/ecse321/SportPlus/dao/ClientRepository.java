package ca.mcgill.ecse321.SportPlus.dao;

import ca.mcgill.ecse321.SportPlus.model.Client;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, String> {
    /**
     * Find a client by email
     * @param email
     * @return Client
     */
    Client findClientByEmail(String email);

    /**
     * Delete the client with the provided email
     * @param email
     */
    void deleteClientByEmail(String email);

    /**
     * Find all clients
     */
    List<Client> findAll();

}
