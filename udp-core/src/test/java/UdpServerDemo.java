import org.smartboot.socket.udp.MessageProcessor;
import org.smartboot.socket.udp.UdpBootstrap;
import org.smartboot.socket.udp.UdpChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * @author 三刀
 * @version V1.0 , 2019/8/16
 */
public class UdpServerDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        UdpBootstrap<String> server = new UdpBootstrap<String>(new StringProtocol(), new MessageProcessor<String>() {
            @Override
            public void process(UdpChannel<String> session, SocketAddress remote, String msg) {
                System.out.println("msg:" + msg);
                byte[] b = msg.getBytes();
                ByteBuffer buffer = ByteBuffer.allocate(4 + b.length);
                buffer.putInt(b.length);
                buffer.put(b);
                buffer.flip();
                try {
                    session.write(buffer, remote);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 9999);
        server.start();


        UdpBootstrap<String> client = new UdpBootstrap<String>(new StringProtocol(), new MessageProcessor<String>() {
            @Override
            public void process(UdpChannel<String> session, SocketAddress remote, String msg) {
                System.out.println("msg:" + msg);
//                byte[] b = msg.getBytes();
//                ByteBuffer buffer = ByteBuffer.allocate(4 + b.length);
//                buffer.putInt(b.length);
//                buffer.put(b);
//                buffer.flip();
//                try {
//                    session.write(buffer, remote);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
        UdpChannel<String> channel = client.start();
        byte[] b = "HelloWorld".getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(4 + b.length);
        buffer.putInt(b.length);
        buffer.put(b);
        buffer.flip();
        channel.write(buffer, new InetSocketAddress("localhost",9999));
    }
}
