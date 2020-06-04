package projet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class L3New_MpI_25_Main {

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
				
				L3New_MpI_25_Automate original = new L3New_MpI_25_Automate(autoFile);
				original.Affichage();

				L3New_MpI_25_Automate deter = L3New_MpI_25_Determinisation_completion.algorithm(original);
				
				L3New_MpI_25_Automate comp = L3New_MpI_25_Complementaire.Comp(deter);
				System.out.println(" -- Comp -- ");
				comp.info();
				comp.Affichage();
				
				L3New_MpI_25_Automate mini = L3New_MpI_25_Minimalisation.Minimal(deter);
				System.out.println(" -- Mini -- ");
				mini.info();
				mini.Affichage();
				
				L3New_MpI_25_Automate copy = new L3New_MpI_25_Automate(deter);
				System.out.println(" -- Stand -- ");
				L3New_MpI_25_Standardisation.standardisation(copy);
				copy.info();
				copy.Affichage();
				
				System.out.println(" -- Reco deter -- ");
				L3New_MpI_25_ReconnaissanceMot.reconnaissanceMot(deter);
				System.out.println(" -- Reco comp -- ");
				L3New_MpI_25_ReconnaissanceMot.reconnaissanceMot(comp);
			} else {
				System.out.println("Le fichier n'existe pas");
			}
		}
	}
}
