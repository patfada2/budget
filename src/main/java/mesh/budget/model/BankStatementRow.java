package mesh.budget.model;

public class BankStatementRow  implements Comparable<BankStatementRow>{

	// Date Processed Date of Transaction Unique Id Tran Type Reference Description
	// Amount

	private String dateProcessed;
	private String dateOfTransaction;
	private String id;
	private String type;
	private String reference;
	private String description;
	private String amount;
	
	private static String COMMA_DELIMITER = ",";
	
	public static BankStatementRow CreateFromCsv(String line) {
		String[] values = line.split(COMMA_DELIMITER);
		BankStatementRow row = new BankStatementRow();
		row.setDateProcessed(values[0]);
		row.setDateOfTransaction(values[1]);
		row.setId(values[2]);
		row.setType(values[3]);
		row.setReference(values[4]);
		row.setDescription(values[5]);
		row.setAmount(values[6]);
		return row;

	}

	public String toString() {
		return "dateProcessed=" + dateProcessed + "dateOfTransaction=" + dateOfTransaction + "id=" + id + "type=" + type
				+ "referenece=" + reference + "description=" + description + "amount=" + amount;
	}

	public static String cSVHeader() {
		return "dateProcessed=,dateOfTransaction,id,type,referenece,description,amount\n";
	}
	
	public String toCsv() {
		return dateProcessed + "," + dateOfTransaction + "," + id + "," + type
				+ "," + reference + "," + description + "," + amount + "\n";
	}

	public String getDateProcessed() {
		return dateProcessed;
	}

	public void setDateProcessed(String dateProcessed) {
		this.dateProcessed = dateProcessed;
	}

	public String getDateOfTransaction() {
		return dateOfTransaction;
	}

	public void setDateOfTransaction(String dateOfTransaction) {
		this.dateOfTransaction = dateOfTransaction;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public int compareTo(BankStatementRow o) {
		 return this.id.compareTo(o.getId());
	}

}
