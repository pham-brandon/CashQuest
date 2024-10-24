package personal_finance_app_backend.services;

import java.sql.ResultSet;
import personal_finance_app_backend.dao.GoalDAO;
import personal_finance_app_backend.models.Goal;

public class GoalService {

    private GoalDAO goalDAO = new GoalDAO();

    // Add a new goal (Will need to look into uhh some kind of visualization library)
    public boolean addGoal(int userId, String name, double targetAmount, double currentAmount, String deadline) {
        Goal newGoal = new Goal(0, userId, name, targetAmount, currentAmount, deadline);
        return goalDAO.addGoal(newGoal);
    }

    // Retrieve goals for a user
    public ResultSet getUserGoals(int userId) {
        return goalDAO.getGoalsByUser(userId);
    }

    // Update goal progress
    public boolean updateGoalProgress(int goalId, double newCurrentAmount) {
        Goal goal = goalDAO.getGoalById(goalId);
        if (goal != null) {
            goal.setCurrentAmount(newCurrentAmount);
            return goalDAO.updateGoal(goal);
        }
        return false;
    }

    // Delete a goal
    public boolean deleteGoal(int goalId) {
        return goalDAO.deleteGoal(goalId);
    }
}
