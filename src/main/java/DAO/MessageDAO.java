package DAO;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    /**
     * @param message Takes in a {@code Message} object to process and insert a new message into the database.
     * @return
     */
    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch ) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.posted_by);
            preparedStatement.setString(2, message.message_text);
            preparedStatement.setLong(3, message.time_posted_epoch);

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                int generated_id = (int) resultSet.getLong(1);
                return new Message( generated_id,
                                    message.posted_by, 
                                    message.message_text, 
                                    message.time_posted_epoch
                                );
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return {@code List} of {@code Message} from the entire database.  
     */
    public List<Message> getAllMessage(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Message message = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                );
                messages.add(message);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
            if(connection != null){
                try{
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return messages;
    }

    /**
     * Get all message by an account ID instead of just getting all message in database.
     * @param account Takes in a {@code int} for the account ID to process the request.
     * @return {@code List} of {@code Message} from all the message that user has sent. 
     */
    public List<Message> getAllMessage(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql  = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Message message = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null){
                try{
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return messages;
    }

    /**
     * @param id Take in an {@code int} message ID to get the message.
     * @return {@code Message} object with all the data to be used.
     */
    public Message getMessageByID(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(connection != null){
                try{
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * @param message_id Take in a {@code int} message ID to be used to delete data. 
     * @return {@code Message} that includes the original message if it successfully delete or return empty
     * {@code null} if it failed to find.
     */
    public Message deleteMessageByID(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            Message message = getMessageByID(message_id);

            if(message != null){
                String sql = "DELETE from message WHERE message_id = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, message_id);
                int aff = preparedStatement.executeUpdate();

                if(aff > 0){
                    return message;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null){
                try{
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * @param message_id Take in a {@code int} message ID to be used to delete data.
     * @param newMessage Using a {@code String} to change the message with the new message.
     * @return {@code Message} object with the updated message and all the other information,
     * or null if it failed to find the message.
     */
    public Message updateMessage(int message_id, String newMessage){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(1, newMessage);
            preparedStatement.setInt(2, message_id);

            int aff = preparedStatement.executeUpdate();
            if(aff > 0){
                return getMessageByID(message_id);
            }
        } catch(SQLException e){
            e.printStackTrace();
        } finally {
            if(connection != null){
                try{
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
