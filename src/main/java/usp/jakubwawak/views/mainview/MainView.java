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
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

@PageTitle("Printer View")
@Route(value = "mainview")
@RouteAlias(value = "/")
public class MainView extends VerticalLayout {

    Grid<Printer_View> grid;
    TonerPrinter_View tpv;

    Button update_button;
    Button addprinter_button;
    Button setlocation_button;
    Button setinstancename_button;

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
        setlocation_button = new Button("Update location",this::setlocation);
        setinstancename_button = new Button("Set instance name");

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

        HorizontalLayout hl = new HorizontalLayout();
        hl.add(addprinter_button);
        hl.add(update_button);
        hl.add(setlocation_button);

        ArrayList<Printer_View> view = tpv.list_view;
        grid.setItems(view);
        Icon vaadinIcon = new Icon(VaadinIcon.PRINT);
        add(vaadinIcon);
        add(new H6(PrintApplication.version+"/"+PrintApplication.build+"/"+PrintApplication.database.get_instance_name()));
        add(update_time_label);
        add(hl);
        add(grid);

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
     * Function for setting location
     * @param e
     */
    private void setlocation(ClickEvent e){
        setlocation_button.getUI().ifPresent(
                ui -> ui.navigate("setlocation"));
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
