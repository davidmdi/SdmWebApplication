package logic.Logic.My_CLASS;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.awt.*;

public class MyStoreItem {
    private MyItem myItem;
    private int price;
    private int storeId;
    private String itemKind; // store offer.
    private int howManyTimeSold = 0 ;

    public MyStoreItem(MyItem myItem, int price, int storeId , String itemKind) {
        this.myItem = myItem;
        this.price = price;
        this.storeId = storeId;
        this.itemKind = itemKind;
    }

    public MyItem getMyItem() {
        return myItem;
    }

    public void setMyItem(MyItem myItem) {
        this.myItem = myItem;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName(){
        return this.myItem.getSdmItem().getName();
    }

    public Spinner<Double> getSpinner() {
        return this.getMyItem().getSpinner();
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getItemKind() {
        return itemKind;
    }

    public void setItemKind(String itemKind) {
        this.itemKind = itemKind;
    }

    public int getHowManyTimeSold() {
        return howManyTimeSold;
    }

    public void setHowManyTimeSold(int howManyTimeSold) {
        this.howManyTimeSold = howManyTimeSold;
    }

    @Override
    public String toString() {
        return String.valueOf(this.getMyItem().getSdmItem().getId()) +
                " :" + this.getName();
    }
}
