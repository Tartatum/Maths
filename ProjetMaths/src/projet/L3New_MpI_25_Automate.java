package projet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

public class L3New_MpI_25_Automate implements Cloneable {
	/*
	 * Contenu de l'automate Le nombre de symboles (nbrsymbs) La liste des symboles
	 * (listSymbs) La liste des entr�es (entrees) La liste des sorties (sorties) Le
	 * nombre d'�tats (nbrEtats) La liste des �tats (listEtats) Le nombre de
	 * transitions (nbrTrans) La liste des transitions (en cha�ne de caract�res
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

	public L3New_MpI_25_Automate() {

	}

	public L3New_MpI_25_Automate(L3New_MpI_25_Automate a) {
		this.nbrsymbs = a.nbrsymbs;
		listSymbs = new ArrayList<String>();
		for (String string : a.listSymbs) {
			listSymbs.add(string);
		}
		entrees = new ArrayList<String>();
		for (String string : a.entrees) {
			entrees.add(string);
		}
		sorties = new ArrayList<String>();
		for (String string : a.sorties) {
			sorties.add(string);
		}
		this.nbrEtats = a.nbrEtats;
		listEtats = new ArrayList<String>();

		for (String string : a.listEtats) {
			listEtats.add(string);
		}
		this.nbrTrans = a.nbrTrans;
		listTrans = new ArrayList<String[]>();

		for (String[] string : a.listTrans) {
			listTrans.add(string);
		}
		this.tabTransition = a.tabTransition;
	}

	public L3New_MpI_25_Automate(File autoFile) {
		try {

			System.out.println("---------- Cr�ation de l'automate ----------");
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

			// La liste des �tats et le nombre d'�tats
			String[] tabEtats = ligne.nextLine().split(" ");
			listEtats = new ArrayList<String>();
			for (String ent : tabEtats) {
				listEtats.add(ent);
			}
			// System.out.println("Rajout de l'�tat poubelle 'P'");
			// listEtats.add("P");
			nbrEtats = tabEtats.length;
			// La liste des entr�es
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
			// System.out.println("tabTransition : " + tabTransition.length);
			listTrans = new ArrayList<String[]>();

			for (int i = 1; i <= nbrTrans; i++) {
				String ligneTrans = ligne.nextLine();
				String[] trans = splitTrans(ligneTrans);
				// System.out.println(" Trans : " +trans.length);

				listTrans.add(trans); // la liste des transitions

				if (listSymbs.indexOf(trans[1]) == -1) {

					System.out.println("Apparition d'un symbole inconnu");
					System.out.println("Sortie du programme.");
					return;
				}
				// System.out.println(listEtats.indexOf(trans[0]));
				// System.out.println(listSymbs.indexOf(trans[1]));
				// System.out.println(tabTransition.length);
				if (tabTransition[listEtats.indexOf(trans[0])][listSymbs.indexOf(trans[1])] == "-") {
					tabTransition[listEtats.indexOf(trans[0])][listSymbs.indexOf(trans[1])] = trans[2];
				} else {
					tabTransition[listEtats.indexOf(trans[0])][listSymbs.indexOf(
							trans[1])] = tabTransition[listEtats.indexOf(trans[0])][listSymbs.indexOf(trans[1])] + ","
									+ trans[2];
				}
			}
			info();

			ligne.close();
		} catch (FileNotFoundException e) {
			System.out.println("Erreur � la cr�ation");
			System.exit(0);
		}
	}

	public L3New_MpI_25_Automate(int nbrsymbs2, ArrayList<String> listSymbs2, List<String> newentrees, List<String> newsorties,
			int nbrEtats2, List<String> listEtats2, int nbrTrans2, List<String[]> listTrans2,
			String[][] tabTransition2) {
		this.nbrsymbs = nbrsymbs2;
		this.listSymbs = listSymbs2;
		this.entrees = newentrees;
		this.sorties = newsorties;
		this.nbrEtats = nbrEtats2;
		this.listEtats = listEtats2;
		this.nbrTrans = nbrTrans2;
		this.listTrans = listTrans2;
		this.tabTransition = tabTransition2;
	}

	public String[] splitTrans(String ligne) {
		String[] r = new String[3];
		String[] test;
		for (String symb : listSymbs) {
			if (symb.equals("*")) {
				test = ligne.split("\\*");
			} else {
				test = ligne.split(symb);
			}

			if (test.length > 1) {
				r = new String[] { test[0], symb, test[1] };
			}
		}
		return r;
	}

	public void Affichage() {
		System.out.println("---------- Affichage de l'automate ----------");
		String[][] format = new String[nbrEtats + 1][nbrsymbs + 1];

		int max = 0;
		for (int i = 1; i < nbrEtats + 1; i++) {
			if (listEtats.get(i - 1).length() > max) {
				max = listEtats.get(i - 1).length();
			}
			format[i][0] = "\n" + listEtats.get(i - 1);
		}

		for (int i = 1; i < nbrEtats + 1; i++) {
			int diff = max - listEtats.get(i - 1).length();
			for (int k = 0; k < diff; k++) {
				format[i][0] += " ";
			}
		}

		format[0][0] = "\n";
		for (int k = 0; k < max; k++) {
			format[0][0] += " ";
		}

		for (int j = 1; j < nbrsymbs + 1; j++) {
			max = 0;
			for (int i = 1; i < nbrEtats + 1; i++) {
				if (tabTransition[i - 1][j - 1].length() > max) {
					max = tabTransition[i - 1][j - 1].length();
				}
			}
			for (int i = 1; i < nbrEtats + 1; i++) {
				format[i][j] = " | " + tabTransition[i - 1][j - 1];
				if (tabTransition[i - 1][j - 1].length() < max) {
					int diff = max - tabTransition[i - 1][j - 1].length();
					for (int k = 0; k < diff; k++) {
						format[i][j] += " ";
					}
				}
			}
			format[0][j] = " | " + listSymbs.get(j - 1);
			int diff = max - listSymbs.get(j - 1).length();
			for (int k = 0; k < diff; k++) {
				format[0][j] += " ";
			}
		}
		String aff = "";
		for (int i = 0; i < nbrEtats + 1; i++) {
			for (int j = 0; j < nbrsymbs + 1; j++) {
				aff += format[i][j];
			}
		}
		System.out.println(aff);

		System.out.println();
		System.out.println();
	}

	@Override
	public String toString() {
		return "Automate [nbrsymbs=" + nbrsymbs + ", listSymbs=" + listSymbs + ", entrees=" + entrees + ", sorties="
				+ sorties + ", nbrEtats=" + nbrEtats + ", nbrTrans=" + nbrTrans + ", tabTransition="
				+ Arrays.toString(tabTransition) + "]";
	}

	public List<String[]> transition_commancant_par(String etat) {
		List<String[]> t = new ArrayList<String[]>();
		for (String[] trans : this.listTrans) {
			if (trans[0].equals(etat)) {
				// System.out.println(trans[0] +" -> " + trans[1] +" -> "+ trans[2]);
				t.add(trans);
			}
		}
		return t;
	}

	public List<String[]> transition_async_entree_symb(TreeSet<String> checking, String string) {
		List<String[]> t = new ArrayList<String[]>();
		for (String i : checking) {
			for (String[] trans : listTrans) {
				if (trans[1].equals(string) && (i.equals(trans[0]))) {
					t.add(trans);
				}
			}
		}
		return t;
	}

	public List<String[]> transition_epsilon_commancant_par(String etat) {
		List<String[]> t = new ArrayList<String[]>();
		for (String[] trans : transition_commancant_par(etat)) {
			if (trans[1].equals("*")) {
				// System.out.println(trans[0] +" -> " + trans[1] +" -> "+ trans[2]);
				t.add(trans);
			}
		}
		return t;
	}

	public boolean contient_entree(TreeSet<String> fe) {
		for (String e : entrees) {
			for (String f : fe) {
				// System.out.println(Integer.toString(e)+" == "+ Integer.toString(f) + " = "
				// +(e == f) );
				if (e.equals(f)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean existe_in(String[] transTest) {
		for (String[] trans : this.listTrans) {
			if(trans[0].equals(transTest[0].trim()) && trans[1].equals(transTest[1].trim()) && trans[2].equals(transTest[2].trim())) {
				return true;
			}
		}
		return false;
	}

	public void info() {
		System.out.println("Information sur l'automate :");
		System.out.println("Le nombre de symboles : " + nbrsymbs);
		System.out.println("La liste des symboles : " + listSymbs);
		System.out.print("La liste des entr�es : [");
		int first = 1;
		for (String e : entrees) {
			if (first == 1) {
				System.out.print(" {" + e + "}");
				first = 0;
			} else {
				System.out.print(", {" + e + "}");
			}
		}
		first = 1;
		System.out.println(" ]");
		System.out.print("La liste des sorties : [");
		for (String s : sorties) {
			if (first == 1) {
				System.out.print(" {" + s + "}");
				first = 0;
			} else {
				System.out.print(", {" + s + "}");
			}
		}
		System.out.println(" ]");
		first = 1;
		System.out.println("Le nombre d'états : " + nbrEtats);
		System.out.print("La liste des �tats : [");
		for (String l : listEtats) {
			if (first == 1) {
				System.out.print(" {" + l + "}");
				first = 0;
			} else {
				System.out.print(", {" + l + "}");
			}
		}
		System.out.println(" ]");
		System.out.println("Le nombre de transitions : " + nbrTrans);
		System.out.println("La liste des transitions : ");
		for (String[] trans : listTrans) {
			System.out.println(trans[0] + " -> " + trans[1] + " -> " + trans[2]);
		}

	}

	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e) {
			return null;
		}
	}

}
