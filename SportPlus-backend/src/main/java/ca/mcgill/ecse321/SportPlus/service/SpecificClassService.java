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

        Instructor instructor = instructorRepository.findByAccountId(instructorId);
        ClassType classType = classTypeRepository.findByTypeId(classTypeId);
        SpecificClass specificClass = new SpecificClass(date, startTime, endTime, 0, classType);
        specificClass.setSupervisor(instructor);

        // SAve the new SpecificClass
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

        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();
        LocalTime startTim = startTime.toLocalTime();
        LocalTime endTim = endTime.toLocalTime();

        Instructor instructor = instructorRepository.findByAccountId(instructorId);
        ClassType classType = classTypeRepository.findByTypeId(classTypeId);

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) { // Iterates through all the days in
                                                                                    // the range
            if (date.getDayOfWeek().getValue() == dayOfWeek) {// If the day of the week matches the date add that class
                                                              // with the date to the list

                SpecificClass specificClass = new SpecificClass(java.sql.Date.valueOf(date),
                        java.sql.Time.valueOf(startTim), java.sql.Time.valueOf(endTim), 0, classType);
                specificClass.setSupervisor(instructor); // Set the instructor separately
                specificClassRepository.save(specificClass); // Save the SpecificClass instance
                recurringClasses.add(specificClass); // Add to the list
            }
        }

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
        return specificClass; // Return thr obj

    }

    @Transactional
    public SpecificClass updateTimeSpecificClass(int sessionId, Time newStartTime, Time newEndTime) {
        SpecificClass specificClass = specificClassRepository.findBySessionId(sessionId);

        if (newStartTime.after(newEndTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        specificClass.setStartTime(newStartTime);
        specificClass.setEndTime(newEndTime);
        specificClassRepository.save(specificClass);

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

        return specificClass;
    }

    @Transactional
    public SpecificClass assignInstructorSpecificClass(int sessionId, int instructorId) {
        SpecificClass specificClass = specificClassRepository.findBySessionId(sessionId);
        Instructor instructor = instructorRepository.findByAccountId(instructorId);

        specificClass.setSupervisor(instructor);
        specificClassRepository.save(specificClass);
        return specificClass;
    }

    @Transactional
    public SpecificClass removeInstructorSpecificClass(int sessionId) {
        SpecificClass specificClass = specificClassRepository.findBySessionId(sessionId);

        specificClass.setSupervisor(null);
        specificClassRepository.save(specificClass);
        return specificClass;
    }

    @Transactional
    public List<SpecificClass> getByInstructor(int instructorId) {
        Instructor instructor = instructorRepository.findByAccountId(instructorId);

        List<SpecificClass> classes = specificClassRepository.findBySupervisor(instructor);
        return classes;
    }

    @Transactional
    public List<SpecificClass> getByDate(Date date) {
        List<SpecificClass> classes = specificClassRepository.findByDate(date);
        return classes;
    }

    @Transactional
    public List<SpecificClass> getByClassType(int classTypeId) {
        ClassType classType = classTypeRepository.findByTypeId(classTypeId);

        List<SpecificClass> classes = specificClassRepository.findByClassType(classType);
        return classes;
    }

    @Transactional
    public List<SpecificClass> getByDateRange(Date startDate, Date endDate) {
        List<SpecificClass> classes = specificClassRepository.findClassesBetweenDates(startDate, endDate);
        return classes;
    }

    @Transactional
    public List<SpecificClass> getAvailable() {

        Date now = new Date(System.currentTimeMillis()); // Current time
        List<SpecificClass> potentialClassesWithSupervisor = specificClassRepository
                .findBySupervisorIsNotNullAndStartTimeAfter(now);
        List<SpecificClass> availableClasses = new ArrayList<>();

        for (SpecificClass specificClass : potentialClassesWithSupervisor) {
            List<Registration> registrations = registrationRepository.findBySpecificClass(specificClass);

            if (registrations.size() < 30) {
                // specifcClass with a supervisor and less than 30 registrations

                availableClasses.add(specificClass);
            }
        }
        return availableClasses;
    }

    @Transactional
    public SpecificClass getByDateAndStartTime(Date date, Time startTime) {
        SpecificClass specificClasses = specificClassRepository.findByDateAndStartTime(date, startTime);
        return specificClasses;
    }

    @Transactional
    public List<SpecificClass> getAll() {
        List<SpecificClass> specificClasses = specificClassRepository.findAll();
        return specificClasses;

    }

    @Transactional
    public void deleteByClassType(int classTypeId) {
        ClassType classType = classTypeRepository.findByTypeId(classTypeId);
        specificClassRepository.deleteByClassType(classType);
    }

}
