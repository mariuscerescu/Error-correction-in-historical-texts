import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("Cerescu Marius | IA - 212 | Teaza de an | Corectarea erorilor din texte (sec 18)");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
