package Origins;
import java.net.*;
import java.util.ConcurrentModificationException;
import java.io.*;


//** Classe associée à chaque client **
//** Il y aura autant d'instances de cette classe que de clients connectés **
//implémentation de l'interface Runnable (une des 2 méthodes pour créer un thread)
/**
 * Classe associée à chaque client
 * @author Yoann CAPLAIN
 * @version 1.0
 */
class Client implements Runnable
{
  private Thread _t; // contiendra le thread du client
  private Socket _s; // recevra le socket liant au client
  private PrintWriter _out; // pour gestion du flux de sortie
  private BufferedReader _in; // pour gestion du flux d'entrée
  private Serveur _serveur; // pour utilisation des méthodes de la classe principale
  private int _numClient=0; // contiendra le numéro de client géré par ce thread
  
  private String[] MsgSplitterBuffer, MsgSplitterBuffer2;
  
  //** Constructeur : crée les éléments nécessaires au dialogue avec le client **
  Client(Socket s, Serveur serveur) // le param s est donnée dans Serveur par ss.accept()
  {
    _serveur=serveur; // passage de local en global (pour gestion dans les autres méthodes)
    _s=s; // passage de local en global
    try
    {
      // fabrication d'une variable permettant l'utilisation du flux de sortie avec des string
      _out = new PrintWriter(_s.getOutputStream());
      // fabrication d'une variable permettant l'utilisation du flux d'entrée avec des string
      _in = new BufferedReader(new InputStreamReader(_s.getInputStream()));
      // ajoute le flux de sortie dans la liste et récupération de son numero
      _numClient = serveur.addClient(_out);
    }
    catch (IOException e){ }

    _t = new Thread(this); // instanciation du thread
    _t.start(); // demarrage du thread, la fonction run() est ici lancée
  }

  //** Methode :  exécutée au lancement du thread par t.start() **
  //** Elle attend les messages en provenance du serveur et les redirige **
  // cette méthode doit obligatoirement être implémentée à cause de l'interface Runnable
  public void run()
  {
    String message = ""; // déclaration de la variable qui recevra les messages du client
    // on indique dans la console la connection d'un nouveau client
    //System.out.println("Un nouveau client s'est connecte, no "+_numClient);
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
          if(charCur[0]=='\u0000'){ // le dernier caractère était '\u0000' (char de terminaison nulle)
          	// on envoi le message en disant qu'il faudra concaténer '\u0000' lors de l'envoi au client
        	  if(message.startsWith("sendCreer")){
        		  sendCreerServeur(message);
        		  _serveur.sendAll("newMsg"+_serveur.getLastMsgCreer(),"\n");
        	  }
        	  if(message.equals("getMsg"))
        		  _serveur.sendAll(_serveur.getLastMsgCreer(),"\n");
        	  if(message.startsWith("infoJ")){
        		  _serveur.sendAll(message,""+'\u0000');
        	  }
        	  
        	  
        	  if(message.equals("disco1") || message.equals("disco2"))
        		  _serveur.sendAll(message,""+'\u0000');
        	  
        	  
        	  if(message.equals("startGame"))
        		  _serveur.initAndGame();
        	  
        	  if(message.startsWith("all")){
        		  message = message.substring(3);
        		 // System.out.println("dans Client : "+message);
        		  _serveur.sendAll(""+message,""+'\u0000');
        	  }
        	  
        	  if(_serveur.getGame() != null){
        		  if(message.startsWith("checkDeath")){	// checkDeathJ1ouJ2-id:
	        		  message=message.substring(10);
	        		  try{
	        			  MsgSplitterBuffer = message.split(":");
	        			  message="unitChecked";
	        			  for(int i=0;i<MsgSplitterBuffer.length;i++){
		        			  MsgSplitterBuffer2 = MsgSplitterBuffer[i].split("-");	        			 
		        			  
		        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("J1")){
		        				  int ll=0, tailleArray=_serveur.getGame().arrayMonsterOwner.size(); // Si ll == max unit alors c'est qu'elle n'existe plus donc faut la tuer pour J2
		        				  for(Monster v : _serveur.getGame().arrayMonsterOwner){	//****ATTENTION*** L'array peut changer pendant cette vérification *********
		        					  if(v.getId() == Integer.valueOf(MsgSplitterBuffer2[1])){
		        						  if(v.getVie()<=0){	// unitCheckedJ1ouJ2-id-kill:
		        							  message+="J1-"+v.getId()+"-kill:";
		        							break;
		        						  }else{				// unitCheckedJ1ouJ2-id-vie-x-y:
		        							  message+="J1-"+v.getId()+"-"+v.getVie()+"-"+((int)v.getX())+"-"+((int)v.getY())+":";
		        							break;
		        						  }
		        					  }	// Fin if id = id2
		        					  ll++;
		        					  if(ll==tailleArray)	// unitCheckedJ1ouJ2-id-kill:	car n'existe pas
		        						  message+="J1-"+Integer.valueOf(MsgSplitterBuffer2[1])+"-kill:";
		        				  }	// Fin for
		        			  }else if(MsgSplitterBuffer2[0].equalsIgnoreCase("J2")){
		        				  int ll=0, tailleArray=_serveur.getGame().arrayMonsterEnnemy.size(); // Si ll == max unit alors c'est qu'elle n'existe plus donc faut la tuer pour J2
		        				  for(Monster v : _serveur.getGame().arrayMonsterEnnemy){
		        					  if(v.getId() == Integer.valueOf(MsgSplitterBuffer2[1])){
		        						  if(v.getVie()<=0){	// unitCheckedJ1ouJ2-id-kill:
		        							  message+="J2-"+v.getId()+"-kill:";
		        							break;
		        						  }else{				// unitCheckedJ1ouJ2-id-vie-x-y:
		        							  message+="J2-"+v.getId()+"-"+v.getVie()+"-"+((int)v.getX())+"-"+((int)v.getY())+":";
		        							break;
		        						  }
		        					  }	// Fin if id = id2
		        					  ll++;
		        					  if(ll==tailleArray)	// unitCheckedJ1ouJ2-id-kill:	car n'existe pas
		        						  message+="J2-"+Integer.valueOf(MsgSplitterBuffer2[1])+"-kill:";
		        				  }
		        			  }
	        			  }	// Fin for buffer1
	        			  if(!message.equalsIgnoreCase("unitChecked") && !message.equalsIgnoreCase("J1") && !message.equalsIgnoreCase("J2") && !message.equalsIgnoreCase(""))
		        		   		_serveur.sendJ2(message,""+'\u0000');
	        		  }catch(ConcurrentModificationException e){}
	        		  message="";
	        	  }    		  
        		  
	        	  if(message.startsWith("J2askChangeM")){
	        		  message = message.substring(2);
	        		  MsgSplitterBuffer = message.split(":");
	        		  message="J2";
	        		  for(int i=0;i<MsgSplitterBuffer.length;i++){
	        			  MsgSplitterBuffer2 = MsgSplitterBuffer[i].split("-");	        			 
	        			  
	        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("askChangeM")){	// askChangeM-id-IntMetamorphose:
	        				  for(Monster v : _serveur.getGame().arrayMonsterEnnemy){
	        					  if(v != null)
	        					  if(v.getId() != Integer.valueOf(MsgSplitterBuffer2[1])){
	        						  continue;
	        			  			}else{
	        			  				if(v != null){
	        			  					if(v.changeMetamor(Integer.valueOf(MsgSplitterBuffer2[2])))	// False -> pas assez de mana donc on n'ajoute pas au nessage (transformation non effectuer)
	        			   						message+="changeM-"+v.getId()+"-"+Integer.valueOf(MsgSplitterBuffer2[2])+":";		// J2changeM-id-IntMetamorphose:
	        		            			break;
	        			  				}
	        			  			}
	        				  }
	        			  }
	        		  }	// fin for
	        		  if(!message.equals("J2askChangeM") && !message.equals("J2") && !message.equals(""))
	        		   		_serveur.sendAll(message,""+'\u0000');  		
	        	  }
	        	  
	        	  if(message.startsWith("J2askSpawnM")){
	        		  message = message.substring(2);
	        		  MsgSplitterBuffer = message.split(":");
	        		  message="J2";
	        		  for(int i=0;i<MsgSplitterBuffer.length;i++){
	        			  MsgSplitterBuffer2 = MsgSplitterBuffer[i].split("-");	        			 
	        			  
	        			  if(MsgSplitterBuffer2[0].equalsIgnoreCase("askSpawnM")){	// askSpawnM-id-xDest-yDest-IntMetamorphose-moveAttack:
	        				  // J2spawn-id-xDest-yDest-IntMetamorphose-moveAttack:	
	        				  if(_serveur.getGame().spawnPossibleJ2(Integer.valueOf(MsgSplitterBuffer2[4]))){
	        					  int idSav=Monster.IDincrement+1;	// Car il a encore la valeur de l'autre unité qui a été créé
	        					  // A VERIFIER _serveur.getGame().joueur qu'il est bien initialisée
	        					  //_serveur.getGame().spawnUnit(2, _serveur.getGame().joueur,idSav, Integer.valueOf(MsgSplitterBuffer2[2]), Integer.valueOf(MsgSplitterBuffer2[3]), Integer.valueOf(MsgSplitterBuffer2[4]), MsgSplitterBuffer2[5]);
	        					  
	        					  // Car c'est forcément le joueur 1 qui Host :p
	        					  _serveur.getGame().spawnUnit(2, 1,idSav, Integer.valueOf(MsgSplitterBuffer2[2]), Integer.valueOf(MsgSplitterBuffer2[3]), Integer.valueOf(MsgSplitterBuffer2[4]), MsgSplitterBuffer2[5]);
	        					  message+="spawn-"+idSav+"-"+Integer.valueOf(MsgSplitterBuffer2[2])+"-"+Integer.valueOf(MsgSplitterBuffer2[3])+"-"+Integer.valueOf(MsgSplitterBuffer2[4])+"-"+MsgSplitterBuffer2[5]+":";
	        				  }
	        			  }
	        		  }	// fin for
	        		  if(!message.equals("J2askSpawnM") && !message.equals("J2") && !message.equals(""))
	        		   		_serveur.sendAll(message,""+'\u0000');  		
	        	  }
        	  }
        		  
           // _serveur.sendAll(message,""+charCur[0]);
          }else{
        	  if(message.startsWith("sendCreer")){
        		  sendCreerServeur(message);
        	  }
        	  
        	 // _serveur.sendAll(message,""); // sinon on envoi le message à tous
        	  
          }
          message = ""; // on vide la chaine de message pour qu'elle soit réutilisée
        }
      }
    }
    catch (Exception e){ }
    finally // finally se produira le plus souvent lors de la deconnexion du client
    {
      try
      {
    	  // ****************************************************
    	  // Peut-etre faire ici l'envoie du score. Donc que J1 envoie le score
    	  
      	// on indique à la console la deconnexion du client
        //System.out.println("Le client no "+_numClient+" s'est deconnecte");
        //System.out.println(""+_serveur.getGame());
        _serveur.delClient(_numClient); // on supprime le client de la liste
        _s.close(); // fermeture du socket si il ne l'a pas déjà été (à cause de l'exception levée plus haut)
      }
      catch (IOException e){ }
    }
  }
  
  public void sendCreerServeur(String message){
	  _serveur.addMsg(message.substring(9));
  }
}