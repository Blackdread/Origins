package Origins;
/**
 * Classe tire archer
 * @author Yoann CAPLAIN
 * @version 1.5
 */
public class TireArcher extends Tire{
	// /** Rotation de l'image pour que la fleche soit bien axée sur l'ennemie */
	// private int rotationImage;	// Non fait encore // FINALEMENT A partir du 2 avril, l'image de la fleche a ete change donc l'image va aussi utiliser l'attribut qui est dans Tire
	// Mon système d'héritage n'est plus vraiment utile, il faudrait que je fasses la classe Tire en abstract
	
	public  TireArcher(int x, int y, int xx, int yy, int rotation){
		super(x,y,xx,yy);
		//this.rotationImage = rotation;
		
		//this.setType(3);
		this.setDeltaX(getDeltaX() * 5.0f);
		this.setDeltaY(getDeltaY() * 5.0f);
	}
	
	public  TireArcher(int x, int y, int xx, int yy, int dmg, int id, int idCible, int rotation){
		super(x,y,xx,yy,dmg,id,idCible);
		//this.rotationImage = rotation;
		
		//this.setType(3);
		this.setDeltaX(getDeltaX() * 5.0f);
		this.setDeltaY(getDeltaY() * 5.0f);
	}
}
