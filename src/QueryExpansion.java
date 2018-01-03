import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class QueryExpansion {

	public String expandedQueryByMetricCluster(String query, List<String> resultDataOfRegualarQuery) {
		StringBuffer allBooksInResult = new StringBuffer();
		for (String data : resultDataOfRegualarQuery) {
			allBooksInResult.append(data);
		}
		Map<String, Integer> freqMapOfAllTerms = QueryReader
				.buildLemmaDictionaryForQueryResult(allBooksInResult.toString(), IndexBuilding.stopWordsList);
		Map<String, Integer> allQueryLemmas = QueryReader.buildLemmaDictionaryForQuery(query,
				IndexBuilding.stopWordsList);
		HashMap<String, List<String>> expandedQueryTerms = new HashMap<String, List<String>>();

		for (String queryLemma : allQueryLemmas.keySet()) {
			double maxSTerm1 = 0;
			double maxSTerm2 = 0;
			String term1 = "";
			String term2 = "";
			for (String term : freqMapOfAllTerms.keySet()) {
				double sTerm = 0.0;
				double cTerm = 0.0;
				for (String data : resultDataOfRegualarQuery) {
					String lemmasArray[] = getLemmasFromDoc(data).split(" ");
					int queryTermIndex = Arrays.asList(lemmasArray).indexOf(queryLemma);
					int prevQueryTermIndex = 0;
					do {
						if (queryTermIndex != -1) {
							int termIndex = Arrays.asList(lemmasArray).indexOf(term);
							int prevTermIndex=0;
							do {
								if (termIndex != -1) {
									cTerm += (1.0 / (double) (Math.abs((queryTermIndex+prevQueryTermIndex) - (termIndex+prevTermIndex))));
									prevTermIndex = prevTermIndex + termIndex + 1;
									termIndex = Arrays.asList(lemmasArray).subList(termIndex+1, lemmasArray.length)
											.indexOf(term);
								}
							} while (termIndex != -1);
							prevQueryTermIndex = prevQueryTermIndex + queryTermIndex + 1;
							queryTermIndex = Arrays.asList(lemmasArray).subList(prevQueryTermIndex+1, lemmasArray.length)
									.indexOf(queryLemma);
						}
					} while (queryTermIndex != -1);

				}
				if (freqMapOfAllTerms.get(queryLemma) != null && freqMapOfAllTerms.get(term) != null)
					sTerm = cTerm / (double) (freqMapOfAllTerms.get(queryLemma) * freqMapOfAllTerms.get(term));
				if (sTerm > maxSTerm1) {
					maxSTerm2 = maxSTerm1;
					term2 = term1;
					maxSTerm1 = sTerm;
					term1 = term;
				} else if (sTerm > maxSTerm2) {
					maxSTerm2 = sTerm;
					term2 = term;
				}
			}
			expandedQueryTerms.put(queryLemma, new ArrayList<>(Arrays.asList(term1, term2)));
		}
		String expandedQuery = query;
		Set<String> newTerms = new HashSet<>();
		newTerms.addAll(allQueryLemmas.keySet());
		for (List<String> terms : expandedQueryTerms.values())
			for (String term : terms)
				newTerms.add(term);
		
		for(String key:newTerms) {
			expandedQuery = expandedQuery + " " + key;
		}
		System.out.println("EXPANDED QUERY BY METRIC CLUSTERING" + expandedQuery);
		return expandedQuery;

		// return expandedQuery;
	}

	private String getLemmasFromDoc(String data) {

		StringBuffer strBuffer = new StringBuffer();
		StringTokenizer st = new StringTokenizer(data, " ");
		String strCurrent = "";
		while (st.hasMoreTokens()) {
			strCurrent = st.nextToken();
			strCurrent = strCurrent.replaceAll("[^\\w\\s-'.!:;]", "");
			strCurrent = strCurrent.replaceAll("['.`]+", "");
			List<String> docLemmas = getLemmasForDocumentInOrder(strCurrent, IndexBuilding.stopWordsList);
			for (String docLemma : docLemmas)
				strBuffer.append(docLemma).append(" ");
		}
		return strBuffer.toString();

	}

	private List<String> getLemmasForDocumentInOrder(String tokenstring, Set<String> stopWordsDic) {
		List<String> lemmas = new ArrayList<>();
		if (tokenstring.trim().length() > 0) {
			tokenstring = tokenstring.trim();
			// handle special cases
			if (tokenstring.endsWith("'s")) {
				tokenstring = tokenstring.replace("'s", "").trim();
				lemmas.add(addToLemmaDic(tokenstring, stopWordsDic));
			} else if (tokenstring.contains("-")) {
				String[] newTokens = tokenstring.split("-");
				for (String newToken : newTokens) {
					lemmas.add(addToLemmaDic(newToken, stopWordsDic));
				}
			} else if (tokenstring.contains("_")) {
				String[] newTokens = tokenstring.split("_");
				for (String newToken : newTokens) {
					lemmas.add(addToLemmaDic(newToken, stopWordsDic));
				}
			} else {
				// default case
				lemmas.add(addToLemmaDic(tokenstring, stopWordsDic));
			}
		}
		return lemmas;

	}

	private String addToLemmaDic(String strToken, Set<String> stopWordsDic) {
		String lemma = Lemmatizer.getInstance().getLemma(strToken);

		// String lemma = strToken;

		if (lemma.trim().length() > 0) {
			lemma = lemma.replace(" ", "").toLowerCase();
			return lemma;
		} else
			return "";

	}

	public String expandQueryByAssociationCluster(String query, List<String> resultDataOfRegualarQuery) {

		List<Map<String, Integer>> matrix = new ArrayList<>();
		HashSet<String> localVocabulary = new HashSet<>();
		for (String data : resultDataOfRegualarQuery) {
			Map<String, Integer> map = QueryReader.buildLemmaDictionaryForQueryResult(data,
					IndexBuilding.stopWordsList);
			matrix.add(map);
			localVocabulary.addAll(map.keySet());
		}
		Map<String, Integer> allQueryLemmas = QueryReader.buildLemmaDictionaryForQuery(query,
				IndexBuilding.stopWordsList);
		HashMap<String, List<String>> expandedQueryTerms = new HashMap();
		for (String queryLemma : allQueryLemmas.keySet()) {
			double maxSTerm1 = 0;
			double maxSTerm2 = 0;
			double maxSTerm3=0;
			String term1 = "";
			String term2 = "";
			String term3="";
			for (String term : localVocabulary) {
				double sTerm = 0;
				if (!term.equalsIgnoreCase(queryLemma))
					sTerm = cTerm(queryLemma, term, matrix) / (cTerm(queryLemma, queryLemma, matrix)
							+ cTerm(term, term, matrix) + cTerm(queryLemma, term, matrix));
				if (sTerm > maxSTerm1) {
					maxSTerm3 = maxSTerm2;
					maxSTerm2 = maxSTerm1;
					maxSTerm1 = sTerm;
					term3=term2;
					term2 = term1;
					term1 = term;
				} else if(sTerm > maxSTerm2) {
					maxSTerm3 = maxSTerm2;
					maxSTerm2 = sTerm;
					term3=term2;
					term2 = term;
				}
				else if (sTerm > maxSTerm3) {
					maxSTerm3 = sTerm;
					term3 = term;
				}
			}
			expandedQueryTerms.put(queryLemma, new ArrayList<>(Arrays.asList(term1, term2, term3)));
		}
		String expandedQuery = "";
		List<String> newTerms = new ArrayList<String>();
		newTerms.addAll(allQueryLemmas.keySet());
		for (List<String> terms : expandedQueryTerms.values())
			for (String term : terms)
				if(!newTerms.contains(term))
					newTerms.add(term);
		
		for(String key:newTerms) {
			expandedQuery = expandedQuery + " " + key;
		}
		System.out.println("EXPANDED QUERY BY ASSOCIATIVE MATRIX: " + expandedQuery);
		return expandedQuery;
	}

	private double cTerm(String term1, String term2, List<Map<String, Integer>> matrix) {
		double cTerm = 0.0;
		for (Map<String, Integer> documentPostingMap : matrix) {
			if (documentPostingMap.get(term1) != null && documentPostingMap.get(term2) != null) {
				cTerm = cTerm + (double) (documentPostingMap.get(term1) * documentPostingMap.get(term2));
			}
		}
		return cTerm;
	}

}
