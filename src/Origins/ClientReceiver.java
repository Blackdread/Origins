package Origins;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import org.lwjgl.Sys;


/**
 * Classe gère les messages du serveur et lui en envoie
 * @author Yoann CAPLAIN
 * @version 1.0
 */
public class ClientReceiver implements Runnable {
	
	  private Thread _t;
	  private Socket _s;
	  private PrintWriter _out;
	  private BufferedReader _in;
	  private Lancement _lancement;

	  public ArrayList<String> arrayServerText = new ArrayList<String>();
	  public ArrayList<String> arrayServerTextInGameChat = new ArrayList<String>();
	  public ArrayList<Long> arrayServerTextInGameChatTime = new ArrayList<Long>();
	  public String pseudoJ1;
	  public int rangJ1, pointsJ1;	// A init ??
	  public String pseudoJ2;
	  public int rangJ2, pointsJ2;	// A init ??
	  private String[] MsgSplitterBuffer, MsgSplitterBuffer2;	// l'un pour les : puis pous les -
	  
	  private Game game;
	  
	ClientReceiver(Socket s, Lancement lancement)
	  {
	    _s=s;
	    _lancement = lancement;
	    
	    try
	    {
	      // fabrication d'une variable permettant l'utilisation du flux de sortie avec des string
	      _out = new PrintWriter(_s.getOutputStream());
	      // fabrication d'une variable permettant l'utilisation du flux d'entrée avec des string
	      _in = new BufferedReader(new InputStreamReader(_s.getInputStream()));

	    }
	    catch (IOException e){ }

	    _t = new Thread(this); // instanciation du thread
	    _t.start(); // demarrage du thread, la fonction run() est ici lancée
	  }
	
	@Override
	public void run() {
		int i;
		String message = ""; // déclaration de la variable qui recevra les messages du client

	    try
	    {
	      // la lecture des données entrantes se fait caractère par caractère ...
	      // ... jusqu'à trouver un caractère de fin de chaine
	      char charCur[] = new char[1];	// déclaration d'un tableau de char d'1 élement, _in.read() y stockera le char lu
	      while(_in.read(charCur, 0, 1)!=-1) // attente en boucle des messages provenant du client (bloquant sur _in.read())
	      {
	      	// on regarde si on arrive à la fin d'une chaine ...
	        if (charCur[0] != '\u0000' && charCur[0] != '\n' && charCur[0] != '\r')
	                message += charCur[0]; // ... si non, on concatène le caractère dans le message
	        else if(!message.equalsIgnoreCase("")) // juste une vérification de principe
	        {
	          //if(charCur[0]=='\u0000'){ // le dernier caractère était '\u0000' (char de terminaison nulle)
	          	// on envoi le message en disant qu'il faudra concaténer '\u0000' lors de l'envoi au client
	        	  if(message.startsWith("newMsg"))
	        		  addMsg(message.substring(6));
	        	    
	        	  if(message.startsWith("infoJ1")){		//	infoJ1-pseudo-rang-points
	        		  message = message.substring(6);
	        		  MsgSplitterBuffer = message.split("-");
	        		  
	        		  pseudoJ1=MsgSplitterBuffer[0];
	        		  rangJ1= Integer.valueOf(MsgSplitterBuffer[1]).intValue();
	        		  pointsJ1=Integer.valueOf(MsgSplitterBuffer[2]).intValue();
	        	  }
	        	  if(message.startsWith("infoJ2")){		//	infoJ2-pseudo-rang-points
	        		  message = message.substring(6);
	        		  MsgSplitterBuffer = message.split("-");
	        		  
	        		  pseudoJ2=MsgSplitterBuffer[0];
	        		  rangJ2=Integer.valueOf(MsgSplitterBuffer[1]).intValue();
	        		  pointsJ2=Integer.valueOf(MsgSplitterBuffer[2]).intValue();
	        		 
	        		  if(pseudoJ1 != null)
	        			  sendInfoPlayer("infoJ1"+pseudoJ1+"-"+rangJ1+"-"+pointsJ1);
	        	  }
	        	 
	        	  if(message.equals("disco1"))
	        		  _lancement.j1Disco();
	        	  if(message.equals("disco2"))
	        		  _lancement.j2Disco();
	        	
	        	  if(message.equals("startGame"))
	        		  if(_lancement.getJoueur() != 1){
	        			  game = new Game();
	        			  _lancement.startGame();
	        			  System.out.println("Partie commencée");
	        		  }
	        	  
	        	  // *******
	        	  // LE JEU
	        	  // *******
	        	  if(game==null && _lancement.getJoueur()==1)
	        		  game = _lancement.game;
	        	  
	        	  if(message.startsWith("inGameChat")){
	        		  message=message.substring(10);
	        		  try{
	        			  arrayServerTextInGameChat.add(message);
	        			  arrayServerTextInGameChatTime.add(getTime());
	        		  }catch(ConcurrentModificationException e){}
	        	  }
	        	  
	        	  // ================
	        	  // FIN D'UNE PARTIE
	        	  // ================
	        	  if(message.startsWith("gameFini")){
	        		  if(game != null)
	        			  game.setPause(true);
	        		  _lancement.mettreFinPartie();
	        	  }
	        	  
	        	  
	        	  if(_lancement.getJoueur()==2){
	        		  if(message.startsWith("unitChecked")){
	        			  message = message.substring(11);
	        			// J1ouJ2-id-kill:
	        			// J1ouJ2-id-vie-x-y:
	        			  MsgSplitterBuffer = message.split(":");
	        			  for(i=0;i<MsgSplitterBuffer.length;i++){
		        			  MsgSplitterBuffer2 = MsgSplitterBuffer[i].split("-");
		        			  if(MsgSplitterBuffer2[2].equalsIgnoreCase("kill")){
		        				  if(MsgSplitterBuffer2[0].equalsIgnoreCase("J1"))
		        					  game.killUnitReceived(Integer.valueOf(MsgSplitterBuffer2[1]), 1);
		        				  else if(MsgSplitterBuffer2[0].equalsIgnoreCase("J2"))
		        					  game.killUnitReceived(Integer.valueOf(MsgSplitterBuffer2[1]), 2);
		        			  }else{
		        				  if(MsgSplitterBuffer2.length == 5)
		        					  if(MsgSplitterBuffer2[0].equalsIgnoreCase("J1")){
			        					    for(Monster v : game.arrayMonsterOwner)
			        					    	if(v.getId() == Integer.valueOf(MsgSplitterBuffer2[1])){
			        					    		v.setVie(Integer.valueOf(MsgSplitterBuffer2[2]));
			        					    		v.setX(Integer.valueOf(MsgSplitterBuffer2[3]));
			        					    		v.setY(Integer.valueOf(MsgSplitterBuffer2[4]));
			        					    		System.out.println("Fait le check 1");
			        					    	}
		        					  }else if(MsgSplitterBuffer2[0].equalsIgnoreCase("J2")){
		        						  for(Monster v : game.arrayMonsterEnnemy)
			        					    	if(v.getId() == Integer.valueOf(MsgSplitterBuffer2[1])){
			        					    		v.setVie(Integer.valueOf(MsgSplitterBuffer2[2]));
			        					    		v.setX(Integer.valueOf(MsgSplitterBuffer2[3]));
			        					    		v.setY(Integer.valueOf(MsgSplitterBuffer2[4]));
			        					    		System.out.println("Fait le check 2");
			        					    	}
			        				  }
		        			  }
	        			  }	// fin for buffer 1
	        			  message="";
	        		  }	// Fin unitChecked
	        		  if(message.startsWith("updateVarFin")){	// Mettre a jour les variables de fin de partie pour J2
	        			  // les var sont argentTotalJ1=0, argentTotalJ2=0, unitTotalCreerJ1=0, unitTotalCreerJ2=0, unitTotalTuerJ1=0, unitTotalTuerJ2=0;
	        			  message = message.substring(12);
		        			// argentTotalJ1-argentTotalJ2-unitTotalCreerJ1-unitTotalCreerJ2-unitTotalTuerJ1-unitTotalTuerJ2-minute-seconde
			        		MsgSplitterBuffer = message.split("-");
			        		try{
			        			if(game != null){
					        		game.argentTotalJ1 = Integer.valueOf(MsgSplitterBuffer[0]);
					        		game.argentTotalJ2 = Integer.valueOf(MsgSplitterBuffer[1]);
					        		game.unitTotalCreerJ1 = Integer.valueOf(MsgSplitterBuffer[2]);
					        		game.unitTotalCreerJ2 = Integer.valueOf(MsgSplitterBuffer[3]);
					        		game.unitTotalTuerJ1 = Integer.valueOf(MsgSplitterBuffer[4]);
					        		game.unitTotalTuerJ2 = Integer.valueOf(MsgSplitterBuffer[5]);
					        		game.minTimer = Integer.valueOf(MsgSplitterBuffer[6]);
					        		game.secTimer = Integer.valueOf(MsgSplitterBuffer[7]);
			        			}
			        		}catch(Exception e){}	// Normalement il n'y a que IndexBoundException
			        		
			        		message="";
	        		  }
	        	  }// Fin joueur == 2
	        	  //*
	        if(_lancement.getJoueur()!=1)
	        	  if(message.startsWith("J1")){
	        		  message = message.substring(2);
	        		 // MsgSplitterBuffer=null;
	        		  //MsgSplitterBuffer2=null;
		        		  MsgSplitterBuffer = message.split(":");
		        		  for(i=0;i<MsgSplitterBuffer.length;i++){
		        			  MsgSplitterBuffer2 = MsgSplitterBuffer[i].split("-");
		        			  
		        			  
		        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("deplace")){	// deplace-id-destX-destY-metamor-moveAttack
		        				  for(Monster v : game.arrayMonsterOwner){
		        					  if(v != null)
		        					  if(v.getId() != Integer.valueOf(MsgSplitterBuffer2[1])){
		        						  continue;
		        			  			}else{
		        			  				//v.setMetamor(Integer.valueOf(MsgSplitterBuffer2[4]));	ENLEVER POUR LE MOMENT
		        			  				v.changeDestination(Integer.valueOf(MsgSplitterBuffer2[2]), Integer.valueOf(MsgSplitterBuffer2[3]));
		        			  				if(MsgSplitterBuffer2[5].equalsIgnoreCase("true"))
		        			  					v.setMoveAttack(true);
		        			  				else
		        			  					v.setMoveAttack(false);
		        			  				break;
		        			  			}
		        				  }
		        			  }	// Fin deplacement
		        			  
		        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("stop")){	// stop-id
		        				  for(Monster v : game.arrayMonsterOwner){
		        					  if(v != null)
		        					  if(v.getId() != Integer.valueOf(MsgSplitterBuffer2[1])){
		        						  continue;
		        			  			}else{
		        			  				if(v != null){
		        		            			v.setDeltaX(0);
		        		            			v.setDeltaY(0);
		        		            			v.setMoveAttack(false);
		        		            			v.setTenirPos(false);
		        		            			break;
		        			  				}
		        			  			}
		        				  }
		        			  }	// Fin stop
		        			  
		        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("tenir")){	// tenir-id
		        				  for(Monster v : game.arrayMonsterOwner){
		        					  if(v != null)
		        					  if(v.getId() != Integer.valueOf(MsgSplitterBuffer2[1])){
		        						  continue;
		        			  			}else{
		        			  				if(v != null){
		        			  					 v.setTenirPos(true);
		        		            			break;
		        			  				}
		        			  			}
		        				  }
		        			  }	// Fin tenir
		        			  
		        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("changeM")){	// changeM-id-IntMetamorphose:
		        				  for(Monster v : game.arrayMonsterOwner){
		        					  if(v != null)
		        					  if(v.getId() != Integer.valueOf(MsgSplitterBuffer2[1])){
		        						  continue;
		        			  			}else{
		        			  				if(v != null){
		        			  					//v.setMetamor(Integer.valueOf(MsgSplitterBuffer2[2]));	// L'autre méthode m'évite d'avoir besoin d'envoyer la vie, le mana restant
		        			  					v.changeMetamor(Integer.valueOf(MsgSplitterBuffer2[2]));// Tout gérer (nb de kills, ...)
		        		            			break;
		        			  				}
		        			  			}
		        				  }
		        			  }	// Fin change metamorphose
		        			  
		        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("spawn")){	// spawn-id-xDest-yDest-IntMetamorphose-moveAttack:
		        				  game.spawnUnit(1, _lancement.getJoueur(),Integer.valueOf(MsgSplitterBuffer2[1]), Integer.valueOf(MsgSplitterBuffer2[2]), Integer.valueOf(MsgSplitterBuffer2[3]), Integer.valueOf(MsgSplitterBuffer2[4]), MsgSplitterBuffer2[5]);	
		        				  // 2 est pour diff le joueur pour spawn chez l'un ou l'autre, getJoueur() pour decrement le nb d'unite
		        			  }	// Fin spawn
		        			  
		        			  if(_lancement.getJoueur()!=1)
			        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("kill")){	// kill-id:	        				 
			        				  game.killUnitReceived(Integer.valueOf(MsgSplitterBuffer2[1]), 1);
			        				  System.out.println("tue 1: "+Integer.valueOf(MsgSplitterBuffer2[1]));
			        			  }	// Fin kill
		        			  
		        		  }	// Fin for MsgSplitterBuffer
	        	  }
	        	  // LE JOUEUR 2
	        	  if(message.startsWith("J2")){
	        		  message = message.substring(2);
	        		 // MsgSplitterBuffer=null;
	        		  //MsgSplitterBuffer2=null;
		        		  MsgSplitterBuffer = message.split(":");
		        		  for(i=0;i<MsgSplitterBuffer.length;i++){
		        			  MsgSplitterBuffer2 = MsgSplitterBuffer[i].split("-");
		        			  
		        			  
		        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("deplace")){
		        				  for(Monster v : game.arrayMonsterEnnemy){
		        					  if(v != null)
		        					  if(v.getId() != Integer.valueOf(MsgSplitterBuffer2[1])){
		        						  continue;
		        			  			}else{
		        			  				//v.setMetamor(Integer.valueOf(MsgSplitterBuffer2[4]));
		        			  				v.changeDestination(Integer.valueOf(MsgSplitterBuffer2[2]), Integer.valueOf(MsgSplitterBuffer2[3]));
		        			  				if(MsgSplitterBuffer2[5].equalsIgnoreCase("true"))
		        			  					v.setMoveAttack(true);
		        			  				else
		        			  					v.setMoveAttack(false);
		        			  				break;
		        			  			}
		        				  }
		        			  }	// Fin deplacement
		        			  
		        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("stop")){
		        				  for(Monster v : game.arrayMonsterEnnemy){
		        					  if(v != null)
		        					  if(v.getId() != Integer.valueOf(MsgSplitterBuffer2[1])){
		        						  continue;
		        			  			}else{
		        			  				if(v != null){
		        		            			v.setDeltaX(0);
		        		            			v.setDeltaY(0);
		        		            			v.setMoveAttack(false);
		        		            			v.setTenirPos(false);
		        		            			break;
		        			  				}
		        			  			}
		        				  }
		        			  }
		        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("tenir")){
		        				  for(Monster v : game.arrayMonsterEnnemy){
		        					  if(v != null)
		        					  if(v.getId() != Integer.valueOf(MsgSplitterBuffer2[1])){
		        						  continue;
		        			  			}else{
		        			  				if(v != null){
		        			  					 v.setTenirPos(true);
		        		            			break;
		        			  				}
		        			  			}
		        				  }
		        			  }	// Fin tenir
		        			  
		        			  if(_lancement.getJoueur()!=1){
			        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("changeM")){	// changeM-id-IntMetamorphose:
			        				  //System.out.println("Clientreceiver "+MsgSplitterBuffer[i]);
			        				  for(Monster v : game.arrayMonsterEnnemy){
			        					  if(v != null)
			        					  if(v.getId() != Integer.valueOf(MsgSplitterBuffer2[1])){
			        						  continue;
			        			  			}else{
			        			  				if(v != null){
			        			  					 //v.setMetamor(Integer.valueOf(MsgSplitterBuffer2[2]));// L'autre méthode m'évite d'avoir besoin d'envoyer la vie, le mana restant
			        			  					v.changeMetamor(Integer.valueOf(MsgSplitterBuffer2[2]));// Tout gérer (nb de kills, ...)
			        		            			break;
			        			  				}
			        			  			}
			        				  }
			        			  }	// Fin change metamorphose
		        			  }	// Fin joueur !=1
		        			  
		        		if(_lancement.getJoueur()!=1)
		        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("spawn")){	// spawn-id-xDest-yDest-IntMetamorphose-moveAttack:	        				 
		        				  game.spawnUnit(2, _lancement.getJoueur(),Integer.valueOf(MsgSplitterBuffer2[1]), Integer.valueOf(MsgSplitterBuffer2[2]), Integer.valueOf(MsgSplitterBuffer2[3]), Integer.valueOf(MsgSplitterBuffer2[4]), MsgSplitterBuffer2[5]);	
		        				  // 2 est pour diff le joueur pour spawn chez l'un ou l'autre, getJoueur() pour decrement le nb d'unite
		        			  }	// Fin spawn
		        			  
		        			  if(_lancement.getJoueur()!=1)
			        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("kill")){	// kill-id:	        				 
			        				  game.killUnitReceived(Integer.valueOf(MsgSplitterBuffer2[1]), 2);
			        				  System.out.println("tue 2: |"+Integer.valueOf(MsgSplitterBuffer2[1])+"|");
			        			  }	// Fin kill
		        			  
		        		  }	// Fin MsgSplitterBuffer
	        	  }	// Fin startWith J2
	        		  
	        	  /*
	          }else{
	        	  if(message.startsWith("newMsg"))
	        		  addMsg(message.substring(6));
	        	    
	        	  if(message.startsWith("infoJ1")){		//	infoJ1-pseudo-rang-points
	        		  message = message.substring(6);
	        		  MsgSplitterBuffer = message.split("-");
	        		  
	        		  pseudoJ1=MsgSplitterBuffer[0];
	        		  rangJ1= Integer.valueOf(MsgSplitterBuffer[1]).intValue();
	        		  pointsJ1=Integer.valueOf(MsgSplitterBuffer[2]).intValue();
	        	  }
	        	  if(message.startsWith("infoJ2")){		//	infoJ2-pseudo-rang-points
	        		  message = message.substring(6);
	        		  MsgSplitterBuffer = message.split("-");
	        		  
	        		  pseudoJ2=MsgSplitterBuffer[0];
	        		  rangJ2=Integer.valueOf(MsgSplitterBuffer[1]).intValue();
	        		  pointsJ2=Integer.valueOf(MsgSplitterBuffer[2]).intValue();
	        		 
	        		  if(pseudoJ1 != null)
	        			  sendInfoPlayer("infoJ1"+pseudoJ1+"-"+rangJ1+"-"+pointsJ1);
	        	  }
	        	 
	        	  if(message.equals("disco1"))
	        		  _lancement.j1Disco();
	        	  if(message.equals("disco2"))
	        		  _lancement.j2Disco();
	        	
	        	  if(message.equals("startGame"))
	        		  if(_lancement.getJoueur() != 1){
	        			  game = new Game();
	        			  _lancement.startGame();
	        			  System.out.println("Partie commencée");
	        		  }
	        	  
	        	  if(message.startsWith("J1")){
	        		  message = message.substring(2);
		        		  MsgSplitterBuffer = message.split("|");
		        		  for(i=0;i<MsgSplitterBuffer.length;i++){
		        			  MsgSplitterBuffer2 = MsgSplitterBuffer[i].split("-");
		        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("deplace")){
		        				  for(Monster v : game.arrayMonsterOwner)
		        					  if(v.getId() != Integer.valueOf(MsgSplitterBuffer2[1])){
		        						  continue;
		        			  			}else{
		        			  				v.setMetamor(Integer.valueOf(MsgSplitterBuffer2[4]));
		        			  				v.changeDestination(Integer.valueOf(MsgSplitterBuffer2[2]), Integer.valueOf(MsgSplitterBuffer2[3]));
		        			  				if(MsgSplitterBuffer2[5].equalsIgnoreCase("true"))
		        			  					v.setMoveAttack(true);
		        			  				else
		        			  					v.setMoveAttack(false);
		        			  				System.out.println("changement fait et "+MsgSplitterBuffer2[5]+" et "+MsgSplitterBuffer2[1]);
		        			  			}
		        			  }
			        	  
		        		  }
	        	  }
	          }//*/
	          message = ""; // on vide la chaine de message pour qu'elle soit réutilisée
	        }
	      }
	    }
	    catch (Exception e){ }
	    finally // finally se produira le plus souvent lors de la deconnexion du client
	    {
	      try
	      {
	        _s.close(); // fermeture du socket si il ne l'a pas déjà été (à cause de l'exception levée plus haut)
	        _out.close();
	        _in.close();
	        System.out.println("Deco dans ClientReceiver et joueur "+_lancement.getJoueur());
	        // Ici envoyer info de la deco à l'autre
	        
	      }
	      catch (IOException e){ }
	    }
		
	}
	
	public String getLastMsgCreer(){
		  if(arrayServerText.size()>=1)
			  return this.arrayServerText.get(arrayServerText.size()-1);
		  else
			  return "";
	  }
	 public void addMsg(String message){
		  arrayServerText.add(message);
		  if(arrayServerText.size() >= 17)
			  arrayServerText.remove(0);
	  }
	 
	 // **********************************
	 // FONCTION DE BASE
	/**
	*	Send a message to the server
	*/
	 public void sendAnyMsg(String message){
			try {
				_out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(_s.getOutputStream())),true);
				_out.print(message+'\u0000');
				_out.flush();
			} catch (IOException e) {
				//e.printStackTrace();
			}
	 } 
	 // **********************************
	 
	 //	FONCTIONS FAITE JUSTE POUR QUE CE SOIT PLUS LISIBLE DANS LA CLASSE Lancement
	 /** Send a message to the server, only used in menuCreerServeur */
	public void sendCreerServeur(String message){
		try {
			_out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(_s.getOutputStream())),true);
			_out.print("sendCreer"+message+'\u0000');
			_out.flush();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	/** Same as sendAnyMsg (just to be right in Lancement) */
	public void sendInfoPlayer(String message){
		try {
			_out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(_s.getOutputStream())),true);
			_out.print(message+'\u0000');
			_out.flush();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	/**	Get info of Player 2 */
	public void getInfoJ2(){
		try {
			_out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(_s.getOutputStream())),true);
			_out.print("getInfoJ2"+'\u0000');
			_out.flush();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	/**	Send info that player 1 has disconnected*/
	public void sendDisconnetedJ1(){
		try {
			_out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(_s.getOutputStream())),true);
			_out.print("disco1"+'\u0000');
			_out.flush();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	/**	Send info that player 2 has disconnected */
	public void sendDisconnetedJ2(){
		try {
			_out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(_s.getOutputStream())),true);
			_out.print("disco2"+'\u0000');
			_out.flush();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	public void startGame(){
		sendAnyMsg("startGame");
	}
	/**  
	 * Used in Lancement for J2
	 * @return Game
	 * */
	public Game getGame(){
		return game;
	}
	/**
	 *	
	 * @return Int of player
	 */
	public int getJoueur(){
		return _lancement.getJoueur();
	}
	public Lancement getLancement(){
		return _lancement;
	}
	
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	
	// Eviter concurent
	 public void sendCheckDeath(String message){
			try {
				_out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(_s.getOutputStream())),true);
				_out.print(message+'\u0000');
				_out.flush();
			} catch (IOException e) {
				//e.printStackTrace();
			}
	 } 
}
