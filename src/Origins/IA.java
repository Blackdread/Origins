package Origins;
import java.util.ArrayList;

import org.lwjgl.Sys;


/**
 * Classe IA
 * Cr�er une IA basique
 * 
 * @author Yoann CAPLAIN
 * @version 0.5
 */
@SuppressWarnings("unused")
public class IA implements Runnable{
	
	private Game _game;
	
	
	// *************
	// Variable de base de l'IA
	private static int NB_NAIN_MAX = 80;
	
	
	private static int NB_UNIT_ATTAQ_MAX = 200;
	private static int INTERVAL_ATTAQ = 21000; // ms
	// *************
	
	// Une var qui se met automatiquement apres certain temps � false -> Savoir si on est attaquer et gerer �a
	// Une autre pour voir depuis quand on a attaquer l'ennemie, puis preparer une attaque
	// avoir un min de nain, voir pour le milieu
	
	// Si dans un combat, voir les unit�s dans la zone puis transformer ce qui faut

	
	private long lastAttack;
	
	
	// J1
	private ArrayList<Monster> arrayMonsterOwnerInCombat = new ArrayList<Monster>();
	
	
	// J2 (l'ordinateur)
	private ArrayList<Monster> arrayMonsterEnnemyInCombat = new ArrayList<Monster>();	
	
	
	// LES DEUX
	
	
	// Constructeur
	IA(Game game){
		this._game = game;
		lastAttack = getTime();
	}
	
	@Override
	public void run(){
		
	}
	
	private void checkCombat(){
		
	}
	/** Compte les diff�rents combats, combien d'unit� au combat
	 * Mais va surtout compter le nombre d'unit� ennemie dans le combat
	 *  */
	private void checkUnitEngaged(){
		
	}
	
	private void checkUnitDistanceCooldownRun(){
		
	}
	/** V�rifie s'il y a d'autre unit� amis dans un p�rim�tre dans lequel l'unit� se fait attaquer */
	private void checkUnitAllieArround(){
		
	}
	
	
	private void checkUnit(ArrayList<Monster> array){
		
	}
	/** V�rifie le nb de nain, il faut en avoir un minimum */
	private void checkNbNain(){
		
	}
	/** Utilise les zones dans Game zone(x,y) et place les nains */
	private void checkNainPos(){
		
	}
	/** Regarde les unit�s de l'ennemie et agit pour mettre ses nains au milieu */
	private void checkNainStrategy(){
		
	}
	
	/** Verifier s'il est possible d'acheter des nains */
	private void checkAchatNain(){
		while(_game.argentJ2 >= Game.getCOST_NAIN())
			_game.acheterUnit(0,2);
	}
	
	
	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
}
