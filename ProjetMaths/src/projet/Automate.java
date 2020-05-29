package projet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

public class Automate implements Cloneable {
	/*
	 * Contenu de l'automate Le nombre de symboles (nbrsymbs) La liste des symboles
	 * (listSymbs) La liste des entrées (entrees) La liste des sorties (sorties) Le
	 * nombre d'états (nbrEtats) La liste des états (listEtats) Le nombre de
	 * transitions (nbrTrans) La liste des transitions (en chaîne de caractères
	 * "1a2") Le tableau des transitions (
	 */
	public int nbrsymbs;
	public ArrayList<String> listSymbs;
	public List<String> entrees;
	public List<String> sorties;
	public int nbrEtats;
	public List<String> listEtats;
	public int nbrTrans;
	public List<String[]> listTrans;
	public String[][] tabTransition;

	public Automate() {

	}

	public Automate(Automate a) {
		this.nbrsymbs = a.nbrsymbs;
		this.listSymbs = a.listSymbs;
		this.entrees = a.entrees;
		this.sorties = a.sorties;
		this.nbrEtats = a.nbrEtats;
		this.listEtats = a.listEtats;
		this.nbrTrans = a.nbrTrans;
		this.listTrans = a.listTrans;
		this.tabTransition = a.tabTransition;
	}

	public Automate(File autoFile) {
		try {

			System.out.println("---------- Création de l'automate ----------");
			// Symboles
			@SuppressWarnings("resource")

			/*
			 * Lecture du fichier
			 */
			Scanner ligne = new Scanner(autoFile);
			String[] tabsymb = ligne.nextLine().split(" ");
			// System.out.println(tabsymb.length);
			nbrsymbs = tabsymb.length; // le nombre de symboles via la liste

			listSymbs = new ArrayList<String>(); // La liste des symboles
			for (String string : tabsymb) {
				listSymbs.add(string);
			}

			// La liste des états et le nombre d'états
			String[] tabEtats = ligne.nextLine().split(" ");
			listEtats = new ArrayList<String>();
			for (String ent : tabEtats) {
				listEtats.add(ent);
			}
			System.out.println("Rajout de l'état poubelle 'P'");
			listEtats.add("P");
			nbrEtats = tabEtats.length + 1;
			// La liste des entrées
			entrees = new ArrayList<String>();
			String[] tabEnt = ligne.nextLine().split(" ");

			for (String ent : tabEnt) {
				entrees.add(ent);
			}

			// La liste des sorties
			sorties = new ArrayList<String>();
			String[] tabSort = ligne.nextLine().split(" ");

			for (String sort : tabSort) {
				sorties.add(sort);
			}
			for (String string : sorties) {
				System.out.println(string);
			}
			// le nombre de transitions
			nbrTrans = Integer.parseInt(ligne.nextLine());

			tabTransition = new String[nbrEtats][nbrsymbs];
			for (int i = 0; i < nbrEtats; i++) {
				for (int j = 0; j < nbrsymbs; j++) {
					tabTransition[i][j] = "-";
				}
			}

			listTrans = new ArrayList<String[]>();

			for (int i = 1; i <= nbrTrans; i++) {
				String ligneTrans = ligne.nextLine();
				String[] trans = ligneTrans.split("");

				listTrans.add(trans); // la liste des transitions

				if (listSymbs.indexOf(trans[1]) == -1) {

					System.out.println("Apparition d'un symbole inconnu");
					System.out.println("Sortie du programme.");
					return;
				}
				if (tabTransition[Integer.parseInt(trans[0]) - 1][listSymbs.indexOf(trans[1])] == "-") {
					tabTransition[Integer.parseInt(trans[0]) - 1][listSymbs.indexOf(trans[1])] = trans[2];
				} else {
					tabTransition[Integer.parseInt(trans[0]) - 1][listSymbs.indexOf(
							trans[1])] = tabTransition[Integer.parseInt(trans[0]) - 1][listSymbs.indexOf(trans[1])]
									+ "," + trans[2];
				}
			}
			info();

			ligne.close();
		} catch (FileNotFoundException e) {
			System.out.println("Erreur à la création");
			System.exit(0);
		}
	}

	public void Affichage() {
		System.out.println("---------- Affichage de l'automate ----------");
		System.out.print("    ");
		for (String nom : listSymbs) {
			System.out.print(nom + "   ");
		}
		for (int i = 0; i < nbrEtats; i++) {
			System.out.println();
			System.out.print(listEtats.get(i) + " | ");
			for (int j = 0; j < nbrsymbs; j++) {
				if (tabTransition[i][j].equals("-1")) {
					System.out.print("P");
				} else {
					System.out.print(tabTransition[i][j]);
				}
				if (j != nbrsymbs - 1) {
					System.out.print(" | ");
				}
			}

		}

		System.out.println();
		System.out.println();
	}

	@Override
	public String toString() {
		return "Automate [nbrsymbs=" + nbrsymbs + ", listSymbs=" + listSymbs + ", entrees=" + entrees + ", sorties="
				+ sorties + ", nbrEtats=" + nbrEtats + ", nbrTrans=" + nbrTrans + ", tabTransition="
				+ Arrays.toString(tabTransition) + "]";
	}

	public List<String[]> transition_commancant_par(int etat) {
		List<String[]> t = new ArrayList<String[]>();
		for (String[] trans : this.listTrans) {
			if (trans[0].equals(Integer.toString(etat))) {
				// System.out.println(trans[0] +" -> " + trans[1] +" -> "+ trans[2]);
				t.add(trans);
			}
		}
		return t;
	}

	public List<String[]> transition_async_entree_symb(TreeSet<Integer> checking, String string) {
		List<String[]> t = new ArrayList<String[]>();
		for (int i : checking) {
			for (String[] trans : listTrans) {
				if (trans[1].equals(string) && (i == Integer.parseInt(trans[0]))) {
					t.add(trans);
				}
			}
		}
		return t;
	}

	public List<String[]> transition_epsilon_commancant_par(int etat) {
		List<String[]> t = new ArrayList<String[]>();
		for (String[] trans : transition_commancant_par(etat)) {
			if (trans[1].equals("*")) {
				// System.out.println(trans[0] +" -> " + trans[1] +" -> "+ trans[2]);
				t.add(trans);
			}
		}
		return t;
	}

	public boolean contient_entree(TreeSet<Integer> fe) {
		for (String e : entrees) {
			for (int f : fe) {
				// System.out.println(Integer.toString(e)+" == "+ Integer.toString(f) + " = "
				// +(e == f) );
				if (e.equals(f)) {
					return true;
				}
			}
		}
		return false;
	}

	public void info() {
		System.out.println("Information sur l'automate :");
		System.out.println("Le nombre de symboles : " + nbrsymbs);
		System.out.println("La liste des symboles : " + listSymbs);
		System.out.println("La liste des entrées : " + entrees);
		System.out.println("La liste des sorties : " + sorties);
		System.out.println("Le nombre d'états : " + nbrEtats);
		System.out.println("La liste des états : " + listEtats);
		System.out.println("Le nombre de transitions : " + nbrTrans);
		// System.out.println("La liste des transitions : ");
		// for(String[] trans : listTrans) {
		// System.out.println(trans[0] +" -> " + trans[1] +" -> "+ trans[2]);
		// }

	}

	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e) {
			return null;
		}
	}

}