package logic.Logic.My_CLASS;

import java.util.HashSet;
import java.util.Set;

public class MyOwner {
    User user;
    private Set<String>  zonesNames ;

    public MyOwner(User user) {
        this.user = user;
        this.zonesNames = new HashSet<>();
    }

    public Set<String> getZonesNames() {
        return zonesNames;
    }

    public String getUserName() {
        return user.getName();
    }

    public int getCustomerId() {
        return user.getUserId();
    }


}

