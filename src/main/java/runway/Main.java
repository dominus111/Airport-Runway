package runway;

import runway.Controller.*;
import runway.Model.*;

public class Main {

    public static void main(String[] args) {
//        runway.View.GUI.main(args);

        var airport = new Airport();
        var xmle = new XMLExport();

        var xml = xmle.exportXML(airport);
        xmle.printDoc(xml);
    }
}
