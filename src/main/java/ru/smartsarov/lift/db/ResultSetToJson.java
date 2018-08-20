package ru.smartsarov.lift.db;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

public class ResultSetToJson {
    public static final Type RESULT_TYPE = new TypeToken<List<Map<String, Object>>>() {
        @SuppressWarnings("unused")
		private static final long serialVersionUID = -3467016635635320150L;
    }.getType();

    public static void queryToJson(Writer writer, /*String connectionProperties,*/ String query, String indent, boolean closeWriter) {
        Connection conn = null;
        Statement stmt = null;
        GsonBuilder gson = new GsonBuilder();
        JsonWriter jsonWriter = new JsonWriter(writer);

        if (indent != null)
        	jsonWriter.setIndent(indent);

        try {
            //Properties props = readConnectionInfo(connectionProperties);
            //Class.forName(props.getProperty("driver"));
            //conn = openConnection(props);
        	
        	conn = new DataSource().getConnection();
            stmt = conn.createStatement();

            gson.create().toJson(QueryHelper.select(stmt, query), RESULT_TYPE, jsonWriter);

            if (closeWriter)
            	jsonWriter.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (stmt != null)
                	stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (closeWriter && jsonWriter != null)
                	jsonWriter.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

//    private static Properties readConnectionInfo(String resource) throws IOException {
//        Properties properties = new Properties();
//        InputStream in = ResultSetConverter.class.getResourceAsStream(resource);
//        properties.load(in);
//        in.close();
//
//        return properties;
//    }

//    private static Connection openConnection(Properties connectionProperties) throws IOException, SQLException {
//        String database = connectionProperties.getProperty("database");
//        String username = connectionProperties.getProperty("username");
//        String password = connectionProperties.getProperty("password");
//
//        return DriverManager.getConnection(database, username, password);
//    }
}