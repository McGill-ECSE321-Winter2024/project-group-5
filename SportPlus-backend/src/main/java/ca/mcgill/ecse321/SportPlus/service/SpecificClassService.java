package ca.mcgill.ecse321.SportPlus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Instructor;
import ca.mcgill.ecse321.SportPlus.model.Registration;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

@Service
public class SpecificClassService {

    @Autowired
    SpecificClassRepository specificClassRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    ClassTypeRepository classTypeRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    @Transactional
    public SpecificClass createSpecificClass(Date date, Time startTime, Time endTime, int instructorId,
            int classTypeId) {
        // Validate input parameters...
        if (date.before(new java.util.Date())) {
            throw new IllegalArgumentException("The date must be in the future");
        }

        if (startTime.after(endTime)) {
            throw new IllegalArgumentException("The start time should come before the end time");
        }

        // Create the SpecificClass
        Instructor instructor = instructorRepository.findByAccountId(instructorId);
        ClassType classType = classTypeRepository.findByTypeId(classTypeId);
        SpecificClass specificClass = new SpecificClass(date, startTime, endTime, 0, classType, null);
        specificClass.setSupervisor(instructor);

        // Save the new SpecificClass
        specificClassRepository.save(specificClass);

        // Return it
        return specificClass;
    }

    // Creates Recurring Classes on a specifc day of the week.
    // Example yoga on Monday from 10 to 11 for 1 month
    @Transactional
    public List<SpecificClass> createRecurringSpecificClasses(Date startDate, Date endDate, Time startTime,
            Time endTime, int dayOfWeek, int instructorId, int classTypeId) {

        List<SpecificClass> recurringClasses = new ArrayList<>();

        // Sets the dates and times
        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();
        LocalTime startTim = startTime.toLocalTime();
        LocalTime endTim = endTime.toLocalTime();

        // Finds the instrucotr and classType for that class
        Instructor instructor = instructorRepository.findByAccountId(instructorId);
        ClassType classType = classTypeRepository.findByTypeId(classTypeId);

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) { // Iterates through all the days in
                                                                                    // the range
            if (date.getDayOfWeek().getValue() == dayOfWeek) {// If the day of the week matches the date add that class
                                                              // with the date to the list

                SpecificClass specificClass = new SpecificClass(java.sql.Date.valueOf(date),
                        java.sql.Time.valueOf(startTim), java.sql.Time.valueOf(endTim), 0, classType, null);
                specificClass.setSupervisor(instructor); // Set the instructor separately
                specificClassRepository.save(specificClass); // Save the SpecificClass instance
                recurringClasses.add(specificClass); // Add to the list
            }
        }

        // Returns them
        return recurringClasses;

    }

    @Transactional
    public SpecificClass updateDateSpecificClass(int sessionId, Date newDate) {
        // Find the current class
        SpecificClass specificClass = specificClassRepository.findBySessionId(sessionId);

        if (newDate.before(new java.util.Date())) {
            throw new IllegalArgumentException("The new date cannot be in the past.");
        }
        specificClass.setDate(newDate); // Update the date
        specificClassRepository.save(specificClass); // Save to the DB
        return specificClass; // Return the obj

    }

    @Transactional
    public SpecificClass updateTimeSpecificClass(int sessionId, Time newStartTime, Time newEndTime) {

        // Find the SpecificClass
        SpecificClass specificClass = specificClassRepository.findBySessionId(sessionId);

        // Validate the parameter
        if (newStartTime.after(newEndTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        // Update the times of the new class
        specificClass.setStartTime(newStartTime);
        specificClass.setEndTime(newEndTime);
        specificClassRepository.save(specificClass);

        // Returns it
        return specificClass;
    }

    @Transactional
    public SpecificClass updateClassTypeSpecificClass(int sessionId, int classTypeId) {

        // Setup
        SpecificClass specificClass = specificClassRepository.findBySessionId(sessionId);
        ClassType classType = classTypeRepository.findByTypeId(classTypeId);

        // Update the classType
        specificClass.setClassType(classType);

        // Save in the DB
        specificClassRepository.save(specificClass);

        // Return it
        return specificClass;
    }

    @Transactional
    public SpecificClass assignInstructorSpecificClass(int sessionId, int instructorId) {

        // Find the SpecifcClass & Instrcutor
        SpecificClass specificClass = specificClassRepository.findBySessionId(sessionId);
        Instructor instructor = instructorRepository.findByAccountId(instructorId);

        // Set the instructor to the Clas & save it
        specificClass.setSupervisor(instructor);
        specificClassRepository.save(specificClass);

        // Return the class
        return specificClass;
    }

    @Transactional
    public SpecificClass removeInstructorSpecificClass(int sessionId) {

        // Find the SpecificClass
        SpecificClass specificClass = specificClassRepository.findBySessionId(sessionId);

        // Set supervisor to null & save the class
        specificClass.setSupervisor(null);
        specificClassRepository.save(specificClass);

        // Return it
        return specificClass;
    }

    @Transactional
    public List<SpecificClass> getByInstructor(int instructorId) {

        // Find the instructor
        Instructor instructor = instructorRepository.findByAccountId(instructorId);

        // Find classes with that instructor & return them
        List<SpecificClass> classes = specificClassRepository.findBySupervisor(instructor);
        return classes;
    }

    @Transactional
    public List<SpecificClass> getByDate(Date date) {

        // Find classes by Date and return them
        List<SpecificClass> classes = specificClassRepository.findByDate(date);
        return classes;
    }

    @Transactional
    public List<SpecificClass> getByClassType(int classTypeId) {

        // Find the classType
        ClassType classType = classTypeRepository.findByTypeId(classTypeId);

        // Find all the classes with that classType & return them
        List<SpecificClass> classes = specificClassRepository.findByClassType(classType);
        return classes;
    }

    @Transactional
    public List<SpecificClass> getByDateRange(Date startDate, Date endDate) {

        // Find all classes between a certain date range & return them
        List<SpecificClass> classes = specificClassRepository.findClassesBetweenDates(startDate, endDate);
        return classes;
    }

    @Transactional
    public List<SpecificClass> getAvailable() {

        // Current date and time
        LocalDate todayLocalDate = LocalDate.now();
        LocalTime nowLocalTime = LocalTime.now();

        // Convert LocalDate and LocalTime to java.sql.Date and java.sql.Time
        Date today = Date.valueOf(todayLocalDate);
        Time now = Time.valueOf(nowLocalTime);

        // Get all the classes in the future with a supervisor
        List<SpecificClass> futureClassesWithSupervisor = specificClassRepository
                .findBySupervisorIsNotNullAndDateAfterOrDateEqualsAndStartTimeAfter(today, now);

        List<SpecificClass> availableClasses = new ArrayList<>();

        for (SpecificClass specificClass : futureClassesWithSupervisor) {
            List<Registration> registrations = registrationRepository.findBySpecificClass(specificClass);

            if (registrations.size() < 30) {
                // specifcClass with a supervisor and less than 30 registrations

                availableClasses.add(specificClass);
            }
        }

        // Retunr all available classes
        return availableClasses;
    }

    @Transactional
    public SpecificClass getByDateAndStartTime(Date date, Time startTime) {

        // Find the class with the ddate and startime & retunr it
        SpecificClass specificClasses = specificClassRepository.findByDateAndStartTime(date, startTime);
        return specificClasses;
    }

    @Transactional
    public List<SpecificClass> getAll() {

        // Find & return all classes
        List<SpecificClass> specificClasses = specificClassRepository.findAll();
        return specificClasses;

    }

    @Transactional
    public void deleteByClassType(int classTypeId) {

        // Find the classtype
        ClassType classType = classTypeRepository.findByTypeId(classTypeId);

        // Delete all classes with that classType
        specificClassRepository.deleteByClassType(classType);
    }
    @Transactional
    public void deleteBySessionId(int sessionId) {

        // Delete all classes with that classType
        specificClassRepository.deleteBySessionId(sessionId);
    }

}
