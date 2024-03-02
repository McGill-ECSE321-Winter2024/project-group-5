package ca.mcgill.ecse321.SportPlus.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Instructor;

import java.util.Date;
import java.util.List;
import java.sql.Time;

public interface SpecificClassRepository extends CrudRepository<SpecificClass, Integer> {
    /**
     * / Find a registration by its ID
     * 
     * @param sessionId
     * @return SpecificClass
     */
    SpecificClass findBySessionId(Integer sessionId);

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

    /**
     * Find specific classes by date and startTime
     * 
     * @param date
     * @param startTime
     * @return SpecificClass
     */

    SpecificClass findByDateAndStartTime(Date date, Time startTime);

    /**
     * Find Specific Classes with supervisors
     * @return List<SpecificClass>
     */
    List<SpecificClass> findBySupervisorIsNotNull();

     /**
     * Find all Specific Classes
     * 
     * @return List<SpecificClass>
     */
    List<SpecificClass> findAll();

    /**
     * Deletes by ClassType 
     * 
     * @param classType
     */
    void deleteByClassType(ClassType classType);

    /**
     * Deletes by supervisor 
     * 
     * @param supervisor
     */
    void deleteBySupervisor(Instructor supervisor);

    /**
     * Deletes by Date
     * 
     * @param date
     */
    void deleteByDate(Date date);

    // /**
    //  * Deletes by SessionId
    //  * 
    //  * @param sessionId
    //  */
    // void deleteSessionId(int sessionId);
}
