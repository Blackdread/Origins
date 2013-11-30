package Origins;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import org.lwjgl.Sys;

/**
 * Classe du jeu, IA, Kill, etc
 * 
 * Cette classe aurait pu être totalement abstraite, j'aurai ainsi pu redéfinir les fonctions pour le mode multi et solo. Mais vu que j'ai déjà tout fait, j'ai pas envie de le faire et
 * ça ne me permettrait pas vraiment de gagner en efficacité (je le ferai plus tard quand tout le reste sera fait)
 * 
 * @author Yoann CAPLAIN
 * @version 1.5
 */
public class Game implements Runnable{

	private Thread _t;
	private boolean gameContinue=true;
	
	public int joueur;	// Pour que le joueur 2 ne tue pas les unités mortes. Le joueur 1 lui envoie ces info
	private ClientReceiver _clientReceiver;	// Sera utile pour le joueur 1
	
	private boolean gameSolo;	// fait pour différencier une partie solo ou multi. Et m'éviter de devoir faire un GameSolo.java et d'enlever tout ce qui nécessaire pour le multi. Et aussi, 
	// j'ai dans Lancement.java un Game de déclarer et non pas un GameSolo, et il faudrait que je redéfinisse les fonctions, ou alors je fais un GameSolo qui hérite de Game ou faire du polymorphisme en
	// créant une class abstraite avec Game et GameSolo en héritage
	public ArrayList<String> arrayServerTextInGameChatSolo = new ArrayList<String>();
	public ArrayList<Long> arrayServerTextInGameChatTimeSolo = new ArrayList<Long>();
	public String objectifs;	// Pour afficher les objectifs du mod solo, changement de ligne fait par "-"
	
	
	public ArrayList<String> arrayServerTextInGameError = new ArrayList<String>();	// Avertissement milieu gauche. achat ou transf impossible, ou Vous êtes attaqué, etc
	public ArrayList<Long> arrayServerTextInGameTimeError = new ArrayList<Long>();
	
	private int compteurSolo = 0;
	private int modSoloDiff=0;	// Permettre de mettre des mods pour le solo -> Défis et gérer la fin des tutos et défis. Donc 1 à 5 tutos, apres défis
	private Lancement _lancement; // Utiliser seulement pour le solo car ClientReceiver == null  . Fait le 18 avril
	
	private boolean gameRanked;	// Permet d'envoyer ou pas la partie sur le site		Différencier partie multi avec juste pseudo et partie multi avec pseudo et mdp
	
	int pointsJ1=0, pointsJ2=0, argentJ1=10, argentJ2=10;		// Dans une partie
	int argentloop = 0, manaloop = 0;			// argent reçu toutes les ... secondes (voir la fonction)
	
	int minTimer=0,secTimer=0;
	
	
	// FIN DE PARTIE
	int argentTotalJ1=0, argentTotalJ2=0, unitTotalCreerJ1=0, unitTotalCreerJ2=0, unitTotalTuerJ1=0, unitTotalTuerJ2=0;
	
	// Possible que ce boolean soit dans Lancement.java donc il y aura directement une fonction pour le mvt attaque
	//boolean moveAttackSav = false; // Sauvegarde de l'appuie de la touche attaquer
	private boolean pause = false;
	
	
	// Mettre en final ceux qui ne changent pas plus tard
	public static int TIME_MAX_AVANT_SPAWN = 8;	// Temps maxi avant spawn unité acheter obligatoirement
	public static int MAX_UNIT_SELECTED = 30;
	
	private static int SPAWN_OWNER_X = 150;
	private static int SPAWN_OWNER_Y = 350;
	private static int SPAWN_ENNEMY_X = 2600;
	private static int SPAWN_ENNEMY_Y = 2100;
	
	private static int COST_NAIN = 15;
	private static int COST_FANTASSIN = 17;
	private static int COST_MINAUTAURE = 22;
	private static int COST_ARCHER = 17;
	private static int COST_CENTAURE = 24;
	private static int COST_MAGE = 30;
	
	private static int DIST_ATTAQ_CORPS = 20;
	private static int DIST_ATTAQ_DIST = 150;
	
	private static int MAX_MIN_PARTIE = 40;	// 40 minutes de jeu maxi -> en plus le temps n'est pas vraiment réel
	
	long lastClick;
	/** time at last frame */
	long lastFrame;
 
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;
	
	long lastUpdate;
	
	//		JOUEUR 1
	ArrayList<Monster> arrayMonsterOwner = new ArrayList<Monster>();		// Contient les objets Monsters du joueur	
	ArrayList<Integer> arrayMonsterOwnerBought = new ArrayList<Integer>();	// Unités acheter mais non placer encore et on a un maximun de 8 sec pour les placer, sinon c direct au camp
	ArrayList<Long> arrayMonsterOwnerBoughtTime = new ArrayList<Long>();	// Stocke le temps de l'achat
		
	ArrayList<Tire> arrayMonsterOwnerTire = new ArrayList<Tire>();			// Tire mage J1
	 
	//		JOUEUR 2
	ArrayList<Monster> arrayMonsterEnnemy = new ArrayList<Monster>();	
	ArrayList<Integer> arrayMonsterEnnemyBought = new ArrayList<Integer>();		// Aurait pu etre fait dans arrayMonsterOwnerBought car (pour le moment) je ne fais pas de vérification de l'achat, si sa change alors mettre if(joueur==) dans afficherListeUnitAcheter()
	ArrayList<Long> arrayMonsterEnnemyBoughtTime = new ArrayList<Long>();
	
	ArrayList<Tire> arrayMonsterEnnemyTire = new ArrayList<Tire>();			// Tire mage J2
	 
	 //		LES DEUX
	ArrayList<Tire> arrayTireFleche = new ArrayList<Tire>();				// Liste des FLECHES
	ArrayList<Monster> arrayMonsterOwnerSelected = new ArrayList<Monster>();// SOIT ICI SOIT DANS LANCEMENT;
	ArrayList<Monster> arrayMonsterEnnemySelected = new ArrayList<Monster>();// SOIT ICI SOIT DANS LANCEMENT, C'est pour selectionner une unité de l'ennemie

	ArrayList<Monster> arrayMonsterOwnerGroup1 = new ArrayList<Monster>();	// Groupe de selection
	ArrayList<Monster> arrayMonsterOwnerGroup2 = new ArrayList<Monster>();
	ArrayList<Monster> arrayMonsterOwnerGroup3 = new ArrayList<Monster>();
	ArrayList<Monster> arrayMonsterOwnerGroup4 = new ArrayList<Monster>();
	ArrayList<Monster> arrayMonsterOwnerGroup5 = new ArrayList<Monster>();
	ArrayList<Monster> arrayMonsterOwnerGroup6 = new ArrayList<Monster>();
	ArrayList<Monster> arrayMonsterOwnerGroup7 = new ArrayList<Monster>();
	ArrayList<Monster> arrayMonsterOwnerGroup8 = new ArrayList<Monster>();
	ArrayList<Monster> arrayMonsterOwnerGroup9 = new ArrayList<Monster>();
	
	// Les sons, permettra de jouer les sons dans Lancement
	ArrayList<Integer> arraySons = new ArrayList<Integer>();
	
	
	// Ajouter le 14 avril
	ArrayList<TacheSang> arrayTacheSang = new ArrayList<TacheSang>();
	boolean sangActive = false;	// activer le sang, utiliser dans lancement pour afficher ou pas. Et ajout dans l'array ou pas.  Ajout fait dans attaquerUnit()
	
	//***************************
	// Variable pour vérifier la mort d'une unite
	int loopDeath=0;
	/* Variable propre à J2. Car il enlève la vie des unités mais ne les tue pas, sauf qu'il ne gère plus si c <=0 donc faut vérifier s'il est mort chez J1 ou a été "oublier"
	 Et cette vérification n'est faite que à certain temps
	 Utiliser sendCheckDeath(String)
	*/
	
	/**	Iniate the game */
	Game(){
		gameContinue=true;
		pause=false;
		minTimer=0;
		secTimer=0;
		getDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer
		lastClick = getTime();
		lastUpdate = getTime();
		
		gameSolo=false;
		
		if(arrayMonsterOwner!=null)
			arrayMonsterOwner.clear();
		if(arrayMonsterEnnemy!=null)
			arrayMonsterEnnemy.clear();
		
		for(int i=0;i<5;i++){
			arrayMonsterOwner.add(new Monster(100+40*i,150));
			arrayMonsterEnnemy.add(new Monster(SPAWN_ENNEMY_X+40*i,2100));
		}
		Monster.nbreNain=5;
		Monster.nbreUnite=5;
		
		_t = new Thread(this);
	    _t.start();	
	}
	
	// ***********
	// Partie SOLO
	// ***********
	Game(int partie, Lancement lancement){
		gameContinue=true;
		pause=false;
		minTimer=0;
		secTimer=0;
		getDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer
		lastClick = getTime();
		lastUpdate = getTime();
		
		gameSolo=true;
		
		if(arrayMonsterOwner!=null)
			arrayMonsterOwner.clear();
		if(arrayMonsterEnnemy!=null)
			arrayMonsterEnnemy.clear();
		if(pointsJ1!=0)
			pointsJ1=0;
		if(pointsJ2!=0)
			pointsJ2=0;
		
		_lancement = lancement;
		
		// Switch des parties
		siwtchDesPartie(partie);	// pour éviter d'avoir un constructeur super long
		modSoloDiff=partie;
		
		_t = new Thread(this);
	    _t.start();	
	}
	
	@Override
	public void run() {
		
		while(gameContinue){
			
			if(getTime() - lastUpdate >= 10){
				while(pause){
				}
				try{	// Sert surtout pour le for (moveMonster, etc)
				augmenterArgent();
				changeTimer();
				
				if(joueur==1)	// Modifier pour que le serveur soit le seul à gérer ça, le serveur envoie les infos au joueur 2
					enleverUnitDead();
				enleverTireFiniEtPerdu();
				
				// Modifier pour que le serveur soit le seul à gérer ça
				// En faite, le joueur 2 envoie un message au serveur pour lui dire que J2 veut spawn une unité
				//if(joueur==1)
					spawnUnitTimeElasped();	// Pour le moment, le spawn de J2 n'est pas fait mais envoie un message au serveur et serveur spawn l'unit si J2 a assez d'argent
				
				
				 for(Monster v : arrayMonsterOwner)	// Pour Owner
						if(v != null){
							if(v.getVie() > 0){
								v.moveMonster();
								augmenterMana(v);
								IAcheck(v, 1);			// Gestion pour les combats (dist et corps) peut-etre de colision (à voir)
							}
						}
				}catch(ConcurrentModificationException e){}
				 try{	// Sert surtout pour le for (moveMonster, etc)
				 for(Monster v : arrayMonsterEnnemy)	// Pour ennemie
						if(v != null){
							if(v.getVie() > 0){
								v.moveMonster();
								augmenterMana(v);
								IAcheck(v, 2);			// Gestion pour les combats (dist et corps) peut-etre de colision (à voir)
								// Voir si le joueur 2 gere automatiquement le deplacement des unites dans IAcheck, car J1 lui envoie les info pour ce cas là aussi
							}
						}
				 
				 
				 faireSuivreTireUnit();	// Que J1 tue les unités dans cette fonction, info envoyé à J2
				 faireAvancerFleche();
				 
				 checkManaLoop();
				 checkLoopDeath();
				 
				 // Les modes en Solo
				 if(gameSolo)
					 gererDifferentModeSolo();
				 
				 // FIN GAME
				 isPartieFinie();
				 
				 }catch(ConcurrentModificationException e){debPhrs("Error Concurrent Modif");}
				 lastUpdate=getTime();
			}
			 
		}
	}
	/**	Check money loop and increase (depends on dwarf) for both player, then reset loop */
	private void augmenterArgent(){
    	int nbNain=0;
    	if(argentloop>=500){	// 5 sec
    		// 	****************************************************************************************************
    		for(Monster o : arrayMonsterOwner)
    			if(o!= null)
    				if(o.getMetamor() == 0){
    					nbNain+=nainBonnePos(o,1);
    					//System.out.println(nbNain);
    					//nbNain++;
    				}    					
    		argentJ1+=nbNain;
    		argentTotalJ1+=nbNain;
    				
    		nbNain=0;
    		for(Monster o : arrayMonsterEnnemy)
    			if(o!= null)
    				if(o.getMetamor() == 0){
    					nbNain+=nainBonnePos(o,2);
    					//System.out.println(nbNain);
    					//nbNain++;
    				}
    		argentJ2+=nbNain;
    		argentTotalJ2+=nbNain;
    				
    		argentloop=0;
    		
    		ajouterTexteError("");	// Pour enlever les msg dépasser
    		
    		// MIS CAR DES FLECHES SE "PERDENT"
			// C pas gênant car cet array est juste pour afficher les fleches, les dmg sont immédiats
    		arrayTireFleche.clear();
    		
    	}else
    		argentloop++;
    }
	/**Increase mana on max loop*/
	private void augmenterMana(Monster v){
    	if(manaloop>=400)
    		v.augmenteMana();
    }
    
    private void spawnUnitTimeElasped(){
    	String message="J1";
    	int diff,k,l=0;
    	for(k=0; k < arrayMonsterOwnerBought.size() - l;k++){
    		if(!arrayMonsterOwnerBoughtTime.isEmpty()){
    			diff = (int)( (arrayMonsterOwnerBoughtTime.get(k) - getTime() ) / 1000)+TIME_MAX_AVANT_SPAWN;
    			if(diff<=0){
    				spawnUnit(1);
    				// Envoyer info au J2	// spawn-id-xDest-yDest-IntMetamorphose-moveAttack:
    				// POUR LE MOMENT, JE SUPPOSE QUE L"UNITE A ETE CREE ET EST LA DERNIERE DANS LA LISTE DU OWNER
    				if(arrayMonsterOwner.size()>=1)
    					message+=("spawn-"+arrayMonsterOwner.get(arrayMonsterOwner.size()-1).getId()+"-"+arrayMonsterOwner.get(arrayMonsterOwner.size()-1).getDestX()+"-"+arrayMonsterOwner.get(arrayMonsterOwner.size()-1).getDestY()+"-"+arrayMonsterOwner.get(arrayMonsterOwner.size()-1).getMetamor()+"-true"+":");
    				arrayMonsterOwnerBought.remove(0);
    				arrayMonsterOwnerBoughtTime.remove(0);
    				unitCree();
    				l++;
    			}else	// Les autres derrières ne peuvent pas etre inférieure si le 1er ne l'est pas    				
    			break;
    		}
    	}
    	if(!message.equalsIgnoreCase("J1") && !message.equalsIgnoreCase(""))	// Ici y a que J1 donc on envoie l'info du spawn d'une unite
    		if(_clientReceiver!=null)
    			_clientReceiver.sendAnyMsg("all"+message);
    	
    	message="J2";
    	// Pour ennemie
    	l=0;
    	for(k=0; k < arrayMonsterEnnemyBought.size() - l;k++){
    		if(!arrayMonsterEnnemyBoughtTime.isEmpty()){
    			diff = (int)( (arrayMonsterEnnemyBoughtTime.get(k) - getTime() ) / 1000)+TIME_MAX_AVANT_SPAWN;
    			if(diff<=0){
    				//spawnUnit(2);
    				// askSpawnM-id-xDest-yDest-IntMetamorphose-moveAttack:		L'id est inutile car c'est le serveur qui les assignes
    				message+=("askSpawnM-"+0+"-"+(SPAWN_ENNEMY_X+(int)(-100+Math.random()*200))+"-"+(SPAWN_ENNEMY_Y+(int)(-100+Math.random()*200))+"-"+arrayMonsterEnnemyBought.get(0)+"-true"+":");	
    				arrayMonsterEnnemyBought.remove(0);
    				arrayMonsterEnnemyBoughtTime.remove(0);
    				l++;
    			}else	// Les autres derrières ne peuvent pas etre inférieure si le 1er ne l'est pas  				
    			break;
    		}
    	}
    	if(!message.equalsIgnoreCase("J2") && !message.equalsIgnoreCase(""))	// Que pour J2 et ainsi demande au serveur la création d'une unité à lui
    		if(_clientReceiver!=null)
    			_clientReceiver.sendAnyMsg(""+message);	// Gérer dans Client (donc pas de "all")
    }
    
    
    public void spawnUnit(int joueurSpawn){
    	int xx, yy;
    	if(joueurSpawn == 1){	// Y aura un batiment à mettre pour les deux joueurs
    		unitTotalCreerJ1++;
    		xx =SPAWN_OWNER_X;
    		yy = SPAWN_OWNER_Y;
    		arrayMonsterOwner.add(new Monster(xx,yy,arrayMonsterOwnerBought.get(0),SPAWN_OWNER_X+(int)(-100+Math.random()*200),SPAWN_OWNER_Y+(int)(-100+Math.random()*200),true)); 
    	}else{
    		unitTotalCreerJ2++;
    		xx =SPAWN_ENNEMY_X;
    		yy = SPAWN_ENNEMY_Y;
    		arrayMonsterEnnemy.add(new Monster(xx,yy,arrayMonsterEnnemyBought.get(0),SPAWN_ENNEMY_X+(int)(-100+Math.random()*200),SPAWN_ENNEMY_Y+(int)(-100+Math.random()*200),true));
    		// Peut-etre mettre ici le decrement pour le tye d'unit acheté et le total car les var sont justes pour le J1. Le J2 fera des boucles pour savoir combien il en a
    		switch(arrayMonsterEnnemyBought.get(0)){
    		case 0:
    			Monster.nbreNain--;
    			break;
    		case 1:
    			Monster.nbreFantassin--;
    			break;
    		case 2:
    			Monster.nbreMinautaure--;
    			break;
    		case 3:
    			Monster.nbreArcher--;
    			break;
    		case 4:
    			Monster.nbreCentaure--;
    			break;
    		case 5:
    			Monster.nbreMage--;
    			break;
    		}
    		Monster.nbreUnite--;
    	}
    			
    }
  
    public void spawnUnit(int joueurSpawn, int emplacement, int xDest, int yDest, boolean moveAttack){
    	int xx, yy;
    	if(joueurSpawn == 1){
    		if(joueur == 1)
    			unitCree();
    		unitTotalCreerJ1++;
    		xx = SPAWN_OWNER_X;
    		yy = SPAWN_OWNER_Y;
    		if(!moveAttack)
    			//arrayMonsterOwner.add(new Monster(xx,yy,arrayMonsterOwnerBought.get(emplacement),xDest+deltaX,yDest-deltaY));
    			arrayMonsterOwner.add(new Monster(xx,yy,arrayMonsterOwnerBought.get(emplacement),xDest,yDest));			// *********  Delta deja dans X et Y  ***********
    		else
    			//arrayMonsterOwner.add(new Monster(xx,yy,arrayMonsterOwnerBought.get(emplacement),xDest+deltaX,yDest-deltaY,true));
    			arrayMonsterOwner.add(new Monster(xx,yy,arrayMonsterOwnerBought.get(emplacement),xDest,yDest,true));	// *********  Delta deja dans X et Y  ***********
    	}else{
    		if(joueur == 2)
    			unitCree();
    		unitTotalCreerJ2++;
    		xx = SPAWN_ENNEMY_X;
    		yy = SPAWN_ENNEMY_Y;
    		if(!moveAttack)
    			//arrayMonsterEnnemy.add(new Monster(xx,yy,arrayMonsterEnnemyBought.get(emplacement),xDest+deltaX,yDest-deltaY));
    			arrayMonsterEnnemy.add(new Monster(xx,yy,arrayMonsterEnnemyBought.get(emplacement),xDest,yDest));		// *********  Delta deja dans X et Y  ***********
    		else
    			//arrayMonsterEnnemy.add(new Monster(xx,yy,arrayMonsterEnnemyBought.get(emplacement),xDest+deltaX,yDest-deltaY,true));
    			arrayMonsterEnnemy.add(new Monster(xx,yy,arrayMonsterEnnemyBought.get(emplacement),xDest,yDest,true));	// *********  Delta deja dans X et Y  ***********
    		switch(arrayMonsterEnnemyBought.get(emplacement)){
    		case 0:
    			Monster.nbreNain--;
    			break;
    		case 1:
    			Monster.nbreFantassin--;
    			break;
    		case 2:
    			Monster.nbreMinautaure--;
    			break;
    		case 3:
    			Monster.nbreArcher--;
    			break;
    		case 4:
    			Monster.nbreCentaure--;
    			break;
    		case 5:
    			Monster.nbreMage--;
    			break;
    		}
    		Monster.nbreUnite--;
    	}
    }
    /** Methode créé principalement pour J2 pour ce qu'il reçoit de ClientReceiver */
    public void spawnUnit(int joueurSpawn, int joueur,int id2, int xDest, int yDest, int metamor, String moveAttack){
    	int xx, yy;
    	boolean moveA;
    	if(moveAttack.equalsIgnoreCase("true"))
    		moveA=(true);
		else
			moveA=(false);
    		    	
    	if(joueurSpawn == 1){
    		if(joueur == 1)
    			unitCree();
    		unitTotalCreerJ1++;
    		xx = SPAWN_OWNER_X;
    		yy = SPAWN_OWNER_Y;
    			arrayMonsterOwner.add(new Monster(xx,yy,id2,metamor,xDest,yDest,moveA));	// *********  Delta deja dans X et Y  ***********
    	}else{
    		if(joueur == 2)
    			unitCree();
    		unitTotalCreerJ2++;
    		xx = SPAWN_ENNEMY_X;
    		yy = SPAWN_ENNEMY_Y;
    			arrayMonsterEnnemy.add(new Monster(xx,yy,id2,metamor,xDest,yDest,moveA));	// *********  Delta deja dans X et Y  ***********
    	}
    	if(joueurSpawn != joueur){
	    	switch(metamor){
    		case 0:
    			Monster.nbreNain--;
    			break;
    		case 1:
    			Monster.nbreFantassin--;
    			break;
    		case 2:
    			Monster.nbreMinautaure--;
    			break;
    		case 3:
    			Monster.nbreArcher--;
    			break;
    		case 4:
    			Monster.nbreCentaure--;
    			break;
    		case 5:
    			Monster.nbreMage--;
    			break;
	    	}
    		Monster.nbreUnite--;
    	}
    }
    
    /**	Check attack range, attack if possible else move monster to ennemy	 */
    private void IAcheck(Monster v, int differencierJoueur){
    	int i=0, iUtile=-1;
    	int diff=100000;
    	int delta2Unit;
    	// Gérer moveAttack, cooldown entre chaque attaque, 
    	// ennemie à côté (d'abord le cooldown car moins de loop), deltaX et deltaY == 0 || moveAttack == true
    	// etc
    	// Sauvegarder l'ennemie le plus pret de l'unité en cours de check s'il peut attaquer, voir si hors limite de vision, sinon bouger ou attquer si à porté
    	// Mettre un i increment pour avoir l'id du monstre sur lequel infliger des dégats
    	if(differencierJoueur == 1){
    		String message="J1";	// deplace-id-destX-destY-metamor-moveAttack
	    	if(CooldownOk(v)){
	    		if(v.getVie() > 0 && ((v.getDeltaX() == 0 && v.getDeltaY() == 0) || v.getMoveAttack() == true)){
	    				for(Monster w : arrayMonsterEnnemy){
	    					if(w.getVie() > 0){		// Ainsi les unités attaques seulement si elle a encore de la vie sinon il cherche une autre cible
	    						// et sa m'évite de devoir enlever les unités mortes dans la fonction attaquerUnit()
		    					delta2Unit = (int)Math.sqrt((Math.pow(Math.abs(w.getX() - v.getX()), 2)+ Math.pow(Math.abs(w.getY() - v.getY()), 2)));
		    					//debPhrs("diff : "+diff+" i :"+i+" vie :"+w.getVie());
		    					if(delta2Unit < DIST_ATTAQ_DIST )	// Champs d'attaque à distance et vision des unités
		    						 if(delta2Unit <= diff){
		    							 diff = delta2Unit;
		    							 iUtile = i;
		    						 }
		    					
		    					i++;
	    					}
	    				}
	    		}
	    		i--;	// Dû au fait que i est increment une fois de trop
	    	}
	    	
	    	if(diff != 100000 && iUtile != -1){
	    		if(v.getMetamor() == 3 || v.getMetamor() == 4 || v.getMetamor() == 5){		
	    			// Voir si je fais comme pour les unite de corps à corps -> ils s'arretent de 
	    			// bouger s'il rencontre une unité à distance d'attaque et sont en mode moveAttack true
	    			
					// Unités de distance et zone d'attaque, le 1er vu est attaqué
					attaquerUnit(v,arrayMonsterEnnemy.get(iUtile),1);
					v.setLastAttack(getTime());
					//debPhrs("dist attaque");	
				}else{
	    			// Unités de corps à corps, mais pas zone d'attaque
					if(Math.sqrt((Math.pow(arrayMonsterEnnemy.get(iUtile).getX() - v.getX(), 2)+ Math.pow(arrayMonsterEnnemy.get(iUtile).getY() - v.getY(), 2))) < DIST_ATTAQ_CORPS){
						attaquerUnit(v,arrayMonsterEnnemy.get(iUtile),1);
						v.setLastAttack(getTime());
						//debPhrs("corps attaque");
					}else{
						if(!v.getTenirPos()){
							v.changeDestination((int)arrayMonsterEnnemy.get(iUtile).getX(), (int)arrayMonsterEnnemy.get(iUtile).getY());
							message+="deplace-"+v.getId()+"-"+v.getDestX()+"-"+v.getDestY()+"-"+v.getMetamor()+"-"+v.getMoveAttack()+":";
							//debPhrs("corps changeDest");
						}
					}
				}
			}
	    	
	    	if(!message.equalsIgnoreCase("J1") && !message.equalsIgnoreCase("") && joueur==1)
	    		if(_clientReceiver!=null)
	    			_clientReceiver.sendAnyMsg("all"+message);
    	}else{
    		String message="J2";	// deplace-id-destX-destY-metamor-moveAttack
    		if(CooldownOk(v)){
	    		if(v.getVie() > 0 && ((v.getDeltaX() == 0 && v.getDeltaY() == 0) || v.getMoveAttack() == true)){
	    				for(Monster w : arrayMonsterOwner){
	    					if(w.getVie() > 0){		// Ainsi les unités attaques seulement si elle a encore de la vie sinon il cherche une autre cible
	    						// et sa m'évite de devoir enlever les unités mortes dans la fonction attaquerUnit()
		    					delta2Unit = (int)Math.sqrt((Math.pow(Math.abs(w.getX() - v.getX()), 2)+ Math.pow(Math.abs(w.getY() - v.getY()), 2)));
		    					//debPhrs("diff : "+diff+" i :"+i+" vie :"+w.getVie());
		    					if(delta2Unit < DIST_ATTAQ_DIST )	// Champs d'attaque à distance et vision des unités
		    						 if(delta2Unit <= diff){
		    							 diff = delta2Unit;
		    							 iUtile = i;
		    						 }
		    					
		    					i++;
	    					}
	    				}
	    		}
	    		i--;	// Dû au fait que i est increment une fois de trop
	    	}
	    	
	    	if(diff != 100000 && iUtile != -1){
	    		if(v.getMetamor() == 3 || v.getMetamor() == 4 || v.getMetamor() == 5){		
	    			// Voir si je fais comme pour les unite de corps à corps -> ils s'arretent de 
	    			// bouger s'il rencontre une unité à distance d'attaque et sont en mode moveAttack true
	    			
					// Unités de distance et zone d'attaque, le 1er vu est attaqué
					attaquerUnit(v,arrayMonsterOwner.get(iUtile),2);
					v.setLastAttack(getTime());
					//debPhrs("dist attaque");	
				}else{
	    			// Unités de corps à corps, mais pas zone d'attaque
					if(Math.sqrt((Math.pow(arrayMonsterOwner.get(iUtile).getX() - v.getX(), 2)+ Math.pow(arrayMonsterOwner.get(iUtile).getY() - v.getY(), 2))) < DIST_ATTAQ_CORPS){
						attaquerUnit(v,arrayMonsterOwner.get(iUtile),2);
						v.setLastAttack(getTime());
						//debPhrs("corps attaque");
					}else{
						if(!v.getTenirPos()){
							v.changeDestination((int)arrayMonsterOwner.get(iUtile).getX(), (int)arrayMonsterOwner.get(iUtile).getY());
							message+="deplace-"+v.getId()+"-"+v.getDestX()+"-"+v.getDestY()+"-"+v.getMetamor()+"-"+v.getMoveAttack()+":";
							//debPhrs("corps changeDest");
						}
					}
				}
			}
	    	if(!message.equalsIgnoreCase("J2") && !message.equalsIgnoreCase("") && joueur==1)
	    		if(_clientReceiver!=null)
	    			_clientReceiver.sendAnyMsg("all"+message);
    	}
    }
    /**
	 *	Cooldown depends on metamorphose, 5 -> longest
	 *	Used in Lancement
	 * @return Unit able or not to attack
	 */
    public boolean CooldownOk(Monster v){
    	long time = getTime();
    	switch(v.getMetamor()){
    	case 0:
    		if(time - v.getLastAttack() >= 1200)	// 1.2 sec
    			return true;
    		break;
    	case 1:
    		if(time - v.getLastAttack() >= 1600)
    			return true;
    		break;
    	case 2:
    		if(time - v.getLastAttack() >= 1900)
    			return true;
    		break;
    	case 3:
    		if(time - v.getLastAttack() >= 1700)
    			return true;
    		break;
    	case 4:
    		if(time - v.getLastAttack() >= 1500)
    			return true;
    		break;
    	case 5:
    		if(time - v.getLastAttack() >= 2000)
    			return true;
    		break;
    		
    	}
    	return false;	// S'il peut pas attaquer
    }
    /** Set damage on units depending on resistance and weakness, and create Tire. Ajoute des int dans un array pour jouer le son de l'attaque. Ajoute un int dans un array pour le sang */
    private void attaquerUnit(Monster v, Monster w, int differencierJoueur){
		/*
		 * 	Metamor = 0 -> Unités de base		 			deplacement moyen 		nain
		 *	Metamor = 1 -> Unités de corps à corps			deplacement rapide 		fantasin
		 *	Metamor = 2 -> Unités de corp à corps avancée	deplacement rapide 		minotaure
		 * 	Metamor = 3 -> Unités de distance faible		deplacement lent		archer 		
		 * 	Metamor = 4 -> Unités de de distance avancée	deplacement moyen 		centaure
		 *  Metamor = 5 -> Unités de distance de zone		deplacement lent		mage 
		 */
    	// Les dmg peuvent etre augmenter en fonction du nb de kill de l'unite

    	boolean contient=false;
    	//if(joueur != differencierJoueur)	Marche que si J2 attaque J1. Faudrait en gros mettre ça dans IAcheck
    		for(String l : arrayServerTextInGameError)
    			if(l.equalsIgnoreCase("Vous êtes attaqué !")){
    				contient=true;
    				break;
    			}		
    	if(!contient)
    		ajouterTexteError("Vous êtes attaqué !");
    	
    	if(sangActive)
    		try{
    			arrayTacheSang.add(new TacheSang((int)w.getX(), (int)w.getY()));
    		}catch(Exception e){}
    	
    	try{
    		int deltaXcopy=0;
    		int deltaYcopy=0;
    		boolean effetON=true;
    		if(_clientReceiver != null){
    			if(_clientReceiver.getLancement() != null){
    			deltaXcopy = _clientReceiver.getLancement().deltaX;
    			deltaYcopy = _clientReceiver.getLancement().deltaY;
    			effetON = _clientReceiver.getLancement().getEffetON();
    			}
    		}
    		if(gameSolo){
    			if(_lancement != null){
    				deltaXcopy = _lancement.deltaX;
        			deltaYcopy = _lancement.deltaY;
        			effetON = _lancement.getEffetON();
    			}
    		}
    		
    		if(effetON)
		    	if(v.getX() >= deltaXcopy && v.getX() <= (deltaXcopy+1100+10) && v.getY() >= (-deltaYcopy) && v.getY() <= (-deltaYcopy+750+10)){ // 1100 et 750 sont les variables de LANCEMENT, FENHAUT et FENLARG
		    		switch(v.getMetamor()){
		    		case 0:
		    			arraySons.add((int)(2+Math.random()*2));
		    			break;
		    		case 1:
		    			arraySons.add((int)(1+Math.random()*5));
		    			break;
		    		case 2:
		    			arraySons.add(9);
		    			break;
		    		case 3:
		    			arraySons.add(7);
		    			break;
		    		case 4:
		    			arraySons.add(7);
		    			break;
		    		case 5:
		    			arraySons.add(8);
		    			break;
		    		default:
		    			arraySons.add(1);
		    			break;
		    		}
		    	}
    	}catch(NullPointerException e){e.printStackTrace();
    	}catch(Exception e){e.printStackTrace();}
    	
    	switch(v.getMetamor()){
    	case 0:
        	switch(w.getMetamor()){
        	case 1:
        		w.setDmgSurUnit(v.getDmg() - 7);
        		
        		break;
        	case 2:
        		w.setDmgSurUnit(v.getDmg() - 4);
        		break;
        	default:
        		w.setDmgSurUnit(v.getDmg());
        		break;
        	}
    		break;
    	case 1:
        	switch(w.getMetamor()){
        	case 0:
        		w.setDmgSurUnit(v.getDmg() + 7);
        		break;
        	case 2:
        		w.setDmgSurUnit(v.getDmg() + 4);
        		break;
        	case 3:
        		w.setDmgSurUnit(v.getDmg() + 6);
        		break;
        	case 5:
        		w.setDmgSurUnit(v.getDmg() + 18);
        		break;
        	default:
        		w.setDmgSurUnit(v.getDmg());
        		break;
        	}
    		break;
    	case 2:
        	switch(w.getMetamor()){
        	case 0:
        		w.setDmgSurUnit(v.getDmg() + 7);
        		break;
        	case 1:
        		w.setDmgSurUnit(v.getDmg() - 4);
        		break;
        	case 3:
        		w.setDmgSurUnit(v.getDmg() + 12);
        		break;
        	case 4:
        		w.setDmgSurUnit(v.getDmg() + 9);
        		break;
        	case 5:
        		w.setDmgSurUnit(v.getDmg() - 21);
        		break;
        	default:
        		w.setDmgSurUnit(v.getDmg());
        		break;
        	}
    		break;
    	case 3:	// archer 		
        	switch(w.getMetamor()){
        	case 1:
        		arrayTireFleche.add(new TireArcher((int)v.getX(),(int)v.getY(), (int)w.getX(),(int)w.getY(), 1));	// Le 1 sera remplacer par une fonction pour calculer l'angle
        		w.setDmgSurUnit(v.getDmg() - 10);
        		break;
        	case 2:	// Minautaure
        		arrayTireFleche.add(new TireArcher((int)v.getX(),(int)v.getY(), (int)w.getX(),(int)w.getY(), 1));
        		w.setDmgSurUnit(v.getDmg() - 5);
        		break;
        	default:
        		arrayTireFleche.add(new TireArcher((int)v.getX(),(int)v.getY(), (int)w.getX(),(int)w.getY(), 1));
        		w.setDmgSurUnit(v.getDmg());
        		break;
        	}
    		break;
    	case 4:	// centaure
        	switch(w.getMetamor()){
        	case 1:
        		arrayTireFleche.add(new TireCentaure((int)v.getX(),(int)v.getY(), (int)w.getX(),(int)w.getY(), 1));
        		w.setDmgSurUnit(v.getDmg() - 24);
        		break;
        	case 2:
        		arrayTireFleche.add(new TireCentaure((int)v.getX(),(int)v.getY(), (int)w.getX(),(int)w.getY(), 1));
        		w.setDmgSurUnit(v.getDmg() - 21);
        		break;
        	default:
        		arrayTireFleche.add(new TireCentaure((int)v.getX(),(int)v.getY(), (int)w.getX(),(int)w.getY(), 1));
        		w.setDmgSurUnit(v.getDmg());
        		break;
        	}
    		break;
    	case 5:	// Mage
	    	if(differencierJoueur == 1){
	    		//attaqueMage(); Peut-etre une autre fonction	car je perds ce systeme pour les 2 joueurs ou ajouter un int et différencier ici en fonction de ce int
	        	switch(w.getMetamor()){
	        	case 1:
	        		//w.setDmgSurUnit(v.getDmg() - 29);
	        		arrayMonsterOwnerTire.add(new TireMage((int)v.getX(),(int)v.getY(), (int)w.getX(),(int)w.getY(), v.getDmg()-30,v.getId(),w.getId()));
	        		break;
	        	case 2:	// Minautaure
	        		//w.setDmgSurUnit(v.getDmg() + 15);
	        		arrayMonsterOwnerTire.add(new TireMage((int)v.getX(),(int)v.getY(), (int)w.getX(),(int)w.getY(), v.getDmg()+20,v.getId(),w.getId()));
	        		break;
	        	case 5:
	        		//w.setDmgSurUnit(v.getDmg() - 30);
	        		arrayMonsterOwnerTire.add(new TireMage((int)v.getX(),(int)v.getY(), (int)w.getX(),(int)w.getY(), v.getDmg()-30,v.getId(),w.getId()));
	        		break;
	        	default:
	        		//w.setDmgSurUnit(v.getDmg());
	        		arrayMonsterOwnerTire.add(new TireMage((int)v.getX(),(int)v.getY(), (int)w.getX(),(int)w.getY(), v.getDmg(),v.getId(),w.getId()));
	        		//debPhrs("Mage");
	        		break;
	        	}//*/
        	}else{
	        	switch(w.getMetamor()){
	        	case 1:
	        		//w.setDmgSurUnit(v.getDmg() - 29);
	        		arrayMonsterEnnemyTire.add(new TireMage((int)v.getX(),(int)v.getY(), (int)w.getX(),(int)w.getY(), v.getDmg()-30,v.getId(),w.getId()));
	        		break;
	        	case 2:	// Minautaure
	        		//w.setDmgSurUnit(v.getDmg() + 15);
	        		arrayMonsterEnnemyTire.add(new TireMage((int)v.getX(),(int)v.getY(), (int)w.getX(),(int)w.getY(), v.getDmg()+20,v.getId(),w.getId()));
	        		break;
	        	case 5:
	        		//w.setDmgSurUnit(v.getDmg() - 30);
	        		arrayMonsterEnnemyTire.add(new TireMage((int)v.getX(),(int)v.getY(), (int)w.getX(),(int)w.getY(), v.getDmg()-30,v.getId(),w.getId()));
	        		break;
	        	default:
	        		//w.setDmgSurUnit(v.getDmg());
	        		arrayMonsterEnnemyTire.add(new TireMage((int)v.getX(),(int)v.getY(), (int)w.getX(),(int)w.getY(), v.getDmg(),v.getId(),w.getId()));
	        		//debPhrs("Mage");
	        		break;
	        	}
        	}
    		break;
    	}
    	
    	if(w.getVie() <= 0){
    		v.setKills();	
    		augmenterPoint(w.getMetamor(), differencierJoueur);
    		
    		// Statistique de fin de jeu
    		ajouterTotalTuer(w);
    	}
    	
    }
    /**Increase point depending the unit killed*/
    private void augmenterPoint(int meta, int differencierJoueur){
    	int point=0;
		switch(meta){
		case 0:
			point = 5;
			break;
		case 1:
			point = 7;
			break;
		case 2:
			point = 10;
			break;
		case 3:
			point = 7;
			break;
		case 4:
			point = 12;
			break;
		case 5:
			point = 15;
			break;
		}
		
		if(differencierJoueur == 1)
			pointsJ1 += point;
		else
			pointsJ2 += point;
    }
    
    /**	Destroy lost and ended shoot */
    private void enleverTireFiniEtPerdu(){
    	int j,u;
    	j=0;
		for(u=0; u<arrayMonsterOwnerTire.size()-j;u++)	// Faire disparaitre les tires fini
			if(arrayMonsterOwnerTire.get(u).getDeltaX() == 0 && arrayMonsterOwnerTire.get(u).getDeltaY() == 0){
				arrayMonsterOwnerTire.get(u).killTire();
				arrayMonsterOwnerTire.remove(u);
				j++;
			}
		j=0;
		for(Monster v : arrayMonsterEnnemy)	// Enlever tire qui se sont perdus
			if(v != null)
				if(!arrayMonsterOwnerTire.isEmpty())
					if(arrayMonsterOwnerTire.get(0) != null){
						if(arrayMonsterOwnerTire.get(0).getIdCible() == v.getId()){
							break;
						}else{
							if(j == arrayMonsterEnnemy.size()-1){
								//debPhrs("j == ennemy Size");
								arrayMonsterOwnerTire.get(0).killTire();
								arrayMonsterOwnerTire.remove(0);
							}
							j++;
						}
					}
		
		// Pour ceux de l'ennemie
    	j=0;
		for(u=0; u<arrayMonsterEnnemyTire.size()-j;u++)	// Faire disparaitre les tires fini
			if(arrayMonsterEnnemyTire.get(u).getDeltaX() == 0 && arrayMonsterEnnemyTire.get(u).getDeltaY() == 0){
				arrayMonsterEnnemyTire.get(u).killTire();
				arrayMonsterEnnemyTire.remove(u);
				j++;
			}
		j=0;
		for(Monster v : arrayMonsterOwner)	// Enlever tire qui se sont perdus
			if(v != null)
				if(!arrayMonsterEnnemyTire.isEmpty())
					if(arrayMonsterEnnemyTire.get(0) != null){
						if(arrayMonsterEnnemyTire.get(0).getIdCible() == v.getId()){
							break;
						}else{
							if(j == arrayMonsterOwner.size()-1){
								arrayMonsterEnnemyTire.get(0).killTire();
								arrayMonsterEnnemyTire.remove(0);
							}
							j++;
						}
					}
		j=0;
		for(u=0; u<arrayTireFleche.size() - j;u++)
			if(arrayTireFleche.get(u) != null)
				if(arrayTireFleche.get(u).getDeltaX() == 0 && arrayTireFleche.get(u).getDeltaY() == 0){
					arrayTireFleche.get(u).killTire();
					arrayTireFleche.remove(u);
					j++;
				}
    }
    /**	Make fireballs follow its ennemy*/
    private void faireSuivreTireUnit(){
    	String message="J1";
    	int j, u;
    	int temp; // Instancier ici pour éviter que ce soit fait dans la boucle (Appris dans l'optimisation des programmes de drakulo.com)
		 j=0;
		// for(Tire v : arrayMonsterOwnerTire)	// tire suit l'unité
		for(u=0; u < arrayMonsterOwnerTire.size() - j;u++)	
		 	if(arrayMonsterOwnerTire.get(u) != null){
				 for(Monster o : arrayMonsterEnnemy){
					 if(o != null){
						 if(o.getId() == arrayMonsterOwnerTire.get(u).getIdCible()){
							
							(arrayMonsterOwnerTire.get(u)).changeDestination((int)o.getX(), (int)o.getY());
							 arrayMonsterOwnerTire.get(u).moveTire();
							// debPhrs("Tire avance");
						 }
						 if(arrayMonsterOwnerTire.get(u).getDeltaX() == 0 && arrayMonsterOwnerTire.get(u).getDeltaY() == 0){	// Dégât de zone pas pris en compte encore, pas sûr que je le fasse (unit trop puissante ?)
							 o.setDmgSurUnit(arrayMonsterOwnerTire.get(u).getDmg());
							 temp = arrayMonsterOwnerTire.get(u).getIdAttaquant();
							 arrayMonsterOwnerTire.remove(u);
							 j++;
							 if(o.getVie() <= 0){
								 // Statistique fin de partie
								 unitTotalTuerJ1++;
								 for(Monster p : arrayMonsterOwner)
									 	if(p.getId() == temp)
									 		p.setKills();
								 augmenterPoint(o.getMetamor(), 1);
								if(joueur==1){	// QUE J1 QUI GERE, ENVOYER INFO À J2
									message+="kill-"+""+o.getId()+":";
									o.killMonster();
									arrayMonsterEnnemy.remove(arrayMonsterEnnemy.indexOf(o));
								}
								 break;
							 }
							 break;
						 }
					 }
				 }
			 }
		if(!message.equalsIgnoreCase("J1") && !message.equalsIgnoreCase(""))
			if(_clientReceiver!=null)
				_clientReceiver.sendAnyMsg("all"+message);
		
		// Pour l'ennemie
		int k=0;
		j=0;
		message="J2";
		for(u=0; u < arrayMonsterEnnemyTire.size() - j;u++)	
		 	if(arrayMonsterEnnemyTire.get(u) != null){
				 for(Monster o : arrayMonsterOwner){
					 if(o != null){
						 //k++;
						 if(o.getId() == arrayMonsterEnnemyTire.get(u).getIdCible()){
							 
								 ( arrayMonsterEnnemyTire.get(u)).changeDestination((int)o.getX(), (int)o.getY());
							 arrayMonsterEnnemyTire.get(u).moveTire();
							// debPhrs("Tire avance");
						 }
						 if(arrayMonsterEnnemyTire.get(u).getDeltaX() == 0 && arrayMonsterEnnemyTire.get(u).getDeltaY() == 0){	// Dégât de zone pas pris en compte encore, pas sûr que je le fasse (unit trop puissante ?)
							 // Je mettrai surement un dmg / 2 pour les autres qui se trouvent dans la zone
							 o.setDmgSurUnit(arrayMonsterEnnemyTire.get(u).getDmg());
							 temp = arrayMonsterEnnemyTire.get(u).getIdAttaquant();
							 arrayMonsterEnnemyTire.remove(u);
							 j++;
							 if(o.getVie() <= 0){
								// Statistique fin de partie
								 unitTotalTuerJ2++;
								 for(Monster p : arrayMonsterEnnemy)
									 	if(p.getId() == temp){
									 		p.setKills();
									 	}
								 
								 k = arrayMonsterOwner.indexOf(o);	// Trouver la position de l'unité dans le arrayMonsterOwner
								if(k != -1)
									decrementGroup(k,1);//*/
								augmenterPoint(o.getMetamor(), 2);
								if(joueur==1){	// QUE J1 QUI GERE, ENVOYER INFO À J2
									message+="kill-"+""+o.getId()+":";
									o.killMonster(o.getMetamor());	// mettre les variables au point mort et decrementé cette fois car c'est une unité au joueur
									arrayMonsterOwner.remove(arrayMonsterOwner.indexOf(o));
								}
								 break;
							 }
							 break;
						 }
					 }
				 }
			 }
		if(!message.equalsIgnoreCase("J2") && !message.equalsIgnoreCase(""))
			if(_clientReceiver!=null)
				_clientReceiver.sendAnyMsg("all"+message);	// On pourrait l'envoyer seulement au joueur 2
    }
    /**	Move arrow, increment x and y depending on deltaX and deltaY*/
    private void faireAvancerFleche(){
    	for(Tire o : arrayTireFleche)
    		if(o!= null)
    			o.moveTire();
    }
    /**	Destroy dead unit */
    private void enleverUnitDead(){
    	String message="J1";
    	int u,j;
    	//  Peut etre fait dans attaquerUnit en vérifiant que la vie est <= 0
		j=0;
		for(u=0; u<arrayMonsterOwner.size()-j;u++)	// Faire disparaitre les unités mortes
			if(arrayMonsterOwner.get(u).getVie() <= 0){
				decrementGroup(u,1);
				message+="kill-"+""+arrayMonsterOwner.get(u).getId()+":";
				arrayMonsterOwner.get(u).killMonster(arrayMonsterOwner.get(u).getMetamor());	// <-- donc pour J1
				arrayMonsterOwner.remove(u);
				j++;
			}
		if(!message.equalsIgnoreCase("J1") && !message.equalsIgnoreCase(""))
			if(_clientReceiver!=null)
				_clientReceiver.sendAnyMsg("all"+message);
		// Les unités de l'ennemie sont tuées dans la fonction attaquerUnit(Monster, Monster)
		// Mais je vais comme meme faire un check ici
		j=0;
		message="J2";
		for(u=0; u<arrayMonsterEnnemy.size()-j;u++)	// Faire disparaitre les unités mortes
			if(arrayMonsterEnnemy.get(u).getVie() <= 0){
				decrementGroup(u,2);	//	Car c'est l'ennemie, et son nb d'unité n'est pas affichée ni quoi que ce soit
				message+="kill-"+""+arrayMonsterEnnemy.get(u).getId()+":";
				arrayMonsterEnnemy.get(u).killMonster();
				arrayMonsterEnnemy.remove(u);
				j++;
			}
		if(!message.equalsIgnoreCase("J2") && !message.equalsIgnoreCase(""))
			if(_clientReceiver!=null)
				_clientReceiver.sendAnyMsg("all"+message);
    }
    /**	Mana is incremented after a while */
    private void checkManaLoop(){
    	if(manaloop>=400)
    		manaloop=0;
    	else
    		manaloop++;
    }
    /**	Decrement unit groups on death of one of them */
    private void decrementGroup(int a, int diff){
    	int k=0;
    	if(diff==1){
			for(Monster o : arrayMonsterOwnerSelected){
				if(o.getId() == arrayMonsterOwner.get(a).getId()){
					arrayMonsterOwnerSelected.remove(k);
					break;
				}
				k++;
			}
	    	
	    	k=0;
			for(Monster o : arrayMonsterOwnerGroup1){
				if(o.getId() == arrayMonsterOwner.get(a).getId()){
					arrayMonsterOwnerGroup1.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup2){
				if(o.getId() == arrayMonsterOwner.get(a).getId()){
					arrayMonsterOwnerGroup2.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup3){
				if(o.getId() == arrayMonsterOwner.get(a).getId()){
					arrayMonsterOwnerGroup3.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup4){
				if(o.getId() == arrayMonsterOwner.get(a).getId()){
					arrayMonsterOwnerGroup4.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup5){
				if(o.getId() == arrayMonsterOwner.get(a).getId()){
					arrayMonsterOwnerGroup5.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup6){
				if(o.getId() == arrayMonsterOwner.get(a).getId()){
					arrayMonsterOwnerGroup6.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup7){
				if(o.getId() == arrayMonsterOwner.get(a).getId()){
					arrayMonsterOwnerGroup7.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup8){
				if(o.getId() == arrayMonsterOwner.get(a).getId()){
					arrayMonsterOwnerGroup8.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup9){
				if(o.getId() == arrayMonsterOwner.get(a).getId()){
					arrayMonsterOwnerGroup9.remove(k);
					break;
				}
				k++;
			}
    	}else{
    		k=0;
    		for(Monster o : arrayMonsterOwnerSelected){
				if(o.getId() == arrayMonsterEnnemy.get(a).getId()){
					arrayMonsterOwnerSelected.remove(k);
					break;
				}
				k++;
			}
	    	
	    	k=0;
			for(Monster o : arrayMonsterOwnerGroup1){
				if(o.getId() == arrayMonsterEnnemy.get(a).getId()){
					arrayMonsterOwnerGroup1.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup2){
				if(o.getId() == arrayMonsterEnnemy.get(a).getId()){
					arrayMonsterOwnerGroup2.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup3){
				if(o.getId() == arrayMonsterEnnemy.get(a).getId()){
					arrayMonsterOwnerGroup3.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup4){
				if(o.getId() == arrayMonsterEnnemy.get(a).getId()){
					arrayMonsterOwnerGroup4.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup5){
				if(o.getId() == arrayMonsterEnnemy.get(a).getId()){
					arrayMonsterOwnerGroup5.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup6){
				if(o.getId() == arrayMonsterEnnemy.get(a).getId()){
					arrayMonsterOwnerGroup6.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup7){
				if(o.getId() == arrayMonsterEnnemy.get(a).getId()){
					arrayMonsterOwnerGroup7.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup8){
				if(o.getId() == arrayMonsterEnnemy.get(a).getId()){
					arrayMonsterOwnerGroup8.remove(k);
					break;
				}
				k++;
			}
			k=0;
			for(Monster o : arrayMonsterOwnerGroup9){
				if(o.getId() == arrayMonsterEnnemy.get(a).getId()){
					arrayMonsterOwnerGroup9.remove(k);
					break;
				}
				k++;
			}
    	}
    }
    
    
 /** 
	 * Calculate how many milliseconds have passed 
	 * since last frame.
	 * 
	 * @return milliseconds passed since last frame 
	 */
	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
 
	    return delta;
	}
 
	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	/**Increment Timer */
	private void changeTimer(){
    	if(argentloop%60==0)
    		secTimer++;
    	if(secTimer>=60){
    		secTimer=0;
    		minTimer++;
    		if(minTimer>99)
    			minTimer=0;
    	}
    }

	public void debPhrs(String phrs){	// DebugPhrs
		System.out.println(phrs);
	}

	/**
	 *	Spawn possible
	 * @return true if player 2 is able to spawn a unit
	 */
	public boolean spawnPossibleJ2(int metamor){
		switch(metamor){
    	case 0:
    		if(argentJ2>=COST_NAIN){
    			argentJ2-=COST_NAIN;
    			return true;
    		}
    		break;
    	case 1:
    		if(argentJ2>=COST_FANTASSIN){
    			argentJ2-=COST_FANTASSIN;
    			return true;
    		}
    		break;
    	case 2:
    		if(argentJ2>=COST_MINAUTAURE){
    			argentJ2-=COST_MINAUTAURE;
    			return true;
    		}
    		break;
    	case 3:
    		if(argentJ2>=COST_ARCHER){
    			argentJ2-=COST_ARCHER;
    			return true;
    		}
    		break;
    	case 4:
    		if(argentJ2>=COST_CENTAURE){
    			argentJ2-=COST_CENTAURE;
    			return true;
    		}
    		break;
    	case 5:
    		if(argentJ2>=COST_MAGE){
    			argentJ2-=COST_MAGE;
    			return true;
    		}
    		break;
    	}
				
		return false;
	}
	/**	Check if player may buy a unit and add it. Only server may check */
	public void acheterUnit(int typeUnit, int joueurrr){
    	/*	0-> nain	1-> fantasin	2-> minotaure
    	 * 	3->A voir	4-> centaure	5-> mage
    	 */
		if(joueurrr==1)
	    	switch(typeUnit){
	    	case 0:
	    		if(argentJ1>=COST_NAIN){
	    			argentJ1-=COST_NAIN;
	    			arrayMonsterOwnerBought.add(0);
	    			arrayMonsterOwnerBoughtTime.add(getTime());
	    		}else{
	    			ajouterTexteError("Achat impossible. Coût: "+COST_NAIN);
	    		}
	    		break;
	    	case 1:
	    		if(argentJ1>=COST_FANTASSIN){
	    			argentJ1-=COST_FANTASSIN;
	    			arrayMonsterOwnerBought.add(1);
	    			arrayMonsterOwnerBoughtTime.add(getTime());
	    		}else{
	    			ajouterTexteError("Achat impossible. Coût: "+COST_FANTASSIN);
	    		}
	    		break;
	    	case 2:
	    		if(argentJ1>=COST_MINAUTAURE){
	    			argentJ1-=COST_MINAUTAURE;
	    			arrayMonsterOwnerBought.add(2);
	    			arrayMonsterOwnerBoughtTime.add(getTime());
	    		}else{
	    			ajouterTexteError("Achat impossible. Coût: "+COST_MINAUTAURE);
	    		}
	    		break;
	    	case 3:
	    		if(argentJ1>=COST_ARCHER){
	    			argentJ1-=COST_ARCHER;
	    			arrayMonsterOwnerBought.add(3);
	    			arrayMonsterOwnerBoughtTime.add(getTime());
	    		}else{
	    			ajouterTexteError("Achat impossible. Coût: "+COST_ARCHER);
	    		}
	    		break;
	    	case 4:
	    		if(argentJ1>=COST_CENTAURE){
	    			argentJ1-=COST_CENTAURE;
	    			arrayMonsterOwnerBought.add(4);
	    			arrayMonsterOwnerBoughtTime.add(getTime());
	    		}else{
	    			ajouterTexteError("Achat impossible. Coût: "+COST_CENTAURE);
	    		}
	    		break;
	    	case 5:
	    		if(argentJ1>=COST_MAGE){
	    			argentJ1-=COST_MAGE;
	    			arrayMonsterOwnerBought.add(5);
	    			arrayMonsterOwnerBoughtTime.add(getTime());
	    		}else{
	    			ajouterTexteError("Achat impossible. Coût: "+COST_MAGE);
	    		}
	    		break;
	    	}
		else
			switch(typeUnit){
	    	case 0:
	    		if(argentJ2>=COST_NAIN){
	    			argentJ2-=COST_NAIN;
	    			arrayMonsterEnnemyBought.add(0);
	    			arrayMonsterEnnemyBoughtTime.add(getTime());
	    		}else{
	    			ajouterTexteError("Achat impossible. Coût: "+COST_NAIN);
	    		}
	    		break;
	    	case 1:
	    		if(argentJ2>=COST_FANTASSIN){
	    			argentJ2-=COST_FANTASSIN;
	    			arrayMonsterEnnemyBought.add(1);
	    			arrayMonsterEnnemyBoughtTime.add(getTime());
	    		}else{
	    			ajouterTexteError("Achat impossible. Coût: "+COST_FANTASSIN);
	    		}
	    		break;
	    	case 2:
	    		if(argentJ2>=COST_MINAUTAURE){
	    			argentJ2-=COST_MINAUTAURE;
	    			arrayMonsterEnnemyBought.add(2);
	    			arrayMonsterEnnemyBoughtTime.add(getTime());
	    		}else{
	    			ajouterTexteError("Achat impossible. Coût: "+COST_MINAUTAURE);
	    		}
	    		break;
	    	case 3:
	    		if(argentJ2>=COST_ARCHER){
	    			argentJ2-=COST_ARCHER;
	    			arrayMonsterEnnemyBought.add(3);
	    			arrayMonsterEnnemyBoughtTime.add(getTime());
	    		}else{
	    			ajouterTexteError("Achat impossible. Coût: "+COST_ARCHER);
	    		}
	    		break;
	    	case 4:
	    		if(argentJ2>=COST_CENTAURE){
	    			argentJ2-=COST_CENTAURE;
	    			arrayMonsterEnnemyBought.add(4);
	    			arrayMonsterEnnemyBoughtTime.add(getTime());
	    		}else{
	    			ajouterTexteError("Achat impossible. Coût: "+COST_CENTAURE);
	    		}
	    		break;
	    	case 5:
	    		if(argentJ2>=COST_MAGE){
	    			argentJ2-=COST_MAGE;
	    			arrayMonsterEnnemyBought.add(5);
	    			arrayMonsterEnnemyBoughtTime.add(getTime());
	    		}else{
	    			ajouterTexteError("Achat impossible. Coût: "+COST_MAGE);
	    		}
	    		break;
	    	}
    }
	
	
	// Fonction que au J2
	/** Player 2 kill the unit (depends on id). Id given by server */
	public void killUnitReceived(int id, int diffJoueur){
		int j,u;
		if(diffJoueur==1){
			for(Monster v : arrayMonsterOwner)
				if(v.getId() == id){
											
					j=0;
					for(u=0; u<arrayMonsterOwner.size()-j;u++)
						if(arrayMonsterOwner.get(u).getId() == id){
							decrementGroup(u,1);
							arrayMonsterOwner.remove(u);
							j++;
							break;
						}
					
					v.killMonster();	// mettre a null ? , les trucs de groupes, ...
					v = null;	// ATTENTION A VOIR SI C BIEN ! PAS SUR QUE J'AI FAIT VERIFICATION PARTOUT
					break;
				}
	}else{
			for(Monster y : arrayMonsterEnnemy)
				if(y.getId() == id){
											
					j=0;
					for(u=0; u<arrayMonsterEnnemy.size()-j;u++)
						if(arrayMonsterEnnemy.get(u).getId() == id){
							decrementGroup(u,2);
							arrayMonsterEnnemy.get(u).killMonster(arrayMonsterEnnemy.get(u).getMetamor());	// Seulement si c que J2 qui utilise cette fonction
							arrayMonsterEnnemy.remove(u);
							j++;
							break;
						}
					
					//y.killMonster();
					y = null; // ATTENTION
					break;
				}
		}
	}
	/** Pouvoir envoyer des information au serveur puis aux joueurs 
	 * @param a ClientReceiver
	 * */
 	public void setClientReceiver(ClientReceiver a){
			this._clientReceiver=a;
		}
	/** Increment loop, on max send message to server */
	public void checkLoopDeath(){
		if(loopDeath >= 400){
			loopDeath=0;
			String msg="checkDeath";
			if(_clientReceiver!=null){	// Syntaxe checkDeathJ1ouJ2-id:
				for(Monster v : arrayMonsterOwner)
					if(v.getVie()<=0)
						msg+="J1-"+v.getId()+":";
				for(Monster w : arrayMonsterEnnemy)
					if(w.getVie()<=0)
						msg+="J2-"+w.getId()+":";
				if(!msg.equalsIgnoreCase("") && !msg.equalsIgnoreCase("checkDeath"))
					if(_clientReceiver!=null)
						_clientReceiver.sendCheckDeath(""+msg);
			}
		}else
			loopDeath++;
		
			
	}
	
	
	/** Pour texte qui apparait dans les tutos */
	private void ajouterTexteSolo(String message, int temps){
		arrayServerTextInGameChatSolo.add("2:"+message);
		arrayServerTextInGameChatTimeSolo.add(getTime()+temps);
	}
	/** 
	 * Ajouter texte pour actions impossible etc
	 * Gère un maximum de message dans cette liste et remplace si y a pas assez de place
	 * Enlève les messages qui excèdent un temps imparti à chaque message 
	 */
	public void ajouterTexteError(String message){
		// L'appel de la fonction est faite dans un interval de 4 sec environ en ajoutant un msg "" dans augmenterArgent
		boolean check=true;
		if(arrayServerTextInGameError.size() >= 10){
			arrayServerTextInGameError.remove(0);
			if(!arrayServerTextInGameTimeError.isEmpty())
				arrayServerTextInGameTimeError.remove(0);
		}
			
			arrayServerTextInGameError.add(""+message);
			arrayServerTextInGameTimeError.add(getTime());
		
		do{
			if(!arrayServerTextInGameError.isEmpty()){
				if(arrayServerTextInGameTimeError.isEmpty()){
					arrayServerTextInGameError.remove(0);
					check=false;
				}else{
					if( ((int)(  ((arrayServerTextInGameTimeError.get(0))  - getTime() ) / 1000) + 5) <= 0){
						arrayServerTextInGameError.remove(0);
						arrayServerTextInGameTimeError.remove(0);
					}else{
						check=false;
					}
				}
			}else{
				check=false;
			}
		}while(check);
	}
	/** Ajoute Unité créé dans texte error si une unité est créé */
	private void unitCree(){
		boolean contient=false;
    	//if(joueur != differencierJoueur)
    		for(String l : arrayServerTextInGameError)
    			if(l.equalsIgnoreCase("Unité créé")){
    				contient=true;
    				break;
    			}		
    	if(!contient)
    		ajouterTexteError("Unité créé");
	}
	
	/** Initialise la partie solo */
	private void siwtchDesPartie(int partie){
		switch(partie){
		case 1:	// L'economie et les nains
			argentJ1=10;
			
			for(int i=0;i<5;i++)
				arrayMonsterOwner.add(new Monster(100+40*i,150));	
			
			Monster.nbreNain=5;
			Monster.nbreUnite=5;
			Monster.nbreCentaure=0;
			
			arrayServerTextInGameChatSolo.clear();
			arrayServerTextInGameChatTimeSolo.clear();
			
			objectifs = "Bienvenue dans le tuto des nains-" +
					"Ici le but est d'avoir 200 or le plus-" +
					"rapidement possible-" +
					"-" +
					"Quelques astuces :-" +
					"    Obtention de l'or toutes les 5 sec-" +
					"    base     1or/nain-" +
					"    milieu   2or/nain-" +
					"    ennemie  4or/nain";
		  	
			ajouterTexteSolo("Salut, aujourd'hui on vas se remplir la bourse avec nos\"amis\"les nains", 1000);
			ajouterTexteSolo(" ",1000);
			ajouterTexteSolo("Votre but ici va être d'atteindre 200 or le plus rapidement possible", 3000);
			ajouterTexteSolo("Une fois ce but atteint, vous pouvez quitter ou essayer de tuer toutes les unités adverses.", 8000);
			ajouterTexteSolo("Quand vous serez grand vous découvrirez plus tard les transformations, les faiblesses et les avantages.", 10000);
			ajouterTexteSolo(" ", 12000);
			ajouterTexteSolo(" ",12000);
			ajouterTexteSolo("Bon jeu !!", 12000);
			
			ajouterTexteSolo("Sachez que les nains vous permettent d'avoir de l'or", 15000);
			ajouterTexteSolo("Il vous faut donc en avoir le plus possible mais cette unité est très faible !", 18000);
			ajouterTexteSolo(" ",20000);
			ajouterTexteSolo("Elle coûte 15 or. Vous recevez de l'or toutes les 5 secondes.", 20000);
			ajouterTexteSolo("Chaque nain rapporte 1 or dans votre base, 2 au milieu et 4 dans le camps de l'ennemie.", 21000);
			ajouterTexteSolo("Donc à vous de choisir entre l'économie et l'attaque lorsque vous ferez des parties sur internet", 24000);
			ajouterTexteSolo(" ",32000);
			ajouterTexteSolo(" ",32000);
			
			ajouterTexteSolo("Regarder en bas à droite, il y a écrit \"Transformation\" ou \"achat\"",32000);
			ajouterTexteSolo(" ",32000);
			ajouterTexteSolo("Si c'est achat, vous pouvez acheter des unités en cliquant sur les icons des unités ou",36000);
			ajouterTexteSolo("en utilisant les touches raccourcis.",38000);
			ajouterTexteSolo("Nous avons donc nain, fantassin, minautaure, archer, centaure, et mage", 42000);
			ajouterTexteSolo(" ",47000);
			ajouterTexteSolo("Ici vous n'allez acheter que des nains.",47000);
			ajouterTexteSolo("Meme si je sais que certains vont faire les malins et en acheter d'autres ",49000);
			ajouterTexteSolo(" ",54000);
			ajouterTexteSolo("Pour transformer une unité, il faut la sélectionner puis utiliser les mêmes icons",54000);
			ajouterTexteSolo("qui vous servent à acheter des unités. Mais ceci sera vu dans un autre tuto.",56000);
			
			ajouterTexteSolo(" ",65000);
			ajouterTexteSolo(" ",65000);
			ajouterTexteSolo("Voila c'est fini, Bon jeu !", 65000);
			ajouterTexteSolo("N'hésiter pas à visiter le site tout les jours.", 75000);
			break;	
		case 2:	// Corps à corps
			argentJ1=400;
			
			for(int i=0;i<5;i++)
				for(int k=0;k<5;k++){
					arrayMonsterOwner.add(new Monster(100+40*k,100+40*i,1));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,1));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,1));
					
					arrayMonsterOwner.add(new Monster(400+40*k,100+40*i,2));
					arrayMonsterOwner.add(new Monster(400+40*k,350+40*i,2));
					arrayMonsterOwner.add(new Monster(400+40*k,650+40*i,2));
					
					arrayMonsterOwner.add(new Monster(650+40*k,650+40*i,2));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,100+40*i,4));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1100+40*i,4));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,700+40*i,5));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,400+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,900+40*i,3));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,1500+40*i,2));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1600+40*i,3));
				}
		
			Monster.nbreUnite = 150;
			Monster.nbreNain = 0;
			Monster.nbreFantassin = 75;
			Monster.nbreMinautaure = 75;
			Monster.nbreArcher = 0;
			Monster.nbreCentaure = 0;
			Monster.nbreMage = 0;
			
			arrayServerTextInGameChatSolo.clear();
			arrayServerTextInGameChatTimeSolo.clear();
			
			objectifs = "Bienvenue dans le tuto du corps à corps-" +
					"Des fantassins et des minautaures !!-" +
					"-" +
					"-" +
					"But :-" +
					"    Apprendre le \"attaque clique\"-" +
					"Sélectionner les unités, puis bouton-" +
					"des épées (touche a ou q) puis-" +
					"clique droit vers où les unités vont-" +
					"attaquer-" +
					"    Regarder les faiblesses et avantages-" +
					"des unités-" +
					"    Le bouton Stop-" +
					"    Les formations étoile et rectangle-" +
					"    Les groupes de sélection (ctrl+1,2,...)-" +
					"--" +
					"Tuer tout et vous aurez fini !";
			
			ajouterTexteSolo("Salut, ce tutoriel porte sur les unités de corps à corps.", 1000);
			ajouterTexteSolo(" ",4000);			
			ajouterTexteSolo("Aujourd'hui vous allez pouvoir découvrir les fantassins et les minautaures", 4000);
			ajouterTexteSolo("et aussi castagner accesoirement.",4000);
			ajouterTexteSolo(" ", 10000);
			ajouterTexteSolo("Si vous voulez faire quelques tests, lancer plusieurs fois ce tuto.", 10000);
			ajouterTexteSolo("Il y a 7 groupes d'ennemie de 25 unités donc faites des essais pour",13000);
			ajouterTexteSolo("commencer à voir les avantages et faiblesses des unités.",15000);
			ajouterTexteSolo(" ", 21000);
			ajouterTexteSolo(" ", 21000);
			ajouterTexteSolo("ATTENTION, on va apprendre le move-attaque ! Il faut sélectionner des", 21000);
			ajouterTexteSolo("unités, appuyer sur l'icon avec les deux flèches (haut gauche) puis", 24000);
			ajouterTexteSolo("faire un clique droit vers où vous voulez que les unités se déplacent,", 28000);
			ajouterTexteSolo("elles iront automatiquement attaquer les unités ennemies si elles en voient", 33000);
			ajouterTexteSolo("sur le chemin.",34000);
			ajouterTexteSolo(" ", 40000);
			ajouterTexteSolo(" ", 40000);
			ajouterTexteSolo("Essayer d'attaquer des mages avec des fantassins puis des minautaures,", 40000);
			ajouterTexteSolo("ou fantassins contre archers puis voir la différence avec minautaure contre archer", 45000);
			ajouterTexteSolo(" ", 51000);
			ajouterTexteSolo(" ", 51000);
			ajouterTexteSolo("LES GROUPES DE SELECTION, sélectionner des unités, maintenez la touche control (ctrl) puis", 51000);
			ajouterTexteSolo("appuyer sur les chiffres 1 à 6. Vous aurez ainsi un accès rapide de vos unités pour", 54000);
			ajouterTexteSolo("leur donner des ordres.",55500);
			ajouterTexteSolo(" ", 60000);
			ajouterTexteSolo(" ", 60000);
			ajouterTexteSolo("Vous pouvez commencer à voir les autres icons qui sont :", 60000);
			ajouterTexteSolo("move-Attaque, stop, tenir position, position en étoile puis en rectangle", 62000);
			ajouterTexteSolo(" ", 64000);
			
			ajouterTexteSolo(" ", 70000);
			ajouterTexteSolo(" ", 70000);
			ajouterTexteSolo("Bon jeu ! Continuer les tutos pour en apprendre encore plus !", 70000);	
			ajouterTexteSolo("N'hésiter pas à visiter le site tout les jours.", 85000);
			break;
		case 3:	// Les archers
			argentJ1=400;
			
			for(int i=0;i<5;i++)
				for(int k=0;k<5;k++){
					arrayMonsterOwner.add(new Monster(100+40*k,100+40*i,3));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,3));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,3));
					
					arrayMonsterOwner.add(new Monster(400+40*k,100+40*i,4));
					arrayMonsterOwner.add(new Monster(400+40*k,350+40*i,4));
					arrayMonsterOwner.add(new Monster(400+40*k,650+40*i,4));
					
					arrayMonsterOwner.add(new Monster(650+40*k,650+40*i,5));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,100+40*i,1));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1100+40*i,1));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,700+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,400+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,900+40*i,0));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,1500+40*i,1));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1600+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1000+40*k,1400+40*i,5));
					arrayMonsterEnnemy.add(new Monster(400+40*k,1400+40*i,3));
				}
		
			Monster.nbreUnite = 175;
			Monster.nbreNain = 0;
			Monster.nbreFantassin = 0;
			Monster.nbreMinautaure = 0;
			Monster.nbreArcher = 75;
			Monster.nbreCentaure = 75;
			Monster.nbreMage = 25;
			
			arrayServerTextInGameChatSolo.clear();
			arrayServerTextInGameChatTimeSolo.clear();
			
			objectifs = "Bienvenue dans le tuto des archers et-mages-" +
					"Le \"attaque clique\" ne marche pas pour-eux-" +
					"il faut les contrôler et utiliser le bouton-" +
					"\"stop\" (touche s ou w)-" +
					"-" +
					"  Regarder la différence quand les unités-de" +
					"corps à corps vous poursuivent-" +
					"  Tester les faiblesses et avantages-" +
					"--" +
					"Tuer tout et vous aurez fini !";
			
			ajouterTexteSolo("Cette fois, c'est nos amis les archers et les mages que nous allons voir.", 1000);
			ajouterTexteSolo(" ", 4000);
			ajouterTexteSolo(" ", 4000);
			ajouterTexteSolo("Si vous voulez faire quelques tests, lancer plusieurs fois ce tuto.", 4000);
			ajouterTexteSolo(" ", 8000);
			ajouterTexteSolo(" ", 8000);
			ajouterTexteSolo("Utiliser la touche STOP vous sera extrêment utile pour les unités qui attaque à distance.", 8000);
			ajouterTexteSolo(" ",11000 );
			ajouterTexteSolo("Elles ne possèdent pas la capacité de move-attaque ! Il faut ordonner à vos unités", 11000);
			ajouterTexteSolo("d'avancer puis de s'arrêter pour qu'ils puissent attaquer !", 15000);
			ajouterTexteSolo(" ", 21000);
			ajouterTexteSolo("Il vous faudra donc micro-gérer ces unités de façons à vous enfuir lorsque vos unités", 21000);
			ajouterTexteSolo("ont leur cooldown qui n'est pas rechargé puis faire STOP lorsqu'elles peuvent attaquer", 25000);
			ajouterTexteSolo("puis fuir et ainsi de suite.", 29000);
			/*ajouterTexteSolo("", 1000);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);*/
			
			ajouterTexteSolo(" ", 35000);
			ajouterTexteSolo(" ", 35000);
			ajouterTexteSolo("Bon jeu ! Plus que 2 tutos !", 35000);
			ajouterTexteSolo("N'hésiter pas à visiter le site tout les jours.", 45000);
			break;
		case 4:	// Les transformations
			argentJ1=10;
			
			for(int i=0;i<5;i++)
				for(int k=0;k<5;k++){
					arrayMonsterOwner.add(new Monster(100+40*k,100+40*i,0));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,0));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,0));
					
					arrayMonsterOwner.add(new Monster(400+40*k,100+40*i,0));
					arrayMonsterOwner.add(new Monster(400+40*k,350+40*i,0));
					arrayMonsterOwner.add(new Monster(400+40*k,650+40*i,0));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,100+40*i,1));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1100+40*i,1));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,700+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,400+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,900+40*i,0));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,1500+40*i,1));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1600+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1000+40*k,1400+40*i,5));
					arrayMonsterEnnemy.add(new Monster(400+40*k,1400+40*i,3));
				}
		
			Monster.nbreUnite = 150;
			Monster.nbreNain = 150;
			Monster.nbreFantassin = 0;
			Monster.nbreMinautaure = 0;
			Monster.nbreArcher = 0;
			Monster.nbreCentaure = 0;
			Monster.nbreMage = 0;
			
			arrayServerTextInGameChatSolo.clear();
			arrayServerTextInGameChatTimeSolo.clear();
						
			objectifs ="Bienvenue dans le tuto des-transformations-" +
					"Chaque unité a du mana pour se-transformer-" +
					"-" +
					"Le coût pour se transfomer est différent-" +
					"pour chaque unité et sa régénération-aussi-" +
					"-" +
					"Aller voir sur le site pour de meilleurs-" +
					"informations-" +
					"-" +
					"Continuer de regarder les faiblesses-" +
					"Les unités 100 mana max mais le mage a-" +
					"150 mana max" +
					"--" +
					"Tuer tout et vous aurez fini !";
			
			ajouterTexteSolo("Vous avez cette fois-ci que des nains, vous serez amené à transformer vos unités.", 1000);
			ajouterTexteSolo(" ", 4000);
			ajouterTexteSolo(" ", 4000);
			ajouterTexteSolo("Il vous faut du mana pour vous métamorphoser, celui-ci se regagne lentement",4000);
			ajouterTexteSolo("mais une transformation vous permet de regagner la vie de l'unité.",7000);
			ajouterTexteSolo(" ",11000);
			ajouterTexteSolo(" ",11000);
			ajouterTexteSolo("Les unités ne sont pas toutes bonnes, allez sur le site d'Origins pour voir",11000);
			ajouterTexteSolo("les avantages et faiblesses des unités.", 13000);
			ajouterTexteSolo(" ", 16000);
			ajouterTexteSolo("",16000);
			ajouterTexteSolo("Toutes les unités ne regagnent pas leur mana de la même façons, aller voir le site.",16000);
			ajouterTexteSolo("Toutes ont 100 de mana maximun sauf le mage qui a 150.", 19000);
			ajouterTexteSolo(" ",24000);
			ajouterTexteSolo(" ",24000);
			ajouterTexteSolo("Maintenant explorer la map et utliser les bonnes unités pour attaquer l'ennemie",24000);
			
			
			ajouterTexteSolo(" ",30000);
			ajouterTexteSolo(" ",30000);
			ajouterTexteSolo("Bon jeu ! N'hésiter pas à visiter le site tout les jours.", 30000);
			break;
		case 5:	// Les faiblesses et avantages
			argentJ1=400;
			
			for(int i=0;i<5;i++)
				for(int k=0;k<5;k++){
					arrayMonsterOwner.add(new Monster(100+40*k,100+40*i,0));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,1));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,2));
					
					arrayMonsterOwner.add(new Monster(400+40*k,100+40*i,3));
					arrayMonsterOwner.add(new Monster(400+40*k,350+40*i,4));
					arrayMonsterOwner.add(new Monster(400+40*k,650+40*i,5));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,100+40*i,1));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1100+40*i,1));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,700+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,400+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,900+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,1500+40*i,5));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1600+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1000+40*k,1400+40*i,5));
					arrayMonsterEnnemy.add(new Monster(400+40*k,1400+40*i,3));
				}
		
			Monster.nbreUnite = 150;
			Monster.nbreNain = 25;
			Monster.nbreFantassin = 25;
			Monster.nbreMinautaure = 25;
			Monster.nbreArcher = 25;
			Monster.nbreCentaure = 25;
			Monster.nbreMage = 25;
			
			arrayServerTextInGameChatSolo.clear();
			arrayServerTextInGameChatTimeSolo.clear();
			
			objectifs = "Bienvenue dans le dernier tuto-" +
					"Vous avez 25 unités de chaque-" +
					"-" +
					"Le but est de faire ses tests sur les unités-" +
					"-" +
					"    N'oublier pas qu'il est possible-" +
					"de sélectionner des unités en-" +
					"double cliquant sur une unité-" +
					"    Et que appuyer sur shift permet-" +
					"d'en sélectionner plusieurs une par une-" +
					"ou par groupe" +
					"---" +
					"Tuer tout et vous aurez fini !";
				
			ajouterTexteSolo("On a pu voir dans les autres tutoriels les différentes unités, comment les acheter/transformer...",1000);
			ajouterTexteSolo(" ",4000);
			/*ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			/*ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);
			ajouterTexteSolo("",);*/
			ajouterTexteSolo("Bon jeu",7000);
			break;
		case 6:	// Armée de nain
			argentJ1=100;
			
			for(int i=0;i<5;i++)
				for(int k=0;k<5;k++){
					arrayMonsterOwner.add(new Monster(100+40*k,100+40*i,0));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,0));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,0));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,100+40*i,1));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1100+40*i,1));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,700+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,400+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,900+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,1500+40*i,5));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1600+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1000+40*k,1400+40*i,5));
					arrayMonsterEnnemy.add(new Monster(400+40*k,1400+40*i,3));
					
					arrayMonsterEnnemy.add(new Monster(2000+40*k,1600+40*i,5));
					arrayMonsterEnnemy.add(new Monster(2000+40*k,400+40*i,4));
					arrayMonsterEnnemy.add(new Monster(2000+40*k,1000+40*i,5));
				}
			
			Monster.nbreUnite = 75;
			Monster.nbreNain = 75;
			Monster.nbreFantassin = 0;
			Monster.nbreMinautaure = 0;
			Monster.nbreArcher = 0;
			Monster.nbreCentaure = 0;
			Monster.nbreMage = 0;
			
			objectifs = "Vous ne pouvez utiliser que des nains-" +
					"A vous de faire avec mais attention-l'ennemie" +
					" créé des unités toutes les-minutes-" +
					"-" +
					"Tuer tout !";
			
			ajouterTexteSolo("Ne se battre qu'avec des nains contre une armée entière !", 4000);
			ajouterTexteSolo(" ",8000);
			ajouterTexteSolo("On m'adit que j'étais fou ! Ils avaient peut-être raison !", 8000);
			ajouterTexteSolo(" ",14000);
			ajouterTexteSolo(" ",14000);
			ajouterTexteSolo(" ",14000);
			ajouterTexteSolo("Aller ! Bonne chance ! Je vous ai laisser une bonne armé !",14000);
			ajouterTexteSolo("Mais n'attendez pas trop car l'ennemie achète des unités aussi !",19000);
			ajouterTexteSolo("Une toutes les minutes !",25000);
			break;
		case 7:	// Attaque à distance
			argentJ1=100;
			
			for(int i=0;i<5;i++)
				for(int k=0;k<5;k++){
					arrayMonsterOwner.add(new Monster(100+40*k,100+40*i,3));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,5));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,4));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,100+40*i,1));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1100+40*i,1));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,700+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,400+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,900+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,1500+40*i,5));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1600+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1000+40*k,1400+40*i,5));
					arrayMonsterEnnemy.add(new Monster(400+40*k,1400+40*i,3));
					
					arrayMonsterEnnemy.add(new Monster(2000+40*k,1600+40*i,5));
					arrayMonsterEnnemy.add(new Monster(2000+40*k,400+40*i,4));
					arrayMonsterEnnemy.add(new Monster(2000+40*k,1000+40*i,5));
				}
			
			Monster.nbreUnite = 75;
			Monster.nbreNain = 0;
			Monster.nbreFantassin = 0;
			Monster.nbreMinautaure = 0;
			Monster.nbreArcher = 25;
			Monster.nbreCentaure = 25;
			Monster.nbreMage = 25;
			
			objectifs = "Vous ne pouvez utiliser que des unités-" +
					"d'attaques à distance-" +
					"Et vous ne pouvez pas acheter d'unité-donc" +
					" gérer bien vos unités-" +
					"-" +
					"Tuer tout !";
			
			break;
		case 8:	// tu cours moins vite
			argentJ1=100;
			
			for(int i=0;i<5;i++)
				for(int k=0;k<5;k++){
					arrayMonsterOwner.add(new Monster(100+40*k,100+40*i,0));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,2));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,4));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,100+40*i,1));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1100+40*i,1));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,700+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,400+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,900+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,1500+40*i,5));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1600+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1000+40*k,1400+40*i,5));
					arrayMonsterEnnemy.add(new Monster(400+40*k,1400+40*i,3));
					
					arrayMonsterEnnemy.add(new Monster(2000+40*k,1600+40*i,5));
					arrayMonsterEnnemy.add(new Monster(2000+40*k,400+40*i,4));
					arrayMonsterEnnemy.add(new Monster(2000+40*k,1000+40*i,5));
				}
			
			Monster.nbreUnite = 75;
			Monster.nbreNain = 25;
			Monster.nbreFantassin = 0;
			Monster.nbreMinautaure = 25;
			Monster.nbreArcher = 0;
			Monster.nbreCentaure = 25;
			Monster.nbreMage = 0;
			
			objectifs = "Vos unités ralentissent après un ordre-" +
					"de déplacement, donc vous avez qq mili-" +
					"secondes où vos unités avancent à vitesse-" +
					"normal.-" +
					"-" +
					"Tuer toutes les unités !";
			break;
		case 9:	// massacre 1
			argentJ1=10;
			sangActive=true;
			
			for(int i=0;i<5;i++)
				for(int k=0;k<5;k++){
					arrayMonsterOwner.add(new Monster(100+40*k,100+40*i,0));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,2));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,4));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,100+40*i,1));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1100+40*i,1));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,700+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,400+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,900+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,1500+40*i,5));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1600+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1300+40*k,900+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1100+40*k,1500+40*i,5));
					arrayMonsterEnnemy.add(new Monster(500+40*k,1600+40*i,4));
					arrayMonsterEnnemy.add(new Monster(300+40*k,1400+40*i,5));
					arrayMonsterEnnemy.add(new Monster(1800+40*k,1400+40*i,3));
					
					arrayMonsterEnnemy.add(new Monster(2000+40*k,1600+40*i,5));
					arrayMonsterEnnemy.add(new Monster(2000+40*k,400+40*i,4));
					arrayMonsterEnnemy.add(new Monster(2000+40*k,1000+40*i,5));
				}
			
			Monster.nbreUnite = 75;
			Monster.nbreNain = 25;
			Monster.nbreFantassin = 0;
			Monster.nbreMinautaure = 25;
			Monster.nbreArcher = 0;
			Monster.nbreCentaure = 25;
			Monster.nbreMage = 0;
			
			objectifs = "Des unités ennemies apparaissent partout-" +
					"-" +
					"-" +
					"Tuer tout avant d'être dépasser !";
			break;
		case 10:	// Des contres
			argentJ1=10;
			sangActive=true;
			
			for(int i=0;i<5;i++)
				for(int k=0;k<5;k++){
					arrayMonsterOwner.add(new Monster(100+40*k,100+40*i,1));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,2));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,4));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,100+40*i,1));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1100+40*i,1));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,700+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,400+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,900+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,1500+40*i,5));
					arrayMonsterEnnemy.add(new Monster(500+40*k,1600+40*i,4));
					arrayMonsterEnnemy.add(new Monster(300+40*k,1400+40*i,5));
					arrayMonsterEnnemy.add(new Monster(1800+40*k,1400+40*i,3));
					
					arrayMonsterEnnemy.add(new Monster(2000+40*k,1600+40*i,5));
					arrayMonsterEnnemy.add(new Monster(2000+40*k,400+40*i,4));
					arrayMonsterEnnemy.add(new Monster(2000+40*k,1000+40*i,5));
				}
			
			Monster.nbreUnite = 75;
			Monster.nbreNain = 0;
			Monster.nbreFantassin = 25;
			Monster.nbreMinautaure = 25;
			Monster.nbreArcher = 0;
			Monster.nbreCentaure = 25;
			Monster.nbreMage = 0;
			
			objectifs = "Les unités ennemies se transforment-" +
					"de façcons à battre vos armées !-" +
					"-" +
					"Bonne chance !-" +
					"-" +
					"Tuer tout avant d'être dépasser !";
			break;
		case 11:	// Tes poursuivis
			argentJ1=10;
			
			for(int i=0;i<5;i++)
				for(int k=0;k<5;k++){
					arrayMonsterOwner.add(new Monster(100+40*k,100+40*i,1));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,2));
					arrayMonsterOwner.add(new Monster(190+40*k,350+40*i,4));
					arrayMonsterOwner.add(new Monster(300+40*k,250+40*i,3));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,100+40*i,1));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1100+40*i,1));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,700+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,400+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,900+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,1500+40*i,5));
					arrayMonsterEnnemy.add(new Monster(500+40*k,1600+40*i,4));
					arrayMonsterEnnemy.add(new Monster(300+40*k,1400+40*i,5));
					arrayMonsterEnnemy.add(new Monster(1800+40*k,1400+40*i,3));
					
					arrayMonsterEnnemy.add(new Monster(2000+40*k,1600+40*i,5));
					arrayMonsterEnnemy.add(new Monster(2000+40*k,400+40*i,4));
					arrayMonsterEnnemy.add(new Monster(2000+40*k,1000+40*i,5));
				}
			
			Monster.nbreUnite = 100;
			Monster.nbreNain = 0;
			Monster.nbreFantassin = 25;
			Monster.nbreMinautaure = 25;
			Monster.nbreArcher = 25;
			Monster.nbreCentaure = 25;
			Monster.nbreMage = 0;
			
			objectifs = "Les unités ennemies vous cherchent !-" +
					"-" +
					"Bonne chance !-" +
					"-" +
					"Tuer tout avant d'être dépasser !";
			break;
		case 12:	// massacre 2
			argentJ1=10;
			sangActive=true;
			
			for(int i=0;i<5;i++)
				for(int k=0;k<5;k++){
					arrayMonsterOwner.add(new Monster(100+40*k,100+40*i,0));
					arrayMonsterOwner.add(new Monster(100+40*k,350+40*i,2));
					arrayMonsterOwner.add(new Monster(400+40*k,350+40*i,4));
					arrayMonsterOwner.add(new Monster(100+40*k,100+40*i,2));
					arrayMonsterOwner.add(new Monster(2000+40*k,2000+40*i,4));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,100+40*i,1));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1100+40*i,1));
					
					arrayMonsterEnnemy.add(new Monster(1100+40*k,700+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,400+40*i,2));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,900+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1600+40*k,1500+40*i,5));
					arrayMonsterEnnemy.add(new Monster(100+40*k,1600+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1300+40*k,900+40*i,4));
					arrayMonsterEnnemy.add(new Monster(1100+40*k,1500+40*i,5));
					arrayMonsterEnnemy.add(new Monster(500+40*k,1600+40*i,4));
					arrayMonsterEnnemy.add(new Monster(300+40*k,1400+40*i,5));
					arrayMonsterEnnemy.add(new Monster(1800+40*k,1400+40*i,3));
					
					arrayMonsterEnnemy.add(new Monster(2000+40*k,1600+40*i,5));
					arrayMonsterEnnemy.add(new Monster(2000+40*k,400+40*i,4));
					arrayMonsterEnnemy.add(new Monster(2000+40*k,1000+40*i,5));
				}
			
			Monster.nbreUnite = 125;
			Monster.nbreNain = 25;
			Monster.nbreFantassin = 0;
			Monster.nbreMinautaure = 50;
			Monster.nbreArcher = 0;
			Monster.nbreCentaure = 50;
			Monster.nbreMage = 0;
			
			objectifs = "Des unités ennemies apparaissent partout-" +
					"En nombre plus important !-" +
					"-" +
					"Dépêchez vous !" +
					"-" +
					"-" +
					"Tuer tout avant d'être dépasser !";
			break;
		}
	}
	
	/** Va vérifier les unités du joueur et celle de l'ennemie et agir selon le mode en cours (uniquement Solo) */
	private void gererDifferentModeSolo(){
		switch(modSoloDiff){
		case 6:	// Armée de nains -> que des nains chez J1
			for(Monster o : arrayMonsterOwner)
				if(o != null)
					if(o.getMetamor() != 0){
						//int tempVie = o.getVie();
						switch(o.getMetamor()){
						case 1:
							Monster.nbreFantassin--;
							break;
						case 2:
							Monster.nbreMinautaure--;
							break;
						case 3:
							Monster.nbreArcher--;
							break;
						case 4:
							Monster.nbreCentaure--;
							break;
						case 5:
							Monster.nbreMage--;
							break;
						}
						o.setMetamor(0);
						//o.setVie(tempVie);
						Monster.nbreNain++;
					}
			if(compteurSolo >= 60000){ // 1 min
				for(int i=0;i<5;i++)
					for(int k=0;k<5;k++){
						arrayMonsterEnnemy.add(new Monster(1+(int)Math.random()*2300+40*k,400+40*i,2));
						arrayMonsterEnnemy.add(new Monster(1+(int)Math.random()*1600+40*k,900+40*i,4));
					}
				compteurSolo=0;
			}else
				compteurSolo++;
			break;
		case 7:	// Attaque à distance
			// Si j'autorise les nains, alors j'utilise compteurSolo pour spawn des ennemies
			for(Monster o : arrayMonsterOwner)
				if(o != null)
					if(o.getMetamor() != 3 && o.getMetamor() != 4 && o.getMetamor() != 5){
						switch(o.getMetamor()){
						case 0:
							Monster.nbreNain--;
							break;
						case 1:
							Monster.nbreFantassin--;
							break;
						case 2:
							Monster.nbreMinautaure--;
							break;
						}
						o.setMetamor(3);
						Monster.nbreArcher++;
					}
			break;
		case 8:	// Tu cours moins vite
			if(compteurSolo >= 30){	// Donc le joueur a un petit temps où la vitesse est normala
				compteurSolo++;
				double dX, dY;
				for(Monster o : arrayMonsterOwner)
					if(o != null){
						dX = o.getDeltaX();
						dY = o.getDeltaY();
						if(dX > 1 || dY > 1){
							switch(o.getMetamor()){
							case 0:
								o.setDeltaX(dX/(4+Math.random()*2));
								o.setDeltaY(dY/(4+Math.random()*2));
								break;
							case 1:
								o.setDeltaX(dX/(3+Math.random()*4));
								o.setDeltaY(dY/(3+Math.random()*4));
								break;
							case 2:
								o.setDeltaX(dX/(6+Math.random()*5));
								o.setDeltaY(dY/(6+Math.random()*5));
								break;
							case 3:
								o.setDeltaX(dX/(5+Math.random()*5));
								o.setDeltaY(dY/(5+Math.random()*5));
								break;
							case 4:
								o.setDeltaX(dX/(7+Math.random()*3));
								o.setDeltaY(dY/(7+Math.random()*3));
								break;
							case 5:
								o.setDeltaX(dX/(1+Math.random()));
								o.setDeltaY(dY/(1+Math.random()));
								break;
							}
						}
					}
				compteurSolo = 0;
			}else
				compteurSolo++;
			break;
		case 9:	// Massacre 1
			if(compteurSolo >= 1000){	// Donc le joueur a un petit temps pour économiser et aller tuer les unités ennemies
				compteurSolo = 0;
				for(int i=0;i<5;i++){
						arrayMonsterEnnemy.add(new Monster(500+40*(int)Math.random()*i,100+40*i,1));
						arrayMonsterEnnemy.add(new Monster(1000+40*(int)Math.random()*i,900+40*i,2));
						arrayMonsterEnnemy.add(new Monster(1600+40*i,1500+40*(int)Math.random()*i,3));
						arrayMonsterEnnemy.add(new Monster(2000+40*(int)Math.random()*i,1600+40*i,4));
						arrayMonsterEnnemy.add(new Monster(800+40*(int)Math.random()*i,500+40*i,4));
				}
			}else
				compteurSolo++;
			break;
		case 10:	// des contres
			int delta1;
			for(Monster o : arrayMonsterEnnemy)
				if(o != null){
					for(Monster v : arrayMonsterOwner)
						if(v!=null){
							delta1 = (int)Math.sqrt((Math.pow(Math.abs(o.getX() - v.getX()), 2)+ Math.pow(Math.abs(o.getY() - v.getY()), 2)));
	    					if(delta1 < DIST_ATTAQ_DIST ){	// l'ordi triche pour les transformations
	    						switch(v.getMetamor()){
	    						case 1:
	    							o.setMetamor(2);
	    							break;
	    						case 2:
	    							o.setMetamor(5);
	    							break;
	    						case 3:
	    							o.setMetamor(2);
	    							break;
	    						case 4:
	    							o.setMetamor(2);
	    							break;
	    						case 5:
	    							o.setMetamor(1);
	    							break;
	    						}
	    					}else
	    						continue;
						}
				}
			
			break;
		case 11:	// tes poursuivis
			for(Monster o : arrayMonsterOwner)
				if(o!=null){
					for(Monster v : arrayMonsterEnnemy)
						v.changeDestination((int)o.getX(), (int)o.getY());
					break;
				}
			break;
		case 12:	// Une massacre 2
			if(compteurSolo >= 1200){	// Donc le joueur a un petit temps pour économiser et aller tuer les unités ennemies
				compteurSolo = 0;
				for(int i=0;i<5;i++){
					for(int k=0;k<5;k++){
						arrayMonsterEnnemy.add(new Monster(500+40*(int)Math.random()*k,100+40*i,1));
						arrayMonsterEnnemy.add(new Monster(1000+40*(int)Math.random()*k,900+40*i,2));
						arrayMonsterEnnemy.add(new Monster(1600+40*k,1500+40*(int)Math.random()*i,3));
						arrayMonsterEnnemy.add(new Monster(2000+40*(int)Math.random()*k,1600+40*i,4));
						arrayMonsterEnnemy.add(new Monster(800+40*(int)Math.random()*k,500+40*i,4));
					}
				}
			}else
				compteurSolo++;
			break;
		}
	}

	/** 
	 * Joueur 1 a ses unités dans 1 et J2 dans 2. Si je transforme en FFA alors faudra mettre les unités du joueur actuelle dans Owner
	 * @return L'argent obtenue grâce à la position de l'unité
	 *  */
	private int nainBonnePos(Monster o, int arrayQui){
		// Si joueur 1, zone 1 == 1, zone 3 == 4
		//		arrayQui == 1 -> arrayMonsterOwner
		//
		// Si joueur 2, zone 1 == 4, zone 3 == 1
		//		arrayQui == 2 -> arrayMonsterEnnemy
		//if(joueur==1){
			if(arrayQui == 1)
				switch(zone((int)o.getX(), (int)o.getY())){
				case 0:
					return	0;
				case 1:
					return	1;
				case 2:
					return	2;
				case 3:
					return	4;
				default:
					return 0;
				}
			if(arrayQui == 2)
				switch(zone((int)o.getX(), (int)o.getY())){
				case 0:
					return	0;
				case 1:
					return	4;
				case 2:
					return	2;
				case 3:
					return	1;
				default:
					return 0;
				}
			//System.out.println(zone((int)o.getX(), (int)o.getY()));
	//	}else{
			
		//}
		
		// Return par défaut (jamais atteint normalement)
		return 1;
	}
	
	/** 
	 * Donne un numero au zone suivant où se trouve l'unité
	 * @return numero zone
	 *  */
	public int zone(int x, int y){
		/*
		 * Zone 1 Haut gauche
		 * Zone 2 milieu
		 * Zone 3 bas droit
		 */
		if(x >= 10 && x <= 510 && y >= 10 && y <= 510)
			return 1;
		else if(x >= 1200 && x <= 1700 && y >= 1050 && y <= 1550)
			return 2;
		else if(x >= 2500 && x <= 3000 && y >= 2000 && y <= 2500)
			return 3;
			
			return 0; // Par defaut, ne reçois rien
	}

	
	
	// ====================
	// ==    FIN GAME    ==
	// ====================
	/** Seulement pour le multi. Voir si la partie est finie, max de min de jeu ou plus d'unité ou ...
	 * Si le joueur 1 quitte la partie en fermant le jeu, la partie n'est pas enregistrer. Si J2 part c enregistrer puisque J1 jouera seul et tuera toutes les unités
	 * Si J1 quitte avec Quitter alors elle est enregistrer (mais ailleur qu'ici)
	 *  */
	private void isPartieFinie(){	// Ne fait pas différence entre une partie qui a été jouer en mettant (pseudo et MDP) ou juste le (pseudo)
		if(!gameSolo){
			if(minTimer >= MAX_MIN_PARTIE){	// Partie finie avec le temps
				if(pointsJ1 > pointsJ2){
					gereInfoFinGame(1);
				}else{
					gereInfoFinGame(2);
				}
				_clientReceiver.sendAnyMsg("all"+"gameFini");	// envoie 'info à tous. puis clientReceiver fera ce qui est nécessaire
			}
			// Partie fini car le joueur n'a plus d'unité même s'il a encore de l'argent
			if(arrayMonsterOwner.isEmpty()){
				gereInfoFinGame(2);
				_clientReceiver.sendAnyMsg("all"+"gameFini");
			}
			if(arrayMonsterEnnemy.isEmpty()){
				gereInfoFinGame(1);
				_clientReceiver.sendAnyMsg("all"+"gameFini");
			}
			
			// Test pour debug
			/*if(arrayMonsterOwner.contains(null))
				System.out.println("Owner contient null");
			if(arrayMonsterEnnemy.contains(null))
				System.out.println("Ennemy contient null");*/
		}else{
			if(modSoloDiff==1){
				if(argentJ1 >= 200){
					pause=true;
					_lancement.mettreFinPartie();
				}
			}else if(modSoloDiff>=2 && modSoloDiff<=12){
				if(arrayMonsterEnnemy.isEmpty()){
					pause=true;
					_lancement.mettreFinPartie();
				}else if(arrayMonsterOwner.isEmpty()){
					pause=true;
					_lancement.mettreFinPartie();
				}
			}else{
				switch(modSoloDiff){	// Les défis mais pas sûr
				
				}
			}
		}
			
	}
	private void gereInfoFinGame(int joueurGagne){
		//String reponse="";
		try {
				// ******************
				// pseudo1 est toujours le GAGNANT,points1 est pour le gagnant
				// ******************
			pause=true;
			if(joueur == 1){
				envoyerInfoVarFin();
				if(gameRanked){
					if(joueurGagne == 1)
						System.out.println(get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=partieFinie&pseudo1="+_clientReceiver.pseudoJ1+"&points1="+pointsJ1+"&pseudo2="+_clientReceiver.pseudoJ2+"&points2="+pointsJ2));			
					else
						System.out.println(get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=partieFinie&pseudo1="+_clientReceiver.pseudoJ2+"&points1="+pointsJ2+"&pseudo2="+_clientReceiver.pseudoJ1+"&points2="+pointsJ1));	
				}
			}

		} catch (IOException e) {
			//errorMsg="Impossible d'accéder au serveur\nde vérification";
			//errorON=true;
			e.printStackTrace();
		} catch (Exception e) {
			// Voir si c'est vraiment inconnu
		//	errorMsg="         Erreur inconnu";
		//	errorON=true;
			e.printStackTrace();
		}
		//reponse=null;
	}
	
	/** Envoie de J1 vers J2. Envoie les variables de stats de partie car J2 peut ne pas avoir les même (souvent le cas, surtout l'or et le temps -> si l'ordi de J1 ou J2 est plus lent que l'autre) */
	private void envoyerInfoVarFin(){
		// updateVarFinargentTotalJ1-argentTotalJ2-unitTotalCreerJ1-unitTotalCreerJ2-unitTotalTuerJ1-unitTotalTuerJ2-minute-seconde
		_clientReceiver.sendAnyMsg("all"+"updateVarFin"+argentTotalJ1+"-"+argentTotalJ2+"-"+unitTotalCreerJ1+"-"+unitTotalCreerJ2+"-"+unitTotalTuerJ1+"-"+unitTotalTuerJ2+"-"+minTimer+"-"+secTimer);
	}
	
	/** Cherche si l'objet se trouve dans l'un des array et donne le point à l'autre */
	private void ajouterTotalTuer(Monster w){
		// Statistique de fin de jeu
		if(arrayMonsterOwner.contains(w))
			unitTotalTuerJ2++;
		else if(arrayMonsterEnnemy.contains(w))
			unitTotalTuerJ1++;
	}
	
	public static int getCOST_NAIN() {
		return COST_NAIN;
	}
	public static int getDIST_ATTAQ_CORPS() {
		return DIST_ATTAQ_CORPS;
	}
	public static int getDIST_ATTAQ_DIST() {
		return DIST_ATTAQ_DIST;
	}
	public void setPause(boolean a){
		this.pause = a;
	}
	public void setGameRanked(boolean a){
		this.gameRanked = a;
	}
	public boolean getGameRanked(){
		return gameRanked;
	}
	public boolean getPause(){
		return pause;
	}
	/**
	    * Avoir ce que contient la page d'une URL
	    * @param url of the website
	    * @throws IOException Internet is off, or couldn't get the website.
	    * @throws Exception Je sais pas
	    * @return Response from the site
	    */
	   private String get(String url) throws IOException, Exception{	// Les erreurs sont gérer à l'appel de la fonction pour personnalisé le msg d'erreur  
		   String source ="";
		   URL urlObject = new URL(url);
		   URLConnection urlCon = urlObject.openConnection();
		   BufferedReader in2 = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
		   String inputLine2;
		   
		   while ((inputLine2 = in2.readLine()) != null)
			   source +=inputLine2;
		   try{
			   in2.close();
		   }catch(Exception e){}
	   return source;
	   }
	
}
