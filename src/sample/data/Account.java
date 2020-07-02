package sample.data;

public class Account {

    private String accountName;
    private int accountNumber;
    private int accountPin;
    private double amount;


    public Account() {
        this.amount = 0.0;
    }

    public Account(String accountOwner, int accountNumber, int accountPin) {
        this.accountName = accountOwner;
        this.accountNumber = accountNumber;
        this.accountPin = accountPin;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAccountPin() {
        return accountPin;
    }

    public void setAccountPin(int accountPin) {
        this.accountPin = accountPin;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    @Override
    public String toString() {
        return "Account: " +
                "accountOwner='" + accountName + '\'' +
                ", accountNumber=" + accountNumber +
                ", accountPin=" + accountPin +
                ", amount=" + amount;
    }

    public void withdraw(double withdrawAmount) {
        amount = amount - withdrawAmount;
    }

    public void deposit(double depositAmount) {
        amount = amount + depositAmount;
    }


}
