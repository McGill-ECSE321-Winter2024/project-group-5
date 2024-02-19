package ca.mcgill.ecse321.SportPlus.dao;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import ca.mcgill.ecse321.SportPlus.model.Instructor;

public interface InstructorRepository extends CrudRepository<Instructor, Integer> {
    /**
     * Find a Instructor by email
     * 
     * @param email
     * @return Instructor
     */
    Instructor findInstructorByEmail(String email);

    /**
     * Delete the Instructor by email
     * 
     * @param email
     */
    void deleteInstructorByEmail(String email);

    /**
     * Find all Instructor
     * 
     * @return List<Instructor>
     */
    List<Instructor> findAll();

    /**
     * Find Instructor by accountId
     * 
     * @param accountId
     * @return
     */
    Instructor findByAccountId(Integer accountId);
}
