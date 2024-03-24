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

    @PostMapping(value = { "/create", "/create/" })
    @ResponseStatus(HttpStatus.CREATED)
    public ClassTypeResponseDto createNewClassType(@RequestBody ClassTypeRequestDto requestDto) {
        ClassType createdClassType = classTypeService.instructorCreate(requestDto.getName(),
                requestDto.getDescription(), null); // Implement owner logic as needed
        return new ClassTypeResponseDto(createdClassType);
    }

    @GetMapping(value = { "/all", "/all/" })
    public ClassTypeListDto findAll() {
        List<ClassTypeResponseDto> listToReturn = new ArrayList<>();
        List<ClassType> listOrigin = classTypeService.getAllClassTypes();
        for (ClassType classType : listOrigin) {
            listToReturn.add(new ClassTypeResponseDto(classType));
        }
        return new ClassTypeListDto(listToReturn);
    }

    @GetMapping(value = { "/get/{name}", "/get/{name}/" })
    public ClassTypeResponseDto findByName(@PathVariable("name") String name) {
        ClassType classType = classTypeService.findByName(name);
        return new ClassTypeResponseDto(classType);
    }

    @DeleteMapping(value = { "/delete/{name}", "/delete/{name}/" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByName(@PathVariable("name") String name) {
        classTypeService.deleteByName(name);
    }

    @PostMapping(value = { "/approve/{typeId}", "/approve/{typeId}/" })
    public ClassTypeResponseDto approveClassType(@PathVariable("typeId") int typeId) {
        ClassType classType = classTypeService.approve(typeId);
        return new ClassTypeResponseDto(classType);
    }

    @PutMapping(value = { "/updateDescription/{typeId}", "/updateDescription/{typeId}/" })
    public ClassTypeResponseDto updateDescription(@PathVariable("typeId") int typeId, @RequestBody String description) {
        ClassType classType = classTypeService.updateDescription(typeId, description);
        return new ClassTypeResponseDto(classType);
    }

    @PutMapping(value = { "/updateName/{typeId}", "/updateName/{typeId}/" })
    public ClassTypeResponseDto updateName(@PathVariable("typeId") int typeId, @RequestBody String name) {
        ClassType classType = classTypeService.updateName(typeId, name);
        return new ClassTypeResponseDto(classType);
    }

}
