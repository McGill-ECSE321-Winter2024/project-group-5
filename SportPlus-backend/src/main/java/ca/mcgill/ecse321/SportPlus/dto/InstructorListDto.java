package ca.mcgill.ecse321.SportPlus.dto;

import java.util.List; 

public class InstructorListDto {

    private List<InstructorResponseDto> instructors;

    public InstructorListDto(List<InstructorResponseDto> instructors) {
        this.instructors = instructors;
    }

    public List<InstructorResponseDto> getInstructors() {
        return instructors;
    }

    public void setPeople(List<InstructorResponseDto> instructors) {
        this.instructors = instructors;
    }
    
}
