package mesh.budget.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.SimpleStringProperty;
import mesh.budget.App;

public class Category {
	private static final Logger logger = LoggerFactory.getLogger(Category.class);
	
	public  static final String UNKNOWN="no category";

	private SimpleStringProperty name;
	private List<String> matches = new ArrayList<String>();
	public String getName() {
		return name.get();
	}
	public void setName(String name) {
		this.name.set(name);
	}
	public List<String> getMatches() {
		return matches;
	}
	public void setMatches(List<String> matches) {
		this.matches = matches;
	}
	
	public Category() {
		name = new SimpleStringProperty(Category.UNKNOWN);
	}
	public Category createFromCsv(String line) {
		String[] v = line.split(",");	
		logger.info("!!!!!!v0"+ v[0]);
		Category c = new Category();
		c.setName(v[0]);
		for (int i = 1; i< v.length; i++) {
			c.matches.add(v[i]);
		}
		return c;
		
	}
	
	
	public String findMatch(String description) {
		String result = Category.UNKNOWN;
		Iterator<String> it = matches.iterator();
		logger.info(this.getName() + "matches"+ matches.toString());
		while (it.hasNext()) {
			String match = it.next();
			if (description.contains(match)) {
				result = this.getName();
				logger.info("match found:"+ match);		
				break;
			}
			else logger.info(description + "does not contain " + match);
		}
		return result;

	}

}
