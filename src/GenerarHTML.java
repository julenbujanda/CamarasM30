import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class GenerarHTML {

    private static String ruta = "./camaras.xml";

    private static Document leerXML() {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = null;
        try {
            document = saxBuilder.build(new File(ruta));
            parsearXML(document);
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    private static LinkedList<Camara> parsearXML(Document document) {
        Element element = document.getRootElement();
        List<Element> camarasXML = element.getChildren("Camara");
        LinkedList<Camara> camaras = new LinkedList<>();
        for (Element camara : camarasXML) {
            List<Element> posicion = camara.getChildren("Posicion");
            String latitud = posicion.get(0).getChild("Latitud").getText();
            String longitud = posicion.get(0).getChild("Longitud").getText();
            String url = camara.getChild("URL").getText();
            camaras.add(new Camara(latitud, longitud, url));
        }
        return camaras;
    }

    private static String generarHTML(LinkedList<Camara> camaras) {
        // language=HTML
        String html = "<html>\n" +
                "<body>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <style>\n" +
                "        /* Set the size of the div element that contains the map */\n" +
                "        .map {\n" +
                "            height: 187.5px; /* The height is 400 pixels */\n" +
                "            width: 250px; /* The width is the width of the web page */\n" +
                "        }\n" +
                "body {\n" +
                "    font-family: \"Verdana\", Times, sans-serif;\n" +
                "}" +
                "td {\n" +
                "    text-align: center;\n" +
                "}" +
                "    </style>\n" +
                "</head>\n" +
                "<table align='center'>\n" +
                "<tr><thead><td>Cámara</td><td>Localización</td></thead><tbody></tr>";
        int i = 0;
        String initMap = "function initMap() {";
        for (Camara camara : camaras) {
            // language=HTML
            html += "<tr><td><img src=\"http://" + camara.getImagen() + "\" height=\"187.5\" width=\"250\"></td>" +
                    "<td><div id=\"map" + i + "\" class=\"map\"></div>\n</td></tr>";
            initMap += "var location" + i + " = {lat: " + camara.getLatitud() + ", lng: " + camara.getLongitud() + "};\n" +
                    "var map" + i + " = new google.maps.Map(\n" +
                    "document.getElementById('map" + i + "'), {zoom: 4, center: location" + i + ", zoom: 13});\n" +
                    "var marker" + i + " = new google.maps.Marker({position: location" + i + ", map: map" + i + "});";
            i++;
        }
        initMap += "}";
        // language=HTML
        html += "</tbody></table>\n" +
                "<script>" + initMap + "</script>" +
                "<script async defer\n" +
                "        src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyDuX6StS8fbUJnw904k5EWD_PTeBnFw-d4&callback=initMap\">\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";
        return html;
    }

    private static void generarFichero(String html_pagina) {
        String nombre_fichero = "./camaras.html";
        try {
            FileWriter fw = new FileWriter(nombre_fichero, false);
            fw.write(html_pagina);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        generarFichero(generarHTML(parsearXML(leerXML())));
    }

}
