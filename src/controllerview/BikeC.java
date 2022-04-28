package controllerview;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Bike;

import java.io.IOException;

/**
 * Controller-Klasse Bike für View bikeV.fxml
 */
public class BikeC
{
	@FXML
	private TextField tfRahmennr;
	@FXML
	private TextField tfMarkeType;
	@FXML
	private TextField tfText;
	@FXML
	private Button btCancel;
	@FXML
	private Button btSelect;
	@FXML
	private Button btSave;

	private Bike model;
	private BooleanProperty hideNonkeyProperty = new SimpleBooleanProperty();

	/**
	 * View in gegebener Stage anzeigen
	 *
	 * @param stage gegebene Stage
	 */
	public static void show(Stage stage)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(BikeC.class.getResource("bikeV.fxml"));
			Parent root = loader.load();

			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Bikes");
			stage.show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			Platform.exit();
		}
	}

	/**
	 * Betätigung des Cancel-Buttons
	 *
	 * @param event
	 */
	@FXML
	void btCancelOnAction(ActionEvent event)
	{
		cancel();
	}

	/**
	 * Betätigung des Save-Buttons
	 *
	 * @param event
	 */
	@FXML
	void btSaveOnAction(ActionEvent event)
	{
		save();
	}

	/**
	 * Betätigung des Select-Buttons
	 *
	 * @param event
	 */
	@FXML
	void btSelectOnAction(ActionEvent event)
	{
		select();
	}

	/**
	 * Initialisierung. Wird vom FXML-Loader aufgerufen und dient zum Initialisieren der Controls, dem Einrichten von
	 * Bindings, etc.
	 */
	@FXML
	public void initialize()
	{
		// Controls initialisieren

		// Visibility
		tfRahmennr.disableProperty().bind(hideNonkeyProperty.not());
		tfMarkeType.disableProperty().bind(hideNonkeyProperty);
		tfText.disableProperty().bind(hideNonkeyProperty);

		// Dis/Enable Buttons
		btSelect.disableProperty().bind(hideNonkeyProperty.not());
		btSave.disableProperty().bind(hideNonkeyProperty);

		// Start hidden
		hideNonkeyProperty.set(true);
	}

	/**
	 * Cancel-Button.
	 */
	private void cancel()
	{
		if (model != null)
		{
			tfRahmennr.textProperty().unbindBidirectional(model.rahmennrProperty());
			tfMarkeType.textProperty().unbindBidirectional(model.markeTypeProperty());
			tfText.textProperty().unbindBidirectional(model.textProperty());
		}

		tfRahmennr.setText("");
		tfMarkeType.setText("");
		tfText.setText("");

		hideNonkeyProperty.set(true);
	}

	/**
	 * Select-Button.
	 */
	private void select()
	{
		try
		{
			model = Bike.select(tfRahmennr.getText());

			tfRahmennr.textProperty().bindBidirectional(model.rahmennrProperty());
			tfMarkeType.textProperty().bindBidirectional(model.markeTypeProperty());
			tfText.textProperty().bindBidirectional(model.textProperty());

			hideNonkeyProperty.set(false);
		}
		catch (Exception e)
		{
			error(e.getMessage());
		}
	}

	/**
	 * Save-Button.
	 */
	private void save()
	{
		try
		{
			model.save();
			cancel();
			info("Ok, Bike gesichert!");
		}
		catch (Exception e)
		{
			error(e.getMessage());
		}
	}

	/**
	 * Anzeige einer informellen Nachricht.
	 *
	 * @param msg Nachricht
	 */
	private void info(String msg)
	{
		Alert error = new Alert(Alert.AlertType.INFORMATION, msg);
		error.showAndWait();
	}

	/**
	 * Anzeige eines Fehlers
	 *
	 * @param msg Fehlernachricht
	 */
	private void error(String msg)
	{
		Alert error = new Alert(Alert.AlertType.ERROR, msg);
		error.showAndWait();
	}
}