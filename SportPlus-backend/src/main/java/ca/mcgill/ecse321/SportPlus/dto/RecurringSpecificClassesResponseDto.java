package ca.mcgill.ecse321.SportPlus.dto;

import java.util.List;

public class RecurringSpecificClassesResponseDto {
    private List<SpecificClassResponseDto> specificClasses;

    @SuppressWarnings("unused")
    public RecurringSpecificClassesResponseDto() {
    }

    // Constructor
    public RecurringSpecificClassesResponseDto(List<SpecificClassResponseDto> specificClasses) {
        this.specificClasses = specificClasses;
    }

    // Getter
    public List<SpecificClassResponseDto> getSpecificClasses() {
        return specificClasses;
    }

    // Setter
    public void setSpecificClasses(List<SpecificClassResponseDto> specificClasses) {
        this.specificClasses = specificClasses;
    }
}
