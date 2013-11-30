package Origins;
/**
 * Classe tire mage extends Tire
 * @author Yoann CAPLAIN
 * @version 1.0
 */
public class TireMage extends Tire{
	/** Zone d'effet de la boule de feu */
	int radius;	// N'a pas été mis pour le moment, peut-être trop puissant
	
	
	public TireMage(int x, int y, int xx, int yy, int dmg, int id, int idCible){
		super(x,y,xx,yy,dmg,id,idCible);
		
		//this.setType(5);
		this.setDeltaX(getDeltaX() * 3.0f);
		this.setDeltaY(getDeltaY() * 3.0f);
		this.radius = 50;
	}
	// Changer le nom car je suis pas sûr que y aura différenciation entre celui de la classe mère et fille
	/** Move tire mage depending his deltaX and deltaY. override celui de Tire	 */
	public void changeDestination(int xx, int yy){	// Permetttra de suivre la cible
		this.setDeltaX((double)(xx - (int)(this.getX()))/Math.sqrt(Math.pow((xx - this.getX()), 2) + Math.pow((yy - this.getY()), 2)));
		this.setDeltaY((double)(yy - (int)(this.getY()))/Math.sqrt(Math.pow((xx - this.getX()), 2) + Math.pow((yy - this.getY()), 2)));
		
		this.setDestX(xx);
		this.setDestY(yy);
		
		this.setDeltaX(getDeltaX() * 3.0f);
		this.setDeltaY(getDeltaY() * 3.0f);
		//System.out.println("Tire Mage");
	}

}
