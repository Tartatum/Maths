package projet;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class L3New_MpI_25_Standardisation {
	
	public static void standardisation(L3New_MpI_25_Automate a) {
		//test si l'automate est standard
		boolean aucun_retour_sur_entree = false;
		//test de l'entree unique
		if (a.entrees.size() == 1) {
			aucun_retour_sur_entree = true;
			//test des transitions retour vers
			for (int etat = 0; etat < a.nbrEtats; etat++) {
				for (int symbNum = 0; symbNum < a.nbrsymbs; symbNum++) {
					if (a.tabTransition[etat][symbNum].equals(a.entrees.get(0))) {
						aucun_retour_sur_entree = false;
					}
				}
			}
		}
		//l'automate est deja standard
		if (aucun_retour_sur_entree) {
			System.out.println("Automate deja standard");
		} else {
			
			String[][] tabTransition = new String[a.nbrEtats+1][a.nbrsymbs];
			ArrayList<ArrayList<String>> new_arcs = new ArrayList<>();
			
			for (int symbNum = 0; symbNum < a.nbrsymbs; symbNum++) {
				new_arcs.add(new ArrayList<>());
			}
			//ajout des arcs partants des anciennes entrees
			for (String etat : a.entrees) {
				for (int symbNum = 0; symbNum < a.nbrsymbs; symbNum++) {
					new_arcs.get(symbNum).add(a.tabTransition[a.listEtats.indexOf(etat)][symbNum]);
				}
			}
			//copie de l'ancien tableau de transition dans le nouveau
			for (int etat = 0; etat < a.nbrEtats; etat++) {
				for (int symbNum = 0; symbNum < a.nbrsymbs; symbNum++) {
					tabTransition[etat][symbNum] = a.tabTransition[etat][symbNum];
				}
			}
			
			//remplissage du tableau pour la ligne de l'etat entrant rajoute
			for (int symbNum = 0; symbNum < a.nbrsymbs; symbNum++) {
				String new_arc = "";
				String[] trans;
				if (new_arcs.get(symbNum).size() > 0) {
					new_arc += new_arcs.get(symbNum).get(0);
					trans = a.splitTrans("i" + a.listSymbs.get(symbNum) + new_arcs.get(symbNum).get(0));
					if(!a.existe_in(trans)){
						a.listTrans.add(a.splitTrans("i"+ a.listSymbs.get(symbNum) + new_arcs.get(symbNum).get(0)));
						a.nbrTrans += 1;
					}
					for(int index = 1; index < new_arcs.get(symbNum).size(); index ++) {
						if(!Arrays.stream(new_arc.split(";")).anyMatch(new_arcs.get(symbNum).get(index)::equals)) {
							new_arc += ";" + new_arcs.get(symbNum).get(index);
						}
						trans = a.splitTrans("i" + a.listSymbs.get(symbNum) + new_arcs.get(symbNum).get(index));
						if(!a.existe_in(trans)){
							a.listTrans.add(a.splitTrans("i"+ a.listSymbs.get(symbNum) + new_arcs.get(symbNum).get(index)));
							a.nbrTrans += 1;
						}
					}
				}
				
				//System.out.println(new_arc + "    " + a.nbrEtats + "     " + symbNum);
				tabTransition[a.nbrEtats][symbNum] = new_arc;
				
			}
//			HashSet<String[]> n = new HashSet<String[]>(a.listTrans);
//			List<String[]> v = new ArrayList<String[]>();
//			for (String[] e : n) {
//				v.add(e);
//			}
//			a.listTrans = v;
//			System.out.println(n.size());
			a.entrees = new ArrayList<>();
			a.entrees.add("i");
			a.nbrEtats += 1;
			a.listEtats.add("i");
			a.tabTransition = tabTransition;
		}
	}

}
