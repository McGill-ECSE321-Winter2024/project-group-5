package ca.mcgill.ecse321.SportPlus.service;

import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.LoginRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.model.Account;
import ca.mcgill.ecse321.SportPlus.model.Login;
import ca.mcgill.ecse321.SportPlus.service.utilities.HelperMethods;

@Service
public class LoginService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    LoginRepository loginRepository;

    // -----------Wrappers-----------//

     /**
     * Retrieves login information based on ID. 
     * 
     * @param id The ID of the login.
     * @return The Login object.
     * @throws IllegalArgumentException If no login exists with the given ID.
     */
    @Transactional
    public Login getLoginFromId(int id) {
        Login login = loginRepository.findByLoginId(id);
        if (login == null) {
            throw new IllegalArgumentException("Login does not exist!");
        }
        return login;
    }

     /**
     * Retrieves login information based on account. 
     * 
     * @param account The account associated with the login.
     * @return The Login object.
     * @throws IllegalArgumentException If the account is null or no login exists for the account.
     */
    @Transactional
    public Login getLoginFromAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account is null!");
        }
        Login login = loginRepository.findByAccount(account);
        if (login == null) {
            throw new IllegalArgumentException("Login does not exist!");
        }
        return login;
    }

     /**
     * Retrieves all login information.
     * 
     * @return List of all Login objects.
     */
    @Transactional
    public List<Login> getAllLogins() {
        return HelperMethods.toList(loginRepository.findAll());
    }
    // ------------EndWrappers----------//

     /**
     * Logs in a user.
     * 
     * @param type        The type of the account (OWNER, INSTRUCTOR, CLIENT).
     * @param email       The email associated with the account.
     * @param password    The password of the account.
     * @param currentTime The current time.
     * @return The Login object representing the logged-in session.
     * @throws IllegalArgumentException If the account type is invalid, the email does not exist,
     *                                  the password is incorrect, or the login fails for any reason.
     */
    @Transactional
    public Login logIn(String type, String email, String password, Time currentTime) {
        Account account = null;
        if (type.equals("OWNER")) {
            if (!email.equals("owner@sportplus.com")) {
                throw new IllegalArgumentException("This is not and Owner email.");
            }
            account = ownerRepository.findByEmail(email);
        } else if (type.equals("INSTRUCTOR")) {
            account = instructorRepository.findInstructorByEmail(email);
        } else if (type.equals("CLIENT")) {
            account = clientRepository.findByEmail(email);
        } else {
            throw new IllegalArgumentException("Account type is invalid!");
        }

        if (account == null) {
            throw new IllegalArgumentException("Account of Type " + type + " with given email does not exist.");
        }
        Login found = null;
        try {
            found = getLoginFromAccount(account);
            if (found != null) {
                loginRepository.deleteById(found.getLoginId());
            }
        } catch (Exception e) {
        }
        if (!HelperMethods.isPasswordOk(account, password)) {
            throw new IllegalArgumentException("Wrong Password!");
        }
        Time endTime = HelperMethods.updateEndTime(currentTime);

        Login login = new Login(0, currentTime, endTime, account);
        loginRepository.save(login);

        return login;
    }

     /**
     * Logs out a user.
     * 
     * @param loginId The ID of the login session to be logged out.
     */
    @Transactional
    public void logOut(int loginId) {
        loginRepository.deleteByLoginId(loginId);
    }

     /**
     * Checks if a user is still logged in.
     * 
     * @param loginId    The ID of the login session.
     * @param currentTime The current time.
     * @return True if the user is still logged in, otherwise false.
     */
    @Transactional
    public boolean isStillLoggedIn(int loginId, Time currentTime) {
        return HelperMethods.isLoginTimeStillValid(getLoginFromId(loginId).getEndTime(), currentTime);
    }

     /**
     * Updates the end time of a login session.
     * 
     * @param loginId    The ID of the login session.
     * @param currentTime The current time.
     * @return The updated Login object.
     */
    @Transactional
    public Login updateEndTime(int loginId, Time currentTime) {
        Login login = getLoginFromId(loginId);
        Time newTime = HelperMethods.updateEndTime(currentTime);
        login.setEndTime(newTime);
        loginRepository.save(login);
        return login;
    }

}
