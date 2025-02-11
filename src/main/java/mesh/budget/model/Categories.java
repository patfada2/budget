package mesh.budget.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.paint.Color;
import mesh.budget.App;
import mesh.budget.Utils;

public class Categories {
	private static final Logger logger = LoggerFactory.getLogger(Categories.class);
	// these need to be defined in the css
	public static String[] catColours = { "-fx-bar-fill: #f9d900;", "-fx-bar-fill: #a9e200;", "-fx-bar-fill: #22bad9;",
			"-fx-bar-fill: #0181e2;", "-fx-bar-fill: #2f357f;",
			"-fx-bar-fill: #860061;",
			"-fx-bar-fill: #c62b00;",
			" -fx-bar-fill: #ff5700;",
			"-fx-bar-fill: lawngreen;",
			"-fx-bar-fill: indianred;",
			"-fx-bar-fill: blue;",
			"-fx-bar-fill: darksalmon;",
			"-fx-bar-fill: cornflowerblue ;",
			"-fx-bar-fill: honeydew ;",
			"-fx-bar-fill: goldenrod ;",
			"-fx-bar-fill: darkred ;",
			"-fx-bar-fill: purple ;",
			"-fx-bar-fill: red ;",
			"-fx-bar-fill: seagreen;",
			"-fx-bar-fill: yellow;"
			
	};

	private List<Category> categories = new ArrayList<Category>();

	public List<Category> getCategories() {
		return categories;
	}

	public String getCatColour(String catName) {
		Category cat = this.getCategoryByName(catName);
		if (cat.getColour()==null){
			return getDefaultCatColour(catName);
		} else {
			return cat.getColour();
		}
		
	} 
	
	public String getDefaultCatColour(String catName) {
		int i = this.getIndexOfName(catName);
		if ((i >= 0) && (i < catColours.length)) {
			logger.trace("colour for "+catName+" is "+ catColours[i]);
			return catColours[i];
		}
		else {
			logger.debug(catName+" is not found, using"+ catColours[0]);
			return  catColours[0];
			
		}
		
	}

	public void add(Category cat) {
		categories.add(cat);
	}

	public Double getBudgetTotal() {

		Double total = Double.valueOf(0);
		for (int i = 0; i < categories.size(); i++) {
			total += categories.get(i).getBudget();
		}
		return total;
	}

	// return matching category, or UNKNOWN
	public String findDescriptionMatch(String description) {
		String result = Category.UNKNOWN;
		Iterator<Category> it = categories.iterator();
		while (it.hasNext()) {
			Category cat = it.next();
			logger.info("looking in " + cat.getName());
			result = cat.findDescriptionMatch(description);

			if (result.equals(Category.UNKNOWN)) {
				logger.info("no match found for description" + description + "in category " + cat.getName());
			} else {

				logger.info("match found for description" + description);
				break;
			}
		}
		return result;

	}

	public String findReferenceMatch(String reference) {
		String result = Category.UNKNOWN;
		Iterator<Category> it = categories.iterator();
		while (it.hasNext()) {
			Category cat = it.next();
			logger.info("looking in " + cat.getName());
			result = cat.findReferenceMatch(reference);

			if (result.equals(Category.UNKNOWN)) {
				logger.info("no match found for reference" + reference + "in category " + cat.getName());
			} else {

				logger.info("match found for reference" + reference);
				break;
			}
		}
		return result;

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

	// the grand total of categories
	public double getTotal() {
		double result = 0;
		for (Category c : categories) {
			if (!c.getName().equals("Transfer")) {
				result += c.getTotal();
				logger.debug("!!adding " + c.getName() + ":" + c.getTotal());

			}

		}
		return result;

	}

	public void loadFromFile(String filename) {
		logger.info("loading category file from: " + filename);

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;

			while ((line = br.readLine()) != null) {

				logger.debug("line =" + line);
				categories.add(Category.createFromJson(line));

			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveToFile(String filename) {
		logger.info("saving  category file to: " + filename);
		String str;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {

			Iterator<Category> it = categories.iterator();

			while (it.hasNext()) {

				writer.append(it.next().toJson() + "\n");

			}
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int getIndexOfName(String name) {

		int result = -1;
		for (int i = 0; i < categories.size(); i++) {
			if (categories.get(i).getName().equals(name)) {
				result = i;
				break;
			}
		}	
		return result;
	}

	public void delete(String name) {
		int i = getIndexOfName(name);
		if (i < 0) {
			logger.error("category " + name + "not found");
		} else {
			ArrayList c = (ArrayList) categories;
			c.remove(i);

		}
		logger.info("removed category: " + name);

	}

}
