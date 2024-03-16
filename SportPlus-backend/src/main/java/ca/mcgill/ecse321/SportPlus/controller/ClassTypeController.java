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

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ClassTypeResponseDto createNewClassType(@RequestBody ClassTypeRequestDto requestDto) {
        ClassType createdClassType = classTypeService.instructorCreate(requestDto.getName(), requestDto.getDescription(), null); // Implement owner logic as needed
        return new ClassTypeResponseDto(createdClassType);
    }

    @GetMapping("/all")
    public ClassTypeListDto findAll() {
        List<ClassTypeResponseDto> listToReturn= new ArrayList<>();
        List<ClassType> listOrigin=classTypeService.getAllClassTypes();
        for (ClassType classType : listOrigin) {
            listToReturn.add(new ClassTypeResponseDto(classType));
        }
        return new ClassTypeListDto(listToReturn) ;
    }

    @GetMapping("/get/{name}")
    public ClassTypeResponseDto findByName(@PathVariable String name) {
        ClassType classType = classTypeService.findByName(name);
        return new ClassTypeResponseDto(classType);
    }

    @DeleteMapping("/delete/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByName(@PathVariable String name) {
        classTypeService.deleteByName(name);
    }

    @PostMapping("/approve/{typeId}")
    public ClassTypeResponseDto approveClassType(@PathVariable int typeId) {
        ClassType classType = classTypeService.approve(typeId);
        return new ClassTypeResponseDto(classType);
    }

    @PutMapping("/updateDescription/{typeId}")
    public ClassTypeResponseDto updateDescription(@PathVariable int typeId, @RequestBody String description) {
        ClassType classType = classTypeService.updateDescription(typeId, description);
        return new ClassTypeResponseDto(classType);
    }

    @PutMapping("/updateName/{typeId}")
    public ClassTypeResponseDto updateName(@PathVariable int typeId, @RequestBody String name) {
        ClassType classType = classTypeService.updateName(typeId, name);
        return new ClassTypeResponseDto(classType);
    }

}
