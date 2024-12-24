package mesh.budget;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import mesh.budget.model.BankStatementRow;
import mesh.budget.model.Budget;

@SpringBootApplication
public class Budget1Application {
	
	
	static String budgetFileName = "C:\\Users\\patri\\git\\budget\\budget.csv";

	
	
	
	public static void main(String[] args) {
		SpringApplication.run(Budget1Application.class, args);
		Budget budget = new Budget();
		budget.loadFromFile(Budget1Application.budgetFileName);
		//add exports
		budget.addExportFile("C:\\Users\\patri\\Downloads\\Export20241223152105.csv");
		budget.saveToFile(Budget1Application.budgetFileName);
		

	}

}
