/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.views.mainview;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.Lumo;
import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.scenaio.UpdateTonerData_Scenario;
import usp.jakubwawak.view.Printer_View;
import usp.jakubwawak.view.TonerPrinter_View;

import java.util.ArrayList;

/**
 * MainView / main page
 */

@PageTitle("MainView")
@Route(value = "mainview")
@RouteAlias(value = "/")
public class MainView extends VerticalLayout {

    Grid<Printer_View> grid;
    TonerPrinter_View tpv;

    Button update_button;

    Button addprinter_button;

    H3 update_time_label;
    /**
     * Constructor
     */
    public MainView(){
        setSpacing(false);
        this.getElement().setAttribute("theme", Lumo.DARK);
        tpv = new TonerPrinter_View(PrintApplication.database);
        update_button = new Button("Update", this::update);
        addprinter_button = new Button("Add printer",this::addprinter);
        tpv.load_view();
        grid = new Grid<>(Printer_View.class,false);

        update_time_label = new H3("Last update: "+tpv.list_view.get(0).getLastUpdate());

        grid.addColumn(Printer_View::getID).setHeader("AtmosferaID");
        grid.addColumn(Printer_View::getName).setHeader("Nazwa drukarki");
        grid.addColumn(Printer_View::getLocalization).setHeader("Lokalizacja");
        grid.addColumn(Printer_View::getIP).setHeader("Adres IP");
        grid.addColumn(Printer_View::getCyan).setHeader("Cyan");
        grid.addColumn(Printer_View::getMagenta).setHeader("Magenta");
        grid.addColumn(Printer_View::getYellow).setHeader("Yellow");
        grid.addColumn(Printer_View::getBlack).setHeader("Black");

        ArrayList<Printer_View> view = tpv.list_view;
        grid.setItems(view);
        add(new H2("PrintApp "+PrintApplication.version));
        add(update_time_label);
        add(addprinter_button);
        add(grid);
        add(update_button);
        add(new H4("Build: "+PrintApplication.build));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    /**
     * Function for routing to the addprinter page
     * @param event
     */
    private void addprinter(ClickEvent event){
        addprinter_button.getUI().ifPresent(ui ->
                ui.navigate("addprinter"));
    }


    /**
     * Function for performing update
     * @param event
     */
    private void update(ClickEvent event){
        update_button.setText("Updating...");
        System.out.println("Toner update start");
        UpdateTonerData_Scenario utds = new UpdateTonerData_Scenario();
        utds.run_scenario();
        System.out.println("Toner update finish");
        tpv = new TonerPrinter_View(PrintApplication.database);
        tpv.load_view();
        grid.getDataProvider().refreshAll();
        update_time_label.setText("Last update: "+tpv.list_view.get(0).getLastUpdate());
        update_button.setText("Update");
    }
}
