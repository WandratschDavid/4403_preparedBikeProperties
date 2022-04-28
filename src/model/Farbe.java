package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

/**
 * Enumeration Farbe
 */
public enum Farbe
{
	rot, gruen, gelb, blau;

	/**
	 * Ermöglicht das Einfügen der Enum-Werten in eine Dropdownlist, etc.
	 * @return
	 */
	public static ObservableList<Farbe> valuesAsObservableList()
	{
		return FXCollections.observableList(Arrays.asList(values()));
	}
}