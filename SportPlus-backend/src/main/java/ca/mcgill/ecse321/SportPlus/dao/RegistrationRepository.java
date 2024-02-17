package ca.mcgill.ecse321.SportPlus.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.SportPlus.model.Registration;
import java.util.List;

public interface RegistrationRepository extends CrudRepository<Registration, Integer> {

    // Find a registration by its ID
    Registration findByRegId(int regId);

    // Find all registrations for a specific class
    // List<Registration> findBySpecificClassId(int specificClassId);

    // Find all registrations for a client
    // List<Registration> findByClientId(int clientId);

}
