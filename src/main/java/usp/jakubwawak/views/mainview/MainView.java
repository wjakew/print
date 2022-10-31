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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
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

    H3 update_time_label;
    /**
     * Constructor
     */
    public MainView(){
        setSpacing(false);

        tpv = new TonerPrinter_View(PrintApplication.database);
        update_button = new Button("Update", this::update);
        tpv.load_view();
        grid = new Grid<>(Printer_View.class);
        update_time_label = new H3("Last update: "+tpv.list_view.get(0).getLastUpdate());

        ArrayList<Printer_View> view = tpv.list_view;
        grid.setItems(view);
        add(new H2("PrintApp"));
        add(update_time_label);
        add(grid);
        add(update_button);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
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
