package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;

@SpringBootTest
public class SpecificClassRepositoryTests {

    @Autowired
    private SpecificClassRepository specificClassRepository;

    @Autowired
    private ClassTypeRepository classTypeRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        specificClassRepository.deleteAll();
        classTypeRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testFindBysessionId() {

        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);

        SpecificClass specificClass = new SpecificClass(null, null, null, 0, yoga);

        specificClassRepository.save(specificClass);
        classTypeRepository.save(yoga);
        ownerRepository.save(owner);

        SpecificClass foundSpecificClass = specificClassRepository.findBysessionId(specificClass.getSessionId());

        assertNotNull(foundSpecificClass, "The specific class should not be null");
        assertEquals(specificClass.getSessionId(), foundSpecificClass.getSessionId(), "Session IDs should match");
        assertEquals(foundSpecificClass.getClassType(), foundSpecificClass.getClassType(),
                "Class type should be the same");
        assertEquals("Owner", foundSpecificClass.getClassType().getApprover().getFirstName(),
                "Owner should be the same");

    }
}
