package ca.mcgill.ecse321.SportPlus.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import java.util.Date;
import java.util.List;

public interface SpecificClassRepository extends CrudRepository<SpecificClass, Integer> {
    // private int sessionId;
    SpecificClass findBysessionId(int sessionId);

    // TODO: To implement later !!
    // Find specific classes by date
    // List<SpecificClass> findByDate(Date date);

    // // Find specific classes by class type ID
    // List<SpecificClass> findByClassTypeId(int classTypeId);

    // // Find specific classes within a time range on a specific date
    // List<SpecificClass> findByDateAndStartTimeBeforeAndEndTimeAfter(Date date,
    // Date startTime, Date endTime);

    // // Find specific classes by instructor/supervisor ID
    // List<SpecificClass> findBySupervisorId(int supervisorId);
}
