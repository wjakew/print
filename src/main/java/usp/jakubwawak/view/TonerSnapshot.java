/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.view;

/**
 * Class for representing toner snapshot data
 */
public class TonerSnapshot {
    public String time;
    public String toner_data;
    float cyan;
    float magenta;
    float yellow;
    float black;

    public TonerSnapshot(String time, String toner_data) {
        this.time = time;
        this.toner_data = toner_data;
    }

    /**
     * Function for adding details to TonerSnapshot object
     * @param cyan
     * @param magenta
     * @param yellow
     * @param black
     */
    public void add_details(float cyan,float magenta,float yellow, float black){
        this.cyan = cyan;
        this.magenta = magenta;
        this.yellow = yellow;
        this.black = black;
    }

    public String getTime() {
        return time;
    }

    public String getToner_data() {
        return toner_data;
    }

    public String getCyan(){
        return Float.toString(cyan);
    }

    public String getMagenta(){
        return Float.toString(magenta);
    }

    public String getYellow(){
        return Float.toString(yellow);
    }

    public String getBlack(){
        return Float.toString(black);
    }
}
