


public class Sprite
{
	public int id;
	public float scr_Wf,scr_Hf;
	public float pos[];
	public int textureid;
	public int flags;
	public float color[];
	
	
	public Sprite()
	{
		pos=new float[]{0.0f, 0.0f, 0.0f, 0.0f, -0.5f};
		color=new float[]{1.0f, 1.0f, 1.0f, 1.0f};
		flags=0;
		scr_Wf=1.0f;
		scr_Hf=1.0f;
	}
	
	public void move(float dx,float dy)
	{
		pos[0]+=dx*scr_Wf;
		pos[2]+=dx*scr_Wf;
		pos[1]-=dy*scr_Hf;
		pos[3]-=dy*scr_Hf;
	}
	
	public void moveto(float x,float y)
	{
		float w,h;
		w=pos[2]-pos[0];
		h=pos[3]-pos[1];
		
		pos[0]=x*scr_Wf-1.0f;
		pos[1]=(2.0f/scr_Hf-y)*scr_Hf-1.0f-h;
		pos[2]=(x)*scr_Wf-1.0f+w;
		pos[3]=(2.0f/scr_Hf-y)*scr_Hf-1.0f;
	}
	
	public void setDepth(float z)
	{
		pos[4]=-z;
	}
	
}