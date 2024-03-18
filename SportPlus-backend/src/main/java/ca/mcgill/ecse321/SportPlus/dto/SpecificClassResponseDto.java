package ca.mcgill.ecse321.SportPlus.dto;

import ca.mcgill.ecse321.SportPlus.model.SpecificClass;

import java.sql.Date;
import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SpecificClassResponseDto {

    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date date;
    private Time startTime;
    private Time endTime;
    private InstructorResponseDto supervisor;
    private ClassTypeResponseDto classType;

    // Constructors, Getters, and Setters

    @SuppressWarnings("unused")
    public SpecificClassResponseDto() {
    }

    public SpecificClassResponseDto(SpecificClass specificclass) {
        this.id = specificclass.getSessionId();
        this.date = specificclass.getDate();
        this.startTime = specificclass.getStartTime();
        this.endTime = specificclass.getEndTime();
        this.classType = new ClassTypeResponseDto(specificclass.getClassType());
        if (specificclass.getSupervisor() != null) {
            this.supervisor = new InstructorResponseDto(specificclass.getSupervisor());
        } else {
            this.supervisor = null; // Or some default value that indicates no instructor
        }

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

    public ClassTypeResponseDto getClassType() {
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

    public void setClassType(ClassTypeResponseDto classType) {
        this.classType = classType;
    }
}
