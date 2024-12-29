package mesh.budget.model;

import javafx.beans.property.SimpleStringProperty;
import mesh.budget.model.BankStatementRow;

public class BankStatementRow  implements Comparable<BankStatementRow>{

	// Date Processed Date of Transaction Unique Id Tran Type Reference Description
	// Amount

	private SimpleStringProperty  dateProcessed;
	private SimpleStringProperty  dateOfTransaction;
	private SimpleStringProperty  id;
	private SimpleStringProperty  type;
	private SimpleStringProperty  reference;
	private SimpleStringProperty  description;
	private SimpleStringProperty  amount;
	
	

	
	public BankStatementRow(String dateProcessed, String dateOfTransaction, String id, String type, 
			 String reference, String description, String amount) {
	        this.dateProcessed = new SimpleStringProperty(dateProcessed);
	        this.dateOfTransaction = new SimpleStringProperty(dateOfTransaction);
	        this.id = new SimpleStringProperty(id);
	        this.type = new SimpleStringProperty(type);
	        this.reference = new SimpleStringProperty(reference);
	        this.description = new SimpleStringProperty(description);
	        this.amount = new SimpleStringProperty(amount);
	    }
	
	public static BankStatementRow CreateFromCsv(String line) {
		String[] values = line.split(COMMA_DELIMITER);		
		BankStatementRow row = new BankStatementRow(values[0],values[1],values[2],values[3],values[4],values[5],values[6]);		
		return row;

	}

	private static String COMMA_DELIMITER = ",";
	

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
		return dateProcessed.get();
	}

	public void setDateProcessed(String dp) {
		this.dateProcessed.set(dp);
	}

	public String getDateOfTransaction() {
		return dateOfTransaction.get();
	}

	public void setDateOfTransaction(String dateOfTransaction) {
		this.dateOfTransaction.set(dateOfTransaction);
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

	@Override
	public int compareTo(BankStatementRow o) {
		
		int result =  this.id.get().compareTo(o.getId());
		
		 return result;
	}

}
