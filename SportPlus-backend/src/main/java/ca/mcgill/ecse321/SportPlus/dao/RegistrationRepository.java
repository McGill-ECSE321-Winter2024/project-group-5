package ca.mcgill.ecse321.SportPlus.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.SportPlus.model.Registration;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.model.Client;

import java.util.List;

public interface RegistrationRepository extends CrudRepository<Registration, Integer> {

    /**
     * / Find a Registration by its ID
     * 
     * @param regId
     * @return Registration
     */
    Registration findByRegId(int regId);

    /**
     * / Find all registrations for a specific class
     * 
     * @param specificClass
     * @return List<Registration>
     */
    List<Registration> findBySpecificClass(SpecificClass specificClass);

    /**
     * Find all registrations for a client
     * 
     * @param Client
     * @return List<Registration>
     */

    List<Registration> findByClient(Client client);

}
