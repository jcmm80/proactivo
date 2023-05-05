/*
* ImageUtils.java
*
* Created on 20 de julio de 2005, 11:47
* Resizes jpeg image files on your file system.
* Uses the com.sun.image.codec.jpeg package shipped
* by Sun with Java 2 Standard Edition.
*
* @author Randy Belknap
* @revision Alejandro S�nchez Marcos
* se aprovecha la nueva clase ImageIO de 1.4
* y se a�aden algunos m�todos nuevos
*/
 
package com.utilidades;
 
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
 
/**

*/
public class ImageUtils {
static Logger logger = Logger.getLogger(ImageUtils.class);
/*
* devuelve la lista de formatos disponibles a leer por ImageIO
* @return un array de strings con los mismos.
*/
public static String[] getAvailableFormats(){
return ImageIO.getReaderFormatNames();
}
 
/*
* devuelve una imagen (buffer) en funci�n de la ruta de un archivo
* mejoras
* @param la ruta del archivo con su nombre
* @return BufferedImage la imagen en el buffer
*/
public static BufferedImage loadBufferedImage(String fileName) {
BufferedImage image = null;
try {
/*FileInputStream fis = new FileInputStream(fileName);JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(fis);bi = decoder.decodeAsBufferedImage();fis.close();*/
// MAS RAPIDO a partir de 1.4
image = ImageIO.read( new File( fileName ) );
}
catch (Exception e) {
logger.debug("error al intentar leer la im�gen");
return null;
}
return image;
 
}
/**
* comprueba que la im�gen tiene un m�nimo en pixels
* width y height tienen que tener un tama�o igual o mayor que el pasado
* como argumento
*
* @param w ancho m�nimo
* @param h alto m�nimo
* @return true o false
*
*
public static boolean isGreaterThanMinSize(int w, int h, BufferedImage imgSrc){
int nHeight = imgSrc.getHeight();
int nWidth = imgSrc.getWidth();
if ((nHeight return false;
} else {
return true;
}
}
 
/*
* calcula el factor de escala m�nimo y en base a eso escala la imagen
* seg�n el dicho factor.
* @param nMaxWidth minimo tama�o para el ancho
* @param nMaxHeight minimo tama�o para el alto
* @param imgSrc la im�gen
*/
public static BufferedImage scaleToSize(int nMaxWidth, int nMaxHeight, BufferedImage imgSrc) {
int nHeight = imgSrc.getHeight();
int nWidth = imgSrc.getWidth();
double scaleX = (double)nMaxWidth / (double)nWidth;
double scaleY = (double)nMaxHeight / (double)nHeight;
double fScale = Math.min(scaleX, scaleY);
return scale(fScale, imgSrc);
}
 
/*
* escala una imagen en porcentaje.
* @param scale ejemplo: scale=0.6 (escala la im�gen al 60%)
* @param srcImg una imagen BufferedImage
* @return un BufferedImage escalado
*/
public static BufferedImage scale(double scale, BufferedImage srcImg) {
if (scale == 1 ) {
return srcImg;
}
AffineTransformOp op = new AffineTransformOp
(AffineTransform.getScaleInstance(scale, scale), null);
 
return op.filter(srcImg, null);
 
}
 
public static void saveImageToDisk(BufferedImage bi, String str, String format) {
if (bi != null && str != null) {
 
// m�s r�pido con ImageIO
try {
ImageIO.write( bi, format /* formato */, new File( str ) /* destino */ );


} catch (Exception e){}
}
}

 public static void copyFile(String fileName, InputStream in,String path) {
           try {  
               
            //String path=System.getProperty(fileName); 
                //URL is = ClassLoader.getSystemResource();
                // write the inputStream to a FileOutputStream
              // URL is = ClassLoader.getSystemResource("hombre.jpeg");
               System.out.println(path); 
              OutputStream out = new FileOutputStream(path + fileName);
             
                int read = 0;
                byte[] bytes = new byte[1024];
             
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
             
                in.close();
                out.flush();
                out.close();
             
                System.out.println("New file created!");
                } catch (IOException e) {
                System.out.println(e.getMessage());
                }
    }


public static void resize(File img, int width, int height) throws Exception {
              BufferedImage dest = ImageIO.read(img);
              BufferedImage bImgEscaladaP = ImageUtils.scaleToSize(width, height, dest);
              BufferedImage bImgEscaladaG = ImageUtils.scaleToSize(600, 400, dest);
              
              
              URL is = ClassLoader.getSystemResource("web/Imagenes/Faces/"+img.getName()+".jpg");
              URL isg = ClassLoader.getSystemResource("web/Imagenes/Faces/"+img.getName()+"G.jpg");
              //ImageUtils.scaleToSize(width, height, dest); 
              System.out.println(is.getFile());
              System.out.println(isg.getPath());
              
              ImageUtils.saveImageToDisk(bImgEscaladaP,is.getFile(),"JPEG");
              ImageUtils.saveImageToDisk(bImgEscaladaG,isg.getFile(),"JPEG");
              //Image i=dest.getScaledInstance(width, height, width);
              //ar.setFileImg("C:\\proyectos\\TPV\\tpvlibre\\imagen\\"+ar.titulo+".jpg");
              //ar.setFlujo(ar.getFileImg());
              //ar.setFileImgG("C:\\proyectos\\TPV\\tpvlibre\\imagen\\"+ar.titulo+"G.jpg");
              //ar.setFlujoG(ar.getFileImgG());

       }


public static void main(String args[]) {
if(args.length != 3){usage();}
System.out.println(args[0]);
BufferedImage bImg = loadBufferedImage(args[0]);
System.out.println(bImg);
BufferedImage bImgEscaladaG = scaleToSize(800, 600, bImg);
BufferedImage bImgEscaladaP = scaleToSize(320, 200, bImg);
 
saveImageToDisk(bImgEscaladaG, args[1],"JPEG");
saveImageToDisk(bImgEscaladaP, args[2],"JPEG");
System.exit(0);
}
 
public static void usage(){
System.out.println("usage: java ImageUtils archivo_original imagen_grande imagen_peque�a ");
System.exit(1);
}
 
}