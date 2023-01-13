/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.views.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.Lumo;
import usp.jakubwawak.Maintanance.LogObject;
import usp.jakubwawak.print.PrintApplication;

/**
 * Object for creating log view component
 */
public class LogView_Component {

    public Dialog main_dialog;
    VerticalLayout main_layout;

    Grid<LogObject> log_view;

    /**
     * Constructor
     */
    public LogView_Component(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();

        main_dialog.setWidth("900px");
        main_dialog.setHeight("700px");

        main_layout.setSpacing(false);
        log_view = new Grid<>(LogObject.class,false);
        prepare_view();
        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");
        main_dialog.add(main_layout);
    }

    /**
     * Function for preparing view
     */
    void prepare_view(){
        log_view.addColumn(LogObject::getID).setHeader("ID");
        log_view.addColumn(LogObject::get_time).setHeader("Time");
        log_view.addColumn(LogObject::get_code).setHeader("Code");
        log_view.addColumn(LogObject::get_desc).setHeader("Information");

        log_view.setItems(PrintApplication.database.load_log());
        main_layout.add(new H1("Action History"));
        main_layout.add(log_view);
    }
}
