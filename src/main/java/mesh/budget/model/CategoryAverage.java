package mesh.budget.model;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class CategoryAverage implements Comparable<CategoryAverage>  {

	private SimpleStringProperty categoryName;
	private SimpleDoubleProperty categoryAverage;
	private SimpleDoubleProperty categoryBudget;
	private List<Double> values;

	public Double getCategoryAverage() {
		Double average = Double.valueOf(0);

		for (int i = 0; i < values.size(); i++) {
			average += values.get(i);
		}
		
		average = average / values.size();

		return average;
	}

	public String getCategoryName() {
		return categoryName.get();
	}
	
	public Double getCategoryBudget() {
		return categoryBudget.get();
	}

	public void setCategoryName(String categoryName) {
		this.categoryName.set(categoryName);
	}

	public String toString() {
		String result = " category = " + getCategoryName() + " amount = " + getCategoryAverage();
		return result;
	}

	public CategoryAverage(String categoryName, double budget) {

		this.categoryName = new SimpleStringProperty(categoryName);
		this.categoryAverage = new SimpleDoubleProperty();
		this.categoryBudget = new SimpleDoubleProperty(budget);
		values = new ArrayList<Double>();
	}

	public void addValue(Double value) {
		values.add(value);

	}

	@Override
	public int compareTo(CategoryAverage o) {
		// TODO Auto-generated method stub
		return o.getCategoryName().compareTo(this.getCategoryName());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj.getClass() != this.getClass()) {
			return false;
		}

		final CategoryAverage other = (CategoryAverage) obj;
		if (this.getCategoryName().equals(other.getCategoryName()) ) {
			return true;

		} else
			return false;

	}

	
}
