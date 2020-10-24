package logic.Logic.My_CLASS;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MyUsers {
    private Set<User> userSet;
    private Set<MyOwner> ownerSet;
    private Set<MyCustomer> customerSet;

    public MyUsers() {
        this.userSet = new HashSet<>();
        this.ownerSet = new HashSet<>();
        this.customerSet = new HashSet<>();
    }

    public synchronized Set<MyOwner> getOwnerSet() {
        return Collections.unmodifiableSet(this.ownerSet);
    }

    public synchronized Set<MyCustomer> getCustomerSet() {
        return Collections.unmodifiableSet(this.customerSet);
    }


    public synchronized void  addUser(String userName , String userType){
        // creat user and add to correct data structure.
        User user = new User(userName,userType);
        userSet.add(user);
        if(userType.equals(User.CUSTOMER))
            this.customerSet.add(new MyCustomer(user));
        else
            this.ownerSet.add(new MyOwner(user)) ;
    }

    public boolean isUserExists(String username) {
        //Set<User> usersSet = getAllUsersSet();
        for (User user : this.userSet) {
            if(user.getName().equals(username))
                return true;
        }
        return false;
    }

    public synchronized void removeUser(User user) {
        if(user.getType().equals(User.CUSTOMER))
            this.customerSet.remove(user);
        else
            this.ownerSet.remove(user);

        this.userSet.remove(user);
    }

    public synchronized Set<User> getUserSet() {
        return userSet;
    }

    /*
            private synchronized Set<User> getAllUsersSet(){
                     Set<User> usersSet = new HashSet<>();
                     usersSet.addAll(this.getCustomerSet());
                     usersSet.addAll(this.getOwnerSet());
                     retn usersSet;
            }
        /*
            public synchronized Set<User> getUsers(){ // creating users instance just present  online users in sights.
                Set<User> usersSet = new HashSet<>();
                for(MyCustomer customer : getCustomerSet()) {
                    usersSet.add(new User(customer.getName(),customer.getType()));
                }
                for(MyOwner owner : getOwnerSet()) {
                    usersSet.add(new User(owner.getName(), owner.getType()));
                }

                return usersSet;
            }
        */
    public  User getUserByName(String userName) {
       // Set<User> usersSet = getAllUsersSet();

        for (User user : this.userSet) {
            if (user.getName().equals(userName))
                return user;
        }
        return  null;
    }


    public MyOwner findOwnerByName(String ownerName) {
        for (MyOwner owner : this.getOwnerSet()) {
            if(owner.getUserName().equals(ownerName))
                return owner;
        }
        return null; // should not come to this return ever
    }

    public synchronized MyCustomer findCustomerByName(String customerName){
        for(MyCustomer customer : this.getCustomerSet()){
            if(customer.getUserName().equals(customerName))
                return customer;
        }
        return null; // should not come to this return ever
    }
}
