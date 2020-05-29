package projet;

public class Complementaire {

	public static Automate Comp(Automate A) {
		Automate Aret = new Automate(A);
		Aret.sorties = Aret.listEtats;
		for (String removed : A.sorties) {
			Aret.sorties.remove(removed);
		}
		System.out.println();
		return Aret;
	}
}
