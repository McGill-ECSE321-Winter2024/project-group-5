package ca.mcgill.ecse321.SportPlus.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.SportPlus.dto.ClassTypeListDto;
import ca.mcgill.ecse321.SportPlus.dto.ClassTypeRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.ClassTypeResponseDto;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.service.ClassTypeService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/classType")
public class ClassTypeController {

    @Autowired
    private ClassTypeService classTypeService;

    // Creates a new class type based on the provided request data and returns the created class type as a DTO.
    @PostMapping(value = { "/create", "/create/" })
    @ResponseStatus(HttpStatus.CREATED)
    public ClassTypeResponseDto createNewClassType(@RequestBody ClassTypeRequestDto requestDto) {
        // Calls the service layer to create a new class type, passing the necessary details.
        ClassType createdClassType = classTypeService.instructorCreate(requestDto.getName(),
                requestDto.getDescription(), null); // Placeholder for owner logic, to be implemented as needed.
        // Converts the created class type model into a DTO for the response.
        return new ClassTypeResponseDto(createdClassType);
    }

    // Retrieves all class types from the system and returns them as a list of DTOs.
    @GetMapping(value = { "/all", "/all/" })
    public ClassTypeListDto findAll() {
        List<ClassTypeResponseDto> listToReturn = new ArrayList<>();
        // Calls the service layer to get all class types.
        List<ClassType> listOrigin = classTypeService.getAllClassTypes();
        // Converts each class type model into a DTO and adds it to the list for the response.
        for (ClassType classType : listOrigin) {
            listToReturn.add(new ClassTypeResponseDto(classType));
        }
        // Wraps the list of DTOs in a list DTO object for the response.
        return new ClassTypeListDto(listToReturn);
    }

    // Finds a specific class type by its name and returns it as a DTO.
    @GetMapping(value = { "/get/{name}", "/get/{name}/" })
    public ClassTypeResponseDto findByName(@PathVariable("name") String name) {
        // Calls the service layer to find the class type by name.
        ClassType classType = classTypeService.findByName(name);
        // Converts the found class type model into a DTO for the response.
        return new ClassTypeResponseDto(classType);
    }

    // Deletes a class type identified by its name.
    @DeleteMapping(value = { "/delete/{name}", "/delete/{name}/" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByName(@PathVariable("name") String name) {
        // Calls the service layer to delete the class type by name.
        classTypeService.deleteByName(name);
    }

    // Approves a class type identified by its type ID and returns the approved class type as a DTO.
    @PostMapping(value = { "/approve/{typeId}", "/approve/{typeId}/" })
    public ClassTypeResponseDto approveClassType(@PathVariable("typeId") int typeId) {
        // Calls the service layer to approve the class type by its type ID.
        ClassType classType = classTypeService.approve(typeId);
        // Converts the approved class type model into a DTO for the response.
        return new ClassTypeResponseDto(classType);
    }


    // Updates the description of a class type identified by its type ID and returns the updated class type as a DTO.
    @PutMapping(value = { "/updateDescription/{typeId}", "/updateDescription/{typeId}/" })
    public ClassTypeResponseDto updateDescription(@PathVariable("typeId") int typeId, @RequestBody String description) {
        // Calls the service layer to update the description of the class type by its type ID.
        ClassType classType = classTypeService.updateDescription(typeId, description);
        // Converts the updated class type model into a DTO for the response.
        return new ClassTypeResponseDto(classType);
    }

    // Updates the name of a class type identified by its type ID and returns the updated class type as a DTO.
    @PutMapping(value = { "/updateName/{typeId}", "/updateName/{typeId}/" })
    public ClassTypeResponseDto updateName(@PathVariable("typeId") int typeId, @RequestBody String name) {
        // Calls the service layer to update the name of the class type by its type ID.
        ClassType classType = classTypeService.updateName(typeId, name);
        // Converts the updated class type model into a DTO for the response.
        return new ClassTypeResponseDto(classType);
    }

}

