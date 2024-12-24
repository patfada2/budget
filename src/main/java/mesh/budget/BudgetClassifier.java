package mesh.budget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mesh.budget.model.Budget;

public class BudgetClassifier {
	private static final Logger logger = LoggerFactory.getLogger(BudgetClassifier.class);		
	Map<String, Set<String> > categories;
	
	public BudgetClassifier() {
		categories = new HashMap<String, Set<String> >();
		addCategory("Insurance");
		addCategory("food");	
	}
	
	public void addCategory(String category) {
		if (!categories.containsKey(category)) {
			categories.put(category, new TreeSet<String>());
		}		
	}
	
	public void addMatch(String category, String match) {
		addCategory(category);
		Set<String> matches = categories.get(category);
		matches.add(match);		
	}
	
	public String getCategory(String match) {
		
		String category = null;
	
		for (Map.Entry<String, Set<String>> entry : categories.entrySet()) {
			Set<String> matches =entry.getValue();
			Iterator<String> it = matches.iterator();
			while (it.hasNext()) {
				String s = it.next();
				if (match.startsWith(s)) {
					category =entry.getKey();
					logger.info("category for %s is %s", match, category);
				break;
				}
			}	
			if (category != null)
				break;
			
		}
		return category;
		

	}

}
