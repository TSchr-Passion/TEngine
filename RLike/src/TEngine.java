
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TEngine
{
	int program[][]; //[count] [program,vertexshader,fragmentshader]
	Texture texture[]; //all texturemaps
	
	int vertex_buffer;
	
	public TEngine()
	{
		
	}
	
	public void init(String title,int width,int height)
	{
		try {
			Display.setDisplayMode(new DisplayMode(width,height));
			Display.setTitle(title);
			Display.create();
			
			System.out.println("OpenGL ver.: "+GL11.glGetString(GL11.GL_VERSION));
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
					GL20.glDeleteProgram(program[i][0]);
					GL20.glDeleteShader(program[i][1]);
					GL20.glDeleteShader(program[i][2]);
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
			GL20.glDeleteProgram(program[num][0]);
			GL20.glDeleteShader(program[num][1]);
			GL20.glDeleteShader(program[num][2]);
		}
		
		program[num][0]=GL20.glCreateProgram();
		program[num][1]=GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		program[num][2]=GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		
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
		GL20.glShaderSource(program[num][1],vsSource);
		GL20.glCompileShader(program[num][1]);
		if (GL20.glGetShaderi(program[num][1],GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)  {System.out.println("error compiling vertexshader");}
		
		GL20.glShaderSource(program[num][2],fsSource);
		GL20.glCompileShader(program[num][2]);
		if (GL20.glGetShaderi(program[num][2],GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)  {System.out.println("error compiling fragmentshader");}
		
		GL20.glAttachShader(program[num][0],program[num][1]);
		GL20.glAttachShader(program[num][0],program[num][2]);
		
		GL20.glLinkProgram(program[num][0]);
		GL20.glValidateProgram(program[num][0]);
				
	}
	
	public void useProgram(int num)
	{
		if ((program==null) || (num<0) || (num>=program.length) || (program[num]==null) || (program[num][0]==0)) return;
		GL20.glUseProgram(program[num][0]);
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
	
	public boolean isRunning()
	{
		return !Display.isCloseRequested();
	}
	
	public void setupVBO()
	{
		// create vertex data 
		float[] positionData = new float[] {
		    	0f,		0f,		0f,
		    	-1f,	0f, 	0f,
		    	0f,		0.8f,		0f
		};
 
		// create color data
		float[] colorData = new float[]{
				0f,			0f,			1f,
				1f,			0f,			0f,
				0f,			1f,			0f
		};
		
		// create color data
		float[] textureData = new float[]{
				0f,			0f,			1.0f,
				1f,			0f,			1.0f,
				0f,			1f,			1.0f
		};
 
		// convert vertex array to buffer
		FloatBuffer positionBuffer = BufferUtils.createFloatBuffer(positionData.length);
		positionBuffer.put(positionData);
		positionBuffer.flip();
 
		// convert color array to buffer
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colorData.length);
		colorBuffer.put(colorData);
		colorBuffer.flip();
		
		FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(textureData.length);
		textureBuffer.put(positionData);
		textureBuffer.flip();
 
		// create vertex byffer object (VBO) for vertices
		int positionBufferHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, positionBufferHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, GL15.GL_STATIC_DRAW);
 
		// create VBO for color values
		int colorBufferHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);
 
		int texBufferHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texBufferHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);
		
		// unbind VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
 
		// create vertex array object (VAO)
		int vaoHandle = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoHandle);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
 
		// assign vertex VBO to slot 0 of VAO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, positionBufferHandle);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
 
		// assign vertex VBO to slot 1 of VAO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferHandle);
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);
		
		// assign vertex VBO to slot 2 of VAO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texBufferHandle);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);
 
		// unbind VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
 
		vertex_buffer= vaoHandle;
	}
	
	public void draw()
	{
		texture[1].bind();
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL30.glBindVertexArray(vertex_buffer);
		GL20.glEnableVertexAttribArray(0); // VertexPosition
		GL20.glEnableVertexAttribArray(1); // VertexColor
		GL20.glEnableVertexAttribArray(2); // VertexTex
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
		
		Display.update();
		Display.sync(60);
	}
}