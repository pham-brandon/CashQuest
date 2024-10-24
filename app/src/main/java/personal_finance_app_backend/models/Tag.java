package personal_finance_app_backend.models;

public class Tag {
    private int tagId;
    private String name;
    private int userId;

    public Tag(int tagId, String name, int userId) {
        this.tagId = tagId;
        this.name = name;
        this.userId = userId;
    }

    public int getTagId() { return tagId; }
    public void setTagId(int tagId) { this.tagId = tagId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
