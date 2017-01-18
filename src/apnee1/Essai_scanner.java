package apnee1;
import java.io.IOException;
import java.util.*;



class Essai_scanner {
	public static void main(String [] args) throws IOException {
		ChargeurNiveaux.init();
		ChargeurNiveaux.prochainNiveau();


	}


	private static int input(Scanner my_scanner, String string) {
		boolean valid=false;
		int nb=0;
		System.out.println(string);
		while (!valid){
			try{
				nb = my_scanner.nextInt();
				valid = true;
			}catch(InputMismatchException e){
				System.out.println("Entier invalide");
			}catch(NoSuchElementException e){
				System.out.println("Error !!!!!!!!!!");
			}
		}

		return nb;
	}
}
