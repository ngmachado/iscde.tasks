package pa.iscde.metrix.internal;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class HtmlFileWrite {
	
		//Delimiter used in HTML file
	
		private static final String BEGIN_TR = "<tr>";
		private static final String END_TR = "</tr>";
		private static final String BEGIN_TD = "<td>";
		private static final String END_TD = "</td>";
		
		protected static void writeHtmlFile(String fileName, HashMap<String, Integer> hashMap) {
			
			FileWriter fileWriter = null;
					
			try {
				fileWriter = new FileWriter(fileName);
				
				
				//Write a HTML file
				
					
					Set set = hashMap.entrySet();
		            Iterator iterator = set.iterator();
		            
		            fileWriter.append("<html>");
		            fileWriter.append("<title>ISCDE - Metrix </title>");
					fileWriter.append("<head>");
					fileWriter.append("<style> table { border-collapse: collapse; width: 30%; 	} th, td { text-align: left; padding: 8px; } tr:nth-child(even){background-color: #f2f2f2} th { background-color: #4CAF50; color: white; } </style>");
									
					fileWriter.append("</head>");
					fileWriter.append("<body>");
					fileWriter.append("<table>");
					fileWriter.append(BEGIN_TR);
					fileWriter.append("<th>" + "Metric" + "</th>");
					fileWriter.append("<th>" + "Value" + "</th>");
					fileWriter.append(END_TR);
					 
		            
		            while(iterator.hasNext()) {
		               Map.Entry mentry = (Map.Entry)iterator.next();
		               
		               fileWriter.append(BEGIN_TR);
		               fileWriter.append(BEGIN_TD + (CharSequence) mentry.getKey() + END_TD);
		               fileWriter.append(BEGIN_TD + (CharSequence) " " +mentry.getValue() + END_TD);
		               fileWriter.append(END_TR);
		            }
		            
		            fileWriter.append("</body></table></html>");
				
				System.out.println("HTML file was created successfully !!!");
				
			} catch (Exception e) {
				System.out.println("Error in HTMLFileWriter !!!");
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
