import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Browser;
import util.DataManager;
import util.LinkManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//После реализации всех функций вам нужно провести тестирование. Вот ключевые моменты для проверки:
//
//    Убедитесь, что одна и та же ссылка, сокращенная разными пользователями, генерирует уникальные короткие ссылки.✅
//    Проверьте, что при исчерпании лимита переходов переход по ссылке блокируется.✅
//    Тестируйте удаление ссылок по истечении срока жизни. Попробуйте задать различные временные интервалы (например, час или сутки) и убедитесь, что система корректно удаляет «протухшие» ссылки.
//    Убедитесь, что пользователи получают уведомления о том, что их ссылка недоступна из-за исчерпания лимита или истечения срока жизни.

public class AppTest {

    static ByteArrayOutputStream out = new ByteArrayOutputStream();
    static ByteArrayInputStream in;

    static {
        System.setOut(new PrintStream(out));
    }

    public static void setConsoleInput(String input) {
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }

    @BeforeEach
    public void cleanConsole() {
        out.reset();
    }

    @Test
    public void testBrowser() {
        //Некорректный урл
        Browser.browseUrl("123");
        assertTrue(out.toString().contains("Некорректный url"));
        cleanConsole();
        //Корректный урл
        Browser.browseUrl("https://mvnrepository.com");
        assertFalse(out.toString().contains("Некорректный url"));
        cleanConsole();
        assertFalse(out.toString().contains("Браузер не поддерживается"));
    }

    @Test
    public void testLinkManager() {
        //проверка правильности генерации ссылки
        String testLink = LinkManager.generateLink();
        assertTrue(testLink.startsWith("clck.ru/"));
        assertEquals(testLink.length(), "clck.ru/".length() + 6);
        String testLink1 = LinkManager.generateLink();
        assertFalse(testLink.equals(testLink1));

        LinkManager.addLink(testLink);
        assertTrue(LinkManager.getLinkMap().containsKey(testLink));

        //проверка дефолтных ограничений для ссылки
        Long[] constraints = LinkManager.getLinkMap().get(testLink);
        long defaultUsages = 3;
        assertEquals(defaultUsages, (long) constraints[0]);
        //3 min
        long defaultLifeTime = 3 * 60 * 1000;
        assertEquals(System.currentTimeMillis() + defaultLifeTime, (long) constraints[1]);

        //проверка установки количества использования ссылки
        long customUsages = 5;
        LinkManager.setLinkUsages(testLink, customUsages);
        constraints = LinkManager.getLinkMap().get(testLink);
        assertEquals(customUsages, (long) constraints[0]);

        //Проверка на просроченность ссылки
        assertFalse(LinkManager.linkIsExpired(testLink));

    }

    String testUUID = "024e904a-7524-421b-882f-379458bc8937";
    //Каждая ссылка всегда уникальна, тк создается с Random , есть проверка на коллизии
    String testLink = LinkManager.generateLink();
    String testUrl = "https://torscan-ru.ntc.party/";
    List<String> linkList = new ArrayList<>();

    {
        linkList.add(testLink);
        DataManager.getUuidLinkMap().put(testUUID, linkList);
        DataManager.getLinkUrlMap().put(testLink, testUrl);
        LinkManager.addLink(testLink);
        Main.setCurrentLink(testLink);
    }

    //тест создания и перехода по ссылке
    @Test
    public void testFullPathToUrl() {
        //Эмуляция создания uuid и ссылки

        //Проводим в консоль полный конвейер команд, чтобы в конечном итоге перейти по ссылке
        setConsoleInput(testUUID + "\n" +
                "add\n" +
                testUrl + "\n" +
                "1\n\n" +
                testLink + "\n");
        Main.main(new String[0]);
    }

    //тест блока перехода по ссылке
    @Test
    public void testLinkBlock() {

        //Проводим в консоль эту же ссылку, но использований после предыдущего теста 0, поэтому не перейдет 2-й раз
        setConsoleInput(testUUID + "\n" +
                "\n" +
                testLink + "\n" +
                "exit");
        Main.main(new String[0]);
    }

}