package model;

import database.Database;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Model-Klasse Bike
 */
public class Bike
{
	private final StringProperty rahmennr = new SimpleStringProperty();
	private final StringProperty markeType = new SimpleStringProperty();
	private final StringProperty text = new SimpleStringProperty();

	/**
	 * Konstruktor aus Rahmennummer
	 *
	 * @param rahmennr Rahmennummer
	 */
	public Bike(String rahmennr)
	{
		setRahmennr(rahmennr);
	}

	/**
	 * Konstruktor mit allen Attributen.
	 *
	 * @param rahmennr  Rahmennummer
	 * @param markeType Marke und Type
	 * @param text      Text
	 */
	public Bike(String rahmennr, String markeType, String text)
	{
		setRahmennr(rahmennr);
		setMarkeType(markeType);
		setText(text);
	}

	/**
	 * Selektion eines Bike aus der Datenbank.
	 *
	 * @param rahmennr Rahmennummer
	 *
	 * @return Bike
	 *
	 * @throws SQLException
	 * @throws BikeException
	 */
	public static Bike select(String rahmennr) throws SQLException
	{
		String sql = " select rahmennr" +
				"          ,      markeType" +
				"          ,      text " +
				"          from bike " +
				"          where rahmennr = '" + rahmennr + "'";

		ResultSet resultSet = Database.getInstance().getStatement().executeQuery(sql);

		Bike bike;
		if (resultSet.next())
		{
			bike = new Bike(resultSet.getString("rahmennr"),
			                resultSet.getString("markeType"),
			                resultSet.getString("text"));
		}
		else
		{
			bike = new Bike(rahmennr);
		}

		return bike;
	}

	public String getRahmennr()
	{
		return rahmennr.get();
	}

	public void setRahmennr(String rahmennr)
	{
		this.rahmennr.set(rahmennr);
	}

	public StringProperty rahmennrProperty()
	{
		return rahmennr;
	}

	public String getMarkeType()
	{
		return markeType.get();
	}

	public void setMarkeType(String markeType)
	{
		this.markeType.set(markeType);
	}

	public StringProperty markeTypeProperty()
	{
		return markeType;
	}

	public String getText()
	{
		return text.get();
	}

	public void setText(String text)
	{
		this.text.set(text);
	}

	public StringProperty textProperty()
	{
		return text;
	}

	/**
	 * Defaulting und Überprüfung. Wird vor jedem Schreiben auf die Datenbank aufgerufen.
	 *
	 * @throws BikeException
	 */
	private void fillAndKill() throws BikeException
	{
		if (rahmennr.get() == null)
		{
			throw new BikeException("Rahmennummer muss angegeben werden!");
		}

		if (rahmennr.get().length() < 5)
		{
			throw new BikeException("Rahmennummer muss zumindest 5 Stellen haben!");
		}

		if (markeType.get() == null)
		{
			throw new BikeException("Marke und Type muss angegeben werden!");
		}

		if (markeType.get().length() < 3)
		{
			throw new BikeException("Marke und Type muss zumindest 3 Stellen haben!");
		}

		if (text.get() == null || text.get().length() == 0)
		{
			throw new BikeException("Text muss angegeben werden!");
		}
	}

	/**
	 * Bike in Datenbank speichern.
	 * <p>
	 * Zunächst wird versucht das Bike einzufügen. Wenn dies wegen einer Primärschlüssel-Violation schief geht, wird
	 * versucht es abzuändern. (insert-default-update)
	 * @throws BikeException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void save() throws BikeException, SQLException, IOException
	{
		fillAndKill();

		try
		{
			// Insert versuchen
			String sql
					= "  insert "
					+ " into Bike (rahmennr "
					+ "             ,markeType "
					+ "             ,Text "
					+ "             ) "
					+ " values ( '" + getRahmennr() + "' "
					+ "        , '" + getMarkeType() + "' "
					+ "        , '" + getText() + "' "
					+ "        )";

			Database.getInstance().getStatement().executeUpdate(sql);
		}
		catch (SQLException e)
		{
			// Primary Key Violation
			if (e.getSQLState().equals("23505"))
			{
				// Update versuchen
				String sql = "update Bike " +
						" set markeType  = '" + getMarkeType() + "'" +
						" ,   text       = '" + getText() + "'" +
						" where rahmennr = '" + getRahmennr() + "'";

				Database.getInstance().getStatement().execute(sql);
			}
			else
			{
				throw e;
			}
		}
	}

	@Override
	public String toString()
	{
		return "Bike{" +
				"rahmennr=" + rahmennr +
				", markeType=" + markeType +
				", text=" + text +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		Bike bike = (Bike) o;

		return getRahmennr().equals(bike.getRahmennr());
	}
}