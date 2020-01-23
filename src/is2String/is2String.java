package is2String;

import org.apache.commons.lang3.StringUtils;
//import net.sf.json.JSONArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class is2String {
    public static String resultSet2Json(ResultSet rs, String table)throws SQLException, JSONException{
        //JSONArray array = new JSONArray();
        ResultSetMetaData metaData = rs.getMetaData();
        //移动游标
        rs.first();
        int col = metaData.getColumnCount();
        String [] COL = new String[col - 1];
        List<String[]> VAL = new ArrayList<String[]>();
        JSONObject jsonObj = new JSONObject();
        while(rs.next()){
            String [] val = new String[col - 1];
            for(int i = 0;i < col;i++){
                if (i + 1 == 1){
                    continue;
                }
                String colName = metaData.getColumnName(i + 1);
                val[i - 1] = rs.getString(colName);
                if(COL[i - 1] != colName){
                    COL[i - 1] = colName;
                }
            }
            VAL.add(val);
            //array.put(jsonObj);
        }
        //String temp = StringUtils.join(COL,",");
        //String [] array = new String[temp.length()];
        //temp = temp.substring(0,temp.length() - 1);
        //COL[6] = "";
        //array = COL;
        //temp = StringUtils.strip(array.toString(),"[]");
        //temp = temp.substring(1,temp.length() - 1);
        //temp = temp.substring(1,temp.length());
        jsonObj.put("field", COL);
        //JSONArray array1 = new JSONArray();
        //jsonObj.append("data", StringUtils.strip(VAL,"[]"));
        //array1.put(VAL);
        //temp = StringUtils.strip(array1.toString(),"[]");
        //temp.replace("\\","");
        //temp = temp.substring(1,temp.length() - 1);
        //temp = StringUtils.join(temp,",");
        //temp = temp.substring(0,temp.length() - 1);

        //temp = StringUtils.join(VAL.toString(),",");
        //array.put(temp);
        //JSONArray array2 = new JSONArray();
        //array2.put(temp);
        jsonObj.put("data",VAL);
        JSONObject result = new JSONObject();
        result.append(table,jsonObj.toString());
        String result2Sr = result.toString();
        //System.out.println(result2Sr);
        return result2Sr;
    };
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

    public static List<ResultSet> allTable(Connection conn) throws SQLException{
        int t = 0;
        //int i = 1;
        List<ResultSet> Datalist = new ArrayList<ResultSet>();
        //设置参数保证可操作结果集游标
        Statement Tname = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
        //String sql = "SELECT name FROM MSysObjects where type=1 and flags=0";
        String sql = "";
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        ResultSet Tables = databaseMetaData.getTables(null,null,null,new String[] { "TABLE" });
        int size = 1000;
        String [] names = new String[size];
        while (Tables.next()){
            names[t] = Tables.getString("TABLE_NAME");
            sql = "SELECT * FROM " + names[t++];
            ResultSet Data = Tname.executeQuery(sql);
            Datalist.add(Data);
        }
        return Datalist;
    };

}
