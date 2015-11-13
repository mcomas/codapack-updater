/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package codapackupdater;


import codapackupdater.json.JSONException;
import codapackupdater.json.JSONObject;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mcomas
 */
public class LocalConfiguration {
    /*
     * Decimal configuration output
     */
    public static String HTTP_ROOT = "http://ima.udg.edu/codapack/versioning/";

    public static String CoDaVersion = "10 01 4";
    public static int CoDaUpdaterVersion = 4;

    public static int[] getVersionNum(){
        int num[] = new int[3];
        String[] version = CoDaVersion.split(" ");
        num[0] = Integer.parseInt(version[0]);
        num[1] = Integer.parseInt(version[1]);
        num[2] = Integer.parseInt(version[2]);
        return num;
    }
    public static String getVersion(){
        String[] version = CoDaVersion.split(" ");
        return version[0] + "." + version[1] + "." + version[2];
    }
    public static boolean updateNeeded(String v){
        int[] localVersion = getVersionNum();

        String[] serverVersion = v.split(" ");
        boolean b = false;
        if(localVersion[0] < Integer.parseInt(serverVersion[0])) b = true;
        else if (localVersion[1] < Integer.parseInt(serverVersion[1])) b = true;
        else if (localVersion[2] < Integer.parseInt(serverVersion[2])) b = true;

        return b;
    }
    private final static DecimalFormatSymbols decimalFormatSymb = new DecimalFormatSymbols();
    static{
        decimalFormatSymb.setDecimalSeparator('.');
    }
    private static char decimalFormat = '.';
    private static String decimalOutputFormat = "0.0000";
    private static String decimalTableFormat = "0.00";
    private static String decimalExportFormat = "0.00########";
    //private static DecimalFormat decimalOutputFormat = new DecimalFormat("0.0000", decimalFormat);
    //private static DecimalFormat decimalOutputFormat = new DecimalFormat("##0.##E0", decimalFormat);
    //private static DecimalFormat decimalTableFormat = new DecimalFormat("0.00", decimalFormat);
    //private static DecimalFormat decimalTableFormat = new DecimalFormat("##0.##E0", decimalFormat);
    //private static DecimalFormat decimalExportFormat = new DecimalFormat("0.00########");

    public static void setDecimalFormat(char f){
        decimalFormat = f;
        decimalFormatSymb.setDecimalSeparator(f);
    }
    public static char getDecimalFormat(){
        return decimalFormat;
    }
    public static void setDecimalOutputFormat(String df){
        decimalOutputFormat = df;
    }
    public static DecimalFormat getDecimalOutputFormat(){ 
        return new DecimalFormat(decimalOutputFormat, decimalFormatSymb);
    }
    public static void setDecimalTableFormat(String df){
        decimalTableFormat = df;
    }
    public static DecimalFormat getDecimalTableFormat(){
        return new DecimalFormat(decimalTableFormat, decimalFormatSymb);
    }
    public static void setDecimalExportFormat(String df){
        decimalExportFormat = df;
    }
    public static DecimalFormat getDecimalExportFormat(){ 
        return new DecimalFormat(decimalExportFormat);
    }

    private static Color ouputColor = new Color(162,193,215);
    public static void setOutputColor(Color c){ ouputColor = c; }
    public static Color getOutputColor(){ return ouputColor; }

    public static void saveConfiguration() throws FileNotFoundException, JSONException{
        JSONObject configuration = new JSONObject();

        configuration.put("codapack-version", CoDaVersion);
        configuration.put("codapack-updater-version", CoDaUpdaterVersion);

        configuration.put("decimal-format", decimalFormat);
        configuration.put("decimal-output", decimalOutputFormat);
        configuration.put("decimal-table", decimalTableFormat);
        configuration.put("decimal-export", decimalExportFormat);

        PrintWriter printer = new PrintWriter("codapack.conf");
        configuration.write(printer);
        printer.close();
    }
    public static void loadConfiguration(){
        try {
            FileReader file = null;
            JSONObject configuration;
            file = new FileReader("codapack.conf");
            BufferedReader br = new BufferedReader(file);
            configuration = new JSONObject(br.readLine());
            file.close();

            CoDaVersion = configuration.getString("codapack-version");
            CoDaUpdaterVersion = configuration.getInt("codapack-updater-version");

            decimalFormat = (char) configuration.getInt("decimal-format");
            decimalOutputFormat = configuration.getString("decimal-output");
            decimalTableFormat = configuration.getString("decimal-table");
            decimalExportFormat = configuration.getString("decimal-export");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LocalConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LocalConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }catch (JSONException ex) {
            Logger.getLogger(LocalConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
