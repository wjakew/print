/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.views.components;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import usp.jakubwawak.view.Printer_View;
import usp.jakubwawak.view.TonerSnapshot;

/**
 * Object for creating details compomponent dialog window
 */
public class TonerDetails_Component {

    public Dialog main_dialog;
    VerticalLayout main_layout;
    int printer_id;

    H1 header;
    Grid<TonerSnapshot> snapshotgrid;
    /**
     * Constructor
     */
    public TonerDetails_Component(int printer_id){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        this.printer_id = printer_id;
        snapshotgrid = new Grid(TonerSnapshot.class,false);
    }

    /**
     * Function for creating dialog components
     */
    public void create_dialog(){
        Printer_View printer = new Printer_View(printer_id);
        header = new H1("Snapshot list for "+printer.printer_name);
        snapshotgrid.addColumn(TonerSnapshot::getTime).setHeader("Time").setResizable(true);
        snapshotgrid.addColumn(TonerSnapshot::getCyan).setHeader("Cyan");
        snapshotgrid.addColumn(TonerSnapshot::getMagenta).setHeader("Magenta");
        snapshotgrid.addColumn(TonerSnapshot::getYellow).setHeader("Yellow");
        snapshotgrid.addColumn(TonerSnapshot::getBlack).setHeader("Black");
        snapshotgrid.setItems(printer.snapshots_list);
        snapshotgrid.setSizeFull();
        snapshotgrid.setWidth("800px");
        snapshotgrid.setHeight("600px");

        main_layout.add(header);
        main_layout.add(snapshotgrid);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");

        main_dialog.add(main_layout);

    }
}
