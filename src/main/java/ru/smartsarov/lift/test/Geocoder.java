package ru.smartsarov.lift.test;

import static ru.smartsarov.lift.Constants.YANDEX_GEOCODER_URL;
import static ru.smartsarov.lift.Constants.JSON_INDENT;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jsoup.Jsoup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import ru.smartsarov.lift.db.DataSource;
import ru.smartsarov.lift.db.Queries;
import ru.smartsarov.lift.test.geocoder.GeoAddress;
import ru.smartsarov.lift.test.geocoder.YaGeocoderResponse;

@Path("/")
public class Geocoder {
	public static final Type RESULT_TYPE = new TypeToken<Map<String, Object>>() {
		@SuppressWarnings("unused")
		private static final long serialVersionUID = -3467016635635320150L;
    }.getType();
    
	
    @GET
    @Path("/geo")
    @Produces(MediaType.APPLICATION_JSON)
    public static String geocode(@DefaultValue("false") @QueryParam("todb") Boolean toDb, @DefaultValue("false") @QueryParam("tofile") Boolean toFile) throws Exception
    {
    	String query =
    			" select " +
    			"	idlift, " +
    			"	v_lifts.street || ',' || v_lifts.house || ',' || v_lifts.pod disp_address, " +
    			"	regions.nameregion || ',' || v_lifts.town || ',' || + v_lifts.street || ',' || v_lifts.house || ',' || v_lifts.pod address " +
    			" from v_lifts " +
    			" join disps on disps.iddisp = v_lifts.iddisp " +
    			" join regions on regions.idregion = disps.IDREG " +
    			" order by idlift";
		
		Connection conn = new DataSource().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        Gson gson = new GsonBuilder().create();
        List<String> queries = new ArrayList<>();
        
        while (rs.next()) {
        	GeoAddress geoAddress = gson.fromJson(geocoder(rs.getInt("idlift")), GeoAddress.class);
        	queries.add("insert into lifts_geo(idlift, address, fulladdress, lat, lng) values(" + rs.getString("idlift") + ",'" + rs.getString("disp_address") + "','" + geoAddress.fullAddress + "'," + geoAddress.lat + "," + geoAddress.lng + ");"/* + System.lineSeparator()*/);
        	if (toDb) {
        		conn.createStatement().execute(queries.get(queries.size() - 1));
        	}
        }
        
        if (stmt != null)
        	stmt.close();
        
        conn.commit();
        if (toFile) {
        	java.nio.file.Path file = Paths.get("C:/esd.txt");
        	Files.write(file, queries, Charset.forName("UTF-8"));
        }
        
        /*String listString = String.join(System.lineSeparator(), queries);
        if (toDb) {
        	stmt.addBatch(String.join(System.lineSeparator(), queries));
        	stmt.executeBatch();
        }*/

        return "OK";
    }
    
    // TODO создать отдельную ф. geocoder, принимающую адрес, возвращающую класс GeoAddress. С приёмом массива адресов через POST
    
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 * 
	 * <p>Для создания java класса из json необходимо использовать
	 * онлайн-сервис http://www.jsonschema2pojo.org
	 */
	@GET
    @Path("/geo/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static String geocoder(@PathParam("id") int id) throws Exception
    {
		String url = YANDEX_GEOCODER_URL + Queries.getAddress(id);
		String resp = Jsoup.connect(url).ignoreContentType(true).get().body().text();

		Gson gson = new GsonBuilder().create();
		YaGeocoderResponse yandexGeo = gson.fromJson(resp, YaGeocoderResponse.class);
		
		//String pos = geo.response.geoObjectCollection.featureMember.get(0).geoObject.point.pos;
		String[] pos = yandexGeo.response.geoObjectCollection.featureMember.get(0).geoObject.point.pos.split(" ");
		String fullAddress = yandexGeo.response.geoObjectCollection.featureMember.get(0).geoObject.metaDataProperty.geocoderMetaData.text;
	
		// Создание объекта для конвертирования в json в общем виде через Map<String, Object>.
		/*Map<String, Object> record = new LinkedHashMap<String, Object>();
		record.put("fullAddress", fullAddress);
		record.put("lat", new BigDecimal(pos[1]));
		record.put("lng", new BigDecimal(pos[0]));
		gson.toJson(record, RESULT_TYPE, jsonWriter);*/
		//gson.toJson(record, GeoAddress.class, jsonWriter);
		
		StringWriter strOut = new StringWriter();
		JsonWriter jsonWriter = new JsonWriter(strOut);
		jsonWriter.setIndent(JSON_INDENT);
		
		gson.toJson(new GeoAddress(fullAddress, new BigDecimal(pos[1]), new BigDecimal(pos[0])), GeoAddress.class, jsonWriter);
		
		/*List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
		records.add(record);
		gson.toJson(records, ResultSetToJson.RESULT_TYPE, jsonWriter);*/
		
    	return strOut.toString();
    }
}