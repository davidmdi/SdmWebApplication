package logic.Logic.My_CLASS;

import java.util.Date;

public class AccountAction {
    private String type  ;
    private Date date ;
    private double sumOfAction;
    private double balanceBefore;
    private double balanceAfter;

    public AccountAction(String type, Date date, double sumOfAction, double balanceBefore, double balanceAfter) {
        this.type = type;
        this.date = date;
        this.sumOfAction = sumOfAction;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
    }

    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public double getSumOfAction() {
        return sumOfAction;
    }

    public double getBalanceBefore() {
        return balanceBefore;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }


}
