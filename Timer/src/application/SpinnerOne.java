package application;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Date;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.text.NumberFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
;
 
@SuppressWarnings("unused")
public class SpinnerOne extends Application {
	
	private int STARTTIME;
    private Timeline timeline;
    private Label timerLabel = new Label();
    private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME*100);
    private String cboxSelect = "";
    private String alignment = "";
 
    @Override
    public void start(Stage stage) {
    	
    	createAndShowGUI();
    	Label label = new Label("Select Minutes to next Coffee: ");
    	
    	
    	
//        final Spinner<Integer> spinner = new Spinner<Integer>();
//        final int initialValue = 60;
//        // Value factory.
//        SpinnerValueFactory<Integer> valueFactory = 
//                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999, initialValue);
// 
//        spinner.setValueFactory(valueFactory);     
        
        Button okBtn = new Button("OK");
        
        TextField t1 = new TextField();
        
        
        ObservableList<String> boxlist = 
        	FXCollections.observableArrayList(
        			"Non Transparent",
        			"Transparent"
        			);
        	
        ComboBox<String> cbox = new ComboBox<String>();
        cbox.setItems(boxlist);
        cbox.getSelectionModel().selectLast();
        
        ObservableList<String> boxlist2 = 
            	FXCollections.observableArrayList(
            			"Top Left",
            			"Bottom Right"
            			);
            	
        ComboBox<String> cbox2 = new ComboBox<String>();
        cbox2.setItems(boxlist2);
        cbox2.getSelectionModel().selectLast();
            
            
        
        FlowPane root = new FlowPane();
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10));
        
        root.getChildren().addAll(label, okBtn, t1, cbox, cbox2);

        cbox.setVisible(true);
 
        Scene scene = new Scene(root, 300, 120);
 
        stage.setTitle("Pick Time in minutes");
        stage.setScene(scene);
        stage.show();    
        
        okBtn.setOnMouseClicked((event) -> {   
        	String t1t = t1.getText();
        	if(t1t.matches("[0-9]+")){
        	STARTTIME = Integer.parseInt(t1t) *60;
        	stage.hide();     	
        	cboxSelect = cbox.getValue().toString();
        	alignment = cbox2.getValue().toString();
        	Stage stage2 = new Stage();
        	load(stage2, cboxSelect, alignment);
        	}
        	else {
        		String errordigits = "Input may only contain Numbers!";
        		    JOptionPane.showMessageDialog(new JFrame(), errordigits, "Dialog",
        		        JOptionPane.ERROR_MESSAGE);
        	}
        });
        
    }
    
    public void load(Stage primaryStage, String cboxValue, String aligment) {
    	
        primaryStage.setTitle("COFFEE!"); 
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        
        if(alignment.equals("Top Left")) {        
        	primaryStage.setX(primaryScreenBounds.getMinX() + 15);
        	primaryStage.setY(primaryScreenBounds.getMinY() + 15);
        }
        if(alignment.equals("Bottom Right")) {
        	primaryStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 300);
            primaryStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 110);	
        }


        Group root = new Group();
        Scene scene = new Scene(root, 300, 300);
        
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setResizable(false);
        Button NotAus = new Button("NOTAUS");   
        
        
        
        // Bind the timerLabel text property to the timeSeconds property
        timerLabel.textProperty().bind(timeSeconds.divide(100).asString());
        timerLabel.setTextFill(Color.RED);
        timerLabel.setStyle("-fx-font-size: 6em;");
        
        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(
                timeSeconds.divide(STARTTIME*100.0).subtract(1).multiply(-1));
        
        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds.set((STARTTIME+1)*100);
        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(STARTTIME+1),
                new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();

        VBox vb = new VBox(20);             // gap between components is 20
        vb.setAlignment(Pos.CENTER);        // center the components within VBox
        vb.setPrefWidth(scene.getWidth());
        vb.getChildren().addAll(timerLabel);
        vb.setLayoutY(30);

        
        VBox vb2 = new VBox(20);
        vb2.setAlignment(Pos.BOTTOM_CENTER);
        vb2.setPrefWidth(scene.getWidth());
        vb2.getChildren().addAll(progressBar, NotAus);
        vb2.setLayoutY(160);
        if(cboxValue.equals("Transparent")) {
        	primaryStage.initStyle(StageStyle.TRANSPARENT);
            vb2.setVisible(false);
        }else {
        	primaryStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 400);
            primaryStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 400);
        }
                       
        root.getChildren().add(vb);
        root.getChildren().add(vb2);
        
        scene.setFill(null);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        NotAus.setOnMouseClicked((event) -> {
        	System.exit(0);
        });
         
        new java.util.Timer().schedule(new TimerTask(){
            @Override
            public void run() {         
                final String path = System.getenv("windir") + File.separator + "System32" + File.separator + "rundll32.exe";
                Runtime rt = Runtime.getRuntime();
                try {
					Process pr = rt.exec(path + " user32.dll,LockWorkStation");
					System.exit(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        },1000*STARTTIME,1000*STARTTIME); 
        
        new java.util.Timer().schedule(new TimerTask(){
            @Override
            public void run() {
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            }
        },1000*(STARTTIME-3),1000*(STARTTIME-3)); 
        
        new java.util.Timer().schedule(new TimerTask(){
            @Override
            public void run() {
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            }
        },1000*(STARTTIME-2),1000*(STARTTIME-2)); 
        
        new java.util.Timer().schedule(new TimerTask(){
            @Override
            public void run() {
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	java.awt.Toolkit.getDefaultToolkit().beep();
            }
        },1000*(STARTTIME-1),1000*(STARTTIME-1)); 
             }
    
    private static void createAndShowGUI() {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(createImage("images/coffee.gif", "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();
         
        // Create a popup menu components
        MenuItem aboutItem = new MenuItem("About");
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        Menu displayMenu = new Menu("Display");
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warning");
        MenuItem infoItem = new MenuItem("Info");
        MenuItem noneItem = new MenuItem("None");
        MenuItem exitItem = new MenuItem("Exit");
         
        //Add components to popup menu
        popup.add(aboutItem);
        popup.addSeparator();
//        popup.add(cb1);
//        popup.add(cb2);
//        popup.addSeparator();
//        popup.add(displayMenu);
//        displayMenu.add(errorItem);
//        displayMenu.add(warningItem);
//        displayMenu.add(infoItem);
//        displayMenu.add(noneItem);
        popup.add(exitItem);
         
        trayIcon.setPopupMenu(popup);
         
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }
         
        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "CoffeTimer ©Christopher Schmidt");
            }
        });
         
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "CoffeTimer ©Christopher Schmidt");
            }
        });
         
        cb1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int cb1Id = e.getStateChange();
                if (cb1Id == ItemEvent.SELECTED){
                    trayIcon.setImageAutoSize(true);
                } else {
                    trayIcon.setImageAutoSize(false);
                }
            }
        });
         
        cb2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int cb2Id = e.getStateChange();
                if (cb2Id == ItemEvent.SELECTED){
                    trayIcon.setToolTip("Sun TrayIcon");
                } else {
                    trayIcon.setToolTip(null);
                }
            }
        });
         
//        ActionListener listener = new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                MenuItem item = (MenuItem)e.getSource();
//                //TrayIcon.MessageType type = null;
//                System.out.println(item.getLabel());
//                if ("Error".equals(item.getLabel())) {
//                    //type = TrayIcon.MessageType.ERROR;
//                    trayIcon.displayMessage("Sun TrayIcon Demo",
//                            "This is an error message", TrayIcon.MessageType.ERROR);
//                     
//                } else if ("Warning".equals(item.getLabel())) {
//                    //type = TrayIcon.MessageType.WARNING;
//                    trayIcon.displayMessage("Sun TrayIcon Demo",
//                            "This is a warning message", TrayIcon.MessageType.WARNING);
//                     
//                } else if ("Info".equals(item.getLabel())) {
//                    //type = TrayIcon.MessageType.INFO;
//                    trayIcon.displayMessage("Sun TrayIcon Demo",
//                            "This is an info message", TrayIcon.MessageType.INFO);
//                     
//                } else if ("None".equals(item.getLabel())) {
//                    //type = TrayIcon.MessageType.NONE;
//                    trayIcon.displayMessage("Sun TrayIcon Demo",
//                            "This is an ordinary message", TrayIcon.MessageType.NONE);
//                }
//            }
//        };
//         
//        errorItem.addActionListener(listener);
//        warningItem.addActionListener(listener);
//        infoItem.addActionListener(listener);
//        noneItem.addActionListener(listener);
         
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });
    }
     
    //Obtain the image URL
    protected static Image createImage(String path, String description) {
        URL imageURL = ClassLoader.getSystemResource(path);
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
    
    
   
    public static void main(String[] args) {
        Application.launch(args);
    }
 
}

/*CHANGELOG:
			v0.2: NotAus Button added
				  Spinner not writeable anymore
				  window not resizeable anymore
				  minor Bugfixes
*/