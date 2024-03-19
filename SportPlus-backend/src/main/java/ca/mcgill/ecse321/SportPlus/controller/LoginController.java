package ca.mcgill.ecse321.SportPlus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportPlus.dto.LoginRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.LoginResponseDto;
import ca.mcgill.ecse321.SportPlus.model.Login;
import ca.mcgill.ecse321.SportPlus.service.LoginService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@CrossOrigin(origins = "*")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping(value = { "/login/{password}", "/login/{password}/" })
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequest, @PathVariable("password") String password){
        Login login = loginService.logIn(loginRequest, password);
        return new LoginResponseDto(login, loginRequest.getAccountType());
    }

    @PostMapping(value = { "/logout", "/logout/" })
    public void logout(@RequestBody LoginRequestDto request){
        loginService.logOut(request);
    }

}
