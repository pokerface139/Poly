import java.awt.PageAttributes.ColorType;
import java.math.*;

/**
 * Classe PixelMapPlus
 * Image de type noir et blanc, tons de gris ou couleurs
 * Peut lire et ecrire des fichiers PNM
 * Implemente les methodes de ImageOperations
 * @author : 
 * @date   : 
 */

public class PixelMapPlus extends PixelMap implements ImageOperations 
{
	/**
	 * Constructeur creant l'image a partir d'un fichier
	 * @param fileName : Nom du fichier image
	 */
	PixelMapPlus(String fileName)
	{
		super( fileName );
	}
	
	/**
	 * Constructeur copie
	 * @param type : type de l'image a creer (BW/Gray/Color)
	 * @param image : source
	 */
	PixelMapPlus(PixelMap image)
	{
		super(image); 
	}
	
	/**
	 * Constructeur copie (sert a changer de format)
	 * @param type : type de l'image a creer (BW/Gray/Color)
	 * @param image : source
	 */
	PixelMapPlus(ImageType type, PixelMap image)
	{
		super(type, image); 
	}
	
	/**
	 * Constructeur servant a allouer la memoire de l'image
	 * @param type : type d'image (BW/Gray/Color)
	 * @param h : hauteur (height) de l'image 
	 * @param w : largeur (width) de l'image
	 */
	PixelMapPlus(ImageType type, int h, int w)
	{
		super(type, h, w);
	}
	
	/**
	 * Genere le negatif d'une image
	 */
	public void negate()
	{
		for (int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				imageData[i][j] = imageData[i][j].Negative();
			}
		}
	}
	
	/**
	 * Convertit l'image vers une image en noir et blanc
	 */
	public void convertToBWImage()
	{
		PixelMap copy = new PixelMap(ImageType.BW, this);
		imageData = copy.imageData;
	}
	
	/**
	 * Convertit l'image vers un format de tons de gris
	 */
	public void convertToGrayImage()
	{
		PixelMap copy = new PixelMap(ImageType.Gray, this);
		imageData = copy.imageData;
	}
	
	/**
	 * Convertit l'image vers une image en couleurs
	 */
	public void convertToColorImage()
	{
		PixelMap copy = new PixelMap(ImageType.Color, this);
		imageData = copy.imageData;
	}
	
	public void convertToTransparentImage()
	{
		PixelMap copy = new PixelMap(ImageType.Transparent, this);
		imageData = copy.imageData;
	}
	
	/**
	 * Fait pivoter l'image de 10 degres autour du pixel (row,col)=(0, 0)
	 * dans le sens des aiguilles d'une montre (clockWise == true)
	 * ou dans le sens inverse des aiguilles d'une montre (clockWise == false).
	 * Les pixels vides sont blancs.
	 * @param clockWise : Direction de la rotation 
	 */
	public void rotate(int x, int y, double angleRadian)
	{
		AbstractPixel[][] temp = new AbstractPixel[height][width];
		
		long xp;
		long yp;
		
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				xp = Math.round(Math.cos(angleRadian) * j + Math.sin(angleRadian) * i 
						- Math.cos(angleRadian) * x - Math.sin(angleRadian) *y + x); 
				
				yp = Math.round(-Math.sin(angleRadian) * j + Math.cos(angleRadian) * i 
						+ Math.sin(angleRadian) * x - Math.cos(angleRadian) *y + y);
				
				if(yp >= 0  && yp < height && xp >= 0 && xp < width)
					temp[i][j] = imageData[(int)yp][(int)xp];
				else
					temp[i][j] = new BWPixel();
			}
		}
		
		
		imageData = temp;
	}
	
	/**
	 * Modifie la longueur et la largeur de l'image 
	 * @param w : nouvelle largeur
	 * @param h : nouvelle hauteur
	 */
	public void resize(int w, int h) throws IllegalArgumentException
	{
		if(w < 0 || h < 0)
			throw new IllegalArgumentException();
		
		// compl�ter
		
		float xRatio;
		float yRatio;
		
		// Calcul des ratios
		xRatio = (float)width/(float)w;
		yRatio = (float)height/(float)h;

		
		AbstractPixel[][] temp1 = new AbstractPixel[height][w];
		AbstractPixel[][] temp2 = new AbstractPixel[h][w];
		
		// Élargissement sur la largeur
		for (int i = 0; i < height; i++)
		{
			for(int j = 0; j < w; j++)
			{
				temp1[i][j] = imageData[i][(int)(xRatio*j)];
			}	
		}
		
		// Élargissement sur la hauteur
		for (int j = 0; j < w; j++)
		{
			for(int i = 0; i < h; i++)
			{
				temp2[i][j] = temp1[(int)(i*yRatio)][j];
			}
		}
		
		// Ajustement de la taille de la fenêtre
		width = w;
		height = h;
		
		// Assignation de l'image rétrécie
		imageData = temp2;
		
		
	}
	
	/**
	 * Insert pm dans l'image a la position row0 col0
	 */
	public void inset(PixelMap pm, int row0, int col0)
	{
		int xMaxIndex = (pm.height + row0 > height) ? height : row0+pm.height;
		int yMaxIndex = (pm.width + col0 > width) ? width : col0+pm.width;
		
		for (int i = row0; i < xMaxIndex; i++)
		{
			for (int j = col0; j < yMaxIndex ; j++)
			{
				 switch (imageType){
					case BW : 
						imageData[i][j] = pm.imageData[i-row0][j-col0].toBWPixel();
						break;
						
					case Gray : 
						
						imageData[i][j] = pm.imageData[i-row0][j-col0].toGrayPixel();
						break;
						
					case Color : 
						
						imageData[i][j] = pm.imageData[i-row0][j-col0].toColorPixel();
						break;
						
					case Transparent : 
						
						imageData[i][j] = pm.imageData[i-row0][j-col0].toTransparentPixel();
						break;			
						
					}
			}
		}
	}
	
	/**
	 * Decoupe l'image 
	 */
	public void crop(int h, int w)
	{

		AbstractPixel[][] temp = new AbstractPixel[h][w];
		
		if(h>0 && w > 0)
		{
			for (int i = 0; i < h; i++)
			{
				for(int j = 0; j < w; j++)
				{
					if(i < height && j < width)
					{
						temp[i][j] = imageData[i][j];
					}
					else
					{
						temp[i][j] = new BWPixel();
					}
				}
			}
		
			width = w;
			height = h;
			imageData = temp;
		}
	}
	
	/**
	 * Effectue une translation de l'image 
	 */
	public void translate(int rowOffset, int colOffset)
	{
		AbstractPixel[][] temp = new AbstractPixel[height][width];
		AbstractPixel[][] temp2 = new AbstractPixel[height][width];
		
		// Translation en x
		if (rowOffset >= 0)
		{
			for (int i = 0 ; i < height ; i++)
			{
				for (int j = 0; j < width; j++)
				{
					if( j - rowOffset >= 0)
					{
						temp[i][j] = imageData[i][j-rowOffset];
					}
					else
					{
						temp[i][j] = new BWPixel();
					}
				}
			}
		}
		else
		{
			for (int i = 0 ; i < height ; i++)
			{
				for (int j = 0; j < width; j++)
				{
					if( j  >= width + rowOffset)
					{
						temp[i][j] = new BWPixel();
					}
					else
					{
						temp[i][j] = imageData[i][j - rowOffset];
					}
				}
			}
		}
		
		
		// Translation en y
		
		if (colOffset >= 0)
		{
			for (int j = 0 ; j < width ; j++)
			{
				for (int i = 0; i < height; i++)
				{
					if( i - colOffset >= 0)
					{
						temp2[i][j] = temp[i - colOffset][j];
					}
					else
					{
						temp2[i][j] = new BWPixel();
					}
				}
			}
		}
		else
		{
			for (int j = 0 ; j < width ; j++)
			{
				for (int i = 0; i < height; i++)
				{
					if( j  >= height + colOffset)
					{
						temp2[i][j] = new BWPixel();
					}
					else
					{
						temp2[i][j] = temp[i-colOffset][j];
					}
				}
			}
		}
		imageData = temp2;
		
	}
	
	/**
	 * Effectue un zoom autour du pixel (x,y) d'un facteur zoomFactor 
	 * @param x : colonne autour de laquelle le zoom sera effectue
	 * @param y : rangee autour de laquelle le zoom sera effectue  
	 * @param zoomFactor : facteur du zoom a effectuer. Doit etre superieur a 1
	 */
	public void zoomIn(int x, int y, double zoomFactor) throws IllegalArgumentException
	{
		if(zoomFactor < 1.0)
			throw new IllegalArgumentException();
		
		int w = (int) (this.width / zoomFactor);
		int h = (int) (this.height/ zoomFactor);
		
		int x0;
		int y0;
		
		if (y < h / 2)
		{
			y0 = 0;
		}
		else if (y > height - h/2)
		{
			y0 = height - h;
		}
		else
		{
			y0 = y - h/2;
		}
		
		if (x < w / 2)
		{
			x0 = 0;
		}
		else if (x > width - w/2)
		{
			x0 = width - w;
		}
		else
		{
			x0 = x - w/2;
		}
		
	
		AbstractPixel[][] temp = new AbstractPixel[h][w];
		
		for (int i = y0; i < y0+h; i++)
		{
			for(int j= x0; j < x0+w; j++)
			{
				temp[i-y0][j-x0] = imageData[i][j];
			}
		}
		
		imageData = temp;
		int wTemp = width;
		int hTemp = height;
		width = w;
		height =  h;
		resize(wTemp,hTemp);
		
	}
}
