package ca.mcgill.ecse321.SportPlus.dao;

import ca.mcgill.ecse321.SportPlus.model.ClassType;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ClassTypeRepository extends CrudRepository<ClassType, Integer> {

    /**
     * Find ClassType by name
     * @param name
     * @return ClassType
     */

    ClassType findByName(String name);
    /**
     * Find ClassType by id
     * @param typeId
     * @return ClassType
     */
    ClassType findByTypeId(Integer typeId);

    /**
     * Returns a list of classType by their approval
     * @param approved
     * @return List<ClassType> by approved or not approved
     */
    List<ClassType> findByApproved(Boolean approved);
    /**
     * Find all classTypes
     * @return List<ClassType>
     */
    List<ClassType> findAll();

    /**
     * Deletes ClassType by name
     * @param name
     */
    void deleteByName(String name);
    
}
