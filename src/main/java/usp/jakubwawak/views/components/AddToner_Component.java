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
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import usp.jakubwawak.database.Database_Warehouse;
import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.view.Printer_View;

/**
 * Component for adding toner data
 */
public class AddToner_Component {

    Dialog main_dialog;
    VerticalLayout main_layout;
    Button add_button,remove_button;

    ComboBox template_combobox;

    int printer_id;

    /**
     * Constructor
     * @param printer_id
     */
    public AddToner_Component(int printer_id){
        this.printer_id = printer_id;
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();

        add_button = new Button("", VaadinIcon.PLUS.create(),this::addtoner_action);
        remove_button = new Button("",VaadinIcon.MINUS.create(),this::removetoner_action);

        Database_Warehouse dw = new Database_Warehouse(PrintApplication.database);
        template_combobox = new ComboBox();
        template_combobox.setItems(dw.list_warehouse_templates());
    }

    /**
     * Function for creating dialog compontents
     */
    void create_dialog(){
        Printer_View pv = new Printer_View(printer_id);
        main_layout.add(new H1(pv.printer_name));
        main_layout.add(new HorizontalLayout(template_combobox,add_button,remove_button));

        main_dialog.add(main_layout);
    }

    /**
     * Function for adding toner action
     * @param e
     */
    private void addtoner_action(ClickEvent e){
        Database_Warehouse dw = new Database_Warehouse(PrintApplication.database);
        Printer_View pv = new Printer_View(printer_id);
        String printer_name = pv.printer_name;
        String template_name = template_combobox.getValue().toString();
        if ( dw.check_element(printer_name,template_name) == 0){
            if ( !printer_name.equals("") && !template_name.equals("") ){

                int ans = dw.add_element(printer_name,template_name);
                if ( ans == 1 ){
                    Notification addelement_noti = Notification.show("Warehouse updated");
                    main_dialog.close();
                }
                else {
                    Notification addelement_noti = Notification.show("Database error!");
                }
            }
            else{
                Notification addelement_noti = Notification.show("Wrong selection");
            }
        }
        else{
            Notification addelement_noti = Notification.show("Warehouse has this toner already!");
        }
    }

    /**
     * Function for removing toner action
     * @param e
     */
    private void removetoner_action(ClickEvent e){
        Database_Warehouse dw = new Database_Warehouse(PrintApplication.database);
        Printer_View pv = new Printer_View(printer_id);
        String printer_name = pv.printer_name;
        String template_name = template_combobox.getValue().toString();
        if (dw.check_element(printer_name, template_name) == 1) {
            if ( !printer_name.equals("") && !template_name.equals("") ){
                int ans = dw.remove_element(printer_name,template_name);
                if ( ans == 1 ){
                    Notification addelement_noti = Notification.show("Warehouse updated");
                    main_dialog.close();
                }
                else{
                    Notification addelement_noti = Notification.show("Database error!");
                }
            }
            else{
                Notification addelement_noti = Notification.show("Wrong selection");
            }
        }
        else{
            Notification addelement_noti = Notification.show("Object didn't exist in warehouse!");
        }
    }
}
