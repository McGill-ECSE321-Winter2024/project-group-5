package ca.mcgill.ecse321.SportPlus.dto;

import ca.mcgill.ecse321.SportPlus.model.ClassType;

public class ClassTypeResponseDto {

    private int typeId;
    private String name;
    private String description;
    private boolean approved;
    private OwnerResponseDto approver; // Assuming OwnerDto is already defined elsewhere

    public ClassTypeResponseDto() {
    }

    public ClassTypeResponseDto(ClassType classType) {
        this.typeId = classType.getTypeId();
        this.name = classType.getName();
        this.description = classType.getDescription();
        this.approved = classType.getApproved();
        if (classType.getApprover() != null) {
            // This assumes that OwnerDto has a constructor that accepts an Owner object
            this.approver = new OwnerResponseDto(classType.getApprover());
        }
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public OwnerResponseDto getApprover() {
        return approver;
    }

    public void setApprover(OwnerResponseDto approver) {
        this.approver = approver;
    }
}
