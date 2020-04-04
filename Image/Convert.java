package Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Convert {
    public static byte[] bufferedImageToBytes(BufferedImage image, String fmt) throws IOException {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
            ImageIO.write(image, fmt, out);
            return out.toByteArray();
        }
    }

    public static BufferedImage bytesToBuffedImage(byte[] bytes) throws IOException {
        try(InputStream in = new ByteArrayInputStream(bytes)){
            return ImageIO.read(in);
        }
    }

    public static BufferedImage bytesToBufferedImage(byte[] bytes, int len) throws IOException {
        return bytesToBuffedImage(Arrays.copyOfRange(bytes, 0, len));
    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}
