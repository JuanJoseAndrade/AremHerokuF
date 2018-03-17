package andrade.arem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable{
    
/**
 *
 * @author 2107990
 */
    private Socket clientSocket;

    public Server(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    @Override
    public void run() {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String path = in.readLine();
            String formato;
            String longitud;
            byte[] bytes = null;
            if(path != null){
                path = path.split(" ")[1];
                if(path.contains(".html")){
                    bytes = Files.readAllBytes(new File("./" + path).toPath());
                    longitud = "" + bytes.length;
                    formato = "text/html";
                }
                else if(path.contains(".jpg")){
                    bytes = Files.readAllBytes(new File("./" + path).toPath());
                    longitud = "" + bytes.length;
                    formato = "image/jpg";
                }
                else{
                    bytes = Files.readAllBytes(new File("./index.html").toPath());
                    longitud = "" + bytes.length;
                    formato = "text/html";
                }
            }
            else{
                bytes = Files.readAllBytes(new File("./index.html").toPath());
                longitud = "" + bytes.length;
                formato = "text/html";
            }
            
            String output = "HTTP/1.1 200 OK\r\n"+ "Content-Type: " + formato + "\r\n"+"Content-Length: " + longitud + "\r\n\r\n";
               
            byte [] losbyte = output.getBytes();
            byte[] res = new byte[bytes.length + losbyte.length];
            for (int i = 0; i < losbyte.length; i++) {res[i] = losbyte[i];}
            for (int i = losbyte.length; i < losbyte.length + bytes.length; i++) {
                res[i] = bytes[i - losbyte.length];
            }
            System.out.println(res.length);
            clientSocket.getOutputStream().write(res);
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}