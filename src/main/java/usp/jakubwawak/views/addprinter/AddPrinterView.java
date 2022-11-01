package usp.jakubwawak.views.addprinter;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.Lumo;
import usp.jakubwawak.database.Database_Manager;
import usp.jakubwawak.print.PrintApplication;

@PageTitle("Add printer")
@Route(value = "addprinter")
public class AddPrinterView extends VerticalLayout {

    TextField field_printer_name,field_printer_model,field_printer_localization,field_printer_ip;
    Button addprinter_button,cancel_button;

    /**
     * Constructor
     */
    public AddPrinterView(){
        setSpacing(false);
        this.getElement().setAttribute("theme", Lumo.DARK);
        field_printer_name = new TextField();
        field_printer_model = new TextField();
        field_printer_localization = new TextField();
        field_printer_ip = new TextField();

        addprinter_button = new Button("Add printer",this::addprinter);
        cancel_button = new Button("Cancel",this::cancel);

        add(new H3("Printer name"));
        add(field_printer_name);
        add(new H3("Printer model"));
        add(field_printer_model);
        add(new H3("Printer localization"));
        add(field_printer_localization);
        add(new H3("Printer IP"));
        add(field_printer_ip);
        add(addprinter_button);
        add(cancel_button);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");

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
                cancel_button.setText("Return");
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
     * Funtion for canceling/going back to main page
     * @param event
     */
    private void cancel(ClickEvent event){
        cancel_button.getUI().ifPresent(ui ->
                ui.navigate("mainview"));
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
