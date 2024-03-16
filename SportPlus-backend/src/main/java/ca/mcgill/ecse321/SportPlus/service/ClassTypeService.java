package ca.mcgill.ecse321.SportPlus.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.service.utilities.HelperMethods;

@Service
public class ClassTypeService {

    @Autowired
    private ClassTypeRepository classTypeRepository;

    @Transactional
    public ClassType findByName(String name) {
        return classTypeRepository.findByName(name);
    }

    @Transactional
    public ClassType findByTypeId(Integer typeId) {
        return classTypeRepository.findByTypeId(typeId);
    }

    @Transactional
    public List<ClassType> findByApproval(Boolean approved) {
        return classTypeRepository.findByApproved(approved);
    }
    
    @Transactional
    public List<ClassType> getAllClassTypes() {
        return HelperMethods.toList(classTypeRepository.findAll());
    }
    @Transactional
    public void deleteByName(String name) {
        classTypeRepository.deleteByName(name);
    }

    @Transactional
    public ClassType instructorCreate(String name, String description, Owner approver) {
        ClassType classType = new ClassType(name, description, 0, false, approver);
        return classTypeRepository.save(classType);
    }

    @Transactional
    public ClassType ownerCreate(String name, String description, Owner approver) {
        ClassType classType = new ClassType(name, description, 0, false, approver);
        return classTypeRepository.save(classType);
    }

    @Transactional
    public ClassType approve(int typeId) {
        ClassType classType = classTypeRepository.findByTypeId(typeId);
        if (classType != null) {
            classType.setApproved(true);
            return classTypeRepository.save(classType);
        }
        return null; // Or throw an exception
    }

    @Transactional
    public ClassType updateDescription(int typeId, String description) {
        ClassType classType = classTypeRepository.findByTypeId(typeId);
        if (classType != null) {
            classType.setDescription(description);
            return classTypeRepository.save(classType);
        }
        return null; // Or throw an exception
    }

    @Transactional
    public ClassType updateName(int typeId, String name) {
        ClassType classType = classTypeRepository.findByTypeId(typeId);
        if (classType != null) {
            classType.setName(name);
            return classTypeRepository.save(classType);
        }
        return null; // Or throw an exception
    }
}
