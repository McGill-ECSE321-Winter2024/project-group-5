package ca.mcgill.ecse321.SportPlus.dto;

import java.util.List;

public class RecurringSpecificClassesResponseDto {
    private List<SpecificClassResponseDto> specificClasses;

    @SuppressWarnings("unused")
    public RecurringSpecificClassesResponseDto() {
    }

    public RecurringSpecificClassesResponseDto(List<SpecificClassResponseDto> specificClasses) {
        this.specificClasses = specificClasses;
    }

    public List<SpecificClassResponseDto> getSpecificClasses() {
        return specificClasses;
    }

    public void setSpecificClasses(List<SpecificClassResponseDto> specificClasses) {
        this.specificClasses = specificClasses;
    }
}
