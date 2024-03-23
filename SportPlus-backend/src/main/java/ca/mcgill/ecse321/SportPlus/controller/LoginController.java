package ca.mcgill.ecse321.SportPlus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ca.mcgill.ecse321.SportPlus.dto.LoginResponseDto;
import ca.mcgill.ecse321.SportPlus.dto.LoginRequestDto;
import ca.mcgill.ecse321.SportPlus.model.Login;
import ca.mcgill.ecse321.SportPlus.service.LoginService;

@CrossOrigin(origins = "*")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping(value = { "/login", "/login/" })
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponseDto logIn(@RequestBody LoginRequestDto newLogin) {
        Login createdLogin = loginService.logIn(newLogin.getType(), newLogin.getEmail(), newLogin.getPassword(),
                newLogin.getCurrentTime());
        return new LoginResponseDto(createdLogin, newLogin.getType());
    }

    @DeleteMapping(value = { "/logout/{loginId}", "/logout/{loginId}/" })
    public void logOut(@PathVariable("loginId") String theId) {
        loginService.logOut(Integer.parseInt(theId));
    }

}
