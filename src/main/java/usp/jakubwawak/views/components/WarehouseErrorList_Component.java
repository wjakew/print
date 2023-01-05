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
import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.warehouse.Warehouse_Error;
import usp.jakubwawak.warehouse.Warehouse_Manager;

/**
 * Object for showing list of errors
 */
public class WarehouseErrorList_Component {

    public Dialog main_dialog;
    public VerticalLayout main_layout;

    Grid<Warehouse_Error> error_grid;

    /**
     * Constructor
     */
    public WarehouseErrorList_Component(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        error_grid = new Grid<>(Warehouse_Error.class,false);
    }

    /**
     * Function for creating dialog components
     */
    public void create_dialog(){
        Warehouse_Manager wm = new Warehouse_Manager(PrintApplication.database);
        error_grid.addColumn(Warehouse_Error::getPrinter_name).setHeader("Nazwa drukarki").setResizable(true);
        error_grid.addColumn(Warehouse_Error::getError_data).setHeader("Opis błędu").setResizable(true);
        error_grid.setItems(wm.prepare_raport());
        main_layout.add(new H1("Warehouse Errors"));
        main_layout.add(error_grid);
        main_layout.setAlignItems(FlexComponent.Alignment.CENTER);
        error_grid.setSizeFull();
        error_grid.setWidth("700px");
        error_grid.setHeight("450px");
        main_dialog.add(main_layout);
    }

}
