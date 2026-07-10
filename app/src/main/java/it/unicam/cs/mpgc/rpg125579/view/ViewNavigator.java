package it.unicam.cs.mpgc.rpg125579.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;
import java.io.IOException;
import java.util.Objects;

/**
 * Centralizza la navigazione tra le schermate JavaFX. Ogni view è un nodo radice
 * separato (VBox/HBox) caricato da FXML: questa classe si occupa di caricare
 * il nuovo FXML e sostituire la scena sull'unico Stage dell'applicazione,
 * restituendo il controller appena creato per permettere l'inizializzazione
 * dei suoi dati (es. passare l'eroe selezionato alla MainView).
 */
public final class ViewNavigator {

    @Setter
    private static Stage primaryStage;

    private ViewNavigator() {
    }

    /**
     * Carica il FXML indicato e lo mostra sullo Stage principale.
     *
     * @param fxmlPath Percorso del FXML (es. "/fxml/MainView.fxml")
     * @param title    Titolo da assegnare alla finestra
     * @return Il controller associato al FXML appena caricato
     */
    public static <T> T switchTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(ViewNavigator.class.getResource(fxmlPath))
            );
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title);
            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Impossibile caricare " + fxmlPath, e);
        }
    }
}
