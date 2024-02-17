package ca.mcgill.ecse321.SportPlus.Repository;

import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.model.SportPlus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.SportPlusRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;

@SpringBootTest
public class ClassTypeTests {

    @Autowired
    private ClassTypeRepository classTypeRepository;
    @Autowired
    private SportPlusRepository sportPlusRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    public void testFindByName() {

        // Given
        SportPlus sportplus = new SportPlus();
        sportplus = sportPlusRepository.save(sportplus);

        Owner owner = new Owner(null, null, null, null, 0, sportplus);
        owner = ownerRepository.save(owner);

        ClassType classType = new ClassType("Yoga", "A relaxing class", 0, true, sportplus, owner);
        classType = classTypeRepository.save(classType); // Save and reassign to capture the persisted entity

        // When
        ClassType found = classTypeRepository.findByName(classType.getName());

        // Then
        assertThat(found.getName()).isEqualTo(classType.getName());
    }

    @Test
    public void findByTypeId() {

        // Given
        SportPlus sportplus = new SportPlus();
        sportplus = sportPlusRepository.save(sportplus);

        Owner owner = new Owner(null, null, null, null, 0, sportplus);
        owner = ownerRepository.save(owner);

        ClassType classType = new ClassType("Yoga", "A relaxing class", 0, true, sportplus, owner);
        classType = classTypeRepository.save(classType); // Save and reassign to capture the persisted entity

        // When
        ClassType found = classTypeRepository.findByName(classType.getName());

        // Then
        assertThat(found.getName()).isEqualTo(classType.getName());
    }

}
