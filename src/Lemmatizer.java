import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
public class Lemmatizer {
	static Lemmatizer m_lemmatizer;
	Properties props;
	StanfordCoreNLP pipeline;
	private Map<String, Integer> lemmaDictionary;

	Lemmatizer() {
		props = new Properties();
		props.put("annotators", "tokenize,ssplit, pos,  lemma");
		pipeline = new StanfordCoreNLP(props, false);
	}


	public static Lemmatizer getInstance() {
		if (m_lemmatizer == null) {
			m_lemmatizer = new Lemmatizer();
		}
		return m_lemmatizer;

	}
	public String getLemma(String text) {
				String lemma = "";
		
		Annotation document = pipeline.process(text);
				for (CoreMap sentence : document.get(SentencesAnnotation.class)) {
					for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
						lemma += token.get(LemmaAnnotation.class) + " ";
					}
				}
				return lemma;
			}
	
	public Map<String, Integer> buildLemmaDictionary(File file, Set<String> stopwordsDictionary) {
				try {
					lemmaDictionary = new HashMap<String, Integer>();
					FileInputStream fileInputStream = null;
					DataInputStream dataInputStream = null;
					BufferedReader bufferedReader = null;
					
					if (file.isFile()) {				
						fileInputStream = new FileInputStream(file);
						dataInputStream = new DataInputStream(fileInputStream);
						bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
						String strline = null;			
					
						
						while((strline=bufferedReader.readLine())!=null){
		
							StringTokenizer tokenizer = new StringTokenizer(strline," ");
		
							while(tokenizer.hasMoreTokens()){
		
								String currString = tokenizer.nextToken();
								// removing xml tags and changing to lower case
								//currString =currString.replaceAll("<[^>]>", "").toLowerCase();
								// removing white spaces and numeric values
								//currString = currString.replaceAll("[0-9]","");
								//removing certain special characters
								currString = currString.replaceAll("[^\\w\\s-'.!:;]", "");
								if(!(stopwordsDictionary.contains(currString)))
								{
									currString = currString.replaceAll("['.`]", "");
									tokenizeAndaddToLemmaDictionary(currString,lemmaDictionary, stopwordsDictionary);		
								}
		
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return lemmaDictionary;
			}

	public static void tokenizeAndaddToLemmaDictionary(String tokenstring,
						Map<String, Integer> dicname, Set<String> stopWordsDic) {
			
					if (tokenstring.trim().length() > 0) {
			
						// handle special cases
						if (tokenstring.endsWith("'s")) {
							tokenstring = tokenstring.replace("'s", "").trim();
							addToLemmaDic(tokenstring, dicname, stopWordsDic);
						} else if (tokenstring.contains("-")) {
							String[] newTokens = tokenstring.split("-");
							for (String newToken : newTokens) {
								addToLemmaDic(newToken, dicname, stopWordsDic);
							}
						} else if (tokenstring.contains("_")) {
							String[] newTokens = tokenstring.split("_");
							for (String newToken : newTokens) {
								addToLemmaDic(newToken, dicname, stopWordsDic);
							}
						} else {
							// default case
							addToLemmaDic(tokenstring, dicname, stopWordsDic);
						}
					}
			
			}
	public static void addToLemmaDic(String strToken, Map<String, Integer> dicname, Set<String> stopWordsDic) {
				String lemma = Lemmatizer.getInstance().getLemma(strToken);
				
				//String lemma = strToken;
				
				if (lemma.trim().length() > 0) {
					lemma=lemma.replace(" ", "").toLowerCase();
					if(!stopWordsDic.contains(lemma)){
					//System.out.println(lemma);
					// check if lemma already exits
					if (dicname.containsKey(lemma)) {
						dicname.put(lemma, dicname.get(lemma)+1);
					} else
						// if doesnot exists enter new row
						dicname.put(lemma, 1);
				}
				}
		
			}
		
			public static void main(String[] args) {
				System.out.println(Lemmatizer.getInstance().getLemma("artistic"));
			}
		

}
