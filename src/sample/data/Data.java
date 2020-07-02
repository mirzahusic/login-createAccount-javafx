package sample.data;


import java.sql.*;
import java.util.ArrayList;

public class Data {
    private static Data instance = new Data();
    public static final String DB_NAME = "atm.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:/home/ciza/IdeaProjects/AtmNew/" + DB_NAME;

    public static final String TABLE_ACCOUNTS = "accounts";
    public static final String COLUMN_ACCOUNT_ID = "_id";
    public static final String COLUMN_ACCOUNT_NAME = "name";
    public static final String COLUMN_ACCOUNT_NUMBER = "number";
    public static final String COLUMN_ACCOUNT_PIN = "pin";
    public static final String COLUMN_ACCOUNT_AMOUNT = "amount";

    public static final String INSERT_ACCOUNT = "INSERT INTO " + TABLE_ACCOUNTS + '('
            + COLUMN_ACCOUNT_NAME + ", " + COLUMN_ACCOUNT_NUMBER + ", " + COLUMN_ACCOUNT_PIN + ", " + COLUMN_ACCOUNT_AMOUNT + ") VALUES (?, ?, ?, ?)";

    public static final String QUERY_ACCOUNT = "SELECT * FROM " + TABLE_ACCOUNTS + " WHERE "
            + COLUMN_ACCOUNT_NAME + " = ? " + " AND " + COLUMN_ACCOUNT_PIN + " = ?";

    public static final String UPDATE_ACCOUNT_AMOUNT = "UPDATE " + TABLE_ACCOUNTS + " SET " + COLUMN_ACCOUNT_AMOUNT
            + " = ? WHERE " + COLUMN_ACCOUNT_NAME + " = ? " + " AND " + COLUMN_ACCOUNT_PIN + " = ?";


    private Connection connection;
    private PreparedStatement insertIntoAccounts;
    private PreparedStatement queryAccount;
    private PreparedStatement updateAccountAmount;

    private ArrayList<Account> accountList = new ArrayList<>();

    public static Data getInstance() {
        return instance;
    }


    public void connectDatabase() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            insertIntoAccounts = connection.prepareStatement(INSERT_ACCOUNT);
            queryAccount = connection.prepareStatement(QUERY_ACCOUNT);
            updateAccountAmount = connection.prepareStatement(UPDATE_ACCOUNT_AMOUNT);

        } catch (SQLException e) {
            System.out.println("Couldn't load database!");
        }
    }

    public void closeDatabase() {
        try {
            if (updateAccountAmount != null)
                updateAccountAmount.close();
            if (queryAccount != null)
                queryAccount.close();
            if (insertIntoAccounts != null)
                insertIntoAccounts.close();
            if (connection != null)
                connection.close();

        } catch (SQLException e) {
            System.out.println("Couldn't close database!");
        }
    }


    public void insertAccount(Account account) {
        try {

            connection.setAutoCommit(false);
            insertIntoAccounts.setString(1, account.getAccountName());
            insertIntoAccounts.setInt(2, account.getAccountNumber());
            insertIntoAccounts.setInt(3, account.getAccountPin());
            insertIntoAccounts.setDouble(4, account.getAmount());


            int rows = insertIntoAccounts.executeUpdate();

            if (rows == 1) {
                connection.commit();
            } else {
                throw new SQLException("Account insert failed!");
            }

        } catch (Exception e) {

            System.out.println("1" + e.getMessage());
            try {
                System.out.println("Rollback...");
                connection.rollback();
            } catch (Exception e2) {
                System.out.println(e2.getMessage());
            }
        } finally {

            try {
                connection.setAutoCommit(true);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public Account queryAccountForLogin(String name, int pin) {
        try {
            queryAccount.setString(1, name);
            queryAccount.setInt(2, pin);
            ResultSet resultSet = queryAccount.executeQuery();
            if (resultSet.next()) {
                Account account = new Account();
                account.setAccountName(resultSet.getString(2));
                account.setAccountNumber(resultSet.getInt(3));
                account.setAccountPin(resultSet.getInt(4));
                account.setAmount(resultSet.getInt(5));
                addAccountToList(account);


                return account;
            } else
                throw new SQLException();


        } catch (SQLException e) {
            System.out.println("Couldn't query account");
            return null;
        }

    }

    public boolean updateAmountForAccount(String name, int pin, double amount) {

        try {
            updateAccountAmount.setDouble(1, amount);
            updateAccountAmount.setString(2, name);
            updateAccountAmount.setInt(3, pin);
            updateAccountAmount.executeUpdate();
            int result = updateAccountAmount.executeUpdate();
            if (result != 1)
                throw new SQLException();
            else
                return true;


        } catch (SQLException e) {
            System.out.println("Exception in update");
            return false;
        }

    }


    public ArrayList<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(ArrayList<Account> accountList) {
        this.accountList = accountList;

    }

    public void addAccountToList(Account account) {
        accountList.add(account);
    }

    public void deleteAccountFromList(Account account) {
        accountList.remove(account);
    }

}
