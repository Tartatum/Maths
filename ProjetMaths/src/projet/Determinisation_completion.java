package projet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.List;
import java.util.Set;

public class Determinisation_completion {

	
	static Automate algorithm(Automate AF) {
		Automate AFDC;
		System.out.println("--- D�but de la d�terminisation et la compl�tion de l'automate ---");
		System.out.println("L'automate est-il un automate asynchrone ? ");
		if (est_un_automate_asynchrone(AF)) {
			System.out.println("--- D�terminisation et compl�tion automate asynchrone(AF) ---");
			AFDC = determinisation_et_completion_automate_asynchrone(AF);
		}
		else {
			System.out.println("L'automate est-il un automate d�terministe ?");
			if(est_un_automate_deterministe(AF)) {
				System.out.println("L'automate est-il un automate complet ?");
				if(est_un_automate_complet(AF)) {
					System.out.println(" --- Donc AFDC = AF ---");
					AFDC = AF;
				}
				else {
					System.out.println(" --- AFDC = compl�tion(AF) ---");
					AFDC = completion(AF);
				}
			}
			else {
				System.out.println("--- D�terminisation et compl�tion automate synchrone(AF) ---");
				AFDC = determinisation_et_completion_automate_synchrone(AF);
			}
		}
		System.out.println("--- Affichage de l'automate d�terministe complet(AF) ---");
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
		if(!AF.listEtats.contains("P")) {
			
			completionAFD.nbrEtats = AF.nbrEtats + 1 ;
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
			
		}
		else {
			tabTransition = new String[completionAFD.nbrEtats][completionAFD.nbrsymbs];
		}
		for (int i = 0; i < completionAFD.nbrEtats; i++) {
			for (int j = 0; j < completionAFD.nbrsymbs; j++) {
				if(tabTransition[i][j].equals("-")) {
					tabTransition[i][j]= "P";
				}
			}
		}
		completionAFD.tabTransition = tabTransition;
		completionAFD.info();
		completionAFD.Affichage();
		return completionAFD;
	}

	private static boolean est_un_automate_deterministe(Automate AF) {
		// Condition de l'unicit� de l'entr�e  
		if(AF.entrees.size() != 1) {
			System.out.println("L'automate n'est pas d�terministe car il y a plusieurs entr�es : " + AF.entrees );
			return false;
		}
		for(int i = 0; i < AF.nbrEtats; i++) {
			for(int k = 0; k < AF.nbrsymbs;k++) {
				if(AF.tabTransition[i][k].split(",").length > 1) {
					System.out.println("L'automate n'est pas d�terministe car il y a au moins un �tat poss�dant 2 transitions lib�ll�es par le m�me symboles : ");
					for(String str : AF.tabTransition[i][k].split(",")) {
						System.out.println(AF.listEtats.get(i) + " -> " + AF.listSymbs.get(k) + " -> " + str );
					}
					return false;
				};
			}
		}
		System.out.println("L'automate est d�terministe.");
		return true;
	}

	private static boolean est_un_automate_complet(Automate AF) {
		for(int i = 0; i < AF.nbrEtats; i++) {
			for(int k = 0; k < AF.nbrsymbs;k++) {
				if(AF.tabTransition[i][k].equals("-")) {
					System.out.println("L'automate n'est pas complet.");
					return false;
				};
			}
		}
		System.out.println("L'automate est complet.");
		return true;
	}

	static boolean est_un_automate_asynchrone(Automate AF) {
		List<String[]> epsilon = new ArrayList<String[]>();
		int async = 0;
		for(String[] trans : AF.listTrans ) {
			if (trans[1].equals("*")) {
				if(async == 0) {
					System.out.println("L'automate est asynchrone, voici les �l�ments le prouvant :");
					async = 1;
				}
				epsilon.add(trans);
				System.out.println(trans[0] +" -> " + trans[1] +" -> "+ trans[2]);
			}
		}
		if (async == 1) {
			return true;
		}
		else {
			System.out.println("L'automate n'est pas asynchrone, il est synchrone.");
			return false;
		}
	}
	
	static Automate determinisation_et_completion_automate_asynchrone(Automate AF) {
		// Obtient la liste des fermetures epsilons;
		List<TreeSet<String>> ferm_ep = fermeture_epsilon(AF);
		// Avoir l'état initial

		TreeSet<String> entree = new TreeSet<String>();
		for (TreeSet<String> fe : ferm_ep) {
			if(AF.contient_entree(fe)) {
				entree.addAll(fe);
			}
		}
		//System.out.println(entree.toString());
		
		// Déterminisation  
		System.out.println("--- D�terminisation --- ");
		HashSet<TreeSet<String>> etats = new HashSet<TreeSet<String>>();
		HashSet<TreeSet<String>> etats_a_traiter = new HashSet<TreeSet<String>>();
		List<TreeSet<String>> listEtats = new ArrayList<TreeSet<String>>();
		ArrayList<ArrayList<TreeSet<String>>> tabEtats = new ArrayList<ArrayList<TreeSet<String>>>();
		etats_a_traiter.add(entree);
		int counter = 0 ;
		while(etats_a_traiter.size() != 0 ) {
			System.out.println("It�ration : " + counter);
			counter++;
			TreeSet<String> checking = etats_a_traiter.iterator().next();
			System.out.println("Ensemble de la fermeture epsilon : " + checking);
			etats.add(checking);
			listEtats.add(checking);
			ArrayList<TreeSet<String>> ligne = new ArrayList<TreeSet<String>>();
			tabEtats.add(ligne);
			// Trouve les �tats suivants � partir de l'entr�e pour chaques symboles
			for(int i = 0; i < AF.nbrsymbs-1; i++) {
				// Contients l'état par le symbole
				TreeSet<String> etat_pro = new TreeSet<String>();
				//TreeSet<Integer> etat_pro_check = new TreeSet<Integer>();
				// Obitient la list des transitions contenant les entr�es et le symbole en transition 
				List<String[]> listT = AF.transition_async_entree_symb(checking,AF.listSymbs.get(i));
				for (String[] T : listT) {
					etat_pro.addAll(ferm_ep.get(AF.listEtats.indexOf(T[2])));
				}
				ligne.add(i, etat_pro);
				//if(etats.contains(etat_pro)) {
				if(etat_pro.size() != 0  && !etats.contains(etat_pro)) {
					etats_a_traiter.add(etat_pro);
				}
			}
			//Affichage
			for(int i = 0; i < AF.nbrsymbs-1;i++) {
				System.out.println("Par " +AF.listSymbs.get(i) + " : "+ ligne.get(i));
			}
			etats_a_traiter.remove(checking);
		}
		
		System.out.println("--- Compl�tion --- ");
		// Completion (Ajout de la poubelle)
		ArrayList<TreeSet<String>> ligne = new ArrayList<TreeSet<String>>();
		for(int i = 0; i < AF.nbrsymbs-1; i++) {
			ligne.add(new TreeSet<String>());
		}
		listEtats.add(new TreeSet<String>());
		tabEtats.add(ligne);
		
		//System.out.println("listEtat :" + listEtats.toString());
		//System.out.println("tabEtats : " + tabEtats);
		
		

		// Recherche des entrées
		System.out.println("--- Recherche entrée --- ");
		HashSet<TreeSet<String>> entrees = new HashSet<TreeSet<String>>();
//		for(TreeSet<String> etat : listEtats) {
//			for(String i : AF.entrees) {
//				if(etat.contains(i)) {
//					entrees.add(etat);
//				}
//			}
//		}
		entrees.add(entree);
		//System.out.println("Entrees : " + entrees);
		
		// Recherche des sorties
		System.out.println("--- Recherche sorties --- ");
		HashSet<TreeSet<String>> sorties = new HashSet<TreeSet<String>>();
		for(TreeSet<String> etat : listEtats) {
			for(String i : AF.sorties) {
				if(etat.contains(i)) {
					sorties.add(etat);
				}
			}
		}
		//System.out.println("Sorties : " + sorties);
		
		
		
		Automate AFDC = (Automate) AF.clone();
		AFDC.nbrsymbs = AFDC.nbrsymbs-1;
		AFDC.listSymbs.remove(AFDC.nbrsymbs);
		AFDC.entrees = convert_HashSet_to_List_index(entrees, listEtats);
		AFDC.sorties = convert_HashSet_to_List_index(sorties, listEtats);
		AFDC.nbrEtats = listEtats.size();
		AFDC.listEtats = convert_TreeSetList_to_IntList(listEtats);
		AFDC.nbrTrans = listEtats.size() * AFDC.nbrsymbs;
		AFDC.tabTransition = convert_HashSetTab_to_StringTab(tabEtats,listEtats);
		setTabTrans(AFDC);
		return AFDC;
	}
	
	static void setTabTrans(Automate AF) {
		List<String[]> lT = new ArrayList<String[]>();
		for(int i = 0 ; i < AF.nbrEtats;i++) {
			for(int k = 0; k < AF.nbrsymbs;k++) {
				lT.add(new String[] {AF.listEtats.get(i), AF.listSymbs.get(k) ,AF.tabTransition[i][k]});
				
			}
		}
		AF.listTrans = lT;
	}
	
	static Automate determinisation_et_completion_automate_synchrone(Automate AF) {
		
		// Etat initial
		TreeSet<String> entree = new TreeSet<String>();
		for (String ent : AF.entrees) {
			entree.add(ent);
		}
		// Déterminisation  
		HashSet<TreeSet<String>> etats = new HashSet<TreeSet<String>>();
		HashSet<TreeSet<String>> etats_a_traiter = new HashSet<TreeSet<String>>();
		List<TreeSet<String>> listEtats = new ArrayList<TreeSet<String>>();
		ArrayList<ArrayList<TreeSet<String>>> tabEtats = new ArrayList<ArrayList<TreeSet<String>>>();
		etats_a_traiter.add(entree);
		while(etats_a_traiter.size() != 0 ) {
			
			TreeSet<String> checking = etats_a_traiter.iterator().next();
			//System.out.println(" checking : " + checking);
			etats.add(checking);
			listEtats.add(checking);
			ArrayList<TreeSet<String>> ligne = new ArrayList<TreeSet<String>>();
			tabEtats.add(ligne);
			// Trouve les �tats suivants � partir de l'entr�e pour chaques symboles
			for(int i = 0; i < AF.nbrsymbs; i++) {
				// Contients l'état par le symbole
				TreeSet<String> etat_pro = new TreeSet<String>();
				//TreeSet<Integer> etat_pro_check = new TreeSet<Integer>();
				// Obitient la list des transitions contenant les entr�es et le symbole en transition 
				List<String[]> listT = AF.transition_async_entree_symb(checking,AF.listSymbs.get(i));
				for (String[] T : listT) {
					etat_pro.add(T[2]);
				}
				ligne.add(i, etat_pro);
				if(etat_pro.size() != 0 && !etats.contains(etat_pro)) {
					etats_a_traiter.add(etat_pro);
				}
			}
			//System.out.println("ligne : "+ ligne);
			etats_a_traiter.remove(checking);
		}
		
		// Completion (Ajout de la poubelle)
		ArrayList<TreeSet<String>> ligne = new ArrayList<TreeSet<String>>();
		for(int i = 0; i < AF.nbrsymbs; i++) {
			ligne.add(new TreeSet<String>());
		}
		listEtats.add(new TreeSet<String>());
		tabEtats.add(ligne);
		
		//System.out.println("listEtat :" + listEtats.toString());
		//System.out.println("tabEtats : " + tabEtats);
		
		
		// Recherche des entrées
		HashSet<TreeSet<String>> entrees = new HashSet<TreeSet<String>>();
		entrees.add(entree);
//		for(TreeSet<String> etat : listEtats) {
//			for(String i : AF.entrees) {
//				if(etat.contains(i)) {
//					entrees.add(etat);
//				}
//			}
//		}
		
		//System.out.println("Entrees : " + entrees);
		
		// Recherche des sorties
		HashSet<TreeSet<String>> sorties = new HashSet<TreeSet<String>>();
		for(TreeSet<String> etat : listEtats) {
			for(String i : AF.sorties) {
				if(etat.contains(i)) {
					sorties.add(etat);
				}
			}
		}
		
		//System.out.println("Sorties : " + sorties);
		Automate AFDC = (Automate) AF.clone();
		AFDC.nbrsymbs = AFDC.nbrsymbs;
		AFDC.entrees = convert_HashSet_to_List_index(entrees, listEtats);
		AFDC.sorties = convert_HashSet_to_List_index(sorties, listEtats);
		AFDC.nbrEtats = listEtats.size();
		AFDC.listEtats = convert_TreeSetList_to_IntList(listEtats);
		AFDC.nbrTrans = listEtats.size() * AFDC.nbrsymbs;
		AFDC.tabTransition = convert_HashSetTab_to_StringTab(tabEtats,listEtats);
		setTabTrans(AFDC);
		return AFDC;
	}
	
	static List<String> convert_HashSet_to_List_index(HashSet<TreeSet<String>> s,List<TreeSet<String>> listEtats){
		List<String> r = new ArrayList<String>();
		for(TreeSet<String> e : s) {
			String st = "";
			int first = 0;
			for (String str : e) {
				if(first == 0) {
					st = str;
					first = 1;
				}
				else {
					st = st+ ","+ str;
				}
			}
			r.add(st);
			//r.add(listEtats.indexOf(e));
		}
		//System.out.println(r);
		return r;
	}
	
	/*
	 * static List<String[]> convert_Liste_Transition(String[][] tab, List<Integer>
	 * etats, List<String> symboles){ List<String[]> r = new ArrayList<String[]>();
	 * for(String[] ligne : tab) { for(String col : ligne) { String[] trans =
	 * etats[1] + " "; } } return r; }
	 */
	
	static String[][] convert_HashSetTab_to_StringTab(ArrayList<ArrayList<TreeSet<String>>> s, List<TreeSet<String>> listEtats){
		int l = s.size();
		int c = s.get(0).size();
		String[][] r = new String[l][c];
		for(int i = 0; i < l; i++) {
			for(int j = 0; j < c; j++) {
				TreeSet<String> u = s.get(i).get(j);
				if(u.size() == 0) {
					r[i][j] = "P";
				}
				else {
					String st = "";
					int first = 0;
					for (String str : u) {
						if(first == 0) {
							st = str;
							first = 1;
						}
						else {
							st = st+ ","+ str;
						}
					}
					r[i][j]= st;
				}
			}
		}
		//System.out.println(r);
		return r;
	}


	static List<String> convert_TreeSetList_to_IntList(List<TreeSet<String>> listEtats){
		List<String> r = new ArrayList<String>();
		for(TreeSet<String> s : listEtats) {
			String st = "";
			int first = 0;
			for (String str : s) {
				if(first == 0) {
					st = str;
					first = 1;
				}
				else {
					st = st+ ","+ str;
				}
			}
			if(s.size() == 0) {
				st = "P";
			}
			r.add(st);
			//r.add(listEtats.indexOf(s));
		}
		return r;
	}
	
	static List<TreeSet<String>> fermeture_epsilon(Automate AF){
		List<TreeSet<String>> fe = new ArrayList<TreeSet<String>>();
		for(String etat : AF.listEtats) { 
			//TreeSet<Integer> ferm_epsil= new TreeSet<Integer>();
			//HastSet<Integer> ferm_epsil_check = new TreeSet<Integer>();
			if(AF.tabTransition[AF.listEtats.indexOf(etat)][AF.nbrsymbs-1].equals("-")) {
				//System.out.println("Pas de epsilon");
				TreeSet<String> fer_epsi = new TreeSet<String>();
				fer_epsi.add(etat);
				fe.add(fer_epsi);
			}
			else {
				//System.out.println("Epsilon pour " + etat);
				TreeSet<String> check= new TreeSet<String>();
				TreeSet<String> fer_epsi = new TreeSet<String>();
				check.add(etat); // Ajout dans la list des checks
				int counter = AF.nbrEtats + 1;
				while(check.size() != 0 || counter == 0) {
					
					String checking = check.iterator().next();
					//System.out.println("checking : " + checking);
					// Ajout pour savoir si l'�tat a �t� v�rifi� ou non. 
					fer_epsi.add(checking); 
					// Obtient la list des transitions avec comme d�part l'�tat � v�rifier
					List<String[]> listT = AF.transition_epsilon_commancant_par(checking);
					if(listT.size() != 0) {
						for(String[] trans : listT) {

							// Si l'arrivé n'est pas déjà présent dans la fermeture, l'ajoute dans la verification
							if(!fer_epsi.contains(trans[2])) {
								check.add(trans[2]);
							}
						}
					}
					// Fin de la verification
					counter -= 1 ;
					check.remove(checking);
				}
				fe.add(fer_epsi);
			}
		}
		System.out.println(fe.toString());
		return fe;
	}
	
}
