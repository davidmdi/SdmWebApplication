package logic.Logic.My_CLASS;


import SDM_CLASS.SDMDiscount;
import SDM_CLASS.SDMOffer;
import SDM_CLASS.ThenYouGet;
import javafx.scene.control.ChoiceBox;

public class StoreOffersView {
    private String offerName;
    private String ifYouBuy;
    private double quantity;
    private String operator;
    private ChoiceBox<String> thenYouGet;

    public StoreOffersView(SDMDiscount discount , MyStore store) {
        this.offerName = discount.getName();
        this.ifYouBuy = extractItemNameFromStore(discount.getIfYouBuy().getItemId(),store);
        this.quantity = discount.getIfYouBuy().getQuantity();
        this.operator = discount.getThenYouGet().getOperator();
        this.thenYouGet = createChoiceBox(discount,store);
    }

    private ChoiceBox<String> createChoiceBox(SDMDiscount discount, MyStore store) {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        ThenYouGet thenYouGet = discount.getThenYouGet();

        for (SDMOffer offer: thenYouGet.getSDMOffer()) {
            String item = String.valueOf(offer.getQuantity())  + " " +
                    extractItemNameFromStore(offer.getItemId(),store)
                    + " for" + offer.getForAdditional() + " Nis";
            choiceBox.getItems().add(item);
        }
        return choiceBox;
    }

    private String extractItemNameFromStore(int itemId, MyStore store) {
        return store.getStoreItems().getItemsMap().get(itemId).getName();
    }

    public String getOfferName() {
        return offerName;
    }

    public String getIfYouBuy() {
        return ifYouBuy;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getOperator() {
        return operator;
    }

    public ChoiceBox<String> getThenYouGet() {
        return thenYouGet;
    }
}
