import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author AERO
 */
public class FirstApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        URL url = new File("src/main/java/view/Main.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        
        primaryStage.centerOnScreen();
        primaryStage.setScene(scene);
        //primaryStage.setTitle("Hello World!");
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
