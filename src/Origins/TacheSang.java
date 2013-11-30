package Origins;
import org.lwjgl.Sys;


/**
 * Classe pour afficher du sang sur la map
 * @author Yoann CAPLAIN
 * @version 1.0
 */
public class TacheSang {
	private int x,y;
	/** Multiple sprite, given by a random */
	private int type;
	/** Deleted after an amount of time */
	private long tempsCreation;
	

	/** Default */
	TacheSang(int x, int y){
		this.x=x;
		this.y=y;
		this.type=(int)(Math.random()*5);	// 5 est le nb d'image de sang ­
		this.tempsCreation = getTime();
	}
	
	TacheSang(int x, int y, int type){
		this.x=x;
		this.y=y;
		this.type=type;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getType() {
		return type;
	}
	public long getTempsCreation() {
		return tempsCreation;
	}

	
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setTempsCreation(long tempsCreation) {
		this.tempsCreation = tempsCreation;
	}
	
	
	
	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	private long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
}
