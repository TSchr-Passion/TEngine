#version 400

uniform sampler2D texture[2];
 
// corresponds with output from vertex shader
in vec3 Color;
in vec3 TexCoord;
 
out vec4 FragColor;
 
void main()
{
	// assign vertex color to pixel color
    //FragColor = vec4(Color, 1.0);
    FragColor = texture2D(texture[0], TexCoord.st)*vec4(Color, 1.0);;
}