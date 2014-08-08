

public class Map
{
	public static final int FIELD_WIDTH_MAX=400;
	public static final int FIELD_HEIGHT_MAX=400;
	
	private int m_Width,m_Height,m_Size;
	private int m_Field[];
	
	
	public Map(int width,int height)
	{
		m_Width=width;
		m_Height=height;
		
		if (m_Width<1) m_Width=1;if (m_Width>FIELD_WIDTH_MAX) m_Width=FIELD_WIDTH_MAX;
		if (m_Height<1) m_Height=1;if (m_Height>FIELD_HEIGHT_MAX) m_Height=FIELD_HEIGHT_MAX;
		
		m_Size=m_Width*m_Height;
		m_Field=new int[m_Size];
	}
	
	public int getField(int x,int y)
	{
		if ((x<0) || (x>=m_Width) || (y<0) || (y>=m_Height)) return 0;
		return m_Field[x+y*m_Width];
	}
	
	public void setField(int x,int y,int f)
	{
		if ((x<0) || (x>=m_Width) || (y<0) || (y>=m_Height)) return;
		
		m_Field[x+y*m_Width]=f;
	}
}