package usp.jakubwawak.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import oshi.jna.platform.mac.SystemB;
import usp.jakubwawak.database.Database_Manager;
import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.view.Printer_View;

@PageTitle("Printers")
@Route(value = "printers")

public class PrinterView extends VerticalLayout {

    MultiSelectComboBox<Printer_View> printer_combobox;
    Button return_button,search_button;
    Grid<Printer_View> printer_grid;

    TextField search_printers;
    Grid<Printer_View> main_grid;

    HorizontalLayout main_layout;

    //printer view elements
    H2 printername_label;
    H3 printerlocalization_label;

    Checkbox cyan_checkbox, magenta_checkbox, yellow_checkbox,black_checkbox,waste_containter;

    /**
     * Constructor
     */
    public PrinterView(){
        setSpacing(false);
        this.getElement().setAttribute("theme", Lumo.DARK);

        main_layout = new HorizontalLayout();
        search_printers = new TextField();
        search_printers.setPlaceholder("Search...");
        main_grid = new Grid<>(Printer_View.class,false);
        main_grid.addColumn(Printer_View::getName).setHeader("Printer Name");
        main_grid.addColumn(Printer_View::getPrinter_serialnumber).setHeader("Serial Number");
        Database_Manager dm = new Database_Manager(PrintApplication.database);

        GridListDataView<Printer_View> dataView = main_grid.setItems(dm.list_printers_objects());

        dataView.addFilter(printer ->
        {
           String searchTerm = search_printers.getValue().trim();

           if ( searchTerm.isEmpty() ){
               return true;
           }

           boolean matchesName = printer.getName().contains(searchTerm);

           boolean matchesSerial = printer.getPrinter_serialnumber().contains(searchTerm);

           return matchesName || matchesSerial;
        });

        printername_label = new H2();
        printerlocalization_label = new H3();

        cyan_checkbox = new Checkbox("Cyan Toner");
        magenta_checkbox = new Checkbox("Magenta Toner");
        yellow_checkbox = new Checkbox("Yellow Toner");
        black_checkbox = new Checkbox("Black Toner");
        waste_containter = new Checkbox("Waste Container");

        prepare_view();
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    /**
     * Function for preparing view
     */
    void prepare_view(){
        VerticalLayout left_wing = new VerticalLayout();
        left_wing.add(search_printers);
        left_wing.add(main_grid);

        main_layout.add(left_wing);
        add(main_layout);
    }

    /**
     * Function for preparing details view
     * @param printer_id
     */
    void prepare_details_view(int printer_id){
        this.removeAll();
        prepare_view();
        VerticalLayout right_grid = new VerticalLayout();

        if ( printer_id > 0 )
        {
            //create view

        }
        else
        {
            //set everything to setVisible(false)
        }

        main_layout.add(right_grid);
    }


}
