package Service;

import java.util.ArrayList;
import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO = null;
    private static MessageService instance = null;

    private MessageService(){
        this.messageDAO = new MessageDAO();
    }
    
/*
    private MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
*/

    /**
     * @return The instance of the MessageService object.
     */
    public static synchronized MessageService getInstance(){
        if(instance == null){
            instance = new MessageService();
        }
        return instance;
    }

    /**
     * @param messageDAO Accepts a Account DAO injection into this object.
     */
    public void setMessageDAO(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message message){
        if(message.getMessage_text().equals("") 
            || message.getMessage_text().length() > 255
            || !AccountService.getInstance().checkAccountExist(message.getPosted_by())){
            return null;
        }

        Message nMssage = messageDAO.insertMessage(message);
        if(nMssage != null){
            return nMssage;
        }

        return null;
    }

    /**
     * Used to get all the message in the system.
     * @return {@code List} of {@code Message} if it is successful or {@code null} if it failed.
     */
    public List<Message> getAllMessage() {
        return messageDAO.getAllMessage();
    }

    /**
     * @param message_id Takes in a {@code int} message_id to get information about a certain message.
     * @return Message object from the {@code int} message_id, or returns {@code null} if it doesn't exist. 
     */
    public Message getMessageByID(int message_id){
        return messageDAO.getMessageByID(message_id);
    }

    /**
     * @param message_id Takes in a {@code int} message_id to get information about a certain message.
     * @return {@code Message} that includes the original message if it successfully delete or return empty
     * {@code null} if it failed to find.
     */
    public Message deleteMessageByID(int message_id){
        return messageDAO.deleteMessageByID(message_id);
    }

    /**
     * @param message Take in a {@code int} message ID to be used to delete data.
     * @param newMessage Using a {@code String} to change the message with the new message.
     * @return {@code Message} object with the updated message and all the other information,
     * or null if it failed to find the message.
     */
    public Message updateMessage(int message_id, String newMessage){
        if(newMessage == "" || newMessage.length() > 255){
            return null;
        }
        return messageDAO.updateMessage(message_id, newMessage);
    }

        /**
     * @param account Takes in a {@code int} for the account ID to process the request.
     * @return {@code List} of {@code Message} from all the message that user has sent. 
     */
    public List<Message> getAllAccountMessage(int account_id){
        return messageDAO.getAllMessage(account_id);
    }
}
