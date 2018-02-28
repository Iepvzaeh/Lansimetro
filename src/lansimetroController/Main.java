package lansimetroController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;


/**
 *
 * @author Antti Nieminen, Antti Käyhkö, Anssi Chamorro, Heikki Tanttu
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        ScrollPane scrollPane = new ScrollPane();
//        scrollPane.setContent(root);
//         scrollPane.setPrefSize(583.0,700);
//        Scene scene = new Scene(scrollPane);
        
        Scene scene = new Scene(root);
        stage.setTitle("Länsimetro Simulator 3000");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
        
        
        
    }
    
}
