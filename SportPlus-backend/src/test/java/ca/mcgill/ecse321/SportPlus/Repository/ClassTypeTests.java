package ca.mcgill.ecse321.SportPlus.Repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Owner;

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

    @Test
    public void testfindByTypeId() {

        // Given
        Owner owner = new Owner(null, null, null, null, 0);
        owner = ownerRepository.save(owner);

        ClassType classType = new ClassType("Yoga", "A relaxing class", 12, true, owner);
        classType = classTypeRepository.save(classType); // Save and reassign to capture the persisted entity

        // When
        ClassType found = classTypeRepository.findByTypeId(classType.getTypeId());

        // Then
        assertThat(found.getTypeId()).isEqualTo(classType.getTypeId());
    }

    @Test
    public void findByApproved() {

        // Given
        Owner owner = new Owner(null, null, null, null, 0);
        owner = ownerRepository.save(owner);

        // A few classes with approved and not approved to test
        classTypeRepository.save(new ClassType("Yoga", "A relaxing class", 1, true, owner));
        classTypeRepository.save(new ClassType("Pilates", "A core strength class", 2, true, owner));
        classTypeRepository.save(new ClassType("Kickboxing", "A high-intensity class", 3, false, owner));
        classTypeRepository.save(new ClassType("Zumba", "A dance fitness class", 4, false, owner));

        List<ClassType> approvedClasses = classTypeRepository.findByApproved(true);
        List<ClassType> notApprovedClasses = classTypeRepository.findByApproved(false);

        assertThat(approvedClasses).hasSize(2);
        assertThat(approvedClasses.stream().allMatch(ClassType::getApproved)).isTrue();

        assertThat(notApprovedClasses).hasSize(2);
        assertThat(notApprovedClasses.stream().noneMatch(ClassType::getApproved)).isTrue();

    }

    // List<ClassType> findAll();
    @Test
    public void findAll() {

        // Given
        Owner owner = new Owner(null, null, null, null, 0);
        owner = ownerRepository.save(owner);

        ClassType classType1 = new ClassType("Yoga", "A relaxing class", 12, true, owner);
        ClassType classType2 = new ClassType("Pilates", "A core strength class", 2, true, owner);
        ClassType classType3 = new ClassType("Kickboxing", "A high-intensity class", 3, false, owner);
        ClassType classType4 = new ClassType("Zumba", "A dance fitness class", 4, false, owner);

        classTypeRepository.save(classType1);
        classTypeRepository.save(classType2);
        classTypeRepository.save(classType3);
        classTypeRepository.save(classType4);

        List<ClassType> allClassTypes = (List<ClassType>) classTypeRepository.findAll();

        assertThat(allClassTypes).hasSize(4);

        assertThat(allClassTypes).contains(classType1, classType2, classType3, classType4);

    }

    @Test
    @Transactional
    public void testDeleteByName() {
        // Setup

        Owner owner = new Owner(null, null, null, null, 0);
        owner = ownerRepository.save(owner);

        String nameToDelete = "Yoga";
        ClassType yoga = new ClassType(nameToDelete, "A relaxing class", 0, true, owner);
        ClassType pilates = new ClassType("Pilates", "A core strength class", 1, true, owner);

        // Save both classes
        classTypeRepository.save(yoga);
        classTypeRepository.save(pilates);

        // Action
        // Delete only yoga
        classTypeRepository.deleteByName(nameToDelete);

        // Verification
        // Checks if yoga is deleted
        // Checks if pilates is still there
        assertThat(classTypeRepository.findByName(nameToDelete)).isNull();

        // Checks if pilates still there
        assertThat(classTypeRepository.findAll()).hasSize(1);
        ClassType remainingClassType = classTypeRepository.findByName("Pilates");
        assertThat(remainingClassType).isNotNull(); // Verify "Pilates" still exists
        assertThat(remainingClassType.getName()).isEqualTo("Pilates"); // Further verify the details if needed

    }

}
