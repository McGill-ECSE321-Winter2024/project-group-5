package ca.mcgill.ecse321.SportPlus.dto;

import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.dto.InstructorListDto;
import ca.mcgill.ecse321.SportPlus.dto.InstructorResponseDto;

import java.sql.Date;
import java.sql.Time;

public class SpecificClassResponseDto {

    private Integer id;
    private Date date;
    private Time startTime;
    private Time endTime;
    private InstructorResponseDto supervisor;
    private ClassTypeDto classType;

    // Constructors, Getters, and Setters

    public SpecificClassResponseDto(SpecificClass specificclass) {
        this.id = specificclass.getSessionId();
        this.date = specificclass.getDate();
        this.startTime = specificclass.getStartTime();
        this.endTime = specificclass.getEndTime();
        this.supervisor = new InstructorResponseDto(specificclass.getSupervisor());
        this.classType = new ClassTypeDto(specificclass.getClassType()); 

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

    public InstructorResponseDto getInstructor() {
        return supervisor;
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

    public void setInstructor(InstructorResponseDto instructor) {
        this.supervisor = instructor;
    }

    public void setClassType(ClassTypeDTO classType) {
        this.classType = classType;
    }
}
