package mesh.budget.model;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.SimpleStringProperty;
import mesh.budget.model.BankStatementRow;

public class BankStatementRow implements Comparable<BankStatementRow> {
	private static final Logger logger = LoggerFactory.getLogger(BankStatementRow.class);

	// Date Processed Date of Transaction Unique Id Tran Type Reference Description
	// Amount

	private SimpleStringProperty account;
	private SimpleStringProperty dateProcessed;
	private SimpleStringProperty id;
	private SimpleStringProperty type;
	private SimpleStringProperty reference;
	private SimpleStringProperty description;
	private SimpleStringProperty amount;
	private SimpleStringProperty category;

	public enum RowType {
		CREDIT, DEBIT
	};

	public enum Account {
		Business, // 0148824-01 (Business Account)
		Streamline, // Account 0131102-00 (Streamline)
		Visa, Unknown
	};

	public Month getMonth() {
		String dateStr = this.getDateProcessed();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.ENGLISH);
		LocalDate date = LocalDate.parse(dateStr, formatter);

		return date.getMonth();
	}

	public BankStatementRow(String account, String dateProcessed, String id, String type, String reference,
			String description, String amount, String category) {
		logger.debug("creating new BankStatementRow");
		this.account = new SimpleStringProperty(account);
		this.dateProcessed = new SimpleStringProperty(dateProcessed);
		this.id = new SimpleStringProperty(id);
		this.type = new SimpleStringProperty(type);
		this.reference = new SimpleStringProperty(reference);
		this.description = new SimpleStringProperty(description);
		this.amount = new SimpleStringProperty(amount);
		// to ensure negation of visa
		this.setAmount(amount);
		this.category = new SimpleStringProperty(category);
	}

	public static BankStatementRow CreateFromCsv(Account account, String line) {
		line = line.replace("\\,", "_");
		// ignore commas between quotes
		String[] v = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		ArrayList<String> values = new ArrayList<String>();
		for (int i = 0; i < v.length; i++)
			values.add(v[i]);
		if (values.size() < 8) {
			values.add(Category.UNKNOWN);
		}
		BankStatementRow row;

		if (Account.Visa.equals(account)) {// Date Processed,Date of Transaction,Unique Id,Tran
											// Type,Reference,Description,Amount
			row = new BankStatementRow(account.name(), values.get(0), values.get(2), values.get(3), values.get(4),
					values.get(5), values.get(6), values.get(7));

		} else {
			// Date,Unique Id,Tran Type,Cheque Number,Payee,Memo,Amount

			row = new BankStatementRow(account.name(), values.get(0), values.get(1), values.get(2), values.get(4),
					values.get(5), values.get(6), values.get(7));
		}
		return row;

	}

	public static BankStatementRow CreateFromCsv(String line) {
		line = line.replace("\\,", "_");
		// ignore commas between quotes
		String[] v = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		ArrayList<String> values = new ArrayList<String>();
		for (int i = 0; i < v.length; i++)
			values.add(v[i]);
		if (values.size() < 8) {
			values.add(Category.UNKNOWN);
		}

		if (values.size() != 8) {
			logger.error("invalid row " + line);
			return null;

		} else {

			BankStatementRow row = new BankStatementRow(values.get(0), values.get(1), values.get(2), values.get(3),
					values.get(4), values.get(5), values.get(6), values.get(7));
			return row;
		}

	}

	private RowType typeToRowType(String typrStr) {
		RowType result;
		if (typrStr.contentEquals("DEBIT")) {
			result = RowType.DEBIT;
		} else
			result = RowType.CREDIT;
		return result;
	}

	private static String COMMA_DELIMITER = ",";

	public String toString() {
		return "account=" + account.getValue() + " dateProcessed=" + dateProcessed.getValue() + " id=" + id.getValue()
				+ " type=" + type.getValue() + " reference=" + reference.getValue() + " description="
				+ description.getValue() + " amount=" + amount.getValue() + " category=" + category.getValue();
	}

	public static String cSVHeader() {
		return "account,dateProcessed,id,type,referenece,description,amount,category\n";
	}

	public String toCsv() {
		return account.get() + "," + dateProcessed.get() + "," + id.get() + "," + type.get() + "," + reference.get()
				+ "," + description.get() + "," + amount.get() + "," + category.get() + "\n";
	}

	public String getAccount() {
		return account.get();
	}

	public void setAccount(String acc) {
		this.account.set(acc);
	}

	public String getDateProcessed() {
		return dateProcessed.get();
	}

	public String getId() {
		return id.get();
	}

	public void setId(String id) {
		this.id.set(id);
	}

	public String getType() {
		return type.get();
	}

	public void setType(String type) {
		this.type.set(type);
	}

	public String getReference() {
		return reference.get();
	}

	public void setReference(String reference) {
		this.reference.set(reference);
	}

	public String getDescription() {
		return description.get();
	}

	public void setDescription(String description) {
		this.description.set(description);
	}

	public String getAmount() {
		return amount.get();
	}

	public void setAmount(String amount) {
		this.amount.set(amount);
	}

	public String getCategory() {
		return category.get();
	}

	public void setCategory(String category) {
		this.category.set(category);
	}

	@Override
	public int compareTo(BankStatementRow o) {

		int result = this.id.get().compareTo(o.getId());

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj.getClass() != this.getClass()) {
			return false;
		}

		final BankStatementRow other = (BankStatementRow) obj;
		if (this.id.equals(other.id) && this.account.equals(other.account)) {
			return true;

		} else
			return false;

	}

}
