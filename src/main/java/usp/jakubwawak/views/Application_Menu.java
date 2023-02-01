/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.Lumo;
import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.scenaio.UpdateTonerData_Scenario;
import usp.jakubwawak.view.TonerPrinter_View;
import usp.jakubwawak.views.components.*;
import usp.jakubwawak.warehouse.Warehouse_Manager;

/**
 * Object creating printapp layout
 */
public class Application_Menu extends AppLayout {

    DrawerToggle main_toggle;

    H1 title;

    TonerPrinter_View tpv;

    Button update_button,addprinter_button,setlocation_button,reload_button,log_button
            ,warehouse_button,printerview_button,warehouseerror_button,oldview_button;

    /**
     * Constructor
     */
    public Application_Menu(){
        main_toggle = new DrawerToggle();
        update_button = new Button("Update Printer Data", VaadinIcon.DOWNLOAD.create(), this::update);
        reload_button = new Button("Reload Page",VaadinIcon.REFRESH.create(),this::reloadpage_action);
        log_button = new Button("Application Log",VaadinIcon.ARCHIVE.create(),this::showlog);
        addprinter_button = new Button("Add new printer",VaadinIcon.PRINT.create(),this::addprinter);
        printerview_button = new Button("Printer List",VaadinIcon.LIST.create(),this::showprinters);
        setlocation_button = new Button("Set Printer Location",VaadinIcon.LOCATION_ARROW.create(),this::setlocation);
        warehouse_button = new Button("Warehouse",VaadinIcon.BUILDING.create(),this::warehouseaction);
        Warehouse_Manager wm = new Warehouse_Manager(PrintApplication.database);
        warehouseerror_button = new Button(wm.prepare_raport().size()+" Warehouse errors",this::warehouseerror_action);
        oldview_button = new Button("Old Layout",VaadinIcon.BACKWARDS.create(),this::oldview_action);
        create_layout();
        this.setDrawerOpened(false);
    }

    /**
     * Function for creating side menu
     */
    void create_menu(){
        reload_button.setSizeFull();
        log_button.setSizeFull();
        addprinter_button.setSizeFull();
        printerview_button.setSizeFull();
        setlocation_button.setSizeFull();
        warehouse_button.setSizeFull();

        VerticalLayout menu_layout = new VerticalLayout();

        menu_layout.add(update_button);
        menu_layout.add(reload_button);
        menu_layout.add(log_button);
        menu_layout.add(addprinter_button);
        menu_layout.add(printerview_button);
        menu_layout.add(setlocation_button);
        menu_layout.add(warehouse_button);
        menu_layout.add(oldview_button);
        menu_layout.add(new Text(PrintApplication.version + " / " + PrintApplication.build));

        menu_layout.setSizeFull();
        menu_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        menu_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        menu_layout.getStyle().set("text-align", "center");

        addToDrawer(menu_layout);
    }

    /**
     * Function for creating layout
     */
    void create_layout(){
        create_menu();
        StreamResource res = new StreamResource("icon.png", () -> {
            return Application_Menu.class.getClassLoader().getResourceAsStream("images/icon.png");
        });
        Image logo = new Image(res,"printApp");
        logo.setWidth("64px");
        logo.setHeight("64px");
        HorizontalLayout navbar_layout = new HorizontalLayout(main_toggle,logo,warehouseerror_button,new H3(PrintApplication.database.get_instance_name()));
        navbar_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        navbar_layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        navbar_layout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        addToNavbar(navbar_layout);
        this.getElement().setAttribute("theme", Lumo.DARK);
    }

    /**
     * Function for reloading page content
     * @param e
     */
    private void reloadpage_action(ClickEvent e){
        UI.getCurrent().getPage().reload();
    }

    /**
     * Function for routing to log page
     * @param e
     */
    private void showlog(ClickEvent e){
        LogView_Component lvc = new LogView_Component();
        PrintApplication.main_layout.add(lvc.main_dialog);
        lvc.main_dialog.open();
    }

    /**
     * Function for routing to the addprinter page
     * @param event
     */
    private void addprinter(ClickEvent event){
        AddPrinter_Component apc = new AddPrinter_Component();
        PrintApplication.main_layout.add(apc.dialog);
        apc.dialog.open();
    }

    /**
     * Function for routing to printer page
     * @param e
     */
    private void showprinters(ClickEvent e){
        PrintersView_Component pvc = new PrintersView_Component();
        PrintApplication.main_layout.add(pvc.dialog);
        pvc.dialog.open();
    }

    /**
     * Function for setting location
     * @param e
     */
    private void setlocation(ClickEvent e){
        SetLocation_Component slc = new SetLocation_Component();
        slc.create_dialog();
        PrintApplication.main_layout.add(slc.main_dialog);
        slc.main_dialog.open();
    }

    /**
     * Function for action opening warehouse
     * @param e
     */
    private void warehouseaction(ClickEvent e){
        WarehouseView_Component wvc = new WarehouseView_Component();
        PrintApplication.main_layout.add(wvc.main_dialog);
        wvc.main_dialog.open();
    }

    /**
     * Function for opening dialog window
     * @param e
     */
    private void warehouseerror_action(ClickEvent e){
        WarehouseErrorList_Component welc = new WarehouseErrorList_Component();
        welc.create_dialog();
        PrintApplication.main_layout.add(welc.main_dialog);
        welc.main_dialog.open();
    }

    /**
     * Function for returning to old view action
     * @param e
     */
    private void oldview_action(ClickEvent e){
        oldview_button.getUI().ifPresent(ui ->
                ui.navigate("mainview"));
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
        Notification noti_updatetoner = Notification.show("Toner data updated!");
        update_button.setText("Update");
        UI.getCurrent().getPage().reload();
    }

}
