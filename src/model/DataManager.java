package model;

import contracts.DBContract;
import contracts.SqlCommands;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DataManager {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private static DataManager uniqueInstance;

    private DataManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/" + DBContract.DB_NAME, DBContract.DB_USERNAME,DBContract.DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e){
            System.out.println(e.getException().getMessage());
        }
    }

    public static DataManager getInstance(){
        if(uniqueInstance == null){
            return uniqueInstance = new DataManager();
        }
        return uniqueInstance;
    }

    public void deleteInstance(){
        uniqueInstance = null;
        close();
    }
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (preparedStatement != null){
                preparedStatement.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
        }
    }

    public boolean searchForUser(String userName) {
        try {
            preparedStatement = connect.prepareStatement(SqlCommands.SEARCH_USERNAME);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            ArrayList<String> users = new ArrayList<>();
            while(resultSet.next()){
                users.add(resultSet.getString(DBContract.User.USER_NAME_COLUMN));
            }
            if(users.size() == 0){
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean insertCustomer(Customer customer) {
        try {
            preparedStatement = connect.prepareStatement(SqlCommands.INSERT_USER);
            preparedStatement.setString(1, customer.getUserName());
            preparedStatement.setString(2, customer.getHashedPassword());
            preparedStatement.setString(3, customer.getFirstName());
            preparedStatement.setString(4, customer.getLastName());
            preparedStatement.setString(5, customer.getPhoneNumber());
            preparedStatement.setString(6, customer.getAddress());
            preparedStatement.setString(7, customer.getCredential());
            preparedStatement.setString(8, customer.getEmail());
            int cnt  = preparedStatement.executeUpdate();
            if(cnt == 1){
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUserHashedPassword(String userName){
        try {
            preparedStatement = connect.prepareStatement(SqlCommands.GET_PASSWORD);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            String password = "";
            while(resultSet.next()){
                password = resultSet.getString(DBContract.User.PASSWORD_COLUMN);
            }
            return password;
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public HashMap<String, String> getUser(String userName) {
        try {
            preparedStatement = connect.prepareStatement(SqlCommands.GET_USER);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            HashMap <String, String> userData = new HashMap<>();
            while(resultSet.next()){
                userData.put(DBContract.User.USER_NAME_COLUMN, resultSet.getString(DBContract.User.USER_NAME_COLUMN));
                userData.put(DBContract.User.PASSWORD_COLUMN, resultSet.getString(DBContract.User.PASSWORD_COLUMN));
                userData.put(DBContract.User.FIRST_NAME_COLUMN, resultSet.getString(DBContract.User.FIRST_NAME_COLUMN));
                userData.put(DBContract.User.LAST_NAME_COLUMN, resultSet.getString(DBContract.User.LAST_NAME_COLUMN));
                userData.put(DBContract.User.ADDRESS_COLUMN, resultSet.getString(DBContract.User.ADDRESS_COLUMN));
                userData.put(DBContract.User.PHONE_COLUMN, resultSet.getString(DBContract.User.PHONE_COLUMN));
                userData.put(DBContract.User.CREDENTIAL_COLUMN, resultSet.getString(DBContract.User.CREDENTIAL_COLUMN));
                userData.put(DBContract.User.EMAIL_COLUMN, resultSet.getString(DBContract.User.EMAIL_COLUMN));
            }
            return userData;
        } catch (SQLException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}