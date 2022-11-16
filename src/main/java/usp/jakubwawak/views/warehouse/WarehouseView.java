/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.views.warehouse;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import usp.jakubwawak.database.Database_Manager;
import usp.jakubwawak.database.Database_Warehouse;
import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.view.Printer_View;
import usp.jakubwawak.view.Warehouse_View;

@PageTitle("Warehouse")
@Route(value = "warehouse")
public class WarehouseView extends VerticalLayout {


    Button addelement_button,removeelement_button,return_button;
    ComboBox printer_list,templates_list;
    Grid<Printer_View> grid;
    Warehouse_View wv;


    /**
     * Constructor
     */
    public WarehouseView(){
        setSpacing(false);
        this.getElement().setAttribute("theme", Lumo.DARK);

        wv = new Warehouse_View(PrintApplication.database);
        addelement_button = new Button(VaadinIcon.PLUS.create(),this::addElement);
        return_button = new Button(VaadinIcon.HOME.create(),this::gohome);
        removeelement_button = new Button(VaadinIcon.TRASH.create(),this::removeElement);
        Database_Manager dm = new Database_Manager(PrintApplication.database);
        printer_list = new ComboBox();
        printer_list.setItems(dm.list_printers());
        Database_Warehouse dw = new Database_Warehouse(PrintApplication.database);
        templates_list = new ComboBox();
        templates_list.setItems(dw.list_warehouse_templates());


        prepare_view();

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    /**
     * Function for routing to home
     * @param e
     */
    private void gohome(ClickEvent e){
        return_button.getUI().ifPresent(
                ui -> ui.navigate("mainview"));
    }

    /**
     * Function for adding warehouse element to database
     * @param e
     */
    private void addElement(ClickEvent e){
        Database_Warehouse dw = new Database_Warehouse(PrintApplication.database);
        String printer_name = printer_list.getValue().toString();
        String template_name = templates_list.getValue().toString();
        if ( dw.check_element(printer_name,template_name) == 1){
            if ( !printer_name.equals("") && !template_name.equals("") ){

                int ans = dw.add_element(printer_name,template_name);
                if ( ans == 1 ){
                    Notification addelement_noti = Notification.show("Warehouse updated");
                    UI.getCurrent().getPage().reload();
                }
                else{
                    Notification addelement_noti = Notification.show("Database error!");
                }
            }
            else{
                Notification addelement_noti = Notification.show("Wrong selection");
            }
        }
        else{
            Notification addelement_noti = Notification.show("Warehouse has this toner already!");
        }

    }


    /**
     * Function for removing element
     * @param e
     */
    private void removeElement(ClickEvent e){
        Database_Warehouse dw = new Database_Warehouse(PrintApplication.database);
        String printer_name = printer_list.getValue().toString();
        String template_name = templates_list.getValue().toString();
        if (dw.check_element(printer_name, template_name) == 1) {
            if ( !printer_name.equals("") && !template_name.equals("") ){
                int ans = dw.remove_element(printer_name,template_name);
                if ( ans == 1 ){
                    Notification addelement_noti = Notification.show("Warehouse updated");
                    UI.getCurrent().getPage().reload();
                }
                else{
                    Notification addelement_noti = Notification.show("Database error!");
                }
            }
            else{
                Notification addelement_noti = Notification.show("Wrong selection");
            }
        }
        else{
            Notification addelement_noti = Notification.show("Object didn't exist in warehouse!");
        }

    }

    /**
     * Function for preparing view
     */
    void prepare_view(){
        add(return_button);
        add(new H1("Warehouse"));
        HorizontalLayout hl = new HorizontalLayout(printer_list,templates_list,addelement_button,removeelement_button);
        add(hl);
        prepare_warehouse();
    }

    /**
     * Function for preparing warehouse data
     */
    void prepare_warehouse(){
        wv.load_view();

        grid = new Grid<>(Printer_View.class,false);

        grid.addColumn(Printer_View::getName).setHeader("Printer Name");
        grid.addColumn(Printer_View::get_warehouse_status).setHeader("Warehouse status");

        grid.setItems(wv.printers);
        add(grid);
    }

}
