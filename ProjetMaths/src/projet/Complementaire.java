package projet;

public class Complementaire {

	public static Automate Comp(Automate A) {
		Automate Aret = new Automate(A);
		Aret.sorties = Aret.listEtats;
		for (Integer removed : A.sorties) {
			Aret.sorties.remove(removed);
		}
		System.out.println();
		return Aret;
	}
}
