/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import usp.jakubwawak.database.Database_Manager;
import usp.jakubwawak.database.Database_Warehouse;
import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.view.Printer_View;
import usp.jakubwawak.views.components.PrinterDetails_Component;

@PageTitle("Printers")
@Route(value = "printers")

public class PrinterView extends VerticalLayout {

    TextField search_printers;
    Grid<Printer_View> main_grid;
    Button return_button;

    HorizontalLayout checkbox_layout;

    //printer view elements
    H2 printername_label;
    H3 printerlocalization_label;

    Checkbox cyan_checkbox, magenta_checkbox, yellow_checkbox,black_checkbox,waste_containter;

    String tonervalues_old;

    /**
     * Constructor
     */
    public PrinterView(){
        setSpacing(false);
        this.getElement().setAttribute("theme", Lumo.DARK);

        tonervalues_old = "00000";
        checkbox_layout = new HorizontalLayout();
        search_printers = new TextField();
        search_printers.setPlaceholder("Search...");
        main_grid = new Grid<>(Printer_View.class,false);
        main_grid.addColumn(Printer_View::getName).setHeader("Printer Name");
        main_grid.addColumn(Printer_View::getPrinter_serialnumber).setHeader("Serial Number");
        return_button = new Button("",VaadinIcon.HOME.create(),this::returnhome_action);
        Database_Manager dm = new Database_Manager(PrintApplication.database);

        GridListDataView<Printer_View> dataView = main_grid.setItems(dm.list_printers_objects());
        search_printers.addValueChangeListener(e -> dataView.refreshAll());
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
        prepare_details_view(0);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");

        main_grid.setSelectionMode(Grid.SelectionMode.NONE);
        main_grid.addItemClickListener(e -> {
                    Printer_View pv = e.getItem();
                    if ( pv != null ){
                        prepare_details_view(pv.printer_id);
                    }});
    }

    /**
     * Function for preparing view
     */
    void prepare_view(){
        add(return_button);
        add(new H1("Printers"));
        add(search_printers);
        add(main_grid);
        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    /**
     * Function for preparing details view
     * @param printer_id
     */
    void prepare_details_view(int printer_id){
        if ( printer_id > 0 ) {
            PrinterDetails_Component pdc = new PrinterDetails_Component(printer_id);
            pdc.create_dialog();
            add(pdc.main_dialog);
            pdc.main_dialog.open();
        }
    }

    /**
     * Function for returning to home page
     * @param e
     */
    private void returnhome_action(ClickEvent e){
        return_button.getUI().ifPresent(ui ->
                ui.navigate("mainview"));
    }
}
