package logic.Logic.My_CLASS;


import SDM_CLASS.SDMItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class MyItem {
    public static String QUANTITY = "quantity";
    public static String WEIGHT = "weight";

    private SDMItem sdmItem;
    private int itemId;
    private String name;
    private String purchaseCategory;
    private int howManyStoresSellsThisItem=0;
    private double averageItemPrice = 0 ;
    private String averageItemPriceString = "0.0";
    private double howManyTimesItemSold = 0 ;
    private String howManyTimesItemSoldString = "0.0";

    //private Spinner<Double> spinner;

    public MyItem(int id, String name, String purchaseCategory) {
        this.itemId = id;
        this.name = name;
        this.purchaseCategory = purchaseCategory;
    }

    public MyItem(SDMItem sdmItem) {
        this.sdmItem = sdmItem;
        this.itemId = sdmItem.getId();
        this.name = sdmItem.getName();
        this.purchaseCategory = sdmItem.getPurchaseCategory();
        //this.spinner = buildSpinner();
    }

    public SDMItem getSdmItem() {
        return sdmItem;
    }

    //public Spinner<Double> getSpinner() {
       // return spinner;
   // }

    public String getName(){
       return name;
    }

   // public void setSpinner(Spinner<Double> spinner) {
     //   this.spinner = spinner;
    //}

//    public void setSpinnerValue(double value){
//        spinner.getValueFactory().setValue(value);
//    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemId() {
        return itemId;
    }

    public String getPurchaseCategory() {
        return purchaseCategory;
    }

    public int getHowManyStoresSellsThisItem() {
        return howManyStoresSellsThisItem;
    }

    public void setHowManyStoresSellsThisItem(int howManyStoresSellsThisItem) {
        this.howManyStoresSellsThisItem = howManyStoresSellsThisItem;
    }

    public double getAverageItemPrice() {
        return averageItemPrice;
    }

    public void setAverageItemPrice(double averageItemPrice) {
        this.averageItemPrice = averageItemPrice;
        this.averageItemPriceString = String.format("%.2f",averageItemPrice);
    }

    public double getHowManyTimesItemSold() {
        return howManyTimesItemSold;
    }

    public void setHowManyTimesItemSold(double howManyTimesItemSold) {
        this.howManyTimesItemSold = howManyTimesItemSold;
        this.howManyTimesItemSoldString = String.format("%.2f",howManyTimesItemSold);
    }

    public void setSdmItem(SDMItem sdmItem) {
        this.sdmItem = sdmItem;
    }

    public String getAverageItemPriceString() {
        return averageItemPriceString;
    }

    public String getHowManyTimesItemSoldString() {
        return howManyTimesItemSoldString;
    }

    private Spinner<Double> buildSpinner() {
        Spinner<Double>spinner = new Spinner<Double>();
        if(this.getSdmItem().getPurchaseCategory().equals("Weight")){
            SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.
                    DoubleSpinnerValueFactory(0, 500, 0, 0.1);
            spinner.setValueFactory(valueFactory);
        }
        else {
            SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.
                    DoubleSpinnerValueFactory(0, 500, 0, 1);
            spinner.setValueFactory(valueFactory);
        }

        return spinner;
    }

    @Override
    public String toString() {
        return String.valueOf(this.itemId)
                + " :" + this.getName();
    }


    public class ItemJson{
        public int id;
        public int price;

        public ItemJson(int id, int price){
            this.id = id;
            this.price = price;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "id=" + id +
                    ", price=" + price +
                    '}';
        }
    }
}
