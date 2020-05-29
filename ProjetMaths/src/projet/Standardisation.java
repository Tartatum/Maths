package projet;

import java.util.ArrayList;

public class Standardisation {
	
	public static void standardisation(Automate a) {
		//test si l'automate est standard
		boolean aucun_retour_sur_entree = false;
		//test de l'entree unique
		if (a.entrees.size() == 1) {
			aucun_retour_sur_entree = true;
			//test des transitions retour vers
			for (int etat = 0; etat < a.nbrEtats; etat++) {
				for (int symbNum = 0; symbNum < a.nbrsymbs; symbNum++) {
					if (a.tabTransition[etat][symbNum] == a.entrees.get(0) + "") {
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
			for (int etat : a.entrees) {
				for (int symbNum = 0; symbNum < a.nbrsymbs; symbNum++) {
					new_arcs.get(symbNum).add(a.tabTransition[etat][symbNum]);
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
				if (new_arcs.get(symbNum).size() > 0) {
					new_arc += new_arcs.get(symbNum).get(0);
					a.listTrans.add(a.nbrEtats + a.listSymbs.get(symbNum) + new_arcs.get(symbNum).get(0));
					a.nbrTrans += 1;
					for(int index = 1; index < new_arcs.get(symbNum).size(); index ++) {
						new_arc += "," + new_arcs.get(symbNum).get(index);
						a.listTrans.add(a.nbrEtats + a.listSymbs.get(symbNum) + new_arcs.get(symbNum).get(index));
						a.nbrTrans += 1;
					}
				}
				
				System.out.println(new_arc + "    " + a.nbrEtats + "     " + symbNum);
				tabTransition[a.nbrEtats][symbNum] = new_arc;
				
			}
			
			a.entrees = new ArrayList<>();
			a.entrees.add(a.nbrEtats + 1);
			a.nbrEtats += 1;
			a.listEtats.add(a.nbrEtats + 1);
			a.tabTransition = tabTransition;
		}
	}

}
