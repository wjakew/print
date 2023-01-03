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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import usp.jakubwawak.database.Database_Manager;
import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.view.Printer_View;

@PageTitle("Printers")
@Route(value = "printers")

public class PrinterView extends VerticalLayout {

    TextField search_printers;
    TextArea log_area;
    Grid<Printer_View> main_grid;
    Button return_button, update_button;

    HorizontalLayout main_layout;
    VerticalLayout left_wing,right_wing;

    HorizontalLayout checkbox_layout;

    //printer view elements
    H2 printername_label;
    H3 printerlocalization_label;

    int printer_id;

    Checkbox cyan_checkbox, magenta_checkbox, yellow_checkbox,black_checkbox,waste_containter;

    /**
     * Constructor
     */
    public PrinterView(){
        setSpacing(false);
        this.getElement().setAttribute("theme", Lumo.DARK);

        main_layout = new HorizontalLayout();
        main_layout.setSizeFull();
        checkbox_layout = new HorizontalLayout();
        search_printers = new TextField();
        search_printers.setPlaceholder("Search...");
        main_grid = new Grid<>(Printer_View.class,false);
        main_grid.addColumn(Printer_View::getName).setHeader("Printer Name");
        main_grid.addColumn(Printer_View::getPrinter_serialnumber).setHeader("Serial Number");
        log_area = new TextArea();
        update_button = new Button("",VaadinIcon.PENCIL.create());
        return_button = new Button("",VaadinIcon.HOME.create(),this::returnhome_action);
        log_area.setWidth("400px");
        log_area.setHeight("300px");
        log_area.setEnabled(false);
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
        left_wing = new VerticalLayout();
        right_wing = new VerticalLayout();
        search_printers.setSizeFull();

        //left wing
        left_wing.setSpacing(false);
        left_wing.setAlignItems(Alignment.CENTER);
        main_layout.setJustifyContentMode(JustifyContentMode.CENTER);
        left_wing.add(search_printers);
        left_wing.add(main_grid);

        //right wing
        printername_label.setText("");
        right_wing.add(printername_label);
        printerlocalization_label.setText("");
        right_wing.add(printerlocalization_label);
        checkbox_layout.add(cyan_checkbox,magenta_checkbox,yellow_checkbox,black_checkbox,waste_containter,update_button);
        right_wing.add(checkbox_layout);
        right_wing.add(log_area);

        //setting all setVisible(false)
        right_wing.setVisible(false);
        checkbox_layout.setVisible(false);
        log_area.setVisible(false);
        right_wing.setAlignItems(Alignment.CENTER);

        main_layout.add(left_wing);
        main_layout.add(right_wing);

        add(main_layout);
    }

    /**
     * Function for preparing details view
     * @param printer_id
     */
    void prepare_details_view(int printer_id){
        if ( printer_id > 0 )
        {
            this.printer_id = printer_id;
            //create view
            Printer_View printer = new Printer_View(printer_id);
            printer.load_warehouse_data();
            printer.get_warehouse_status();

            printername_label.setText(printer.printer_name);
            if (printer.printer_localization == null){
                printerlocalization_label.setText("Brak lokalizacji");
            }
            else{
                printerlocalization_label.setText(printer.printer_localization);
            }

            String warehouse = printer.get_warehouse_status();

            if ( warehouse.contains("CYAN") ){
                cyan_checkbox.setValue(true);
            }
            else{
                cyan_checkbox.setValue(false);
            }
            if ( warehouse.contains("MAGENTA") ){
                magenta_checkbox.setValue(true);
            }
            else{
                magenta_checkbox.setValue(false);
            }
            if ( warehouse.contains("YELLOW") ){
                yellow_checkbox.setValue(true);
            }
            else{
                yellow_checkbox.setValue(false);
            }
            if ( warehouse.contains("BLACK") ){
                black_checkbox.setValue(true);
            }
            else{
                black_checkbox.setValue(false);
            }
            if ( warehouse.contains("WASTE") ){
                waste_containter.setValue(true);
            }
            else{
                waste_containter.setValue(false);
            }
            checkbox_layout.setEnabled(false);
            log_area.setValue(printer.get_printer_logs());
        }
        right_wing.setVisible(true);
        checkbox_layout.setVisible(true);
        log_area.setVisible(true);
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
