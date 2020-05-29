package projet;

public class Complementaire {

	static Automate Comp(Automate AF) {
		System.out.println("--------- Entrée de la création d'un automate complémentaire ----------");
		Automate Aret = new Automate(AF);
		Aret.sorties.clear();
		for (String etat : Aret.listEtats) {
			Aret.sorties.add(etat);
		}
		System.out.println("--------- Ajout et suppression des anciennes sorties ----------");
		for (String sortiesOri : AF.sorties) {
			Aret.sorties.remove(sortiesOri);
		}
		System.out.println("--------- Affichage ----------");
		Aret.info();
		System.out.println();
		return Aret;
	}
}
