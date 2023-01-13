/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.views.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.Lumo;
import usp.jakubwawak.database.Database_Manager;
import usp.jakubwawak.print.PrintApplication;

/**
 * Component for adding printer to database
 */
public class AddPrinter_Component {

    public Dialog dialog;
    public VerticalLayout main_layout;

    TextField field_printer_name,field_printer_model,field_printer_localization,field_printer_ip;
    Button addprinter_button;

    /**
     * Constructor
     */
    public AddPrinter_Component(){
        dialog = new Dialog();
        main_layout = new VerticalLayout();

        main_layout.setSpacing(false);
        field_printer_name = new TextField();
        field_printer_model = new TextField();
        field_printer_localization = new TextField();
        field_printer_ip = new TextField();

        addprinter_button = new Button("Add printer",this::addprinter);

        main_layout.add(new H3("Printer name"));
        main_layout.add(field_printer_name);
        main_layout.add(new H3("Printer model"));
        main_layout.add(field_printer_model);
        main_layout.add(new H3("Printer localization"));
        main_layout.add(field_printer_localization);
        main_layout.add(new H3("Printer IP"));
        main_layout.add(field_printer_ip);
        main_layout.add(addprinter_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");

        dialog.add(main_layout);
    }

    /**
     * Function for adding printer to database
     */
    private void addprinter(ClickEvent event){
        if (check_fields()){
            Database_Manager dm = new Database_Manager(PrintApplication.database);
            if (dm.add_printer(field_printer_name.getValue(),field_printer_ip.getValue(),field_printer_localization.getValue(),
                    field_printer_model.getValue(),"Eksploatowany") == 1){

                Notification noti = Notification.show("Printer added!");
                field_printer_name.setEnabled(false);
                field_printer_ip.setEnabled(false);
                field_printer_localization.setEnabled(false);
                field_printer_model.setEnabled(false);
                addprinter_button.setEnabled(false);
                UI.getCurrent().getPage().reload();
            }
            else{
                Notification noti = Notification.show("Database error");
            }
        }
        else{
            Notification noti = Notification.show("Error, one or more fields empty");
        }
    }


    /**
     * Function for checking fields content
     * @return
     */
    boolean check_fields(){
        return !field_printer_name.getValue().equals("") && !field_printer_model.getValue().equals("") && !field_printer_localization.getValue().equals("")
                && !field_printer_ip.getValue().equals("");
    }
}
