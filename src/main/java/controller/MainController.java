package controller;

import dao.ImageDAO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author AERO
 */
public class MainController implements Initializable {

    @FXML
    private VBox imageContainer;
    @FXML
    private TabPane tabPaneMain;
    @FXML
    private Tab tab1;
    @FXML
    private Tab tab2;
    @FXML
    private Tab tab3;
    @FXML
    private ImageView spriteImageView;
    
    // Dimensions of a single frame
    // Atur sesuai panjang dan lebar karakter
    private static final int FRAME_WIDTH = 55;  // Adjust as per your sprite size
    private static final int FRAME_HEIGHT = 80; // Adjust as per your sprite size

    // Starting position of the desired character (e.g., karakter baris ke-5 )
    private static final int CHARACTER_START_X = 0; // X position of the first frame
    private static final int CHARACTER_START_Y = 350; // baris ke 5 berada di koordinat 350

    private static final int FRAME_COUNT = 8; // Number of walking frames, sesuaikan dengan jumlah gerakan
    private static final int ANIMATION_DURATION = 150; // Duration per frame in milliseconds, semakin lama semakin lambat
    private static final double MOVEMENT_SPEED = 2; // Untuk mengatur kecepatan maju
    @FXML
    private ImageView imagePreview;
    @FXML
    private Button chooseImageButton;

    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.playSprite();
        this.moveHorizontal();
        this.loadImages();
    }    

    // The character walk on the spot
    private void playSprite()
    {
        spriteImageView.setFitWidth(FRAME_WIDTH);  // Fixed display size
        spriteImageView.setFitHeight(FRAME_HEIGHT); // Fixed display size
        spriteImageView.setX(0);  // Keep the character in a fixed X coordinate
        spriteImageView.setY(0);  // Keep the character in a fixed Y coordinate

        // Create a Timeline for animating the frames
        Timeline timeline = new Timeline();
        for (int i = 0; i < FRAME_COUNT; i++) {
            int frameX = CHARACTER_START_X + i * (FRAME_WIDTH + 12); // Add gap offset if necessary
            KeyFrame keyFrame = new KeyFrame(
                Duration.millis(i * ANIMATION_DURATION),
                e -> spriteImageView.setViewport(new Rectangle2D(frameX, CHARACTER_START_Y, FRAME_WIDTH, FRAME_HEIGHT))
            );
            timeline.getKeyFrames().add(keyFrame);
        }
         // Set the animation to loop
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    private void moveHorizontal()
    {
        // Gunakan bagian ini jika ingin menggerakan karakter ke kanan
        // Create a Timeline for horizontal movement
        Timeline movementTimeline = new Timeline(new KeyFrame(Duration.millis(ANIMATION_DURATION), e -> {
            // Move the character to the right
            spriteImageView.setX(spriteImageView.getX() + MOVEMENT_SPEED);

            // Reset position when reaching the edge of the screen
            if (spriteImageView.getX() > 400) {
                spriteImageView.setX(-FRAME_WIDTH); // Reset to the left
            }
        }));
        movementTimeline.setCycleCount(Timeline.INDEFINITE);
        movementTimeline.play();
    }

    @FXML
    private void openFileChoose(MouseEvent event) throws IOException, SQLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(chooseImageButton.getScene().getWindow());
        if (selectedFile != null) {
            // Optional: Display the selected image in the ImageView
            Image image = new Image(selectedFile.toURI().toString());
            imagePreview.setImage(image);
            
            ImageDAO.insertEntry(this.readFileToByteArray(selectedFile), selectedFile);
        }
    }
    

    /**
     * Reads a file into a byte array.
     *
     * @param file The file to read.
     * @return A byte array containing the file data.
     * @throws IOException If an I/O error occurs.
     */
    private byte[] readFileToByteArray(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            return data;
        }
    } 
    
    
    // Load image in tab 1
    public void loadImages() {
        try {
            // Clear the VBox to avoid duplicate images
            imageContainer.getChildren().clear();
            ArrayList<byte[]> result = ImageDAO.getImages();
            for (byte[] imageData : result) {
                
                if (imageData != null) {
                    // Convert the byte array to an Image
                    Image image = new Image(new ByteArrayInputStream(imageData));

                    // Create an ImageView for the image
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(100);  // Set image width
                    imageView.setFitHeight(100); // Set image height
                    imageView.setPreserveRatio(true);
                    

                    // Add the ImageView to the FlowPane
                    imageContainer.getChildren().add(imageView);
                    imageContainer.setMargin(imageView, new Insets(20)); // Apply margin to imageView
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
