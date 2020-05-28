package projet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("unused")

public class Automate {
	public int nbrsymbs;
	public ArrayList<String> listSymbs;
	public List<Integer> entrees;
	public List<Integer> sorties;
	public int nbrEtats;
	public int nbrTrans;
	public String[][] tabTransition;

	public Automate(File autoFile) {
		try {
			System.out.println("---------- Création de l'automate ----------");
			// Symboles
			Scanner ligne = new Scanner(autoFile);
			String[] tabsymb = ligne.nextLine().split(" ");
			// System.out.println(tabsymb.length);
			nbrsymbs = tabsymb.length;
			listSymbs = new ArrayList<String>();
			for (String string : tabsymb) {
				listSymbs.add(string);
			}

			// Nbr Etats
			String[] tabEtats = ligne.nextLine().split(" ");
			nbrEtats = tabEtats.length;

			// List Entrées Sorties
			entrees = new ArrayList<Integer>();
			String[] tabEnt = ligne.nextLine().split(" ");

			for (String ent : tabEnt) {
				entrees.add(Integer.parseInt(ent));
			}
			sorties = new ArrayList<Integer>();
			String[] tabSort = ligne.nextLine().split(" ");

			for (String string : tabSort) {
				for (String sort : tabEnt) {
					sorties.add(Integer.parseInt(sort));
				}
			}

			// Transitions
			nbrTrans = Integer.parseInt(ligne.nextLine());

			tabTransition = new String[nbrEtats][nbrsymbs];
			for (int i = 0; i < nbrEtats; i++) {
				for (int j = 0; j < nbrsymbs; j++) {
					tabTransition[i][j] = "*";
				}
			}
			for (int i = 1; i <= nbrTrans; i++) {
				String[] trans = ligne.nextLine().split("");
				if (listSymbs.indexOf(trans[1]) == -1) {
					System.out.println("Apparition d'un symbole inconnu");
					System.out.println("Sortie du programme.");
					return;
				}
				if (tabTransition[Integer.parseInt(trans[0])][listSymbs.indexOf(trans[1])] == "*")
					tabTransition[Integer.parseInt(trans[0])][listSymbs.indexOf(trans[1])] = trans[2];
				else {
					tabTransition[Integer.parseInt(trans[0])][listSymbs
							.indexOf(trans[1])] = tabTransition[Integer.parseInt(trans[0])][listSymbs.indexOf(trans[1])]
									+ "," + trans[2];
				}
			}

			ligne.close();
		} catch (FileNotFoundException e) {
			System.out.println("Erreur à la création");
			System.exit(0);
		}
	}

	public void Affichage() {
		System.out.println("---------- Affichage de l'automate ----------");
		
		for (int i = 0; i < nbrEtats; i++) {
			System.out.println();
			for (int j = 0; j < nbrsymbs; j++) {
				System.out.print(tabTransition[i][j]);
				if (j != nbrsymbs - 1) {
					System.out.print(" | ");
				}
			}
		}
		System.out.println();
		System.out.println();
	}
}