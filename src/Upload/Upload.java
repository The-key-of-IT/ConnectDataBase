package Upload;
import is2String.*;
import jdk.nashorn.internal.runtime.Context;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;


public class Upload  {
    public static void upload(String ip, String json) throws IOException {
        URL url = new URL(ip);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        //connection.setChunkedStreamingMode(1000000);
        connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
        connection.connect();
        OutputStream os = connection.getOutputStream();
        OutputStreamWriter out = new OutputStreamWriter(os,"UTF-8");
        out.append(json);
        out.flush();
        out.close();
        //FileInputStream fs = new FileInputStream(path);
        //int len = 0;
        //byte[] bytes = new byte[1024];
        //while((len = fs.read(bytes)) != -1){
        //os.write(bytes,0,len);
        //}
        //fs.close();
        //os.close();
        System.out.println(os);
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK){
            InputStream inputStream = connection.getInputStream();
            String result = is2String.is2String(inputStream);
            System.out.println(result);
            System.out.println("上传成功！");
            connection.disconnect();
        }else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND){
            System.out.println("服务器无响应！");
            connection.disconnect();
        }else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){
            //String msg = URLDecoder.decode(connection.getResponseMessage(), "UTF-8");
           // Context.ThrowErrorManager logger = null;
            //logger.error("同步错误信息:" + msg);
            System.out.println("服务器内部错误！");
            connection.disconnect();
        }

    };
}
