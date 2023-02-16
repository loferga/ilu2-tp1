package villagegaulois;

import java.util.Arrays;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private Marche marche;
	private int nbVillageois = 0;

	public Village(String nom, int nbVillageoisMaximum, int nbEtalMaximum) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		marche = new Marche(nbEtalMaximum);
	}
	
	private static class Marche {
		
		private Etal[] etals;
		
		private Marche(int nbEtals) {
			etals = new Etal[nbEtals];
			for (int i = 0; i<nbEtals; i++)
				etals[i] = new Etal();
		}
		
		private void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			Etal etal = etals[indiceEtal];
			if (etal.isEtalOccupe()) return;
			
			etal.occuperEtal(vendeur, produit, nbProduit);
		}
		
		private int trouverEtalLibre() {
			int i = 0;
			while (i<etals.length && etals[i].isEtalOccupe()) i++;
			return i == etals.length ? -1 : i;
		}
		
		private Etal[] trouverEtals(String produit) {
			Etal[] res = new Etal[etals.length];
			int nbEtals = 0;
			for (Etal etal : etals)
				if (etal.isEtalOccupe() && etal.contientProduit(produit))
					res[nbEtals++] = etal;
			return Arrays.copyOfRange(res, 0, nbEtals);
		}
		
		private Etal trouverVendeur(Gaulois vendeur) {
			Etal res = null;
			for (Etal etal : etals)
				if (etal.isEtalOccupe() && etal.getVendeur().equals(vendeur))
					res = etal;
			return res;
		}
		
		private String afficherMarche() {
			StringBuilder str = new StringBuilder();
			int nbEtalVide = 0;
			for (Etal etal : etals) {
				if (etal.isEtalOccupe())
					str.append(etal.afficherEtal());
				else nbEtalVide++;
			}
			str.append("Il reste " +
					nbEtalVide + " étals non utilisés dans le marché.\n");
			return str.toString();
		}
		
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}
	
	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		StringBuilder str = new StringBuilder();
		str.append(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + " " + produit + ".\n");
		int etalLibre = marche.trouverEtalLibre();
		marche.utiliserEtal(etalLibre, vendeur, produit, nbProduit);
		str.append("Le vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n°" + (etalLibre+1) + ".\n");  // +1 car nous comptons et indexons différement
		return str.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {
		StringBuilder str = new StringBuilder();
		str.append("Les vendeurs qui proposent des " + produit + " sont :\n");
		Etal[] etals = marche.trouverEtals(produit);
		for (Etal etal : etals)
			str.append("- " + etal.getVendeur().getNom() + "\n");
		return str.toString();
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}
	
	public String partirVendeur(Gaulois vendeur) {
		Etal etal = rechercherEtal(vendeur);
		if (etal == null) return vendeur + " n'as pas d'étal dans le marcher.";
		return etal.libererEtal();
	}
	
	public String afficherMarche() {
		StringBuilder str = new StringBuilder();
		str.append("Le marché du Village \"" + nom + "\" possède plusieurs étals :\n");
		str.append(marche.afficherMarche());
		return str.toString();
	}

	public String afficherVillageois() {
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
}