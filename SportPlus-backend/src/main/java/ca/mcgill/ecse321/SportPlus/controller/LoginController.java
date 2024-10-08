package ca.mcgill.ecse321.SportPlus.controller;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ca.mcgill.ecse321.SportPlus.dto.LoginResponseDto;
import ca.mcgill.ecse321.SportPlus.dto.LoginListDto;
import ca.mcgill.ecse321.SportPlus.dto.LoginRequestDto;
import ca.mcgill.ecse321.SportPlus.model.Account;
import ca.mcgill.ecse321.SportPlus.model.Login;
import ca.mcgill.ecse321.SportPlus.service.ClientService;
import ca.mcgill.ecse321.SportPlus.service.InstructorService;
import ca.mcgill.ecse321.SportPlus.service.LoginService;
import ca.mcgill.ecse321.SportPlus.service.OwnerService;

@CrossOrigin(origins = "*")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private ClientService clientService;

     /**
     * Logs in a user.
     * 
     * @param newLogin The LoginRequestDto containing login information.
     * @return The LoginResponseDto with created login information.
     */
    @PostMapping(value = { "/login", "/login/" })
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponseDto logIn(@RequestBody LoginRequestDto newLogin) {
        Login createdLogin = loginService.logIn(newLogin.getType(), newLogin.getEmail(), newLogin.getPassword(),
                newLogin.getCurrentTime());
        return new LoginResponseDto(createdLogin, newLogin.getType());
    }

    /**
     * Logs out a user.
     * 
     * @param theId The ID of the login to be logged out.
     */
    @DeleteMapping(value = { "/logout/{loginId}", "/logout/{loginId}/" })
    public void logOut(@PathVariable("loginId") String theId) {
        loginService.logOut(Integer.parseInt(theId));
    }

     /**
     * Retrieves login information based on account email and type.
     * 
     * @param email The email associated with the account.
     * @param type  The type of the account (OWNER, INSTRUCTOR, CLIENT).
     * @return The LoginResponseDto containing login information.
     */
    @GetMapping(value = { "/login/getByAccount/{email}/{accountType}", "/login/getByAccount/{email}/{accountType}/" })
    public LoginResponseDto getLoginByAccount(@PathVariable("email") String email,
            @PathVariable("accountType") String type) {
        Account account = null;
        if (type.equals("OWNER")) {
            if (!email.equals("owner@sportplus.com")) {
                throw new IllegalArgumentException("This is not and Owner email.");
            }
            account = ownerService.getOwner();
        } else if (type.equals("INSTRUCTOR")) {
            account = instructorService.getInstructor(email);
        } else if (type.equals("CLIENT")) {
            account = clientService.getClient(email);
        } else {
            throw new IllegalArgumentException("Account type is invalid!");
        }
        if (account == null) {
            throw new IllegalArgumentException("Account of Type " + type + " with given email does not exist.");
        }
        Login login = loginService.getLoginFromAccount(account);
        return new LoginResponseDto(login, type);
    }

     /**
     * Retrieves login information based on login ID.
     * 
     * @param id The id of the login.
     * @return The LoginResponseDto containing login information.
     */
    @GetMapping(value = { "/login/getById/{id}", "/login/getById/{id}/" })
    public LoginResponseDto getLoginFromId(@PathVariable("id") int id) {
        Login login = loginService.getLoginFromId(id);
        Account account = login.getAccount();
        String type = "";
        if (account.getEmail().endsWith("@sportplus.com")) {
            if (account.getEmail().equals("owner@sportplus.com")) {
                type = "OWNER";
            } else {
                type = "INSTRUCTOR";
            }
        } else {
            type = "CLIENT";
        }
        return new LoginResponseDto(login, type);
    }

     /**
     * Retrieves all login information.
     * 
     * @return The LoginListDto containing a list of login information.
     */
    @GetMapping(value = { "/login/getAll", "/login/getAll/" })
    public LoginListDto getAllLogins() {
        List<LoginResponseDto> logins = new ArrayList<>();
        for (Login login : loginService.getAllLogins()) {
            Account account = login.getAccount();
            String type = "";
            if (account.getEmail().endsWith("@sportplus.com")) {
                if (account.getEmail().equals("owner@sportplus.com")) {
                    type = "OWNER";
                } else {
                    type = "INSTRUCTOR";
                }
            } else {
                type = "CLIENT";
            }
            logins.add(new LoginResponseDto(login, type));
        }
        return new LoginListDto(logins);
    }

     /**
     * Checks if a user is still logged in.
     * 
     * @param id    The ID of the login.
     * @param time  The current time.
     * @return True if the user is still logged in, otherwise false.
     */
    @GetMapping(value = { "login/isStillLoggedIn/{id}/{currentTime}", "login/isStillLoggedIn/{id}/{currentTime}/" })
    public boolean isStillLoggedIn(@PathVariable("id") int id, @PathVariable("currentTime") Time time) {

        return loginService.isStillLoggedIn(id, time);
    }

    /**
     * Updates the end time of a login session.
     * 
     * @param id    The ID of the login.
     * @param time  The current time.
     * @return The updated LoginResponseDto.
     */
    @PutMapping(value = { "/login/updateEndtime/{id}/{currentTime}", "/login/updateEndtime/{id}/{currentTime}/" })
    public LoginResponseDto updateEndTime(@PathVariable("id") int id, @PathVariable("currentTime") Time time) {
        Login login = loginService.updateEndTime(id, time);
        Account account = login.getAccount();
        String type = "";
        if (account.getEmail().endsWith("@sportplus.com")) {
            if (account.getEmail().equals("owner@sportplus.com")) {
                type = "OWNER";
            } else {
                type = "INSTRUCTOR";
            }
        } else {
            type = "CLIENT";
        }
        return new LoginResponseDto(login, type);
    }

}
