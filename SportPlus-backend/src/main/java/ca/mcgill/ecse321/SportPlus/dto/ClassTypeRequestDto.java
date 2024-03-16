package ca.mcgill.ecse321.SportPlus.dto;

public class ClassTypeRequestDto {

    private String name;
    private String description;
    private Boolean approved; // Optional, based on business logic
    private Integer approverAccountId; // Assuming the approver's identification can be provided by accountId

    public ClassTypeRequestDto() {
    }
    public ClassTypeRequestDto(String name, String description, Boolean approved, Integer approverAccountId) {
        this.name = name;
        this.description = description;
        this.approved = approved;
        this.approverAccountId = approverAccountId;
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

    public Integer getApproverAccountId() {
        return approverAccountId;
    }

    public void setApproverAccountId(Integer approverAccountId) {
        this.approverAccountId = approverAccountId;
    }
}
