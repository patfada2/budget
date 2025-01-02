package mesh.budget.model;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.SimpleStringProperty;
import mesh.budget.model.BankStatementRow;

public class BankStatementRow  implements Comparable<BankStatementRow>{
	private static final Logger logger = LoggerFactory.getLogger(BankStatementRow.class);

	// Date Processed Date of Transaction Unique Id Tran Type Reference Description
	// Amount

	private SimpleStringProperty  dateProcessed;
	private SimpleStringProperty  dateOfTransaction;
	private SimpleStringProperty  id;
	private SimpleStringProperty  type;
	private SimpleStringProperty  reference;
	private SimpleStringProperty  description;
	private SimpleStringProperty  amount;
	private SimpleStringProperty  category;
	
	

	
	public BankStatementRow(String dateProcessed, String dateOfTransaction, String id, String type, 
			 String reference, String description, String amount, String category) {
		    logger.debug("creating new BankStatementRow");
	        this.dateProcessed = new SimpleStringProperty(dateProcessed);
	        this.dateOfTransaction = new SimpleStringProperty(dateOfTransaction);
	        this.id = new SimpleStringProperty(id);
	        this.type = new SimpleStringProperty(type);
	        this.reference = new SimpleStringProperty(reference);
	        this.description = new SimpleStringProperty(description);
	        this.amount = new SimpleStringProperty(amount);
	        this.category = new SimpleStringProperty(category);
	    }
	
	public static BankStatementRow CreateFromCsv(String line) {
		String[] v = line.split(COMMA_DELIMITER);	
		ArrayList<String> values = new ArrayList<String>();
		for (int i=0; i< v.length; i++)
		values.add(v[i]);	
		if (values.size() < 8) {
			values.add(Category.UNKNOWN);
		}
		BankStatementRow row = new BankStatementRow(values.get(0),values.get(1),values.get(2),values.get(3),
				values.get(4),values.get(5),values.get(6),values.get(7));		
		return row;

	}

	private static String COMMA_DELIMITER = ",";
	

	public String toString() {
		return "dateProcessed=" + dateProcessed + " dateOfTransaction=" + dateOfTransaction + " id=" + id + " type=" + type
				+ " referenece=" + reference + " description=" + description + " amount=" + amount + "category=" + category;
	}

	public static String cSVHeader() {
		return "dateProcessed=,dateOfTransaction,id,type,referenece,description,amount\n";
	}
	 
	public String toCsv() {
		return dateProcessed + "," + dateOfTransaction + "," + id + "," + type
				+ "," + reference + "," + description + "," + amount + "," + category +"\n";
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

	public String getCategory() {
		return category.get();
	}

	public void setCategory(String category) {
		this.category.set(category);
	}

	@Override
	public int compareTo(BankStatementRow o) {
		
		int result =  this.id.get().compareTo(o.getId());
		
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
	        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
	            return false;
	        }

	        return true;
	    }

}
