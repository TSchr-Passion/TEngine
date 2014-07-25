//VERTEX SHADER
varying vec4 color;

void main()
{
	color = gl_Color.rgba;
	gl_Position=ftransform();
	gl_TexCoord[0] = gl_MultiTexCoord0;
}