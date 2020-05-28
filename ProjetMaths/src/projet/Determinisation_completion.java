package projet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Determinisation_completion {

	static boolean est_un_automate_asynchrone(Automate AF) {
		System.out.println(" --- l'automate est-il un automate asynchrone ? ---");
		List<String[]> epsilon = new ArrayList<String[]>();
		int async = 0;
		for(String[] trans : AF.listTrans ) {
			if (trans[1].equals("*")) {
				if(async == 0) {
					System.out.println("L'automate est asynchrone, voici les éléments le prouvant :");
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
		//for(int entree : AF.entrees) {
		//	HashSet<Integer> ent = new HashSet<Integer>(AF.entrees);
		//}
		
		Automate AFDC = new Automate();
		return AFDC;
	}
	
	
	static List<HashSet<Integer>> fermeture_epsilon(Automate AF){
		List<HashSet<Integer>> fe = new ArrayList<HashSet<Integer>>();
		for(int etat : AF.listEtats) { 
			//HashSet<Integer> ferm_epsil= new HashSet<Integer>();
			//HastSet<Integer> ferm_epsil_check = new HashSet<Integer>();
			if(AF.tabTransition[etat - 1][AF.nbrsymbs-1].equals("-")) {
				System.out.println("Pas de epsilon");
				HashSet<Integer> fer_epsi = new HashSet<Integer>();
				fer_epsi.add(etat);
				fe.add(fer_epsi);
			}
			else {
				System.out.println("Epsilon pour " + etat);
				HashSet<Integer> check= new HashSet<Integer>();
				HashSet<Integer> fer_epsi = new HashSet<Integer>();
				check.add(etat); // Ajout dans la list des checks
				int counter = AF.nbrEtats + 1;
				while(check.size() != 0 || counter == 0) {
					
					int checking = check.iterator().next();
					System.out.println("checking : " + checking);
					// Ajout pour savoir si l'état a été vérifié ou non. 
					fer_epsi.add(checking); 
					// Obtient la list des transitions avec comme départ l'état à vérifier
					List<String[]> listT = AF.transition_epsilon_commancant_par(checking);
					if(listT.size() != 0) {
						for(String[] trans : listT) {
							// Si l'arrivé n'est pas déjà présent dans la fermeture, l'ajoute dans la verification
							if(!fer_epsi.contains(Integer.parseInt(trans[2]))) {
								check.add(Integer.parseInt(trans[2]));
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
