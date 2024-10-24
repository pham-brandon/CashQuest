package personal_finance_app_backend.services;
import java.sql.ResultSet;
import personal_finance_app_backend.dao.BudgetDAO;
import personal_finance_app_backend.models.Budget;

public class BudgetService {

    private BudgetDAO budgetDAO = new BudgetDAO();

    // Add a new budget for a user
    public boolean addBudget(int userId, int categoryId, double amount, String period, String startDate, String endDate) {
        Budget newBudget = new Budget(0, userId, categoryId, amount, period, startDate, endDate);
        return budgetDAO.addBudget(newBudget);
    }

    // Retrieve budgets for a user
    public ResultSet getUserBudgets(int userId) {
        return budgetDAO.getBudgetsByUser(userId);
    }

    // Check if a budget has been exceeded (based on transactions)
    public boolean isBudgetExceeded(int userId, int categoryId, double totalSpent) {
        ResultSet resultSet = budgetDAO.getBudgetsByUser(userId);
        try {
            while (resultSet.next()) {
                if (resultSet.getInt("category_id") == categoryId && totalSpent > resultSet.getDouble("amount")) {
                    return true; // Budget exceeded
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update a budget
    public boolean updateBudget(Budget budget) {
        return budgetDAO.updateBudget(budget);
    }

    // Delete a budget
    public boolean deleteBudget(int budgetId) {
        return budgetDAO.deleteBudget(budgetId);
    }
}
