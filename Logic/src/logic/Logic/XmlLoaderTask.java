package logic.Logic;


import SDM_CLASS.*;
import javafx.concurrent.Task;
import logic.Logic.My_CLASS.MySuperMarket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class XmlLoaderTask extends Task<Boolean> {
    private String filePath;
    Boolean validationForBuild = false;
    private Consumer<MySuperMarket> superMarketDelegate;
    private Consumer<Boolean> isSuperMarketIsValidDelegate;
    private SuperDuperMarketDescriptor sdmJAXB;

    public Boolean getValidationForBuild() {
        return validationForBuild;
    }

    public XmlLoaderTask(SuperDuperMarketDescriptor sdmJAXB, Consumer<MySuperMarket> superMarketConsumer, Consumer<Boolean> validationConsumer ){
        this.sdmJAXB = sdmJAXB;
        this.superMarketDelegate = superMarketConsumer;
        this.isSuperMarketIsValidDelegate = validationConsumer;
    }

    public XmlLoaderTask(String path, Consumer<MySuperMarket> superMarketConsumer, Consumer<Boolean> validationConsumer ){
        this.filePath = path;
        this.superMarketDelegate = superMarketConsumer;
        this.isSuperMarketIsValidDelegate = validationConsumer;
    }

    @Override
    protected Boolean call() throws Exception {
        updateMessage("Loading File..");
        updateProgress(0,1);
        Thread.sleep(500);
        updateProgress(0.25,1);
        Thread.sleep(500);
        updateProgress(0.5,1);
        Thread.sleep(500);
        updateProgress(0.75,1);
        Thread.sleep(500);
        updateProgress(1,1);
        //updateMessage(loadXML(filePath));
        return null;
    }

    /*
    public String loadXML(String xmlPathString)  {
        String returnString ;
        Path xmlPath = Paths.get(xmlPathString);
        if(Files.exists(xmlPath) &&  xmlPathString.substring(xmlPathString.length() - 4).
                toLowerCase().equals(".xml")) {
            returnString = createSDMSuperMarket(xmlPath);  //  create instance of SDM-sdmsupermarket with jaxb
        }
        else {
            returnString = "<" + xmlPathString + " is not " + ".xml" + " file or not exist!>\n"; // 3.1
        }
        return returnString;
    }

    private String createSDMSuperMarket(Path path) {
        String returnString = "";

        try {
            SuperDuperMarketDescriptor temp ;
            JAXBContext jaxbContext = JAXBContext.newInstance(SuperDuperMarketDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            temp = (SuperDuperMarketDescriptor) jaxbUnmarshaller.unmarshal(path.toFile());
            returnString = buildSuperMarket(temp);
            if(validationForBuild)
                this.superMarketDelegate.accept(new MySuperMarket(temp));
        } catch (JAXBException e) {
            returnString = e.getMessage();
        } catch (NullPointerException e) {
            returnString = "<one of the members is NUll>";
        }

        return returnString;
    }
*/
    private String buildSuperMarket(SuperDuperMarketDescriptor temp) {
        String returnString = "";
        boolean isItemIdUnique , isStoreIdUnique, iseReferencedToExistItem ,
                isItemDefinedOnlyOnceAtEachStore ,isEachProductSailedByOneStoreAtLeast, isInCordRange,
                isCustomerIdUniq , isCordUniq,isLegaldiscount;
        if(!(isItemIdUnique = checkIsItemIdUnique(temp.getSDMItems())))
            returnString = "<There are two different items with same id , please load legal .xml>";
        if(!(isStoreIdUnique = checkIsStoreIdUnique(temp.getSDMStores())))
            returnString = "<There are two different stores with same id , please load legal .xml>";
        if(!(iseReferencedToExistItem = checkIseReferencedToExistItem(temp )))
            returnString = "<There is price witch references to un-exist item in the store , please load legal .xml>";
        if(!(isEachProductSailedByOneStoreAtLeast = checkIsEachProductSailedByOneStoreAtLeast(temp)))
            returnString = "<There is Item witch is not sold by any store, please load legal .xml>";
        if(!(isItemDefinedOnlyOnceAtEachStore = checkIsItemDefinedOnlyOnceAtEachStore(temp)))
            returnString = "<There is Item witch sold at same store twice or more , please load legal .xml>";
        if(!(isInCordRange = checkLIsInCordRange(temp)))
            returnString = "<There is store out of range , please load legal .xml >";
//        if(!(isCustomerIdUniq = checkIsCustomerIdUnique(temp)))
//            returnString = "<There are two different customers with same id , please load legal .xml>";
//        if(!(isCordUniq = checKIsCordUniq(temp)))
//            returnString = "<There are two objects at same location , please load legal .xml>";
        if(!(isLegaldiscount = chekIsLegalDiscount(temp)))
            returnString = "<There is item in the discount items offer witch is not sold, please load legal .xml>";
        if(isItemIdUnique && isStoreIdUnique && iseReferencedToExistItem && isItemDefinedOnlyOnceAtEachStore
                && isEachProductSailedByOneStoreAtLeast &&  isInCordRange
                 && isLegaldiscount ) { // from gui-> && isCustomerIdUniq && isCordUniq
            returnString = "<xml file has load successfully>";
            validationForBuild = true;
            this.isSuperMarketIsValidDelegate.accept(true);
        }
        else {
            validationForBuild = false;
            this.isSuperMarketIsValidDelegate.accept(false);
        }
        return returnString;
    }

    private Set<Integer> builedIdSet(SDMStore store) {
        Set<Integer> returnSet = new HashSet<>();
        for (SDMSell id:store.getSDMPrices().getSDMSell()) {
            returnSet.add(id.getItemId());
        }
        return returnSet;
    }

    private boolean checkDiscountUnit(SDMDiscount discount, Set<Integer> idSet) {
        int ifYouBuyItemId = discount.getIfYouBuy().getItemId();
        if(!idSet.contains(ifYouBuyItemId))
            return false;
        else {
            List<SDMOffer> offerList = discount.getThenYouGet().getSDMOffer();
            for (SDMOffer offer:offerList) {
                if(!idSet.contains(offer.getItemId()))
                    return false;
            }
        }
        return true;
    }

//    private boolean checKIsCordUniq(SuperDuperMarketDescriptor temp) {
//        boolean returnBoolean =true;
//        List<Location> locations = new ArrayList<>();
//        List<SDMStore> stores = temp.getSDMStores().getSDMStore();
//        List<SDMCustomer> customers = temp.getSDMCustomers().getSDMCustomer();
//        for (SDMStore store : stores) {
//            locations.add(store.getLocation());
//        }
//        for (SDMCustomer customer:customers) {
//            locations.add(customer.getLocation());
//        }
//        int size = locations.size(); // locations size
//        for(int i = 0 ; i<size-1;++i) {
//            for (int j = i + 1; j < size; ++j) {
//                if(locations.get(i).getX() == locations.get(j).getX() &&
//                        locations.get(i).getY() == locations.get(j).getY())
//                    returnBoolean = false   ;
//            }
//        }
//        return returnBoolean;
//    }


    //3.3.3
    private boolean chekIsLegalDiscount(SuperDuperMarketDescriptor temp) {
        boolean returnBoolean = true;
        List<SDMStore> storeList = temp.getSDMStores().getSDMStore();
        for (SDMStore store:storeList) {
            Set<Integer> idSet = builedIdSet(store);
            if(store.getSDMDiscounts() != null) {
                List<SDMDiscount> discountList = store.getSDMDiscounts().getSDMDiscount();
                for (SDMDiscount discount : discountList) {
                    returnBoolean = checkDiscountUnit(discount, idSet);
                    if (!returnBoolean)
                        return returnBoolean;
                }
            }
        }
        return returnBoolean;
    }

    //3.3.2
//    private boolean checkIsCustomerIdUnique(SuperDuperMarketDescriptor temp) {
//        boolean returnBoolean =true;
//        List<SDMCustomer> arr = temp.getSDMCustomers().getSDMCustomer();
//        int size = arr.size();
//        for(int i = 0 ; i<size-1;++i) {
//            for (int j = i + 1; j < size; ++j) {
//                if (arr.get(i).getId() == arr.get(j).getId())
//                    returnBoolean = false;
//            }
//        }
//                return returnBoolean;
//    }

    //3.7 + 3.3.1
    private boolean

    checkLIsInCordRange(SuperDuperMarketDescriptor temp) {
        boolean returnBoolean = true;
        int x ,y ;
        for (SDMStore store: temp.getSDMStores().getSDMStore()) {
            x = store.getLocation().getX();
            y = store.getLocation().getY();
            if(x<1 || x>50 || y<1 || y>50)
                returnBoolean =false ;
        }
//        for (SDMCustomer customer:temp.getSDMCustomers().getSDMCustomer()) {
//            x = customer.getLocation().getX();
//            y = customer.getLocation().getY();
//            if(x<1 || x>50 || y<1 || y>50)
//                returnBoolean =false ;
//
//        }
        return returnBoolean;
    }

    //3.6
    private boolean checkIsItemDefinedOnlyOnceAtEachStore(SuperDuperMarketDescriptor temp) {
        boolean returnBoolean = true;
        int size ;
        for (SDMStore store: temp.getSDMStores().getSDMStore()) {
            size = store.getSDMPrices().getSDMSell().size();
            for(int i = 0 ; i < size-1 ;++i) {
                for (int j = i + 1; j < size; ++j) {
                    if (store.getSDMPrices().getSDMSell().get(i).getItemId() ==
                            store.getSDMPrices().getSDMSell().get(j).getItemId()) {
                        returnBoolean = false;
                    }
                }
            }
        }

        return returnBoolean;
    }

    //3.5
    private boolean checkIsEachProductSailedByOneStoreAtLeast(SuperDuperMarketDescriptor temp) {
        boolean returnBoolean = true;
        Set<Integer> itemsById = new HashSet<Integer>();
        Set<Integer> itemesIdByStore = new HashSet<Integer>();
        for (SDMItem item :temp.getSDMItems().getSDMItem()) {
            itemsById.add(item.getId());
        }
        for (SDMStore store: temp.getSDMStores().getSDMStore()) {
            for (SDMSell sell:store.getSDMPrices().getSDMSell()) {
                itemesIdByStore.add(sell.getItemId());
            }
        }
        if(!(itemesIdByStore.containsAll(itemsById)))
            returnBoolean =false;
        return returnBoolean;
    }

    //3.4
    private boolean checkIseReferencedToExistItem(SuperDuperMarketDescriptor temp){
        boolean returnBoolean = true;
        Set<Integer> itemsById = new HashSet<Integer>();
        for (SDMItem item :temp.getSDMItems().getSDMItem()) {
            itemsById.add(item.getId());
        }
        for (SDMStore store: temp.getSDMStores().getSDMStore()) {
            for (SDMSell sell:store.getSDMPrices().getSDMSell()) {
                if(!(itemsById.contains(sell.getItemId())))
                    returnBoolean =false;
            }
        }
        return returnBoolean;
    }

    //3.3
    private boolean checkIsStoreIdUnique(SDMStores sdmStores) {
        boolean returnBoolean = true;
        int size = sdmStores.getSDMStore().size();
        for(int i = 0 ; i<size-1;++i)
            for(int j=i+1;j<size;++j)
                if(sdmStores.getSDMStore().get(i).getId() == sdmStores.getSDMStore().get(j).getId() &&
                        !sdmStores.getSDMStore().get(i).getName().equals(sdmStores.getSDMStore().get(j).getName())){
                    returnBoolean = false;
                    break;
                }
        return returnBoolean;
    }

    //3.2
    private boolean checkIsItemIdUnique(SDMItems sdmItems) {
        boolean returneBoolean = true ;
        int size = sdmItems.getSDMItem().size();
        for(int i = 0 ; i<size-1 ; ++i)
            for(int j = i+1 ; j < size;++j )
                if(sdmItems.getSDMItem().get(i).getId() == sdmItems.getSDMItem().get(j).getId() &&
                        !sdmItems.getSDMItem().get(i).getName().equals(sdmItems.getSDMItem().get(j).getName())){
                    returneBoolean = false ;
                    break;
                }
        return returneBoolean ;
    }

}
