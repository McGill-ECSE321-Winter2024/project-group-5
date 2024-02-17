package ca.mcgill.ecse321.SportPlus.Repository;

import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Owner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;

@SpringBootTest
public class ClassTypeTests {

    @Autowired
    private ClassTypeRepository classTypeRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        classTypeRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    public void testFindByName() {

        // Given
        Owner owner = new Owner(null, null, null, null, 0);
        owner = ownerRepository.save(owner);

        ClassType classType = new ClassType("Yoga", "A relaxing class", 0, true, owner);
        classType = classTypeRepository.save(classType); // Save and reassign to capture the persisted entity

        // When
        ClassType found = classTypeRepository.findByName(classType.getName());

        // Then
        assertThat(found.getName()).isEqualTo(classType.getName());
    }

}
