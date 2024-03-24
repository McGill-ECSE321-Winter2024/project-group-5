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

    @Transactional
    public Login getLoginFromId(int id) {
        Login login = loginRepository.findByLoginId(id);
        if (login == null) {
            throw new IllegalArgumentException("Login does not exist!");
        }
        return login;
    }

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


    @Transactional
    public List<Login> getAllLogins() {
        return HelperMethods.toList(loginRepository.findAll());
    }
    // ------------EndWrappers----------//

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

    @Transactional
    public void logOut(int loginId) {
        loginRepository.deleteByLoginId(loginId);
    }

    @Transactional
    public boolean isStillLoggedIn(int loginId, Time currentTime) {
        return HelperMethods.isLoginTimeStillValid(getLoginFromId(loginId).getEndTime(), currentTime);
    }

    @Transactional
    public Login updateEndTime(int loginId, Time currentTime) {
        Login login = getLoginFromId(loginId);
        Time newTime = HelperMethods.updateEndTime(currentTime);
        login.setEndTime(newTime);
        loginRepository.save(login);
        return login;
    }

}
