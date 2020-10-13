package logic.Logic.My_CLASS;

import java.util.ArrayList;
import java.util.List;

public class AccountManager {
    private static final String TYPE_LOAD = "load";
    private static final String TYPE_RECEIVE_PAYMENT = "receive";
    private static final String TYPE_TRANSFER_PAYMENT = "transfer";

    private List<AccountAction> actionList ;
    private double balance;

    public AccountManager(double balance) {
        this.actionList = new ArrayList<>();
        this.balance = balance;
    }

    public void addAction(AccountAction action){
        this.actionList.add(action);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public List<AccountAction> getActionList() {
        return actionList;
    }
}
