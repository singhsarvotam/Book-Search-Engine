import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/MainController")
public class MainController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query = request.getParameter("query");
		System.out.println(query);

		request.setAttribute("resp", query);
		HttpSession session = request.getSession();

		if (session.getAttribute("index") != null) {
			Map<String, DictionaryEntryTerm> index = (Map<String, DictionaryEntryTerm>) session.getAttribute("index");

			ArrayList<String> list = IndexBuilding.fetchData(query, index);
			if(list.size()>10)
				request.setAttribute("list", new ArrayList<String>(list.subList(0, 10)));
			else
				request.setAttribute("list", list);

			// Query Expansion By Association Matrix
			QueryExpansion queryExpansion = new QueryExpansion();
			String expandedQueryAssociation = queryExpansion.expandQueryByAssociationCluster(query, list);
			request.setAttribute("expandedQueryAssoc", expandedQueryAssociation);
			ArrayList<String> expandedList = IndexBuilding.fetchData(expandedQueryAssociation, index);
			if(expandedList.size()>10)
				request.setAttribute("associationList", new ArrayList<String>(expandedList.subList(0, 10)));
			else
				request.setAttribute("associationList", expandedList);
			request.setAttribute("expandedQueryAssoc", expandedQueryAssociation);

			// Query Expansion By Metric Cluster
//			String expandedQueryMetric = queryExpansion.expandedQueryByMetricCluster(query, list);
//			ArrayList<String> metricList = IndexBuilding.fetchData(expandedQueryMetric, index);
//			request.setAttribute("metricList", metricList);

			ArrayList<String> googleSearchList = GoogleSearchAPI.googleSearch(query);
			if(googleSearchList.size()>10)
				request.setAttribute("googleSearchList", new ArrayList<String>(googleSearchList.subList(0, 10)));
			else
				request.setAttribute("googleSearchList", googleSearchList);
			
			ArrayList<String> bingList = BingSearchAPI.bingSearch(query);
			if(bingList.size()>10)
				request.setAttribute("bingList", new ArrayList<String>(bingList.subList(0, 10)));
			else
				request.setAttribute("bingList", bingList);

		}

		else {
			Map<String, DictionaryEntryTerm> index = IndexBuilding.buildIndex(
					"D:/EclipseWorkspace/BOOKPROJECT/goodreads/books",
					"D:/EclipseWorkspace/BOOKPROJECT/goodreads/stopwords.txt",
					"D:/EclipseWorkspace/BOOKPROJECT/goodreads/index");
			session.setAttribute("index", index);
			ArrayList<String> list = IndexBuilding.fetchData(query, index);
			if(list.size()>10)
				request.setAttribute("list", new ArrayList<String>(list.subList(0, 10)));
			else
				request.setAttribute("list", list);

			// Query Expansion By Association Matrix
			QueryExpansion queryExpansion = new QueryExpansion();
			String expandedQueryAssociation = queryExpansion.expandQueryByAssociationCluster(query, list);
			ArrayList<String> expandedList = IndexBuilding.fetchData(expandedQueryAssociation, index);
			if(expandedList.size()>10)
				request.setAttribute("associationList", new ArrayList<String>(expandedList.subList(0, 10)));
			else
				request.setAttribute("associationList", expandedList);
			
			
			// Query Expansion By Metric Cluster
//			String expandedQueryMetric = queryExpansion.expandedQueryByMetricCluster(query, list);
//			ArrayList<String> metricList = IndexBuilding.fetchData(expandedQueryMetric, index);
//			if(metricList.size()>10)
//				request.setAttribute("metricList", metricList.subList(0, 10));
//			else
//				request.setAttribute("metricList", metricList);
			
			ArrayList<String> googleSearchList = GoogleSearchAPI.googleSearch(query);
			request.setAttribute("googleSearchList", googleSearchList);
			
			ArrayList<String> bingList = BingSearchAPI.bingSearch(query);
			if(bingList.size()>10)
				request.setAttribute("bingList", new ArrayList<String>(bingList.subList(0, 10)));
			else
				request.setAttribute("bingList", bingList);
			
		}

		request.getRequestDispatcher("index.jsp").forward(request, response);
	}


}
