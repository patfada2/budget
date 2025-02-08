package mesh.budget.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.SimpleSetProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.Parent;
import javafx.scene.Group;

import mesh.budget.Utils;
import mesh.budget.model.BankStatementRow.Account;

public class Budget {
	private static final Logger logger = LoggerFactory.getLogger(Budget.class);

	private ObservableList<BankStatementRow> budget;

	public ObservableList<BankStatementRow> getBudget() {
		return budget;
	}

	public int dedupe() {
		logger.info("looking for duplicates");
		Set<BankStatementRow> hashset = new HashSet<BankStatementRow>();
		int count = 0;

		BankStatementRow row;
		for (int i = 0; i < budget.size(); i++) {
			row = budget.get(i);

			if (!hashset.add(row)) {
				logger.info("id " + row.getId() + " is a duplicate");
				count++;
				logger.debug("count = " + count);
			}
		}

		return count;
	}

	// de-duplicates
	public boolean add(BankStatementRow row) {
		/*
		 * boolean result = false; if (budget.indexOf(row) < 0) { budget.add(row);
		 * result = true; } else { logger.info("duplicate found :" + row.getId()); }
		 */
		boolean result = true;
		if (budget.indexOf(row) > 0) {
			logger.info("duplicate found :" + row.getId());
		} else
			budget.add(row);
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
		ObservableList<BankStatementRow> rows = loadExportCsv(filename);

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

	private Account getAccount(String line) {
		Account result = Account.Unknown;

		for (int i = 0; i < Account.values().length; i++) {
			if (line.contains("Card Number")) {
				result = Account.Visa;
				break;
			} else if (line.contains(Account.values()[i].name())) {
				result = Account.values()[i];
				break;
			}
		}

		return result;

	}

	private ObservableList<BankStatementRow> loadExportCsv(String filename) {

		logger.info("loading file " + filename);
		ObservableList<BankStatementRow> rows = FXCollections.observableArrayList();

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;

			BankStatementRow row;
			Account account = Account.Unknown;
			// skip headers
			do {
				line = br.readLine();
				if (line == null)
					break;
				if (line.contains("Account") || line.contains("Card Number")) {
					account = getAccount(line);
					logger.info("account = " + account.name());
				}
			} while (!line.startsWith("202"));

			if (line != null)
				rows.add(BankStatementRow.CreateFromCsv(account, line));

			while ((line = br.readLine()) != null) {
				row = BankStatementRow.CreateFromCsv(account, line);
				logger.debug(row.toString());
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

	private ObservableList<BankStatementRow> loadCsv(String filename) {

		logger.info("loading budget file " + filename);
		ObservableList<BankStatementRow> rows = FXCollections.observableArrayList();

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;

			BankStatementRow row;
			Account account = Account.Unknown;
			// skip headers
			line = br.readLine();

			while ((line = br.readLine()) != null) {
				row = BankStatementRow.CreateFromCsv(line);
				logger.debug(row.toString());
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
		logger.info("calculating category totals");

		ObservableList<PieChart.Data> result = FXCollections.observableArrayList();
		// clear totals
		for (Category cat : cats.getCategories()) {
			cat.setTotal(0);
		}
		// calc totals
		for (Category cat : cats.getCategories()) {
			for (BankStatementRow row : this.budget) {
				if (row.getCategory().equals(cat.getName())) {
					cat.addToTotal(row.getAmount());
				}
				if (row.getCategory().equals("Food") && cat.getName().equals("Food")) {
					logger.debug("!!!!added Food " + row.getAmount() + "to total, now " + cat.getTotal());
				}

			}
		}

		for (Category cat : cats.getCategories()) {
			if (!cat.getName().equals("Transfer")) {
				PieChart.Data d = new PieChart.Data(cat.getName(), cat.getTotal());
				result.add(d);
				logger.info("category " + cat.getName() + " total " + cat.getTotal());
			}

		}

		return result;

	}

	public XYChart.Series<String, Number> calcCategoryTotalsForMonth(Categories cats, Month month) {
		logger.info("calculating totals for month: " + month.name());
		// clear totals
		for (Category cat : cats.getCategories()) {
			cat.setTotal(0);
		}
		for (Category cat : cats.getCategories()) {
			for (BankStatementRow row : this.budget) {
				if (row.getCategory().equals(cat.getName()) && row.getMonth().equals(month)) {
					cat.addToTotal(row.getAmount());
				}

			}
		}

		XYChart.Series<String, Number> result = new XYChart.Series<>();

		for (Category cat : cats.getCategories()) {
			if (!cat.getName().equals("Transfer")) {
				result.getData().add(new XYChart.Data<String, Number>(cat.getName(), cat.getTotal()));
				logger.info("category " + cat.getName() + " total " + cat.getTotal());
			}

		}

		return result;
	}

	public ArrayList<XYChart.Series<String, Number>> calcMonthTotalsPerCategory(Categories categories) {
		logger.info("calcMonthTotalsPerCategory");

		Category theCat = null;
		Double theTotal = Double.valueOf(0);
		Month month = null;

		// calculate totals
		for (BankStatementRow row : this.budget) {
			theCat = categories.getCategoryByName(row.getCategory());

			month = row.getMonth();
			theTotal = theCat.getMonthTotals().get(month);
			theTotal = theTotal + Double.parseDouble(row.getAmount());
			theCat.getMonthTotals().put(month, theTotal);
		}

		// populate Series for response
		ArrayList<XYChart.Series<String, Number>> barchartdata = new ArrayList<XYChart.Series<String, Number>>();

		for (Category cat : categories.getCategories()) {

			if (!cat.getName().equals("Transfer")) {
				logger.debug("!!!!!!!!!!!!"+ cat.getName());

				XYChart.Series<String, Number> series = new XYChart.Series<>();
				series.setName(cat.getName());
				// series.getNode().setStyle(null);

				for (Month m : Month.values()) {
					Double total = cat.getMonthTotals().get(m);
					total = total * -1;
					// Double total = Double.valueOf(100);
					XYChart.Data<String, Number> p = new XYChart.Data<String, Number>(m.name(), total);

					p.nodeProperty().addListener(new ChangeListener<Node>() {
						@Override
						public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
							if (node != null) {
								setNodeStyle(p, categories.getCatColour(cat.getName()));
								//displayLabelForData(p);
							}
						}
					});

					logger.debug(cat.getName() + "total for month " + m.name() + "=" + total);
					series.getData().add(p);
				}

				barchartdata.add(series);
			}
		}

		return barchartdata;
	}

	
	private void displayLabelForData(XYChart.Data<String, Number> data) {
		    final Node node = data.getNode();
		    final Text dataText = new Text(data.getYValue() + "");
		    node.parentProperty().addListener(new ChangeListener<Parent>() {
		      @Override public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
		        Group parentGroup = (Group) parent;
		        parentGroup.getChildren().add(dataText);
		      }
		    });
		    
	 }
	
	private void setNodeStyle(XYChart.Data<String, Number> data, String colour) {
		Node node = data.getNode();
		node.setStyle(colour);
	}

}