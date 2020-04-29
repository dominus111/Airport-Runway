package runway.Controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import runway.Model.*;

import static org.junit.jupiter.api.Assertions.*;


public class XMLTest {

    @DisplayName("XML Tests")
    @Tag("fast")
    @Test
    public void testXML() {
        var xmle = new XMLExport();
        var xmli = new XMLImport();
        var airport = new Airport();

        var doc = xmle.exportXML(airport);
        try {
            var ap = xmli.parseAirport(doc);

            assertEquals(true, ap.isSame(airport));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
