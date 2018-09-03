package ru.smartsarov.lift.db;

import static ru.smartsarov.lift.Constants.JSON_INDENT;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Queries {
	
	public static String sql1() {
		return execQuery(DBProperties.get().getProperty("db.sql1"), true);
	}
	
	public static String getAddress(int idLift) throws Exception {
		String query =
    			" select " +
    			"	regions.nameregion || ',' || v_lifts.town || ',' || + v_lifts.street || ',' || v_lifts.house || ',' || v_lifts.pod address " +
    			" from v_lifts " +
    			" join disps on disps.iddisp = v_lifts.iddisp " +
    			" join regions on regions.idregion = disps.IDREG " +
    			" where idlift=" + String.valueOf(idLift) +
    			" order by idlift";
		
		Connection conn = new DataSource().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        
		return rs.getString(1);
	}
	
	public static String getAddresses() {
		String query = "select street, house, pod entrance from v_lifts";
		return execQuery(query, true);
	}
	
	public static String getLifts() {
		String query =
			" select " +
				" v_lifts.idlift id, " +
				" v_lifts.name, " +
				" schemes.namesch scheme_name, " +
				" lifts_geo.address, " +
				" lifts_geo.fulladdress full_address, " +
				" lifts_geo.lat, " +
				" lifts_geo.lng " +
			" from v_lifts " +
			" join schemes on v_lifts.idscheme = schemes.numsch " +
			" join lifts_geo on v_lifts.idlift = lifts_geo.idlift " +
			" order by v_lifts.idlift";
		return execQuery(query, true);
	}
	
	public static String getLiftsStates() {
		/*String condition0 = "states.errorcode = 0 and (states.errortext is null or states.errortext = '')";
		String condition1 = "states.errorcode > 0 and states.errortext is not null and states.errortext <> ''";
		String condition2 = "(states.errorcode = 0 and states.errortext like 'Отсутствует связь с БЛ%') or (states.errorcode = -255 and states.errortext = '')";	// states.errorcode = -255 - "состояние не опрошено" или "не определено"
		String query =
			" select " +
			"   states.idlift id, " +
			"   states.datestate \"timestamp\", " +
			"   states.errorcode error_code, " +
			"   states.errortext error_description, " +
			"   case " +
			"     when " + condition0 + " then 0 " +
			"     when " + condition1 + " then 1 " +
			"     when " + condition2 + " then 2 " +
			"     else 3 " +
			"   end state, " +
			"   case " +
			"     when " + condition0 + " then 'green' " +
			"     when " + condition1 + " then 'red' " +
			"     when " + condition2 + " then 'yellow' " +
			"     else 'grey' " +
			"   end color " +
			" from states " +
			" join v_lifts on states.idlift = v_lifts.idlift " +
			" order by states.idlift";*/
		
		String query =
				" select " +
				"   states.idlift id, " +
				"   states.datestate \"timestamp\", " +
				"   states.errorcode error_code, " +
				"   states.errortext error_description, " +
				"   states.state_id state, " +
				"   case states.state_id " +
				"     when 0 then 'green' " +
				"     when 1 then 'red' " +
				"     when 2 then 'yellow' " +
				"     else 'grey' " +
				"   end color " +
				" from states " +
				" join v_lifts on states.idlift = v_lifts.idlift " +
				" order by states.idlift";

		return execQuery(query, true);
	}
	
	public static String getLiftsErrorsStates() {
		String query =
			" select " + 
			" v_lifts.idlift id, states.datestate \"timestamp\", states.errorcode, states.errortext " + 
			" from states " + 
			" join v_lifts on v_lifts.idlift = states.idlift " + 
			" where states.state_id = 1 " + 
			" order by v_lifts.idlift";
		return execQuery(query, true);
	}
	
	public static String getLiftsProblemsStates() {
		String query =
			" select " + 
			" v_lifts.idlift id, states.datestate \"timestamp\", states.errorcode, states.errortext " + 
			" from states " + 
			" join v_lifts on v_lifts.idlift = states.idlift " + 
			" where states.state_id in (1,2,3) " + 
			" order by v_lifts.idlift";
		return execQuery(query, true);
	}
	
	public static String getLiftsEvents() {
		String query =
			"select nameevent event_name, count(*) " +
			"from journal " +
			"where " +
			"	nameevent is not null and nameevent not in ('','Снятие события: ') and nameevent not like 'Восстановление связи с БЛ%' and nameevent not like 'Отсутствие связи с БЛ%' " +
			"	and idlift <> -1 " +
			"group by nameevent " +
			"order by count(*) desc ";
		return execQuery(query, true);
	}
	
	public static String getLiftEvents(int id) {
		String query =
			" select nameevent event_name, dtevent event_datetime " +
			" from journal " +
			" where " +
			"	idlift = " + String.valueOf(id) +
			" order by dtevent desc ";
		return execQuery(query, true);
	}
	
	public static String getLiftLastEvent(int id, Integer count) {
		String query =
			" select first " + (count == null ? "1" : String.valueOf(count)) + " nameevent event_name, dtevent event_datetime " +
			" from journal " +
			" where " +
			"	idlift = " + String.valueOf(id) +
			" order by dtevent desc ";
		return execQuery(query, true);
	}
	
	public static String getLiftsStatesAlert() {
		String query =
			" select idlift, datestate, errorcode, errortext " +
			" from states_alert " +
			" where " +
			"	states_alert.removed = 0 " +
			" order by idlift";
		return execQuery(query, true);
	}
	
	private static String execQuery(String query, boolean closeWriter) {
		StringWriter strOut = new StringWriter();
		String indent = JSON_INDENT;
		//PrintWriter writer = new PrintWriter(System.out);
	    //String dbProps = "/database.properties";

	    ResultSetToJson.queryToJson(strOut, /*dbProps,*/ query, indent, closeWriter);
	    
        return strOut.toString();
	    //writer.println("\n\nIntermediate SELECT:");
	    //ResultSetConverter.queryToJson(writer, dbProps, "SELECT first_name, last_name, getAge(date_of_birth) as age FROM Beatles", indent, true);
	}
}