package controller;

import contracts.Users;
import model.AccountManager;
import model.Book;
import model.DataManager;
import model.UserFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckOutController {

    HashMap<Book, Integer> orders = new HashMap<>();
    String crediCardNumber;
    String cvv, expireDate;

    public CheckOutController(final HashMap<Book, Integer> orders, final String crediCardNumber, final String cvv, final String expireDate) {
        this.orders = orders;
        this.crediCardNumber = crediCardNumber;
        this.cvv = cvv;
        this.expireDate = expireDate;
    }
    public void checkOut(){
        ArrayList<String> parametes = new ArrayList<String>();
        parametes.add(AccountManager.getManager().getCurrentUser().getUserName());
        java.util.Date date = new java.util.Date();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
        parametes.add(timestamp.toString());
        parametes.add(crediCardNumber);
        parametes.add(cvv);
        parametes.add(expireDate);
        DataManager.getInstance().insertPurchase(parametes, orders);
    }
}