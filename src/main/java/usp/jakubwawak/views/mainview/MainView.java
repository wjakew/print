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
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.lumo.Lumo;
import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.scenaio.UpdateTonerData_Scenario;
import usp.jakubwawak.view.Printer_View;
import usp.jakubwawak.view.TonerPrinter_View;
import usp.jakubwawak.views.components.TonerDetails_Component;
import usp.jakubwawak.views.components.WarehouseErrorList_Component;
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

    Button reload_button;

    Button log_button;

    Button warehouse_button;

    Button printerview_button;

    Button warehouseerror_button;

    H3 update_time_label;
    /**
     * Constructor
     */
    public MainView(){
        setSpacing(false);
        this.getElement().setAttribute("theme", Lumo.DARK);
        tpv = new TonerPrinter_View(PrintApplication.database);
        update_button = new Button("Update",VaadinIcon.DOWNLOAD.create(), this::update);
        addprinter_button = new Button("Add printer",VaadinIcon.PLUS_CIRCLE.create(),this::addprinter);
        setlocation_button = new Button("Set Printer Location",VaadinIcon.LOCATION_ARROW.create(),this::setlocation);
        warehouse_button = new Button("Warehouse",VaadinIcon.BUILDING.create(),this::warehouseaction);
        Warehouse_Manager wm = new Warehouse_Manager(PrintApplication.database);
        warehouseerror_button = new Button(wm.prepare_raport().size()+" Errors",this::warehouseerror_action);
        setinstancename_button = new Button("Set instance name");
        reload_button = new Button(VaadinIcon.REFRESH.create(),this::reloadpage);
        log_button = new Button(VaadinIcon.ARCHIVE.create(),this::showlog);
        printerview_button = new Button(VaadinIcon.PRINT.create(),this::showprinters);

        tpv.load_view();
        grid = new Grid<>(Printer_View.class,false);

        update_time_label = new H3("Last update: "+tpv.list_view.get(0).getLastUpdate());

        grid.addColumn(Printer_View::getID).setHeader("AtmosferaID");
        grid.addColumn(Printer_View::getName).setHeader("Nazwa drukarki").setResizable(true);
        grid.addColumn(Printer_View::getLocalization).setHeader("Lokalizacja");
        grid.addColumn(Printer_View::getIP).setHeader("Adres IP");
        grid.addColumn(Printer_View::getCyan).setHeader("Cyan");
        grid.addColumn(Printer_View::getMagenta).setHeader("Magenta");
        grid.addColumn(Printer_View::getYellow).setHeader("Yellow");
        grid.addColumn(Printer_View::getBlack).setHeader("Black");

        HorizontalLayout hl = new HorizontalLayout();
        hl.add(reload_button);
        hl.add(addprinter_button);
        hl.add(update_button);
        hl.add(setlocation_button);
        hl.add(warehouse_button);
        hl.add(warehouseerror_button);
        hl.add(log_button);
        hl.add(printerview_button);
        ArrayList<Printer_View> view = tpv.list_view;
        grid.setItems(view);
        Icon vaadinIcon = new Icon(VaadinIcon.PRINT);
        add(vaadinIcon);
        add(new H6(PrintApplication.version+"/"+PrintApplication.build+"/"+PrintApplication.database.get_instance_name()));
        add(hl);
        add(update_time_label);
        add(grid);

        grid.addItemClickListener(e -> {
            Printer_View pv = e.getItem();
            if ( pv != null ){
                show_tonerdetailsdialog(pv.printer_id);
            }});

        add(new H6("By Jakub Wawak / kubawawak@gmail.com / j.wawak@usp.pl"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    /**
     * Function for opening dialog window
     * @param e
     */
    private void warehouseerror_action(ClickEvent e){
        WarehouseErrorList_Component welc = new WarehouseErrorList_Component();
        welc.create_dialog();
        add(welc.main_dialog);
        welc.main_dialog.open();
    }

    /**
     * Function for routing to printer page
     * @param e
     */
    private void showprinters(ClickEvent e){
        printerview_button.getUI().ifPresent(ui ->
                ui.navigate("printers"));
    }

    /**
     * Function for routing to log page
     * @param e
     */
    private void showlog(ClickEvent e){
        log_button.getUI().ifPresent(ui ->
                ui.navigate("history"));
    }

    /**
     * Function for reloading page
     * @param event
     */
    private void reloadpage(ClickEvent event){
        UI.getCurrent().getPage().reload();
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
     * Function for showing printer details dialog
     * @param printer_id
     */
    private void show_tonerdetailsdialog(int printer_id){
        TonerDetails_Component tdc = new TonerDetails_Component(printer_id);
        tdc.create_dialog();
        add(tdc.main_dialog);
        tdc.main_dialog.open();
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
