package logic.Logic.My_CLASS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyOwner {
    User user;
    private Set<String>  zonesNames ;
    private List<Alertable> alerts;

    public MyOwner(User user) {
        this.user = user;
        this.zonesNames = new HashSet<>();
        this.alerts = new ArrayList<>();
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

    public void addAlert(Alertable alert){
        this.alerts.add(alert);
    }

    public void removeAlert(Alertable alert){
        this.alerts.remove(alert);
    }

    public void removeAllAlerts(){
        this.alerts.clear();
    }

    public synchronized List<Alertable> getAlerts() {
        return alerts;
    }
}

