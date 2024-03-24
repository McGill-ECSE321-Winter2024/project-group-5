package ca.mcgill.ecse321.SportPlus.dto;

import java.sql.Date;

public class RecurringSpecificClassRequestDto extends SpecificClassRequestsDto {

    // Fields not present in the normal SpecificClass
    private Date endDate;
    private int dayOfWeek; // 1 = Monday, 7 = Sunday

    // Constructor calling the SpecificClassRequestsDto
    public RecurringSpecificClassRequestDto() {
        super();
    }

    // Getters
    public Date getEndDate() {
        return endDate;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    // Setters
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

}
