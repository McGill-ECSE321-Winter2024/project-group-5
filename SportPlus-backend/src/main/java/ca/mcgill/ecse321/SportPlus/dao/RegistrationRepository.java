package ca.mcgill.ecse321.SportPlus.dao;

import ca.mcgill.ecse321.SportPlus.model.Registration;
import org.springframework.data.repository.CrudRepository;

public interface RegistrationRepository extends CrudRepository<Registration, String> {



}
