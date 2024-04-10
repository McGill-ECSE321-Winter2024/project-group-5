package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.sql.Time;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.LoginRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.model.Instructor;

@SpringBootTest
public class SpecificClassRepositoryTests {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private ClassTypeRepository classTypeRepository;

    @Autowired
    private SpecificClassRepository specificClassRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        loginRepository.deleteAll();
        registrationRepository.deleteAll();
        specificClassRepository.deleteAll();
        classTypeRepository.deleteAll();
        instructorRepository.deleteAll();
        paymentMethodRepository.deleteAll();
        clientRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testFindBySessionId() {
        // creates owner and class type in order to create a specific class
        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);
        ownerRepository.save(owner);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        classTypeRepository.save(yoga);

        SpecificClass specificClass = new SpecificClass(null, null, null, 0, yoga, null);
        specificClassRepository.save(specificClass);

        // gets the specific class's id
        SpecificClass foundSpecificClass = specificClassRepository.findBySessionId(specificClass.getSessionId());

        // checks that the specific class exists in the database
        assertNotNull(foundSpecificClass, "The specific class should not be null");
        assertEquals(specificClass.getSessionId(), foundSpecificClass.getSessionId(), "Session IDs should match");
        assertEquals(foundSpecificClass.getClassType(), foundSpecificClass.getClassType(),
                "Class type should be the same");
        assertEquals("Owner", foundSpecificClass.getClassType().getApprover().getFirstName(),
                "Owner should be the same");
    }

    @Test
    @Transactional
    public void testFindByName() {
        // creates owner and class type in order to create a specific class
        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);
        ownerRepository.save(owner);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        classTypeRepository.save(yoga);

        SpecificClass specificClass = new SpecificClass(null, null, null, 0, yoga, "NewName");
        specificClassRepository.save(specificClass);

        // gets the specific class's id
        SpecificClass foundSpecificClass = specificClassRepository.findByName("NewName");

        // checks that the specific class exists in the database
        assertNotNull(foundSpecificClass, "The specific class should not be null");
        assertEquals(specificClass.getSessionId(), foundSpecificClass.getSessionId(), "Session IDs should match");
        assertEquals(foundSpecificClass.getClassType(), foundSpecificClass.getClassType(),
                "Class type should be the same");
        assertEquals("Owner", foundSpecificClass.getClassType().getApprover().getFirstName(),
                "Owner should be the same");
        assertEquals("NewName", foundSpecificClass.getName());
    }

    @Test
    @Transactional
    public void testFindBydate() {
        // Define the dates, such that the first two have same dates but diffrent hours
        LocalDateTime localDateTime = LocalDateTime.of(2024, 3, 6, 10, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, 3, 6, 11, 0);
        LocalDateTime localDateTime3 = LocalDateTime.of(2024, 3, 8, 10, 0);
        Date date1 = Date.valueOf(localDateTime.toLocalDate());
        Date date2 = Date.valueOf(localDateTime2.toLocalDate());
        Date date3 = Date.valueOf(localDateTime3.toLocalDate());

        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);
        ownerRepository.save(owner);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        classTypeRepository.save(yoga);

        SpecificClass specificClass = new SpecificClass(date1, null, null, 0, yoga, null);
        SpecificClass specificClass2 = new SpecificClass(date2, null, null, 0, yoga, null);
        SpecificClass specificClass3 = new SpecificClass(date3, null, null, 0, yoga, null);

        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);
        specificClassRepository.save(specificClass3);

        List<SpecificClass> foundSpecificClasses = specificClassRepository.findByDate(date1);

        // First 2 should have the same date
        assertEquals(2, foundSpecificClasses.size());
        assertThat(foundSpecificClasses).contains(specificClass, specificClass2);
    }

    @Test
    @Transactional
    public void testFindByClassType() {
        // Define the dates, such that the first two have same dates but diffrent hours
        LocalDateTime localDateTime = LocalDateTime.of(2024, 3, 6, 10, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, 3, 6, 11, 0);
        LocalDateTime localDateTime3 = LocalDateTime.of(2024, 3, 8, 10, 0);
        Date date1 = Date.valueOf(localDateTime.toLocalDate());
        Date date2 = Date.valueOf(localDateTime2.toLocalDate());
        Date date3 = Date.valueOf(localDateTime3.toLocalDate());

        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);
        ownerRepository.save(owner);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        ClassType tennis = new ClassType("tennis", "heh class", 0, true, owner);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);

        SpecificClass specificClass = new SpecificClass(date1, null, null, 0, yoga, null);
        SpecificClass specificClass2 = new SpecificClass(date2, null, null, 0, tennis, null);
        SpecificClass specificClass3 = new SpecificClass(date3, null, null, 0, yoga, null);
        SpecificClass specificClass4 = new SpecificClass(date2, null, null, 0, tennis, null);

        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);
        specificClassRepository.save(specificClass3);
        specificClassRepository.save(specificClass4);

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
        // creates 8 local date objects and sets them to starting and ending times of
        // specific classes
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
        ownerRepository.save(owner);

        Instructor supervisor1 = new Instructor("kyle@gmail.com", "kyle", "1234", "elyk", 0);
        Instructor supervisor2 = new Instructor("leandro@gmail.com", "leandro", "12564", "ordnael", 0);
        instructorRepository.save(supervisor1);
        instructorRepository.save(supervisor2);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        ClassType tennis = new ClassType("tennis", "heh class", 0, true, owner);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);

        SpecificClass specificClass = new SpecificClass(date1start, start1, end1, 0, yoga, null);
        SpecificClass specificClass2 = new SpecificClass(date2start, start2, end2, 0, tennis, null);
        SpecificClass specificClass3 = new SpecificClass(date3start, start3, end3, 0, tennis, null);
        SpecificClass specificClass4 = new SpecificClass(date4start, start4, end4, 0, yoga, null);

        specificClass.setSupervisor(supervisor1);
        specificClass2.setSupervisor(supervisor2);
        specificClass3.setSupervisor(supervisor1);
        specificClass4.setSupervisor(supervisor2);

        // saves the specific classes into the database
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

    @Test
    @Transactional
    public void findByDateAndStartTime() {
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
        ownerRepository.save(owner);

        Instructor supervisor1 = new Instructor("kyle@gmail.com", "kyle", "1234", "elyk", 0);
        Instructor supervisor2 = new Instructor("leandro@gmail.com", "leandro", "12564", "ordnael", 0);
        instructorRepository.save(supervisor1);
        instructorRepository.save(supervisor2);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        ClassType tennis = new ClassType("tennis", "heh class", 0, true, owner);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);

        SpecificClass specificClass = new SpecificClass(date1start, start1, end1, 0, yoga, null);
        SpecificClass specificClass2 = new SpecificClass(date2start, start2, end2, 0, tennis, null);
        SpecificClass specificClass3 = new SpecificClass(date3start, start3, end3, 0, tennis, null);
        SpecificClass specificClass4 = new SpecificClass(date4start, start4, end4, 0, yoga, null);

        specificClass.setSupervisor(supervisor1);
        specificClass2.setSupervisor(supervisor2);
        specificClass3.setSupervisor(supervisor1);
        specificClass4.setSupervisor(supervisor2);

        // saves the specific classes into the database
        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);
        specificClassRepository.save(specificClass3);
        specificClassRepository.save(specificClass4);

        SpecificClass foundByDate1 = specificClassRepository.findByDateAndStartTime(date1start, start1);
        SpecificClass foundByDate2 = specificClassRepository.findByDateAndStartTime(date2start, start2);
        SpecificClass foundByDate3 = specificClassRepository.findByDateAndStartTime(date3start, start3);
        SpecificClass foundByDate4 = specificClassRepository.findByDateAndStartTime(date4start, start4);

        assertEquals(foundByDate1, specificClass);
        assertEquals(foundByDate2, specificClass2);
        assertEquals(foundByDate3, specificClass3);
        assertEquals(foundByDate4, specificClass4);

    }

    @Test
    @Transactional
    public void findBySupervisorIsNotNull() {
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
        ownerRepository.save(owner);

        Instructor supervisor1 = new Instructor("kyle@gmail.com", "kyle", "1234", "elyk", 0);
        Instructor supervisor2 = new Instructor("leandro@gmail.com", "leandro", "12564", "ordnael", 0);
        instructorRepository.save(supervisor1);
        instructorRepository.save(supervisor2);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        ClassType tennis = new ClassType("tennis", "heh class", 0, true, owner);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);

        SpecificClass specificClass = new SpecificClass(date1start, start1, end1, 0, yoga, null);
        SpecificClass specificClass2 = new SpecificClass(date2start, start2, end2, 0, tennis, null);
        SpecificClass specificClass3 = new SpecificClass(date3start, start3, end3, 0, tennis, null);
        SpecificClass specificClass4 = new SpecificClass(date4start, start4, end4, 0, yoga, null);

        specificClass.setSupervisor(supervisor1);
        specificClass3.setSupervisor(supervisor1);
        specificClass4.setSupervisor(supervisor2);

        // saves the specific classes into the database
        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);
        specificClassRepository.save(specificClass3);
        specificClassRepository.save(specificClass4);

        List<SpecificClass> found = specificClassRepository.findBySupervisorIsNotNull();

        assertEquals(3, found.size());
        assertThat(found).contains(specificClass, specificClass3, specificClass4);
    }

    @Test
    @Transactional
    public void findAll() {
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
        ownerRepository.save(owner);

        Instructor supervisor1 = new Instructor("kyle@gmail.com", "kyle", "1234", "elyk", 0);
        Instructor supervisor2 = new Instructor("leandro@gmail.com", "leandro", "12564", "ordnael", 0);
        instructorRepository.save(supervisor1);
        instructorRepository.save(supervisor2);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        ClassType tennis = new ClassType("tennis", "heh class", 0, true, owner);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);

        SpecificClass specificClass = new SpecificClass(date1start, start1, end1, 0, yoga, null);
        SpecificClass specificClass2 = new SpecificClass(date2start, start2, end2, 0, tennis, null);
        SpecificClass specificClass3 = new SpecificClass(date3start, start3, end3, 0, tennis, null);
        SpecificClass specificClass4 = new SpecificClass(date4start, start4, end4, 0, yoga, null);

        specificClass.setSupervisor(supervisor1);
        specificClass3.setSupervisor(supervisor1);
        specificClass4.setSupervisor(supervisor2);

        // saves the specific classes into the database
        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);
        specificClassRepository.save(specificClass3);
        specificClassRepository.save(specificClass4);

        List<SpecificClass> found = specificClassRepository.findAll();

        assertEquals(4, found.size());
        assertThat(found).contains(specificClass, specificClass2, specificClass3, specificClass4);

    }

    @Test
    @Transactional
    public void deleteByClassType() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 3, 6, 10, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, 3, 6, 11, 0);
        LocalDateTime localDateTime3 = LocalDateTime.of(2024, 3, 8, 10, 0);
        Date date1 = Date.valueOf(localDateTime.toLocalDate());
        Date date2 = Date.valueOf(localDateTime2.toLocalDate());
        Date date3 = Date.valueOf(localDateTime3.toLocalDate());

        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);
        ownerRepository.save(owner);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        ClassType tennis = new ClassType("tennis", "heh class", 0, true, owner);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);

        SpecificClass specificClass = new SpecificClass(date1, null, null, 0, yoga, null);
        SpecificClass specificClass2 = new SpecificClass(date2, null, null, 0, tennis, null);
        SpecificClass specificClass3 = new SpecificClass(date3, null, null, 0, yoga, null);
        SpecificClass specificClass4 = new SpecificClass(date2, null, null, 0, tennis, null);

        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);
        specificClassRepository.save(specificClass3);
        specificClassRepository.save(specificClass4);

        specificClassRepository.deleteByClassType(yoga);
        List<SpecificClass> found = specificClassRepository.findAll();
        assertEquals(2, found.size());
        assertThat(found).contains(specificClass2, specificClass4);

        specificClassRepository.deleteByClassType(tennis);
        List<SpecificClass> found1 = specificClassRepository.findAll();
        assertEquals(0, found1.size());
    }

    @Test
    @Transactional
    public void deleteBySupervisor() {
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
        ownerRepository.save(owner);

        Instructor supervisor1 = new Instructor("kyle@gmail.com", "kyle", "1234", "elyk", 0);
        Instructor supervisor2 = new Instructor("leandro@gmail.com", "leandro", "12564", "ordnael", 0);
        instructorRepository.save(supervisor1);
        instructorRepository.save(supervisor2);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        ClassType tennis = new ClassType("tennis", "heh class", 0, true, owner);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);

        SpecificClass specificClass = new SpecificClass(date1start, start1, end1, 0, yoga, null);
        SpecificClass specificClass2 = new SpecificClass(date2start, start2, end2, 0, tennis, null);
        SpecificClass specificClass3 = new SpecificClass(date3start, start3, end3, 0, tennis, null);
        SpecificClass specificClass4 = new SpecificClass(date4start, start4, end4, 0, yoga, null);

        specificClass.setSupervisor(supervisor1);
        specificClass2.setSupervisor(supervisor2);
        specificClass3.setSupervisor(supervisor1);
        specificClass4.setSupervisor(supervisor2);

        // saves the specific classes into the database
        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);
        specificClassRepository.save(specificClass3);
        specificClassRepository.save(specificClass4);

        specificClassRepository.deleteBySupervisor(supervisor1);
        List<SpecificClass> found = specificClassRepository.findAll();
        assertEquals(2, found.size());
        assertThat(found).contains(specificClass2, specificClass4);

        specificClassRepository.deleteBySupervisor(supervisor2);
        List<SpecificClass> found2 = specificClassRepository.findAll();
        assertEquals(0, found2.size());

    }

    @Test
    @Transactional
    public void deleteByDate() {
        LocalDateTime localDateTime1 = LocalDateTime.of(2024, 3, 6, 10, 0);
        LocalDateTime localDateTime11 = LocalDateTime.of(2024, 3, 6, 11, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, 3, 7, 11, 0);
        LocalDateTime localDateTime22 = LocalDateTime.of(2024, 3, 7, 12, 0);
        LocalDateTime localDateTime3 = LocalDateTime.of(2024, 3, 8, 10, 0);
        LocalDateTime localDateTime33 = LocalDateTime.of(2024, 3, 8, 11, 0);
        LocalDateTime localDateTime4 = LocalDateTime.of(2024, 3, 9, 11, 0);
        LocalDateTime localDateTime44 = LocalDateTime.of(2024, 3, 9, 12, 0);

        Date date1start = Date.valueOf(localDateTime1.toLocalDate());
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
        ownerRepository.save(owner);

        Instructor supervisor1 = new Instructor("kyle@gmail.com", "kyle", "1234", "elyk", 0);
        Instructor supervisor2 = new Instructor("leandro@gmail.com", "leandro", "12564", "ordnael", 0);
        instructorRepository.save(supervisor1);
        instructorRepository.save(supervisor2);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        ClassType tennis = new ClassType("tennis", "heh class", 0, true, owner);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);

        SpecificClass specificClass = new SpecificClass(date1start, start1, end1, 0, yoga, null);
        SpecificClass specificClass2 = new SpecificClass(date1start, start2, end2, 0, tennis, null);
        SpecificClass specificClass3 = new SpecificClass(date3start, start3, end3, 0, tennis, null);
        SpecificClass specificClass4 = new SpecificClass(date4start, start4, end4, 0, yoga, null);

        specificClass.setSupervisor(supervisor1);
        specificClass3.setSupervisor(supervisor1);
        specificClass4.setSupervisor(supervisor2);

        // saves the specific classes into the database
        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);
        specificClassRepository.save(specificClass3);
        specificClassRepository.save(specificClass4);

        specificClassRepository.deleteByDate(date1start);
        List<SpecificClass> found = specificClassRepository.findAll();
        assertEquals(2, found.size());
        assertThat(found).contains(specificClass3, specificClass4);

        specificClassRepository.deleteByDate(date3start);
        List<SpecificClass> found2 = specificClassRepository.findAll();
        assertEquals(1, found2.size());
        assertThat(found2).contains(specificClass4);

        specificClassRepository.deleteByDate(date4start);
        List<SpecificClass> found3 = specificClassRepository.findAll();
        assertEquals(0, found3.size());

    }

    @Test
    @Transactional
    void testfindBySupervisorIsNotNullAndDateAfterOrDateEqualsAndStartTimeAfter() {
        // Set up current date and time for the test
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Format to java.sql.Date and java.sql.Time\
        LocalDate tomorrow = currentDate.plusDays(1);
        Date sqlTomorrow = Date.valueOf(tomorrow);
        Date sqlCurrentDate = Date.valueOf(currentDate);
        Time sqlCurrentTime = Time.valueOf(currentTime);

        // Creation of an instructor
        Instructor supervisor = new Instructor("email@email.com", "John", "password", "Doe", 0);
        instructorRepository.save(supervisor);

        // Creation of owner
        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);
        ownerRepository.save(owner);

        // Creation of ClassType
        ClassType classType = new ClassType("Football", "Best sport ", 0, true, owner);
        classTypeRepository.save(classType);

        // Example test data creation
        SpecificClass unavailableClass = new SpecificClass();
        unavailableClass.setClassType(classType);
        unavailableClass.setDate(Date.valueOf("2024-02-18")); // In the past
        unavailableClass.setStartTime(Time.valueOf("10:00:00")); // Start time before current time
        unavailableClass.setSupervisor(supervisor); // With supervisor

        SpecificClass availableClass = new SpecificClass();
        unavailableClass.setClassType(classType);
        availableClass.setDate(sqlTomorrow); // Class tomorrow
        availableClass.setStartTime(Time.valueOf("20:00:00")); // Start time after current time
        availableClass.setSupervisor(supervisor); // With supervisor

        // Add to repository and run
        // findBySupervisorIsNotNullAndDateAfterOrDateEqualsAndStartTimeAfter
        specificClassRepository.save(unavailableClass);
        specificClassRepository.save(availableClass);

        List<SpecificClass> classes = specificClassRepository
                .findBySupervisorIsNotNullAndDateAfterOrDateEqualsAndStartTimeAfter(sqlCurrentDate, sqlCurrentTime);

        assertTrue(classes.contains(availableClass));
    }

}
