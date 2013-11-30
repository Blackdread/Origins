package Origins;
import org.lwjgl.Sys;

/**
 * Classe des unitŽs
 * @author Yoann CAPLAIN
 * @version 1.0
 */
public class Monster {
	
	private double deltaX;
	private double deltaY;
	
	private double x;
	private double y;
	
	private int destX;
	private int destY;
	
	private int vie;
	private int mana;
	private int kills;
	private int dmg;
	private int metamorphose;
	
	private boolean moveAttack;	// Bouge en gŽrant IA
	private boolean tenirPos;
	private long lastAttack;		// Temps de la derniere attaque (voir si avec un compte de loop ou temps gŽnŽrale)
	
	public static int nbreUnite = 0;
	public static int nbreNain = 0;
	public static int nbreFantassin = 0;
	public static int nbreMinautaure = 0;
	public static int nbreArcher = 0;
	public static int nbreCentaure = 0;
	public static int nbreMage = 0;
	
	// ID unique pour chaque monstre
	public static int IDincrement=0;
	private int id;
	
	// ID unique de l'unitŽ attaquer et verifier si c tjr a portee sinon voir si y a une autre cible ˆ c™tŽ
	@SuppressWarnings("unused")
	private int idCible;
	
	//*****************************************************************************************
	//    								CONSTRUCTEURS
	//*****************************************************************************************
	/** Default constructor */
	public Monster(int x, int y/*, int camp*/){		
		this.x = x;
		this.y = y;
		
		this.vie = 100;
		this.mana = 100;
		this.kills = 0;
		this.metamorphose = 0;
		this.dmg = 15;
		
		//this.camp = camp;
		this.moveAttack = false;
		this.tenirPos = false;
		this.lastAttack = getTime();
		//checkKill(kills);
		changeVitesse();
		
		Monster.nbreUnite++;
		Monster.nbreNain++;
		Monster.IDincrement++;
		this.id = IDincrement;
		checkStaticInt();
	}
	public Monster(int x, int y/*, int camp*/, int metamor){
		this.x = x;
		this.y = y;
		
		this.vie = 100;
		this.mana = 100;
		this.kills = 0;
		this.metamorphose = metamor;
		incrementNbType();
		changeMetamor(metamor);
		this.mana = 100; // Mis pour remettre a bloc car soustrait pile avant
		
		//this.camp = camp;
		this.moveAttack = false;
		this.tenirPos = false;
		this.lastAttack = getTime();
		//checkKill(kills);
		changeVitesse();
		
		Monster.nbreUnite++;
		Monster.IDincrement++;
		this.id = IDincrement;
		checkStaticInt();
	}
	
	public Monster(int x, int y/*, int camp*/, int metamor, int xx, int yy){
		this.x = x;
		this.y = y;	
		changeDestination(xx,yy);
		
		//this.destX = xx;
		//this.destY = yy;
		
		this.vie = 100;
		this.mana = 100;
		this.kills = 0;
		this.metamorphose = metamor;
		incrementNbType();
		changeMetamor(metamor);
		this.mana = 100;
		
		//this.camp = camp;
		this.moveAttack = false;
		this.tenirPos = false;
		this.lastAttack = getTime();
		//checkKill(kills);
		
		Monster.nbreUnite++;
		Monster.IDincrement++;
		this.id = IDincrement;
		checkStaticInt();
	}
	public Monster(int x, int y/*, int camp*/, int metamor, int xx, int yy, boolean MoveAttack){
		this.x = x;
		this.y = y;	
		changeDestination(xx,yy);
		
		//this.destX = xx;
		//this.destY = yy;
		
		this.vie = 100;
		this.mana = 100;
		this.kills = 0;
		this.metamorphose = metamor;
		incrementNbType();
		changeMetamor(metamor);
		this.mana = 100;
		
		//this.camp = camp;
		this.moveAttack = MoveAttack;	// ICI LA DIFFERENCE
		this.tenirPos = false;
		this.lastAttack = getTime();
		//checkKill(kills);
		
		Monster.nbreUnite++;
		Monster.IDincrement++;
		this.id = IDincrement;
		checkStaticInt();
	}
	
	// Constructeur d'unitŽ de l'ennemie
	/** Enemy constructor, id is given by player 1 */
	public Monster(int x, int y, int id2, int metamor, int xx, int yy, boolean MoveAttack){
		this.x = x;
		this.y = y;	
		changeDestination(xx,yy);
		
		//this.destX = xx;
		//this.destY = yy;
		
		this.vie = 100;
		this.mana = 100;
		this.kills = 0;
		this.metamorphose = metamor;
		incrementNbType();
		changeMetamor(metamor);
		this.mana = 100;
		
		//this.camp = camp;
		this.moveAttack = MoveAttack;	// ICI LA DIFFERENCE
		this.tenirPos = false;
		this.lastAttack = getTime();
		//checkKill(kills);
		
		Monster.nbreUnite++;
		Monster.IDincrement++;
		this.id = id2;
		checkStaticInt();
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
	public void setVie(int vie) {
		this.vie = vie;
	}
	/** Decrement life of the unit, there is not + damage (+ -> - & - -> -)*/
	public void setDmgSurUnit(int d){
		if(d <= 0)
			this.vie+= d;
		else
			this.vie-= d;
	}
	public void setMana(int mana) {
		this.mana += mana;
	}
	public void setKills() {
		this.kills++;
		checkKill(getKills());
	}
	public void setDmg(int dmg) {
		this.dmg = dmg;
	}
	public void setMetamor(int metamor) {
		this.metamorphose = metamor;
	}
	public void setLastAttack(long lastAtt) {
		this.lastAttack = lastAtt;
	}
	public void setTenirPos(boolean pos){
		this.tenirPos = pos;
	}
	public void setMoveAttack(boolean move){
		this.moveAttack = move;
	}
	public void setId(int id){
		this.id = id;
	}
	
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
	public int getVie() {
		return vie;
	}
	public int getMana() {
		return mana;
	}
	public int getKills() {
		return kills;
	}
	public int getDmg() {
		return dmg;
	}
	/*public int getCamp() {
		return camp;
	}*/
	public int getMetamor() {
		return metamorphose;
	}
	public boolean getMoveAttack(){
		return moveAttack;
	}
	public long getLastAttack(){
		return lastAttack;
	}
	public boolean getTenirPos(){
		return tenirPos;
	}
	public int getId(){
		return id;
	}
	
	//*****************************************************************************************
	//                                    METHODES
	//*****************************************************************************************	
	/** Move monster depending his deltaX and deltaY */
	public void moveMonster(){
		if(Math.abs(this.x - this.destX) <= 2)//{
			deltaX=0;
			/*System.out.println("deltaX MIS A 0");
		}//*/
		if(Math.abs(this.y - this.destY) <= 2)//{
			deltaY=0;
			/*System.out.println("deltaY MIS A 0");
		}//*/
		
		if(this.deltaX != 0 || this.deltaY != 0)	// J'AI CHANGE EN || MAIS AVANT SA MARCHAIT AVEC &&, JUSTE POUR TEST, CAR IL SE PEUT QU'ILS SOIENT SUR LE MEME X MAIS PAS MEME Y
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
		//}
			 //System.out.println("deltaX "+this.deltaX+" et "+this.deltaY+"  et  "+this.destX+" et "+this.destY);
	}
	/** Augment mana, max is 100 except for 5 -> 150 */
	public void augmenteMana(){
		switch(getMetamor()){
		case 0:
			setMana(9);
			if(getMana() > 100)
				this.mana = 100;
			break;
		case 1:
			setMana(5);
			if(getMana() > 100)
				this.mana = 100;
			break;
		case 2:
			setMana(6);
			if(getMana() > 100)
				this.mana = 100;
			break;
		case 3:
			setMana(3);
			if(getMana() > 100)
				this.mana = 100;
			break;
		case 4:
			setMana(3);
			if(getMana() > 100)
				this.mana = 100;
			break;
		case 5:
			setMana(4);
			if(getMana() > 150)
				this.mana = 150;
			break;
		default:
			setMana(2);
			break;
		}
		
	}
	
	/**
	 * Change metamorphose
	 * 
	 * @return true if possible
	 */
	public boolean changeMetamor(int metamor){
		/*
		 * 	Metamor = 0 -> UnitŽs de base		 			deplacement moyenne 	nain
		 *	Metamor = 1 -> UnitŽs de corps ˆ corps			deplacement rapide 		fantasin
		 *	Metamor = 2 -> UnitŽs de corp ˆ corps avancŽe	deplacement rapide 		minotaure
		 * 	Metamor = 3 -> UnitŽs de distance faible		deplacement lent		archer 		
		 * 	Metamor = 4 -> UnitŽs de de distance avancŽe	deplacement moyenne 	centaure
		 *  Metamor = 5 -> UnitŽs de distance de zone		deplacement lent		mage 
		 */
		checkStaticInt();
		switch(metamor){
		case 0:
			if(this.mana >= 60){
			setVie(100);
			setDmg(15);
			setMana(-60);
			decrementNbType();
			setMetamor(metamor);
			Monster.nbreNain++;
			killRestore();
			}else
			return false;
			break;
		case 1:
			if(this.mana >= 50){
			setVie(90);
			setDmg(22);
			setMana(-50);
			decrementNbType();
			setMetamor(metamor);
			Monster.nbreFantassin++;
			killRestore();
			break;
			}else
				return false;
		case 2:
			if(this.mana >= 50){
			setVie(160);
			setDmg(33);
			setMana(-50);
			decrementNbType();
			setMetamor(metamor);
			Monster.nbreMinautaure++;
			killRestore();
			break;
			}else
				return false;
		case 3:
			if(this.mana >= 50){
			setVie(130);	
			setDmg(25);
			setMana(-80);
			decrementNbType();
			setMetamor(metamor);
			Monster.nbreArcher++;
			killRestore();
			break;
			}else
				return false;
		case 4:
			if(this.mana >= 80){
			setVie(200);
			setDmg(40);
			setMana(-80);
			decrementNbType();
			setMetamor(metamor);
			Monster.nbreCentaure++;
			killRestore();
			break;
			}else
				return false;
		case 5:
			if(this.mana >= 90){
			setVie(100);
			setDmg(50);
			setMana(-90);
			decrementNbType();
			setMetamor(metamor);
			Monster.nbreMage++;
			killRestore();
			break;
			}else
				return false;
		default:
			if(this.mana >= 60){
				setMetamor(0);
				setVie(100);	
				setDmg(15);
				setMana(-60);
				Monster.nbreNain++;
				killRestore();
			}else
				return false;
		}
		//checkKill(getKills());
		return true;
	}
	public void decrementNbType(){
		switch(this.metamorphose){
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
	}
	public void incrementNbType(){
		switch(this.metamorphose){
		case 0:
			Monster.nbreNain++;
			break;
		case 1:
			Monster.nbreFantassin++;
			break;
		case 2:
			Monster.nbreMinautaure++;
			break;
		case 3:
			Monster.nbreArcher++;
			break;
		case 4:
			Monster.nbreCentaure++;
			break;
		case 5:
			Monster.nbreMage++;
			break;
		}
	}
	/** Change speed depending on metamorphose */
	public void changeVitesse(){
		switch(this.metamorphose){
		case 0:
			this.deltaX *= 2.0f;
			this.deltaY *= 2.0f;
			break;
		case 1:
			this.deltaX *= 1.5f;
			this.deltaY *= 1.5f;
			break;
		case 2:
			this.deltaX *= 3.0f;
			this.deltaY *= 3.0f;
			break;
		case 3:
			this.deltaX *= 2.5f;
			this.deltaY *= 2.5f;
			break;
		case 4:
			this.deltaX *= 3.5f;
			this.deltaY *= 3.5f;
			break;
		case 5:
			this.deltaX *= 0.9f;
			this.deltaY *= 0.9f;
			break;
		default:
			//this.deltaX *= 1.0f;
			//this.deltaY *= 1.0f;
			break;
		}
	}
	
	/** Change deltaX and deltaY */
	public void changeDestination(int xx, int yy){
		this.deltaX = ((double)(xx - (int)(this.x))/Math.sqrt(Math.pow((xx - this.x), 2) + Math.pow((yy - this.y), 2)));
		this.deltaY = ((double)(yy - (int)(this.y))/Math.sqrt(Math.pow((xx - this.x), 2) + Math.pow((yy - this.y), 2)));
		
		this.destX = xx;
		this.destY = yy;
		
		changeVitesse();
		this.tenirPos = false;
	}
	
	/** Augment dmg on number of kill */
	public void checkKill(int kill){
		if(kill == 4)	// Level 1
			setDmg(getDmg() + 1);
		if(kill == 6)
			setDmg(getDmg() + 4);
		if(kill == 11)	// Level 3
			setDmg(getDmg() + 4);
		if(kill == 16)
			setDmg(getDmg() + 7);
		if(kill == 30)	// Level 5
			setDmg(getDmg() + 10);
		}
	/** Restore dmg after a metamorphose */
	public void killRestore(){
		if(this.kills >= 30)
			setDmg(getDmg() + 26);
		else if(this.kills >= 16)
			setDmg(getDmg() + 16);
		else if(this.kills >= 11)
			setDmg(getDmg() + 9);
		else if(this.kills >= 6)
			setDmg(getDmg() + 5);
		else if(this.kills >= 4)
			setDmg(getDmg() + 1);
		else
			setDmg(getDmg());
	}

	/**
	 * Get the accurate system time 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	/**
	 * Get information about this Monster
	 * 
	 * @return Info this.monster
	 */
	public String toString(){
		return ("deltaX: "+this.deltaX+" deltaY: "+this.deltaY+" Kill: "+ this.getKills()+" Vie: "+this.getVie()+" x: "+this.getX()+" y: "+this.getY());
	}
	
	/** Kill this Monster, change variable */
	public void killMonster(){
		if(this.vie <= 0){
			// Cas de tuer unitŽ ennemie
			this.x = -100;
			this.y = -100;
			this.deltaX =0;
			this.deltaY=0;
			this.destX=0;
			this.destY=0;
		}
	}
	/** Kill this Monster, change variable, and decrement unit count */
	public void killMonster(int meta){
		if(this.vie <= 0){ 	// Verification
		nbreUnite--;
		this.x = -100;
		this.y = -100;
		this.deltaX =0;
		this.deltaY=0;
		this.destX=0;
		this.destY=0;
		switch(meta){
		case 0:
			nbreNain--;
			break;
		case 1:
			nbreFantassin--;
			break;
		case 2:
			nbreMinautaure--;
			break;
		case 3:
			nbreArcher--;
			break;
		case 4:
			nbreCentaure--;
			break;
		case 5:
			nbreMage--;
			break;
		}
		}
	}

	/** VŽrifier static int pour que ce ne soit pas en-dessous de 0 */
	private void checkStaticInt(){
		if(Monster.nbreUnite<0)
			Monster.nbreUnite = 0;
		if(Monster.nbreNain<0)
			Monster.nbreNain = 0;
		if(Monster.nbreFantassin<0)
			Monster.nbreFantassin = 0;
		if(Monster.nbreMinautaure<0)
			Monster.nbreMinautaure = 0;
		if(Monster.nbreArcher<0)
			Monster.nbreArcher = 0;
		if(Monster.nbreCentaure<0)
			Monster.nbreCentaure = 0;
		if(Monster.nbreMage<0)
			Monster.nbreMage = 0;
		
		
		
	}
}
