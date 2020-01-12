package is2String;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class is2String {
    public static String is2String(InputStream inputStream) throws IOException {
        StringBuffer sb = new StringBuffer();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((line=br.readLine()) != null){
            sb.append(line);
        }
        String str = sb.toString();
        return str;
    };

}
