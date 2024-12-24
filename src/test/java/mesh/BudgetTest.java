package mesh;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import mesh.budget.BudgetClassifier;
import mesh.budget.model.BankStatementRow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BudgetTest {
	private static final Logger logger = LoggerFactory.getLogger(BudgetTest.class);

	
	@Test
	public void classifier() {
		logger.info("testing");
		BudgetClassifier c = new BudgetClassifier();
		c.addMatch("Insurance", "IAG NZ");
		String cat = c.getCategory("IAG NZ - ASB INSURANCE AUCKLAND");
	
		assertEquals(cat, "Insurance");
		
		
	}
		
}
