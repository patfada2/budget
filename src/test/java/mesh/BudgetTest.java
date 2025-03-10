package mesh;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;


import mesh.budget.Utils;
import mesh.budget.model.BankStatementRow;
import mesh.budget.model.BankStatementRow.Account;
import mesh.budget.model.Budget;
import mesh.budget.model.Categories;
import mesh.budget.model.Category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



public class BudgetTest {
	private static final Logger logger = LoggerFactory.getLogger(BudgetTest.class);

	
	//@Test
	public void budget() {
		logger.info("testing");
		//BudgetClassifier c = new BudgetClassifier();
		
		Budget budget = new Budget();
		//budget.loadFromFile(Utils.budgetFileName);
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
		
		String match = categories.findDescriptionMatch(row.getDescription());
		
		logger.info("done testing");
		
		
	}
	
	@Test
	public void exportLoadVisa() {
		Budget budget = new Budget();
		String filename = "C:\\Users\\patri\\Downloads\\Export20241224162534.csv";
		budget.loadFromFile(filename);
		logger.info("loaded " + budget.getBudget().size() + " records");
		assertEquals(578,  budget.getBudget().size());
		
	}
	
	@Test
	public void exportLoadStreamline() {
		Budget budget = new Budget();
		
		String filename = "C:\\Users\\patri\\Downloads\\Export20241224162527.csv";
		budget.loadFromFile(filename);
		logger.info("loaded " + budget.getBudget().size() + " records");
		assertEquals(178,  budget.getBudget().size());
		
	}
	
	@Test
	public void streamlineLoad() {
		
		String line = "2024/06/13,2024061302,D/C,,\"D/C FROM BRADLEY, J R\",\"beer mule fe  Dad\",60.00";
		
		BankStatementRow row =BankStatementRow.CreateFromCsv(Account.Streamline, line);
		
		logger.info(row.toString());
	}
	
	@Test
	public void visaLoad() {
		
		String line = "2024/06/10,2024/06/09,2024061001,DEBIT,8128,\"MITRE 10 MEGA KAPITI PARAPARAUMU\",23.98";
		
		BankStatementRow row =BankStatementRow.CreateFromCsv(Account.Visa, line);
		
		logger.info(row.toString());
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
		
		
		String result = cat.findDescriptionMatch(description);
		if (result != null) {
			logger.info("matching category is " + result);
		}
	    assertEquals("Food",result);
			
	}
	
	
	@Test
	public void matchReference() {
		
		logger.info("testing reference matcher");
		
		Category cat = new Category("Food");
		List<String> matches = new ArrayList<String>();
		matches.add("Southern Cross");
		cat.setRefernceMatches(matches);
		
		String dateProcessed="2024/10/24";
		String dateOfTransaction="2024102402";
		String id="1";
		String type= "D/D";
		String reference="Southern Cross Health";
		String description="countdown";
		String amount="109.21";
		String category="no category";
		
		BankStatementRow row = new BankStatementRow(dateProcessed,dateOfTransaction,id,type,reference,description,amount,category);
		
		
		String result = cat.findReferenceMatch(reference);
		if (result != null) {
			logger.info("matching category is " + result);
		}
	    assertEquals("Food",result);
			
	}
	
	@Test
	public void categoryFile() {
		Categories categories = new Categories();
		Category cat = new Category("Food");
		List<String> matches = new ArrayList<String>();
		matches.add("Southern Cross");
		cat.setDescriptionMatches(matches);
		cat.setReferenceMatches(matches);
		
		categories.add(cat);
	
		
		//Gson gson = new Gson();	
		//logger.info(cat.toCsv());
		logger.info(cat.toJson().toString());
		
	
		//categories.saveToFile(Utils.categoryFileName);
		
		//categories.loadFromFile(Utils.categoryFileName);
		//cat = categories.getCategoryByName("Food");
		//assertNotNull(cat);
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			String catAsString = objectMapper.writeValueAsString(cat);
			
			Category cat2 = objectMapper.readValue(catAsString, Category.class);	
			String cat2AsString = objectMapper.writeValueAsString(cat2);
			
			assertEquals(catAsString, cat2AsString);
			
			logger.info(cat2.getName());
			assertEquals(cat.getDescriptionMatches().get(0), cat2.getDescriptionMatches().get(0));
			logger.info(cat2.getDescriptionMatches().get(0));
			
			
			String s2 = cat.toJson();
			logger.info("!!!!!!!!!!!!"+s2);
			
			Category cat3 = Category.createFromJson(s2);
			logger.info(cat3.getDescriptionMatches().get(0));
			
			categories.saveToFile(Utils.categoryFileName+"_test");
			categories.loadFromFile(Utils.categoryFileName+"_test");
			
			logger.info(categories.getCategories().get(0).getDescriptionMatches().get(0));
			
			
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//List<Strinh> listCar = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>(){});
		
	}
	
	
}
