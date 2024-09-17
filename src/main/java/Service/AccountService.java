package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class AccountService {
    private AccountDAO accountDAO = null;
    private static AccountService instance = null;
    
    private AccountService(){
        this.accountDAO = new AccountDAO();
    }

/* 
    private AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
*/

    /**
     * @return The instance of the AccountService object.
     */
    public static synchronized AccountService getInstance(){
        if(instance == null){
            instance = new AccountService();
        }
        return instance;
    }

    /**
     * @param accountDAO Accepts a Account DAO injection into this object.
     */
    public void setAccountDAO(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /**
     * Used to create a new user account within the system.
     * @param account Takes in an account to be used to get the username of the accounts, 
     *  and compares the passwords with the one that the user provides.
     * @return {@code Account} object if the operation was succesfully or {@code null} object if it failed to 
     * create a new account.
     */
    public Account createAccount(Account account){
        if(accountDAO.getAccountByUsername(account.getUsername()) == null 
            && account.getUsername() != ""
            && account.getPassword().length() >= 4)
        {
            return accountDAO.insertAccount(account);
        }
        return null;
    }

    /**
     * Used to get the user account information.
     * @param account Takes in an account to be used to get the username of the accounts, 
     *  and compares the passwords with the one that the user provides.
     * @return {@code Account} object that contains all the correct information about the account, or 
     * returns a {@code null} object if the operation was a failure.
     */
    public Account getAccount(Account account){
        if(account.getUsername().equals("") 
            || account.getPassword().equals(""))
        {
            return null;
        }

        Account returnedAccount = accountDAO.getAccountByUsername(account.username);

        if(returnedAccount == null || !account.getPassword().equals(returnedAccount.getPassword())){
            return null;
        }

        return returnedAccount;
    }

    /**
     * @param id Take in the {@code int} of the user ID for checking the account.
     * @return {@code True} if the account exist or {@code false} if the account doesn't exist.
     */
    public boolean checkAccountExist(int id){
        return accountDAO.checkIfAccountExist(id);
    }
}
