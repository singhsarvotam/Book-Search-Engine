import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class DocumentWeight {
	public static void main(String[] args) {

		String[] s1 = { "travel", "happiness", "family", "New", "York", "Los", "Angeles" };
		String[] s2 = { "medicine", "biology", "university", "cells", "San", "Diego" };
		String[] s3 = { "science", "knowledge", "travel", "San", "Diego" };
		String[] s4 = { "mother", "girl", "child", "family", "New", "York" };
		String[] s5 = { "singing", "dancing", "shopping", "shopping", "singing", "shopping", "New", "York" };
		String[] s6 = { "fitness", "beach", "Los", "Angeles", "San", "Diego" };
		String[] s7 = { "Computers", "medicine", "university", "internet", "family" };
		String[] s8 = { "chemistry", "substance", "science", "nature", "family" };
		String[] s9 = { "Medicine", "New", "York", "Los", "Angeles" };
		String[] s10 = { "Science", "family", "travel", "shopping" };

		ArrayList<String[]> list = new ArrayList<String[]>();
		ArrayList<String> global = new ArrayList<String>();
		ArrayList<String> filenames = new ArrayList<String>();

		File folder = new File("D:/EclipseWorkspace/BOOKPROJECT/goodreads/books");
		List<File> files = Arrays.asList(folder.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return f.isFile() && f.getName().endsWith(".txt");

			}
		}));
		BufferedReader in = null;
		for (File f : files) {
			try {
				in = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StringBuffer sb = new StringBuffer();
			String s = null;
			try {
				while ((s = in.readLine()) != null) {
					sb.append(s);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// input cleaning regex
			String[] d = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
			for (String u : d)
				if (!global.contains(u))
					global.add(u);
			list.add(d);
			filenames.add(f.getName());
		}

		ArrayList<double[]> vecspace = new ArrayList<double[]>();
		for (String[] s : list) {
			double[] d = new double[global.size()];
			for (int i = 0; i < global.size(); i++)
				d[i] = tf(s, global.get(i)) * idf(list, global.get(i));
			vecspace.add(d);
		}
		HashMap<double[], TreeSet<Integer>> clusters = new HashMap<double[], TreeSet<Integer>>();
		HashMap<double[], TreeSet<Integer>> step = new HashMap<double[], TreeSet<Integer>>();
		HashSet<Integer> rand = new HashSet<Integer>();
		TreeMap<Double, HashMap<double[], TreeSet<Integer>>> errorsums = new TreeMap<Double, HashMap<double[], TreeSet<Integer>>>();
		int k = 3;

		// might need to adjust depending on our data.
		int maxiter = 500;
		for (int init = 0; init < 100; init++) {
			clusters.clear();
			step.clear();
			rand.clear();
			// randomly initialize cluster centers
			while (rand.size() < k)
				rand.add((int) (Math.random() * vecspace.size()));
			for (int r : rand) {
				double[] temp = new double[vecspace.get(r).length];
				System.arraycopy(vecspace.get(r), 0, temp, 0, temp.length);
				step.put(temp, new TreeSet<Integer>());
			}
			boolean go = true;
			int iter = 0;
			while (go) {
				clusters = new HashMap<double[], TreeSet<Integer>>(step);
				// cluster assignment step
				for (int i = 0; i < vecspace.size(); i++) {
					double[] cent = null;
					double sim = 0;
					for (double[] c : clusters.keySet()) {
						double csim = cosSim(vecspace.get(i), c);
						if (csim > sim) {
							sim = csim;
							cent = c;
						}
					}
					clusters.get(cent).add(i);
				}

				step.clear();
				for (double[] cent : clusters.keySet()) {
					double[] updatec = new double[cent.length];
					for (int d : clusters.get(cent)) {
						double[] doc = vecspace.get(d);
						for (int i = 0; i < updatec.length; i++)
							updatec[i] += doc[i];
					}
					for (int i = 0; i < updatec.length; i++)
						updatec[i] /= clusters.get(cent).size();
					step.put(updatec, new TreeSet<Integer>());
				}
				String oldcent = "", newcent = "";
				for (double[] x : clusters.keySet())
					oldcent += Arrays.toString(x);
				for (double[] x : step.keySet())
					newcent += Arrays.toString(x);
				if (oldcent.equals(newcent))
					go = false;
				if (++iter >= maxiter)
					go = false;
			}
			System.out.println(clusters.toString().replaceAll("\\[[\\w@]+=", ""));
			if (iter < maxiter)
				System.out.println("Converged in " + iter + " steps.");
			else
				System.out.println("Stopped after " + maxiter + " iterations.");
			System.out.println("");

			// calculate similarity sum and map it to the clustering
			double sumsim = 0;
			for (double[] c : clusters.keySet()) {
				TreeSet<Integer> cl = clusters.get(c);
				for (int vi : cl) {
					sumsim += cosSim(c, vecspace.get(vi));
				}

			}
			errorsums.put(sumsim, new HashMap<double[], TreeSet<Integer>>(clusters));

		}

		System.out.println("Best Convergence:");
		System.out.println(errorsums.get(errorsums.lastKey()).toString().replaceAll("\\[[\\w@]+=", ""));
		System.out.print("{");
		for (double[] cent : errorsums.get(errorsums.lastKey()).keySet()) {
			System.out.print("[");
			for (int pts : errorsums.get(errorsums.lastKey()).get(cent)) {
				// System.out.print(filenames.get(pts).substring(0,filenames.get(pts).lastIndexOf(".txt"))+",
				// ");
				System.out.print(filenames.get(pts) + ", ");
			}
			System.out.print("\b\b], ");
		}
		System.out.println("\b\b}");

	}

	static double cosSim(double[] a, double[] b) {
		double dotp = 0, maga = 0, magb = 0;
		for (int i = 0; i < a.length; i++) {
			dotp += a[i] * b[i];
			maga += Math.pow(a[i], 2);
			magb += Math.pow(b[i], 2);
		}
		maga = Math.sqrt(maga);
		magb = Math.sqrt(magb);
		double d = dotp / (maga * magb);
		return d == Double.NaN ? 0 : d;
	}

	static double tf(String[] doc, String term) {
		double n = 0;
		for (String s : doc)
			if (s.equalsIgnoreCase(term))
				n++;
		return n / doc.length;
	}

	static double idf(ArrayList<String[]> docs, String term) {
		double n = 0;
		for (String[] x : docs)
			for (String s : x)
				if (s.equalsIgnoreCase(term)) {
					n++;
					break;
				}
		return Math.log(docs.size() / n);
	}

}
