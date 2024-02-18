package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//import org.checkerframework.checker.units.qual.Time;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.model.Instructor;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.sql.Time;

@SpringBootTest
public class SpecificClassRepositoryTests {

    @Autowired
    private SpecificClassRepository specificClassRepository;

    @Autowired
    private ClassTypeRepository classTypeRepository;

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private InstructorRepository instructorRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        specificClassRepository.deleteAll();
        classTypeRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testFindBySessionId() {

        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);

        SpecificClass specificClass = new SpecificClass(null, null, null, 0, yoga);

        specificClassRepository.save(specificClass);
        classTypeRepository.save(yoga);
        ownerRepository.save(owner);

        SpecificClass foundSpecificClass = specificClassRepository.findBySessionId(specificClass.getSessionId());

        assertNotNull(foundSpecificClass, "The specific class should not be null");
        assertEquals(specificClass.getSessionId(), foundSpecificClass.getSessionId(), "Session IDs should match");
        assertEquals(foundSpecificClass.getClassType(), foundSpecificClass.getClassType(),
                "Class type should be the same");
        assertEquals("Owner", foundSpecificClass.getClassType().getApprover().getFirstName(),
                "Owner should be the same");

    }

    @Test
    @Transactional
    public void testFindBydate() {

        // Define the dates
        // First 2 Are the same date different hours
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

        ownerRepository.save(owner);
        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);
        specificClassRepository.save(specificClass3);
        classTypeRepository.save(yoga);

        List<SpecificClass> foundSpecificClasses = specificClassRepository.findByDate(date1);

        // First 2 should have the same date
        assertEquals(2, foundSpecificClasses.size());
        assertThat(foundSpecificClasses).contains(specificClass, specificClass2);

    }

    @Test
    @Transactional
    public void testFindByClassType() {

        // Define the dates
        // First 2 Are the same date different hours
        LocalDateTime localDateTime = LocalDateTime.of(2024, 3, 6, 10, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, 3, 6, 11, 0);
        LocalDateTime localDateTime3 = LocalDateTime.of(2024, 3, 8, 10, 0);
        Date date1 = Date.valueOf(localDateTime.toLocalDate());
        Date date2 = Date.valueOf(localDateTime2.toLocalDate());
        Date date3 = Date.valueOf(localDateTime3.toLocalDate());

        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        ClassType tennis = new ClassType("tennis", "heh class", 0, true, owner);

        SpecificClass specificClass = new SpecificClass(date1, null, null, 0, yoga);
        SpecificClass specificClass2 = new SpecificClass(date2, null, null, 0, tennis);
        SpecificClass specificClass3 = new SpecificClass(date3, null, null, 0, yoga);
        SpecificClass specificClass4 = new SpecificClass(date2, null, null, 0, tennis);

        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);
        specificClassRepository.save(specificClass3);
        specificClassRepository.save(specificClass4);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);
        ownerRepository.save(owner);

        List<SpecificClass> foundYogaSpecificClasses = specificClassRepository.findByClassType(yoga);
        List<SpecificClass> foundTennisSpecificClasses = specificClassRepository.findByClassType(tennis);

        // First 2 should have the same date
        assertEquals(2, foundYogaSpecificClasses.size());
        assertThat(foundYogaSpecificClasses).contains(specificClass, specificClass3);
        assertEquals(2, foundTennisSpecificClasses.size());
        assertThat(foundTennisSpecificClasses).contains(specificClass2, specificClass4);

    }

    @Test
    @Transactional
    public void testFindBySupervisor() {
        LocalDateTime localDateTime1 = LocalDateTime.of(2024, 3, 6, 10, 0);
        LocalDateTime localDateTime11 = LocalDateTime.of(2024, 3, 6, 11, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, 3, 7, 11, 0);
        LocalDateTime localDateTime22 = LocalDateTime.of(2024, 3, 7, 12, 0);
        LocalDateTime localDateTime3 = LocalDateTime.of(2024, 3, 8, 10, 0);
        LocalDateTime localDateTime33 = LocalDateTime.of(2024, 3, 8, 11, 0);
        LocalDateTime localDateTime4 = LocalDateTime.of(2024, 3, 9, 11, 0);
        LocalDateTime localDateTime44 = LocalDateTime.of(2024, 3, 9, 12, 0);

        Date date1start = Date.valueOf(localDateTime1.toLocalDate());
        Date date2start = Date.valueOf(localDateTime2.toLocalDate());
        Date date3start = Date.valueOf(localDateTime3.toLocalDate());
        Date date4start = Date.valueOf(localDateTime4.toLocalDate());

        Time start1 = Time.valueOf(localDateTime1.toLocalTime());
        Time end1 = Time.valueOf(localDateTime11.toLocalTime());
        Time start2 = Time.valueOf(localDateTime2.toLocalTime());
        Time end2 = Time.valueOf(localDateTime22.toLocalTime());
        Time start3 = Time.valueOf(localDateTime3.toLocalTime());
        Time end3 = Time.valueOf(localDateTime33.toLocalTime());
        Time start4 = Time.valueOf(localDateTime4.toLocalTime());
        Time end4 = Time.valueOf(localDateTime44.toLocalTime());

        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);
        Instructor supervisor1 = new Instructor("kyle@gmail.com", "kyle", "1234", "elyk", 4);
        Instructor supervisor2 = new Instructor("leandro@gmail.com", "leandro", "12564", "ordnael", 5);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        ClassType tennis = new ClassType("tennis", "heh class", 0, true, owner);

        SpecificClass specificClass = new SpecificClass(date1start, start1, end1, 0, yoga);
        SpecificClass specificClass2 = new SpecificClass(date2start, start2, end2, 0, tennis);
        SpecificClass specificClass3 = new SpecificClass(date3start, start3, end3, 0, tennis);
        SpecificClass specificClass4 = new SpecificClass(date4start, start4, end4, 0, yoga);

        specificClass.setSupervisor(supervisor1);
        specificClass2.setSupervisor(supervisor2);
        specificClass3.setSupervisor(supervisor1);
        specificClass4.setSupervisor(supervisor2);

        ownerRepository.save(owner);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);
        instructorRepository.save(supervisor1);
        instructorRepository.save(supervisor2);
        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);
        specificClassRepository.save(specificClass3);
        specificClassRepository.save(specificClass4);

        List<SpecificClass> foundSpecificClasses1 = specificClassRepository.findBySupervisor(supervisor1);
        List<SpecificClass> foundSpecificClasses2 = specificClassRepository.findBySupervisor(supervisor2);

        // First 2 should have the same date
        assertEquals(2, foundSpecificClasses1.size());
        assertThat(foundSpecificClasses1).contains(specificClass, specificClass3);
        assertEquals(2, foundSpecificClasses2.size());
        assertThat(foundSpecificClasses2).contains(specificClass2, specificClass4);

    }
    // findBySupervisor(Instructor supervisor);
}
