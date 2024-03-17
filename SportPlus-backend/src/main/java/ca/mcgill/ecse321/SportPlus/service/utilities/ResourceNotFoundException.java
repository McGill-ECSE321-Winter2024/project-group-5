package ca.mcgill.ecse321.SportPlus.service.utilities;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
