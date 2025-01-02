package mesh;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

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

	
	//@Test
	public void descriptionMatches() {
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
		categories.getCategoryByName("Food").getDescriptionMatches().add("EFTPOS");
		logger.info("row 0:"+row.getDescription());
		
		String match = categories.findMatch(row.getDescription());
		
		logger.info("done testing");
		
		
	}
		
	@Test
	public void matcher() {
		
		logger.info("testing matcher");
		
		Category cat = new Category("Food");
		List<String> matches = new ArrayList<String>();
		matches.add("countdown");
		cat.setDescriptionMatches(matches);
		
		String dateProcessed="2024/10/24";
		String dateOfTransaction="2024102402";
		String id="1";
		String type= "D/D";
		String reference="Southern Cross Health";
		String description="countdown";
		String amount="109.21";
		String category="no category";
		
		BankStatementRow row = new BankStatementRow(dateProcessed,dateOfTransaction,id,type,reference,description,amount,category);
		
		
		String result = cat.findMatch(description);
		if (result != null) {
			logger.info("matching category is " + result);
		}
	    assertEquals("Food",result);
			
	}
	
	@Test
	public void categoryFile() {
		Categories categories = new Categories();
		categories.loadFromFile(Utils.categoryFileName);
		logger.info("categories count" + categories.getCategories().size());
		categories.saveToFile(Utils.categoryFileName);
		
	}
	
	
}
