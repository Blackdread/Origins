package Origins;
import java.awt.Font;
import java.net.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.io.*;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
//import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;
import org.newdawn.slick.Color;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.TextField;


@SuppressWarnings("deprecation")
public class Lancement extends BasicGame {
	
	static AppGameContainer app;
	
	
	// Changer l'adresse. Y a déjà le / à la fin
	// http://localhost:8888		http://origins.dev.free.fr		http://08.innovgame.efrei.fr		http://innovgame.efrei.fr/2012/Origins
	static String adresseSiteServeur = "http://08.innovgame.efrei.fr";
	public static final int portUtilise = 18000;
	
	// Accueil et logo
	private Image accueil, logo;
	private boolean accueilON=true, logoON=false;
	
	// Login
	private Image login,logOK,logOKHover,logQuit,logQuitHover;
	private boolean logOKHoverON=false,logQuitHoverON=false;
	private boolean loginON=false;
	
	// Menu
	private Image menu, menuHoverJouer, menuHoverLadder, menuHoverOption, menuHoverCredits, menuHoverQuitter;
	private Image menuJouerChoix, menuJouerChoixSoloHover, menuJouerChoixMultiHover;
	private boolean menuON=false;
	private boolean menuJouerChoixSoloHoverON=false, menuJouerChoixMultiHoverON=false;
	private boolean menuHoverJouerON=false, menuHoverLadderON=false, menuHoverOptionON=false, menuHoverCreditsON=false, menuHoverQuitterON=false;
	
	// Menu Multi
	private Image menuMulti, menuMultiRetourHover, menuMultiCreerServeurHover, menuMultiIPHover, menuMultiRafraichirHover;
	private Image ipDirect,ipDirectAnnulerHover,ipDirectConnecterHover, partieHover;
	private boolean menuMultiON=false;
	private boolean menuMultiRetourHoverON=false, menuMultiCreerServeurHoverON=false, menuMultiIPHoverON=false, menuMultiRafraichirHoverON=false;
	
	private String saveListeServer="";
	
	// Menu Multi IpDirect
	private boolean ipDirectAnnulerHoverON=false,ipDirectConnecterHoverON=false;
	private boolean ipDirectON=false;
	
	// Menu Multi Serveur
	private Image menuMultiServeurCreer,menuMultiServeurCreerPartieHover,menuMultiServeurCreerRetourHover,menuMultiSeveurCreerEnvoyerHover;
	private boolean menuMultiServeurCreerON=false;
	private boolean menuMultiServeurCreerPartieHoverON=false,menuMultiServeurCreerRetourHoverON=false,menuMultiSeveurCreerEnvoyerHoverON=false;
	
	// Menu Ladder
	private Image ladder, ladderRetour, ladderRetourHover;
	private boolean menuLadderON=false;
	private boolean menuLadderRetourHoverON=false;
	
	private String saveListeLadder="";
	
	// Menu Credits
	private boolean creditsON=false;
	
	// FIN GAME
	private boolean finGameON=false, finGameRetourHoverON=false;
	private Image finGame, finGameRetourHover;
	
	// Chargement
	private boolean chargementON=false;
	private Image chargement;
	
	// ******************
	// InGame choix et Option
	private Image inGameChoix, inGameOption;
	private Image inGameOptionOver, inGameChoixOptionOver, inGameChoixRetourOver, inGameChoixQuitterOver;
	private Image optionPlus, optionMoins, choixOui;
	private boolean inGameChoixON=false, inGameOptionON=false, inGameOptionRetourON=false;
	private boolean inGameOptionOverON=false, inGameChoixOptionOverON=false, inGameChoixRetourOverON=false, inGameChoixQuitterOverON=false;
	private boolean optionPlusON1=false, optionPlusON2=false, optionMoinsON1=false, optionMoinsON2=false;
	
	
	// TAILLE ECRAN
	// Avoir le jeu en plein écran
	private boolean ecranTaille1280=false,ecranTaille1280x800=false, ecranTaille1366=false, ecranTaille1440=false;
	private Image bord;
	
	private int yPleinEcran=750;	// change en fonction de la taille en hauteur, car dans Slick 2d  0,0 se trouve en bas à gauche
	
	// ******************
	// Menu Solo		POUR LE RETOUR, J'ai REUTILISE UN BOOLEAN
	private Image menuSolo;
	private boolean menuSoloON=false;
	
	// *******************
	// LES SONS
	// *******************
	Music musSwirlOfDust;
	Sound arc,explosion,hacheOs,epee1,epee2,epee3,epee4;
	
	
	// SANG
	private Image sang, sang0,sang1,sang2,sang3,sang4;
	private int TEMPS_SANG_RENDER = 12;
	
	/*
	 *		Game SOLO 
	 */
	private boolean gameSolo=false, objectifHoverON=false, objectifON=false;
	private Image objectif, objectifHover, objectifZone;
	 /*private ArrayList<String> arrayServerTextInGameChatSolo = new ArrayList<String>();
	 private ArrayList<Long> arrayServerTextInGameChatTimeSolo = new ArrayList<Long>();*/	//MIS dans Game
	
	/*
	 *		Game multi-joueur
	 */
	private boolean gameON=false;
	private boolean gameAttaON=false, gameStopON=false, gameTenirON=false, gameEtoileON=false, gameRectON=false;
	private boolean gameNainON=false, gameFantaON=false, gameMinauON=false, gameArchON=false, gameCentON=false, gameMageON=false;
	private Image jeu, map,minimap, barreSelec, units, unitSelec, unitSelecR, jeuGroupUnit;
	private Image mapRepeat, mineOr; // Dû au fait que la map est trop grande et est mal chargée des fois
	private Image nain, fantasin, minautaure,archer, centaure, mage;
	private Image nain2, fantasin2, minautaure2,archer2, centaure2, mage2;	// Pour ennemie
	private Image ico, icoNain, icoFanta, icoMinau, icoArcher, icoCent, icoMage;
	private Image tire, tireFl;//, tireArba;
	private Image surbrillanceIcon;
	private Image barreVieG, barreVieD, barreVieM;
	private Image barreManaG, barreManaD, barreManaM;
	private Image carreR, carreB;	// pour unit mini map
	private Image chatInGame;
	
	private Image error, errorHover;
	private String errorMsg = "";	// 35 CARACTERES MAXI PAR LIGNE
	private boolean errorON=false,errorHoverON=false;

	private static int FENLARG = 1100,FENHAUT = 750;
	private static int XMAXUNIT=2990,YMAXUNIT=2490;
	
	private static int TEMPS_DOUBLE_KEY = 400;	// Pour double click sur les groupe de selection avec le clavier
	
	// BROUILLARD DE GUERRE ET 2 POUR MINIMAP
	private static Image fog, fog2;
	static Graphics gFog = new Graphics(), gFog2 = new Graphics();
	
	
	private int secoTime = 0;
	
	@SuppressWarnings("unused")
	private Input input;
	
	private int joueur, rang=0, points=0;
	private String pseudo="";
	
	// OPTION ET NETWORK
	private boolean networkON, raccourcisON=true, qwertyON=false, affichVieMaInGameON=false, musicON=true, effetON=true, doubleClick=false, antiBugMap=true;	// qwertyON, false => azerty
		
	// COMMUNICATION
	private String messageJeu="";
	
	int deltaX = 0, deltaY = 0;
	int lastX, lastY, m=0;
	int countMonsterEtoile=0, posUnitStrategy=2;	// posUnitStrategy=1 pour etoile sinon c'est rectangle
	boolean moveAttackSav = false; // Sauvegarde de l'appuie de la touche attaquer
	
	int multiSensibilite=1, multiSensibilite2=1;	// Via les fleches puis souris sur les bords
	String ipOptionSaveTextField = "";
	
	
	long lastClick=getTime();
	long lastKeyPressed=getTime();	// Pour le double clique avec les groupe de selection
	int lastKeyPressedNum;
	
	//	**********
	//	*  FONT  *
	//	**********
	Font raccourFont, jeuFont;
	
	
	Graphics _g;	// Pour mettre g en globale
	boolean doOnce=true;	// Pour avoir le graphics et c'est fait une fois
	
	// Variable pour afficher plus facilement les untiés etc
	// et eviter de devoir passer par ClientReceiver
	Game game;
	
	
	/*	
	 * 		SERVEUR
	 */
	Serveur serveur;
	
	/*
	 * 		Client, SOCKET
	 */
	Socket socket, socketOwner;
	
	PrintWriter output;
	BufferedReader in;
	ClientReceiver clientReceiver;
	
	ArrayList<String> arrayServerText = new ArrayList<String>();	// Messages dans le lobby avant la partie
	
	// Fonts
	TrueTypeFont fontLogin, fontRaccour, fontJeu;
	
	// TextField
	TextField textLogin, textPass;
	TextField textIP;
	TextField textServerChat, textInGameChat;
	
	TextField ipOptionTextField;
	
	private boolean textInGameChatON=false;	// Afficher ou pas le textField
	
	public Lancement() {
        super("Origins");
    }
	
	public void init(GameContainer gc) throws SlickException {
        // Initialisation / Chargement des textures
    	try{  		
    		String [] refs ={"images/ico16.png","images/ico32.png"};
            gc.setIcons(refs);
            gc.setIcon("images/ico16.png");
            
            //app.setIcons(refs);
            //app.setIcon("images/ico16.png");
    		
    		fog = new Image(FENLARG,FENHAUT);
	    	gFog = fog.getGraphics();
	    	fog2 = new Image(192,128);
	    	gFog2 = fog2.getGraphics();
	    	
	    	accueil = new Image("images/accueil.png");
			logo = new Image("images/logo.png");
			chargement = new Image("images/chargement.png");
			
			login = new Image("images/login/login.png");
			logOK = new Image("images/login/logOK.png");
			logOKHover = new Image("images/login/logOKHover.png");
			logQuit = new Image("images/login/logQuit.png");
			logQuitHover = new Image("images/login/logQuitHover.png");
			
			menu = new Image("images/menu/menu.png");
			menuHoverJouer = new Image("images/menu/menuHoverJouer.png");
			menuHoverLadder = new Image("images/menu/menuHoverLadder.png");
			menuHoverOption = new Image("images/menu/menuHoverOption.png");
			menuHoverCredits = new Image("images/menu/menuHoverCredits.png");
			menuHoverQuitter = new Image("images/menu/menuHoverQuitter.png");
			menuJouerChoixSoloHover = new Image("images/menu/menuJouerChoixSoloHover.png");
			menuJouerChoixMultiHover = new Image("images/menu/menuJouerChoixMultiHover.png");
			menuJouerChoix = new Image("images/menu/menuJouerChoix.png");
			
			menuMulti = new Image("images/menu/multi/menuMulti.png");
			menuMultiRetourHover = new Image("images/menu/multi/menuMultiRetourHover.png");
			menuMultiCreerServeurHover = new Image("images/menu/multi/menuMultiCreerServeurHover.png");
			menuMultiIPHover = new Image("images/menu/multi/menuMultiIPHover.png");
			//menuMultiSeparation = new Image("images/menu/multi/menuMultiSeparation.png");
			//menuMultiRejoindre = new Image("images/menu/multi/menuMultiRejoindre.png");
			//menuMultiRejoindreHover = new Image("images/menu/multi/menuMultiRejoindreHover.png");
			menuMultiRafraichirHover = new Image("images/menu/multi/menuMultiRafraichirHover.png");
			partieHover = new Image("images/menu/partieHover.png");
			
			ipDirect = new Image("images/menu/multi/ip_direct/ipDirect.png");
			ipDirectAnnulerHover = new Image("images/menu/multi/ip_direct/ipDirectAnnulerHover.png");
			ipDirectConnecterHover = new Image("images/menu/multi/ip_direct/ipDirectConnecterHover.png");
			
			menuMultiServeurCreer = new Image("images/menu/multi/serveur/menuMultiServeurCreer.png");
			menuMultiServeurCreerPartieHover = new Image("images/menu/multi/serveur/menuMultiServeurCreerPartieHover.png");
			menuMultiServeurCreerRetourHover = new Image("images/menu/multi/serveur/menuMultiServeurCreerRetourHover.png");
			menuMultiSeveurCreerEnvoyerHover = new Image("images/menu/multi/serveur/menuMultiSeveurCreerEnvoyerHover.png");
			
			ladder = new Image("images/menu/ladder/ladder.png");
			ladderRetour = new Image("images/menu/ladder/menuMultiLadderRetour.png");
			ladderRetourHover = new Image("images/menu/ladder/menuMultiLadderRetourHover.png");
			
			error = new Image("images/errors/errorFull.png");
			errorHover = new Image("images/errors/errorFullHover.png");
			
	    	// ***************
	    	// *	LE JEU	 *
	    	// ***************
			jeu = new Image("images/jeu/jeu.png");
			inGameOptionOver = new Image("images/jeu/optionJeu/optionHover.png");
			map = new Image("images/map/map.png");
			mapRepeat = new Image("images/map/mapRepeat2.png");
			mineOr = new Image("images/map/mine2.png");
			minimap = new Image("images/map/minimap.png");
			
			objectif = new Image("images/jeu/objectif.png");
			objectifHover = new Image("images/jeu/objectifHover.png");
			objectifZone = new Image("images/jeu/objectifZone.png");
			
			jeuGroupUnit = new Image("images/jeu/jeuGroupUnit.png");
			 
			barreSelec = new Image("images/jeu/barreSelec.png");
			unitSelec  = new Image("images/jeu/selected.png");
			unitSelecR = new Image("images/jeu/selectedR.png");
			 
			units = new Image("images/jeu/unite/nainB.png");
			nain = new Image("images/jeu/unite/nainB.png");
			fantasin = new Image("images/jeu/unite/fantassinB.png");
			minautaure = new Image("images/jeu/unite/minautaureB.png");
			archer = new Image("images/jeu/unite/archerB.png");
			centaure = new Image("images/jeu/unite/centaureB.png");
			mage = new Image("images/jeu/unite/mageB.png");
			
			nain2 = new Image("images/jeu/unite/nainR.png");
			fantasin2 = new Image("images/jeu/unite/fantassinR.png");
			minautaure2 = new Image("images/jeu/unite/minautaureR.png");
			archer2 = new Image("images/jeu/unite/archerR.png");
			centaure2 = new Image("images/jeu/unite/centaureR.png");
			mage2 = new Image("images/jeu/unite/mageR.png");
			 
			ico = new Image("images/jeu/miniature/miniatureNain2.png");
			icoNain = new Image("images/jeu/miniature/miniatureNain2.png");
			icoFanta = new Image("images/jeu/miniature/miniatureFantassin2.png");
			icoMinau = new Image("images/jeu/miniature/miniatureMinautaure2.png");
			icoArcher = new Image("images/jeu/miniature/miniatureArcher2.png");
			icoCent = new Image("images/jeu/miniature/miniatureCentaure2.png");
			icoMage = new Image("images/jeu/miniature/miniatureMage2.png");
			
			// SANG
			sang0 = new Image("images/jeu/sang/sang0.png");
			sang1 = new Image("images/jeu/sang/sang1.png");
			sang2 = new Image("images/jeu/sang/sang2.png");
			sang3 = new Image("images/jeu/sang/sang3.png");
			sang4 = new Image("images/jeu/sang/sang4.png");
			
			// Chat In Game
			chatInGame = new Image("images/jeu/chatInGame.png");
			inGameChoix = new Image("images/jeu/optionJeu/optionJeu.png");
			inGameOption = new Image("images/menu/option/option.png");		// OPTION le meme pour Ingame et Menu
			
			// OPTION
			optionPlus = new Image("images/menu/option/optionPlus.png");	
			optionMoins = new Image("images/menu/option/optionMoins.png");
			choixOui = new Image("images/menu/option/choixOui.png");
			
			inGameChoixRetourOver = new Image("images/jeu/optionJeu/optionHoverRetour.png");
			inGameChoixOptionOver = new Image("images/jeu/optionJeu/optionHoverOption.png");
			inGameChoixQuitterOver = new Image("images/jeu/optionJeu/optionHoverQuitter.png");
			
			// FIN DE GAME
			finGame = new Image("images/menu/finJeu/finGame.png");
			finGameRetourHover = new Image("images/menu/finJeu/retourMultiHover.png");
			
			
			// SOLO
			menuSolo = new Image("images/menu/solo/solo.png");
			
			
			surbrillanceIcon = new Image("images/jeu/surbrillanceIcon.png");
			 
			// PLEIN ECRAN
			bord = new Image("images/jeu/bord.png");
			
			// JEU
			tire = new Image("images/jeu/tire3.png");
			tireFl = new Image("images/jeu/tireFl.png");
			
			carreR = new Image("images/jeu/unite/carreR.png");
			carreB = new Image("images/jeu/unite/carreB.png");
			
			barreVieG = new Image("images/jeu/barreVieG.png");
			barreVieM = new Image("images/jeu/barreVieM.png");
			barreVieD = new Image("images/jeu/barreVieD.png");
			
			barreManaG = new Image("images/jeu/barreManaG.png");
			barreManaM = new Image("images/jeu/barreManaM.png");
			barreManaD = new Image("images/jeu/barreManaD.png");
			
			raccourFont = new Font("Times New Roman", Font.PLAIN, 13);
			fontRaccour = new TrueTypeFont(raccourFont, false);
			jeuFont = new Font("Times New Roman", Font.BOLD, 14);
			fontJeu = new TrueTypeFont(jeuFont, false);
			Font awtFont = new Font("Times New Roman", Font.PLAIN, 18);
			
			
			fontLogin = new TrueTypeFont(awtFont, false);
    		textLogin = new TextField(gc, fontLogin ,410, 317, 220, 40);
    		textPass = new TextField(gc, fontLogin ,410, 375, 220, 40);
    		textLogin.setBackgroundColor(null);
    		textLogin.setBorderColor(null);
    		textLogin.setMaxLength(15);
    		textPass.setBackgroundColor(null);
    		textPass.setBorderColor(null);
    		textPass.setMaxLength(15);
    		
    		textIP = new TextField(gc, fontLogin ,440, 280, 195, 40);
    		textIP.setBackgroundColor(null);
    		textIP.setBorderColor(null);
    		textIP.setMaxLength(20);
    		
    		textServerChat = new TextField(gc, fontLogin ,560, 631, 380, 40);
    		textServerChat.setBackgroundColor(null);
    		textServerChat.setBorderColor(null);
    		textServerChat.setMaxLength(39);
    		
    		
    		// In game
    		textInGameChat = new TextField(gc, fontLogin ,307, 610, 435, 18);
    		textInGameChat.setBackgroundColor(null);
    		textInGameChat.setBorderColor(null);
    		textInGameChat.setMaxLength(55);
    		
    		
    		// OPTION
    		ipOptionTextField= new TextField(gc, fontLogin ,785, 508, 165, 18);
    		ipOptionTextField.setBackgroundColor(null);
    		ipOptionTextField.setBorderColor(null);
    		ipOptionTextField.setMaxLength(18);
    		
    		
    		
    		// Les SONS
    		try{
    		arc = new Sound("sons/effets/arc2.wav");
    		explosion = new Sound("sons/effets/explosion2.wav");
    		hacheOs = new Sound("sons/effets/hacheOs.wav");
    		epee1 = new Sound("sons/effets/epee1.wav");
    		epee2 = new Sound("sons/effets/epee2.wav");
    		epee3 = new Sound("sons/effets/epee3.wav");
    		epee4 = new Sound("sons/effets/epee4.wav");
    		
    		musSwirlOfDust = new Music("sons/SwirlofDust.wav");	// pas forcément mis dans le JAR
    		}catch(Exception e){e.printStackTrace();}
    		
    	} catch (RuntimeException e) {
			e.printStackTrace();
			//System.out.println("Erreur chargement images");
			Display.destroy();
			System.exit(0);
		}
    	
		initGLetAutre(gc);
    }
    
    public void initGLetAutre(GameContainer gc) throws SlickException {
    	gc.setMinimumLogicUpdateInterval(60);
		gc.setMaximumLogicUpdateInterval(60);
		gc.setUpdateOnlyWhenVisible(false);
		gc.setAlwaysRender(true);
    	
		gc.setShowFPS(false);
		
		input = gc.getInput();
		
		arrayServerText.add("Bienvenue dans le jeu");
		//gc.getInput().addMouseListener(listenerMouse);
    }
    
    public void update(GameContainer gc, int delta) throws SlickException {
       // Mise à jour de la logique du jeu
       // Déplacement des personnages, gestion des collisions...

    //	debPhrs(""+delta);
    	secoTime++;
    	//secoTime+=10; // Juste pour les test
    	if(secoTime == 110){
    		accueilON=false;
    		logoON=true;
    	}
    	if(secoTime == 180){
    		logoON=false;
    		loginON=true;
    		textLogin.setFocus(true);
    	}

    	if(menuMultiServeurCreerON){
    		try{
    			if(arrayServerText.get(arrayServerText.size()-1) != null){
    				if(arrayServerText.get(arrayServerText.size()-1) != clientReceiver.getLastMsgCreer())
						arrayServerText.add(clientReceiver.getLastMsgCreer());
    			}
    		}catch(ArrayIndexOutOfBoundsException e){ }
    	}
    	
    	// ***************
    	// *	LE JEU	 *
    	// ***************
    	if(gameON){
    		gc.setMinimumLogicUpdateInterval(10);
    		gc.setMaximumLogicUpdateInterval(10);
    		
	        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
	        	moveCamera(0,1);
	        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
	        	moveCamera(0,-1);
	        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
	        	moveCamera(-1,0);
	        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
	        	moveCamera(1,0);
	        
	        // Pour la souris
	        moveCamera(0,0);
	        
	        selection();
	        
	        // ***
	        // SONS
	        jouerSons();
	        // ***
    	}
    	
    }
    
    public void render(GameContainer gc, Graphics g) throws SlickException {
    	if(doOnce){
    		_g=g;
    		doOnce=false;
    		try{	// La musique ne sera pas forcément mise
    			if(musSwirlOfDust!=null)
    				musSwirlOfDust.loop();
    		}catch(Exception e){}
    	}
    	if(accueilON || creditsON)
    		g.drawImage(accueil, 0, 0);
    	if(creditsON)
    		if(menuLadderRetourHoverON)		//	Réutilisation de variable
    			g.drawImage(ladderRetourHover,200,670);
    		else
    			g.drawImage(ladderRetour,200,670);
    	
    	if(logoON)
    		g.drawImage(logo, 0, 0);
    	if(loginON){
    		g.drawImage(login, 0, 0);
    		g.drawImage(logQuit, 500, 440);
    		g.drawImage(logOK, 670, 440);
    		textLogin.render(gc, g);
    		textPass.render(gc, g);
    		if(logQuitHoverON)
    			g.drawImage(logQuitHover, 500, 440);
    		if(logOKHoverON)
    			g.drawImage(logOKHover, 670, 440);
    		
    	}
    	if(menuON){
    		g.drawImage(menu, 0, 0);
    		g.drawString(pseudo, 790, 20);
    		g.drawString("Rang: "+rang, 790, 38);
    		g.drawString("Points: "+points, 790, 53);
    		if(menuHoverJouerON){
    			g.drawImage(menuHoverJouer, 222, 436);
    			g.drawImage(menuJouerChoix, 222, 541);
    		}
    		if(menuHoverLadderON)
    			g.drawImage(menuHoverLadder, 360, 436);
    		if(menuHoverOptionON)
    			g.drawImage(menuHoverOption, 500, 436);
    		if(menuHoverCreditsON)
    			g.drawImage(menuHoverCredits, 640, 436);
    		if(menuHoverQuitterON)
    			g.drawImage(menuHoverQuitter, 779, 436);
    		
    		if(menuJouerChoixSoloHoverON)
    			g.drawImage(menuJouerChoixSoloHover, 222, 541);
    		if(menuJouerChoixMultiHoverON)
    			g.drawImage(menuJouerChoixMultiHover, 222, 541);
    	}
    	if(menuMultiON){
    		g.drawImage(menuMulti, 0, 0);
    		
    		if(!saveListeServer.equalsIgnoreCase("")){	// Simple vérification
				String bufferListe[],bufferInfo[];
				int ii=0,oo=0;
				bufferListe = saveListeServer.split(":");
				for(String v : bufferListe){
					bufferInfo = v.split("-");
					try{
						g.drawString(bufferInfo[0], 164,95+ii*40+oo);
						g.drawString(bufferInfo[1], 400,95+ii*40+oo);
						g.drawString(bufferInfo[2], 569,95+ii*40+oo);
						// L'ip n'a pas être afficher, et je n'affiche pas les places restantes pour le moment
					}catch(Exception e){}
					ii++;
					if(ii==9)
						oo=2;
				}
    		}
    		int yy = Mouse.getY();
    		yy=yPleinEcran-yy;
    		if(yy >= 611 || yy <= 78)
    			yy=81;
    		int serverSelected = (yy-80)/41;
    		if(serverSelected<=9)
    			g.drawImage(partieHover, 142, serverSelected*41+78-serverSelected);
    		else
    			g.drawImage(partieHover, 142, serverSelected*41+80-serverSelected);
    			
    		if(menuMultiRetourHoverON)
    			g.drawImage(menuMultiRetourHover, 114, 670);
    		if(menuMultiIPHoverON)
    			g.drawImage(menuMultiIPHover, 250, 671);
    		if(menuMultiCreerServeurHoverON)
    			g.drawImage(menuMultiCreerServeurHover, 390, 671);
    		if(menuMultiRafraichirHoverON)
    			g.drawImage(menuMultiRafraichirHover, 571, 670);
    			
    		//if(menuMultiRejoindreON)
    			
    		//if(menuMultiRejoindreHoverON)		
    	}
    	if(ipDirectON){
    		g.drawImage(ipDirect, 380, 210);
    		textIP.render(gc, g);
    		if(ipDirectConnecterHoverON)
    			g.drawImage(ipDirectConnecterHover, 437, 328);
    		if(ipDirectAnnulerHoverON)
    			g.drawImage(ipDirectAnnulerHover, 555, 328);
    			
    	}
    	
    	if(menuMultiServeurCreerON){
    		g.drawImage(menuMultiServeurCreer, 0, 0);
    		textServerChat.render(gc, g);
    		if(menuMultiServeurCreerRetourHoverON)
    			g.drawImage(menuMultiServeurCreerRetourHover, 113, 625);
    		if(menuMultiServeurCreerPartieHoverON)
    			g.drawImage(menuMultiServeurCreerPartieHover, 271, 625);
    		if(menuMultiSeveurCreerEnvoyerHoverON)
    			g.drawImage(menuMultiSeveurCreerEnvoyerHover, 958, 625);
    		int i=0;
    		
    		if(joueur==1){
	    		g.drawString(pseudo, 130, 120);
	    		g.drawString("rang: "+rang+"     points: "+points,325,120);
	    		if(clientReceiver.pseudoJ2 != null){
	    			g.drawString(clientReceiver.pseudoJ2, 130, 225);
	    			g.drawString("rang: "+clientReceiver.rangJ2+"     points: "+clientReceiver.pointsJ2,325,225);
	    		}
    		}else{
    			if(clientReceiver.pseudoJ1 != null){
	    			g.drawString(clientReceiver.pseudoJ1, 130, 120);
	    			g.drawString("rang: "+clientReceiver.rangJ1+"     points: "+clientReceiver.pointsJ1,325,120);
	    		}
    			g.drawString(pseudo, 130, 225);
    			g.drawString("rang: "+rang+"     points: "+points,325,225);
    		}
	    		
    		for(String v : arrayServerText){
    			g.drawString(v, 565, 300+i*18);
    			i++;
    			}
    	}
    	
    	if(menuLadderON){
    		g.drawImage(ladder,0,0);
    		
    		if(!saveListeLadder.equalsIgnoreCase("")){	// Simple vérification
				String bufferListe[],bufferInfo[];
				int ii=0,oo=0;
				bufferListe = saveListeLadder.split(":");
				for(String v : bufferListe){
					bufferInfo = v.split("-");
					try{
						g.drawString(bufferInfo[0], 223,95+ii*48+oo);
						g.drawString(bufferInfo[1], 430,95+ii*48+oo);
						g.drawString(bufferInfo[2], 595,95+ii*48+oo);
					}catch(Exception e){}
					ii++;
					if(ii==5)
						oo=8;
				}
    		}
				
    		if(menuLadderRetourHoverON)
    			g.drawImage(ladderRetourHover,200,670);
    		else
    			g.drawImage(ladderRetour,200,670);
    	}
    	
    	if(menuSoloON){
    		g.drawImage(menuSolo,0,0);
    		if(menuMultiServeurCreerRetourHoverON)
    			g.drawImage(menuMultiServeurCreerRetourHover, 113, 626);
    	}
    	
    	// ***************
    	// *	LE JEU	 *
    	// ***************
    	if(gameON){
    		if(!antiBugMap){
    			g.drawImage(map, 0-deltaX, 0+deltaY);
    		}else{
	    		// ******************
	    		// Anti bug de la map
	    		// c'est dû au fait que l'image de la map est trop grande et Slick 2d ou autre le gère mal
	    		// Le cache n'est pas mis à jour et on se retrouve à avoir une map qui contient des éléments mis en cache par d'autres jeux.
    			// De plus, ceci permet à l'ordi d'être plus rapide car il a à afficher seulement des petites images. On gagne en FPS
	    		for(int i=0;i<2;i++){
	    			g.drawImage(mapRepeat, 0+600*i, 0);
	    			g.drawImage(mapRepeat, 0+600*i, 500);
	    		}
	    		g.drawImage(mineOr, 0-deltaX, 0+deltaY);
	    		g.drawImage(mineOr, 1200-deltaX, 1050+deltaY);
	    		g.drawImage(mineOr, 2500-deltaX, 2000+deltaY);
	    		// ******************
    		}
    		
    		// *****
    		if(joueur==1)
	    		if(game==null)// Fais ça car l'autre endroit où je fais ça, le game du serveur est encore null au moment de l'appel de la fonction
	    			game=serveur.getGame();
	    			
    		if(joueur==2)
    			if(game==null)
    				game=clientReceiver.getGame();
    		
    		if(game!=null){
    			game.joueur=joueur;
    			game.setClientReceiver(clientReceiver);
    			game.setGameRanked(networkON);
    		}
    		// *****
    		
    		try{
    			afficherSang();
    			
	    		afficherCercleSelection();
	    		afficherUnitEnnemie();  		
	    		afficherUnitJoueur();
	    		
	    		afficherTireMage();
	    		afficherTireFleche();
	    		
	    		afficherVieManaInGame();	// Vie et Mana au-dessus des unités (choisis dans les options)
	    		
	    		brouillard();	// Mettre le brouillard de guerre
	    		
	    		afficherSelection();	// Rectangle vert
	    		
	    		g.drawImage(jeu, 0, 0);
	    		g.drawImage(minimap, 2, 624);
	    		if(gameSolo)
		    		if(objectifHoverON){
		    			g.drawImage(objectifHover, 1005, 29);
		    		}else{
		    			g.drawImage(objectif, 1005, 29);
		    		}
	    		
	    		afficherUnitMiniMap();
	    		brouillardMiniMap();
	    		afficherRectangleMiniMap();
	    		
	    		afficherInfoUnitSelec();
	    		afficherListeUnitAcheter();
	    		
	    		afficherInfoReste();
	    		afficherGroupSelection();
	    		afficherRaccourcis();
	    		afficherSurbrillance();
	    		
	    		afficherTextField(gc);
	    		afficherTexteDiscussion();
	    		afficherTexteError();
	    		
	    		afficherObjectif();
    		}catch(ConcurrentModificationException e){	// Dû au fait que j'ai mes thread qui modifie les array et ici j'y accède direct (faudrait peut-etre faire un synchronised)
    			//e.printStackTrace();					// et y a une erreur d'abstract list avec les next
    			//debPhrs("ERROR Concurrent");	
    		}catch(NullPointerException e){
    		
    		}catch(Exception e){}
    		
    		
    	if(inGameOptionOverON)
    		g.drawImage(inGameOptionOver, 0, 0);
    	if(inGameChoixON){
    		g.drawImage(inGameChoix, 350, 150);
    		if(inGameChoixRetourOverON)
    			g.drawImage(inGameChoixRetourOver, 470, 225);
    		if(inGameChoixOptionOverON)
    			g.drawImage(inGameChoixOptionOver, 472, 323);
    		if(inGameChoixQuitterOverON)
    			g.drawImage(inGameChoixQuitterOver, 472, 424);
    	}
    	}	// Fin de gameON
    	
    	// FIN GAME
    	if(finGameON){
    		g.drawImage(finGame, 0, 0);
    		if(game != null){
    			g.setColor(Color.red);
    			g.setFont(fontLogin);
        		g.drawString(""+game.pointsJ1, 318, 155);
        		g.drawString(""+game.pointsJ2, 711, 155);
        		
        		g.setColor(Color.white);
        		// Or final
        		g.drawString(""+game.argentJ1, 280, 427);
        		g.drawString(""+game.argentJ2, 674, 427);
        		
        		// Or total
        		g.drawString(""+game.argentTotalJ1, 279, 396);
        		g.drawString(""+game.argentTotalJ2, 673, 396);
        		
        		// Unités total tuée
        		g.drawString(""+game.unitTotalTuerJ1, 311, 319);
        		g.drawString(""+game.unitTotalTuerJ2, 700, 319);
        		
        		// Unités total crée
        		g.drawString(""+game.unitTotalCreerJ1, 321, 349);
        		g.drawString(""+game.unitTotalCreerJ2, 705, 349);
        		
        		// TIMER
        		if(game.minTimer<10 && game.secTimer < 10)
        			_g.drawString("0"+game.minTimer+" : 0"+game.secTimer,592, 560);
        		else if(game.minTimer<10)
        			_g.drawString("0"+game.minTimer+" : "+game.secTimer,592, 560);
        		else
        			_g.drawString(""+game.minTimer+" : "+game.secTimer, 592, 560);
    		}
    		if(finGameRetourHoverON)
    			g.drawImage(finGameRetourHover, 115, 625);
    	}
    	
    	// Ici car commun au jeu et au menu du debut
    	if(inGameOptionON){
    		g.drawImage(inGameOption,0,0);
    		
    		g.setColor(Color.black);
    		g.drawString(""+multiSensibilite, 830, 319);
    		g.drawString(""+multiSensibilite2, 830, 443);
    		ipOptionTextField.render(gc, g);
    		g.setColor(Color.white);
    		
    		if(raccourcisON)
    			g.drawImage(choixOui, 405, 47);
    		if(!qwertyON)
    			g.drawImage(choixOui, 986, 50);
    		if(affichVieMaInGameON)
    			g.drawImage(choixOui, 418, 164);
    		if(game != null)
    			if(game.sangActive)
    				g.drawImage(choixOui, 988, 137);
    		if(musicON)
    			g.drawImage(choixOui, 820, 195);
    		if(effetON)
    			g.drawImage(choixOui, 820, 230);
    		if(antiBugMap)
    			g.drawImage(choixOui, 435, 88);
    		
    		if(ecranTaille1280)
    			g.drawImage(choixOui, 504, 595);
    		if(ecranTaille1280x800)
    			g.drawImage(choixOui, 504, 630);
    		if(ecranTaille1366)
    			g.drawImage(choixOui, 691, 595);
    		if(ecranTaille1440)
    			g.drawImage(choixOui, 878, 595);
    		
    		
    		if(optionPlusON1)
    			g.drawImage(optionPlus, 869, 312);
    		if(optionMoinsON1)
    			g.drawImage(optionMoins, 757, 312);
    		if(optionPlusON2)
    			g.drawImage(optionPlus, 868, 438);
    		if(optionMoinsON2)
    			g.drawImage(optionMoins, 758, 438);
    		
    		if(inGameOptionRetourON)
    			g.drawImage(inGameChoixRetourOver, 150, 656);
    	}
    	
    	// **** Gerer le PLEIN ECRAN ****
    	gererBordsPleinEcran();
    	
    	if(chargementON)	// Ne sera jamais affiché normalement, il aurait fallu que cet affichage soit en dehors de la "boucle du programme"
    		g.drawImage(chargement, 0, 0);
    	
    	if(errorON){
    		if(errorHoverON)
    			g.drawImage(errorHover, 0, 0);
    		else
    			g.drawImage(error, 0, 0);
    		
    		g.drawString(errorMsg, 400, 325);
    	}
    	
    	
    }
	
    public void mousePressed(int button, int x, int y) {
		if (button == 0) {
			if(loginON){
				if (logQuitHoverON){	// Bouton quitter
					Display.destroy();
					System.exit(1);
				}
				if (logOKHoverON){	// Bouton OK
					if(textLogin.getText().equals("")){
						errorMsg="           Pseudo vide\nsi vous voulez jouer en hors ligne\nécrivez votre pseudo mais pas le\nmot de passe";
	    				errorON=true;
					}else if(!textLogin.getText().equals("") && textPass.getText().equals("")){
						pseudo=textLogin.getText();
						loginON=false;
						menuON=true;
			    		textLogin.deactivate();
			    		textPass.deactivate();
			    		networkON=false;
					}else{
						pseudo=textLogin.getText();
						String reponse="";
						try {
							chargementON=true;
							reponse = get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=connex&pseudo="+pseudo+"&pass="+textPass.getText());
							//System.out.println("la page contient:|"+get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=connex&pseudo="+pseudo+"&pass="+textPass.getText())+"|");
							
							if(reponse.startsWith("true")){
								reponse = reponse.substring(4);
								String bufferInfo[];
								bufferInfo = reponse.split("-");
								try{
									rang = Integer.valueOf(bufferInfo[0]);
									points = Integer.valueOf(bufferInfo[1]);
								}catch(Exception e){}
								
								loginON=false;
								menuON=true;
					    		textLogin.deactivate();
					    		textPass.deactivate();
					    		networkON=true;
					    		
					    		bufferInfo=null;
							}else if(reponse.startsWith("false")){
								errorMsg="      Pseudo ou mot de passe\n            incorrect";
			    				errorON=true;
							}else{
								// Erreur inconnu
								errorMsg="         Erreur inconnu";
			    				errorON=true;
							}
						} catch (IOException e) {
							errorMsg="Impossible d'accéder au serveur\nde vérification";
		    				errorON=true;
							e.printStackTrace();
							//System.out.println("IO error");
						} catch (Exception e) {
							// Voir si c'est vraiment inconnu
							errorMsg="         Erreur inconnu";
		    				errorON=true;
							e.printStackTrace();
							//System.out.println("Exception error");
						}
						chargementON=false;
						reponse=null;
					}	// fin else
				} // Fin logOKHoverON
			}	// Fin LoginON
			if(menuON){
				if (menuJouerChoixSoloHoverON){	// JOUER SOLO
					menuON = false;
					menuSoloON=true;
					gameSolo=true;
					game=null;
				}
				if (menuJouerChoixMultiHoverON){	// JOUER MULTI
					menuON = false;
					menuMultiON=true;
					gameSolo=false;
					game=null;
					if(networkON){
						try {
							chargementON=true;
							saveListeServer = get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=getServeurs");
							
							//System.out.println("la page contient:|"+get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=getServeurs"));
							
							if(saveListeServer.startsWith("true")){	// Simple vérification
								if(saveListeServer.equalsIgnoreCase("true")){	// Je sais pas pourquoi mais le "true" n'est pas supprime
									saveListeServer="";
								}else{
									saveListeServer = saveListeServer.substring(4);
								}
								/*String bufferListe[],bufferInfo[];
								bufferListe = reponse.split(":");
								for(String v : bufferListe){
									bufferInfo = v.split("-");
									try{
										
									}catch(Exception e){}
								}*/
							}else{
								errorMsg="Il est possible qu'il y ait eu\nun problème lors de l'accès\nà la database";
			    				errorON=true;
							}
						} catch (IOException e) {
							errorMsg="Impossible d'accéder au serveur\nde vérification\nVérifier votre connexion";
		    				errorON=true;
							e.printStackTrace();
							//System.out.println("IO error");
						} catch (Exception e) {
							// Voir si c'est vraiment inconnu
							errorMsg="         Erreur inconnu";
		    				errorON=true;
							e.printStackTrace();
							//System.out.println("Exception error");
						}
						chargementON=false;
						//reponse=null;
					}	// Fin
				}
				if (menuHoverLadderON){	// LADDER
					//if(networkON){	CAR LA PERSONNE PEUT TRES BIEN AVOIR INTERNET MAIS NE C PAS CONNECTER
						//String reponse="";
						try {
							chargementON=true;
							saveListeLadder = get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=getLadder");
							
							//System.out.println("la page contient:|"+get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=getLadder"));
							
							if(!saveListeLadder.equalsIgnoreCase("")){	// Simple vérification
								/*String bufferListe[],bufferInfo[];
								bufferListe = reponse.split(":");
								for(String v : bufferListe){
									bufferInfo = v.split("-");
									try{
										
									}catch(Exception e){}
								}*/
							}else{
								errorMsg="Il est possible qu'il y ait eu\nun problème lors de l'accès\nà la database";
			    				errorON=true;
							}
						} catch (IOException e) {
							errorMsg="Impossible d'accéder au serveur\nde vérification\nVérifier votre connexion";
		    				errorON=true;
							e.printStackTrace();
							//System.out.println("IO error");
						} catch (Exception e) {
							// Voir si c'est vraiment inconnu
							errorMsg="         Erreur inconnu";
		    				errorON=true;
							e.printStackTrace();
							//System.out.println("Exception error");
						}
						chargementON=false;
						//reponse=null;
					//}	// Fin
					menuON = false;
					menuLadderON=true;
				}
				if (menuHoverOptionON){	// OPTION
					menuON = false;
					inGameOptionON=true;
					ipOptionTextField.setText(ipOptionSaveTextField);
				}
				if (menuHoverCreditsON){	// CREDITS
					menuON = false;
					creditsON=true;
				}
				if (menuHoverQuitterON){	// QUITTER
					Display.destroy();
					System.exit(1);
				}
			}
			if(menuMultiON && !ipDirectON){
				if(networkON)
					cliqueSurUnServeur(x,y);	// Juste pour allégé le code ici
				
				if(menuMultiRetourHoverON){
					menuON=true;
					menuMultiON=false;
					ipDirectON=false;
				}
				if(menuMultiIPHoverON){
					ipDirectON=true;
				}
				if(menuMultiCreerServeurHoverON){
					if(networkON){
						String reponse="";
						try {
							chargementON=true;
							if(ipOptionSaveTextField.equalsIgnoreCase(""))
								reponse = get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=serveur&pseudo="+pseudo+"&points="+points+"&rang="+rang+"&ip=rien");
							else
								reponse = get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=serveur&pseudo="+pseudo+"&points="+points+"&rang="+rang+"&ip="+ipOptionSaveTextField);
							
							
							//System.out.println("la page contient:|"+get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=serveur&pseudo="+pseudo+"&points="+points+"&rang="+rang+"&ip=rien"));
							
							if(reponse.startsWith("true")){
								
							}else{
								errorMsg="Il est possible qu'il y ait eu\nun problème lors de l'ajout de\nla partie sur le serveur";
			    				errorON=true;
							}
						} catch (IOException e) {
							errorMsg="Impossible d'accéder au serveur\nde vérification\nVérifier votre connexion";
		    				errorON=true;
							e.printStackTrace();
							//System.out.println("IO error");
						} catch (Exception e) {
							// Voir si c'est vraiment inconnu
							errorMsg="         Erreur inconnu";
		    				errorON=true;
							e.printStackTrace();
						//	System.out.println("Exception error");
						}
						chargementON=false;
						reponse=null;
					}	// Fin
					
					joueur=1;
					menuMultiServeurCreerON=true;
					menuMultiON=false;
					ipDirectON=false;
					textIP.deactivate();
					
					
					// Creer le serveur
					if(serveur == null){
						serveur = new Serveur();
						debPhrs("Serveur instancier");
					}
					
					if(serveur!=null){
						serveur.setNbClients(0);
						debPhrs("Mis a 0");
					}
					//	serveur.setContinuer(true);
	    			try{
	    				chargementON=true;
	    				SocketAddress sockaddr = new InetSocketAddress("127.0.0.1", portUtilise);
	    				socket = new Socket();
	    				socket.connect(sockaddr, 6000);	//*/
	    				clientReceiver = new ClientReceiver(socket, this);
	    				
	    				clientReceiver.pseudoJ1=pseudo;
	    				clientReceiver.rangJ1=rang;
	    				clientReceiver.pointsJ1=points;
	    				
	    				menuMultiServeurCreerON=true;
						menuMultiON=false;
						ipDirectON=false;
						textIP.deactivate();
	    			} catch (SocketTimeoutException e) {
	    				errorMsg="Connexion impossible, Time Out";
		    			errorON=true;
		    			menuMultiServeurCreerON=false;
						menuMultiON=true;
	    			}catch(Exception e) {
	    				errorMsg="Connexion impossible.\nLe serveur n'a pas eu le temps\nde se lancer.\nVeuillez rééssayer.";
		    			errorON=true;
		    			menuMultiServeurCreerON=false;
						menuMultiON=true;
	    			}//*/
	    			chargementON=false;
				}	// Fin menuMultiCreerServeurHoverON
				
				if(menuMultiRafraichirHoverON){
					if(networkON){
						try {
							chargementON=true;
							saveListeServer = get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=getServeurs");
							
							//System.out.println("la page contient:|"+get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=getServeurs"));
							
							if(saveListeServer.startsWith("true")){	// Simple vérification
								if(saveListeServer.equalsIgnoreCase("true")){	// Je sais pas pourquoi mais le "true" n'est pas supprime
									saveListeServer="";
								}else{
									saveListeServer = saveListeServer.substring(4);
								}
								
							}else{
								errorMsg="Il est possible qu'il y ait eu\nun problème lors de l'accès\nà la database";
			    				errorON=true;
							}
						} catch (IOException e) {
							errorMsg="Impossible d'accéder au serveur\nde vérification\nVérifier votre connexion";
		    				errorON=true;
							e.printStackTrace();
							//System.out.println("IO error");
						} catch (Exception e) {
							// Voir si c'est vraiment inconnu
							errorMsg="         Erreur inconnu";
		    				errorON=true;
							e.printStackTrace();
							//System.out.println("Exception error");
						}
						chargementON=false;
						//reponse=null;
					}else{	// Fin
						errorMsg="Il faut être connecté pour voir\nles serveurs.";
	    				errorON=true;
					}
				}	// menuMultiRafraichirHoverON
			}	// menuMultiON
			
	    	if(ipDirectON){
	    		if(ipDirectConnecterHoverON){
	    			joueur=2;
	    			try{
	    				chargementON=true;
	    				SocketAddress sockaddr = new InetSocketAddress(textIP.getText(), portUtilise);
	    				socket = new Socket();
	    				socket.connect(sockaddr, 6000);	//*/
	    				clientReceiver = new ClientReceiver(socket, this);
	    				
	    				clientReceiver.pseudoJ2=pseudo;
	    				clientReceiver.rangJ2=rang;
	    				clientReceiver.pointsJ2=points;

	    				menuMultiServeurCreerON=true;
						menuMultiON=false;
						ipDirectON=false;
						textIP.deactivate();
						
						clientReceiver.sendInfoPlayer("infoJ2"+pseudo+"-"+rang+"-"+points);
	    			} catch (SocketTimeoutException e) {
	    				errorMsg="Connexion impossible, Time Out";
		    			errorON=true;
	    			}catch(Exception e) {
	    				errorMsg="Connexion impossible, vérifier \nl'addresse IP";
		    			errorON=true;
	    			}
	    			chargementON=false;
	    		}
	    		if(ipDirectAnnulerHoverON){
	    			menuMultiON=true;
	    			ipDirectON=false;
	    			textIP.deactivate();
	    		}
	    	}
	    	
	    	if(menuMultiServeurCreerON){
	    		if(menuMultiServeurCreerRetourHoverON){	// Enlever le serveur, close, ...
	    			if(networkON){
						String reponse="";
						try {
							chargementON=true;
							if(ipOptionSaveTextField.equalsIgnoreCase(""))
								reponse = get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=EnleverServeur&ip=rien");
							else
								reponse = get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=EnleverServeur&ip="+ipOptionSaveTextField);
							
							//System.out.println("la page contient:|"+get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=EnleverServeur&ip=rien")+"|");
							
							if(reponse.startsWith("true")){
							
							}else{
								errorMsg="Il est possible qu'il y ait eu\nun problème lors de la suppression de\nla partie sur le serveur";
			    				errorON=true;
							}
						} catch (IOException e) {
							errorMsg="Impossible d'accéder au serveur\nde vérification\nVérifier votre connexion";
		    				errorON=true;
							e.printStackTrace();
							//System.out.println("IO error");
						} catch (Exception e) {
							// Voir si c'est vraiment inconnu
							errorMsg="         Erreur inconnu";
		    				errorON=true;
							e.printStackTrace();
							//System.out.println("Exception error");
						}
						reponse=null;
						chargementON=false;
					}	// Fin
	    			
	    			menuMultiON=true;
	    			menuMultiServeurCreerON=false;		
	    			if(joueur==1)
	    				try{
	    					clientReceiver.sendDisconnetedJ1();
	    					// Ou faire : clientReceiver.sendAnyMsg("disco"+joueur);	Et peut donc se mettre en dehors des if(joueur==)
	    					socket.close();	// Remplace celui de dessous
	    					//socketOwner.close();	// ENLEVER POUR FAIRE COMME SI C'ETAIT UN CLIENT	*************************
	    					serveur.videMsg();
	    					serveur.setNbClients(0);
	    					//serveur.setContinuer(false);
	    				}catch (IOException e){ }
					if(joueur==2)
						try{
							clientReceiver.sendDisconnetedJ2();
							// Ou faire : clientReceiver.sendAnyMsg("disco"+joueur);
							socket.close();
						}catch (IOException e){ }
					arrayServerText.clear();
					
					arrayServerText.add("Bienvenue dans le jeu");
	    		}
	    		try{
	    			if(menuMultiServeurCreerPartieHoverON && joueur==1 && !clientReceiver.pseudoJ2.equals("") && clientReceiver.pseudoJ2 != null){
	    				if(networkON){
							try {
								get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=partieLancer&pseudo="+pseudo);
							} catch (IOException e) {
								e.printStackTrace();
								//System.out.println("IO error");
							} catch (Exception e) {
								// Voir si c'est vraiment inconnu
								/*errorMsg="         Erreur inconnu";
			    				errorON=true;*/
								e.printStackTrace();
								//System.out.println("Exception error");
							}
						}	// Fin
	    				
	    				menuMultiServeurCreerON=false;
	    				gameON=true;
	    				clientReceiver.startGame();
	    				game=serveur.getGame();
	    				if(game!=null){
	    					game.setClientReceiver(clientReceiver);
	    					game.joueur=joueur;
	    					game.setGameRanked(networkON);
	    				}
	    			}
	    		}catch(NullPointerException e){ }
	    		
	    		if(menuMultiSeveurCreerEnvoyerHoverON){
	    			if(!textServerChat.getText().equals("")){
	    				clientReceiver.sendCreerServeur(joueur+":"+textServerChat.getText());
	    				// Ou faire : clientReceiver.sendAnyMsg("sendCreer"+joueur+":"+textServerChat.getText());
	    				
	    				/*if(joueur==1){	// ENLEVER POUR FAIRE COMME SI C'ETAIT UN CLIENT	*************************
	    					try {
	    						output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketOwner.getOutputStream())),true);
	    						output.print("sendCreer"+textServerChat.getText()+'\u0000');
								output.flush();
								
	    					} catch (IOException e) {
	    						e.printStackTrace();
	    					}
	    				}
	    				if(joueur==2){
	    					clientReceiver.sendCreerServeur(textServerChat.getText());
	    				}*/
	    			}
	    			
	    			textServerChat.setText("");
	    			textServerChat.setFocus(true);
	    			if(arrayServerText.size() >= 17)
	    				arrayServerText.remove(0);
	    		}
	    	}
	    	
	    	if(menuLadderON){
	    		if(menuLadderRetourHoverON){
	    			menuLadderON=false;
	    			menuON=true;
	    		}
	    	}
	    	if(creditsON)
	    		if(menuLadderRetourHoverON){
	    			creditsON=false;
	    			menuON=true;
	    		}
	    	
	    	if(errorON){
	    		if(errorHoverON){
	    			errorHoverON=false;
	    			errorON=false;
	    		}
	    	}
	    	
	    	if(menuSoloON){
	    		if(menuMultiServeurCreerRetourHoverON){
	    			menuON=true;
	    			menuSoloON=false;
	    		}
	    		joueur=1;
	    		// Tutos
	    		if(x>=107 && x<=365 && y >=78 && y<= 90){	// nains
	    			game=new Game(1, this);
	    			gameON=true;
	    			menuSoloON=false;
	    		}
	    		if(x>=480 && x<=715 && y >=78 && y<= 90){	// transf
	    			game=new Game(4, this);
	    			gameON=true;
	    			menuSoloON=false;
	    		}
	    		if(x>=107 && x<=395 && y >=111 && y<= 129){	// Corps à corps
	    			game=new Game(2, this);
	    			gameON=true;
	    			menuSoloON=false;
	    		}
	    		if(x>=480 && x<=874 && y >=111 && y<= 129){	// faiblesses et avantages
	    			game=new Game(5, this);
	    			gameON=true;
	    			menuSoloON=false;
	    		}
	    		if(x>=107 && x<=248 && y >=144 && y<= 163){	// archer
	    			game=new Game(3, this);
	    			gameON=true;
	    			menuSoloON=false;
	    		}
	    		// Défis
	    		if(x>=107 && x<=335 && y >=246 && y<= 264){	// armée nains
	    			game=new Game(6, this);
	    			gameON=true;
	    			menuSoloON=false;
	    		}
	    	   if(x>=107 && x<=335 && y >=278 && y<= 297){	// distance
	    		   game=new Game(7, this);
	    		   gameON=true;
	    		   menuSoloON=false;		
	    	    }
	    	    if(x>=107 && x<=335 && y >=315 && y<= 326){	// moins vite
	    	    	game=new Game(8, this);
		    		gameON=true;
		    		menuSoloON=false;	
	    	    }
	    	    	
    	    	if(x>=480 && x<=643 && y >=246 && y<= 264){	// massacre
    	    		game=new Game(9, this);
		    		gameON=true;
		    		menuSoloON=false;	
    	    	}
    	    	if(x>=480 && x<=643 && y >=278 && y<= 297){	// contres
    	    		game=new Game(10, this);
		    		gameON=true;
		    		menuSoloON=false;		
    	    	}
    	    	if(x>=480 && x<=643 && y >=315 && y<= 326){	// poursuivis
    	    		game=new Game(11, this);
		    		gameON=true;
		    		menuSoloON=false;	
    	    	}
	    		if(x>=782 && x<=966 && y >=246 && y<= 264){	// massacre 2
	    			game=new Game(12, this);
		    		gameON=true;
		    		menuSoloON=false;	
	    		}
    	    	//if(x>=782 && x<=643 && y >=278 && y<= 297)	// 
    	    	    			
    	    	//if(x>=782 && x<=643 && y >=315 && y<= 326)	// 
	    	//  */  	    
	    	}
	    	
	    	
	    	if(inGameOptionRetourON && !gameON){
	    		menuON=true;
    			inGameOptionON=false;
    			ipOptionSaveTextField = ipOptionTextField.getText();
    		}
	    	
	    	// ***************
	    	// *	LE JEU	 *
	    	// ***************
	    	if(gameON){
	    		if(inGameOptionOverON)
	    			inGameChoixON=true;
	    		if(inGameChoixRetourOverON)
	    			inGameChoixON=false;
	    		if(inGameChoixOptionOverON){
	    			inGameChoixON=false;
	    			inGameOptionON=true;	
	    			ipOptionTextField.setText(ipOptionSaveTextField);
	    		}
	    		// Objectif
	    		if(objectifHoverON)
	    			objectifON=true;
	    		
	    		// *******************************************************
	    		// *******************************************************
	    		if(inGameChoixQuitterOverON){
	    			gameON=false;
	    			inGameChoixON=false;
	    			enregistrerPartie();
	    			//game=null;
	    			_g.setColor(Color.white);
	    			j1Disco();
	    			j2Disco();
	    			
	    			if(clientReceiver!=null)
	    				clientReceiver=null;
	    			if(serveur!=null){
	    				serveur.setNbClients(0);
	    				serveur.arreterServeur();    	
	    				
	    				serveur=null;
	    			}
	    			if(socket!=null)
	    				socket=null;
	    		}
	    		// *******************************************************
	    		
	    		if(inGameOptionRetourON){
	    			inGameChoixON=true;
	    			inGameOptionON=false;
	    			ipOptionSaveTextField = ipOptionTextField.getText();
	    		}
	    		
		    	if(!inGameOptionON){
		    		if(gameAttaON)
		    			moveAttackSav=true;
		    		if(gameEtoileON)
		    			posUnitStrategy=1;
		    		if(gameRectON)
		    			posUnitStrategy=2;
		    		
		        	if(gameStopON)
		        		stopUnit();	
		        	if(gameTenirON)
		        		tenirUnit();
		        	
		    		if(gameNainON) 
		    			switchAchaTransf(0);
		    		if(gameFantaON)
		    			switchAchaTransf(1);
		    		if(gameMinauON)
		    			switchAchaTransf(2);
		    		if(gameArchON)
		    			switchAchaTransf(3);
		    		if(gameCentON)
		    			switchAchaTransf(4);
		    		if(gameMageON)
		    			switchAchaTransf(5);
		    		
		    		// Change les Delta via la minimap
		    		changeDeltaMinimap(x,y);
		    	}
	    	}// Fin gameON
	    	// Fin de partie (FIN GAME)
	    	if(finGameON){
	    		if(finGameRetourHoverON)
	    		{
	    			finGameON=false;
	    			menuON=true;
	    		}
	    	}
	    	
	    	// *****************************************
	    	if(inGameOptionON){	// OPTION
	    		if(optionPlusON1)
	    			multiSensibilite++;
	    		if(optionMoinsON1)
	    			multiSensibilite--;
	    		if(optionPlusON2)
	    			multiSensibilite2++;			
	    		if(optionMoinsON2)
	    			multiSensibilite2--;
	    		
	    		if(multiSensibilite < -18)
	    			multiSensibilite = -18;
	    		if(multiSensibilite2 < -18)
	    			multiSensibilite2 = -18;
	    		
	    		if(x>= 403 && x<= 422 && y>= 56 && y<=77)
	    			if(raccourcisON){
	    				raccourcisON=false;
	    			}else{
	    				raccourcisON=true;}
		    	if(x>= 985 && x<= 1005 && y>= 60 && y<= 79)
		    		if(qwertyON)
		    			qwertyON=false;
		    		else
		    			qwertyON=true;
		    	if(x>= 414 && x<= 438 && y>= 175 && y<= 193)
		    		if(affichVieMaInGameON)
		    			affichVieMaInGameON=false;
		    		else
		    			affichVieMaInGameON=true;
		    	if(x>= 820 && x<= 840 && y>= 206 && y<= 227)
		    		if(musicON){
		    			try{
		    				musSwirlOfDust.stop();
		    				musicON=false;
		    			}catch(Exception e){}
		    		}else{
		    			try{
		    				musSwirlOfDust.loop();
		    				musicON=true;
		    			}catch(Exception e){}
		    			
		    		}
		    	if(x>= 820 && x<= 840 && y>= 237 && y<= 257)
		    		if(effetON)
		    			effetON=false;
		    		else
		    			effetON=true;
		    	if(x>= 439 && x<= 460 && y>= 96 && y<= 117)
		    		if(antiBugMap)
		    			antiBugMap=false;
		    		else
		    			antiBugMap=true;
		    	
		    	
		    	// Boolean de sang est dans Game, On ne peut donc activer le sang que lorsqu'on est dans une partie
		    	if(game != null)
		    		if(x>= 945 && x<= 1007 && y>= 143 && y<= 163)
		    			if(game.sangActive){
		    				game.sangActive=false;
		    				game.arrayTacheSang.clear();
		    			}else{
		    				game.sangActive=true;
		    			}
		    	
		    	if(x>= 877 && x<= 900 && y>= 607 && y<= 630)
		    		if(ecranTaille1440){
		    			ecranTaille1440=false;
		    			try {
							app.setDisplayMode(1100, 750, false);
							yPleinEcran=750;
						} catch (SlickException e) {
							errorMsg="Erreur, Contactez le créateur\ndu jeu.\nMerci";
		    				errorON=true;
							e.printStackTrace();
						}
		    		}else{  			
		    			try {
							app.setDisplayMode(1440, 900, true);
							ecranTaille1440=true;
							ecranTaille1280=false;
							ecranTaille1280x800=false;
							ecranTaille1366=false;
							yPleinEcran=900;
						} catch (SlickException e) {
							errorMsg="Cette résolution d'écran n'est\npas disponible.";
		    				errorON=true;
							e.printStackTrace();
						}
		    		}
		    	if(x>= 504 && x<= 528 && y>= 607 && y<= 630)
		    		if(ecranTaille1280){
		    			ecranTaille1280=false;
		    			try {
							app.setDisplayMode(1100, 750, false);
							yPleinEcran=750;
						} catch (SlickException e) {
							errorMsg="Erreur, Contactez le créateur\ndu jeu.\nMerci";
		    				errorON=true;
							e.printStackTrace();
						}
		    		}else{	    			
		    			try {
							app.setDisplayMode(1280, 1024, true);
							ecranTaille1280=true;
							ecranTaille1366=false;
							ecranTaille1280x800=false;
							ecranTaille1440=false;
							yPleinEcran=1024;
						} catch (SlickException e) {
							errorMsg="Cette résolution d'écran n'est\npas disponible.";
		    				errorON=true;
							e.printStackTrace();
						}
		    		}
		    	if(x>= 690 && x<= 713 && y>= 607 && y<= 630)
		    		if(ecranTaille1366){
		    			ecranTaille1366=false;
		    			try {
							app.setDisplayMode(1100, 750, false);
							yPleinEcran=750;
						} catch (SlickException e) {
							errorMsg="Erreur, Contactez le créateur\ndu jeu.\nMerci";
		    				errorON=true;
							e.printStackTrace();
						}
		    		}else{
		    			try {
							app.setDisplayMode(1366, 768, true);
							ecranTaille1366=true;
							ecranTaille1280=false;
							ecranTaille1280x800=false;
							ecranTaille1440=false;
							yPleinEcran=768;
						} catch (SlickException e) {
							errorMsg="Cette résolution d'écran n'est\npas disponible.";
		    				errorON=true;
							e.printStackTrace();
						}
		    		}
		    	if(x>= 504 && x<= 528 && y>= 637 && y<= 657)
		    		if(ecranTaille1280x800){
		    			ecranTaille1280x800=false;
		    			try {
							app.setDisplayMode(1100, 750, false);
							yPleinEcran=750;
						} catch (SlickException e) {
							errorMsg="Erreur, Contactez le créateur\ndu jeu.\nMerci";
		    				errorON=true;
							e.printStackTrace();
						}
		    		}else{
		    			try {
							app.setDisplayMode(1280, 800, true);
							ecranTaille1280x800=true;
							ecranTaille1366=false;
							ecranTaille1280=false;
							ecranTaille1440=false;
							yPleinEcran=800;
						} catch (SlickException e) {
							errorMsg="Cette résolution d'écran n'est\npas disponible.";
		    				errorON=true;
							e.printStackTrace();
						}
		    		}
	    	}
	    	
		}	// Fin button 0
		
		if (button == 1) {
			if(gameON){
				if(x >= 1 && x <= 190 && y>= 623 && y <= 750){
					// XMAXUNIT=2990,YMAXUNIT=2490;
					// on a v.changeDestination(x+deltaX, y-deltaY); donc suffit de faire : deplaceUnit((int)(x*15.9f)-deltaX, (y-623)*19+deltaY)
					deplaceUnit((int)(x*15.9f)-deltaX, (y-623)*19+deltaY);	// XMAX... sont gérés dans collisionRect et Etoile
				}else
					deplaceUnit(x,y);
        		moveAttackSav=false;
			}
		}
	}
	
    public void mouseReleased(int button, int x, int y) {
    	if(gameON){
    		
    	}
    }
    
	public void mouseClicked(int button, int x, int y, int clickCount) {
		//System.out.println("CLIC :"+x+","+y+" "+clickCount+" "+doubleClick+"\net: "+deltaX+" et: "+deltaY);
		
		if(gameON){	
			doubleClick=false;
			 // *************
			 // DOUBLE CLIQUE
			 // *************
	    	/*
			if(button == Input.MOUSE_LEFT_BUTTON)
				if(x>0 && y < 655)
					if(getTime() - lastClick <= 250){	// Gérer double click
						if(game.arrayMonsterOwnerSelected.size() == 1){	// Pour le moment, je fais comme ça, c'est pour éviter une boucle de plus, et normalement ma fonction selection
							// a déjà selectionner 1 ou des unités
							int meta1 = game.arrayMonsterOwnerSelected.get(0).getMetamor();
							game.arrayMonsterOwnerSelected.clear();
							for(Monster v : game.arrayMonsterOwner){
								if(v != null)
									if(Math.abs(v.getX()-deltaX-x) <= 350 && Math.abs(v.getY()+deltaY-y) <= 350 && v.getMetamor() == meta1){
										game.arrayMonsterOwnerSelected.add(v);
										if(game.arrayMonsterOwnerSelected.size() >= game.MAX_UNIT_SELECTED)
											break;
									}
							}
							//continue; // Suivant, et on evite de reset arrayMonsterOwnerSelected
						}
					}else{
						lastClick = getTime();
						// **************************************************
						// MAUVAIS ENDROIT NORMALEMENT
						// **************************************************
						game.arrayMonsterOwnerSelected.clear();	// A ete mis là et enlever de selection();
						game.arrayMonsterEnnemySelected.clear();
					}//*/
			//*
			if(button == Input.MOUSE_LEFT_BUTTON){
				doubleClick=false;
				if(x>0 && y < 618){
					if(clickCount==1 && !game.arrayMonsterOwnerSelected.isEmpty() && !Keyboard.isKeyDown(Input.KEY_LSHIFT)){	// LSHIFT ajouter pour permettre d'ajouter des unités a la selection
						game.arrayMonsterOwnerSelected.clear();	// A ete mis là et enlever de selection(); puis remis :p pour adaption au nouveau code de Slick
						game.arrayMonsterEnnemySelected.clear();
					}
					if(clickCount==2 && !game.arrayMonsterOwnerSelected.isEmpty()){
						doubleClick=true;
						if(game.arrayMonsterOwnerSelected.size() == 1){	// Pour le moment, je fais comme ça, c'est pour éviter une boucle de plus, et normalement ma fonction selection
							// a déjà selectionner 1 ou des unités
							int meta1 = game.arrayMonsterOwnerSelected.get(0).getMetamor();
							game.arrayMonsterOwnerSelected.clear();
							if(joueur==1){
								for(Monster v : game.arrayMonsterOwner){
									if(v != null)
										if(Math.abs(v.getX()-deltaX-x) <= 350 && Math.abs(v.getY()+deltaY-y) <= 350 && v.getMetamor() == meta1){
											game.arrayMonsterOwnerSelected.add(v);
											if(game.arrayMonsterOwnerSelected.size() >= Game.MAX_UNIT_SELECTED)
												break;
										}
								}
							}else{
								for(Monster v : game.arrayMonsterEnnemy){
									if(v != null)
										if(Math.abs(v.getX()-deltaX-x) <= 350 && Math.abs(v.getY()+deltaY-y) <= 350 && v.getMetamor() == meta1){
											game.arrayMonsterOwnerSelected.add(v);
											if(game.arrayMonsterOwnerSelected.size() >= Game.MAX_UNIT_SELECTED)
												break;
										}
								}
							}
						}
					}
				}//*/
			}
		}
		
	}
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		// Si la souris est appuyée puis déplacée, on cré une nouvelle ligne
		logQuitHoverON = false;
		logOKHoverON = false;
		
		menuHoverLadderON=false;
		menuHoverCreditsON=false;
		menuHoverOptionON=false;
		menuHoverQuitterON=false;
		menuJouerChoixSoloHoverON=false;
		menuJouerChoixMultiHoverON=false;
		
		menuMultiRetourHoverON=false;
		menuMultiCreerServeurHoverON=false;
		menuMultiIPHoverON=false;
		menuMultiRafraichirHoverON=false;
		
		ipDirectAnnulerHoverON=false;
		ipDirectConnecterHoverON=false;
		
		menuMultiServeurCreerPartieHoverON=false;
		menuMultiServeurCreerRetourHoverON=false;
		menuMultiSeveurCreerEnvoyerHoverON=false;
		
		menuLadderRetourHoverON=false;
		
		gameAttaON=false; gameStopON=false; gameTenirON=false; gameEtoileON=false; gameRectON=false;
		gameNainON=false; gameFantaON=false; gameMinauON=false; gameArchON=false; gameCentON=false; gameMageON=false;
		
		errorHoverON=false;
		
		inGameOptionOverON=false;
		inGameChoixOptionOverON=false;
		inGameChoixRetourOverON=false;
		inGameChoixQuitterOverON=false;
		inGameOptionRetourON=false;
		
		 optionPlusON1=false;
		 optionPlusON2=false;
		 optionMoinsON1=false;
		 optionMoinsON2=false;
		 
		 finGameRetourHoverON=false;
		 	 
		 objectifHoverON=false;
		
		if(loginON){
			if (newx >= 500 && newx <= 655 && newy >= 440 && newy <= 488)
				logQuitHoverON = true;
			if (newx >= 670 && newx <= 730 && newy >= 440 && newy <= 488)
				logOKHoverON = true;
		}
		if(menuON){
			if(newx >= 362 || newx <= 222 || newy <= 435)	// Afficher SOLO et MULTI
					menuHoverJouerON=false;

			if (newx >= 222 && newx <= 362 && newy >= 435 && newy <= 541)
				menuHoverJouerON=true;
			if (newx >= 363 && newx <= 503 && newy >= 435 && newy <= 541)
				menuHoverLadderON=true;
			if (newx >= 504 && newx <= 644 && newy >= 435 && newy <= 541)
				menuHoverOptionON=true;
			if (newx >= 645 && newx <= 785 && newy >= 435 && newy <= 541)
				menuHoverCreditsON=true;
			if (newx >= 786 && newx <= 915 && newy >= 435 && newy <= 541)
				menuHoverQuitterON=true;
			
			if (newx >= 222 && newx <= 362 && newy >= 541 && newy <= 579)
				menuJouerChoixSoloHoverON=true;
			if (newx >= 222 && newx <= 362 && newy >= 580 && newy <= 615)
				menuJouerChoixMultiHoverON=true;		
		}
		if(menuMultiON){
			//menuMultiSeparationON=false;
			//menuMultiRejoindreON=false;
			//menuMultiRejoindreHoverON=false;
			
			if (newx >= 115 && newx <= 235 && newy >= 670 && newy <= 700)
				menuMultiRetourHoverON=true;
			if (newx >= 250 && newx <= 365 && newy >= 670 && newy <= 700)
				menuMultiIPHoverON=true;
			if (newx >= 390 && newx <= 550 && newy >= 670 && newy <= 700)
				menuMultiCreerServeurHoverON=true;
			if(newx >= 570 && newx <= 718 && newy >= 670 && newy <= 700)
				menuMultiRafraichirHoverON=true;
			/*
			menuMultiSeparationON=true;
			menuMultiRejoindreON=true;
			menuMultiRejoindreHoverON=true;*/
		}
		if(ipDirectON){
			if (newx >= 438 && newx <= 552 && newy >= 334 && newy <= 357)
				ipDirectConnecterHoverON=true;
			if (newx >= 557 && newx <= 673 && newy >= 334 && newy <= 357)
				ipDirectAnnulerHoverON=true;
		}
    	if(menuMultiServeurCreerON){
    		if (newx >= 114 && newx <= 233 && newy >= 625 && newy <= 657)
    			menuMultiServeurCreerRetourHoverON=true;
    		if (newx >= 272 && newx <= 442 && newy >= 625 && newy <= 657)
    			menuMultiServeurCreerPartieHoverON=true;
    		if (newx >= 959 && newx <= 1078 && newy >= 625 && newy <= 657)
    			menuMultiSeveurCreerEnvoyerHoverON=true;
    	}
    	if(menuLadderON){
    		if (newx >= 200 && newx <= 319 && newy >= 670 && newy <= 702)
    			menuLadderRetourHoverON=true;
    	}
    	if(creditsON){
    		if (newx >= 200 && newx <= 319 && newy >= 670 && newy <= 702)
    			menuLadderRetourHoverON=true;
    	}
    	
		
    	if(errorON){	// 437, 328
    		if (newx >= 585 && newx <= 682 && newy >= 405 && newy <= 430)
    			errorHoverON=true;
    	}
    	
    	// ***************************************************
    	if(inGameOptionON){		// OPTION
			if (newx >= 150 && newx <= 268 && newy >= 656  && newy <= 690)
				inGameOptionRetourON=true;
			if (newx >= 868 && newx <= 905 && newy >= 315  && newy <= 340)
				optionPlusON1=true;
			if (newx >= 868 && newx <= 905 && newy >= 440  && newy <= 466)
				optionPlusON2=true;
			if (newx >= 759 && newx <= 800 && newy >= 315  && newy <= 340)
				optionMoinsON1=true;
			if (newx >= 759 && newx <= 800 && newy >= 440  && newy <= 466)
				optionMoinsON2=true;
		}
    	
    	if(menuSoloON){
    		if (newx >= 114 && newx <= 233 && newy >= 625 && newy <= 657)
    			menuMultiServeurCreerRetourHoverON=true;	
    	}
    	
    	// ***************
    	// *	LE JEU	 *
    	// ***************
    	if(gameON){
    		if (newx >= 0 && newx <= 50 && newy >= 0  && newy <= 50)
    			inGameOptionOverON=true;
    		if(inGameChoixON){
	    		if (newx >= 472 && newx <= 592 && newy >= 225  && newy <= 268)
	    			inGameChoixRetourOverON=true;
	    		if (newx >= 472 && newx <= 592 && newy >= 325  && newy <= 357)
	    			inGameChoixOptionOverON=true;
	    		if (newx >= 472 && newx <= 592 && newy >= 420  && newy <= 454)
	    			inGameChoixQuitterOverON=true;
    		}
    		
    		if (newx >= 1005 && newx <= 1100 && newy >= 29  && newy <= 64)
    			objectifHoverON=true;
	
    		if (newx >= 855 && newx <= 905 && newy >= 620  && newy <= 665)
	    		gameAttaON=true;
    		if (newx >= 906 && newx <= 955 && newy >= 620  && newy <= 665)
	    		gameStopON=true;
    		if (newx >= 956 && newx <= 1005 && newy >= 620  && newy <= 665)
	    		gameTenirON=true;
    		if (newx >= 1006 && newx <= 1055 && newy >= 620  && newy <= 665)
	    		gameEtoileON=true;
    		if (newx >= 1056 && newx <= 1100 && newy >= 620  && newy <= 665)
	    		gameRectON=true;
	    		
    		if (newx >=  855 && newx <= 896 && newy >= 703  && newy <= 750)
	    		gameNainON=true;
    		if (newx >= 897 && newx <= 938 && newy >= 703  && newy <= 750)
	    		gameFantaON=true;
    		if (newx >= 939 && newx <= 978 && newy >= 703  && newy <= 750)
	    		gameMinauON=true;
    		if (newx >= 979 && newx <= 1019 && newy >= 703  && newy <= 750)
	    		gameArchON=true;
    		if (newx >= 1020 && newx <= 1059 && newy >= 703  && newy <= 750)
	    		gameCentON=true;
    		if (newx >= 1060 && newx <= 1100 && newy >= 703  && newy <= 750)
	    		gameMageON=true;		
    	}
	
    	// Fin GAME
    	if(finGameON){
    		if(newx >=  117 && newx <= 288 && newy >= 628  && newy <= 652)
    			finGameRetourHoverON=true;
    	}
	}
	
	
    public void keyPressed(int key, char c) {	// Voir lors de ESCAPE
		if (key == Input.KEY_ESCAPE) {
			if(gameON){
				if(!inGameChoixON && !objectifON)
					inGameChoixON=true;
				else if(inGameChoixON=true && !inGameOptionON)
					inGameChoixON=false;
				
				if(objectifON)
					objectifON=false;
				
				// ******************************************************************
				// MIS CAR DES FLECHES SE "PERDENT"
				// C pas gênant car cet array est juste pour afficher les fleches, les dmg sont immédiats
				// Soit je laisse comme ça, soit je fais en sorte que ce soit fait toutes les 30 sec à 1 min
				if(game != null)
					game.arrayTireFleche.clear();
				// ******************************************************************
			}
		}
		
		if(!inGameOptionON)
			if(key == Input.KEY_ENTER || key == Input.KEY_RETURN || key == 28)
				if(!textInGameChat.hasFocus()){
					textInGameChatON=true;
					textInGameChat.setFocus(true);
				}else{
			//if(textInGameChat.hasFocus())
			//	if(key == Input.KEY_ENTER || key == Input.KEY_RETURN || key == 28){	// Fais ça car sur mon mac (QWERTY), il reconnais pas la touche comme ENTER (ou faut que j'appuie sur une autre touche)
	    			if(!textInGameChat.getText().equals(""))
	    				if(clientReceiver != null)
	    					clientReceiver.sendAnyMsg("allinGameChat"+joueur+":"+textInGameChat.getText());
	    			
	    			textInGameChat.setText("");
					textInGameChat.deactivate();
					textInGameChatON=false;
				}
		if(!inGameOptionON && textInGameChat.hasFocus() && key == Input.KEY_ESCAPE){
			textInGameChat.setText("");
			textInGameChat.deactivate();
			textInGameChatON=false;
		}
		
		
		if(textServerChat.hasFocus())
			if(key == Input.KEY_ENTER || key == Input.KEY_RETURN || key == 28)	// Fais ça car sur mon mac (QWERTY), il reconnais pas la touche comme ENTER (ou faut que j'appuie sur une autre touche)
    			if(!textServerChat.getText().equals("")){
    				if(clientReceiver != null)
    					clientReceiver.sendCreerServeur(joueur+":"+textServerChat.getText());
    			
    			textServerChat.setText("");
    			textServerChat.setFocus(true);
    			if(arrayServerText.size() >= 17)
    				arrayServerText.remove(0);
    			}
		
    	// ***************
    	// *	LE JEU	 *
    	// ***************
		if(gameON){
			switch(key){
			case Input.KEY_1:	// Groupes de selection
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
        			if(!game.arrayMonsterOwnerSelected.isEmpty()){
        				game.arrayMonsterOwnerGroup1.clear();
        				game.arrayMonsterOwnerGroup1.addAll(game.arrayMonsterOwnerSelected);
        		}
        		}else{
        			if(!game.arrayMonsterOwnerGroup1.isEmpty()){
        				game.arrayMonsterOwnerSelected.clear();
        				game.arrayMonsterOwnerSelected.addAll(game.arrayMonsterOwnerGroup1);
        				if(lastKeyPressedNum==Input.KEY_1 && (getTime() - lastKeyPressed < TEMPS_DOUBLE_KEY)){
        					deltaX=(int)game.arrayMonsterOwnerSelected.get(0).getX() - (int)(FENLARG / 2);
        					deltaY=-((int)game.arrayMonsterOwnerSelected.get(0).getY() - (int)(FENHAUT/2));
        				}
        			}
        		}
				break;
			case Input.KEY_2:
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
        			if(!game.arrayMonsterOwnerSelected.isEmpty()){
        				game.arrayMonsterOwnerGroup2.clear();
        				game.arrayMonsterOwnerGroup2.addAll(game.arrayMonsterOwnerSelected);
        		}
        		}else{
        			if(!game.arrayMonsterOwnerGroup2.isEmpty()){
        				game.arrayMonsterOwnerSelected.clear();
        				game.arrayMonsterOwnerSelected.addAll(game.arrayMonsterOwnerGroup2);
        				if(lastKeyPressedNum==Input.KEY_2 && (getTime() - lastKeyPressed < TEMPS_DOUBLE_KEY)){
        					deltaX=(int)game.arrayMonsterOwnerSelected.get(0).getX() - (int)(FENLARG / 2);
        					deltaY=-((int)game.arrayMonsterOwnerSelected.get(0).getY() - (int)(FENHAUT/2));
        				}
        			}
        		}
				break;
			case Input.KEY_3:
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
        			if(!game.arrayMonsterOwnerSelected.isEmpty()){
        				game.arrayMonsterOwnerGroup3.clear();
        				game.arrayMonsterOwnerGroup3.addAll(game.arrayMonsterOwnerSelected);
        		}
        		}else{
        			if(!game.arrayMonsterOwnerGroup3.isEmpty()){
        				game.arrayMonsterOwnerSelected.clear();
        				game.arrayMonsterOwnerSelected.addAll(game.arrayMonsterOwnerGroup3);
        				if(lastKeyPressedNum==Input.KEY_3 && (getTime() - lastKeyPressed < TEMPS_DOUBLE_KEY)){
        					deltaX=(int)game.arrayMonsterOwnerSelected.get(0).getX() - (int)(FENLARG / 2);
        					deltaY=-((int)game.arrayMonsterOwnerSelected.get(0).getY() - (int)(FENHAUT/2));
        				}
        			}
        		}
				break;
			case Input.KEY_4:
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
        			if(!game.arrayMonsterOwnerSelected.isEmpty()){
        				game.arrayMonsterOwnerGroup4.clear();
        				game.arrayMonsterOwnerGroup4.addAll(game.arrayMonsterOwnerSelected);
        		}
        		}else{
        			if(!game.arrayMonsterOwnerGroup4.isEmpty()){
        				game.arrayMonsterOwnerSelected.clear();
        				game.arrayMonsterOwnerSelected.addAll(game.arrayMonsterOwnerGroup4);
        				if(lastKeyPressedNum==Input.KEY_4 && (getTime() - lastKeyPressed < TEMPS_DOUBLE_KEY)){
        					deltaX=(int)game.arrayMonsterOwnerSelected.get(0).getX() - (int)(FENLARG / 2);
        					deltaY=-((int)game.arrayMonsterOwnerSelected.get(0).getY() - (int)(FENHAUT/2));
        				}
        			}
        		}
				break;
			case Input.KEY_5:
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
        			if(!game.arrayMonsterOwnerSelected.isEmpty()){
        				game.arrayMonsterOwnerGroup5.clear();
        				game.arrayMonsterOwnerGroup5.addAll(game.arrayMonsterOwnerSelected);
        		}
        		}else{
        			if(!game.arrayMonsterOwnerGroup5.isEmpty()){
        				game.arrayMonsterOwnerSelected.clear();
        				game.arrayMonsterOwnerSelected.addAll(game.arrayMonsterOwnerGroup5);
        				if(lastKeyPressedNum==Input.KEY_5 && (getTime() - lastKeyPressed < TEMPS_DOUBLE_KEY)){
        					deltaX=(int)game.arrayMonsterOwnerSelected.get(0).getX() - (int)(FENLARG / 2);
        					deltaY=-((int)game.arrayMonsterOwnerSelected.get(0).getY() - (int)(FENHAUT/2));
        				}
        			}
        		}
				break;
			case Input.KEY_6:
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
        			if(!game.arrayMonsterOwnerSelected.isEmpty()){
        				game.arrayMonsterOwnerGroup6.clear();
        				game.arrayMonsterOwnerGroup6.addAll(game.arrayMonsterOwnerSelected);
        			}
        		}else{
        			if(!game.arrayMonsterOwnerGroup6.isEmpty()){
        				game.arrayMonsterOwnerSelected.clear();
        				game.arrayMonsterOwnerSelected.addAll(game.arrayMonsterOwnerGroup6);
        				if(lastKeyPressedNum==Input.KEY_6 && (getTime() - lastKeyPressed < TEMPS_DOUBLE_KEY)){
        					deltaX=(int)game.arrayMonsterOwnerSelected.get(0).getX() - (int)(FENLARG / 2);
        					deltaY=-((int)game.arrayMonsterOwnerSelected.get(0).getY() - (int)(FENHAUT/2));
        				}
        			}
        		}
				break;
			case Input.KEY_7:
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
        			if(!game.arrayMonsterOwnerSelected.isEmpty()){
        				game.arrayMonsterOwnerGroup7.clear();
        				game.arrayMonsterOwnerGroup7.addAll(game.arrayMonsterOwnerSelected);
        			}
        		}else{
        			if(!game.arrayMonsterOwnerGroup7.isEmpty()){
        				game.arrayMonsterOwnerSelected.clear();
        				game.arrayMonsterOwnerSelected.addAll(game.arrayMonsterOwnerGroup7);
        				if(lastKeyPressedNum==Input.KEY_7 && (getTime() - lastKeyPressed < TEMPS_DOUBLE_KEY)){
        					deltaX=(int)game.arrayMonsterOwnerSelected.get(0).getX() - (int)(FENLARG / 2);
        					deltaY=-((int)game.arrayMonsterOwnerSelected.get(0).getY() - (int)(FENHAUT/2));
        				}
        			}
        		}
				break;
			case Input.KEY_8:
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
        			if(!game.arrayMonsterOwnerSelected.isEmpty()){
        				game.arrayMonsterOwnerGroup8.clear();
        				game.arrayMonsterOwnerGroup8.addAll(game.arrayMonsterOwnerSelected);
        			}
        		}else{
        			if(!game.arrayMonsterOwnerGroup8.isEmpty()){
        				game.arrayMonsterOwnerSelected.clear();
        				game.arrayMonsterOwnerSelected.addAll(game.arrayMonsterOwnerGroup8);
        				if(lastKeyPressedNum==Input.KEY_8 && (getTime() - lastKeyPressed < TEMPS_DOUBLE_KEY)){
        					deltaX=(int)game.arrayMonsterOwnerSelected.get(0).getX() - (int)(FENLARG / 2);
        					deltaY=-((int)game.arrayMonsterOwnerSelected.get(0).getY() - (int)(FENHAUT/2));
        				}
        			}
        		}
				break;
			case Input.KEY_9:
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
        			if(!game.arrayMonsterOwnerSelected.isEmpty()){
        				game.arrayMonsterOwnerGroup9.clear();
        				game.arrayMonsterOwnerGroup9.addAll(game.arrayMonsterOwnerSelected);
        		}
        		}else{
        			if(!game.arrayMonsterOwnerGroup9.isEmpty()){
        				game.arrayMonsterOwnerSelected.clear();
        				game.arrayMonsterOwnerSelected.addAll(game.arrayMonsterOwnerGroup9);
        				if(lastKeyPressedNum==Input.KEY_9 && (getTime() - lastKeyPressed < TEMPS_DOUBLE_KEY)){
        					deltaX=(int)game.arrayMonsterOwnerSelected.get(0).getX() - (int)(FENLARG / 2);
        					deltaY=-((int)game.arrayMonsterOwnerSelected.get(0).getY() - (int)(FENHAUT/2));
        				}
        			}
        		}
				break;
				
			case Input.KEY_E:	// Tenir Pos
				tenirUnit();
				break;
			case Input.KEY_R:	// etoile
				posUnitStrategy = 1;
				break;
			case Input.KEY_T:	// Rectangle
				posUnitStrategy = 2;
				break;
			case Input.KEY_S:	// Fantassin
				switchAchaTransf(1);
				break;
			case Input.KEY_D:	// Minautaure
				switchAchaTransf(2);
				break;
			case Input.KEY_F:	// Archer
				switchAchaTransf(3);
				break;
			case Input.KEY_G:	// Centaure
				switchAchaTransf(4);
				break;
			case Input.KEY_H:	// Mage
				switchAchaTransf(5);
				break;
				}
			if(qwertyON){
				switch(key){
			case Input.KEY_Q:	// Attaquer
				moveAttackSav = true;
				break;
			case Input.KEY_W:	// Stop
				stopUnit();
				break;
			case Input.KEY_A:	// Nain
				switchAchaTransf(0);
				break;
				}
				}else{
					switch(key){
				case Input.KEY_A:	// Attaquer
					moveAttackSav = true;
					break;
				case Input.KEY_Z:	// Stop
					stopUnit();
					break;
				case Input.KEY_Q:	// Nain
					switchAchaTransf(0);
					break;
					}
				}
			
			lastKeyPressed=getTime();
			lastKeyPressedNum=key;
			}	// FIN GAMEON
		
		// Touche F1 : change la résolution
		/*if (key == Input.KEY_F1) {
			if (app != null) {
				try {
					app.setDisplayMode(600, 600, false);
					app.reinit();
				} catch (Exception e) {  }
			}
		}*/
		}	// Fin KeyPressed
	
    public void keyReleased(int key, char c) {
		//message = "Vous avez appuyé la touche "+key+" (caractère = "+c+")";	
	}
    
    
	
	public void j1Disco(){
		finGameON=true;
		menuMultiServeurCreerON=false;		
			try{
				socket.close();
				serveur.videMsg();
			}catch (IOException e){		
			}catch (NullPointerException e){
			}
		arrayServerText.clear();
		arrayServerText.add("Bienvenue dans le jeu");
	}
	public void j2Disco(){
		if(clientReceiver!=null){
			clientReceiver.pseudoJ2="";
			clientReceiver.pointsJ2=0;
			clientReceiver.rangJ2=0;
		}
	}
	
	public int getJoueur(){
		return joueur;
	}
	public void startGame(){
		menuON=false;
		menuMultiON=false;	// Simple précaution
		menuMultiServeurCreerON=false;
		gameON=true;
		game=clientReceiver.getGame();
		if(game!=null)
			game.joueur=joueur;
	}
	
    
	/** Enregistre la partie dans le ladder si J1 quitte la partie alors qu'elle n'est pas terminée (il perd) */
	private void enregistrerPartie(){
		if(game != null && networkON && joueur == 1)
			if(game.getGameRanked())
				if(game.getPause()){
					// On fait rien car la partie s'est déjà enregistrer
				}else{	// J1 a quitté mais la partie n'est pas finie
					try {
						//System.out.println("J1 quitte !");
						//System.out.println(get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=partieFinie&pseudo1="+clientReceiver.pseudoJ2+"&points1="+game.pointsJ2+"&pseudo2="+pseudo+"&points2="+game.pointsJ1));
						get(""+Lancement.adresseSiteServeur+"/ranking/multi.php?action=partieFinie&pseudo1="+clientReceiver.pseudoJ2+"&points1="+game.pointsJ2+"&pseudo2="+pseudo+"&points2="+game.pointsJ1);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}	
				}
		
	}
	
	public static void main(String[] args) throws SlickException {
        app = new AppGameContainer(new Lancement());      
        app.setDisplayMode(1100, 750, false); // Mode fenêtré
        app.setTargetFrameRate(60);
        app.start();
   }
    
    public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
    public boolean getEffetON(){
    	return effetON;
    }
    
	// ***************
	// *	LE JEU	 *
	// ***************
    /** Move camera depending on mouse and keyboard */ 
    private void moveCamera(int x2, int y2) {
   	 int x = Mouse.getX();
   	 int y = Mouse.getY();
   	 
   	 if(x2 == -1)
   		 deltaX-=20+multiSensibilite;
   	 if(x2 == 1)
   		 deltaX+=20+multiSensibilite;
   	 if(y2 == -1)
	     deltaY-=20+multiSensibilite;
	 if(y2 == 1)
	     deltaY+=20+multiSensibilite;
   	 
   	 // Deplacer la carte et modifier deltaX, deltaY
     	if(x<10)
     		deltaX-=20+multiSensibilite2;
     	if(x>1090)
     		deltaX+=20+multiSensibilite2;
     	if(y<10)
     		deltaY-=20+multiSensibilite2;
     	if(y>740)
     		deltaY+=20+multiSensibilite2;	
     	if(deltaX<0)
     		deltaX = 0;
     	if(deltaX>1900)
     		deltaX = 1900;
     	if(deltaY<-1910)
     		deltaY = -1910;
     	if(deltaY>0)
     		deltaY = 0;
   }
    /** Add monster in the Selected arraylist if in the area */
    private void selection(){	//	LE y=750-y DOIT ETRE CHANGER EN FONCTION DE LA HAUTEUR DE L'ECRAN !! PAREIL DANS L"AFFICHAGE DE LA SELECTION
    	//Color.white.bind();
	    	int x = Mouse.getX();
	    	int y = Mouse.getY();
	    	y=yPleinEcran-y;
	    	if(Mouse.isButtonDown(0) && m==0){	// 1er pasage, on voit que bouton gauche est presse
	   			lastX = x;lastY = y;m=1;
	    	}
			/*		// ENLEVER CAR MIS DANS RENDER
	    	if(Mouse.isButtonDown(0) && m==1){	// Bouton toujours presse, on commence a entourer la zone
				 // Gestion des X
				 if(x<lastX){
					 for(i=0;i<=lastX-x;i++)
						 _g.drawImage(barreSelec,lastX-i,lastY);	// de gauche haut vers droite haut
					 for(i=0;i<=lastX-x;i++)
						 _g.drawImage(barreSelec,lastX-i,y);	// de gauche bas vers droite bas
				 }else{
				 for(i=0;i<=x-lastX;i++)
					 _g.drawImage(barreSelec,lastX+i,lastY);	// de gauche haut vers droite haut
				 for(i=0;i<=x-lastX;i++)
					 _g.drawImage(barreSelec,lastX+i,y);	// de gauche bas vers droite bas
				 }
				 // Gestion des Y
				 if(y<lastY){
					 for(i=0;i<=lastY-y;i++)
						 _g.drawImage(barreSelec,lastX,lastY-i);	// de gauche bas vers haut
					 for(i=0;i<=lastY-y;i++)
						 _g.drawImage(barreSelec,x,lastY-i);	// de droite bas vers haut 
				 }else{
				 for(i=0;i<=y-lastY;i++)
					 _g.drawImage(barreSelec,lastX,lastY+i);	// de gauche haut vers bas
				 for(i=0;i<=y-lastY;i++)
					 _g.drawImage(barreSelec,x,lastY+i);	// de droite haut vers bas
				 }
			 }//*/
	    	
	    	// PEUT-ETRE RÉDUIRE ET METTRE LA CONDITION DU JOUEUR LÀ OÙ Y A BESOIN (2 FOIS), SA EVITE D'AVOIR UN CODE ENORME
	    	if(joueur==1){
			 if(!Mouse.isButtonDown(0) && m==1 && game != null){	// On a relacher le bouton, on a selectionné la zone
				 if(/*game.arrayMonsterOwnerSelected.size() < 1*/ !Keyboard.isKeyDown(Input.KEY_LSHIFT) && ((Math.abs(lastX-x) >= 25) || ((int)Math.abs(lastY-y) >= 25) || !doubleClick || game.arrayMonsterOwnerSelected.size() > Monster.nbreUnite) /*&& x < 850*/ && y < 618){	// Il peut arriver que le joueur voit plus d'unite qu'il en a mais generalement, il selectionnera et fera au minimun un clique dans le boolean sera sur false
					 game.arrayMonsterOwnerSelected.clear();	// On enleve toutes unites selectionnées
					 //System.out.println("clear");
				 		// FINALEMENT FAIRE >= 25 devrait suffire, mais je laisse les précautions
				 }
				 try{
				 	for(Monster v : game.arrayMonsterOwner){	// Selectionner les unites dans la zone
							if(game.arrayMonsterOwnerSelected.size() >= Game.MAX_UNIT_SELECTED)	// Unites max que l'on peut selectionner
								break;
								if(v != null){
									if(((int)v.getX()) >= lastX+deltaX && ((int)v.getX()) <=x+deltaX && 
											((int)v.getY()) >= lastY-deltaY && ((int)v.getY()) <= y-deltaY){	// Cas où c'est de haut à gauche vers bas droite	lastX < x   lastY < y
										game.arrayMonsterOwnerSelected.add(v);
									}
									if(((int)v.getX()) >= lastX+deltaX && ((int)v.getX()) <=x+deltaX && 
											((int)v.getY()) <= lastY-deltaY && ((int)v.getY()) >= y-deltaY){	// Cas où c'est de bas à gauche vers haut droite	lastX < x   lastY > y
										game.arrayMonsterOwnerSelected.add(v);
									}
									if(((int)v.getX()) <= lastX+deltaX && ((int)v.getX()) >=x+deltaX && 
											((int)v.getY()) >= lastY-deltaY && ((int)v.getY()) <= y-deltaY){	// Cas où c'est de haut à droite vers bas gauche	lastX > x   lastY < y
										game.arrayMonsterOwnerSelected.add(v);
									}
									if(((int)v.getX()) <= lastX+deltaX && ((int)v.getX()) >=x+deltaX && 
											((int)v.getY()) <= lastY-deltaY && ((int)v.getY()) >= y-deltaY){	// Cas où c'est de bas à droite vers haut gauche	lastX > x   lastY > y
										game.arrayMonsterOwnerSelected.add(v);
									}
	
									//debPhrs("lastX-x : "+(int)(Math.abs(lastX-x))+" lastY-y : "+(int)(Math.abs(lastY-y))+"- (y-deltaY) : "+(int)(Math.abs(v.getY()-(y-deltaY)))+" v.get x - : "+(int)(Math.abs(v.getX()-(x+deltaX))) );
									// Donner la possibliter de selectionner une unité
									// SI LA LOOP N'EST PAS ASSEZ RAPIDE, Y A PAS DE SELECTION DONC A VOIR SI JE LAISSE ÇA LÀ
									if(game.arrayMonsterOwnerSelected.isEmpty() || Keyboard.isKeyDown(Input.KEY_LSHIFT))
										if(Math.abs(lastX-x) <= 25 && ((int)Math.abs(lastY-y)) <= 25)
											if(((int)Math.abs(v.getX()-(x+deltaX))) <= 25 && ((int)(Math.abs(v.getY()-(y-deltaY)))) <= 25){
												game.arrayMonsterOwnerSelected.add(v);
												break;
											}
								}
						}
				 }catch(NullPointerException e){}
						//if(!game.arrayMonsterOwnerSelected.isEmpty())
							game.arrayMonsterEnnemySelected.clear();

						// Selectionner unité Ennemie
						if(game.arrayMonsterOwnerSelected.isEmpty())
							for(Monster v : game.arrayMonsterEnnemy)
								if(v != null)
									if(Math.abs(lastX-x) <= 25 && ((int)Math.abs(lastY-y)) <= 25)
										if(((int)Math.abs(v.getX()-(x+deltaX))) <= 25 && ((int)(Math.abs(v.getY()-(y-deltaY)))) <= 25){
											game.arrayMonsterEnnemySelected.add(v);
											break;
										}
						
				 //debPhrs("y a : "+arrayMonsterOwnerSelected.size()+" et deltaX vaut : "+deltaX+" deltaY : "+deltaY+"\nx vaut : "+x+" y : "+y);
				
				// debPhrs("y a "+arrayMonsterOwner.size()+"unites");
				 m=0;lastX = x; lastY = y;
			 }
	    	}else{
				 if(!Mouse.isButtonDown(0) && m==1 && game != null){	// On a relacher le bouton, on a selectionné la zone
					 if(/*game.arrayMonsterOwnerSelected.size() < 1*/ !Keyboard.isKeyDown(Input.KEY_LSHIFT) && ((Math.abs(lastX-x) >= 25) || ((int)Math.abs(lastY-y) >= 25) || !doubleClick || game.arrayMonsterOwnerSelected.size() > Monster.nbreUnite) /*&& x < 850*/ && y < 618)	// Il peut arriver que le joueur voit plus d'unite qu'il en a mais generalement, il selectionnera et fera au minimun un clique dans le boolean sera sur false
						 game.arrayMonsterOwnerSelected.clear();	// On enleve toutes unites selectionnées	A ETE ENLEVE POUR DOUBLE CLICK MAIS A VOIR SI SA MARCHE MIEUX OU SI JE REGLE ÇA
					 		
					 	for(Monster v : game.arrayMonsterEnnemy){	// Selectionner les unites dans la zone
								if(game.arrayMonsterOwnerSelected.size() >= Game.MAX_UNIT_SELECTED)	// Unites max que l'on peut selectionner
									break;
									if(v != null){
										if(((int)v.getX()) >= lastX+deltaX && ((int)v.getX()) <=x+deltaX && 
												((int)v.getY()) >= lastY-deltaY && ((int)v.getY()) <= y-deltaY){	// Cas où c'est de haut à gauche vers bas droite	lastX < x   lastY < y
											game.arrayMonsterOwnerSelected.add(v);
										}
										if(((int)v.getX()) >= lastX+deltaX && ((int)v.getX()) <=x+deltaX && 
												((int)v.getY()) <= lastY-deltaY && ((int)v.getY()) >= y-deltaY){	// Cas où c'est de bas à gauche vers haut droite	lastX < x   lastY > y
											game.arrayMonsterOwnerSelected.add(v);
										}
										if(((int)v.getX()) <= lastX+deltaX && ((int)v.getX()) >=x+deltaX && 
												((int)v.getY()) >= lastY-deltaY && ((int)v.getY()) <= y-deltaY){	// Cas où c'est de haut à droite vers bas gauche	lastX > x   lastY < y
											game.arrayMonsterOwnerSelected.add(v);
										}
										if(((int)v.getX()) <= lastX+deltaX && ((int)v.getX()) >=x+deltaX && 
												((int)v.getY()) <= lastY-deltaY && ((int)v.getY()) >= y-deltaY){	// Cas où c'est de bas à droite vers haut gauche	lastX > x   lastY > y
											game.arrayMonsterOwnerSelected.add(v);
										}
		
										//debPhrs("lastX-x : "+(int)(Math.abs(lastX-x))+" lastY-y : "+(int)(Math.abs(lastY-y))+"- (y-deltaY) : "+(int)(Math.abs(v.getY()-(y-deltaY)))+" v.get x - : "+(int)(Math.abs(v.getX()-(x+deltaX))) );
										// Donner la possibliter de selectionner une unité
										// SI LA LOOP N'EST PAS ASSEZ RAPIDE, Y A PAS DE SELECTION DONC A VOIR SI JE LAISSE ÇA LÀ
										if(game.arrayMonsterOwnerSelected.isEmpty() || Keyboard.isKeyDown(Input.KEY_LSHIFT))
											if(Math.abs(lastX-x) <= 25 && ((int)Math.abs(lastY-y)) <= 25)
												if(((int)Math.abs(v.getX()-(x+deltaX))) <= 25 && ((int)(Math.abs(v.getY()-(y-deltaY)))) <= 25){
													game.arrayMonsterOwnerSelected.add(v);
													break;
												}
									}
							}
							//if(!game.arrayMonsterOwnerSelected.isEmpty())
								game.arrayMonsterEnnemySelected.clear();

							// Selectionner unité Ennemie
							if(game.arrayMonsterOwnerSelected.isEmpty())
								for(Monster v : game.arrayMonsterOwner)
									if(v != null)
										if(Math.abs(lastX-x) <= 25 && ((int)Math.abs(lastY-y)) <= 25)
											if(((int)Math.abs(v.getX()-(x+deltaX))) <= 25 && ((int)(Math.abs(v.getY()-(y-deltaY)))) <= 25){
												game.arrayMonsterEnnemySelected.add(v);
												break;
											}
							
					 //debPhrs("y a : "+arrayMonsterOwnerSelected.size()+" et deltaX vaut : "+deltaX+" deltaY : "+deltaY+"\nx vaut : "+x+" y : "+y);
					
					// debPhrs("y a "+arrayMonsterOwner.size()+"unites");
					 m=0;lastX = x; lastY = y;
				 }
	    	}
    }
    /** Show rectangle of selection */
    private void afficherSelection(){	// ICI AUSSI
    	int i;
    	int x = Mouse.getX();
    	int y = Mouse.getY();
    	y=yPleinEcran-y;
		 if(Mouse.isButtonDown(0) && m==1){	// Bouton toujours presse, on commence a entourer la zone
			 // Gestion des X
			 if(x<lastX){
				 for(i=0;i<=lastX-x;i++)
					 _g.drawImage(barreSelec,lastX-i,lastY);	// de gauche haut vers droite haut
				 for(i=0;i<=lastX-x;i++)
					 _g.drawImage(barreSelec,lastX-i,y);	// de gauche bas vers droite bas
			 }else{
			 for(i=0;i<=x-lastX;i++)
				 _g.drawImage(barreSelec,lastX+i,lastY);	// de gauche haut vers droite haut
			 for(i=0;i<=x-lastX;i++)
				 _g.drawImage(barreSelec,lastX+i,y);	// de gauche bas vers droite bas
			 }
			 // Gestion des Y
			 if(y<lastY){
				 for(i=0;i<=lastY-y;i++)
					 _g.drawImage(barreSelec,lastX,lastY-i);	// de gauche bas vers haut
				 for(i=0;i<=lastY-y;i++)
					 _g.drawImage(barreSelec,x,lastY-i);	// de droite bas vers haut 
			 }else{
			 for(i=0;i<=y-lastY;i++)
				 _g.drawImage(barreSelec,lastX,lastY+i);	// de gauche haut vers bas
			 for(i=0;i<=y-lastY;i++)
				 _g.drawImage(barreSelec,x,lastY+i);	// de droite haut vers bas
			 }
		 }
    }
    /** Show Mage shoots */
    private void afficherTireMage(){	// Afficher seulement ce qui se trouve dans la vision du joueur ? (ça réduit vraiment les calculs ??) Car 
    	for(Tire o : game.arrayMonsterOwnerTire)
    		if(o != null){
    			//if(o.getDeltaX() != 0 || o.getDeltaY() != 0)
    			tire.setRotation(o.getRotationMage());
    			tire.draw((int)o.getX()-deltaX, (int)o.getY()+deltaY);
    			//_g.drawImage(tire, (int)o.getX()-deltaX,(int)o.getY()+deltaY);
    		}
    	for(Tire o : game.arrayMonsterEnnemyTire)
    	    if(o != null){
    	    	tire.setRotation(o.getRotationMage());
    			tire.draw((int)o.getX()-deltaX, (int)o.getY()+deltaY);
    	    	//_g.drawImage(tire, (int)o.getX()-deltaX,(int)o.getY()+deltaY);
    	    }
    }
    /** Show Tire arrows */
    private void afficherTireFleche(){	// Il arrive que des flèches ne bougent plus mais reste afficher (Donc faudrait n'afficher que celles dans sa vision ? moins de calculs ?). A régler.
    	for(Tire o : game.arrayTireFleche)
    		if(o != null)
    			if(o.getDeltaX() != 0 || o.getDeltaY() != 0){	//	PAS SÛR QUE C INTERESSANT
    				//_g.drawImage(tireFl, (int)o.getX()-deltaX,(int)o.getY()+deltaY);
    				tireFl.setRotation(o.getRotationMage());
    				tireFl.draw((int)o.getX()-deltaX, (int)o.getY()+deltaY);
    			}
    	
    }
    /** Show ennemy units */
    private void afficherUnitEnnemie(){
	    if(!game.arrayMonsterEnnemy.isEmpty())
			for(Monster v : game.arrayMonsterEnnemy)
				if(v != null){
					if(v.getX() >= deltaX && v.getX() <= (deltaX+FENLARG+10) && v.getY() >= (-deltaY) && v.getY() <= (-deltaY+FENHAUT+10)){ 
						switch(v.getMetamor()){
						case 0:
							units = nain2;
							break;
						case 1:
							units = fantasin2;
							break;
						case 2:
							units = minautaure2;
							break;
						case 3:
							units = archer2;
							break;
						case 4:
							units = centaure2;
							break;
						case 5:
							units = mage2;
							break;
						}
						
						if(game.CooldownOk(v))
							units.setRotation(0);
						else
							units.setRotation(-20.0f);
						
						// l'image est centre sur le point X et Y
						_g.drawImage(units,(int)(v.getX())-deltaX-(int)(units.getWidth()/2),(int)(v.getY())+deltaY-(int)(units.getHeight()/2));
					}
				}
    }
    /**  */
    private void afficherCercleSelection(){
    	try{
		if(!game.arrayMonsterOwnerSelected.isEmpty()){
			for(Monster v : game.arrayMonsterOwnerSelected)
				if(v != null)	// La différence entre les unités n'ait pas faite car ce n'est pas les images finales
					_g.drawImage(unitSelec,(int)(v.getX())-deltaX-(int)(unitSelec.getWidth()/2),(int)(v.getY())+deltaY-(int)(unitSelec.getHeight()/2));
		}else if(!game.arrayMonsterEnnemySelected.isEmpty()){	// Afficher le cercle de l'ennemie selectionner
			_g.drawImage(unitSelecR,(int)(game.arrayMonsterEnnemySelected.get(0).getX())-deltaX-(int)(unitSelecR.getWidth()/2),(int)(game.arrayMonsterEnnemySelected.get(0).getY())+deltaY-(int)(unitSelecR.getHeight()/2));
		}
    	}catch(NullPointerException e){}
    }
    /** Show owner units */
    private void afficherUnitJoueur(){
		if(!game.arrayMonsterOwner.isEmpty())
			for(Monster v : game.arrayMonsterOwner)
				if(v != null){
					if(v.getX() >= deltaX && v.getX() <= (deltaX+FENLARG+10) && v.getY() >= (-deltaY) && v.getY() <= (-deltaY+FENHAUT+10)){ 
					// Voir si je regarde si l'unité se trouve dans le rectangle de vue du joueur et afficher ou pas
						switch(v.getMetamor()){
						case 0:
							units = nain;
							break;
						case 1:
							units = fantasin;
							break;
						case 2:
							units = minautaure;
							break;
						case 3:
							units = archer;
							break;
						case 4:
							units = centaure;
							break;
						case 5:
							units = mage;
							break;
						}
						
						// Ajout du 2 Avril (Rotation si cooldown false => pour faire un effet d'attaque)
						if(game.CooldownOk(v))
							units.setRotation(0);
						else
							units.setRotation(-20.0f);
						
						// l'image est centre sur le point X et Y
						_g.drawImage(units,(int)(v.getX())-deltaX-(int)(units.getWidth()/2),(int)(v.getY())+deltaY-(int)(units.getHeight()/2));
					}
				}
    }
    /** Give info of life, mana, dmg, kill */
    private void afficherInfoUnitSelec(){
    	try{
	    	Color.white.bind();
	    	_g.setColor(Color.black);
	    	int k=0,l=0,i=0;
	    	int ma=0;
	    	if(!game.arrayMonsterOwnerSelected.isEmpty())
	    		if(game.arrayMonsterOwnerSelected.size() == 1){
	    			_g.drawString("Vie :"+game.arrayMonsterOwnerSelected.get(0).getVie(), 413, 670);
	    			_g.drawString("Mana :"+game.arrayMonsterOwnerSelected.get(0).getMana(), 413, 688);
	    			_g.drawString("Dmg :"+game.arrayMonsterOwnerSelected.get(0).getDmg(), 413, 706);
	    			_g.drawString("Tués :"+game.arrayMonsterOwnerSelected.get(0).getKills(), 413, 724);
	    			//_g.drawString("ID :"+game.arrayMonsterOwnerSelected.get(0).getId(), 540, 700);	// Pour debug
	    		
	    		}else{
	    			//fontNbUnit.drawString(413, 688,("Afficher les miniatures et vie, mana"), Color.black);
	    			for(k=0; k < game.arrayMonsterOwnerSelected.size();k++){
	        			if(k%10==0 && k != 0)
	        				l++;
	        			if(l>=1)
	        				ma=1;
	        			switch(game.arrayMonsterOwnerSelected.get(k).getMetamor()){
	        			case 0:
	        				ico = icoNain;
	        				break;
	        			case 1:
	        				ico = icoFanta;
	        				break;
	        			case 2:
	        				ico = icoMinau;
	        				break;
	        			case 3:
	        				ico = icoArcher;
	        				break;
	        			case 4:
	        				ico = icoCent;
	        				break;
	        			case 5:
	        				ico = icoMage;
	        				break;
        			}
	    			
        		try{
	        			_g.drawImage(ico,413+(ico.getWidth()+5)*(k%10),653+ico.getHeight()*l+4*l);	// Faudra afficher la vie et le mana plus tard donc un espace un peu plus grand sur la hauteur
	        			
	        			// Mettre la barre de vie
	        			_g.drawImage(barreVieG,413+(ico.getWidth()+5)*(k%10),653+22+ico.getHeight()*l+4*l);	// 5*l espace pour mana
	        			
	        			if(game.arrayMonsterOwnerSelected.get(k).getMetamor() == 0 || game.arrayMonsterOwnerSelected.get(k).getMetamor() == 1 || game.arrayMonsterOwnerSelected.get(k).getMetamor()==5)	// Pour afficher correctement (%)
		        			for(i=1;i<(int)(game.arrayMonsterOwnerSelected.get(k).getVie())/4-1;i++)
		        				_g.drawImage(barreVieM,413+(ico.getWidth()+5)*(k%10)+i,653+22+ico.getHeight()*l+4*l);
	        			
	        			if(game.arrayMonsterOwnerSelected.get(k).getMetamor() == 2)	// Pour minautaure
		        			for(i=1;i<(int)(game.arrayMonsterOwnerSelected.get(k).getVie())/6.1-1;i++)
		        				_g.drawImage(barreVieM,413+(ico.getWidth()+5)*(k%10)+i,653+22+ico.getHeight()*l+4*l);
	        			
	        			if(game.arrayMonsterOwnerSelected.get(k).getMetamor() == 3)	// Pour archer
		        			for(i=1;i<(int)(game.arrayMonsterOwnerSelected.get(k).getVie())/5.2-1;i++)
		        				_g.drawImage(barreVieM,413+(ico.getWidth()+5)*(k%10)+i,653+22+ico.getHeight()*l+4*l);
	        			
	        			if(game.arrayMonsterOwnerSelected.get(k).getMetamor() == 4)	// Pour centaure
		        			for(i=1;i<(int)(game.arrayMonsterOwnerSelected.get(k).getVie())/8-1;i++)
		        				_g.drawImage(barreVieM,413+(ico.getWidth()+5)*(k%10)+i,653+22+ico.getHeight()*l+4*l);
	        			
	        			_g.drawImage(barreVieD,413+(ico.getWidth()+5)*(k%10)+i+1,653+22+ico.getHeight()*l+4*l);
	    			
	        			if(l==0)
	        				ma=5;
	        			else if(l==1)
	        				ma=9;
	        			else if(l==2)
	        				ma=12;
	        			// Mettre la barre de mana
	        			_g.drawImage(barreManaG,413+(ico.getWidth()+5)*(k%10),653+22+ico.getHeight()*l+ma);
	        			
	        			if(game.arrayMonsterOwnerSelected.get(k).getMetamor() == 5)
	        				for(i=1;i<(int)(game.arrayMonsterOwnerSelected.get(k).getMana())/6-1;i++)
	        					_g.drawImage(barreManaM,413+(ico.getWidth()+5)*(k%10)+i,653+22+ico.getHeight()*l+ma);
	        			else
	        				for(i=1;i<(int)(game.arrayMonsterOwnerSelected.get(k).getMana())/4-1;i++)
	        					_g.drawImage(barreManaM,413+(ico.getWidth()+5)*(k%10)+i,653+22+ico.getHeight()*l+ma);
	        			
	        			_g.drawImage(barreManaD,413+(ico.getWidth()+5)*(k%10)+i+1,653+22+ico.getHeight()*l+ma);
        			}catch(IndexOutOfBoundsException e){}
    			}
    		}
    	}catch(Exception e){}
    	
    	// Donner info unité ennemie
    	if(game.arrayMonsterOwnerSelected.isEmpty() && !game.arrayMonsterEnnemySelected.isEmpty()){
    		_g.drawString("Vie :"+game.arrayMonsterEnnemySelected.get(0).getVie(), 413, 670);
			_g.drawString("Mana :"+game.arrayMonsterEnnemySelected.get(0).getMana(), 413, 688);
			_g.drawString("Dmg :"+game.arrayMonsterEnnemySelected.get(0).getDmg(), 413, 706);
			_g.drawString("Tués :"+game.arrayMonsterEnnemySelected.get(0).getKills(), 413, 724);
		//	_g.drawString("ID :"+game.arrayMonsterEnnemySelected.get(0).getId(), 540, 700);	// Pour debug
    	}
    }
    
    /** Afficher vie et mana au-dessus des unites */
    private void afficherVieManaInGame(){
    	if(affichVieMaInGameON){
    		int x, y,i=0;
    		int lar = (int)(ico.getWidth()/2), hau = (int)(ico.getHeight()/2);
    		int diff=5;
    		for(Monster v : game.arrayMonsterOwner){
    			if(v!=null){
    				x=(int)(v.getX());
    				y=(int)(v.getY());
		    		if(v.getX() >= deltaX && v.getX() <= (deltaX+FENLARG) && v.getY() >= (-deltaY) && v.getY() <= (-deltaY+FENHAUT)){  			   			
			    		// Mettre la barre de vie
						_g.drawImage(barreVieG,x-lar-deltaX,y+hau+deltaY);
						
						if(v.getMetamor() == 0 || v.getMetamor() == 1 || v.getMetamor()==5)	// Pour afficher correctement (%)
			    			for(i=1;i<(int)(v.getVie())/4-1;i++)
			    				_g.drawImage(barreVieM,x-lar+i-deltaX, y+hau+deltaY);
						
						if(v.getMetamor() == 2)	// Pour minautaure
			    			for(i=1;i<(int)(v.getVie())/6.1-1;i++)
			    				_g.drawImage(barreVieM,x-lar+i-deltaX, y+hau+deltaY);
						
						if(v.getMetamor() == 3)	// Pour archer
			    			for(i=1;i<(int)(v.getVie())/5.2-1;i++)
			    				_g.drawImage(barreVieM,x-lar+i-deltaX, y+hau+deltaY);
						
						if(v.getMetamor() == 4)	// Pour centaure
			    			for(i=1;i<(int)(v.getVie())/8-1;i++)
			    				_g.drawImage(barreVieM,x-lar+i-deltaX, y+hau+deltaY);
						
						_g.drawImage(barreVieD,x-lar+i+1-deltaX, y+hau+deltaY);
					
						// Mettre la barre de mana
						_g.drawImage(barreManaG,x-lar-deltaX, y+hau+diff+deltaY);
						
						if(v.getMetamor() == 5)
							for(i=1;i<(int)(v.getMana())/6-1;i++)
								_g.drawImage(barreManaM,x-lar+i-deltaX, y+hau+diff+deltaY);
						else
							for(i=1;i<(int)(v.getMana())/4-1;i++)
								_g.drawImage(barreManaM,x-lar+i-deltaX, y+hau+diff+deltaY);
						
						_g.drawImage(barreManaD,x-lar+i+1-deltaX, y+hau+diff+deltaY);
		    		}
    			}
    		}// Fin pour owner
    		
    		for(Monster v : game.arrayMonsterEnnemy){
    			if(v!=null){
    				x=(int)(v.getX());
    				y=(int)(v.getY());
		    		if(v.getX() >= deltaX && v.getX() <= (deltaX+FENLARG) && v.getY() >= (-deltaY) && v.getY() <= (-deltaY+FENHAUT)){  			   			
			    		// Mettre la barre de vie
						_g.drawImage(barreVieG,x-lar-deltaX,y+hau+deltaY);
						
						if(v.getMetamor() == 0 || v.getMetamor() == 1 || v.getMetamor()==5)	// Pour afficher correctement (%)
			    			for(i=1;i<(int)(v.getVie())/4-1;i++)
			    				_g.drawImage(barreVieM,x-lar+i-deltaX, y+hau+deltaY);
						
						if(v.getMetamor() == 2)	// Pour minautaure
			    			for(i=1;i<(int)(v.getVie())/6.1-1;i++)
			    				_g.drawImage(barreVieM,x-lar+i-deltaX, y+hau+deltaY);
						
						if(v.getMetamor() == 3)	// Pour archer
			    			for(i=1;i<(int)(v.getVie())/5.2-1;i++)
			    				_g.drawImage(barreVieM,x-lar+i-deltaX, y+hau+deltaY);
						
						if(v.getMetamor() == 4)	// Pour centaure
			    			for(i=1;i<(int)(v.getVie())/8-1;i++)
			    				_g.drawImage(barreVieM,x-lar+i-deltaX, y+hau+deltaY);
						
						_g.drawImage(barreVieD,x-lar+i+1-deltaX, y+hau+deltaY);
					
						// Mettre la barre de mana
						_g.drawImage(barreManaG,x-lar-deltaX, y+hau+diff+deltaY);
						
						if(v.getMetamor() == 5)
							for(i=1;i<(int)(v.getMana())/6-1;i++)
								_g.drawImage(barreManaM,x-lar+i-deltaX, y+hau+diff+deltaY);
						else
							for(i=1;i<(int)(v.getMana())/4-1;i++)
								_g.drawImage(barreManaM,x-lar+i-deltaX, y+hau+diff+deltaY);
						
						_g.drawImage(barreManaD,x-lar+i+1-deltaX, y+hau+diff+deltaY);
		    		}
    			}
    		}// Fin pour ennemie
    		
    	}	// Fin du if de viEtManaON
    }
    /** Liste des unites acheter, afficher en bas à gauche au dessus de la mini-map */
    private void afficherListeUnitAcheter(){
    	int k,diff=0;
    	if(!game.arrayMonsterOwnerBought.isEmpty())
    		for(k=0; k < game.arrayMonsterOwnerBought.size();k++){
    			if(k>=10)
    				break;
    		try{
    			switch(game.arrayMonsterOwnerBought.get(k)){
    			case 0:
    				ico = icoNain;
    				break;
    			case 1:
    				ico = icoFanta;
    				break;
    			case 2:
    				ico = icoMinau;
    				break;
    			case 3:
    				ico = icoArcher;
    				break;
    			case 4:
    				ico = icoCent;
    				break;
    			case 5:
    				ico = icoMage;
    				break;
    			}
    		}catch(IndexOutOfBoundsException e){}
    			_g.drawImage(ico,1+28*k,588);
    			if(!game.arrayMonsterOwnerBoughtTime.isEmpty()){
    				try{
    					diff = (int)( (game.arrayMonsterOwnerBoughtTime.get(k) - getTime() ) / 1000)+Game.TIME_MAX_AVANT_SPAWN;
    				}catch(IndexOutOfBoundsException e){}
	    			if(diff>2){
	    				//Color.green.bind();
	    				_g.setColor(Color.green);
	    				_g.drawString(""+diff,4+28*k, 600);
	    			}else{
	    				//Color.red.bind();
	    				_g.setColor(Color.red);
	    				_g.drawString(""+diff,4+28*k, 600);
	    			}
	    			//Color.white.bind();
	    	    	_g.setColor(Color.white);
    			}
    		}
    	
    	// JOUEUR 2
    	diff=0;
    	if(!game.arrayMonsterEnnemyBought.isEmpty())
    		for(k=0; k < game.arrayMonsterEnnemyBought.size();k++){
    			if(k>=10)
    				break;
    			try{
	    			switch(game.arrayMonsterEnnemyBought.get(k)){
	    			case 0:
	    				ico = icoNain;
	    				break;
	    			case 1:
	    				ico = icoFanta;
	    				break;
	    			case 2:
	    				ico = icoMinau;
	    				break;
	    			case 3:
	    				ico = icoArcher;
	    				break;
	    			case 4:
	    				ico = icoCent;
	    				break;
	    			case 5:
	    				ico = icoMage;
	    				break;
	    			}
    			}catch(IndexOutOfBoundsException e){}
    			_g.drawImage(ico,1+28*k,588);
    			if(!game.arrayMonsterEnnemyBoughtTime.isEmpty()){
    				try{
    					diff = (int)( (game.arrayMonsterEnnemyBoughtTime.get(k) - getTime() ) / 1000)+Game.TIME_MAX_AVANT_SPAWN;
    				}catch(IndexOutOfBoundsException e){}
	    			if(diff>2){
	    				//Color.green.bind();
	    				_g.setColor(Color.green);
	    				_g.drawString(""+diff,4+28*k, 600);
	    			}else{
	    				//Color.red.bind();
	    				_g.setColor(Color.red);
	    				_g.drawString(""+diff,4+28*k, 600);
	    			}
	    			//Color.white.bind();
	    	    	_g.setColor(Color.white);
    			}
    		}
    	
    }
    /** Affiche points, nb d'unite, timer, transformation ou achat */
    /** Afficher points,argent, nb unité, timer, transf ou achat */
    private void afficherInfoReste(){
    	_g.setColor(Color.black);
    	_g.setFont(fontJeu);
    	
    	_g.drawString(""+game.pointsJ1, 772, 3);
    	_g.drawString(""+game.pointsJ2, 930, 3);
    	if(joueur==1)
    		_g.drawString(""+game.argentJ1, 1040, 3);
    	else
    		_g.drawString(""+game.argentJ2, 1040, 3);
    	
    	_g.drawString(""+Monster.nbreUnite, 280, 653);
    	_g.drawString(""+Monster.nbreNain, 275, 665);
    	_g.drawString(""+Monster.nbreFantassin, 303, 677);
    	_g.drawString(""+Monster.nbreMinautaure, 310, 690);
    	_g.drawString(""+Monster.nbreArcher, 285, 702);
    	_g.drawString(""+Monster.nbreCentaure, 297, 714);
    	_g.drawString(""+Monster.nbreMage, 273, 726);
    	
    	//	TIMER
		if(game.minTimer<10 && game.secTimer < 10)
			_g.drawString("0"+game.minTimer+" : 0"+game.secTimer,1047, 596);
		else if(game.minTimer<10)
			_g.drawString("0"+game.minTimer+" : "+game.secTimer,1047, 596);
		else
			_g.drawString(""+game.minTimer+" : "+game.secTimer, 1047, 596);
		
		//	TRANSFORMATION OU ACHAT	
		if(!game.arrayMonsterOwnerSelected.isEmpty())
			_g.drawString("Transformation",870, 675);
		else
			_g.drawString("Achat",870, 675);
		
    }
    /** Affiche groupe de sélection */
    private void afficherGroupSelection(){
    	_g.setColor(Color.black);
    	
    	if(!game.arrayMonsterOwnerGroup1.isEmpty()){
    		_g.drawImage(jeuGroupUnit, 240, 616);
    		_g.drawString(""+game.arrayMonsterOwnerGroup1.size(),250, 618);
    	}
		if(!game.arrayMonsterOwnerGroup2.isEmpty()){
    		_g.drawImage(jeuGroupUnit, 280, 616);
			_g.drawString(""+game.arrayMonsterOwnerGroup2.size(), 290, 618);
		}
		if(!game.arrayMonsterOwnerGroup3.isEmpty()){
    		_g.drawImage(jeuGroupUnit, 320, 616);
			_g.drawString(""+game.arrayMonsterOwnerGroup3.size(), 330, 618);
		}
		if(!game.arrayMonsterOwnerGroup4.isEmpty()){
    		_g.drawImage(jeuGroupUnit, 360, 616);
			_g.drawString(""+game.arrayMonsterOwnerGroup4.size(), 370, 618);
		}
		if(!game.arrayMonsterOwnerGroup5.isEmpty()){
    		_g.drawImage(jeuGroupUnit, 400, 616);
			_g.drawString(""+game.arrayMonsterOwnerGroup5.size(), 410, 618);
		}
		if(!game.arrayMonsterOwnerGroup6.isEmpty()){
    		_g.drawImage(jeuGroupUnit, 440, 616);
			_g.drawString(""+game.arrayMonsterOwnerGroup6.size(), 450, 618);
		}
		if(!game.arrayMonsterOwnerGroup7.isEmpty()){
    		_g.drawImage(jeuGroupUnit, 480, 616);
			_g.drawString(""+game.arrayMonsterOwnerGroup7.size(), 490, 618);
		}
		if(!game.arrayMonsterOwnerGroup8.isEmpty()){
    		_g.drawImage(jeuGroupUnit, 520, 616);
			_g.drawString(""+game.arrayMonsterOwnerGroup8.size(), 530, 618);
		}
		if(!game.arrayMonsterOwnerGroup9.isEmpty()){
    		_g.drawImage(jeuGroupUnit, 560, 616);
			_g.drawString(""+game.arrayMonsterOwnerGroup9.size(), 570, 618);
		}
    }
    /** Affiche les raccourcis, change on AZERTY or QWERTY */
    private void afficherRaccourcis(){
    	if(raccourcisON){
    		_g.setColor(Color.white);
    		_g.setFont(fontRaccour);
    		if(qwertyON){
    			_g.drawString("q", 860, 647);
    			_g.drawString("w", 912, 647);
    			_g.drawString("e", 960, 647);
    			_g.drawString("r", 1010, 647);
    			_g.drawString("t", 1060, 647);
    			
      			_g.drawString("a", 860, 733);
    			_g.drawString("s", 900, 733);
    			_g.drawString("d", 944, 733);
    			_g.drawString("f", 986, 733);
    			_g.drawString("g", 1025, 733);
    			_g.drawString("h", 1065, 733);
    		}else{
    			_g.drawString("a", 860, 647);
    			_g.drawString("z", 912, 647);
    			_g.drawString("e", 960, 647);
    			_g.drawString("r", 1010, 647);
    			_g.drawString("t", 1060, 647);
    			
      			_g.drawString("q", 860, 733);
    			_g.drawString("s", 900, 733);
    			_g.drawString("d", 944, 733);
    			_g.drawString("f", 986, 733);
    			_g.drawString("g", 1025, 733);
    			_g.drawString("h", 1065, 733);
    		}
    		_g.setFont(fontLogin);
    	}
    }
    /** Surbrillance sur passqge de a souris sur les icones pour acheter, transformer et changer attaque, stop, ... */
    private void afficherSurbrillance(){
    	if(gameAttaON)
    		_g.drawImage(surbrillanceIcon, 861,622);
    	if(gameStopON)
    		_g.drawImage(surbrillanceIcon, 911,622);
    	if(gameTenirON)
    		_g.drawImage(surbrillanceIcon, 960,622);
    	if(gameEtoileON) 
    		_g.drawImage(surbrillanceIcon, 1009,622);
    	if(gameRectON)
    		_g.drawImage(surbrillanceIcon, 1058,622);
    	
		if(gameNainON) 
			_g.drawImage(surbrillanceIcon, 857,707);
		if(gameFantaON) 
			_g.drawImage(surbrillanceIcon, 898,707);
		if(gameMinauON) 
			_g.drawImage(surbrillanceIcon, 939,707);
		if(gameArchON) 
			_g.drawImage(surbrillanceIcon, 980,707);
		if(gameCentON) 
			_g.drawImage(surbrillanceIcon, 1020,707);
		if(gameMageON)
			_g.drawImage(surbrillanceIcon, 1060,707);
    }
    /** Affiche du sang et enlève ceux excedant TEMPS_SANG_RENDER et en laisse 1 dans l'array*/
    private void afficherSang(){
    	if(game != null)
	    	if(game.sangActive)
	    		for(TacheSang o : game.arrayTacheSang)
		    		if(o != null)
		    			if(o.getX() >= deltaX && o.getX() <= (deltaX+FENLARG) && o.getY() >= (-deltaY) && o.getY() <= (-deltaY+FENHAUT)){
		    				switch(o.getType()){
		    				case 0:
		    					sang = sang0;
		    					break;
		    				case 1:
		    					sang = sang1;
		    					break;
		    				case 2:
		    					sang = sang2;
		    					break;
		    				case 3:
		    					sang = sang3;
		    					break;
		    				case 4:
		    					sang = sang4;
		    					break;
		    				default:
		    					sang = sang0;
		    					break;
		    				}
		    				_g.drawImage(sang, o.getX()-deltaX,o.getY()+deltaY);
		    			}
    	// Fait ici pour éviter que j'enlève des trucs dans l'array pendant l'affichage
    	if(!game.arrayTacheSang.isEmpty())
    		if(game.arrayTacheSang.get(0) != null)
    			try{
    				for(int i=0; i<game.arrayTacheSang.size()-1;i++)	// Un de laisser
			    		if( ((int)(  ((game.arrayTacheSang.get(0).getTempsCreation())  - getTime() ) / 1000) + TEMPS_SANG_RENDER) <= 0){
			    			game.arrayTacheSang.set(0, null);
			    			game.arrayTacheSang.remove(0);
			    		}else{
			    			break;
			    		}
    			}catch(Exception e){}
    	
    }
    
    /** Saisie de texte inGame */
    private void afficherTextField(GameContainer gc){
    	if(textInGameChatON){		
    		_g.drawImage(chatInGame,300,605);  
    		textInGameChat.render(gc, _g);
    	}
    }
    /** Texte de discussion, disparais après 8 sec. Pour le solo, c'est 15 sec et écrit en vert */
    private void afficherTexteDiscussion(){
    	int i=0;
    	if(clientReceiver!=null){
    		for(String m : clientReceiver.arrayServerTextInGameChat){
    			if(m!=null)
    				if(m.charAt(0)=='1'){
    					if(m.length()>2)
    						m=m.substring(2);
    					_g.setColor(Color.blue);
						//_g.drawString(""+m,300, 585-i*19);	// Bas vers haut
    					//_g.drawString(""+m,300, 565-clientReceiver.arrayServerTextInGameChat.size()+i*19);
						_g.drawString(""+m,300, 435+i*19);
    				}else{
    					if(m.length()>2)
    						m=m.substring(2);
    					_g.setColor(Color.red);
    					//_g.drawString(""+m,300, 585-i*19);	// Bas vers haut
    					//_g.drawString(""+m,300, 565-clientReceiver.arrayServerTextInGameChat.size()+i*19);
    					_g.drawString(""+m,300, 435+i*19);
    				}
    			i++;
    		}
    		// Fait ici car faut pas que la boucle soit entrain de se faire et que l'on supprime un message
    		if(!clientReceiver.arrayServerTextInGameChat.isEmpty())
    			if(clientReceiver.arrayServerTextInGameChatTime.isEmpty()){
    				clientReceiver.arrayServerTextInGameChat.remove(0);
    			}else{
    				if( ((int)(  ((clientReceiver.arrayServerTextInGameChatTime.get(0))  - getTime() ) / 1000) + 8) <= 0){
    					clientReceiver.arrayServerTextInGameChat.remove(0);
    					clientReceiver.arrayServerTextInGameChatTime.remove(0);
    				}
    			}
    	}
    	i=0;
    	// Afficher les instructions pour J1  (ce qu'il doit faire)
    	if(gameSolo && game!=null){
    		for(String m : game.arrayServerTextInGameChatSolo){
    			if(!game.arrayServerTextInGameChatTimeSolo.isEmpty())
    				try{	// Le if sert à afficher seulement les messages nécessaire en temps voulu
    					if( ((int)(  ((game.arrayServerTextInGameChatTimeSolo.get(i))  - getTime() ) / 1000) + 15) >= 0 && ((int)(  ((game.arrayServerTextInGameChatTimeSolo.get(i))  - getTime() ) / 1000) + 15) <= 16 ){
			    			if(m!=null)
			    				/*if(m.charAt(0)=='1'){
			    					if(m.length()>2)
			    						m=m.substring(2);
			    					_g.setColor(Color.blue);
									_g.drawString(""+m,300, 585-i*19);
			    				}else{
			    					if(m.length()>2)
			    						m=m.substring(2);
			    					_g.setColor(Color.blue);	// Une couleur qui se voit bien sur le noir et le blanc pour les explications
			    					_g.drawString(""+m,300, 585-i*19);
			    				}*/
			    				if(m.length()>2)
		    						m=m.substring(2);
		    					_g.setColor(Color.blue);	// Une couleur qui se voit bien sur le noir et le blanc pour les explications
		    					//_g.drawString(""+m,300, 585-i*19); Soit comme ça
		    					_g.drawString(""+m,300, 415+i*19);
			    			i++;
    					}else{
    						//break;
    						// le reste, pas besoin d'être affiché
    					}
		    		}catch(Exception e){ debPhrs("out of range"+i);}	// Out of array range
    		}
    		// Fait ici car faut pas que la boucle soit entrain de se faire et que l'on supprime un message
    		if(!game.arrayServerTextInGameChatSolo.isEmpty())
    			if(game.arrayServerTextInGameChatTimeSolo.isEmpty()){
    				game.arrayServerTextInGameChatSolo.remove(0);
    			}else{
    				if( ((int)(  ((game.arrayServerTextInGameChatTimeSolo.get(0))  - getTime() ) / 1000) + 15) <= 0){
    					game.arrayServerTextInGameChatSolo.remove(0);
    					game.arrayServerTextInGameChatTimeSolo.remove(0);
    				}
    			}
    	}
    	_g.setColor(Color.white);
    }
    /** Texte Unité créé, Achat impossible, Vous etes attaque... */
    private void afficherTexteError(){
    	int i=0;
    	if(game!=null){
    		try{
	    		for(String m : game.arrayServerTextInGameError){	// Le maximum de msg est dans Game
	    			if(m!=null)
	    				if(!m.equalsIgnoreCase("")){	// L'espace dû au i est volontairement laissé
	    					_g.setColor(Color.red);
	    					_g.drawString(""+m,5, 240+i*19);
	    				}
	    			i++;
	    		}
    		}catch(Exception e){}
    	}
    }
    
    /** Appeler dans Render */
    private void afficherObjectif(){
    	if(objectifON){
    		_g.drawImage(objectifZone,360,120);
    		if(game.objectifs != null){
    			//_g.drawString(game.objectifs,370,180);
    			String buffer[] = game.objectifs.split("-");
    			for(int i=0;i<buffer.length;i++)
    				_g.drawString(buffer[i],370,180+i*19);
    		}
    	}
    }
    private void gererBordsPleinEcran(){
    	if(ecranTaille1280){ // 1280x1024
    		_g.setColor(Color.black);
    		_g.fillRect(1100, 0, 180, 800);
    		_g.fillRect(0, 750, 1280, 274);
    		_g.setColor(Color.white);
    	}
    	if(ecranTaille1280x800){
    		_g.setColor(Color.black);
    		_g.fillRect(1100, 0, 180, 800);
    		_g.fillRect(0, 750, 1280, 50);
    		_g.setColor(Color.white);	
    	}
    	if(ecranTaille1366){ // 1366x768
    		_g.setColor(Color.black);
    		_g.fillRect(1100, 0, 266, 800);
    		_g.fillRect(0, 750, 1366, 18);
    		_g.setColor(Color.white);
    	}
    		
    	if(ecranTaille1440){
    		_g.setColor(Color.black);
    		_g.fillRect(1100, 0, 340, 800);
    		_g.fillRect(0, 750, 1440, 150);
    		_g.setColor(Color.white);
    	}
    	if(ecranTaille1280 || ecranTaille1366 || ecranTaille1440 || ecranTaille1280x800){
    		bord.setRotation(0.0f);
	    	bord.draw(-240,749);
	    	bord.draw(210,749);
	    	bord.draw(655,749);
	    	bord.setRotation(90.0f);
	    	bord.draw(878,110);
	    	bord.draw(878,525);
    	}
    }
	
    
    /** Brouillard de guerre */
    private void brouillard(){
    	Rectangle rect = new Rectangle(0, 0, FENLARG, FENHAUT);
    	Color couleur = new Color(40,40,40,0);		// Changer le 4eme int pour plus ou moins noir (c'est le fog)
    	Color couleur2 = new Color(20,20,20,255);	// Vision mais pas attaque
    	//gFog.setWorldClip(rect);
    	gFog.setColor(couleur);
        gFog.fill(rect);
 
        gFog.setColor(couleur2);
        if(joueur==1){
        for(Monster v : game.arrayMonsterOwner)
        	if(v != null)
        		//if(v.getX() >= deltaX && v.getX() <= (deltaX+FENLARG) && v.getY() >= (-deltaY) && v.getY() <= (-deltaY+FENHAUT))
        		gFog.fillOval((int)(v.getX())-deltaX-(int)(units.getWidth()) - 250,  (int)(v.getY())+deltaY-(int)(units.getHeight()) - 260,560,560);
        gFog.setColor(Color.white);
        for(Monster v : game.arrayMonsterOwner)
        	if(v != null)
        		//if(v.getX() >= deltaX && v.getX() <= (deltaX+FENLARG) && v.getY() >= (-deltaY) && v.getY() <= (-deltaY+FENHAUT))
        		gFog.fillOval((int)(v.getX())-deltaX-(int)(units.getWidth()) - 200,  (int)(v.getY())+deltaY-(int)(units.getHeight()) - 210,460,460);
        //*/
        }else{
        	for(Monster v : game.arrayMonsterEnnemy)
            	if(v != null)
            		//if(v.getX() >= deltaX && v.getX() <= (deltaX+FENLARG) && v.getY() >= (-deltaY) && v.getY() <= (-deltaY+FENHAUT))
            		gFog.fillOval((int)(v.getX())-deltaX-(int)(units.getWidth()) - 250,  (int)(v.getY())+deltaY-(int)(units.getHeight()) - 260,560,560);
            gFog.setColor(Color.white);
            for(Monster v : game.arrayMonsterEnnemy)
            	if(v != null)
            		//if(v.getX() >= deltaX && v.getX() <= (deltaX+FENLARG) && v.getY() >= (-deltaY) && v.getY() <= (-deltaY+FENHAUT))
            		gFog.fillOval((int)(v.getX())-deltaX-(int)(units.getWidth()) - 200,  (int)(v.getY())+deltaY-(int)(units.getHeight()) - 210,460,460);
        }
        _g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);   
        _g.drawImage(fog, 0, 0);
        _g.setDrawMode(Graphics.MODE_NORMAL);
        gFog.clear();
        gFog.resetFont();
        gFog.resetTransform();
    }
    /** Brouillard de guerre sur la minimap */
    private void brouillardMiniMap(){
    	// Les siens en carré bleu, ennemie: carré rouge
    	Rectangle rect = new Rectangle(0, 622, 1900, 1208);
    	Color couleur = new Color(40,40,40,0);		// Changer le 4eme int pour plus ou moins noir (c'est le fog)
    	Color couleur2 = new Color(20,20,20,255);	// Vision mais pas attaque
    	//gFog.setWorldClip(rect);
    	gFog2.setColor(couleur);
        gFog2.fill(rect);
        //*
        gFog2.setColor(couleur2);
        if(joueur==1){
        for(Monster v : game.arrayMonsterOwner)
        	if(v != null)
        		gFog2.fillOval((int)(v.getX()/17)+5-14,(int)(v.getY()/21)-12,30,30);
        gFog2.setColor(Color.white);
        for(Monster v : game.arrayMonsterOwner)
        	if(v != null)
        		gFog2.fillOval((int)(v.getX()/17)+5-11,(int)(v.getY()/21)-9,25,25);
        
        }else{
        	for(Monster v : game.arrayMonsterEnnemy)
            	if(v != null)
            		gFog2.fillOval((int)(v.getX()/17)+5-14,(int)(v.getY()/21)-12,30,30);
            gFog2.setColor(Color.white);
            for(Monster v : game.arrayMonsterEnnemy)
            	if(v != null)
            		gFog2.fillOval((int)(v.getX()/17)+5-11,(int)(v.getY()/21)-9,25,25);
        }
        _g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);   
        _g.drawImage(fog2, 0, 622);
        _g.setDrawMode(Graphics.MODE_NORMAL);
        gFog2.clear();
        gFog2.resetFont();
        gFog2.resetTransform();//*/
    }
    
    /** 
     * Affiche des carres bleu ou rouge (unites) sur la minimap
     * Show red or blue unit on the minimap 
     * */
    private void afficherUnitMiniMap(){ 
	   if(!game.arrayMonsterOwner.isEmpty())
			for(Monster v : game.arrayMonsterOwner)
				if(v != null)
					_g.drawImage(carreB,(int)(v.getX()/17)+5,(int)(v.getY()/21)+625);	// Un peu de décalage par rapport aux bords de la minimap (+5)
	   if(!game.arrayMonsterEnnemy.isEmpty())
			for(Monster v : game.arrayMonsterEnnemy)
				if(v != null)
					_g.drawImage(carreR,(int)(v.getX()/17)+5,(int)(v.getY()/21)+625);
    }
	
    /** Affiche un rectangle vide bord blanc qui représente ce que voit le joueur avec la camera */
    private void afficherRectangleMiniMap(){
    	int xModif=0;
    	_g.setColor(Color.white);
    	//if(deltaY<-1785)
    		//yModif=(int)((1890+deltaY)/6);
    	if(deltaX> 100)
    		xModif=7;
    	_g.drawRect(deltaX/15+4-xModif, -deltaY/20+625, 65, 35/*-yModif*/);
    }
    
    /** Change destX et destY de l'unite pour creer un rectangle */
    /** Change destX and destY depending on countMonsterEtoile, fais un rectangle */
    private void collisionRectangle(Monster v){
    	if(v!=null){
    		int x = v.getDestX();
    		int y = v.getDestY();
    		
    		switch(countMonsterEtoile){
			case 0:
				
				break;
			case 1:
				v.changeDestination(x+30,y);
				break;
			case 2:
				v.changeDestination(x,y+30);
				break;
			case 3:
				v.changeDestination(x-30,y);
				break;
			case 4:
				v.changeDestination(x,y-30);
				break;
			case 5:
				v.changeDestination(x+30,y-30);
				break;
			case 6:
				v.changeDestination(x+30,y+30);
				break;
			case 7:
				v.changeDestination(x-30,y+30);
				break;
			case 8:
				v.changeDestination(x-30,y-30);
				break;
			case 9:
				v.changeDestination(x+60,y-30);
				break;
			case 10:
				v.changeDestination(x+60,y);
				break;
			case 11:
				v.changeDestination(x+60,y+30);
				break;
			case 12:
				v.changeDestination(x+60,y+60);
				break;
			case 13:
				v.changeDestination(x+30,y+60);
				break;
			case 14:
				v.changeDestination(x,y+60);
				break;
			case 15:
				v.changeDestination(x-30,y+60);
				break;
			case 16:
				v.changeDestination(x-60,y+60);
				break;
			case 17:
				v.changeDestination(x-60,y+30);
				break;
			case 18:
				v.changeDestination(x-60,y);
				break;
			case 19:
				v.changeDestination(x-60,y-30);
				break;
			case 20:
				v.changeDestination(x-60,y-60);
				break;
			case 21:
				v.changeDestination(x-30,y-60);
				break;
			case 22:
				v.changeDestination(x,y-60);
				break;
			case 23:
				v.changeDestination(x+30,y-60);
				break;
			case 24:
				v.changeDestination(x+60,y-60);
				break;
			case 25:
				v.changeDestination(x+90,y);
				break;
			case 26:
				v.changeDestination(x+90,y+30);
				break;	
			case 27:
				v.changeDestination(x+90,y-30);
				break;	
			case 28:
				v.changeDestination(x+90,y+60);
				break;	
			case 29:
				v.changeDestination(x+90,y-60);
				break;	
			case 30:
				//v.changeDestination(x+90,y);	A VOIR CAR sa fait pas rect sinon
				break;	
			default:
				
				break;
		}
    		if(v.getDestX() <= 10)
    			v.changeDestination(10,v.getDestY());
    		if(v.getDestY() <= 10)
    			v.changeDestination(v.getDestX(),10);
    		if(v.getDestX() >= XMAXUNIT)
    			v.changeDestination(XMAXUNIT,v.getDestY());
    		if(v.getDestY() >= YMAXUNIT)
    			v.changeDestination(v.getDestX(),YMAXUNIT);
    			
    		
    		countMonsterEtoile++;
    	}
    }
    /** Change destX and destY depending on countMonsterEtoile, une sorte d'étoile */
    private void collisionEtoile(Monster v){
    	// Voir si je fais un arrayList qui stocke les ID des unites en mvt car j'aurai surement un arrylist pour les unites en mvt attaque
    		if(v!=null){
    				int x = v.getDestX();
    				int y = v.getDestY();
    				
    				switch(countMonsterEtoile){
    					case 0:
    						
    						break;
    					case 1:
    						v.changeDestination(x+30,y);
    						break;
    					case 2:
    						v.changeDestination(x+30,y+30);
    						break;
    					case 3:
    						v.changeDestination(x,y+30);
    						break;
    					case 4:
    						v.changeDestination(x-30,y+30);
    						break;
    					case 5:
    						v.changeDestination(x-30,y);
    						break;
    					case 6:
    						v.changeDestination(x-30,y-30);
    						break;
    					case 7:
    						v.changeDestination(x,y-30);
    						break;
    					case 8:
    						v.changeDestination(x+30,y-30);
    						break;
    					case 9:
    						v.changeDestination(x+60,y);
    						break;
    					case 10:
    						v.changeDestination(x+60,y+60);
    						break;
    					case 11:
    						v.changeDestination(x,y+60);
    						break;
    					case 12:
    						v.changeDestination(x-60,y+60);
    						break;
    					case 13:
    						v.changeDestination(x-60,y);
    						break;
    					case 14:
    						v.changeDestination(x-60,y-60);
    						break;
    					case 15:
    						v.changeDestination(x,y-60);
    						break;
    					case 16:
    						v.changeDestination(x+60,y-60);
    						break;
    					case 17:
    						v.changeDestination(x+90,y);
    						break;
    					case 18:
    						v.changeDestination(x+90,y+90);
    						break;
    					case 19:
    						v.changeDestination(x,y+90);
    						break;
    					case 20:
    						v.changeDestination(x-90,y+90);
    						break;
    					case 21:
    						v.changeDestination(x-90,y);
    						break;
    					case 22:
    						v.changeDestination(x-90,y-90);
    						break;
    					case 23:
    						v.changeDestination(x,y-90);
    						break;
    					case 24:
    						v.changeDestination(x+90,y-90);
    						break;
    					case 25:
    						v.changeDestination(x+120,y);
    						break;
    					case 26:
    						v.changeDestination(x+60,y-30);
    						break;	
    					case 27:
    						v.changeDestination(x+60,y+30);
    						break;	
    					case 28:
    						v.changeDestination(x-60,y+30);
    						break;	
    					case 29:
    						v.changeDestination(x-60,y-30);
    						break;	
    					case 30:
    						
    						break;	
    					default:
    						
    						break;
    				}
    				countMonsterEtoile++;
    	    		if(v.getDestX() <= 10)
    	    			v.changeDestination(10,v.getDestY());
    	    		if(v.getDestY() <= 10)
    	    			v.changeDestination(v.getDestX(),10);
    	    		if(v.getDestX() >= XMAXUNIT)
    	    			v.changeDestination(XMAXUNIT,v.getDestY());
    	    		if(v.getDestY() >= YMAXUNIT)
    	    			v.changeDestination(v.getDestX(),YMAXUNIT);
    				//v.setUnMoved();
    			//}
    		}
    }
    
    /** Move camera to the position clicked on the minimap */
    private void changeDeltaMinimap(int x,int y){
    	
    	if(x>0 && x <190 && y < 750 && y > 625){
    		y=y-625;
    		
    		if(x<40)
    			x-=20;
    		if(x>140)
    			x+=40;
    		if(y<30 && y>=10)
    			y-=10;
    		if(y>90)
    			y+=20;
    		
    		deltaX=x*10;
    		deltaY=(int)(y*(-15.3));
    		
	     	if(deltaX<0)
	     		deltaX = 0;
	     	if(deltaX>1900)
	     		deltaX = 1900;
	     	if(deltaY<-1910)
	     		deltaY = -1910;
	     	if(deltaY>0)
	     		deltaY = 0;
    	}
    }
    
    // **************
    // FONCTIONS RESEAUX ET SONT RÉUTILISABLE POUR LE SOLO
    // **************
    
    private void changeJ1ouJ2Message(){
	   if(joueur==1 && messageJeu.equalsIgnoreCase(""))
			messageJeu="J1";
		if(joueur==2 && messageJeu.equalsIgnoreCase(""))
			messageJeu="J2";
   }
   
    /** Send information units changed their destination */
    private void deplaceUnit(int x, int y){
	   countMonsterEtoile=0;	// Init
		messageJeu="";
		for(Monster v : game.arrayMonsterOwnerSelected)
			if(v != null){
				if(moveAttackSav)
					v.setMoveAttack(true);
				else
					v.setMoveAttack(false);
		
					
				v.changeDestination(x+deltaX, y-deltaY);
				v.setTenirPos(false);
				if(posUnitStrategy == 1)
					collisionEtoile(v);
				else
					collisionRectangle(v);
				
				changeJ1ouJ2Message();
				
				messageJeu+="deplace-"+v.getId()+"-"+v.getDestX()+"-"+v.getDestY()+"-"+v.getMetamor()+"-"+v.getMoveAttack()+":";
			}
		if(!messageJeu.equals("J1") && !messageJeu.equals("J2") && !messageJeu.equals(""))
			if(clientReceiver!=null)
				clientReceiver.sendAnyMsg("all"+messageJeu);
   }
    /** Send information unit has stopped moving */
    private void stopUnit(){
	   messageJeu="";
   	for(Monster v : game.arrayMonsterOwnerSelected)
   		if(v != null){
   			v.setDeltaX(0);
   			v.setDeltaY(0);
   			v.setMoveAttack(false);
   			v.setTenirPos(false);
   			
   			changeJ1ouJ2Message();
   			
   			messageJeu+="stop-"+v.getId()+":";
   		}
   	if(!messageJeu.equals("J1") && !messageJeu.equals("J2") && !messageJeu.equals(""))
   		if(clientReceiver!=null)
   			clientReceiver.sendAnyMsg("all"+messageJeu);
   }
   
    /** Send information units hold their position */
    private void tenirUnit(){
	   messageJeu="";
	   for(Monster v : game.arrayMonsterOwnerSelected)
		   if(v != null){
			   v.setTenirPos(true);
			   
			   changeJ1ouJ2Message();
	   			
	   			messageJeu+="tenir-"+v.getId()+":";
		   }
	   if(!messageJeu.equals("J1") && !messageJeu.equals("J2") && !messageJeu.equals(""))
		   if(clientReceiver!=null)
	   			clientReceiver.sendAnyMsg("all"+messageJeu);
   }
   
    /** Send information units changed their metamorphose or units have been bought */
    private void switchAchaTransf(int a){
	   messageJeu="";
	   changeJ1ouJ2Message();
	   
   	if(game.arrayMonsterOwnerSelected.isEmpty())
   		game.acheterUnit(a,joueur);	// Information non envoyé au serveur, je ferai la vérification à la création de l'unité
   	else
   		for(Monster v : game.arrayMonsterOwnerSelected)
   			if(v != null)
   				if(joueur==1){
   					if(v.changeMetamor(a))	// False -> pas assez de mana donc on n'ajoute pas au message (transformation non effectuer)
   						messageJeu+="changeM-"+v.getId()+"-"+a+":";		// J1changeM-id-IntMetamorphose:
   					//else
   						//checkErrorMetaImpossible();	// Ne peux servir qu'à J1 sinon faut que je refasse une fonction pour déterminer si le mana est suffisant sans changer la metamor
   				}else{
   					messageJeu+="askChangeM-"+v.getId()+"-"+a+":";	// J2 ne fait pas le changement direct. Y a d'abord une verification au serveur puis le serveur lui fait les changements
   				}
   		if(joueur==2){
   			clientReceiver.sendAnyMsg(""+messageJeu);
   			messageJeu="";
   		}
   		if(!messageJeu.equals("J1") && !messageJeu.equals("J2") && !messageJeu.equals(""))
   			if(clientReceiver!=null)
   				clientReceiver.sendAnyMsg("all"+messageJeu);
   
   }

   @SuppressWarnings("unused")
   private void checkErrorMetaImpossible(){
	   boolean contient=false;
   	//if(joueur != differencierJoueur)	Marche que si J2 attaque J1. Faudrait en gros mettre ça dans IAcheck
   		for(String l : game.arrayServerTextInGameError)
   			if(l.equalsIgnoreCase("Transformation impossible, mana insuffissant")){
   				contient=true;
   				break;
   			}		
   	if(!contient)
   		try{
   			game.ajouterTexteError("Transformation impossible, mana insuffissant");
   		}catch(Exception e){}
   }
    
    // ***
    // LES EFFETS (SONS)
    // **
    private void jouerSons(){
    	// **
    	// Je suppose que jouer un son n'est pas bloquant. Je n'ai pas trouvé dans la javadoc
    	// **
    	
    	int k=0;
    	// bcp de précaution :[
    	// Pour le moment, je parcours en entier l'array, peut-être qu'il ne faudrait pas... Après tout update a un interval de 10
    	try{
    		if(game !=null)
		    	for(int i=0; i<game.arraySons.size() - k;i++){
		    		if(!game.arraySons.isEmpty()){
		    			if(game.arraySons.get(0) != null){
		    				switch(game.arraySons.get(0)){
		    				// les sons : arc,explosion,hacheOs,epee1,epee2,epee3,epee4
		    				// sons prévus : si attaquer et non vision. Unité créé
		    				case 1:
		    					if(epee1 != null)
		    						epee1.play();
		    					break;
		    				case 2:
		    					if(epee2 != null)
		    						epee2.play();
		    					break;
		    				case 3:
		    					if(epee3 != null)
		    						epee3.play();
		    					break;
		    				case 4:
		    					if(epee4 != null)
		    						epee4.play();
		    					break;
		    				case 7:
		    					if(arc != null)
		    						arc.play();
		    					break;
		    				case 8:
		    					if(explosion != null)
		    						explosion.play();
		    					break;
		    				case 9:
		    					if(hacheOs != null)
		    						hacheOs.play();
		    					break;
		    				default:
		    					if(epee1 != null)
		    						epee1.play();
		    					break;
		    				}
		    				
		    				game.arraySons.remove(0);
		    				k++;
		    			}else{
		    				game.arraySons.remove(0);
		    				break;
		    			}
		    		}else{
		    			break;
		    		}
		    	}// fin for
    	}catch(Exception e){}
    }
   	
    
    // Fonctions pour l'IP, les rang, envoie de la victoire, etc
    /**  Get the IP with  InetAddress.getLocalHost(); or get the IP given in Option */
   public void getIP(){	// Si la personne est derrière un routeur, c'est son ip local que je vais avoir normalement et ça m'intéresse pas, je vais donc faire confiance a l'adresse IP que me donnera le site
		InetAddress LocaleAdresse;
		try {
			LocaleAdresse = InetAddress.getLocalHost();
			System.out.println("L'adresse locale est : "+LocaleAdresse ); 
		} catch (UnknownHostException e) {			
			e.printStackTrace();
		}
		//return ""+(LocaleAdresse.getHostAddress());
   }
   /**
    * Avoir ce que contient la page d'une URL
    * @param url of the website
    * @throws IOException Internet is off, or couldn't get the website.
    * @throws Exception Je sais pas
    * @return Response from the site
    */
   private static String get(String url) throws IOException, Exception{	// Les erreurs sont gérer à l'appel de la fonction pour personnalisé le msg d'erreur  
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
   
   /**
    * Avoir ce que contient la page d'une URL et en mettant des paramètre (POST)
    * @param url of the website
    * @param requestParameters parameters of the url (must have a good syntax)
    * @throws IOException Internet is off, or couldn't get the website
    * @throws Exception Je sais pas
    * @return Response of the site
    */
   @SuppressWarnings("unused")
   private static String get(String url, String requestParameters) throws IOException, Exception{	// Les erreurs sont gérer à l'appel de la fonction pour personnalisé le msg d'erreur  
	   String source ="";
	   if (requestParameters != null && requestParameters.length () > 0)
	   {
	   url += "?" + requestParameters;
	   }
	   URL urlObject = new URL(url);
	   URLConnection urlCon = urlObject.openConnection();
	   // Récupérer la réponse
	   BufferedReader in2 = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
	   String inputLine2;
	   
	   while ((inputLine2 = in2.readLine()) != null)
		   source +=inputLine2;
	   try{
		   in2.close();
	   }catch(Exception e){}
   return source;
   }
   
   /** Rejoins le serveur que l'on a cliqué dessus */
   private void cliqueSurUnServeur(int x, int y){
	  // System.out.println("y:"+y+" par 41: "+(y-80)/41+" par 40: "+(y-80)/40);
	   if(y >= 543+38 && y <= 543+78 && x >= 220 && x <= 365){
		   // Juste pour eviter un clique non voulu
	   }else if(x >= 144 && x <= 980 && y >= 78 && y <= 610 && !ipDirectON && !errorON){
		   int serverSelected = (y-80)/41, iVoulu=0;
		  		   
		   if(serverSelected >= 0 && serverSelected <= 12 && !saveListeServer.equalsIgnoreCase("")){	// Simple vérification
				String bufferListe[],bufferInfo[];
				bufferListe = saveListeServer.split(":");
				if(bufferListe.length > serverSelected)	// Gère le 0 avec le >
					for(String v : bufferListe){
						if(iVoulu == serverSelected){
							bufferInfo = v.split("-");
							try{
								String ipp = bufferInfo[3];
								joueur=2;
				    			try{
				    				chargementON=true;
				    				SocketAddress sockaddr = new InetSocketAddress(ipp, portUtilise);
				    				socket = new Socket();
				    				socket.connect(sockaddr, 6000);
				    				clientReceiver = new ClientReceiver(socket, this);
				    				
				    				clientReceiver.pseudoJ2=pseudo;
				    				clientReceiver.rangJ2=rang;
				    				clientReceiver.pointsJ2=points;

				    				menuMultiServeurCreerON=true;
									menuMultiON=false;
									ipDirectON=false;
									textIP.deactivate();
									
									clientReceiver.sendInfoPlayer("infoJ2"+pseudo+"-"+rang+"-"+points);
				    			} catch (SocketTimeoutException e) {
				    				errorMsg="Connexion impossible, Time Out";
					    			errorON=true;
				    			}catch(Exception e) {
				    				errorMsg="Connexion impossible, vérifier \nl'addresse IP";
					    			errorON=true;
				    			}
							}catch(Exception e){
								errorMsg="Erreur pour avoir l'ip.";
				    			errorON=true;
							}
							chargementON=false;
						}
						iVoulu++;
					}
   			}//*/
	   }
   }
   
   public void debPhrs(String phrs){
		System.out.println(phrs);
	}
   
   /** Appeler dans ClientReceiver */
   public void mettreFinPartie(){
	   finGameON=true;
	   gameON=false;
		inGameChoixON=false;
		if(game !=null)
			game.setPause(true);
		//game=null;
		
		j1Disco();
		j2Disco();
		if(clientReceiver!=null)
			clientReceiver=null;
		if(serveur!=null){
			serveur.setNbClients(0);
			serveur.arreterServeur();    	
			
			serveur=null;
		}
		if(socket!=null)
			socket=null;
   }
   
   

}