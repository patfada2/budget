package mesh.budget.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import mesh.budget.Utils;

public class Budget {
	private static final Logger logger = LoggerFactory.getLogger(Budget.class);

	private ObservableList<BankStatementRow> budget;

	public ObservableList<BankStatementRow> getBudget() {
		return budget;
	}

	// de-duplicates
	public boolean add(BankStatementRow row) {
		boolean result = true;
		if (budget.indexOf(row) < 0) {
			budget.add(row);
			result = true;
		}
		return result;
	}

	public void loadExports() {
		List<File> exports = Utils.findExports(Utils.exportsDir);

		for (File e : exports) {
			addExportFile(e.getAbsolutePath());

		}

	}

	// load the csv file and add to the budget. Count the number of non-duplciates
	private void addExportFile(String filename) {
		int count = 0;
		logger.info("loading " + filename);
		ObservableList<BankStatementRow> rows = loadCsv(filename);

		Iterator<BankStatementRow> it = rows.iterator();
		while (it.hasNext()) {
			if (this.add(it.next())) {
				count++;
			}
		}

		if (count > 0) {
			logger.info(count + " new records found");
		} else
			logger.info("no new records found");

	}

	public void runMatches(Categories categories) {
		// ObservableList<BankStatementRow> budget;
		logger.info("running matches!!");
		Iterator<BankStatementRow> it = budget.iterator();

		while (it.hasNext()) {

			BankStatementRow row = it.next();
			if (row.getCategory().equals(Category.UNKNOWN)) {
				String match = categories.findReferenceMatch(row.getReference());
				if (match.equals(Category.UNKNOWN)) {
					match = categories.findDescriptionMatch(row.getDescription());
				}

				if (!match.equals(Category.UNKNOWN)) {
					row.setCategory(match);
					logger.info("match assigned: " + match);
				} else
					logger.info(row.getDescription() + " does not match any categories");

			}
		}
	}

	private ObservableList<BankStatementRow> loadCsv(String filename) {

		logger.info("loading file " + filename);
		ObservableList<BankStatementRow> rows = FXCollections.observableArrayList();

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;

			BankStatementRow row;
			// skip headers
			do {
				line = br.readLine();
				logger.info("!!@@## " + line);
				if (line == null)
					break;
			} while (!line.startsWith("202"));

			if (line != null)
				rows.add(BankStatementRow.CreateFromCsv(line));

			while ((line = br.readLine()) != null) {
				row = BankStatementRow.CreateFromCsv(line);
				logger.info(row.toString());
				rows.add(row);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rows;

	}

	public void renameCategories(String oldName, String newName) {

		logger.info("renaming categories from " + oldName + " to " + newName);

		int count = 0;
		Iterator<BankStatementRow> it = budget.iterator();

		while (it.hasNext()) {

			BankStatementRow row = it.next();
			if (row.getCategory().equals(oldName)) {
				row.setCategory(newName);
				count++;
			}
		}
		logger.info("renamed " + count + " categories from " + oldName + " to " + newName);
	}

	public void loadFromFile(String filename) {
		budget = loadCsv(filename);
	}

	public void saveToFile(String filename) {
		String str;
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(filename, false));
			Iterator<BankStatementRow> it = budget.iterator();
			writer.append(BankStatementRow.cSVHeader());
			while (it.hasNext()) {

				writer.append(it.next().toCsv());

			}
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ObservableList<PieChart.Data> calcCategoryTotals(Categories cats) {

		ObservableList<PieChart.Data> result = FXCollections.observableArrayList();


		for (Category cat : cats.getCategories()) {
			for (BankStatementRow row : this.budget) {
				if (row.getCategory().equals(cat.getName())) {
					cat.addToTotal(row.getAmount());
				}

			}
		}

		for (Category cat : cats.getCategories()) {
			PieChart.Data d = new PieChart.Data(cat.getName(), cat.getTotal());
			result.add(d);

		}

		return result;

	}

}