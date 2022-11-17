/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.views.mainview;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.lumo.Lumo;
import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.scenaio.UpdateTonerData_Scenario;
import usp.jakubwawak.view.Printer_View;
import usp.jakubwawak.view.TonerPrinter_View;
import usp.jakubwawak.warehouse.Warehouse_Error;
import usp.jakubwawak.warehouse.Warehouse_Manager;

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

    Button warehouse_button;

    H3 update_time_label;
    /**
     * Constructor
     */
    public MainView(){
        setSpacing(false);
        this.getElement().setAttribute("theme", Lumo.DARK);
        tpv = new TonerPrinter_View(PrintApplication.database);
        update_button = new Button(VaadinIcon.DOWNLOAD.create(), this::update);
        addprinter_button = new Button(VaadinIcon.PLUS_CIRCLE.create(),this::addprinter);
        setlocation_button = new Button(VaadinIcon.LOCATION_ARROW.create(),this::setlocation);
        warehouse_button = new Button(VaadinIcon.BUILDING.create(),this::warehouseaction);
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
        hl.add(warehouse_button);

        ArrayList<Printer_View> view = tpv.list_view;
        grid.setItems(view);
        Icon vaadinIcon = new Icon(VaadinIcon.PRINT);
        add(vaadinIcon);
        add(new H6(PrintApplication.version+"/"+PrintApplication.build+"/"+PrintApplication.database.get_instance_name()));
        add(hl);
        Warehouse_Manager wm = new Warehouse_Manager(PrintApplication.database);
        if ( wm.prepare_raport().size() > 0 ){
            add(warning_grid_loader());
        }
        add(update_time_label);
        add(grid);

        add(new H6("By Jakub Wawak / kubawawak@gmail.com / j.wawak@usp.pl"));

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

    private void warehouseaction(ClickEvent e){
        warehouse_button.getUI().ifPresent(
                ui -> ui.navigate("warehouse"));
    }

    /**
     * Function for load grid fill with warnings
     * @return Grid
     */
    private Grid<Warehouse_Error> warning_grid_loader(){
        Warehouse_Manager wm = new Warehouse_Manager(PrintApplication.database);
        Grid<Warehouse_Error> warning_grid = new Grid<>(Warehouse_Error.class,false);
        warning_grid.addColumn(Warehouse_Error::getPrinter_name).setHeader("Nazwa drukarki");
        warning_grid.addColumn(Warehouse_Error::getError_data).setHeader("Opis błędu");
        warning_grid.setItems(wm.prepare_raport());
        return warning_grid;
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
        Notification noti_updatetoner = Notification.show("Toner data updated!");
        update_button.setText("Update");
        UI.getCurrent().getPage().reload();
    }
}
