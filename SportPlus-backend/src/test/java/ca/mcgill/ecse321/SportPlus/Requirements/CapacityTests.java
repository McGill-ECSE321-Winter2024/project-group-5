package ca.mcgill.ecse321.SportPlus.Requirements;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Time;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.LoginRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.dto.LoginRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.LoginResponseDto;
import ca.mcgill.ecse321.SportPlus.model.Instructor;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CapacityTests {

    @Autowired
    private TestRestTemplate client;

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

    private static final String LOGIN_TYPE = "INSTRUCTOR";
    private static final String LOGIN_PASSWORD = "Pass123";
    private static final Time LOGIN_CURRENTTIME = Time.valueOf("10:30:00");

    private static final String INSTRUCTOR_FISTNAME = "John";
    private static final String INSTRUCTOR_LASTNAME = "Doe";
    private int INSTRUCTOR_VALID_ACCOUNTID = 0;

    private CyclicBarrier barrier;

    @Test
    public void test40UserCapacity() throws InterruptedException, BrokenBarrierException {
        int numThreads = 50;

        // Initialize barrier with the appropriate count
        barrier = new CyclicBarrier(numThreads);

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 1; i <= numThreads; i++) {
            // Modify email for each iteration
            String instructorEmail = "instructor" + i + "@sportplus.com";

            // create a new thread to handle each request
            executor.execute(() -> {
                try {
                    // Wait for all threads to reach the barrier
                    barrier.await();

                    // create Instructor and save to repo
                    Instructor instructor = new Instructor(instructorEmail, INSTRUCTOR_FISTNAME, LOGIN_PASSWORD,
                            INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                    instructorRepository.save(instructor);

                    // verify that instructor indeed exists in repo
                    instructor = instructorRepository.findInstructorByEmail(instructorEmail);
                    assertNotNull(instructor);

                    // create login request
                    LoginRequestDto request = new LoginRequestDto(LOGIN_TYPE, instructorEmail, LOGIN_PASSWORD,
                            LOGIN_CURRENTTIME);

                    // attempt login through rest controller
                    ResponseEntity<LoginResponseDto> response = client.postForEntity("/login", request,
                            LoginResponseDto.class);

                    // verify that response is expected response
                    assertNotNull(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // Shut down the executor
        executor.shutdown();

        // Wait a moment to ensure all threads are ready
        Thread.sleep(1000);

        // Wait for all tasks to finish
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        // asserts that there are at least 40 login in the system
        assertTrue(loginRepository.findAll().size() >= 40);
    }
    
}
