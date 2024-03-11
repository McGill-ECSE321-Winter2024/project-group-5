package ca.mcgill.ecse321.SportPlus.dto;

import java.sql.Date;
import java.sql.Time;

public class SpecificClassResponseDto {

    private Integer id;
    private Date date;
    private Time startTime;
    private Time endTime;
    private InstructorListDto instructor;
    private ClassTypeDto classType;

    // Constructors, Getters, and Setters

    public SpecificClassResponseDto() {
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public InstructorListDto getInstructor() {
        return instructor;
    }

    public ClassTypeDto getClassType() {
        return classType;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public void setInstructor(InstructorListDto instructor) {
        this.instructor = instructor;
    }

    public void setClassType(ClassTypeDTO classType) {
        this.classType = classType;
    }
}
