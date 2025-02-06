package mesh.budget.model;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.beans.property.SimpleStringProperty;
import mesh.budget.App;

public class Category {
	private static final Logger logger = LoggerFactory.getLogger(Category.class);

	public static final String UNKNOWN = "no category";
	private static ObjectMapper mapper = new ObjectMapper();

	private double total;
	private double budget;

	private Map<Month, Double> monthTotals;

	public Map<Month, Double> getMonthTotals() {
		return monthTotals;

	}

	public double getTotal() {
		return total;
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public void addToTotal(String amount) {
		try {
			double d = Double.parseDouble(amount);
			total += d;
		} catch (NumberFormatException e) {
			logger.info(amount + " is not a valid number");
		}

	}

	private SimpleStringProperty name;
	@JsonProperty
	private List<String> descriptionMatches = new ArrayList<String>();

	public void setReferenceMatches(List<String> referenceMatches) {
		this.referenceMatches = referenceMatches;
	}

	@JsonProperty
	private List<String> referenceMatches = new ArrayList<String>();

	public List<String> getReferenceMatches() {
		return referenceMatches;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public List<String> getDescriptionMatches() {
		return descriptionMatches;
	}

	public void setRefernceMatches(List<String> referenceMatches) {
		this.referenceMatches = referenceMatches;
	}

	public void setDescriptionMatches(List<String> matches) {
		this.descriptionMatches = matches;
	}

	public void deleteDescriptionMatch(String match) {
		logger.info("deleting desc match " + match);
		for (int i = 0; i < descriptionMatches.size(); i++) {
			logger.debug("comparing " + descriptionMatches.get(i));

			if (descriptionMatches.get(i).equals(match)) {
				descriptionMatches.remove(i);
				logger.info("deleted desc match" + match + " from category " + this.getName());
				break;
			}
		}
	}
	// deleteReferenceMatch

	public void deleteReferenceMatch(String match) {
		logger.info("deleting ref match " + match);
		for (int i = 0; i < referenceMatches.size(); i++) {
			logger.debug("comparing " + referenceMatches.get(i));

			if (referenceMatches.get(i).equals(match)) {
				referenceMatches.remove(i);
				logger.info("deleted ref match" + match + " from category " + this.getName());
				break;
			}
		}
	}

	public Category() {
		name = new SimpleStringProperty(Category.UNKNOWN);

		monthTotals = new HashMap<Month, Double>();
		// initialize month totals to zero
		for (Month month : Month.values()) {
			monthTotals.put(month, Double.valueOf(0));
		}
	}

	public Category(String name) {

		logger.debug("creating category: " + name);
		this.name = new SimpleStringProperty(name);
		logger.debug("created category: " + name);
	}

	public Category createFromCsv(String line) {
		String[] v = line.split(",");

		Category c = new Category();
		c.setName(v[0]);
		for (int i = 1; i < v.length; i++) {
			c.descriptionMatches.add(v[i]);
		}
		return c;

	}

	public static Category createFromJson(String json) {

		Category cat = null;
		try {
			cat = mapper.readValue(json, Category.class);
		} catch (JsonProcessingException e) {
			logger.error("failed to create category from json:" + json);
			e.printStackTrace();
		}

		return cat;

	}

	public String toCsv() {
		String result = this.name.get();
		if (descriptionMatches.size() > 0) {
			result += ",";
		}
		Iterator<String> it = descriptionMatches.iterator();
		while (it.hasNext()) {
			String match = it.next();
			result += match;
			if (it.hasNext()) {
				result += ",";
			} else
				result += "\n";
		}
		logger.debug("cat row=" + result);
		return result;
	}

	public String findDescriptionMatch(String description) {
		String result = Category.UNKNOWN;
		Iterator<String> it = descriptionMatches.iterator();
		logger.info(this.getName() + "looking for description matches for " + description + " in"
				+ referenceMatches.toString());
		while (it.hasNext()) {
			String match = it.next();
			if (description.contains(match)) {
				result = this.getName();
				logger.info("match found:" + match);
				break;
			} else
				logger.info(description + "does not contain " + match);
		}
		return result;

	}

	public String findReferenceMatch(String reference) {
		String result = Category.UNKNOWN;
		Iterator<String> it = referenceMatches.iterator();
		logger.info(this.getName() + " looking for reference matches for " + reference + " in"
				+ referenceMatches.toString());
		while (it.hasNext()) {
			String match = it.next();
			if (reference.contains(match)) {
				result = this.getName();
				logger.info("reference match found:" + match + " in category " + this.getName());
				break;
			} else
				logger.info(reference + "does not contain " + match);
		}
		return result;

	}

	public String toJson() {
		// TODO Auto-generated method stub
		String result = null;

		try {
			result = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

}
