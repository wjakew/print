/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.views.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.Lumo;
import org.apache.poi.ss.formula.functions.T;
import usp.jakubwawak.database.Database_Manager;
import usp.jakubwawak.print.PrintApplication;

import java.util.ArrayList;

/**
 * Object for creating component for setting location
 */
public class SetLocation_Component {

    public Dialog main_dialog;
    VerticalLayout main_layout;

    ComboBox<String> comboBox;
    TextField localization_field;

    Button update_button;

    /**
     * Constructor
     */
    public SetLocation_Component(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        update_button = new Button("Update",this::updateaction);
        comboBox = new ComboBox<>("Printer");
        localization_field = new TextField("New Localization");
        main_layout.setSpacing(false);
    }

    /**
     * Function for creating dialog
     */
    public void create_dialog(){
        Database_Manager dm = new Database_Manager(PrintApplication.database);
        ArrayList<String> printer_list = dm.list_printers();
        comboBox.setItems(printer_list);

        main_layout.add(comboBox);
        main_layout.add(localization_field);
        main_layout.add(update_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");
        main_dialog.add(main_layout);
    }

    /**
     * Function for update localization
     * @param e
     */
    private void updateaction(ClickEvent e){
        if ( update_button.getText().equals("Update")){
            if ( !localization_field.getValue().equals("")){
                String printer_name = comboBox.getValue();
                String localization = localization_field.getValue();
                Database_Manager dm = new Database_Manager(PrintApplication.database);
                int ans = dm.update_printer_localization(localization,printer_name);
                switch(ans){
                    case 1:
                    {
                        //updated successfully
                        Notification noti_updated = Notification.show("Updated!");
                        noti_updated.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        update_button.setText("Return");
                        localization_field.setEnabled(false);
                        comboBox.setEnabled(false);
                        UI.getCurrent().getPage().reload();
                        break;
                    }
                    case -1:
                    {
                        //database error
                        Notification noti_databaserrror = Notification.show("Database error");
                        noti_databaserrror.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        break;
                    }
                }
            }
            else{
                Notification noti_fieldempty = Notification.show("Localization field is empty!");
                noti_fieldempty.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
        else{
            main_dialog.close();
        }
    }
}
