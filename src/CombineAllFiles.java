import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class CombineAllFiles {

//	public static void main(String[] args) {
//		File folder = new File("D:/EclipseWorkspace/BOOKPROJECT/sample");
//		System.out.println("FILES NOT SAVED");
//		List<String> resultDataOfRegualarQuery = new ArrayList<String>();
//		String sCurrentLine;
//			for(File file : Arrays.asList(folder.listFiles())) {
//				try {
//					BufferedReader input = new BufferedReader(new FileReader(file));
//					String oneFile = "";
//					while ((sCurrentLine = input.readLine()) != null) {
//						oneFile += sCurrentLine;
//					}
//					if(oneFile!="")
//						resultDataOfRegualarQuery.add(oneFile);
//					input.close();
//				} catch (IOException e) {
//					System.out.println("File:" + file.getName() + "\tFolder:" + folder.getName());
//				}
//			}
//			
//			QueryExpansion queryExpansion = new QueryExpansion();
//			System.out.println(queryExpansion.expandedQueryByMetricCluster("harry potter", resultDataOfRegualarQuery));
//
//	}
	
	public static void main(String[] args) {
		File parentFolder = new File("goodreads");
		String[] directories = parentFolder.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File current, String name) {
			    return new File(current, name).isDirectory();
			  }
		});
		int counter=0;
		System.out.println("FILES NOT SAVED");
		for(String folderName: Arrays.asList(directories)) {
			File folder = new File("goodreads/"+folderName);
			for(File file : Arrays.asList(folder.listFiles())) {
				try {
					FileUtils.copyFile(file, new File("booksFINAL/book"+counter+".txt"));
					counter++;
				} catch (IOException e) {
					System.out.println("File:" + file.getName() + "\tFolder:" + folder.getName());
				}
			}
		}

	}


}
