/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.views.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import usp.jakubwawak.view.Printer_View;
import usp.jakubwawak.view.TonerSnapshot;

/**
 * Component for showing printer details
 */
public class PrinterDetails_Component {

    public Dialog main_dialog;
    VerticalLayout main_layout;
    Button close_button;

    H1 printername_header;
    H3 localization_header;

    Grid<TonerSnapshot> snapshotgrid;

    TextArea warehousedata_area,log_area;

    int printer_id;
    Printer_View printer_obj;

    /**
     * Constructor
     */
    public PrinterDetails_Component(int printer_id){
        this.printer_id = printer_id;
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        snapshotgrid = new Grid<>(TonerSnapshot.class,false);

        printername_header = new H1();
        localization_header = new H3();
        warehousedata_area = new TextArea("Warehouse status");
        log_area = new TextArea("Printer events");
        close_button = new Button("Close",this::close_action);

        printer_obj = new Printer_View(printer_id);
        printer_obj.load_warehouse_data();
    }

    /**
     * Function for creating dialog
     */
    public void create_dialog(){
        printername_header.setText(printer_obj.printer_name);
        if (printer_obj.printer_localization != null){
            localization_header.setText(printer_obj.printer_localization);
        }
        else{
            localization_header.setText("Brak ustalonej lokalizacji");
        }
        warehousedata_area.setValue(printer_obj.get_warehouse_status());
        log_area.setValue(printer_obj.get_printer_logs());

        warehousedata_area.setHeight("80px");
        warehousedata_area.setWidth("350px");
        warehousedata_area.setEnabled(false);
        log_area.setHeight("200px");
        log_area.setWidth("350px");

        main_layout.add(printername_header);
        main_layout.add(localization_header);
        main_layout.add(warehousedata_area);
        main_layout.add(log_area);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");

        snapshotgrid.addColumn(TonerSnapshot::getTime).setHeader("Time");
        snapshotgrid.addColumn(TonerSnapshot::getToner_data).setHeader("Snapshot");
        snapshotgrid.setItems(printer_obj.snapshots_list);
        snapshotgrid.setSizeFull();
        snapshotgrid.setWidth("600px");
        snapshotgrid.setHeight("300px");

        VerticalLayout gridlayout = new VerticalLayout(new H3("Snapshots"),snapshotgrid);
        HorizontalLayout splitlayout = new HorizontalLayout(main_layout,gridlayout);
        splitlayout.setSizeFull();
        main_dialog.add(splitlayout);
        main_dialog.getFooter().add(close_button);
    }

    /**
     * Function for closing dialog window
     * @param e
     */
    private void close_action(ClickEvent e){
        main_dialog.close();
    }
}
