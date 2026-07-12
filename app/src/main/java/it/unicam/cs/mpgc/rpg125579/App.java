package it.unicam.cs.mpgc.rpg125579;

import it.unicam.cs.mpgc.rpg125579.utility.SuperpowerSeeder;
import it.unicam.cs.mpgc.rpg125579.view.ViewNavigator;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entry point dell'applicazione JavaFX. Registra lo Stage principale in
 * {@link ViewNavigator} e carica {@code DashboardView.fxml} come prima schermata.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        SuperpowerSeeder.seedIfEmpty();
        ViewNavigator.setPrimaryStage(stage);
        stage.setFullScreen(true);
        ViewNavigator.switchTo("/DashboardView.fxml", "Superbattles");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
