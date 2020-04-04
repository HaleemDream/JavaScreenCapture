import Image.Convert;
import Image.Img;
import Network.Server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

public class Main {

    enum Mode {
        INVALID,
        STREAM,
        VIEW;

        public static Mode get(String mode){
            switch (mode.toLowerCase()){
                case "stream":
                    return STREAM;
                case "view":
                    return VIEW;
                default:
                    return INVALID;
            }
        }
    }

    public static void main(String[] args) throws IOException, AWTException {

        int REQUIRED_ARGS = 4;

        //String mode
        Mode mode = Mode.get(args[0]);
        int localPort = Integer.valueOf(args[1]);
        int remotePort = Integer.valueOf(args[2]);
        String remoteHost = args[3];

        // byte size of payload
        int chunks = 32;

        if(args.length != REQUIRED_ARGS){
            System.out.println("Requires 4 args, [mode (stream/view), local port, remote port, remote host (string)]");
            System.exit(1);
        }

        if(mode == Mode.STREAM){
            Server server = new Server(localPort);

            Img image = new Img();

            while(true){
                DatagramPacket packet = server.recv(8);
                ByteBuffer byteBuffer = ByteBuffer.wrap(packet.getData());
                int width = byteBuffer.getInt();
                int height = byteBuffer.getInt();

                byte[] bytes = Convert.bufferedImageToBytes(Convert.toBufferedImage(image.getScaledImage(width, height)), "PNG");

                for(int i = 0; i < bytes.length; i += chunks){
                    DatagramPacket p = new DatagramPacket(bytes, i, bytes.length - i > chunks ? chunks : bytes.length - i, InetAddress.getByName(remoteHost), remotePort);
                    server.send(p);
                }
            }
        } else if(mode == Mode.VIEW){
            int FPS = 30;

            int width = 800;
            int height = 600;

            byte[] bytes = new byte[1000000];

            Server server = new Server(localPort);
            server.setTimeout(25);

            JFrame frame = new JFrame();
            JPanel panel = new JPanel();
            ImageIcon imageIcon = new ImageIcon();
            JLabel imageLabel = new JLabel(imageIcon);

            frame.add(panel);
            panel.add(imageLabel);

            frame.setSize(new Dimension(width, height));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            new Timer(1000 / FPS, e -> {

                server.send(ByteBuffer.allocate(8).putInt(width).putInt(height).array(), remotePort, remoteHost);
                int length = 0;
                try {
                    while (true){
                        for(byte b : server.recv(chunks).getData()){
                            bytes[length++] = b;
                        }
                    }
                }
                catch (SocketTimeoutException e1) {
                    // do nothing, expected
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }

                if(length > 0){
                    try {
                        imageIcon.setImage(Convert.bytesToBufferedImage(bytes, length));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    imageLabel.repaint();
                    imageLabel.revalidate();
                }
            }).start();
        } else {
            System.out.println("Invalid mode selected.");
            System.exit(1);
        }
    }
}
