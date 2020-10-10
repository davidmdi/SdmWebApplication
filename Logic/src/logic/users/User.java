package logic.users;

public class User {
    private String name;
    private String type;
    public static final String OWNER = "owner";
    public static final String CUSTOMER = "customer";

    public User(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.getName() +  ", " +this.getType();
    }
}
