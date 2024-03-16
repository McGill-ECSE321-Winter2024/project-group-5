package ca.mcgill.ecse321.SportPlus.service;

import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.LoginRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dto.LoginRequestDto;
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
     public Login logIn(LoginRequestDto loginRequest, String password){
        Account account = null;
        switch(loginRequest.getAccountType()){
            case OWNER:
                if(!loginRequest.getAccountEmail().equals("owner@sportplus.com")){
                    throw new IllegalArgumentException("This is not an Owner email.");
                }
                account = ownerService.getOwner();
            case INSTRUCTOR:
                account = instructorService.getInstructor(loginRequest.getAccountEmail());
            case CLIENT:
                account = clientService.getClient(loginRequest.getAccountEmail());
        }
        if(account == null){
            throw new IllegalArgumentException("Account of Type " + loginRequest.getAccountType()+" with given email does not exist.");
        }
        Login found = getLoginFromAccount(account);
        if(found != null){
            deleteByLoginId(found.getLoginId());
        }
        if(!HelperMethods.isPasswordOk(account,password)){
            throw new IllegalArgumentException("Wrong Password!");
        }
        Time endTime = HelperMethods.updateEndTime(loginRequest.getCurrentTime());
        
        return createLogin(0, loginRequest.getCurrentTime(), endTime, account);
    }

    @Transactional 
    public void logOut(LoginRequestDto loginRequest){
        deleteByLoginId(loginRequest.getLoginId());
    }
    @Transactional 
    public void logOut(int loginId){
        deleteByLoginId(loginId);
    }

    @Transactional
    public boolean isStillLoggedIn(LoginRequestDto requestDto){
        return HelperMethods.isLoginTimeStillValid(requestDto.getCurrentTime(), getLoginFromId(requestDto.getLoginId()).getEndTime());
    }

    @Transactional
    public Login updateEndTime(LoginRequestDto request){
        Login login = getLoginFromId(request.getLoginId());
        Time newTime = HelperMethods.updateEndTime(request.getCurrentTime());
        login.setEndTime(newTime);
        loginRepository.save(login);
        return login;
    }

    
}
