
import org.lwjgl.input.*;

public class Main
{
	public static final int SCREEN_WIDTH=1280;
	public static final int SCREEN_HEIGHT=720;
	
	public static void main(String args[])
	{
		TEngine eng=new TEngine(60);
		Sprite s[]=new Sprite[5];
		
		eng.init("Test",SCREEN_WIDTH,SCREEN_HEIGHT);
		eng.createProgramBuffer(1);
		eng.loadProgram(0, "src/simple01.vs", "src/simple01.fs");
		eng.useProgram(0);
		
		eng.createTextureBuffer(4);
		eng.loadTexture(0, "res/tex_rock.jpg", "JPG");
		eng.loadTexture(1, "res/tex_grass.jpg", "JPG");
		
		s[0]=eng.createSprite(0, 10.0f, 10.0f, 128.0f, 128.0f);
		s[1]=eng.createSprite(1, 300.0f, 50.0f, 64.0f, 64.0f);
		
		s[0].move(200.0f, 0.0f);
		s[0].setDepth(.4f);
		
		float d=0.0f;
		int x,y;
		while(eng.isRunning())
		{
			eng.draw();
			
			d+=0.1;
			
			x=Mouse.getX();
			y=Mouse.getY();
			s[1].moveto(x, SCREEN_HEIGHT-y);
			s[0].move((float)(10.0*Math.sin(d)), (float)(2.0*Math.cos(1.3*d)));

			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) s[0].move(0.0f, 5.0f);
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) s[0].move(0.0f, -5.0f);
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) s[0].move(-5.0f,0.0f);
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) s[0].move( 5.0f,0.0f);
		}
		
		eng.destroy();
	}
}
