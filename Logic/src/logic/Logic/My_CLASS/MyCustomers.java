//package logic.Logic.My_CLASS;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class MyCustomers {
//
//    private List<MyCustomer> customerList;
//    private Map<Integer,MyCustomer> customerMap;
//
//    public MyCustomers(SDMCustomers sdmCustomers) {
//        this.sdmCustomers = sdmCustomers;
//        this.customerList = buildCustomerList(sdmCustomers);
//        this.customerMap = buildCustomerMap();
//    }
//
//    private Map<Integer, MyCustomer> buildCustomerMap() {
//        Map<Integer,MyCustomer> map = new HashMap<>();
//        for (MyCustomer customer:customerList) {
//            map.put(customer.getId(),customer);
//
//        }
//        return map;
//    }
//
//    private List<MyCustomer> buildCustomerList(SDMCustomers sdmCustomers) {
//        List<MyCustomer> list = new ArrayList<>();
//        for (SDMCustomer customer: sdmCustomers.getSDMCustomer()) {
//            list.add(new MyCustomer(customer));
//        }
//        return list;
//    }
//
//    public SDMCustomers getSdmCustomers() {
//        return sdmCustomers;
//    }
//
//    public void setSdmCustomers(SDMCustomers sdmCustomers) {
//        this.sdmCustomers = sdmCustomers;
//    }
//
//    public List<MyCustomer> getCustomerList() {
//        return customerList;
//    }
//
//    public void setCustomerList(List<MyCustomer> customerList) {
//        this.customerList = customerList;
//    }
//
//    public Map<Integer, MyCustomer> getCustomerMap() {
//        return customerMap;
//    }
//
//    public void setCustomerMap(Map<Integer, MyCustomer> customerMap) {
//        this.customerMap = customerMap;
//    }
//
//    public ObservableList<MyCustomer> getCustomerObesrvableList() {
//        ObservableList<MyCustomer> returnValue = FXCollections.observableArrayList();
//        List<MyCustomer> customers = this.getCustomerList() ;
//        for (MyCustomer customer : customers) {
//            returnValue.add(customer);
//        }
//        return returnValue;
//    }
//
//    public List<String> getCustomerNames() {
//        List<String> list = new ArrayList<>();
//        for (MyCustomer customer:this.customerList) {
//            list.add(customer.getName() + " ,id: " + customer.getId());
//        }
//        return list;
//    }
//
//
//}
