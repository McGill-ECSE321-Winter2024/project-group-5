package ca.mcgill.ecse321.SportPlus.dao;

import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.Registration;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RegistrationRepository extends CrudRepository<Registration, String> {
    /**
     * find list of registrations by client
     * @param client
     * @return List<Registration>
     */
    List<Registration> findByClient(Client client);

    /**
     * Verifies if a registration exists with client and specificClass
     * @param client
     * @param specificClass
     * @return boolean
     */
    boolean existsByClientAndSpecificClass(Client client, SpecificClass specificClass);
    /**
     * Finds registration with client and specificClass
     * @param client
     * @param specificClass
     * @return registration
     */
    Registration findByClientAndSpecificClass(Client client, SpecificClass specificClass);
    /**
     * find list of registrations by specific class
     * @param specificClass
     * @return List<Registration>
     */
    List<Registration> findBySessionId(SpecificClass specificClass);
    /**
     * Delete a registration by registration id
     * @param regId
     */
    void deleteByRegId(Integer regId);
    /**
     * Find all Registrations
     * @return List<Registration>
     */
    List<Registration> findAll();

}
