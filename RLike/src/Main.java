
import java.io.*;

import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;



public class Main
{
	public static void main(String args[])
	{
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.setTitle("test");
			Display.create();
		}
		catch(Exception e){Display.destroy();System.exit(1);}
		
		int shaderProgram=glCreateProgram();
		int vertexShader=glCreateShader(GL_VERTEX_SHADER);
		int fragmentShader=glCreateShader(GL_FRAGMENT_SHADER);
		
		StringBuilder vsSource=new StringBuilder();
		StringBuilder fsSource=new StringBuilder();
		
		try {
			BufferedReader r=new BufferedReader(new FileReader("src/simple01.vs"));
			String line;
			while((line=r.readLine())!=null){vsSource.append(line+"\n");}
			r.close();
			
			r=new BufferedReader(new FileReader("src/simple01.fs"));
			while((line=r.readLine())!=null){fsSource.append(line+"\n");}
			r.close();
		}
		catch(Exception e){
			Display.destroy();System.exit(1);
		}
		glShaderSource(vertexShader,vsSource);
		glCompileShader(vertexShader);
		if (glGetShaderi(vertexShader,GL_COMPILE_STATUS) == GL_FALSE)  {System.out.println("error");}
		
		glShaderSource(fragmentShader,fsSource);
		glCompileShader(fragmentShader);
		if (glGetShaderi(fragmentShader,GL_COMPILE_STATUS) == GL_FALSE)  {System.out.println("error");}
		
		glAttachShader(shaderProgram,vertexShader);
		glAttachShader(shaderProgram,fragmentShader);
		
		glLinkProgram(shaderProgram);
		glValidateProgram(shaderProgram);
		
		while(!Display.isCloseRequested())
		{
			glUseProgram(shaderProgram);
			
			glBegin(GL_TRIANGLES);
			glColor3f(1,0,0);
			glVertex2f(-0.5f,-0.5f);
			glColor3f(0,1,0);
			glVertex2f(0.5f,-0.5f);
			glColor3f(0,0,1);
			glVertex2f(0.0f,0.5f);
			glEnd();
			
			glUseProgram(0);
			
			Display.update();
			Display.sync(60);
			
			
		}
		
		glDeleteProgram(shaderProgram);
		glDeleteShader(fragmentShader);
		glDeleteShader(vertexShader);
		
		Display.destroy();
		System.exit(0);
	}
}
