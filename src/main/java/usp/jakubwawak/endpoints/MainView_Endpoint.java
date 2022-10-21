/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.endpoints;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.view.Printer_View;
import usp.jakubwawak.view.TonerPrinter_View;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;

/**
 * MainView / main page
 */

@Route(value = "")
@PageTitle("Toner Statistics")
@PermitAll
public class MainView_Endpoint extends VerticalLayout {

    Grid<Printer_View> grid;

    /**
     * Constructor
     */
    public MainView_Endpoint(){
        TonerPrinter_View tpv = new TonerPrinter_View(PrintApplication.database);
        add("mainview-endpoint");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        tpv.load_view();
        grid = new Grid<>(Printer_View.class);

        grid.addColumn(Printer_View::getID).setHeader("Atmosfera ID");
        grid.addColumn(Printer_View::getName).setHeader("Printer Name");
        grid.addColumn(Printer_View::getIP).setHeader("Printer iP");
        grid.addColumn(Printer_View::getLastUpdate).setHeader("Last Update Time");
        grid.addColumn(Printer_View::getCyan).setHeader("Cyan");
        grid.addColumn(Printer_View::getMagenta).setHeader("Magenta");
        grid.addColumn(Printer_View::getYellow).setHeader("Yellow");
        grid.addColumn(Printer_View::getBlack).setHeader("Black");

        ArrayList<Printer_View> view = tpv.list_view;
        grid.setItems(view);
        add(grid);
    }
}
