package ca.mcgill.ecse321.SportPlus.dao;

import ca.mcgill.ecse321.SportPlus.model.Owner;

import org.springframework.data.repository.CrudRepository;

public interface OwnerRepository extends CrudRepository<Owner, Integer> {

    /**
     * Find ClassType by name
     * 
     * @param name
     * @return Owner
     */
    Owner findByEmail(String email);

    /**
     * Find ClassType by name
     * 
     * @param accountId
     * @return Owner
     */
    Owner findByAccountId(Integer accountId);
}
