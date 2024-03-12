package ca.mcgill.ecse321.SportPlus.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.SportPlus.model.Login;
import ca.mcgill.ecse321.SportPlus.model.Account;
import java.util.List;


public interface LoginRepository extends CrudRepository<Login, Integer>{

    /**
     * Find Login by loginId
     * 
     * @param loginId
     * @return Login
     */
    Login findByLoginId(Integer loginId);

    /**
     * Find Login by Account 
     * 
     * @param account
     * @return Login
     */
    Login findByAccount(Account account);

    /**
     * Delete Login by Account 
     * 
     * @param account
     */
    Void deleteByAccount(Account account);

     /**
     * Find all Logins
     * 
     * @return List<Login>
     */
    List<Login> findAll();

    
}
