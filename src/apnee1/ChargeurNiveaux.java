package apnee1;

import java.awt.GraphicsEnvironment;
import java.awt.font.GraphicAttribute;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.util.Elements;

public class ChargeurNiveaux {
	private static final String LECTURE_COUCHE = "Lecture_couche";
	private static final String MODE_NONE = "None";
	private static final String MODE_COUCHES = "Couches";
	private static final String MODE_COMPOSANTS = "Composants";
	static final int DEFAULT_START_NIVEAU = 1;
	static ChargeurNiveaux o;
	int numNiveau;

	/* Public interface */
	static void init() {
		ChargeurNiveaux.o = new ChargeurNiveaux(DEFAULT_START_NIVEAU);
	}

	static Niveau prochainNiveau() throws IOException {
		return ChargeurNiveaux.getInstance().getProchainNiveau();
	}

	/* Private methods */
	private Niveau getProchainNiveau() throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(this.getFilename()));
		String line, tempLine;
		String readMod = MODE_NONE;
		int elementCursor = -1, x = 0, y = 0;
		HashMap<Integer, ComposantGraphique> composants = new HashMap<Integer, ComposantGraphique>();
		Niveau niveau = new Niveau("1", 2); // TODO

		while ((line = reader.readLine()) != null) {
			if (line.trim().equals("Composants:")) {
				readMod = MODE_COMPOSANTS;
				elementCursor = 1;
			} else if (line.trim().equals("Couches:")) {
				readMod = MODE_COUCHES;
				elementCursor = 0;
			} else if (line.trim().split(":")[0].equals(String.valueOf(elementCursor))) {
				if (readMod.equals(MODE_COMPOSANTS)){
					composants.put(elementCursor, this.getComposant(reader));
				}
				else if (readMod.equals(MODE_COUCHES)) {
					y = 0;
				}
				elementCursor++;
			} else {
				tempLine = line.trim();
				x = -1;
				for (char c : tempLine.toCharArray()) {
					x++;
					if (c == ' ' || c == '.')
						continue;
					if (c == '#') {
						if (y == 0) {
							niveau.ajouteComposant(elementCursor - 1, (new BordHorizontal(y, -1)).copieVers(x, y));
						} else {
							niveau.ajouteComposant(elementCursor - 1,
									(new BordVertical(y, (x == 0 ? -1 : 1))).copieVers(x, y));
						}
					} else {
						
						niveau.ajouteComposant(elementCursor - 1,
								composants.get(Character.getNumericValue(c)).copieVers(x, y));
					}
				}
				y++;
			}
		}

		this.changeNiveau();
		return niveau;
	}

	private ComposantGraphique getComposant(BufferedReader reader) throws IOException {
		String type, resistance, couleur, nature;
		type = reader.readLine().trim();
		ComposantGraphique composant = null;

		if (type.equals("type: brique")) {
			resistance = reader.readLine().trim().split(": ")[1];
			couleur = reader.readLine().trim().split(": ")[1];
			composant = new Brique(Integer.parseInt(resistance),
					/* Integer.parseInt(couleur) */ 0);
		} else if (type.equals("type: bonus")) {
			nature = reader.readLine().trim().split(": ")[1];
			composant = new Bonus(Bonus.findType(nature));
		}

		System.out.println("" + composant);
		return composant;
	}

	private void changeNiveau() {
		this.numNiveau += 1;
	}

	private static ChargeurNiveaux getInstance() {
		if (ChargeurNiveaux.o == null)
			ChargeurNiveaux.o = new ChargeurNiveaux(DEFAULT_START_NIVEAU);
		return ChargeurNiveaux.o;

	}

	public ChargeurNiveaux(int niveauDebut) {
		ChargeurNiveaux.o = this;
		this.numNiveau = niveauDebut;
	}

	private String getFilename() {
		return "Niveaux/Niveau-" + this.numNiveau + ".txt";
	}

}
