package ca.mcgill.ecse321.SportPlus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.service.utilities.HelperMethods;

@Service
public class OwnerService {

    @Autowired
    OwnerRepository ownerRepository;

    String ownerEmail = "owner@sportplus.com";

    // -----------Wrappers-----------//

    @Transactional
    public Owner getOwner() {
        return ownerRepository.findByEmail(ownerEmail);
    }

    // ------------EndWrappers----------//

    @Transactional
    public Owner createOwner() {

        // Creates an owner
        Owner owner = new Owner();
        owner.setEmail(ownerEmail);
        ownerRepository.save(owner);
        return owner;
    }

    @Transactional
    public Owner createOwner(String firstName, String password, String lastName) {

        // Input validation
        if (password == null || HelperMethods.PasswordCheck(password).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid password!");
        }
        if (firstName == null || firstName.trim().length() == 0) {
            throw new IllegalArgumentException("Owner first name cannot be empty!");
        }
        if (lastName == null || lastName.trim().length() == 0) {
            throw new IllegalArgumentException("Owner last name cannot be empty!");
        }
        if (getOwner() == null) { // Sets the attributes
            Owner owner = new Owner();
            owner.setEmail(ownerEmail);
            owner.setFirstName(firstName);
            owner.setPassword(password);
            owner.setLastName(lastName);
            ownerRepository.save(owner);
            return owner;
        } else {
            Owner owner = getOwner();
            owner.setEmail(ownerEmail);
            owner.setFirstName(firstName);
            owner.setPassword(password);
            owner.setLastName(lastName);
            ownerRepository.save(owner);
            return owner;
        }
    }

    @Transactional
    public Owner updateOwnerFirstName(String firstName) {

        // Get the owenr and validate
        Owner owner = getOwner();
        if (owner == null) {
            throw new IllegalArgumentException("Owner does not exist!");
        }
        if (firstName == null || firstName.trim().length() == 0) {
            throw new IllegalArgumentException("Owner first name cannot be empty!");
        }
        // Set the first name
        owner.setFirstName(firstName);

        // Save the owner
        ownerRepository.save(owner);
        return owner;
    }

    @Transactional
    public Owner updateOwnerLastName(String lastName) {

        // Get the owner
        Owner owner = getOwner();
        if (owner == null) {
            throw new IllegalArgumentException("Owner does not exist!");
        }
        if (lastName == null || lastName.trim().length() == 0) {
            throw new IllegalArgumentException("Owner last name cannot be empty!");
        }

        // Set the last name
        owner.setLastName(lastName);

        // Save thw owner
        ownerRepository.save(owner);
        return owner;
    }

    @Transactional
    public Owner updateOwnerPassword(String oldPassword, String password) {

        // Get the owner
        Owner owner = getOwner();

        // Validation
        if (owner == null) {
            owner = new Owner();
        }
        if (owner.getPassword() != null && !owner.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Wrong old password!");
        }
        if (password == null || HelperMethods.PasswordCheck(password).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid new password!");
        }

        // Set the possword
        owner.setPassword(password);

        // Save the owner
        ownerRepository.save(owner);
        return owner;
    }

}
