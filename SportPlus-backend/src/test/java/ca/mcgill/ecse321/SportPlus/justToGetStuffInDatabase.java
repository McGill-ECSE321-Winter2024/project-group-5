// REMOVE THIS FILE
// REMOVE THIS FILE
// REMOVE THIS FILE
// REMOVE THIS FILE
// REMOVE THIS FILE
// REMOVE THIS FILE
// REMOVE THIS FILE
// REMOVE THIS FILE
// REMOVE THIS FILE
// REMOVE THIS FILE
// REMOVE THIS FILE
// REMOVE THIS FILE
// REMOVE THIS FILE
// REMOVE THIS FILE
// REMOVE THIS FILE



package ca.mcgill.ecse321.SportPlus;
// remove this file
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.sql.Date;
import java.time.LocalDateTime;
import java.sql.Time;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.model.Instructor;

@SpringBootTest
public class justToGetStuffInDatabase {
   @Autowired
    private SpecificClassRepository specificClassRepository;

    @Autowired
    private ClassTypeRepository classTypeRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void clearDatabase() {
        // Clear all the tables used for the tests
        specificClassRepository.deleteAll();
        classTypeRepository.deleteAll();
        ownerRepository.deleteAll();
        instructorRepository.deleteAll();
    }
    
    @Test 
    public void yourmom(){
 // specific classes
        LocalDateTime localDateTime1 = LocalDateTime.of(2024, 3, 6, 16, 0);
        LocalDateTime localDateTime11 = LocalDateTime.of(2023, 3, 6, 17, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(2025, 3, 7, 11, 0);
        LocalDateTime localDateTime22 = LocalDateTime.of(2025, 3, 7, 12, 0);
        LocalDateTime localDateTime3 = LocalDateTime.of(2025, 3, 8, 10, 0);
        LocalDateTime localDateTime33 = LocalDateTime.of(2025, 3, 8, 11, 0);
        LocalDateTime localDateTime4 = LocalDateTime.of(2025, 3, 9, 11, 0);
        LocalDateTime localDateTime44 = LocalDateTime.of(2025, 3, 9, 12, 0);

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
        ClassType yourmom = new ClassType("tennis", "heh class", 0, false, owner);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);

        SpecificClass specificClass = new SpecificClass(date1start, start1, end1, 0, yoga, "tempy");
        SpecificClass specificClass6 = new SpecificClass(date1start, start2, end1, 0, yoga, "tempy");
        SpecificClass specificClass7 = new SpecificClass(date1start, start3, end1, 0, yoga, "tempy");
        SpecificClass specificClass8 = new SpecificClass(date1start, start1, end1, 0, yoga, "tempy");
        SpecificClass specificClass9 = new SpecificClass(date1start, start4, end1, 0, yoga, "tempy");
        SpecificClass specificClass2 = new SpecificClass(date2start, start2, end2, 0, tennis, "temp");
        SpecificClass specificClass3 = new SpecificClass(date3start, start3, end3, 0, tennis, "yeehaw");
        SpecificClass specificClass4 = new SpecificClass(date4start, start4, end4, 0, yoga, "oops");

        specificClass.setSupervisor(supervisor1);
        specificClass7.setSupervisor(supervisor2);
        specificClass8.setSupervisor(supervisor1);
        specificClass2.setSupervisor(supervisor2);
        specificClass4.setSupervisor(supervisor2);

        // saves the specific classes into the database
        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);
        specificClassRepository.save(specificClass3);
        specificClassRepository.save(specificClass4);
        specificClassRepository.save(specificClass6);
        specificClassRepository.save(specificClass7);
        specificClassRepository.save(specificClass8);
        specificClassRepository.save(specificClass9);

    }
}