package projet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Minimalisation {

	public static Automate Minimal(Automate automate) {

		String[][] tabT = automate.tabTransition;

		HashMap conversionSI = new HashMap();
		HashMap conversionIS = new HashMap();

		int conversion = 1;
		for (String str : automate.listEtats) {
			conversionSI.put(str, conversion);
			conversionIS.put(conversion, str);
			conversion++;
		}

		List<Integer> ET = new ArrayList<Integer>();
		for (String str : automate.sorties) {
			ET.add((Integer) conversionSI.get(str));
		}

		List<Integer> ENT = new ArrayList<Integer>();
		HashMap groupe = new HashMap();

		for (int i = 1; i <= automate.nbrEtats; i++) {
			if (ET.contains(i)) {
				groupe.put(i, "ET");
			} else {
				ENT.add(i);
				groupe.put(i, "ENT");
			}
		}

		List<List<Integer>> listGroupe = new ArrayList<List<Integer>>();
		listGroupe.add(ET);
		listGroupe.add(ENT);

		System.out.print("Etats Terminaux : ");
		for (int i : ET) {
			System.out.print("{" + conversionIS.get(i) + "} ");
		}
		System.out.println();
		System.out.println("\nTransitions partielles :");
		String[][] formatET = new String[2][automate.nbrsymbs + 1];

		int max = 0;
		max = "ET".length();
		formatET[1][0] = "\n" + "ET";
		formatET[0][0] = "\n";
		for (int i = 0; i < max; i++) {
			formatET[0][0] += " ";
		}
		for (int j = 1; j < automate.nbrsymbs + 1; j++) {
			max = ((String) groupe.get(conversionSI.get(tabT[ET.get(0) - 1][j - 1]))).length();
			formatET[1][j] = " | " + groupe.get(conversionSI.get(tabT[ET.get(0) - 1][j - 1]));
			formatET[0][j] = " | " + automate.listSymbs.get(j - 1);
			int diff = max - automate.listSymbs.get(j - 1).length();
			for (int i = 0; i < diff; i++) {
				formatET[0][j] += " ";
			}
		}
		String aff = "";
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < automate.nbrsymbs + 1; j++) {
				aff += formatET[i][j];
			}
		}
		System.out.println(aff);
		System.out.println();
		System.out.println();
		System.out.print("Etats Non Terminaux : ");
		for (int i : ENT) {
			System.out.print("{" + conversionIS.get(i) + "} ");
		}
		System.out.println();
		System.out.println();
		if (ENT.size() > 0) {
			System.out.println("Transitions partielles :");
			String[][] formatENT = new String[2][automate.nbrsymbs + 1];

			max = 0;
			max = "ENT".length();
			formatENT[1][0] = "\n" + "ENT";
			formatENT[0][0] = "\n";
			for (int i = 0; i < max; i++) {
				formatENT[0][0] += " ";
			}
			for (int j = 1; j < automate.nbrsymbs + 1; j++) {
				max = ((String) groupe.get(conversionSI.get(tabT[ET.get(0) - 1][j - 1]))).length();
				formatENT[1][j] = " | " + groupe.get(conversionSI.get(tabT[ET.get(0) - 1][j - 1]));
				formatENT[0][j] = " | " + automate.listSymbs.get(j - 1);
				int diff = max - automate.listSymbs.get(j - 1).length();
				for (int i = 0; i < diff; i++) {
					formatENT[0][j] += " ";
				}
			}
			aff = "";
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < automate.nbrsymbs + 1; j++) {
					aff += formatENT[i][j];
				}
			}
			System.out.println(aff);
			System.out.println();
			System.out.println();
		}

		boolean ismin = true;
		for (List<Integer> k : listGroupe) {
			int i = 1;
			while (ismin && i <= automate.nbrEtats) {
				if (k.contains(i)) {
					int j = 1;
					while (ismin && j <= automate.nbrEtats) {
						if (k.contains(j)) {
							ismin = Memegroupe(i, j, groupe, automate.nbrsymbs, tabT, conversionSI);
						}
						j++;
					}
				}
				i++;
			}
		}
		while (!ismin) {
			List<List<Integer>> nlist = new ArrayList<List<Integer>>();
			HashMap ngroupe = new HashMap();
			for (List<Integer> k : listGroupe) {
				List<List<Integer>> souslist = new ArrayList<List<Integer>>();
				for (int i : k) {
					if (i == k.get(0)) {
						List<Integer> E1 = new ArrayList<Integer>();
						E1.add(i);
						souslist.add(E1);
						ngroupe.put(i, "" + i + "");
					} else {
						boolean trouver = false;
						for (List<Integer> l : souslist) {
							if (!trouver) {
								boolean mg = Memegroupe(i, l.get(0), groupe, automate.nbrsymbs, tabT, conversionSI);
								if (mg) {
									l.add(i);
									ngroupe.put(i, ngroupe.get(l.get(0)));
									trouver = true;
								}
							}
						}
						if (!trouver) {
							List<Integer> newEtat = new ArrayList<Integer>();
							newEtat.add(i);
							souslist.add(newEtat);
							ngroupe.put(i, "" + i + "");
						}
					}
				}
				for (List<Integer> j : souslist) {
					nlist.add(j);
				}
			}
			listGroupe = nlist;
			groupe = ngroupe;

			for (List<Integer> k : listGroupe) {
				System.out.print(groupe.get(k.get(0)) + " : ");
				for (int i : k) {
					System.out.print(conversionIS.get(i) + " ");
				}
				System.out.println();
				System.out.println("\nTransitions partielles :");
				String[][] format = new String[2][automate.nbrsymbs + 1];

				max = 0;
				max = ((String) groupe.get(k.get(0))).length();
				format[1][0] = "\n" + groupe.get(k.get(0));
				format[0][0] = "\n";
				for (int i = 0; i < max; i++) {
					format[0][0] += " ";
				}
				for (int j = 1; j < automate.nbrsymbs + 1; j++) {
					max = ((String) groupe.get(conversionSI.get(tabT[k.get(0) - 1][j - 1]))).length();
					format[1][j] = " | " + groupe.get(conversionSI.get(tabT[k.get(0) - 1][j - 1]));
					format[0][j] = " | " + automate.listSymbs.get(j - 1);
					int diff = max - automate.listSymbs.get(j - 1).length();
					for (int i = 0; i < diff; i++) {
						format[0][j] += " ";
					}
				}
				aff = "";
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < automate.nbrsymbs + 1; j++) {
						aff += format[i][j];
					}
				}
				System.out.println(aff);
				System.out.println();
				System.out.println();
			}

			ismin = true;
			for (List<Integer> k : listGroupe) {
				int i = 1;
				while (ismin && i <= automate.nbrEtats) {
					if (k.contains(i)) {
						int j = 1;
						while (ismin && j <= automate.nbrEtats) {
							if (k.contains(j)) {
								ismin = Memegroupe(i, j, groupe, automate.nbrsymbs, tabT, conversionSI);
							}
							j++;
						}

					}
					i++;
				}
			}
		}

		int nbrsymbs = automate.nbrsymbs;
		ArrayList<String> listSymbs = automate.listSymbs;
		List<String> entrees = automate.entrees;
		List<String> newentrees = new ArrayList<String>();
		for (String str : entrees) {
			int i = (int) conversionSI.get(str);
			String gr = (String) groupe.get(i);
			if (newentrees.contains(gr)) {
			} else {
				newentrees.add(gr);
			}
		}
		List<String> sorties = automate.sorties;
		List<String> newsorties = new ArrayList<String>();
		for (String str : sorties) {
			int i = (int) conversionSI.get(str);
			String gr = (String) groupe.get(i);
			if (newsorties.contains(gr)) {
			} else {
				newsorties.add(gr);
			}
		}
		int nbrEtats = listGroupe.size();
		List<String> listEtats = new ArrayList<String>();
		for (List<Integer> i : listGroupe) {
			listEtats.add((String) groupe.get(i.get(0)));
		}
		int nbrTrans = nbrsymbs * nbrEtats;
		List<String[]> listTrans = new ArrayList<String[]>();
		String[][] tabTransition = new String[nbrEtats][nbrsymbs];
		for (int i = 0; i < nbrEtats; i++) {
			for (int j = 0; j < nbrsymbs; j++) {
				String k = tabT[listGroupe.get(i).get(0) - 1][j];
				String l = (String) groupe.get(conversionSI.get(k));
				tabTransition[i][j] = l;
				String[] newTrans = new String[3];
				newTrans[0] = listEtats.get(i);
				newTrans[1] = listSymbs.get(j);
				newTrans[2] = l;
				listTrans.add(newTrans);
			}
		}

		if (nbrEtats == automate.nbrEtats) {
			System.out.println("L'automate était déjà minimal");
		}

		System.out.println("Correspondance des états");
		for (List<Integer> li : listGroupe) {
			String nomgroupe = (String) groupe.get(li.get(0));
			System.out.print("\n" + nomgroupe + " : ");
			for (int i : li) {
				String origine = (String) conversionIS.get(i);
				System.out.print("{" + origine + "} ");
			}
		}
		System.out.println();
		Automate automateMin = new Automate(nbrsymbs, listSymbs, newentrees, newsorties, nbrEtats, listEtats, nbrTrans,
				listTrans, tabTransition);
		return automateMin;
	}

	private static boolean Memegroupe(int i1, int i2, HashMap groupe, int nbrsymbs, String[][] tabT,
			HashMap conversion) {
		boolean mg = true;
		int j = 0;
		while (j < nbrsymbs && mg) {
			mg = (groupe.get((Integer) conversion.get(tabT[i1 - 1][j]))
					.equals(groupe.get((Integer) conversion.get(tabT[i2 - 1][j]))));
			j++;
		}
		return mg;
	}
}
