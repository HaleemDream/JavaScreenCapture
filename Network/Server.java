package Network;

import java.io.IOException;
import java.net.*;

public class Server {

    private DatagramSocket server;

    public Server(int port) throws SocketException {
        server = new DatagramSocket(port);
    }

    public void send(byte[] buffer, int port, String host){
        try {
            server.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(host), port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(DatagramPacket packet) throws IOException {
        server.send(packet);
    }

    public DatagramPacket recv(int buffSize) throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[buffSize], buffSize);
        server.receive(packet);
        return packet;
    }

    public void sendTestData(int port, String host){
        String msg = "Hello";
        byte[] buffer = msg.getBytes();
        send(buffer, port, host);
    }

    public void setTimeout(int ms) throws SocketException {
        server.setSoTimeout(ms);
    }

    public void close(){
        server.close();
    }
}
