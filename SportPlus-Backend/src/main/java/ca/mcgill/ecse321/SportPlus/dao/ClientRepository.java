package ca.mcgill.ecse321.SportPlus.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.SportPlus.model.Client;

public interface ClientRepository extends CrudRepository<Client, String> {
    Client findClientByName(String name);
}
