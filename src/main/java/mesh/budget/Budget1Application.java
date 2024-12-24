package mesh.budget;

import java.io.File;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import mesh.budget.model.Budget;

@SpringBootApplication
public class Budget1Application {
	
	
	static String budgetFileName = "C:\\Users\\patri\\git\\budget\\budget.csv";

	
	
	
	public static void main(String[] args) {
		SpringApplication.run(Budget1Application.class, args);
		Budget budget = new Budget();
		budget.loadFromFile(Budget1Application.budgetFileName);
		//add exports
		
		List<File> exports = Utils.findExports("C:\\Users\\patri\\Downloads");
		
		for (File e:exports) {
			budget.addExportFile(e.getAbsolutePath());
						
		}
		
		budget.saveToFile(Budget1Application.budgetFileName);
		

	}

}
