package Upload;
import is2String.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class Upload  {
    public static void upload(String ip,String path) throws IOException {
        URL url = new URL(ip);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type","file/*");
        connection.connect();

        OutputStream os = connection.getOutputStream();
        FileInputStream fs = new FileInputStream(path);
        int len = 0;
        byte[] bytes = new byte[1024];
        while((len = fs.read(bytes)) != -1){
            os.write(bytes,0,len);
        }
        fs.close();
        os.close();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK){
            InputStream inputStream = connection.getInputStream();
            String result = is2String.is2String(inputStream);
            System.out.println(result);
        }
    };
}
