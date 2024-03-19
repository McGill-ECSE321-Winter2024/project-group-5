package ca.mcgill.ecse321.SportPlus.service;

import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.LoginRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dto.LoginRequestDto.AccountType;
import ca.mcgill.ecse321.SportPlus.model.Account;
import ca.mcgill.ecse321.SportPlus.model.Login;
import ca.mcgill.ecse321.SportPlus.service.utilities.HelperMethods;


public class LoginService {
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    private InstructorService instructorService;
    @Autowired 
    private OwnerService ownerService;

    @Autowired
    private ClientService clientService;

     //-----------Wrappers-----------//

    @Transactional
     public Login getLoginFromId(int id){
        Login login  = loginRepository.findByLoginId(id);
        if(login == null){
            throw new IllegalArgumentException("Login does not exist!");
        }
        return login;
     }
 
     @Transactional
     public Login getLoginFromAccount(Account account){
        if(account == null){
            throw new IllegalArgumentException("Account is null!");
        }
        Login login = loginRepository.findByAccount(account);
        if(login == null){
            throw new IllegalArgumentException("Login does not exist!");
        }
        return login;
     }

     @Transactional
     public void deleteByLoginId(int id){
        loginRepository.deleteByLoginId(id);
     }


     @Transactional
     public List<Login> getAllLogins(){
        return HelperMethods.toList(loginRepository.findAll());
     }
     //------------EndWrappers----------//

    
     @Transactional
     public Login createLogin(int id, Time startTime, Time endTime, Account account){
        Login login = new Login(id, startTime, endTime, account);
        loginRepository.save(login);
        return login;
     }
     

     @Transactional
     public Login logIn(AccountType type, String email, String password, Time currentTime){
        Account account = null;
        switch(type){
            case OWNER:
                if(!email.equals("owner@sportplus.com")){
                    throw new IllegalArgumentException("This is not an Owner email.");
                }
                account = ownerService.getOwner();
            case INSTRUCTOR:
                try {
                account = instructorService.getInstructor(email); 
                }catch(Exception e){
                    throw new IllegalArgumentException("Account of Type " + type + " with given email does not exist.");
                }

            case CLIENT:
                try {
                account = clientService.getClient(email);
                }catch(Exception e){
                throw new IllegalArgumentException("Account of Type " + type + " with given email does not exist.");
                }
        }
        if(account == null){
            throw new IllegalArgumentException("Account of Type " + type + " with given email does not exist.");
        }
        Login found = getLoginFromAccount(account);
        if(found != null){
            deleteByLoginId(found.getLoginId());
        }
        if(!HelperMethods.isPasswordOk(account,password)){
            throw new IllegalArgumentException("Wrong Password!");
        }
        Time endTime = HelperMethods.updateEndTime(currentTime);
        
        return createLogin(0, currentTime, endTime, account);
    }

    @Transactional 
    public void logOut(int loginId){
        deleteByLoginId(loginId);
    }

    @Transactional
    public boolean isStillLoggedIn(int loginId, Time currentTime){
        return HelperMethods.isLoginTimeStillValid(getLoginFromId(loginId).getEndTime(),currentTime);
    }

    @Transactional
    public Login updateEndTime(int loginId, Time currentTime){
        Login login = getLoginFromId(loginId);
        Time newTime = HelperMethods.updateEndTime(currentTime);
        login.setEndTime(newTime);
        loginRepository.save(login);
        return login;
    }

    
}
