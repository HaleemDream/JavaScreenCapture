package Image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Img {

    private Robot robot;
    private Rectangle subImage;
    private Rectangle fullImage;

    public Img() throws AWTException {
        robot = new Robot();
        subImage = new Rectangle();

        Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        fullImage = new Rectangle(dimension.width, dimension.height);
    }

    /**
     * returns sub-image
     * @param width
     * @param height
     * @return
     */
    public BufferedImage getImage(int width, int height){
        subImage.setSize(width, height);
        return robot.createScreenCapture(subImage);
    }

    /**
     * returns full screen image
     */
    public BufferedImage getImage(){
        return robot.createScreenCapture(fullImage);
    }

    public Image getScaledImage(int width, int height){
        return getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

}
