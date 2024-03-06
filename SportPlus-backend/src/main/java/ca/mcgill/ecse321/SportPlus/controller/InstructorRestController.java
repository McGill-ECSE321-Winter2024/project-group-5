package ca.mcgill.ecse321.SportPlus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportPlus.service.InstructorService;

@CrossOrigin(origins = "*")
@RestController
public class InstructorRestController {
    
    @Autowired
    private InstructorService service;
}
