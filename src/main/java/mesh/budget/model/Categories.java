package mesh.budget.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mesh.budget.App;

public class Categories {
	private static final Logger logger = LoggerFactory.getLogger(Categories.class);

	private List<Category> categories = new ArrayList<Category>();

	public List<Category> getCategories() {
		return categories;
	}

	public void add(Category cat) {
		categories.add(cat);
	}

	public Category getCategoryByName(String name) {

		Category result = null;
		for (Category c : categories) {
			if (c.getName().equals(name)) {
				result = c;
				break;
			}
		}
		return result;
	}

	public void loadFromFile(String filename) {

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;

			while ((line = br.readLine()) != null) {
				Category c = new Category();
				logger.info("!!!!!! line =" + line);
				categories.add(c.createFromCsv(line));

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
