package ca.mcgill.ecse321.SportPlus.dto;

import java.sql.Date;
import java.sql.Time;

public class SpecificClassRequestsDto {

    private Date date;
    private Time startTime;
    private Time endTime;
    private Integer instructorId;
    private Integer classTypeId;

    public SpecificClassRequestsDto() {
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

    public Integer getInstructorId() {
        return instructorId;
    }

    public Integer getClassTypeId() {
        return classTypeId;
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

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public void setClassTypeId(Integer classTypeId) {
        this.classTypeId = classTypeId;
    }

}
