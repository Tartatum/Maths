package projet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class Determinisation_completion {

	static Automate algorithm(Automate AF) {
		Automate AFDC;
		System.out.println("--- D饕ut de la d騁erminisation et la compl騁ion de l'automate ---");
		System.out.println("L'automate est-il un automate asynchrone ? ");
		if (est_un_automate_asynchrone(AF)) {
			System.out.println("--- D騁erminisation et compl騁ion automate asynchrone(AF) ---");
			AFDC = determinisation_et_completion_automate_asynchrone(AF);
		} else {
			System.out.println("L'automate est-il un automate d騁erministe ?");
			if (est_un_automate_deterministe(AF)) {
				System.out.println("L'automate est-il un automate complet ?");
				if (est_un_automate_complet(AF)) {
					System.out.println(" --- Donc AFDC = AF ---");
					AFDC = AF;
				} else {
					System.out.println(" --- AFDC = compl騁ion(AF) ---");
					AFDC = completion(AF);
				}
			} else {
				System.out.println("--- D騁erminisation et compl騁ion automate synchrone(AF) ---");
				AFDC = determinisation_et_completion_automate_synchrone(AF);
			}
		}
		System.out.println("--- Affichage de l'automate d騁erministe complet(AF) ---");
		afficher_automate_deterministe_complete(AFDC);
		return AFDC;
	}

	private static void afficher_automate_deterministe_complete(Automate AFDC) {
		AFDC.info();
		AFDC.Affichage();
	}

	private static Automate completion(Automate AF) {
		Automate completionAFD = (Automate) AF.clone();
		String[][] tabTransition = null;
		if (!AF.listEtats.contains("-1")) {

			completionAFD.nbrEtats = AF.nbrEtats + 1;
			completionAFD.listEtats.add("P");

			tabTransition = new String[completionAFD.nbrEtats][completionAFD.nbrsymbs];
			for (int i = 0; i < AF.nbrEtats; i++) {
				for (int j = 0; j < AF.nbrsymbs; j++) {
					tabTransition[i][j] = AF.tabTransition[i][j];
				}
			}
			for (int j = 0; j < AF.nbrsymbs; j++) {
				tabTransition[AF.nbrEtats][j] = "-";
			}

		} else {
			tabTransition = new String[completionAFD.nbrEtats][completionAFD.nbrsymbs];
		}
		for (int i = 0; i < completionAFD.nbrEtats; i++) {
			for (int j = 0; j < completionAFD.nbrsymbs; j++) {
				if (tabTransition[i][j].equals("-")) {
					tabTransition[i][j] = "-1";
				}
			}
		}
		completionAFD.tabTransition = tabTransition;
		completionAFD.info();
		completionAFD.Affichage();
		return completionAFD;
	}

	private static boolean est_un_automate_deterministe(Automate AF) {
		// Condition de l'unicit� de l'entr馥
		if (AF.entrees.size() != 1) {
			System.out.println("L'automate n'est pas d騁erministe car il y a plusieurs entr馥s : " + AF.entrees);
			return false;
		}
		for (int i = 0; i < AF.nbrEtats; i++) {
			for (int k = 0; k < AF.nbrsymbs; k++) {
				if (AF.tabTransition[i][k].split(",").length > 1) {
					System.out.println(
							"L'automate n'est pas d騁erministe car il y a au moins un 騁at poss馘ant 2 transitions lib駘l馥s par le m麥e symboles : ");
					for (String str : AF.tabTransition[i][k].split(",")) {
						System.out.println(AF.listEtats.get(i) + " -> " + AF.listSymbs.get(k) + " -> " + str);
					}
					return false;
				}
				;
			}
		}
		System.out.println("L'automate est d騁erministe.");
		return true;
	}

	private static boolean est_un_automate_complet(Automate AF) {
		for (int i = 0; i < AF.nbrEtats; i++) {
			for (int k = 0; k < AF.nbrsymbs; k++) {
				if (AF.tabTransition[i][k].equals("-")) {
					System.out.println("L'automate n'est pas complet.");
					return false;
				}
				;
			}
		}
		System.out.println("L'automate est complet.");
		return true;
	}

	static boolean est_un_automate_asynchrone(Automate AF) {
		List<String[]> epsilon = new ArrayList<String[]>();
		int async = 0;
		for (String[] trans : AF.listTrans) {
			if (trans[1].equals("*")) {
				if (async == 0) {
					System.out.println("L'automate est asynchrone, voici les 駘駑ents le prouvant :");
					async = 1;
				}
				epsilon.add(trans);
				System.out.println(trans[0] + " -> " + trans[1] + " -> " + trans[2]);
			}
		}
		if (async == 1) {
			return true;
		} else {
			System.out.println("L'automate n'est pas asynchrone, il est synchrone.");
			return false;
		}
	}

	static Automate determinisation_et_completion_automate_asynchrone(Automate AF) {
		// Obtient la liste des fermetures epsilons;
		List<TreeSet<Integer>> ferm_ep = fermeture_epsilon(AF);
		// Avoir l'騁at initial
		TreeSet<Integer> entree = new TreeSet<Integer>();
		for (TreeSet<Integer> fe : ferm_ep) {
			if (AF.contient_entree(fe)) {
				entree.addAll(fe);
			}
		}
		// System.out.println(entree.toString());

		// D騁erminisation
		System.out.println("--- D騁erminisation --- ");
		HashSet<TreeSet<Integer>> etats = new HashSet<TreeSet<Integer>>();
		HashSet<TreeSet<Integer>> etats_a_traiter = new HashSet<TreeSet<Integer>>();
		List<TreeSet<Integer>> listEtats = new ArrayList<TreeSet<Integer>>();
		ArrayList<ArrayList<TreeSet<Integer>>> tabEtats = new ArrayList<ArrayList<TreeSet<Integer>>>();
		etats_a_traiter.add(entree);
		int counter = 0;
		while (etats_a_traiter.size() != 0) {
			System.out.println("It駻ation : " + counter);
			counter++;
			TreeSet<Integer> checking = etats_a_traiter.iterator().next();
			System.out.println("Ensemble de la fermeture epsilon : " + checking);
			etats.add(checking);
			listEtats.add(checking);
			ArrayList<TreeSet<Integer>> ligne = new ArrayList<TreeSet<Integer>>();
			tabEtats.add(ligne);
			// Trouve les 騁ats suivants � partir de l'entr馥 pour chaques symboles
			for (int i = 0; i < AF.nbrsymbs - 1; i++) {
				// Contients l'騁at par le symbole
				TreeSet<Integer> etat_pro = new TreeSet<Integer>();
				// TreeSet<Integer> etat_pro_check = new TreeSet<Integer>();
				// Obitient la list des transitions contenant les entr馥s et le symbole en
				// transition
				List<String[]> listT = AF.transition_async_entree_symb(checking, AF.listSymbs.get(i));
				for (String[] T : listT) {
					etat_pro.addAll(ferm_ep.get(Integer.parseInt(T[2]) - 1));
				}
				ligne.add(i, etat_pro);
				// if(etats.contains(etat_pro)) {
				if (etat_pro.size() != 0 && !etats.contains(etat_pro)) {
					etats_a_traiter.add(etat_pro);
				}
			}
			// Affichage
			for (int i = 0; i < AF.nbrsymbs - 1; i++) {
				System.out.println("Par " + AF.listSymbs.get(i) + " : " + ligne.get(i));
			}
			etats_a_traiter.remove(checking);
		}

		System.out.println("--- Compl騁ion --- ");
		// Completion (Ajout de la poubelle)
		ArrayList<TreeSet<Integer>> ligne = new ArrayList<TreeSet<Integer>>();
		for (int i = 0; i < AF.nbrsymbs - 1; i++) {
			ligne.add(new TreeSet<Integer>());
		}
		listEtats.add(new TreeSet<Integer>());
		tabEtats.add(ligne);

		// System.out.println("listEtat :" + listEtats.toString());
		// System.out.println("tabEtats : " + tabEtats);

		// Recherche des entr馥s
		System.out.println("--- Recherche entr馥 --- ");
		HashSet<TreeSet<Integer>> entrees = new HashSet<TreeSet<Integer>>();
		for (TreeSet<Integer> etat : listEtats) {
			for (int i : AF.entrees) {
				if (etat.contains(i)) {
					entrees.add(etat);
				}
			}
		}
		// System.out.println("Entrees : " + entrees);

		// Recherche des sorties
		System.out.println("--- Recherche sorties --- ");
		HashSet<TreeSet<Integer>> sorties = new HashSet<TreeSet<Integer>>();
		for (TreeSet<Integer> etat : listEtats) {
			for (int i : AF.sorties) {
				if (etat.contains(i)) {
					sorties.add(etat);
				}
			}
		}
		// System.out.println("Sorties : " + sorties);

		Automate AFDC = (Automate) AF.clone();
		AFDC.nbrsymbs = AFDC.nbrsymbs - 1;
		AFDC.listSymbs.remove(AFDC.nbrsymbs);
		AFDC.entrees = convert_HashSet_to_List_index(entrees, listEtats);
		AFDC.sorties = convert_HashSet_to_List_index(sorties, listEtats);
		AFDC.nbrEtats = listEtats.size();
		AFDC.listEtats = convert_TreeSetList_to_IntList(listEtats);
		AFDC.nbrTrans = listEtats.size() * AFDC.nbrsymbs;
		// AFDC.listTrans = listTrans;
		AFDC.tabTransition = convert_HashSetTab_to_StringTab(tabEtats, listEtats);
		return AFDC;
	}

	static Automate determinisation_et_completion_automate_synchrone(Automate AF) {

		// Etat initial
		TreeSet<Integer> entree = new TreeSet<Integer>();
		for (int ent : AF.entrees) {
			entree.add(ent);
		}
		// D騁erminisation
		HashSet<TreeSet<Integer>> etats = new HashSet<TreeSet<Integer>>();
		HashSet<TreeSet<Integer>> etats_a_traiter = new HashSet<TreeSet<Integer>>();
		List<TreeSet<Integer>> listEtats = new ArrayList<TreeSet<Integer>>();
		ArrayList<ArrayList<TreeSet<Integer>>> tabEtats = new ArrayList<ArrayList<TreeSet<Integer>>>();
		etats_a_traiter.add(entree);
		while (etats_a_traiter.size() != 0) {

			TreeSet<Integer> checking = etats_a_traiter.iterator().next();
			// System.out.println(" checking : " + checking);
			etats.add(checking);
			listEtats.add(checking);
			ArrayList<TreeSet<Integer>> ligne = new ArrayList<TreeSet<Integer>>();
			tabEtats.add(ligne);
			// Trouve les 騁ats suivants � partir de l'entr馥 pour chaques symboles
			for (int i = 0; i < AF.nbrsymbs; i++) {
				// Contients l'騁at par le symbole
				TreeSet<Integer> etat_pro = new TreeSet<Integer>();
				// TreeSet<Integer> etat_pro_check = new TreeSet<Integer>();
				// Obitient la list des transitions contenant les entr馥s et le symbole en
				// transition
				List<String[]> listT = AF.transition_async_entree_symb(checking, AF.listSymbs.get(i));
				for (String[] T : listT) {
					etat_pro.add(Integer.parseInt(T[2]));
				}
				ligne.add(i, etat_pro);
				if (etat_pro.size() != 0 && !etats.contains(etat_pro)) {
					etats_a_traiter.add(etat_pro);
				}
			}
			// System.out.println("ligne : "+ ligne);
			etats_a_traiter.remove(checking);
		}

		// Completion (Ajout de la poubelle)
		ArrayList<TreeSet<Integer>> ligne = new ArrayList<TreeSet<Integer>>();
		for (int i = 0; i < AF.nbrsymbs; i++) {
			ligne.add(new TreeSet<Integer>());
		}
		listEtats.add(new TreeSet<Integer>());
		tabEtats.add(ligne);

		// System.out.println("listEtat :" + listEtats.toString());
		// System.out.println("tabEtats : " + tabEtats);

		// Recherche des entr馥s
		HashSet<TreeSet<Integer>> entrees = new HashSet<TreeSet<Integer>>();
		for (TreeSet<Integer> etat : listEtats) {
			for (int i : AF.entrees) {
				if (etat.contains(i)) {
					entrees.add(etat);
				}
			}
		}

		// System.out.println("Entrees : " + entrees);

		// Recherche des sorties
		HashSet<TreeSet<Integer>> sorties = new HashSet<TreeSet<Integer>>();
		for (TreeSet<Integer> etat : listEtats) {
			for (int i : AF.sorties) {
				if (etat.contains(i)) {
					sorties.add(etat);
				}
			}
		}

		// System.out.println("Sorties : " + sorties);
		Automate AFDC = (Automate) AF.clone();
		AFDC.nbrsymbs = AFDC.nbrsymbs;
		AFDC.entrees = convert_HashSet_to_List_index(entrees, listEtats);
		AFDC.sorties = convert_HashSet_to_List_index(sorties, listEtats);
		AFDC.nbrEtats = listEtats.size();
		AFDC.listEtats = convert_TreeSetList_to_IntList(listEtats);
		AFDC.nbrTrans = listEtats.size() * AFDC.nbrsymbs;
		// AFDC.listTrans = listTrans;
		AFDC.tabTransition = convert_HashSetTab_to_StringTab(tabEtats, listEtats);
		return AFDC;
	}

	static List<Integer> convert_HashSet_to_List_index(HashSet<TreeSet<Integer>> s, List<TreeSet<Integer>> listEtats) {
		List<Integer> r = new ArrayList<Integer>();
		for (TreeSet<Integer> e : s) {
			r.add(listEtats.indexOf(e));
		}
		// System.out.println(r);
		return r;
	}

	/*
	 * static List<String[]> convert_Liste_Transition(String[][] tab, List<Integer>
	 * etats, List<String> symboles){ List<String[]> r = new ArrayList<String[]>();
	 * for(String[] ligne : tab) { for(String col : ligne) { String[] trans =
	 * etats[1] + " "; } } return r; }
	 */

	static String[][] convert_HashSetTab_to_StringTab(ArrayList<ArrayList<TreeSet<Integer>>> s,
			List<TreeSet<Integer>> listEtats) {
		int l = s.size();
		int c = s.get(0).size();
		String[][] r = new String[l][c];
		for (int i = 0; i < l; i++) {
			for (int j = 0; j < c; j++) {
				TreeSet<Integer> u = s.get(i).get(j);
				if (u.size() == 0) {
					r[i][j] = "-1";
				} else {
					r[i][j] = Integer.toString(listEtats.indexOf(u));
				}
			}
		}
		// System.out.println(r);
		return r;
	}

	static List<Integer> convert_TreeSetList_to_IntList(List<TreeSet<Integer>> listEtats) {
		List<Integer> r = new ArrayList<Integer>();
		for (TreeSet<Integer> s : listEtats) {
			r.add(listEtats.indexOf(s));
		}
		return r;
	}

	static List<TreeSet<Integer>> fermeture_epsilon(Automate AF) {
		List<TreeSet<Integer>> fe = new ArrayList<TreeSet<Integer>>();
		for (int etat : AF.listEtats) {
			// TreeSet<Integer> ferm_epsil= new TreeSet<Integer>();
			// HastSet<Integer> ferm_epsil_check = new TreeSet<Integer>();
			if (AF.tabTransition[etat - 1][AF.nbrsymbs - 1].equals("-")) {
				// System.out.println("Pas de epsilon");
				TreeSet<Integer> fer_epsi = new TreeSet<Integer>();
				fer_epsi.add(etat);
				fe.add(fer_epsi);
			} else {
				// System.out.println("Epsilon pour " + etat);
				TreeSet<Integer> check = new TreeSet<Integer>();
				TreeSet<Integer> fer_epsi = new TreeSet<Integer>();
				check.add(etat); // Ajout dans la list des checks
				int counter = AF.nbrEtats + 1;
				while (check.size() != 0 || counter == 0) {

					int checking = check.iterator().next();
					// System.out.println("checking : " + checking);
					// Ajout pour savoir si l'騁at a 騁� v駻ifi� ou non.
					fer_epsi.add(checking);
					// Obtient la list des transitions avec comme d駱art l'騁at � v駻ifier
					List<String[]> listT = AF.transition_epsilon_commancant_par(checking);
					if (listT.size() != 0) {
						for (String[] trans : listT) {
							// Si l'arriv� n'est pas d駛� pr駸ent dans la fermeture, l'ajoute dans la
							// verification
							if (!fer_epsi.contains(Integer.parseInt(trans[2]))) {
								check.add(Integer.parseInt(trans[2]));
							}
						}
					}
					// Fin de la verification
					counter -= 1;
					check.remove(checking);
				}
				fe.add(fer_epsi);
			}
		}
		System.out.println(fe.toString());
		return fe;
	}

}
