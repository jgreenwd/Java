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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

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


    /* ---------- RadioButton example ---------- */
    @FXML private RadioButton phpButton;
    @FXML private RadioButton JavaButton;
    @FXML private RadioButton CsharpButton;
    @FXML private RadioButton CppButton;
    @FXML private Label radioButtonLabel;
    private ToggleGroup faveLangToggleGroup;
    
    public void radioButtonWasUpdated() {
        if (this.faveLangToggleGroup.getSelectedToggle().equals(this.CppButton))
            radioButtonLabel.setText("Selected C++");
        if (this.faveLangToggleGroup.getSelectedToggle().equals(this.CsharpButton))
            radioButtonLabel.setText("Selected C#");
        if (this.faveLangToggleGroup.getSelectedToggle().equals(this.phpButton))
            radioButtonLabel.setText("Selected PHP");
        if (this.faveLangToggleGroup.getSelectedToggle().equals(this.JavaButton))
            radioButtonLabel.setText("Selected Java");
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
        comboBoxLabel.setText("");
        comboBox.getItems().add("C462");
        comboBox.getItems().addAll("C190", "C191", "C960");
        
        // RadioButton example init
        radioButtonLabel.setText("");
        faveLangToggleGroup = new ToggleGroup();
        this.CppButton.setToggleGroup(faveLangToggleGroup);
        this.CsharpButton.setToggleGroup(faveLangToggleGroup);
        this.phpButton.setToggleGroup(faveLangToggleGroup);
        this.JavaButton.setToggleGroup(faveLangToggleGroup);
    }    
    
}