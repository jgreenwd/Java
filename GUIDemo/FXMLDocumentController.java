package guidemo;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class FXMLDocumentController implements Initializable {
    
    /* ---------- CheckBox example ---------- */
    @FXML private Label pizzaOrderLabel;
    @FXML private CheckBox pepperoniCheckBox;
    @FXML private CheckBox pineappleCheckBox;
    @FXML private CheckBox baconCheckBox;
    
    public void pizzaOrderButtonPushed() {
        String order = "Toppins are:";
        if (pepperoniCheckBox.isSelected())
            order += "\npepperoni";
        if (pineappleCheckBox.isSelected())
            order += "\npineapple";
        if (baconCheckBox.isSelected())
            order += "\nbacon";
        
        this.pizzaOrderLabel.setText(order);
    }

    
    /* ---------- ChoiceBox example ---------- */
    @FXML private ChoiceBox choiceBox;
    @FXML private Label choiceBoxLabel;
    
    public void choiceBoxButtonPushed() {
        choiceBoxLabel.setText("My favorite fruit is: " + choiceBox.getValue().toString());
    }
    

    /* ----------- ComboBox example ---------- */
    @FXML private ComboBox comboBox;
    @FXML private Label comboBoxLabel;
    
    public void comboBoxWasUpdated() {
        this.comboBoxLabel.setText("Course selected: \n" + comboBox.getValue().toString());
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // CheckBox example init
        pizzaOrderLabel.setText("");
        
        // ChoiceBox example init
        choiceBoxLabel.setText("");
        choiceBox.getItems().addAll("apples","oranges","pairs", "bananas", "strawberries");
        choiceBox.setValue("apples");
        
        // ComboBox example init
        comboBox.getItems().add("C462");
        comboBox.getItems().addAll("C190", "C191", "C960");
    }    
    
}
