package ca.mcgill.ecse321.SportPlus.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import ca.mcgill.ecse321.SportPlus.service.OwnerService;

@Configuration
public class OwnerInitializationConfig {

    @Autowired
    private OwnerService ownerService;

    @jakarta.annotation.PostConstruct
    public void initializeOwner() {
        if (ownerService.getOwner() == null) {
            ownerService.createOwner();
        }
    }

}
