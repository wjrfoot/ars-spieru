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

//public class TestScanNoDialog {
public class TestScan {

   public static void main(String[] args) throws IOException {
      Imaging imaging = new Imaging("myApp", 0);
      Result result = imaging.scan(Request.fromJson(
              
         "{"
//         + "\"twain_cap_setting\" : [{ "
// //   "ICAP_PIXELTYPE" : "TWPT_RGB", // Color
//         + " \"ICAP_XRESOLUTION\" : \"100\", "// DPI: 100
//         + " \"ICAP_YRESOLUTION\" : \"100\", "
// //   "ICAP_SUPPORTEDSIZES" : "TWSS_USLETTER" // Paper size: TWSS_USLETTER, TWSS_A4, ...
//         + "}],"
         + "\"output_settings\" : [ {"
         + "  \"type\" : \"save\","
         + "  \"format\" : \"jpg\","
         + "  \"save_path\" : \"${TMP}\\\\${TMS}${EXT}\""
         + "} ]"
       + "}"), "select", false, false);

      System.out.println(result == null ? "(null)" : result.toJson(true));
   }
}

