package Origins;
import java.util.ArrayList;

import org.lwjgl.Sys;


/**
 * Classe IA
 * Créer une IA basique
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
	
	// Une var qui se met automatiquement apres certain temps à false -> Savoir si on est attaquer et gerer ça
	// Une autre pour voir depuis quand on a attaquer l'ennemie, puis preparer une attaque
	// avoir un min de nain, voir pour le milieu
	
	// Si dans un combat, voir les unités dans la zone puis transformer ce qui faut

	
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
	/** Compte les différents combats, combien d'unité au combat
	 * Mais va surtout compter le nombre d'unité ennemie dans le combat
	 *  */
	private void checkUnitEngaged(){
		
	}
	
	private void checkUnitDistanceCooldownRun(){
		
	}
	/** Vérifie s'il y a d'autre unité amis dans un périmètre dans lequel l'unité se fait attaquer */
	private void checkUnitAllieArround(){
		
	}
	
	
	private void checkUnit(ArrayList<Monster> array){
		
	}
	/** Vérifie le nb de nain, il faut en avoir un minimum */
	private void checkNbNain(){
		
	}
	/** Utilise les zones dans Game zone(x,y) et place les nains */
	private void checkNainPos(){
		
	}
	/** Regarde les unités de l'ennemie et agit pour mettre ses nains au milieu */
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
