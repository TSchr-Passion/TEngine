


public class Main
{
	public static void main(String args[])
	{
		TEngine eng=new TEngine();
		
		eng.init("Test",800,600);
		eng.createProgram(1);
		eng.loadProgram(0, "src/simple01.vs", "src/simple01.fs");
		
		while(eng.isRunning())
		{
			eng.draw();
		}
		
		eng.destroy();
	}
}
