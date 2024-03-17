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

    @Transactional
    public ClassType findByName(String name) {
        ClassType classType = classTypeRepository.findByName(name);
        if (classType == null) {
            throw new ResourceNotFoundException("ClassType with name " + name + " not found.");
        }
        return classType;
    }

    @Transactional
    public ClassType findByTypeId(Integer typeId) {
        ClassType classType = classTypeRepository.findByTypeId(typeId);
        if (classType == null) {
            throw new ResourceNotFoundException("ClassType with ID " + typeId + " not found.");
        }
        return classType;
    }

    @Transactional
    public List<ClassType> findByApproval(Boolean approved) {
        List<ClassType> classTypes = classTypeRepository.findByApproved(approved);
        if (classTypes.isEmpty()) {
            throw new ResourceNotFoundException("No ClassTypes found with approved status " + approved + ".");
        }
        return classTypes;
    }
    
    @Transactional
    public List<ClassType> getAllClassTypes() {
        List<ClassType> classTypes = classTypeRepository.findAll();
        if (classTypes.isEmpty()) {
            throw new ResourceNotFoundException("No ClassTypes found.");
        }
        return classTypes;
    }

    @Transactional
    public void deleteByName(String name) {
        try {
            classTypeRepository.deleteByName(name);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("ClassType with name " + name + " not found, thus cannot be deleted.");
        }
    }

    @Transactional
    public ClassType instructorCreate(String name, String description, Owner approver) {
        ClassType classType = new ClassType(name, description, 0, false, approver);
        return classTypeRepository.save(classType);
    }

    @Transactional
    public ClassType ownerCreate(String name, String description, Owner approver) {
        ClassType classType = new ClassType(name, description, 0, true, approver);
        return classTypeRepository.save(classType);
    }

    @Transactional
    public ClassType approve(int typeId) {
        ClassType classType = findByTypeId(typeId); 
        classType.setApproved(true);
        return classTypeRepository.save(classType);
    }

    @Transactional
    public ClassType updateDescription(int typeId, String description) {
        ClassType classType = findByTypeId(typeId);

        if (description==null||description=="") {
            throw new ResourceNotFoundException("Description canno't be empty.");
        }
        classType.setDescription(description);
        return classTypeRepository.save(classType);
    }

    @Transactional
    public ClassType updateName(int typeId, String name) {
        ClassType classType = findByTypeId(typeId);
        if (name==null||name=="") {
            throw new ResourceNotFoundException("Name canno't be empty.");
        }
        classType.setName(name);
        return classTypeRepository.save(classType);
    }
}
