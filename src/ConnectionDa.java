import java.sql.*;
import java.io.*;
import Upload.*;


public class ConnectionDa {
    public static void main(String [] args){
        try{
            //Databasa Connection
            //Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            Class.forName("com.hxtt.sql.access.AccessDriver");
            //String url="jdbc:odbc:Access2000";
            String url ="jdbc:Access:///d:/MYDB.mdb";
            Connection connection=DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            String sql="SELECT * FROM table1";
            ResultSet rs = statement.executeQuery(sql);
            //String tt;
            //FileStearm IO
            String path = "F:\\DataBase.txt";
            File file = new File(path);
            while (rs.next()) {
                if (!file.exists()){
                    file.createNewFile();
                    PrintWriter pw = new PrintWriter(file);
                    pw.print(rs);
                    pw.println();
                    pw.flush();
                    pw.close();
                }else{
                    FileWriter fw = new FileWriter(file);
                    //fw.write(rs);
                    //fw.write();
                    fw.flush();
                    fw.close();
                }
            }
            Upload.upload("123",path);
            rs.close();
            connection.close();
        }
        catch(Exception ex){
            System.out.println(ex);
            System.exit(0);
        }
    }
}


