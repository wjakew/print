/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.views.setlocation;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import usp.jakubwawak.database.Database_Manager;
import usp.jakubwawak.print.PrintApplication;

import java.util.ArrayList;

/**
 * Page for setting location view data
 */
@PageTitle("Set Location")
@Route(value = "setlocation")
public class SetLocationView extends VerticalLayout {


    ComboBox<String> comboBox;
    TextField localization_field;

    Button update_button;

    /**
     * Constructor
     */
    public SetLocationView(){
        setSpacing(false);
        this.getElement().setAttribute("theme", Lumo.DARK);

        add(new H1("Update printer location"));

        Database_Manager dm = new Database_Manager(PrintApplication.database);

        comboBox = new ComboBox<>("Printer");
        localization_field = new TextField();
        localization_field.setLabel("Localization");
        update_button = new Button("Update",this::updateaction);
        comboBox.setAllowCustomValue(false);

        ArrayList<String> printer_list = dm.list_printers();
        comboBox.addCustomValueSetListener(e -> {
            comboBox.setItems(printer_list);
        });

        add(comboBox);
        add(localization_field);
        add(update_button);


        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
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
            update_button.getUI().ifPresent(ui ->
                    ui.navigate("mainview"));
        }
    }
}
