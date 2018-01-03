import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class QueryReader {
	public static List<String> parseQueries(String queryFilePath) {
		List<String> queryList = new ArrayList<String>();

		try {
			File queryFile = new File(queryFilePath);
			if (queryFile.isFile()) {
				String querydata = new String(Files.readAllBytes(new File(queryFilePath).toPath()));
				String[] querySplits = Pattern.compile("[Q0-9:]+").split(querydata);
				for (String split : querySplits) {
					String query = split.trim().replaceAll("\\r\\n", " ");
					query = query.replaceAll(".I", "");
					query = query.replaceAll(".W\n", "");
					// query=query.replaceAll("\n", "");
					if (query.length() > 0) {
						queryList.add(query);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return queryList;
	}

	public static ArrayList<String> processQuery(String query, Map<String, DictionaryEntryTerm> dictIndex,
			Map<String, DocumentFrequency> maxDocLemmas, Set<String> stopWordsDict) {

		Map<String, Integer> lemmaDict = buildLemmaDictionaryForQuery(query, stopWordsDict);
		eliminateStopWords(lemmaDict, stopWordsDict);

		Map<String, Double> W1_tab = new HashMap<>();
		Map<String, Double> W2_tab = new HashMap<>();

		for (String queryTerm : lemmaDict.keySet()) {
			DictionaryEntryTerm dictEntryTerm = dictIndex.get(queryTerm);
			if (dictEntryTerm == null) {
				continue;
			}

			int docFrequency = dictEntryTerm.docFrequency;
			for (PostingFileEntry postingFileEntry : dictEntryTerm.postingList) {
				int termFreq = postingFileEntry.frequency;
				long maxTermFrequency = maxDocLemmas.get(postingFileEntry.docId).maxFrequency;
				long docLength = maxDocLemmas.get(postingFileEntry.docId).docLength;
				int collectionSize = maxDocLemmas.size();

				// Cosine_Similarity cs1 = new Cosine_Similarity();
				// double w1 = cs1.Cosine_Similarity_Score(dictEntryTerm.term,
				// queryTerm);
				double w1 = calculateW1(termFreq, maxTermFrequency, docFrequency, collectionSize);
				// double w1 = calculateJaccardSimilarity(dictEntryTerm.term,
				// queryTerm);
				addWeights(W1_tab, postingFileEntry.docId, w1);
			}
		}

		System.out.println("\nTop Relevant documents by Ranking");
		return showTop20Entries(W1_tab);

	}

	public static void addWeights(Map<String, Double> weight_tab, String docId, double w) {
		if (weight_tab.get(docId) == null) {
			weight_tab.put(docId, w);
			return;
		}
		weight_tab.put(docId, w + weight_tab.get(docId));
	}

	public static double calculateW1(int termFreq, long maxTermFreq, int docFreq, int collectionSize) {
		double w1 = 0;
		try {
			w1 = (0.4 + 0.6 * Math.log(termFreq + 0.5) / Math.log(maxTermFreq + 1.0))
					* (Math.log(collectionSize / docFreq) / Math.log(collectionSize));
		} catch (Exception e) {
			w1 = 0;
		}
		return w1;
	}

	public static double calculateW2(int termFreq, long doclength, double avgDoclength, int docFreq,
			int collectionSize) {
		double w2 = 0;
		try {
			w2 = (0.4 + 0.6 * (termFreq / (termFreq + 0.5 + 1.5 * (doclength / avgDoclength)))
					* Math.log(collectionSize / docFreq) / Math.log(collectionSize));
		} catch (Exception e) {
			w2 = 0;
		}
		return w2;
	}

	public static void eliminateStopWords(Map<String, Integer> lemmaDic, Set<String> stopWords) {

		Iterator<String> iterator = lemmaDic.keySet().iterator();
		while (iterator.hasNext()) {
			if (stopWords.contains(iterator.next())) {
				iterator.remove();
			}
		}
	}

	public static Map<String, Integer> buildLemmaDictionaryForQuery(String query, Set<String> stopWordsDict) {
		// TODO Auto-generated method stub
		Map<String, Integer> queryDictionary = new HashMap<String, Integer>();

		StringTokenizer st = new StringTokenizer(query, " ");
		String strCurrent = "";
		while (st.hasMoreTokens()) {
			strCurrent = st.nextToken();

			strCurrent = strCurrent.replaceAll("['.`]+", "");
			Lemmatizer.tokenizeAndaddToLemmaDictionary(strCurrent, queryDictionary, stopWordsDict);

		}
		return queryDictionary;
	}

	public static Map<String, Integer> buildLemmaDictionaryForQueryResult(String query, Set<String> stopWordsDict) {
		Map<String, Integer> queryDictionary = new HashMap<String, Integer>();

		StringTokenizer st = new StringTokenizer(query, " ");
		String strCurrent = "";
		while (st.hasMoreTokens()) {
			strCurrent = st.nextToken();
			strCurrent = strCurrent.replaceAll("[^\\w\\s-'.!:;]", "");
			strCurrent = strCurrent.replaceAll("['.`]+", "");
			Lemmatizer.tokenizeAndaddToLemmaDictionary(strCurrent, queryDictionary, stopWordsDict);

		}
		return queryDictionary;
	}
	
	
	public static ArrayList<String> showTop20Entries(Map<String, Double> weight_table) {

		Map<String, Double> sortedWeightMap = new HashMap<String, Double>();
		sortedWeightMap = sortMap(weight_table);
		System.out.println("Rank : " + "\t Weight   " + " : " + "Document Name");
		int i = 1;
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, Double> entry : sortedWeightMap.entrySet()) {
			if (i > 20)
				break;
			DecimalFormat df = new DecimalFormat("#.####");
			df.setRoundingMode(RoundingMode.CEILING);

			Double d = entry.getValue().doubleValue();

			try {
				String querydata = new String(
						Files.readAllBytes(new File("D:/EclipseWorkspace/BOOKPROJECT/goodreads/books/"
								+ IndexBuilding.doclenMaxFreqDetailsLemmas.get(entry.getKey()).documentName).toPath()));
				list.add(querydata);
				System.out.print(i + "\t" + d.toString() + "\t");
				System.out.print(IndexBuilding.doclenMaxFreqDetailsLemmas.get(entry.getKey()).documentName);
				System.out.println();
				// System.out.println(querydata);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
//		TreeMap<Double, String> sortedMap = new TreeMap<>();
//		for (String data : list) {
//				double rank = data.split("\n")[2].split(",").length;
//				sortedMap.put(-rank, data);
//			}
//		list = new ArrayList<>();
//		i=1;
//		for(double key : sortedMap.keySet()){
//			if (i > 10)
//				break;
//			list.add(sortedMap.get(key));
//			i++;
//		}
		return list;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortMap(final Map<K, V> mapToSort) {

		List<Map.Entry<K, V>> entries = new ArrayList<Map.Entry<K, V>>(mapToSort.size());

		entries.addAll(mapToSort.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
			public int compare(final Map.Entry<K, V> entry1, final Map.Entry<K, V> entry2) {
				return entry2.getValue().compareTo(entry1.getValue());
			}
		});

		Map<K, V> sortedMap = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public static void main(String[] args) {

		ArrayList<String> list = (ArrayList<String>) parseQueries(args[0]);
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i));
		}
	}

	public static Collection<String> union(String string1, String string2) {
		Collection<String> mergedVector = new TreeSet<String>();
		String string1Arr[] = string1.split(" ");
		String string2Arr[] = string2.split(" ");
		Collection<String> string1Coll = new HashSet<String>();
		for (String c1 : string1Arr) {
			string1Coll.add(c1);
		}
		Collection<String> string2Coll = new HashSet<String>();
		for (String c1 : string2Arr) {
			string2Coll.add(c1);
		}
		mergedVector.addAll(string1Coll);
		mergedVector.addAll(string2Coll);
		return uniqueCharactersColl(mergedVector);
	}

	public static Collection<String> uniqueCharactersColl(Collection<String> vector) {
		Collection<String> uniqueSet = new HashSet<String>();
		for (String c : vector) {
			if (!uniqueSet.contains(c)) {
				uniqueSet.add(c);
			}
		}
		return uniqueSet;
	}

	/**
	 * Get the intersection of characters from two strings. The returned set is
	 * returned alphabetically.
	 * 
	 * @param string1
	 *            First String
	 * @param string2
	 *            Second String
	 * @return the set of characters that occur in both strings
	 */
	public static Collection<String> intersect(String string1, String string2) {
		String string1Arr[] = string1.split(" ");
		String string2Arr[] = string2.split(" ");
		Collection<String> vector1 = uniqueCharacters(string1Arr);
		Collection<String> vector2 = uniqueCharacters(string2Arr);
		Collection<String> intersectVector = new TreeSet<String>();
		for (String c1 : vector1) {
			for (String c2 : vector2) {
				if (c1.equals(c2)) {
					intersectVector.add(c1);
				}
			}
		}
		return intersectVector;
	}

	public static Collection<String> uniqueCharacters(String[] string1Arr) {
		Collection<String> uniqueSet = new HashSet<String>();
		for (String c : string1Arr) {
			if (!uniqueSet.contains(c)) {
				uniqueSet.add(c);
			}
		}
		return uniqueSet;
	}

	public static double calculateJaccardSimilarity(String stringOne, String stringTwo) {
		return (double) intersect(stringOne, stringTwo).size() / (double) union(stringOne, stringTwo).size();
	}

}
