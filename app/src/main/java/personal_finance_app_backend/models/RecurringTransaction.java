package personal_finance_app_backend.models;

public class RecurringTransaction {
    private int recurringId;
    private int userId;
    private int categoryId;
    private double amount;
    private String description;
    private String startDate;
    private String frequency;  // 'daily', 'weekly', 'monthly', 'yearly' (if we are not implementing this I will get rid of it)
    private String nextDueDate;

    public RecurringTransaction(int recurringId, int userId, int categoryId, double amount, String description, String startDate, String frequency, String nextDueDate) {
        this.recurringId = recurringId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.startDate = startDate;
        this.frequency = frequency;
        this.nextDueDate = nextDueDate;
    }

    public int getRecurringId() { return recurringId; }
    public void setRecurringId(int recurringId) { this.recurringId = recurringId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public String getNextDueDate() { return nextDueDate; }
    public void setNextDueDate(String nextDueDate) { this.nextDueDate = nextDueDate; }
}
