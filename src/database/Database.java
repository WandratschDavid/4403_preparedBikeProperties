package database;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Singleton zum Zugriff auf eine JDBC-Datenbank. Verbindungsdetails befinden sich in der Datei dbconnect.properties in
 * den Props
 * <ul>
 *   <li>driver</li>
 *   <li>url</li>
 *   <li>username</li>
 *   <li>password</li>
 * </ul>
 */
public class Database
{
	private static Database instance;
	private Connection connection;
	private Statement statement;
	private String driver;
	private String url;
	private String username;
	private String password;

	/**
	 * Privater Konstruktor, da es sich um ein Singleton handelt und die einzige Instanz nur von der Klasse selbst
	 * erstellt und verwaltet wird.
	 */
	private Database()
	{
		// DB-Properties laden
		try (FileInputStream in = new FileInputStream("dbconnect.properties");)
		{
			Properties prop = new Properties();
			prop.load(in);
			driver = prop.getProperty("driver");
			url = prop.getProperty("url");
			username = prop.getProperty("username");
			password = prop.getProperty("password");

			// Alles da?
			if (driver == null || url == null || username == null || password == null)
			{
				throw new Exception("Fehler! Property File muss driver, url, username, password enthalten!");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		// Verbindung erstellen
		try
		{
			connection = DriverManager.getConnection(url, username, password);
			statement = connection.createStatement();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(2);
		}
	}

	/**
	 * Liefert die einzige Instanz.
	 *
	 * @return Instanz
	 */
	public static Database getInstance()
	{
		return instance;
	}

	/**
	 * Öffnen der Datenbank.
	 *
	 * @throws SQLException
	 */
	public static void open() throws SQLException
	{
		instance = new Database();
	}

	/**
	 * Schließen der Datenbank
	 */
	public static void close()
	{
		try
		{
			getInstance().connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(3);
		}
	}

	/**
	 * Getter für Connection.
	 *
	 * @return Connection.
	 */
	public Connection getConnection()
	{
		return connection;
	}

	/**
	 * Getter für dynamisches Datenbank-Statement.
	 *
	 * @return dynamishes DB-Statement.
	 */
	public Statement getStatement()
	{
		return statement;
	}
}