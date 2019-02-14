package guidemo;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class FXMLDocumentController implements Initializable {
    
    // These items for CheckBox example
    @FXML private Label pizzaOrderLabel;
    @FXML private CheckBox pepperoniCheckBox;
    @FXML private CheckBox pineappleCheckBox;
    @FXML private CheckBox baconCheckBox;
    
    // These items for ChoiceBox example
    @FXML private ChoiceBox choiceBox;
    @FXML private Label choiceBoxLabel;
    
    /* This will update ChoiceBox example */
    public void choiceBoxButtonPushed() {
        choiceBoxLabel.setText("My favorite fruit is: " + choiceBox.getValue().toString());
    }
    
    /* This will update CheckBox example */
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

    
    private void handleButtonAction(ActionEvent event) {
        pizzaOrderLabel.setText("stuff");
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pizzaOrderLabel.setText("");
        
        // These are for ChoiceBox example
        choiceBoxLabel.setText("");
        choiceBox.getItems().addAll("apples","oranges","pairs", "bananas", "strawberries");
        choiceBox.setValue("apples");
    }    
    
}
