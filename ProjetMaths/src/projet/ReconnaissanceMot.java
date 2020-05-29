package projet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReconnaissanceMot {
	
	public static void reconnaissanceMot(Automate a) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String entree = "";
		int index = -1;
		System.out.println("Entrez le mot a reconnaitre (fin pour terminer)");
		try {entree = br.readLine();
		} catch (IOException e) {e.printStackTrace();}
		while (!entree.equals("fin")) {
			index = reconnaitre_mot(a,entree);
			if (index == -1) {
				System.out.println("Le mot " + entree + " est reconnu par l'automate");
			} else {
				System.out.println("Le mot " + entree + " n' est pas reconnu par l'automate");
				if (index < entree.toCharArray().length) {
					System.out.println("Le premier caractere empechant le mot d'etre reconnu est :" + entree.toCharArray()[index]);
				} else {
					System.out.println("Le mot n'a pas atteint un état terminal.");
				}
			}
			System.out.println("Entrez le mot a reconnaitre (fin pour terminer)");
			try {entree = br.readLine();
			} catch (IOException e) {e.printStackTrace();}
		}
	}
	
	
	public static int reconnaitre_mot(Automate a, String mot) {
		ArrayList<int[]> indexs = new ArrayList<>();
		//on test pour toutes les entrees
		for (String etat: a.entrees) {
			indexs.add(recursifTest(a,mot.toCharArray(),etat,-1)); 
		}
		int max = -1;
		boolean reconnu = false;
		for (int[] index: indexs) {
			//on garde que la plus grande iteration
			//System.out.println(index[0]);
			//System.out.println(index[1]);
			if (index[0] > max) {
				max = index[0];
			}
			//si le mot a ete entierement reconnu et fini sur un etat de sortie
				
			if (index[0] == mot.length() && a.sorties.contains(a.listEtats.get(index[1]))) {
				reconnu = true;
			}
		}
		if (reconnu) {
			return -1;
		} else {
			return max;
		}
	}
	
	public static int[] recursifTest (Automate a, char[] mot, String etat, int index) {
		index += 1;	
		//si le mot est fini
		if (index == mot.length) {
			//System.out.println("if : " + etat);
			return new int[]{index,a.listEtats.indexOf(etat)};
		}
		String lettre = mot[index] + "";
		//si la lettre est comprise dans l'alphabet
		if (a.listSymbs.indexOf(lettre) != -1) {
			String donnee = a.tabTransition[a.listEtats.indexOf(etat)][a.listSymbs.indexOf(lettre)];
			String[] dests = donnee.split(",");
			//on reapplique sur toutes les destinations possibles
			for (String dest: dests) {
				if(!dest.equals("-")) {
					return recursifTest(a,mot,dest,index++);
				}
			}
		} else {
				//System.out.println("else :" + etat);
				return new int[]{index,a.listEtats.indexOf(etat)};
		}
		return null;
	}

}
