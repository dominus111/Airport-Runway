package runway.Controller;

import javax.print.Doc;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;
import runway.Model.*;

public class XMLExport {
    DocumentBuilderFactory dbf;
    DocumentBuilder db;
    TransformerFactory tf;
    Transformer t;

    public XMLExport() {
        try {
            dbf = DocumentBuilderFactory.newDefaultInstance();
            db = dbf.newDocumentBuilder();
            tf = TransformerFactory.newDefaultInstance();
            t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Document exportXML(Airport airport) {
        var doc = db.newDocument();
        Element root = doc.createElement("Airport");

        root.appendChild(getElement(doc, root, "Name", airport.getName()));
        doc.appendChild(root);

        Element runways = doc.createElement("Runways");
        root.appendChild(runways);

        for (var runway : airport.getObservableRunwayList()) {
            runways.appendChild(getRunway(doc, runway));
        }

        return doc;
    }


    Element getRunway(Document doc, Runway runway) {
        var root = doc.createElement("Runway");

        var leftRunway = doc.createElement("LeftRunway");
        leftRunway.appendChild(getVirtualRunway(doc, runway.getLeftRunway()));
        root.appendChild(leftRunway);

        var rightRunway = doc.createElement("RightRunway");
        rightRunway.appendChild(getVirtualRunway(doc, runway.getRightRunway()));
        root.appendChild(rightRunway);

        return root;
    }

    Element getVirtualRunway(Document doc, VirtualRunway virtualRunway) {
        var root = doc.createElement("VirtualRunway");
        root.appendChild(getElement(doc, root, "designator", virtualRunway.getDesignator()));
        root.appendChild(getRunwayParameters(doc, virtualRunway.getInitialParameters()));
        return root;
    }

    Element getRunwayParameters(Document doc, RunwayParameters rp) {
        var root = doc.createElement("RunwayParameters");
        root.appendChild(getElement(doc, root, "tora", rp.getTora()));
        root.appendChild(getElement(doc, root, "toda", rp.getToda()));
        root.appendChild(getElement(doc, root, "asda", rp.getAsda()));
        root.appendChild(getElement(doc, root, "lda", rp.getLda()));
        root.appendChild(getElement(doc, root, "displacedThreshold", rp.getdispTHR()));
        return root;
    }

    Element getElement(Document doc, Element element, String name, String value) {
        var root = doc.createElement(name);
        root.appendChild(doc.createTextNode(value));
        return root;
    }
    Element getElement(Document doc, Element element, String name, Double value) {
        var root = doc.createElement(name);
        root.appendChild(doc.createTextNode(Double.toString(value)));
        return root;
    }

    public void printDoc(Document doc) {
        try {
            var ds = new DOMSource(doc);
            var c = new StreamResult(System.out);
            t.transform(ds, c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void printDoc(Node doc) {
        try {
            var ds = new DOMSource(doc);
            var c = new StreamResult(System.out);
            t.transform(ds, c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
