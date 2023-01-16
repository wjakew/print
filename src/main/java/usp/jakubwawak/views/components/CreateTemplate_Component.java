/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.views.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import usp.jakubwawak.view.Printer_View;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Object for creating template component
 */
public class CreateTemplate_Component {

    public Dialog dialog;
    VerticalLayout main_layout;
    int printer_id;

    Checkbox cyan_checkbox,magenta_checkbox,yellow_checkbox,black_checkbox,waste_checkbox;
    Button createtemplate_button;

    TextArea template_area;

    /**
     * Constructor
     * @param printer_id
     */
    public CreateTemplate_Component(int printer_id){
        this.printer_id = printer_id;
        dialog = new Dialog();
        main_layout = new VerticalLayout();

        cyan_checkbox = new Checkbox("Cyan Toner");
        magenta_checkbox = new Checkbox("Magenta Toner");
        yellow_checkbox = new Checkbox("Yellow Toner");
        black_checkbox = new Checkbox("Black Toner");
        waste_checkbox = new Checkbox("Waste Container");

        createtemplate_button = new Button("Create Template",this::createtemplate_action);


        template_area = new TextArea("Template");
        template_area.setWidth("400px");
        template_area.setHeight("300px");
    }

    /**
     * Function for creating dialog object
     */
    public void create_dialog(){
        Printer_View printer = new Printer_View(printer_id);
        main_layout.add(new H1("New Mail Template"));
        main_layout.add(new H3(printer.printer_name));
        main_layout.add(new HorizontalLayout(cyan_checkbox,magenta_checkbox,yellow_checkbox));
        main_layout.add(new HorizontalLayout(black_checkbox,waste_checkbox));
        main_layout.add(createtemplate_button);
        template_area.setVisible(false);
        main_layout.add(template_area);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");
        dialog.add(main_layout);
    }

    /**
     * Function for creating template
     * @param e
     */
    private void createtemplate_action(ClickEvent e){
        Printer_View printer = new Printer_View(printer_id);
        String data = "";
        if ( cyan_checkbox.getValue() ){
            data = data + "Cyan Toner\n";
        }
        if ( magenta_checkbox.getValue() ){
            data = data + "Magenta Toner\n";
        }
        if ( yellow_checkbox.getValue() ){
            data = data + "Yellow Toner\n";
        }
        if ( black_checkbox.getValue() ){
            data = data + "Black Toner\n";
        }
        if ( waste_checkbox.getValue() ){
            data = data + "Waste Container";
        }

        String template = "Dzień dobry,\nPiszę do Państwa z uprzejmą prośbą o zamówienie materiałów eksploatacyjnych do drukarki:\n";

        template = template + "Model: " + printer.printer_model + ", Numer seryjny: "+ printer.printer_serialnumber +"\n\n";
        template = template + "Potrzebne materiały:\n" + data + "\n\nPozdrawiam,";

        template_area.setValue(template);
        template_area.setVisible(true);
    }

}
