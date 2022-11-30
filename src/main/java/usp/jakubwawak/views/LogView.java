/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.Lumo;
import usp.jakubwawak.Maintanance.LogObject;
import usp.jakubwawak.print.PrintApplication;

@PageTitle("Printer View")
@Route(value = "history")
public class LogView extends VerticalLayout {

    Grid<LogObject> log_view;

    Button gohome_button;

    /**
     * Constructor
     */
    public LogView(){
        setSpacing(false);
        this.getElement().setAttribute("theme", Lumo.DARK);
        log_view = new Grid<>(LogObject.class,false);
        gohome_button = new Button(VaadinIcon.HOME.create(),this::gohome_action);
        prepare_view();
        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    /**
     * Function for routing to home page
     * @param e
     */
    private void gohome_action(ClickEvent e){
        gohome_button.getUI().ifPresent(ui ->
                ui.navigate("mainview"));
    }

    /**
     * Function for preparing view
     */
    void prepare_view(){
        log_view.addColumn(LogObject::getID).setHeader("ID");
        log_view.addColumn(LogObject::get_time).setHeader("Time");
        log_view.addColumn(LogObject::get_code).setHeader("Code");
        log_view.addColumn(LogObject::get_desc).setHeader("Information");

        log_view.setItems(PrintApplication.database.load_log());

        add(gohome_button);
        add(new H1("Action History"));
        add(log_view);

    }


}
