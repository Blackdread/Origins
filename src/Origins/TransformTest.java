package Origins;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * A test for transforming the graphics context
 *
 * @author kevin
 */
public class TransformTest extends BasicGame {
	/** The current scale applied to the graphics context */
	private float scale = 1;
	/** True if we should be scaling up */
	private boolean scaleUp;
	/** True if we should be scaling down */
	private boolean scaleDown;
	
	private boolean isRot;
	private float rot = 0.0f;
	
	/**
	 * Create a new test of graphics context rendering
	 */
	public TransformTest() {
		super("Transform Test");
	}
	
	/**
	 * @see org.newdawn.slick.BasicGame#init(org.newdawn.slick.GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		container.setTargetFrameRate(100);
	}

	/**
	 * @see org.newdawn.slick.BasicGame#render(org.newdawn.slick.GameContainer, org.newdawn.slick.Graphics)
	 */
	public void render(GameContainer contiainer, Graphics g) {
		g.setColor(Color.red);
		g.drawString("rot"+rot, 10, 50);
		
		g.translate(320,240);
		g.scale(scale, scale);

		
		
		for (int x=0;x<10;x++) {
			for (int y=0;y<10;y++) {
				if(x%2==0)
					g.rotate(-500+(x*100), -500+(y*100), rot);
					//g.rotate(640/2, 480/2, rot);
				//else
					//g.rotate(640/2, 480/2, 0);
				//g.scale(scale, scale);
				g.fillRect(-500+(x*100), -500+(y*100), 80, 80);
				//if(x%2==0)
					g.resetTransform();
					g.translate(320,240);
				g.scale(scale, scale);
				
			}
		}
		
		g.setColor(new Color(1,1,1,0.5f));
		g.fillRect(-320,-240,640,480);
		g.setColor(Color.white);
		g.drawRect(-320,-240,640,480);
	}

	/**
	 * @see org.newdawn.slick.BasicGame#update(org.newdawn.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta) {
		if (scaleUp) {
			scale += delta * 0.001f;
		}
		if (scaleDown) {
			scale -= delta * 0.001f;
		}
		if(isRot){
			rot+=1;
		}
	}

	/**
	 * @see org.newdawn.slick.BasicGame#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			System.exit(0);
		}
		if (key == Input.KEY_Q) {
			scaleUp = true;
		}
		if (key == Input.KEY_A) {
			scaleDown = true;
		}
		if(key == Input.KEY_1){
			rot+=1;
			isRot = true;
		}
		if(key == Input.KEY_2)
			rot-=1;
	}

	/**
	 * @see org.newdawn.slick.BasicGame#keyReleased(int, char)
	 */
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_Q) {
			scaleUp = false;
		}
		if (key == Input.KEY_A) {
			scaleDown = false;
		}
		if(key == Input.KEY_1){
			rot+=1;
			isRot = false;
		}
	}
	
	/**
	 * Entry point to our test
	 * 
	 * @param argv The arguments passed to the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new TransformTest());
			container.setDisplayMode(640,480,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
