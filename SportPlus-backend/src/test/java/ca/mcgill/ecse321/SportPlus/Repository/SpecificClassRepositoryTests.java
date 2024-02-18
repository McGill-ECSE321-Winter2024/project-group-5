package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
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

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

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

    // List<SpecificClass> findByDate(Date date);
    // TODO: TO FInish
    @Test
    @Transactional
    public void testFindBydate() {

        // Define the dates
        // First 2 Are the same different hours
        LocalDateTime localDateTime = LocalDateTime.of(2024, 3, 6, 10, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, 3, 6, 11, 0);
        LocalDateTime localDateTime3 = LocalDateTime.of(2024, 3, 8, 10, 0);
        Date date1 = Date.valueOf(localDateTime.toLocalDate());
        Date date2 = Date.valueOf(localDateTime2.toLocalDate());
        Date date3 = Date.valueOf(localDateTime3.toLocalDate());

        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);

        SpecificClass specificClass = new SpecificClass(date1, null, null, 0, yoga);
        SpecificClass specificClass2 = new SpecificClass(date2, null, null, 0, yoga);
        SpecificClass specificClass3 = new SpecificClass(date3, null, null, 0, yoga);

        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);
        specificClassRepository.save(specificClass3);
        classTypeRepository.save(yoga);
        ownerRepository.save(owner);

        List<SpecificClass> foundSpecificClasses = specificClassRepository.findByDate(date1);

        // First 2 shgould have the same date
        assertEquals(2, foundSpecificClasses.size());
        assertThat(foundSpecificClasses).contains(specificClass, specificClass2);

    }
}
