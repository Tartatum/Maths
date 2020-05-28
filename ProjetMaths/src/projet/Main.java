package projet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws IOException {
		boolean prog = true;
		while (prog) {
			String filename = "Automate/L3New-MpI-25-";
			System.out.println("Veuillez entrer le numéro de l'automate :");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String nbrauto = in.readLine();
			filename = filename + nbrauto + ".txt";
			File autoFile = new File(filename);

			if (autoFile.exists()) {
				System.out.println("Le fichier éxiste.");
				Automate original = new Automate(autoFile);
				original.Affichage();
				Automate comp = Complementaire.Comp(original);
				original.info();
				comp.info();
			} else {
				System.out.println("Le fichier n'existe pas");
			}
		}
	}
}
