/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import com.asprise.imaging.core.Imaging;

/**
 *
 * @author wjrfo
 */
//public class TestScan {
//  public static void main( String[] args ) {
//    Imaging.main(null);
//  }
//}
import java.io.IOException;

import com.asprise.imaging.core.Imaging;
import com.asprise.imaging.core.Request;
import com.asprise.imaging.core.Result;
import com.asprise.imaging.core.scan.twain.TwainConstants;

//public class TestScanNoDialog {
public class TestScan1 {

    private static final String DIR = "\"${TMP}\\\\${TMS}${EXT}\"";

    public static void main(String[] args) throws IOException {
        Imaging imaging = new Imaging("myApp", 0);

        System.out.println(DIR);
        System.out.println(System.getProperty("user.dir"));

        String dir = System.getProperty("user.dir");
        
        Request request = Request.fromJson("{"
                + "\"output_settings\" : [ {"
                + "  \"type\" : \"save\","
                + "  \"format\" : \"jpg\","
//                + "  \"save_path\" : " + DIR
                + "  \"save_path\" : " + dir + "\\${TMS}${EXT}\""
                + "} ]"
                //         + ","
                //                   
                //         + "\"image_info\" : [ {" 
                //         + "  \"YResolution\" : \"6400\" "
                //         + "  \"XResolution\" : \"6400\" "
                //         + "} ]"

                + "}");

//       System.out.println(request.getI18n());
//        request.setTwainCap(TwainConstants.ICAP_XRESOLUTION, "1200", false);
//        request.setTwainCap(TwainConstants.ICAP_YRESOLUTION, "1200", false);

        Result result = imaging.scan(request, "select", false, false);

//      System.out.println(result == null ? "(null)" : result.toJson(true));
    }
}
