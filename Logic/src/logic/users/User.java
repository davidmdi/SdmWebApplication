package logic.users;

public class User {
    private String name;
    private String type;

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
        return "User{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
