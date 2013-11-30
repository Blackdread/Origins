package Origins;
import java.net.*;
import java.io.*;
import java.util.*;

//** Classe principale du serveur, gère les infos globales **
/**
 * Classe serveur, created by player 1
 * @author Yoann CAPLAIN
 * @version 1.0
 */
public class Serveur implements Runnable{
	private Thread _t;
  @SuppressWarnings("rawtypes")
  private Vector _tabClients = new Vector(); // contiendra tous les flux de sortie vers les clients
  private int _nbClients=0; // nombre total de clients connectés
  private ArrayList<String> arrayServerText = new ArrayList<String>();

  private boolean continuer =true;
  
  private Game game;
  
   public Serveur(){
  //** Methode : la première méthode exécutée, elle attend les connections **
 // public static void main(String args [])
 // {
    //Serveur serveurv = new Serveur(); // instance de la classe principale
	   _t = new Thread(this); // instanciation du thread
	   _t.start();
  }
   
  //** Methode : envoie le message à tous les clients **
  synchronized public void sendAll(String message,String sLast)
  {
    PrintWriter out; // declaration d'une variable permettant l'envoi de texte vers le client
    for (int i = 0; i < _tabClients.size(); i++) // parcours de la table des connectés
    {
      out = (PrintWriter) _tabClients.elementAt(i); // extraction de l'élément courant (type PrintWriter)
      if (out != null) // sécurité, l'élément ne doit pas être vide
      {
      	// ecriture du texte passé en paramètre (et concaténation d'une string de fin de chaine si besoin)
        out.print(message+sLast);
        out.flush(); // envoi dans le flux de sortie
      }
    }
  }
  /** Send message to player 1 */
  synchronized public void sendJ1(String message,String sLast)
  {
    PrintWriter out;
      out = (PrintWriter) _tabClients.elementAt(0);
      if (out != null)
      {
        out.print(message+sLast);
        out.flush(); 
      }
  }
  /** Send message to player 2 */
  synchronized public void sendJ2(String message,String sLast)
  {
	  if(_tabClients.size()>1){
	    PrintWriter out;
	      out = (PrintWriter) _tabClients.elementAt(1);
	      if (out != null)
	      {
	        out.print(message+sLast);
	        out.flush(); 
	      }
	  }
  }
  
  /**	Add the message (from menuCreerServeur) to the arrayList */
  synchronized public void addMsg(String message){
	  arrayServerText.add(message);
	  if(arrayServerText.size() >= 17)
		  arrayServerText.remove(0);
	  //for(String f : arrayServerText)
		//  System.out.println(f);
  }


  //** Methode : détruit le client no i **
  synchronized public void delClient(int i)
  {
    _nbClients--;
    try{
	    if (_tabClients.elementAt(i) != null) // l'élément existe ...
	    {
	      _tabClients.removeElementAt(i); // ... on le supprime
	    }
    }catch(ArrayIndexOutOfBoundsException e){ System.out.println("DELCLIENT ERROR"); }
  }

  //** Methode : ajoute un nouveau client dans la liste **
  @SuppressWarnings("unchecked")
synchronized public int addClient(PrintWriter out)
  {
    _nbClients++;
    _tabClients.addElement(out); // on ajoute le nouveau flux de sortie au tableau
    return _tabClients.size()-1; // on retourne le numéro du client ajouté (size-1)
  }

  //** Methode : retourne le nombre de clients connectés **
  synchronized public int getNbClients()
  {
    return _nbClients; // retourne le nombre de clients connectés
  }
  
  synchronized public String getLastMsgCreer(){
	  if(arrayServerText.size()>=1)
		  return arrayServerText.get(arrayServerText.size()-1);
	  else
		  return "";
  }

  @SuppressWarnings("deprecation")
public void arreterServeur(){
	  _tabClients.clear();
	  _nbClients=0;
	 _t.stop();	// deprecated
	 
  }
  
  public void videMsg(){
	  arrayServerText.clear();
	  arrayServerText.add("Bienvenue dans le jeu");
  }
  public Game getGame(){
	  return game;
	}
  /** Wait for clients to connect (max 2) */
@Override
public void run() {
	
	 try
	    {
	      Integer port;
	      //if(args.length<=0) port=new Integer("18000"); // si pas d'argument : port 18000 par défaut
	    //  else port = new Integer(args[0]); // sinon il s'agit du numéro de port passé en argument
	      port=new Integer(""+Lancement.portUtilise);
	      //new Commandes(serveurv); // lance le thread de gestion des commandes

	      ServerSocket ss = new ServerSocket(port.intValue()); // ouverture d'un socket serveur sur port
	      
	      while (continuer) // attente en boucle de connexion (bloquant sur ss.accept)
	      {
	    	 if(getNbClients() < 2)
	    		  new Client(ss.accept(),this); // un client se connecte, un nouveau thread client est lancé
	      }
	    		//  System.out.println("2");
	    }
	    catch (Exception e) { }
}

	synchronized public void initAndGame(){
		//if(game != null)
			//game = null;
		sendJ2("startGame",""+'\u0000');
		  game = new Game();
		  //System.out.println("Partie commencée dans serveur");
	  }

	public void setContinuer(boolean conti){
		continuer=conti;
	}
	public void setNbClients(int i){
		this._nbClients=i;
	}
	
}