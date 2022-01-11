/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import com.asprise.imaging.core.Imaging;
import com.asprise.imaging.core.scan.twain.Source;
import com.asprise.imaging.core.scan.twain.Source.Capability;
import java.util.List;

/**
 *
 * @author wjrfo
 */
public class TestAspireImaging {

    public static void main(String[] args) {
        Imaging imaging = new Imaging("myApp", 0);
        List<Source> sourcesNameOnly = imaging.scanListSources(true, "all", true, true);
        System.out.println("All sources with names only: \n" + sourcesNameOnly);

        List<Source> sourcesWithCaps = imaging.scanListSources(false, "all", true, true);
        System.out.println("All sources with detailed info: \n" + sourcesWithCaps);

        Source source = sourcesWithCaps.get(0);

        System.out.println("Source " + source);

        List<Source.Capability> caps = source.getCaps();

        int linCnt = 0;
        for (Source.Capability cap : caps) {

            System.out.printf("%4d  %6d %s\n     ", linCnt++, cap.getCode(), cap.getName());
        }

//        source = imaging.getSource("select", false, "all", false, false,
//                "CAP_FEEDERENABLED: 1; CAP_DUPLEXENABLED: 1");
//        List<Capability> allCaps = source.getCaps();
//        
//        for (Capability cap : allCaps) {
//            System.out.println(cap);
//        }
    }
}
