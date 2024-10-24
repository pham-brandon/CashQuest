package personal_finance_app_backend.models;

public class Users {
    private int userId;
    private String name;
    private String email;
    private String password;
    private String createdAt;

    public Users(int userId, String name, String email, String password, String createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
