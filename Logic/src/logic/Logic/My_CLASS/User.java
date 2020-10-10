package logic.Logic.My_CLASS;

public class User {
    private static int idGeneretor = 0 ;
    private int userId ;
    private String name;
    private String type;
    public static final String OWNER = "owner";
    public static final String CUSTOMER = "customer";

    public User(String name, String type) {
        this.name = name;
        this.type = type;
        this.userId = ++idGeneretor; // give id to user.
    }



    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return this.getName() +  ", " +this.getType();
    }
}
