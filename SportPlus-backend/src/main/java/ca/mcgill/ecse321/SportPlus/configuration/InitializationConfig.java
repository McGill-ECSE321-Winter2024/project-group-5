package ca.mcgill.ecse321.SportPlus.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.ArrayList;

import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.service.ClassTypeService;
import ca.mcgill.ecse321.SportPlus.service.OwnerService;
import ca.mcgill.ecse321.SportPlus.service.utilities.ResourceNotFoundException;

@Configuration
public class InitializationConfig {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ClassTypeService classTypeService;

    @jakarta.annotation.PostConstruct
    public void initializeOwner() {
        // initalizes owner if owner doesn't exist in database
        if (ownerService.getOwner() == null) {
            ownerService.createOwner();
        }

        List<String> toTest = new ArrayList<>();
        try {
            for (ClassType ct : classTypeService.getAllClassTypes()) {
                toTest.add(ct.getName());
            }

            // checks if classtypes exist and adds them by default
            if (!toTest.contains("Cardio")) {
                classTypeService.instructorCreate("Cardio", "A cardio class", null);
            }
            if (!toTest.contains("Stretching")) {
                classTypeService.instructorCreate("Stretching", "A stretching class", null);
            }
            if (!toTest.contains("Strength Training")) {
                classTypeService.instructorCreate("Strength Training", "A strength training class", null);
            }

        } catch (ResourceNotFoundException e) {
            // if no classtypes exist, adds all three by default
            classTypeService.instructorCreate("Cardio", "A cardio class", null);
            classTypeService.instructorCreate("Stretching", "A stretching class", null);
            classTypeService.instructorCreate("Strength Training", "A strength training class", null);
        }

    }

}
