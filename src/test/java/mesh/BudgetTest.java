package mesh;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


import mesh.budget.Utils;
import mesh.budget.model.BankStatementRow;
import mesh.budget.model.Budget;
import mesh.budget.model.Categories;
import mesh.budget.model.Category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BudgetTest {
	private static final Logger logger = LoggerFactory.getLogger(BudgetTest.class);

	
	@Test
	public void classifier() {
		logger.info("testing");
		//BudgetClassifier c = new BudgetClassifier();
		
		Budget budget = new Budget();
		budget.loadFromFile(Utils.budgetFileName);
		budget.loadExports();
		
		logger.info(budget.getBudget().size() + " items laoded");
		
		logger.info("still testing");
		Categories categories = new Categories();
		categories.loadFromFile(Utils.categoryFileName);
		logger.info("categories count" + categories.getCategories().size());
		
		
		
		//budget.runMatches(categories);
		
		BankStatementRow row = budget.getBudget().get(0);
		row.setCategory("Food");
		categories.getCategoryByName("Food").getMatches().add("EFTPOS");
		logger.info("row 0:"+row.getDescription());
		
		String match = categories.findMatch(row.getDescription());
		
		logger.info("done testing");
		
		
	}
		
}
