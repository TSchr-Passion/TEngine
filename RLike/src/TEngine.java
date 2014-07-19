
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;

public class TEngine
{
	int m_Width,m_Height;
	int program[][]; //[count] [program,vertexshader,fragmentshader]
	Texture texture[]; //all texturemaps
	ArrayList<Sprite> spritelist; 
	int spriteid;
	int m_fps;
	
	public TEngine(int fps)
	{
		spriteid=1;
		m_fps=fps;
	}
	
	public void init(String title,int width,int height)
	{
		try {
			m_Width=width;
			m_Height=height;
			Display.setDisplayMode(new DisplayMode(width,height));
			Display.setTitle(title);
			Display.create();
			
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_DEPTH_TEST);
			
			spriteid=1;
			spritelist=new ArrayList<Sprite>();
		}
		catch(Exception e){
			System.out.println("Could not initialize Display");
			Display.destroy();System.exit(1);
		}
	}
	public void destroy()
	{
		deleteProgramBuffer();
		Display.destroy();
	}
	
	public void createProgramBuffer(int num)
	{
		int c=num;
		if (c<1) c=1;
		deleteProgramBuffer();
		
		program=new int[c][3];
	}
	public void deleteProgramBuffer()
	{
		if (program!=null)
		{
			for(int i=0;i<program.length;++i)
			{
				if (program[i]!=null)
				{
					glDeleteProgram(program[i][0]);
					glDeleteShader(program[i][1]);
					glDeleteShader(program[i][2]);
				}
			}
		}
		program=null;
	}
	public void loadProgram(int num,String vsFile,String fsFile)
	{
		if ((num<0) || (program==null) || (num>=program.length)) return;
		
		if (program[num]!=null)
		{
			glDeleteProgram(program[num][0]);
			glDeleteShader(program[num][1]);
			glDeleteShader(program[num][2]);
		}
		
		program[num][0]=glCreateProgram();
		program[num][1]=glCreateShader(GL_VERTEX_SHADER);
		program[num][2]=glCreateShader(GL_FRAGMENT_SHADER);
		
		StringBuilder vsSource=new StringBuilder();
		StringBuilder fsSource=new StringBuilder();
		
		try {
			BufferedReader r=new BufferedReader(new FileReader(vsFile));
			String line;
			while((line=r.readLine())!=null){vsSource.append(line+"\n");}
			r.close();
			
			r=new BufferedReader(new FileReader(fsFile));
			while((line=r.readLine())!=null){fsSource.append(line+"\n");}
			r.close();
		}
		catch(Exception e){
			System.out.println("Error loading Shaderfiles: "+e.toString());
			Display.destroy();System.exit(1);
		}
		glShaderSource(program[num][1],vsSource);
		glCompileShader(program[num][1]);
		if (glGetShaderi(program[num][1],GL_COMPILE_STATUS) == GL_FALSE)  {System.out.println("error compiling vertexshader");}
		
		glShaderSource(program[num][2],fsSource);
		glCompileShader(program[num][2]);
		if (glGetShaderi(program[num][2],GL_COMPILE_STATUS) == GL_FALSE)  {System.out.println("error compiling fragmentshader");}
		
		glAttachShader(program[num][0],program[num][1]);
		glAttachShader(program[num][0],program[num][2]);
		
		glLinkProgram(program[num][0]);
		glValidateProgram(program[num][0]);
	}
	public void useProgram(int num)
	{
		if ((program==null) || (num<0) || (num>=program.length) || (program[num]==null) || (program[num][0]==0)) return;
		glUseProgram(program[num][0]);
	}
	
	public void destroyTextureBuffer()
	{
		if (texture!=null)
		{
			for(int i=0;i<texture.length;++i)
			{
				if (texture[i]!=null) texture[i].release();
			}
		}
	}
	public void createTextureBuffer(int size)
	{
		int s=size;
		
		if (s<1) s=1;
		destroyTextureBuffer();
		
		texture=new Texture[s];
	}
	public boolean loadTexture(int num,String filename,String filetype)
	{
		if ( (texture==null) || (num<0) || (num>=texture.length) ) return false;
		
		try {
			texture[num] = TextureLoader.getTexture(filetype, ResourceLoader.getResourceAsStream(filename));
		}
		catch(Exception e){e.printStackTrace();}
				
		return true;
	}
	
	public Sprite createSprite(int texture,float x,float y,float w,float h)
	{
		spriteid++;
		
		Sprite s=new Sprite();
		s.id=spriteid;
		s.textureid=texture;
		s.scr_Wf=2.0f/m_Width;
		s.scr_Hf=2.0f/m_Height;
		
		s.pos[0]=2.0f*x/(float)m_Width-1.0f;
		s.pos[1]=2.0f*((float)m_Height-y-h)/(float)m_Height-1.0f;
		s.pos[2]=2.0f*(x+w)/(float)m_Width-1.0f;
		s.pos[3]=2.0f*((float)m_Height-(y))/(float)m_Height-1.0f;
		
		spritelist.add(s);
		
		return s;
	}
	public boolean isRunning()
	{
		return !Display.isCloseRequested();
	}
	
	public void draw()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		for(Sprite s:spritelist)
		{
			texture[s.textureid].bind();
			glBegin(GL_TRIANGLES);
			
			glColor3f(1,1,1);glTexCoord2f(0.0f, 1.0f);glVertex3f(s.pos[0],s.pos[1],s.pos[4]);
			glColor3f(1,1,1);glTexCoord2f(0.0f, 0.0f);glVertex3f(s.pos[0],s.pos[3],s.pos[4]);
			glColor3f(1,1,1);glTexCoord2f(1.0f, 1.0f);glVertex3f(s.pos[2],s.pos[1],s.pos[4]);
			
			glColor3f(1,1,1);glTexCoord2f(1.0f, 1.0f);glVertex3f(s.pos[2],s.pos[1],s.pos[4]);
			glColor3f(1,1,1);glTexCoord2f(0.0f, 0.0f);glVertex3f(s.pos[0],s.pos[3],s.pos[4]);
			glColor3f(1,1,1);glTexCoord2f(1.0f, 0.0f);glVertex3f(s.pos[2],s.pos[3],s.pos[4]);
			
			glEnd();
		}
		
		Display.update();
		Display.sync(m_fps);
	}

}