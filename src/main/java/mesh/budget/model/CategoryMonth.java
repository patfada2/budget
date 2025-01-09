package mesh.budget.model;

import java.time.Month;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class CategoryMonth {
	
	private SimpleStringProperty categoryName;
	private SimpleDoubleProperty categoryAmount;
	private SimpleStringProperty month;
	
	
	

	public String getMonth() {
		return month.get();
	}
	public void setMonth(String month) {
		this.month.set(month);
	}
	

	public Double getCategoryAmount() {
		return categoryAmount.get();
	}
	public void setCategoryAmount(Number categoryAmount) {
		this.categoryAmount.set((double) categoryAmount);
	}
	
	public String getCategoryName() {
		return categoryName.get();
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName.set(categoryName);
	}
	
	public String toString() {
		String result = " month= "+ getMonth() + " category = " + getCategoryName() + " amount = " + getCategoryAmount();
		return result;
	}
	
	public CategoryMonth() {
		this.month = new SimpleStringProperty();
		this.categoryName = new SimpleStringProperty();
		this.categoryAmount = new SimpleDoubleProperty();
	}
}
