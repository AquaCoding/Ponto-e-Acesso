package aquacoding.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {
	
	// Copia uma imagem para um novo path, convertendo para png
	public static boolean copyImage(File file, String newPath) {
		try {
			// Cria uma BufferedImage
			BufferedImage bufferedImage;
			
			// Le a imagem original no buffer
			bufferedImage = ImageIO.read(file);
	
			// Cria uma nova imagem com as informações da imagem original
			BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			
			// Copia a imagem original para a nova imagem
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, null);
			
			// Salva a imagem no formato PNG
			ImageIO.write(newBufferedImage, "png", new File(newPath+".png"));
			
			// Retorna sucesso
			return true;
		} catch (IOException e) {
			throw new RuntimeException("Não foi possivel copiar a imagem");
		}
	}
}
