package util;

import java.awt.*;
import java.net.URI;

public class Browser {
    public static void browseUrl(String url){
        try {

                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                } else {
                    System.out.println("Браузер не поддерживается");
                }

        } catch (Exception e) {
            System.out.println("Некорректный url");
        }
    }
}
