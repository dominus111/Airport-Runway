package runway.Controller;

import org.w3c.dom.*;
import runway.Model.*;

import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.File;
import java.net.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;
import java.util.stream.*;
import java.util.zip.*;

public class XMLImport {
    DocumentBuilderFactory dbf;
    DocumentBuilder db;
    XPathFactory xpf;
    XPath xp;

    public XMLImport() {
        try {
            dbf = DocumentBuilderFactory.newDefaultInstance();
            db = dbf.newDocumentBuilder();
            xpf = XPathFactory.newDefaultInstance();
            xp = xpf.newXPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Airport parseAirport(Document doc) throws Exception {
        doc.normalize();
        var name = xp.compile("(/Airport/Name)[1]").evaluate(doc);

        var airport = new Airport(name);

        var runways = (NodeList) xp.compile("/Airport/Runways/Runway").evaluate(doc, XPathConstants.NODESET);

        for (var i=0; i < runways.getLength(); i++) {
            var runway = parseRunway(runways.item(i));
            airport.addRunway(runway);
        }
        return airport;
    }

    public Airport parseAirportFile(Path path) throws Exception{
        var is = Files.newInputStream(path);
        var doc = db.parse(is);
        doc.normalize();
        var name = xp.compile("(/Airport/Name)[1]").evaluate(doc);

        var airport = new Airport(name);

        var runways = (NodeList) xp.compile("/Airport/Runways/Runway").evaluate(doc, XPathConstants.NODESET);

        for (var i=0; i < runways.getLength(); i++) {
            var runway = parseRunway(runways.item(i));
            airport.addRunway(runway);
        }
        return airport;
    }

    public Obstacle parseObstacleFile(Path path) throws Exception {
        var is = Files.newInputStream(path);
        var doc = db.parse(is);
        doc.normalize();
        var name = xp.compile("//Name").evaluate(doc);
        var height = Double.parseDouble(xp.compile("//Height").evaluate(doc));
        var obstacle = new Obstacle(name, height);

        return obstacle;
    }

    Runway parseRunway(Node node) throws Exception{
        var leftRunway = (Element) xp.compile("(.//LeftRunway[1]/VirtualRunway[1])").evaluate(node, XPathConstants.NODE);
        var rightRunway = (Element) xp.compile("(.//RightRunway[1]/VirtualRunway[1])").evaluate(node, XPathConstants.NODE);

        return new Runway(parseVirtualRunway(leftRunway), parseVirtualRunway(rightRunway));
    }

    VirtualRunway parseVirtualRunway(Element node) throws Exception {
        var designator = xp.compile(".//designator[1]").evaluate(node);
        var parameters = (Element) xp.compile(".//RunwayParameters[1]").evaluate(node, XPathConstants.NODE);
        var tora = Double.parseDouble(xp.compile(".//tora").evaluate(node));
        var toda = Double.parseDouble(xp.compile(".//toda").evaluate(node));
        var asda = Double.parseDouble(xp.compile(".//asda").evaluate(node));
        var lda = Double.parseDouble(xp.compile(".//lda").evaluate(node));
        var dispTHR = Double.parseDouble(xp.compile(".//displacedThreshold").evaluate(node));
        return new VirtualRunway(designator, new RunwayParameters(tora, toda, asda, lda, dispTHR));
    }

    public void readFile (Path path) {
        try {
            System.out.println(path);
            System.out.println(new String(Files.readAllBytes(path)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Airport> getAirports() {
        try {
            URI uri = getClass().getResource("/airports/").toURI();
            Path myPath;
            FileSystem fileSystem = null;
            List<Airport> airports;
            if (uri.getScheme().equals("jar")) {
                fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
                myPath = fileSystem.getPath("/airports/");
            } else {
                myPath = Paths.get(uri);
            }

            airports = Files.list(myPath).map(file -> {
                try {
                    return parseAirportFile(file);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());

            if (uri.getScheme().equals("jar")) {
                fileSystem.close();
            }

            return airports;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Obstacle> getObstacles() {
        try {
            URI uri = getClass().getResource("/obstacles/").toURI();
            Path myPath;
            FileSystem fileSystem = null;
            List<Obstacle> obstacles;
            if (uri.getScheme().equals("jar")) {
                fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
                myPath = fileSystem.getPath("/obstacles/");
            } else {
                myPath = Paths.get(uri);
            }

            obstacles = Files.list(myPath).map(file -> {
                try {
                    return parseObstacleFile(file);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());

            if (uri.getScheme().equals("jar")) {
                fileSystem.close();
            }

            return obstacles;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
