import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedList;
import java.util.List;

public class GenerarHTML {

    private static Document leerXML() {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = null;
        try {
            URL url = new URL("http://www.mc30.es/components/com_hotspots/datos/camaras.xml");
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("./camaras.xml");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            System.out.println("No se ha podido descargar el archivo.");
        }
        try {
            document = saxBuilder.build(new File("./camaras.xml"));
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
            Element posicion = camara.getChild("Posicion");
            String latitud = posicion.getChild("Latitud").getText();
            String longitud = posicion.getChild("Longitud").getText();
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
                "        .map {\n" +
                "            height: 187.5px;\n" +
                "            width: 250px;\n" +
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
                "<thead><tr><td>Cámara</td><td>Localización</td></tr></thead><tbody><tr>";
        int i = 0;
        String initMap = "function initMap() {\n";
        for (Camara camara : camaras) {
            // language=HTML
            html += "<tr><td><img src=\"http://" + camara.getImagen() + "\" height=\"187.5\" width=\"250\"></td>" +
                    "<td><div id=\"map" + i + "\" class=\"map\"></div>\n</td></tr>";
            initMap += "var location" + i + " = {lat: " + camara.getLatitud() + ", lng: " + camara.getLongitud() + "};\n" +
                    "var map" + i + " = new google.maps.Map(\n" +
                    "document.getElementById('map" + i + "'), {center: location" + i + ", zoom: 13});\n" +
                    "var marker" + i + " = new google.maps.Marker({position: location" + i + ", map: map" + i + "});\n";
            i++;
        }
        initMap += "}";
        // language=HTML
        html += "</tbody></table>\n" +
                "<script>" + initMap + "</script>\n" +
                "<script async defer src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyDuX6StS8fbUJnw904k5EWD_PTeBnFw-d4&callback=initMap\">\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";
        return html;
    }

    private static void generarFichero(String html_pagina) {
        try {
            FileWriter fw = new FileWriter("./camaras.html", false);
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
