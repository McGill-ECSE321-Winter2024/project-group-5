package ca.mcgill.ecse321.SportPlus.dto;
import java.util.List;

public class ClassTypeListDto {
    private List<ClassTypeResponseDto> classTypes;

    public ClassTypeListDto() {
    }

    public ClassTypeListDto(List<ClassTypeResponseDto> classTypes) {
        this.classTypes = classTypes;
    }

    public List<ClassTypeResponseDto> getClassTypes() {
        return classTypes;
    }

    public void setClassTypes(List<ClassTypeResponseDto> classTypes) {
        this.classTypes = classTypes;
    }

}