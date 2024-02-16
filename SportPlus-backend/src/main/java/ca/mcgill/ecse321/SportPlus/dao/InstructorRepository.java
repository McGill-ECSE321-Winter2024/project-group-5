package ca.mcgill.ecse321.SportPlus.dao;
import ca.mcgill.ecse321.SportPlus.model.Client;
import org.springframework.data.repository.CrudRepository;


import ca.mcgill.ecse321.SportPlus.model.Instructor;

import java.util.List;

public interface InstructorRepository extends CrudRepository<Instructor, String>{

    /**
     * Find an instructor by email
     * @param email
     * @return Instructor
     */
    Instructor findInstructorByEmail(String email);

    /**
     * Delete an instructor by email
     * @param email
     */
    void deleteInstructorByEmail(String email);
    /**
     * Find Instructors by firstName
     * @param firstName
     * @return List<Instructor>
     */
    List<Instructor> findByFirstName(String firstName);
    /**
     * Find Instructors by lastName
     * @param lastName
     * @return List<Instructor>
     */
    List<Instructor> findByLastName(String lastName);
    /**
     * Find Instructor by registration id
     * @param regId
     * @return
     */

    /**
     * Find all instructors
     * @return List<Instructor>
     */
    List<Instructor> findAll();

    /**
     * Find Instructor by specific class id
     * @param sessionId
     * @return
     */
    Instructor findBySessionId(Integer sessionId);
    /**
     * Find Instructor by accountId
     * @param accountId
     * @return
     */
    Instructor findByAccountId(Integer accountId);

}
