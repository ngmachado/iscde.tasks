package pa.iscde.metrix.internal;



import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;



class CsvFileWriter {
	
	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	protected static void writeCsvFile(String fileName, HashMap<String, Integer> hashMap) {
		
		FileWriter fileWriter = null;
				
		try {
			fileWriter = new FileWriter(fileName);
			
			
			//Write a CSV file
			
			Set set = hashMap.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
               Map.Entry mentry = (Map.Entry)iterator.next();
               
               fileWriter.append((CharSequence) mentry.getKey());
               fileWriter.append(COMMA_DELIMITER);
               fileWriter.append(" " +mentry.getValue());
               fileWriter.append(NEW_LINE_SEPARATOR);
            }
				
			
			
			System.out.println("CSV file was created successfully !!!");
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
	}
}
