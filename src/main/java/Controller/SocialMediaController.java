package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService = null;
    private MessageService messageService = null;
    private ObjectMapper mapper = null;

    public SocialMediaController(){
        accountService = AccountService.getInstance();
        messageService = MessageService.getInstance();
        mapper = new ObjectMapper();
    }

    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::messagesHandler);
        app.get("/messages", this::getAllMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::patchMesageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessageFromUserAccountIDHandler);
        return app;
    }

    /**
     * Method that defines the structure of a register handler. 
     * This handles the API call for registering a new account.
     * @param ctx Takes in a Javalin context object to be processed.
     * @throws JsonProcessingException Throws an {@code JsonProcessingException} if there is an issue
     * with the {@code ObjectMapper}.
     */
    private void registerHandler(Context ctx) throws JsonProcessingException{
        Account account = mapper.readValue(ctx.body(), Account.class);

        Account newAccount = accountService.createAccount(account);
        if(newAccount != null){
            ctx.json(mapper.writeValueAsString(newAccount));
        } else {
            ctx.status(400);
        }
    }

    /**
     * Method that defines the structure of the login handler.
     * This method handles the login API calls.
     * @param ctx Takes in a Javalin context object to be processed.
     * @throws JsonProcessingException Throws an {@code JsonProcessingException} if there is an issue
     * with the {@code ObjectMapper}.
     */
       private void loginHandler(Context ctx) throws JsonProcessingException {
        Account account = mapper.readValue(ctx.body(), Account.class);

        Account newAccount = accountService.getAccount(account);

        if(newAccount != null){
            ctx.json(mapper.writeValueAsString(newAccount));
        } else {
            ctx.status(401);
        }
    }

    /**
     * Method that defines the structure of the post message handler.
     * This method is used to create message.
     * @param ctx Take in a Javalin context.
     * @throws JsonProcessingException Throws an {@code JsonProcessingException} if there is an issue
     * with the {@code ObjectMapper}.
     */
    private void messagesHandler(Context ctx) throws JsonProcessingException{
        Message message = mapper.readValue(ctx.body(), Message.class);

        Message nMessage = messageService.createMessage(message);
        if(nMessage != null){
            ctx.json(mapper.writeValueAsString(nMessage));
        } else {
            ctx.status(400);
        }
    }

    /**
     * Method that defines the structure of the get for message handler.
     * This is used to create every single message within the database.
     * @param ctx Take in a Javalin context.
     * @throws JsonProcessingException Throws an {@code JsonProcessingException} if there is an issue
     * with the {@code ObjectMapper}.
     */
     private void getAllMessageHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessage();
        ctx.json(mapper.writeValueAsString(messages));
    }

    /**
     * Method that defines the structure of the get with param for message handler.
     * This message is used to get a specific message with the use of a messsage ID.
     * @param ctx Take in a Javalin context.
     * @throws JsonProcessingException Throws an {@code JsonProcessingException} if there is an issue
     * with the {@code ObjectMapper}.
     */
    private void getMessageByIDHandler(Context ctx) throws JsonProcessingException{
        int message_id = getIDFromContext(ctx, "message_id");
        Message message = messageService.getMessageByID(message_id);
        if(message != null){
            ctx.json(mapper.writeValueAsString(message));
        }
    }

    /**
     * Method that defines the structure of the delete with param for message handler.
     * This is used to delete a specific message by using the inputted ID.
     * @param ctx Take in a Javalin context.
     * @throws JsonProcessingException Throws an {@code JsonProcessingException} if there is an issue
     * with the {@code ObjectMapper}.
     */
    private void deleteMessageByIDHandler(Context ctx) throws JsonProcessingException{
        int message_id = getIDFromContext(ctx, "message_id");
        Message message = messageService.deleteMessageByID(message_id);
        if(message != null){
            ctx.json(mapper.writeValueAsString(message));
        }
    }

    /**
     * Method that defines the structure of the patch with param for message handler.
     * This is used to update a specific message by using the input ID and new message.
     * @param ctx Take in a Javalin context.
     * @throws JsonProcessingException Throws an {@code JsonProcessingException} if there is an issue
     */
    private void patchMesageByIDHandler(Context ctx) throws JsonProcessingException{
        int message_id = getIDFromContext(ctx, "message_id");
        Message getMessage = mapper.readValue(ctx.body(), Message.class);
        
        Message message = messageService.updateMessage(message_id, getMessage.getMessage_text());
        if(message != null){
            ctx.json(mapper.writeValueAsString(message));
        } else {
            ctx.status(400);
        }
    }

    /**
     * Method that defines the structure of the get with param for account handler.
     * This is used to get all message by using account's information.
     * @param ctx Take in a Javalin context.
     * @throws JsonProcessingException Throws an {@code JsonProcessingException} if there is an issue
     */
    private void getAllMessageFromUserAccountIDHandler(Context ctx) throws JsonProcessingException{
        int id = getIDFromContext(ctx, "account_id");
        List<Message> messages = messageService.getAllAccountMessage(id);
        ctx.json(mapper.writeValueAsString(messages));
    }

    /**
     * Used to get Id from the parameter without having to rewrite this code.
     * @param ctx Takes in a Javalin Context object to process the paramter arg.
     * @param pathArg String object of the 
     * @return {@code Int} with the message ID.
     */
    private int getIDFromContext(Context ctx, String pathArg){
        String message_id_string = ctx.pathParam(pathArg);
        return Integer.parseInt(message_id_string);
    } 
}