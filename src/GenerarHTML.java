import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;

public class GenerarHTML {

    private static String ruta = "./camaras.xml";

    private static void leerXML() {
        SAXBuilder saxBuilder = new SAXBuilder();
        try {
            Document document = saxBuilder.build(new File(ruta));

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generarHTML() {
        // language=HTML
        String html = "<body>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "</body>";
        return html;
    }

    public static void main(String[] args) {

    }

}
