package mesh.budget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mesh.budget.model.Budget;


public class Utils {
	
	static public String categoryFileName = "C:\\Users\\patri\\git\\budget\\categories.csv";
	static public String budgetFileName = "C:\\Users\\patri\\git\\budget\\budget.csv";
	static public String exportsDir = "C:\\Users\\patri\\Downloads";
	
	
	private static final Logger logger = LoggerFactory.getLogger(Utils.class);


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
