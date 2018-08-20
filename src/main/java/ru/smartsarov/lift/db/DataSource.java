package ru.smartsarov.lift.db;

import java.sql.Connection;
import java.util.Properties;

public class DataSource {
	private static final String DRIVER_DEFAULT = "org.firebirdsql.jdbc.FBDriver";
	private static final String URL_DEFAULT = "jdbc:firebirdsql://localhost:3050/C:/CDP/DataBase/cdpEsdkl.GDB?lc_ctype=WIN1251";
	private static final String USER_DEFAULT = "sysdba";
	private static final String PASSWORD_DEFAULT = "masterkey";

	private static final int REGISTER_CLASS_FOR_NAME = 1;
	private static final int REGISTER_PROPERTIES = 2;
	private static final int REGISTER_JDBC4 = 3;

	private static final int CONNECT_DRIVERMANAGER = 1;
	private static final int CONNECT_DRIVER = 2;

	/**
	 * Make a connection to an cdpEsdkl.GDB on your local machine, and demonstrate basic JDBC features.
	 * <p>
	 * On the commandline a JDBC-url, username and password can be passed, otherwise defaults are used.
	 */
	public Connection getConnection() throws Exception {
		Properties props = DBProperties.get();
		String databaseURL = props.getProperty("db.databaseURL", URL_DEFAULT);
		String user = props.getProperty("db.user", USER_DEFAULT);
		String password = PASSWORD_DEFAULT;
		
//		String databaseURL = URL_DEFAULT;
//		String user = USER_DEFAULT;
//		String password = PASSWORD_DEFAULT;
		
//		String databaseURL = args.length == 0 ? URL_DEFAULT : args[0];
//		String user = args.length < 2 ? USER_DEFAULT : args[1];
//		String password = args.length < 3 ? PASSWORD_DEFAULT : args[2];

		/*
		 * Here are the JDBC objects we're going to work with. We're defining them
		 * outside the scope of the try block because they need to be visible in a
		 * finally clause which will be used to close everything when we are done.
		 */
		java.sql.Driver driver = null;
		java.sql.Connection con = null;

		//try {

			/*
			 * Before a JDBC driver can be used, it must have been registered with the
			 * DriverManager.
			 * 
			 * Demonstrate the different methods to register the Firebird JCA-JDBC driver
			 * with the driver manager
			 */
			int registrationAlternative = REGISTER_CLASS_FOR_NAME;
			switch (registrationAlternative) {

			case REGISTER_CLASS_FOR_NAME:
				/*
				 * For JDBC 3.0 and earlier, the standard method of registering the driver is by
				 * loading the class.
				 * 
				 * Class.forName() instructs the java class loader to load and initialize a
				 * class. As part of the class initialization any static clauses associated with
				 * the class are executed.
				 * 
				 * Every driver class is required by the jdbc specification to create an
				 * instance of itself and register that instance with the DriverManager when the
				 * driver class is loaded by the java classloader (this is done via a static
				 * clause associated with the driver class).
				 * 
				 * Notice that the driver name could have been supplied dynamically, so that an
				 * application is not hardwired to any particular driver as would be the case if
				 * a driver constructor were used, eg. new org.firebirdsql.jdbc.FBDriver().
				 */
				try {
					//Class.forName(DRIVER_DEFAULT);
					Class.forName(props.getProperty("db.driver", DRIVER_DEFAULT));
				} catch (java.lang.ClassNotFoundException e) {
					// A call to Class.forName() forces us to consider this exception
					System.out.println("Firebird JCA-JDBC driver not found in class path");
					System.out.println(e.getMessage());
					return null;
				}
				break;

			case REGISTER_PROPERTIES:
				/*
				 * Add the Firebird JCA-JDBC driver name to your system's jdbc.drivers property list.
				 * 
				 * The driver manager will load drivers from this system property list.
				 */
				java.util.Properties sysProps = System.getProperties();
				StringBuffer drivers = new StringBuffer("org.firebirdsql.jdbc.FBDriver");
				String oldDrivers = sysProps.getProperty("jdbc.drivers");
				if (oldDrivers != null)
					drivers.append(":" + oldDrivers);
				sysProps.put("jdbc.drivers", drivers.toString());
				System.setProperties(sysProps);
				break;

			case REGISTER_JDBC4:
				/*
				 * From JDBC 4.0 (Java 6), drivers are required to have a file
				 * /META-INF/services/java.sql.Driver with the classname(s) of the drivers.
				 * 
				 * The DriverManager will automatically load all drivers, so there is no need to
				 * explicitly load the driver.
				 */
				break;
			}

			/*
			 * At this point the driver should be registered with the DriverManager. Try to
			 * find the registered driver that recognizes Firebird URLs
			 */
			try {
				// We pass the entire database URL, but we could just pass "jdbc:firebirdsql:"
				driver = java.sql.DriverManager.getDriver(databaseURL);
				System.out.println("Firebird JCA-JDBC driver version " + driver.getMajorVersion() + "."
						+ driver.getMinorVersion() + " registered with driver manager.");
			} catch (java.sql.SQLException e) {
				System.out.println("Unable to find Firebird JCA-JDBC driver among the registered drivers.");
				showSQLException(e);
				return null;
			}

			/*
			 * Now that Firebird JCA-JDBC driver is registered with the DriverManager, try
			 * to get a connection to an employee.fdb database on this local machine using
			 * one of two alternatives for obtaining connections
			 */
			int connectionAlternative = CONNECT_DRIVERMANAGER;
			switch (connectionAlternative) {

			case CONNECT_DRIVERMANAGER:
				/*
				 * This alternative is driver independent; the DriverManager will find the right
				 * driver for you based on the jdbc subprotocol.
				 */
				try {
					con = java.sql.DriverManager.getConnection(databaseURL, user, password);
					System.out.println("Connection established with databaseURL = " + databaseURL);
				} catch (java.sql.SQLException e) {
					e.printStackTrace();
					System.out.println("Unable to establish a connection through the driver manager.");
					showSQLException(e);
					return null;
				}
				break;

			case CONNECT_DRIVER:
				/*
				 * If you're working with a particular driver, which may or may not be
				 * registered, you can get a connection directly from it, bypassing the
				 * DriverManager
				 */
				try {
					java.util.Properties connectionProperties = new java.util.Properties();
					connectionProperties.put("user", user);
					connectionProperties.put("password", password);
					connectionProperties.put("lc_ctype", "WIN1251");
					con = driver.connect(databaseURL, connectionProperties);
					System.out.println("Connection established.");
				} catch (java.sql.SQLException e) {
					e.printStackTrace();
					System.out.println("Unable to establish a connection through the driver.");
					showSQLException(e);
					return null;
				}
				break;
			}

			// Disable the default autocommit so we can undo our changes later
			try {
				con.setAutoCommit(false);
				System.out.println("Auto-commit is disabled.");
			} catch (java.sql.SQLException e) {
				System.out.println("Unable to disable autocommit.");
				showSQLException(e);
				return null;
			}
			
			return con;
		/*}	finally {
			System.out.println("Closing database resources and rolling back any changes we made to the database.");

			// Now that we're all finished, let's release database resources.
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (java.sql.SQLException e) {
				showSQLException(e);
			}

			// Before we close the connection, let's rollback any changes we may have made.
			try {
				if (con != null)
					con.rollback();
			} catch (java.sql.SQLException e) {
				showSQLException(e);
			}
			try {
				if (con != null)
					con.close();
			} catch (java.sql.SQLException e) {
				showSQLException(e);
			}
		}*/
	}

//			// Now that we have a connection, let's try to get some meta data...
//			try {
//				java.sql.DatabaseMetaData dbMetaData = con.getMetaData();
//
//				// Ok, let's query a driver/database capability
//				if (dbMetaData.supportsTransactions())
//					System.out.println("Transactions are supported.");
//				else
//					System.out.println("Transactions are not supported.");
//
//				// What are the views defined on this database?
//				java.sql.ResultSet tables = dbMetaData.getTables(null, null, "%", new String[] { "VIEW" });
//				while (tables.next()) {
//					System.out.println(tables.getString("TABLE_NAME") + " is a view.");
//				}
//				tables.close();
//			} catch (java.sql.SQLException e) {
//				System.out.println("Unable to extract database meta data.");
//				showSQLException(e);
//			}
//
//			/*
//			 * The query above could just as easily have been dynamic SQL, eg. if the SQL
//			 * had been entered as user input. As a dynamic query, we'd need to query the
//			 * result set meta data for information about the result set's columns.
//			 */
//			try {
//				java.sql.ResultSetMetaData rsMetaData = rs.getMetaData();
//				System.out.println("The query executed has " + rsMetaData.getColumnCount() + " result columns.");
//				System.out.println("Here are the columns: ");
//				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
//					System.out.println(rsMetaData.getColumnName(i) + " of type " + rsMetaData.getColumnTypeName(i));
//				}
//			} catch (java.sql.SQLException e) {
//				System.out.println("Unable to extract result set meta data.");
//				showSQLException(e);
//			}
//
//			// Ok, lets step thru the results of the query...
//			try {
//				System.out.println("Here are the employee's whose salary < $50,000");
//				while (rs.next()) {
//					System.out.println(rs.getString("full_name"));
//				}
//			} catch (java.sql.SQLException e) {
//				System.out.println("Unable to step thru results of query");
//				showSQLException(e);
//				return;
//			}
//		} finally {
//			System.out.println("Closing database resources and rolling back any changes we made to the database.");
//
//			// Now that we're all finished, let's release database resources.
//			try {
//				if (rs != null)
//					rs.close();
//			} catch (java.sql.SQLException e) {
//				showSQLException(e);
//			}
//
//			try {
//				if (stmt != null)
//					stmt.close();
//			} catch (java.sql.SQLException e) {
//				showSQLException(e);
//			}
//
//			// Before we close the connection, let's rollback any changes we may have made.
//			try {
//				if (con != null)
//					con.rollback();
//			} catch (java.sql.SQLException e) {
//				showSQLException(e);
//			}
//			try {
//				if (con != null)
//					con.close();
//			} catch (java.sql.SQLException e) {
//				showSQLException(e);
//			}
//		}
//	}

	/**
	 * Display an SQLException which has occurred in this application.
	 * 
	 * @param e SQLException
	 */
	private void showSQLException(java.sql.SQLException e) {
		/*
		 * Notice that a SQLException is actually a chain of SQLExceptions, let's not
		 * forget to print all of them
		 */
		java.sql.SQLException next = e;
		while (next != null) {
			System.out.println(next.getMessage());
			System.out.println("Error Code: " + next.getErrorCode());
			System.out.println("SQL State: " + next.getSQLState());
			next = next.getNextException();
		}
	}
}