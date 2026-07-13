package it.unicam.cs.mpgc.rpg125579.utility;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Window;

import java.util.Objects;

/**
 * Centralizza la creazione di {@link Alert} già stilizzati con il foglio CSS
 * dell'applicazione, evitando di ripetere in ogni view il boilerplate di
 * impostazione di owner e stylesheet.
 */
public final class AlertFactory {

    private static final String STYLESHEET_PATH = "/styles/app.css";
    private static final double CONTENT_WIDTH = 400.0;
    private static final double DIALOG_WIDTH = 440.0;

    private AlertFactory() {
        // Costruttore privato per nascondere quello pubblico di default
    }

    public static void mostraInfo(Window owner, String messaggio) {
        mostra(Alert.AlertType.INFORMATION, owner, null, null, messaggio);
    }

    public static void mostraAvviso(Window owner, String titolo, String messaggio) {
        mostra(Alert.AlertType.WARNING, owner, titolo, null, messaggio);
    }

    public static void mostraInfoConTitolo(Window owner, String titolo, String headerText, String messaggio) {
        mostra(Alert.AlertType.INFORMATION, owner, titolo, headerText, messaggio);
    }

    public static void mostraInfoConTestoScorrevole(Window owner, String title, String header, String content) {
        Alert alert = creaAlertBase(Alert.AlertType.INFORMATION, owner, title, header);

        // Creiamo una TextArea per il contenuto lungo
        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        // Adattamento della TextArea alla finestra
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        alert.getDialogPane().setContent(expContent);
        alert.getDialogPane().setPrefSize(400, 300);

        alert.showAndWait();
    }

    private static void mostra(Alert.AlertType tipo, Window owner, String titolo, String headerText, String messaggio) {
        Alert alert = creaAlertBase(tipo, owner, titolo, headerText);

        Label contentLabel = new Label(messaggio);
        contentLabel.setWrapText(true);
        contentLabel.setPrefWidth(CONTENT_WIDTH);
        contentLabel.getStyleClass().add("label");

        alert.getDialogPane().setContent(contentLabel);
        alert.getDialogPane().setPrefWidth(DIALOG_WIDTH);

        alert.showAndWait();
    }

    /**
     * Metodo helper privato che centralizza l'inizializzazione comune di tutti gli Alert
     * (owner, foglio di stile, titolo, header, padding e ridimensionamento).
     */
    private static Alert creaAlertBase(Alert.AlertType tipo, Window owner, String titolo, String headerText) {
        Alert alert = new Alert(tipo);

        if (owner != null) {
            alert.initOwner(owner);
        }

        alert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(AlertFactory.class.getResource(STYLESHEET_PATH)).toExternalForm()
        );

        if (titolo != null) {
            alert.setTitle(titolo);
        }

        alert.setHeaderText(headerText);
        alert.getDialogPane().setPadding(new Insets(15));
        alert.setResizable(true);

        return alert;
    }
}