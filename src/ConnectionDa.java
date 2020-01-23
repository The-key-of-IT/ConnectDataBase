import java.sql.*;
import java.io.*;
import java.util.List;

import Upload.*;
import is2String.is2String;
import org.json.JSONObject;

/* TIPS
 * 剩余功能：计时器功能👌
 * 计时器实现概念
 * 使用getRow获取最大行数 一旦发生改变
 * 使用absolute移动光标 读取该条数据
 * 获取该条数据后发送
 * 制定接口码
 * 使用最大行数作为是否上传的标识需要建立文件记录
 */

class ThProcess implements Runnable{
    private List<ResultSet> Data;
    private int size;
    private String[] table;
    private int [] maxCount;
    ThProcess (List<ResultSet> data,int size,String[] table,int [] maxCount){
        this.Data = data;
        this.size = size;
        this.table = table;
        this.maxCount = maxCount;
    }
    @Override
    public void run() {
        while (true){
            try {
                for (int i = 0 ;i < size;i++){
                    ResultSet rs = Data.get(1);
                    rs.last();
                    int currCount = rs.getRow();
                    String JSON = is2String.resultSet2Json(rs,table[1]);
                    if (currCount != maxCount[1]){
                        maxCount[1] = currCount;
                        Upload.upload("http://139.155.71.69/tp5/public/index.php/api/jsontest/test",JSON);
                    }else {
                        System.out.println("数据库未改变故不上传！");
                    }

                    //Upload.upload("http://139.155.71.69/tp5/public/index.php/api",JSON);

                }
                Thread.sleep(10000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}


public class ConnectionDa {
    public static void main(String [] args){
        try {
            //Databasa Connection
            //Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            //String url="jdbc:odbc:Access2000";
            String mdbPath = "D:/Database/Data.mdb";
            String url = "jdbc:ucanaccess:///" + mdbPath;
            Connection connection = DriverManager.getConnection(url);
            if (!connection.isClosed()){
                System.out.println("成功链接access数据库！");
            }else {
                System.out.println("链接数据库失败！");
            }
            //Statement statement = connection.createStatement();
            //String sql = "SELECT * FROM T_Data";
            //ResultSet rs = statement.executeQuery(sql);
            
            List<ResultSet> Data = is2String.allTable(connection);
            //String tt;
            //FileStearm IO
            int size = Data.size();
            String [] table = new String[size];
            int [] maxCount = new int[size];
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet TableRes = databaseMetaData.getTables(null,null,null,new String[]{"TABLE"});
            int t = 0;
            while (TableRes.next()){
                table[t++] = TableRes.getString("TABLE_NAME");
            }
            ResultSet rs = null;
            for (int i = 0 ;i < size;i++){
                rs = Data.get(i);
                rs.last();
                maxCount[i] = rs.getRow();
                String JSON = is2String.resultSet2Json(rs,table[i]);

                Upload.upload("http://139.155.71.69/tp5/public/index.php/api/jsontest/test",JSON);
                //Upload.upload("http://139.155.71.69/tp5/public/index.php/api",JSON);
            }
            ThProcess process = new ThProcess(Data,size,table,maxCount);
            Thread Tprocess = new Thread(process,"循环上传");
            Tprocess.start();
            //Upload.upload("https://baidu.com",JSON);
            while (rs.next()) {
                System.out.print(rs.getString("ID") + "\t");
                //rs.close();
                //connection.close();
            }
            //System.out.println(rs);
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}


