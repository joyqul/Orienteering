package tcpSocket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class tcpSocket extends Socket{

    DataInputStream is;
    DataOutputStream os;
    PrintWriter output;
    BufferedReader input;

    tcpSocket() throws IOException {
        super(InetAddress.getByName("140.113.27.45"), 93128);
        is = new DataInputStream(this.getInputStream());
        os = new DataOutputStream(this.getOutputStream());
        output = new PrintWriter(os);
        input = new BufferedReader(new InputStreamReader(is));
    }

    void send(JSONObject obj){
        output.println(obj.toString());
        output.flush();
    }

    JSONObject recieve(){
        try {
            return new JSONObject(input.readLine());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
