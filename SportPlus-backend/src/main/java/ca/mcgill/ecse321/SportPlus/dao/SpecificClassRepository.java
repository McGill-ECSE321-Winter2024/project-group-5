package ca.mcgill.ecse321.SportPlus.dao;

import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Instructor;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import org.springframework.data.repository.CrudRepository;
import java.sql.Date;

import java.util.List;

public interface SpecificClassRepository extends CrudRepository<SpecificClass, String> {
    /**
     * Returns a list of Specific classes by the instructor
     * @param instructor
     * @return List<SpecificClass>
     */
    List<SpecificClass> findByInstructor(Instructor instructor);
    /**
     * Find a Specific Class by sessionId
     * @param sessionId
     * @return SpecificClass
     */
    SpecificClass findBySessionId(Integer sessionId);
    /**
     * Delete a Specific Class by id
     * @param sessionId
     */
    void deleteById(Integer sessionId);
    /**
     * Returns a list of SpecificClasses by their date
     * @param date
     * @return List<SpecificClass>
     */
    List<SpecificClass> findByDate(Date date);
    /**
     * Returns a list of SpecificClasses by their classtype
     * @param classType
     * @return List<SpecificClass>
     */
    List<SpecificClass> findByClassType(ClassType classType);
}
