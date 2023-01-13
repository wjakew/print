package usp.jakubwawak.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
import usp.jakubwawak.views.Application_Menu;
import usp.jakubwawak.views.PrinterView;
import usp.jakubwawak.views.components.TonerDetails_Component;

import java.util.ArrayList;

@PageTitle("printapp")
@Route(value = "/", layout = Application_Menu.class)
public class HomeView extends VerticalLayout {

    Grid<Printer_View> grid;
    TonerPrinter_View tpv;

    Button update_button;

    H3 update_time_label;

    /**
     * Constructor
     */
    public HomeView(){
        PrintApplication.main_layout = this;
        setSpacing(false);
        setSizeFull();
        getElement().setAttribute("theme", Lumo.DARK);
        tpv = new TonerPrinter_View(PrintApplication.database);
        tpv.load_view();
        grid = new Grid<>(Printer_View.class,false);
        update_time_label = new H3("Last update: "+tpv.list_view.get(0).getLastUpdate());
        update_button = new Button("", VaadinIcon.DOWNLOAD.create(), this::update);

        grid.addColumn(Printer_View::getID).setHeader("AtmosferaID");
        grid.addColumn(Printer_View::getName).setHeader("Nazwa drukarki").setResizable(true);
        grid.addColumn(Printer_View::getLocalization).setHeader("Lokalizacja");
        grid.addColumn(Printer_View::getIP).setHeader("Adres IP");
        grid.addColumn(Printer_View::getCyan).setHeader("Cyan");
        grid.addColumn(Printer_View::getMagenta).setHeader("Magenta");
        grid.addColumn(Printer_View::getYellow).setHeader("Yellow");
        grid.addColumn(Printer_View::getBlack).setHeader("Black");
        ArrayList<Printer_View> view = tpv.list_view;
        grid.setItems(view);
        grid.addItemClickListener(e -> {
            Printer_View pv = e.getItem();
            if ( pv != null ){
                show_tonerdetailsdialog(pv.printer_id);
            }});
        HorizontalLayout tonerdata_layout = new HorizontalLayout(update_time_label);
        tonerdata_layout.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
        add(tonerdata_layout);
        add(update_button);
        add(grid);
        add(new H6("By Jakub Wawak / kubawawak@gmail.com / j.wawak@usp.pl"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
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
