package ca.mcgill.ecse321.SportPlus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ca.mcgill.ecse321.SportPlus.dto.OwnerResponseDto;
import ca.mcgill.ecse321.SportPlus.dto.OwnerRequestDto;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.service.OwnerService;

@CrossOrigin(origins = "*")
@RestController
public class OwnerRestController {

    @Autowired
    private OwnerService ownerService;

    @GetMapping(value = { "/owner/get", "/owner/get/" })
    public OwnerResponseDto findOwnerByEmail() {
        Owner owner = ownerService.getOwner();
        return new OwnerResponseDto(owner);
    }

    @PostMapping(value = { "/owner/create", "/owner/create/" })
    @ResponseStatus(HttpStatus.CREATED)
    public OwnerResponseDto createOwner(@RequestBody OwnerRequestDto owner) {
        Owner createdOwner = ownerService.createOwner(owner.getFirstName(), owner.getPassword(), owner.getLastName());
        return new OwnerResponseDto(createdOwner);
    }

    @PutMapping(value = { "/owner/updateFirstName/{newFirstName}", "/owner/updateFirstName/{newFirstName}/" })
    public OwnerResponseDto updateOwnerFirstName(@PathVariable("newFirstName") String theFirstName) {
        Owner owner = ownerService.getOwner();
        ownerService.updateOwnerFirstName(theFirstName);
        owner = ownerService.getOwner();
        return new OwnerResponseDto(owner);
    }

    @PutMapping(value = { "/owner/updateLastName/{newLastName}", "/owner/updateLastName/{newLastName}/" })
    public OwnerResponseDto updateOwnerLastName(@PathVariable("newLastName") String theLastName) {
        Owner owner = ownerService.getOwner();
        ownerService.updateOwnerLastName(theLastName);
        owner = ownerService.getOwner();
        return new OwnerResponseDto(owner);
    }

    @PutMapping(value = { "/owner/updatePassword/{oldPassword}/{newPassword}",
            "/owner/updatePassword/{oldPassword}/{newPassword}/" })
    public OwnerResponseDto updateOwnerPassword(@PathVariable("oldPassword") String theOldPassword,
            @PathVariable("newPassword") String thePassword) {
        Owner owner = ownerService.getOwner();
        ownerService.updateOwnerPassword(theOldPassword, thePassword);
        owner = ownerService.getOwner();
        return new OwnerResponseDto(owner);
    }

}
