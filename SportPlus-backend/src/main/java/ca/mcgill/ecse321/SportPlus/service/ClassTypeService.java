package ca.mcgill.ecse321.SportPlus.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.service.utilities.ResourceNotFoundException;

@Service
public class ClassTypeService {

    @Autowired
    private ClassTypeRepository classTypeRepository;

    // Finds a class type by its name, throwing an exception if not found.
    @Transactional
    public ClassType findByName(String name) {
        ClassType classType = classTypeRepository.findByName(name);
        if (classType == null) {
            throw new ResourceNotFoundException("ClassType with name " + name + " not found.");
        }
        return classType;
    }

    // Finds a class type by its unique ID, throwing an exception if not found.
    @Transactional
    public ClassType findByTypeId(Integer typeId) {
        ClassType classType = classTypeRepository.findByTypeId(typeId);
        if (classType == null) {
            throw new ResourceNotFoundException("ClassType with ID " + typeId + " not found.");
        }
        return classType;
    }

    // Finds class types based on their approval status, throwing an exception if no matches are found.
    @Transactional
    public List<ClassType> findByApproval(Boolean approved) {
        List<ClassType> classTypes = classTypeRepository.findByApproved(approved);
        if (classTypes.isEmpty()) {
            throw new ResourceNotFoundException("No ClassTypes found with approved status " + approved + ".");
        }
        return classTypes;
    }

    // Retrieves all class types, throwing an exception if none are found.
    @Transactional
    public List<ClassType> getAllClassTypes() {
        List<ClassType> classTypes = classTypeRepository.findAll();
        if (classTypes.isEmpty()) {
            throw new ResourceNotFoundException("No ClassTypes found.");
        }
        return classTypes;
    }

    // Deletes a class type by name, throwing an exception if it does not exist.
    @Transactional
    public void deleteByName(String name) {
        try {
            classTypeRepository.deleteByName(name);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("ClassType with name " + name + " not found, thus cannot be deleted.");
        }
    }

    // Creates a new class type with instructor privileges, setting approval to false by default.
    @Transactional
    public ClassType instructorCreate(String name, String description, Owner approver) {
        ClassType classType = new ClassType(name, description, 0, false, approver);
        return classTypeRepository.save(classType);
    }

    // Creates a new class type with owner privileges, automatically setting approval to true.
    @Transactional
    public ClassType ownerCreate(String name, String description, Owner approver) {
        ClassType classType = new ClassType(name, description, 0, true, approver);
        return classTypeRepository.save(classType);
    }

    // Approves a class type by its ID, ensuring it is marked as approved in the database.
    @Transactional
    public ClassType approve(int typeId) {
        ClassType classType = findByTypeId(typeId);
        classType.setApproved(true);
        return classTypeRepository.save(classType);
    }

    // Updates the description of a class type, throwing an exception if the new description is null or empty.
    @Transactional
    public ClassType updateDescription(int typeId, String description) {
        ClassType classType = findByTypeId(typeId);

        if (description == null || description.isEmpty()) {
            throw new ResourceNotFoundException("Description cannot be empty.");
        }
        classType.setDescription(description);
        return classTypeRepository.save(classType);
    }

    // Updates the name of a class type, throwing an exception if the new name is null or empty.
    @Transactional
    public ClassType updateName(int typeId, String name) {
        ClassType classType = findByTypeId(typeId);
        if (name == null || name.isEmpty()) {
            throw new ResourceNotFoundException("Name cannot be empty.");
        }
        classType.setName(name);
        return classTypeRepository.save(classType);
    }
}

