package mesh.budget;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.paint.Color;
import mesh.budget.model.Budget;
import mesh.budget.model.Categories;


public class Utils {
	
	static public String categoryFileName = "C:\\Users\\patri\\git\\budget\\categories.json";
	static public String budgetFileName = "C:\\Users\\patri\\git\\budget\\budget.csv";
	static public String exportsDir = "C:\\Users\\patri\\Downloads";
	
	
	private static final Logger logger = LoggerFactory.getLogger(Utils.class);
	
	
	static public Color cssStringToColor(String colourString) {
		String s = colourString.substring(colourString.indexOf(":")+1,colourString.length()-1);
		logger.debug("colour:"+s);	
		Color c = Color.web(s.trim());
		return c;
		
	}
	
	static public String colorToCssString(Color c) {
		for (int i=0; i<Categories.catColours.length; i++) {
			if (cssStringToColor(Categories.catColours[i]).equals(c)) {
				return  Categories.catColours[i];
			}
		}
			return Categories.catColours[0];	
	}
	
	static public String toCurrency(Number n) {
		
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(n);
		
	}


	static public List<File> findExports(String startPath) {
		List<File> matches = new ArrayList<>();
		
		File path = new File(startPath);

		File[] files = path.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().startsWith("Export")) {
				matches.add(files[i]);
				logger.info("found export " + files[i].getAbsolutePath() );
			}
		}

		return matches;
	}

}
