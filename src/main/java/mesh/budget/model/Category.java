package mesh.budget.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.SimpleStringProperty;
import mesh.budget.App;

public class Category {
	private static final Logger logger = LoggerFactory.getLogger(Category.class);

	private SimpleStringProperty name;
	private List matches = new ArrayList<String>();
	public String getName() {
		return name.get();
	}
	public void setName(String name) {
		this.name.set(name);
	}
	public List getMatches() {
		return matches;
	}
	public void setMatches(List matches) {
		this.matches = matches;
	}
	
	public Category() {
		name = new SimpleStringProperty("unknown");
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

}
