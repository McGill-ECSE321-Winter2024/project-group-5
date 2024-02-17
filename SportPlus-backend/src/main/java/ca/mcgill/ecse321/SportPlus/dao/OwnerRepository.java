package ca.mcgill.ecse321.SportPlus.dao;

import ca.mcgill.ecse321.SportPlus.model.Owner;
import org.springframework.data.repository.CrudRepository;

public interface OwnerRepository extends CrudRepository<Owner, String> {
    /**
     * Get owner by email
     * @param email
     * @return
     */
    Owner findByEmail(String email);

}
