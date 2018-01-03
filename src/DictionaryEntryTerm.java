import java.io.Serializable;
import java.util.List;

public class DictionaryEntryTerm implements Serializable {
	String term;
	Integer docFrequency;
	Integer termFrequency;
	List<PostingFileEntry> postingList;

	public DictionaryEntryTerm(String term, Integer docFreq, Integer termFreq, List<PostingFileEntry> list) {
		this.term = term;
		this.docFrequency = docFreq;
		this.termFrequency = termFreq;
		this.postingList = list;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Integer getDocFrequency() {
		return docFrequency;
	}

	public void setDocFrequency(Integer docFrequency) {
		this.docFrequency = docFrequency;
	}

	public Integer getTermFrequency() {
		return termFrequency;
	}

	public void setTermFrequency(Integer termFrequency) {
		this.termFrequency = termFrequency;
	}

	public List<PostingFileEntry> getPostingList() {
		return postingList;
	}

	public void setPostingList(List<PostingFileEntry> postingList) {
		this.postingList = postingList;
	}

	@Override
	public String toString() {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("TERM: ").append(term);
		strBuffer.append("TERM FREQUENCY: ").append(termFrequency);
		strBuffer.append("DOC FREQUENCY: ").append(docFrequency);
		strBuffer.append("POSTING LIST: ").append(postingList.toString());
		return strBuffer.toString();
				
	}
	
	
}
