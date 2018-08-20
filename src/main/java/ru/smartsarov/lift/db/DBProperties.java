package ru.smartsarov.lift.db;

import java.io.IOException;
import java.util.Properties;

public final class DBProperties {
	private DBProperties() {
	}

	public static Properties get() {
		Properties props = new Properties();

		try {
			props.load(DBProperties.class.getResourceAsStream("db.properties"));
			
			// TODO доступ к application.properties через ClassLoader
			//props.load(DBProperties.class.getClassLoader().getSystemResourceAsStream("application.properties"));
		} catch (IOException ex) {
			ex.printStackTrace(); // handle an exception here
		}

		return props;
	}
}