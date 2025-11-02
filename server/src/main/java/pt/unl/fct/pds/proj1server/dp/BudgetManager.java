package pt.unl.fct.pds.proj1server.dp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BudgetManager {

    private double remainingBudget;

    public BudgetManager(@Value("${dp.totalBudget}") double totalBudget) {
        this.remainingBudget = totalBudget;
    }

    public synchronized double getRemainingBudget() {
        return remainingBudget;
    }

    public synchronized void reduce(double amount) {
        this.remainingBudget = this.remainingBudget - amount;
        if (this.remainingBudget < 0) {
            throw new BudgetExceededException("budget exceeded");
        }
    }
}
