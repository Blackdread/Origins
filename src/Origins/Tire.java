package Origins;
import org.lwjgl.Sys;

/**
 * Classe tire
 * @author Yoann CAPLAIN
 * @version 2.0
 */
public class Tire {
	
	/*
	 * Classe un peu obsolète au 1er avril 2012
	 * Car au début, rotation n'était que pour les flèches et arbalètes car de base l'image de la boule de feu était ronde
	 * Hors elle a maintenant une direction, un sens. (Mais j'ai pas envie de changer la classe pour le moment car ça m'obligerait à modifier les classes filles)
	 * 
	 * Aussi, je n'avais pas encore bien vu ce qu'était l'héritage et surtout le polymorphisme et aussi l'utilité des classes ABSTRACT
	 * Si je changes TIRE et les autres alors je ferai une classe MERE TIRE en ABSTRACT et j'utiliserai le polymorphisme, 
	 * de la même manière que j'ai fait pour ma classe ListeChaine non implémenter dans ce projet mais déjà prévu pour d'autre programme qui tourneront autour de ce style de programmation
	 * 
	 * 
	 * En gros, une classe mère avec tous les attributs et la méthode changeDestination serait redéfénie dans les classes filles
	 * 
	 */
	
	private double deltaX;
	private double deltaY;
	
	private double x;
	private double y;
	
	private int destX;
	private int destY;
	
	private int dmg;
	//private int type; // Pour différencier (juste pour le mage, faireSuivreTire)
	
	public float rotationMage;	// attribut qui aurait dû être commun à tout les tire (mage,archer et arba) mais sera seulement pour le mage sauf si je refais toutes les classes Tire****
	// dû au fait que l'image de la boule de feu a été changer le 1er avril 2012
	// A partir du 2 avril, cette variable est utilisé aussi pour les fleches (chose prévu au depart mais maintenant le mage a aussi cette necessité)
	
	// ID unique pour chaque tire
	//public static int IDincrement=0;
	
	// ID du celui qui a tiré
	private int idAttaquant;
	
	// ID unique de l'unité attaquer (et verifier si c tjr a portee, pas sûr)
	private int idCible;
	
	//*****************************************************************************************
	//    								CONSTRUCTEURS
	//*****************************************************************************************
	public Tire(int x, int y, int xx, int yy){		
		this.x = x;
		this.y = y;	
		changeDestination(xx,yy);
		changeRotationMage();
	}
	
	
	public Tire(int x, int y, int xx, int yy, int dmg, int id, int idCible){		
		this.x = x;
		this.y = y;	
		this.dmg = dmg;
		this.idAttaquant = id;
		this.idCible = idCible;
		changeDestination(xx,yy);
		changeRotationMage();
	}

	//*****************************************************************************************
	//                                    MUTATEURS
	//*****************************************************************************************	
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void setDeltaX(double x) {
		this.deltaX = x;
	}
	public void setDeltaY(double y) {
		this.deltaY = y;
	}
	public void setDestX(int x) {
		this.destX = x;
	}
	public void setDestY(int y) {
		this.destY = y;
	}
	public void setDmg(int dmg) {
		this.dmg = dmg;
	}
	public void setRotationMage(float rotationMage){
		this.rotationMage = rotationMage;
	}
	/*public void setType(int type){
		this.type = type;
	}*/
	
	 //*****************************************************************************************
	 //                                    ACCESSEURS
	 //*****************************************************************************************
	public double getDeltaX(){
		return deltaX;
	}
	public double getDeltaY(){
		return deltaY;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public int getDestX(){
		return destX;
	}
	public int getDestY(){
		return destY;
	}
	public int getDmg() {
		return dmg;
	}
	public int getIdAttaquant(){
		return idAttaquant;
	}
	public int getIdCible(){
		return idCible;
	}
	public float getRotationMage(){
		return rotationMage;
	}
	
	/*public int getType(){
		return type;
	}*/
	
	//*****************************************************************************************
	//                                    METHODES
	//*****************************************************************************************
	/** Increment x et Y */
	public void moveTire(){
		if(Math.abs(this.x - this.destX) <= 4)
			deltaX=0;
		if(Math.abs(this.x - this.destX) <= 4)
			deltaY=0;
		
		if(this.deltaX != 0 && this.deltaY != 0){
		 //if(this.x != this.destX || this.y != this.destY){
			 if(this.x > this.destX && this.deltaX > 0)
				this.x -= this.deltaX;
			else if(this.x > this.destX && this.deltaX < 0)
				this.x += this.deltaX;
			else if(x < this.destX && this.deltaX > 0)
				this.x += this.deltaX;

			 if(this.y > this.destY && this.deltaY < 0)
				 this.y += deltaY;
			 else if(this.y < this.destY && this.deltaY > 0)
				 this.y += deltaY;
			 
			 changeRotationMage();
		}
	}
	/** Change deltaX et Y et destX et Y */
	public void changeDestination(int xx, int yy){
			this.deltaX = ((double)(xx - (int)(this.x))/Math.sqrt(Math.pow((xx - this.x), 2) + Math.pow((yy - this.y), 2)));
			this.deltaY = ((double)(yy - (int)(this.y))/Math.sqrt(Math.pow((xx - this.x), 2) + Math.pow((yy - this.y), 2)));
			
			this.destX = xx;
			this.destY = yy;
	}
	

	/** Destroy tire */
	public void killTire(){
		// Cas de tuer unité ennemie
		this.x = -100;
		this.y = -100;
		this.deltaX =0;
		this.deltaY=0;
		this.destX=0;
		this.destY=0;
	}
	/** Change rotationMage depending on position of Tire and its destination. Fireball left */
	private void changeRotationMage(){
		double hypo = 1;
		double deltaX1 = 0, deltaY1 = 0;
		
		deltaY1 = Math.abs(this.destY - this.y);
		deltaX1 = Math.abs(this.destX - this.x);
		
		//cote = Math.abs(this.x - this.destX);
		hypo = Math.sqrt((deltaX1*deltaX1) + (deltaY1*deltaY1));
		
		if(hypo == 0)
			hypo=1;
		
		if(this.x >= this.destX && this.y >= this.destY){
			this.rotationMage = (float)(Math.toDegrees(Math.acos( deltaX1/hypo )));
		}else if(this.x >= this.destX && this.y <= this.destY){
			this.rotationMage = -((float)(Math.toDegrees(Math.acos( deltaX1/hypo ))));
		}else if(this.x <= this.destX && this.y >= this.destY){
			this.rotationMage = (float)(180 - (Math.toDegrees(Math.acos( deltaX1/hypo ))));
		}else{
			// Normalement, si ce cas se produit, c'est qu'il y a une bonne raison et on a alors x > destX et y < destY
			this.rotationMage = (float)(180 + Math.abs((Math.toDegrees(Math.acos( deltaX1/hypo )))));
		}
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
