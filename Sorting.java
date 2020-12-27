//package finalproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry; // You may need it to implement fastSort

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable.
	 * It returns an ArrayList containing all the keys from the map, ordered
	 * in descending order based on the values they mapped to.
	 *
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number
	 * of pairs in the map.
	 */
	public static <K, V extends Comparable> ArrayList<K> slowSort (HashMap<K, V> results) {
		ArrayList<K> sortedUrls = new ArrayList<K>();
		sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

		int N = sortedUrls.size();
		for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) < 0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);
				}
			}
		}
		return sortedUrls;
	}


	/*
	 * This method takes as input an HashMap with values that are Comparable.
	 * It returns an ArrayList containing all the keys from the map, ordered
	 * in descending order based on the values they mapped to.
	 *
	 * The time complexity for this method is O(n*log(n)), where n is the number
	 * of pairs in the map.
	 */

	public static <K, V extends Comparable> int partition(ArrayList<K> sortedUrls, int low, int high, HashMap <K, V> results){
		V pivot = results.get(sortedUrls.get((high+low)/2));
		int i = low;
		int j = high;

		K tmp;

		while (i <= j){
			while (results.get(sortedUrls.get(i)).compareTo(pivot) > 0){
				i++;
			}
			while (results.get(sortedUrls.get(j)).compareTo(pivot) < 0){
				j--;
			}
			if (i <= j){
				tmp = sortedUrls.get(i);
				sortedUrls.set(i, sortedUrls.get(j));
				sortedUrls.set(j, tmp);
				i++;
				j--;
			}
		}

//		for (int j = low; j < high; j++){
//			if (results.get(sortedUrls.get(j)).compareTo(pivot) > 0){
//				i ++;
//				K temp = sortedUrls.get(i);
//				sortedUrls.set(i, sortedUrls.get(j));
//				sortedUrls.set(j, temp);
//			}
//		}
//		K temp = sortedUrls.get(i+1);
//		sortedUrls.set(i+1, sortedUrls.get(high));
//		sortedUrls.set(high,temp);

		return i;
	}

	public static <K, V extends Comparable> void quickSort(ArrayList<K> sortedUrls, int low, int high, HashMap <K, V> results){
		if (low < high){
			int partitioningIndex = partition(sortedUrls, low, high, results);

			quickSort(sortedUrls, low, partitioningIndex-1, results);
			quickSort(sortedUrls, partitioningIndex+1, high, results);
		}
	}

	public static <K, V extends Comparable> ArrayList<K> fastSort(HashMap<K, V> results) {
		ArrayList<K> sortedUrls = new ArrayList<K>(); //this will be the final output
		sortedUrls.addAll(results.keySet());	//Start with unsorted list of numbers (we need to sort by value later)
		quickSort(sortedUrls, 0, sortedUrls.size()-1, results);
		return sortedUrls;
	}
}