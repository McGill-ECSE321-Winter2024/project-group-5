package ca.mcgill.ecse321.SportPlus.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Instructor;

import java.sql.Date;
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
     * 
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
     * Find specific classes by name
     * 
     * @param name
     * @return SpecificClass
     */
    SpecificClass findByName(String name);

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

    /**
     * Deletes by SessionId
     *
     * @param sessionId
     */
    void deleteBySessionId(int sessionId);

    // Find SpecifcClasses between a date range.
    @Query("SELECT sc FROM SpecificClass sc WHERE sc.date BETWEEN :startDate AND :endDate")
    List<SpecificClass> findClassesBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Finds Classes in the future with a Supervisor
    @Query("SELECT sc FROM SpecificClass sc WHERE sc.supervisor IS NOT NULL AND (sc.date > :currentDate OR (sc.date = :currentDate AND sc.startTime > :currentTime))")
    List<SpecificClass> findBySupervisorIsNotNullAndDateAfterOrDateEqualsAndStartTimeAfter(
            @Param("currentDate") Date currentDate, @Param("currentTime") Time currentTime);

}
