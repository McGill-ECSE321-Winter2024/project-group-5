package ca.mcgill.ecse321.SportPlus.dto;

public class ClassTypeRequestDto {

    private String name;
    private String description;
    private Boolean approved; // Optional, based on business logic
    private OwnerRequestDto approver; // Assuming the approver's identification can be provided by accountId

    public ClassTypeRequestDto() {
    }

    public ClassTypeRequestDto(String name, String description, Boolean approved, OwnerRequestDto approver) {
        this.name = name;
        this.description = description;
        this.approved = approved;
        this.approver = approver;
    }

    // Getters and setters

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

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public OwnerRequestDto getApproverAccountId() {
        return approver;
    }

    public void setApproverAccountId(OwnerRequestDto approverAccountId) {
        this.approver = approverAccountId;
    }
}
