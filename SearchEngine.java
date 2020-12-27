//

import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String>> wordIndex;   // this will contain a set of pairs (String, LinkedList of Strings)
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception {
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}

	/*
	 * This does a graph traversal of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 *
	 * 	This method will fit in about 30-50 lines (or less)
	 */

	public void newUrl(String url, String original, int returnedToFirst) throws Exception {
		ArrayList<String> childUrls = this.parser.getLinks(url);
		if (url == original && returnedToFirst == 0) {
			this.internet.addVertex(url);
			this.internet.setVisited(url, true);
			ArrayList<String> words = this.parser.getContent(url);
			for (String word : words) {
				if (this.wordIndex.containsKey(word)) {
					this.wordIndex.get(word).add(url);
				} else {
					ArrayList<String> newWordIndexArray = new ArrayList<String>();
					newWordIndexArray.add(url);
					this.wordIndex.put(word, newWordIndexArray);
				}
			}
			returnedToFirst = 1;
		}
		for (String childUrl : childUrls) {
			if ((this.internet.getVisited(childUrl) == true && childUrl != original) || (returnedToFirst == 2 && childUrl == original)) {
				this.internet.addEdge(url, childUrl);
				continue;
			} else if (this.internet.getVisited(childUrl) == false || childUrl == original) {
				if (childUrl != original) {
					this.internet.addVertex(childUrl);
					this.internet.addEdge(url, childUrl);
					ArrayList<String> words = this.parser.getContent(childUrl);
					for (String word : words) {
						if (this.wordIndex.containsKey(word)) {
							this.wordIndex.get(word).add(childUrl);
						} else {
							ArrayList<String> newWordIndexArray = new ArrayList<String>();
							newWordIndexArray.add(childUrl);
							this.wordIndex.put(word, newWordIndexArray);
						}
					}
				}
				this.internet.setVisited(childUrl, true);
				if (childUrl == original) {
					newUrl(childUrl, url, 2);
				} else {
					newUrl(childUrl, url, 1);
				}
			}
		}
	}

	public void crawlAndIndex(String url) throws Exception {
		// TODO : Add code here
		newUrl(url, url, 0);
	}

	/*
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex().
	 * To implement this method, refer to the algorithm described in the
	 * assignment pdf.
	 *
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		// TODO : Add code here
		boolean ep = true;
		ArrayList<Double> ranksPrev = new ArrayList<Double>();
		for (String v : this.internet.getVertices()) {
			ranksPrev.add(1.0);
			internet.setPageRank(v, 1.0);
		}
		while (ep) {
			//computing ranks
			ArrayList<Double> ranks = computeRanks(this.internet.getVertices());

			ep = false;
			for (int i = 0; i < this.internet.getVertices().size(); i++) {
				if (Math.abs(ranks.get(i) - ranksPrev.get(i)) > epsilon) {
					ep = true;
				}
			}
			//setting previous ranks to ranks
			ranksPrev = ranks;

			//assigning ranks
			int counter = 0;
			for (String vertex : this.internet.getVertices()) {
				this.internet.setPageRank(vertex, ranks.get(counter));
				counter += 1;
			}
		}
	}
	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls.
	 * Note that the double in the output list is matched to the url in the input list using
	 * their position in the list.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		// TODO : Add code here
		//first value in array corresponds to first url in ArrayList vertices
		ArrayList<Double> ranks = new ArrayList<Double>(vertices.size());

		for (String vertex : vertices) {
			ranks.add(this.internet.getPageRank(vertex));
		}
		int i = 0;
		for (String vertex : vertices) {
			//System.out.println(vertex);
			double curPr = 0.5;
			for (String out : this.internet.getEdgesInto(vertex)) {
				double val = 0.5 * (this.internet.getPageRank(out) / this.internet.getOutDegree(out));
				curPr += val;
			}
			ranks.set(i, curPr);
			i += 1;
		}
		return ranks;
	}


	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 *
	 * This method should take about 25 lines of code.
	 */
	public ArrayList<String> getResults(String query) {
		// TODO: Add code here
		HashMap<String, Double> containsQuery = new HashMap<>();
		for (String url : this.wordIndex.get(query)) {
			containsQuery.put(url, this.internet.getPageRank(url));
		}
		return Sorting.fastSort(containsQuery);
	}
}



