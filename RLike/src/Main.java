


public class Main
{
	public static void main(String args[])
	{
		TEngine eng=new TEngine();
		
		eng.init("Test",800,600);
		eng.createProgramBuffer(1);
		eng.loadProgram(0, "src/simple01.vs", "src/simple01.fs");
		eng.useProgram(0);
		
		eng.createTextureBuffer(4);
		eng.loadTexture(0, "res/tex_rock.jpg", "JPG");
		eng.loadTexture(1, "res/tex_grass.jpg", "JPG");
		
		while(eng.isRunning())
		{
			eng.draw();
		}
		
		eng.destroy();
	}
}
