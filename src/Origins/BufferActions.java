package Origins;

/**
 * Classe des actions
 * Je voulais à la base sauvegarder toutes les actions du joueur dans un buffer puis envoyer les informations au serveur.
 * (Comme dans Age of Empire II) L'envoi aurait fait toutes les 200ms je penses mais finalement, j'ai préférais envoyer directement
 * @author Yoann CAPLAIN
 * @version 0.0
 */

public class BufferActions implements Runnable{

	private Thread _t;
	//private String buffer;
	
	BufferActions(){
		
	//	buffer="";
		_t = new Thread(this);
	    _t.start();
	}
	
	@Override
	public void run() {
		
		
	}

	
	
}
