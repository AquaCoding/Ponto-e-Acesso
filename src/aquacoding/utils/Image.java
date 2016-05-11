package aquacoding.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {
	
	public static final String APP_ICON_PATH = "img/app_icon.png";
	public static final String PROFILE_IMAGE_PATH = "img/profile/";
	public static final String PROFILE_IMAGE_EXTENSION = ".png";
	public static final String PROFILE_IMAGE_DEFAULT = PROFILE_IMAGE_PATH + "default" + PROFILE_IMAGE_EXTENSION;
	public static final String EMPRESA_IMAGE_PATH = "img/empresa/";
	public static final String EMPRESA_IMAGE_EXTENSION = ".png";
	
	// Copia uma imagem para um novo path, convertendo para png
	public static boolean copyImage(File file, String newPath) {
		try {
			// Cria uma BufferedImage
			BufferedImage bufferedImage;
			
			// Le a imagem original no buffer
			bufferedImage = ImageIO.read(file);
	
			// Cria uma nova imagem com as informa��es da imagem original
			BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			
			// Copia a imagem original para a nova imagem
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, null);
			
			// Salva a imagem no formato PNG
			ImageIO.write(newBufferedImage, "png", new File(newPath+".png"));
			
			// Retorna sucesso
			return true;
		} catch (IOException e) {
			throw new RuntimeException("N�o foi possivel copiar a imagem");
		}
	}
}
