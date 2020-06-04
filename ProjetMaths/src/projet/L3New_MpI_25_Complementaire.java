package projet;

public class L3New_MpI_25_Complementaire {

	static L3New_MpI_25_Automate Comp(L3New_MpI_25_Automate AF) {
		System.out.println("--------- Entrée de la création d'un automate complémentaire ----------");
		L3New_MpI_25_Automate Aret = new L3New_MpI_25_Automate(AF);
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
