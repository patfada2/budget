package mesh.budget.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Budget {
	private static final Logger logger = LoggerFactory.getLogger(Budget.class);

	private Set<BankStatementRow> budget;
	
	
    public Set<BankStatementRow> getBudget() {
		return budget;
	}

	// load the csv file and add to the budget. Count the number of non-duplciates
	public void addExportFile(String filename) {
		int count = 0;
		logger.info("loading " + filename);
		Set<BankStatementRow> rows = loadCsv(filename);
		
		Iterator<BankStatementRow> it = rows.iterator();
		while (it.hasNext()) {
			if (budget.add(it.next())) {
				count++;
			}
		}		
		
		if (count > 0) {
			logger.info(count + " new records found");
		}
		else
			logger.info("no new records found");
		
	}

	private Set<BankStatementRow> loadCsv(String filename) {
		Set<BankStatementRow> rows = new TreeSet<BankStatementRow>();
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			// skip headers
			do {
				line = br.readLine();
				if (line == null)
					break;
			} while (!line.startsWith("202"));

			if (line != null)
				rows.add(BankStatementRow.CreateFromCsv(line));

			while ((line = br.readLine()) != null) {
				BankStatementRow row = BankStatementRow.CreateFromCsv(line);
				logger.trace(row.toString());
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

}