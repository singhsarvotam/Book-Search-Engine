import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class IndexBuilding {
	private static Map<String, DictionaryEntryTerm> lemmaDictionary = new HashMap<String, DictionaryEntryTerm>();
	public static Map<String, DocumentFrequency> doclenMaxFreqDetailsLemmas = new HashMap<String, DocumentFrequency>();
	public static Set<String> stopWordsList;

	public static ArrayList<String> fetchData(String query, Map<String, DictionaryEntryTerm> uncompressedIndex) {
		try {
			// String folderPath = "goodreads/books";
			// String query = "harry potter";
			// String stopWordsFilePath = "goodreads/stopwords.txt";

			IndexBuilding indexBuilding = new IndexBuilding();

			int avgDocLength = getAvgDocLength(uncompressedIndex);

			/*
			 * for (int i = 0; i < queryList.size(); i++) {
			 * System.out.println("\nQuery" + (i + 1) + " : " +
			 * queryList.get(i)); QueryReader.processQuery(queryList.get(i),
			 * uncompressedIndexv1, doclenMaxFreqDetailsLemmas, stopWordsList,
			 * avgDocLength); }
			 */
			return QueryReader.processQuery(query, uncompressedIndex, doclenMaxFreqDetailsLemmas, stopWordsList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, DictionaryEntryTerm> buildIndex(String folderPath, String stopWordsFilePath,
			String path) {
		IndexBuilding indexBuilding = new IndexBuilding();
		stopWordsList = getListOfStopWords(stopWordsFilePath);
		Map<String, DictionaryEntryTerm> uncompressedIndexv1 = indexBuilding.buildIndexVersion1(folderPath,
				stopWordsList);
		printIndex(uncompressedIndexv1, path + "\\Index_Version1.uncompressed");
		return uncompressedIndexv1;
	}

	public Map<String, DictionaryEntryTerm> buildIndexVersion1(String folderPath, Set<String> stopWordsDict) {

		Lemmatizer lemmatizer = new Lemmatizer();
		File folder = new File(folderPath);
		int counter=1;
		for (File file : folder.listFiles()) {

			if (file.isFile()) {
				Map<String, Integer> termFreqMap = lemmatizer.buildLemmaDictionary(file, stopWordsDict);

				buildIndexesVersion1(file.getName(), termFreqMap, file.getName(), file.getName());
			}
			if(counter%500 ==0)
				System.out.println(counter*100/folder.listFiles().length +"% files indexed");
			counter++;
		}

		return lemmaDictionary;
	}

	public void buildIndexesVersion1(String docId, Map<String, Integer> lemmaTermFreqDict, String fileName,
			String title) {

		long maxTermFreq = 0;
		long docLen = 0;

		for (String term : lemmaTermFreqDict.keySet()) {
			int termFreq = lemmaTermFreqDict.get(term);
			docLen += termFreq;
			if (!(stopWordsList.contains(term))) {
				if (termFreq > maxTermFreq) {
					maxTermFreq = termFreq;
				}
				updatePostingValuesVersion1(docId, term, lemmaTermFreqDict.get(term));
			}
		}

		DocumentFrequency entry = new DocumentFrequency(docLen, maxTermFreq, fileName, title);
		doclenMaxFreqDetailsLemmas.put(docId, entry);

	}

	private void updatePostingValuesVersion1(String docId, String term, Integer termFrequency) {
		DictionaryEntryTerm entry = null;
		if (lemmaDictionary != null)
			entry = lemmaDictionary.get(term);

		if (entry == null) {
			entry = new DictionaryEntryTerm(term, 0, 0, new LinkedList<PostingFileEntry>());
			entry.postingList = new LinkedList<PostingFileEntry>();

		}
		entry.postingList.add(new PostingFileEntry(docId, termFrequency));
		entry.docFrequency += 1;
		entry.termFrequency += termFrequency;
		lemmaDictionary.put(term, entry);

	}

	// entry.postingList.add(new PostingFileEntry(docId, termFrequency));
	// entry.docFrequency += 1;
	// entry.termFrequency += termFrequency;
	// lemmaDictionary.put(term, entry);
	public static HashSet<String> getListOfStopWords(String filePath) {

		HashSet<String> stopWordsDict = new HashSet<String>();

		FileInputStream fileInputStream = null;
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;

		try {
			File stopwordsFile = new File(filePath);
			fileInputStream = new FileInputStream(stopwordsFile);
			dataInputStream = new DataInputStream(fileInputStream);
			bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));

			String line = null;

			while ((line = bufferedReader.readLine()) != null) {

				stopWordsDict.add(line.toLowerCase());
			}
		} catch (Exception e) {
			try {

				fileInputStream.close();
				dataInputStream.close();
				bufferedReader.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
		}
		return stopWordsDict;
	}

	public static int getAvgDocLength(Map<String, DictionaryEntryTerm> lemmadictionary) {
		BigInteger length = new BigInteger(Integer.toString(0));
		Set keys = lemmadictionary.keySet();
		Iterator i = keys.iterator();
		try {
			while (i.hasNext()) {
				String key = (String) i.next();
				length = length.add(new BigInteger(lemmadictionary.get(key).docFrequency.toString()));
			}

			length = length.divide(new BigInteger(Integer.toString(lemmadictionary.size())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return length.intValue();
	}

	private static void printIndex(Map<String, DictionaryEntryTerm> Index, String Filename) {
		BufferedWriter writer = null;
		try {
			File newTextFile = new File(Filename);

			writer = new BufferedWriter(new FileWriter(newTextFile, true));

			writer.write("key->termfreq->docfreq->postinglist");
			writer.newLine();
			Set keys = Index.keySet();
			Iterator i = keys.iterator();
			while (i.hasNext()) {
				String key = (String) i.next();
				Integer tf = (Integer) Index.get(key).termFrequency;

				Integer df = (Integer) Index.get(key).docFrequency;

				writer.write(key + "->" + tf + "->" + df + "->");

				Iterator ite = Index.get(key).postingList.iterator();
				while (ite.hasNext()) {
					writer.write(ite.next().toString() + ",");
				}
				writer.newLine();
			}
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}

	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Map<String, DictionaryEntryTerm> index = IndexBuilding.buildIndex("D:/EclipseWorkspace/BOOKPROJECT/goodreads/books", "D:/EclipseWorkspace/BOOKPROJECT/goodreads/stopwords.txt","D:/EclipseWorkspace/BOOKPROJECT/goodreads/index");
		FileOutputStream fout = new FileOutputStream("D:/EclipseWorkspace/BOOKPROJECT/goodreads/indexObjectForSession.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fout);   
		oos.writeObject(index);
		oos.close();
		
		FileInputStream fin = new FileInputStream("D:/EclipseWorkspace/BOOKPROJECT/goodreads/indexObjectForSession.ser");
//		ObjectInputStream ois = new ObjectInputStream(fin);
//		@SuppressWarnings("unchecked")
//		Map<String, DictionaryEntryTerm> readObject = (Map<String, DictionaryEntryTerm>)ois.readObject();
//		for(String key : readObject.keySet()) {
//			System.out.println(key + "  :::: " + readObject.get(key));
//		}
//		ois.close();
		
	}
}
