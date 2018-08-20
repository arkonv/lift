package ru.smartsarov.lift.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.CaseFormat;

public class QueryHelper {
    //static DateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd");
	static DateTimeFormatter FORMAT_TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

    public static List<Map<String, Object>> select(Statement stmt, String query) throws SQLException, ParseException {
        ResultSet resultSet = stmt.executeQuery(query);
        List<Map<String, Object>> records = mapRecords(resultSet);

        resultSet.close();

        return records;
    }

    public static List<Map<String, Object>> mapRecords(ResultSet resultSet) throws SQLException, ParseException {
        List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
        ResultSetMetaData metaData = resultSet.getMetaData();

        while (resultSet.next()) {
            records.add(mapRecord(resultSet, metaData));
        }

        return records;
    }

    public static Map<String, Object> mapRecord(ResultSet resultSet, ResultSetMetaData metaData) throws SQLException, ParseException {
        Map<String, Object> record = new LinkedHashMap<String, Object>();
        
        for (int c = 1; c <= metaData.getColumnCount(); c++) {
            String columnType = metaData.getColumnTypeName(c);
            String columnName = formatPropertyName(metaData.getColumnLabel(c));
            Object value = resultSet.getObject(c);

            if (columnType.equals("TIMESTAMP")) {
            	LocalDateTime dt = LocalDateTime.parse(String.valueOf(value), FORMAT_TIMESTAMP);
            	long timeStamp = dt.atZone(ZoneId.systemDefault()).toEpochSecond();
            	
                //value = DATE_FORMAT.format(value);
            	value = Long.valueOf(timeStamp);
            }

            record.put(columnName, value);
        }

        return record;
    }

    private static String formatPropertyName(String property) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, property);
    }
}