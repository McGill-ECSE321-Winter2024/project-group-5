package ca.mcgill.ecse321.SportPlus.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Instructor;

import java.util.Date;
import java.util.List;

public interface SpecificClassRepository extends CrudRepository<SpecificClass, Integer> {
    /**
     * / Find a registration by its ID
     * 
     * @param sessionId
     * @return SpecificClass
     */
    SpecificClass findBySessionId(int sessionId);

    /**
     * Find specific classes by date
     * 
     * @param date
     * @return List<SpecificClass>
     */
    List<SpecificClass> findByDate(Date date);

    /**
     * Find specific classes by class type
     * 
     * @param classType
     * @return List<SpecificClass>
     */
    List<SpecificClass> findByClassType(ClassType classType);

    /**
     * Find specific classes by instructor/supervisor ID
     * 
     * @param supervisor
     * @return List<SpecificClass>
     */
    List<SpecificClass> findBySupervisor(Instructor supervisor);
}
