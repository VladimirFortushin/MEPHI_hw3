package util;

import lombok.Getter;

import java.util.*;

public class LinkManager {
    private static final long defaultUsages = 3L;
    private static final String LINK_START = "clck.ru/";
    @Getter
    private static final Map<String, Long[]> linkMap = new HashMap<>();

    public static void addLink(String link) {
        Long[] constraints = new Long[2];
        constraints[0] = defaultUsages;
        //линк живет 3 минуты
        constraints[1] = System.currentTimeMillis() + 3 * 1000 * 60;
        linkMap.put(link, constraints);
    }

    public static void setLinkUsages(String link, long usages) {
        if (usages <= 0) {
            usages = 1;
            System.out.println("Минимальное количество использований ссылки: 1");
        }
        System.out.println("Задано количество использований " + usages + " для " + link);
        Long[] constraints = linkMap.get(link);
        constraints[0] = usages;
    }

    public static boolean linkIsExpired(String link) {
        Long[] constraints = linkMap.get(link);
        return System.currentTimeMillis() > constraints[1];
    }

    public static long countLinkUsages(String link) {
        Long[] constraints = linkMap.get(link);
        return constraints[0];
    }

    public static void createLink(String currentUUID, String currentLink, String url) {
        DataManager.getLinkUrlMap().put(currentLink, url);
        DataManager.getUuidLinkMap().get(currentUUID).add(currentLink);
        LinkManager.addLink(currentLink);
    }

    //Ограничение времени жизни ссылки. Время жизни ссылки должно задаваться системой и ограничиваться определенным сроком (например, сутки). После истечения этого срока ссылка должна автоматически удаляться.
    public static void updateLinksStatuses(String currentUUID) {
        List<String> links = DataManager.getUuidLinkMap().get(currentUUID);
        List<String> linksToRemove = new ArrayList<>();
        for (String link : links) {
            String status = getLinkStatus(link);
            if (status.contains("устарела")) {
                linksToRemove.add(link);
            }
        }
        for (String link : linksToRemove) {
            DataManager.removeLink(currentUUID, link);
        }
    }

    //Ограничение времени жизни ссылки. Время жизни ссылки должно задаваться системой и ограничиваться определенным сроком (например, сутки). После истечения этого срока ссылка должна автоматически удаляться.
    public static void printLinksStatuses(String currentUUID) {
        List<String> links = DataManager.getUuidLinkMap().get(currentUUID);
        String firstString = "Список ссылок пользователя";
        if (links.isEmpty()) {
            System.out.println(firstString + " пуст");
        } else {
            System.out.println(firstString);
            for (String link : links) {
                String status = getLinkStatus(link);
                System.out.println(status);
            }
        }
    }


    //Ограничение времени жизни ссылки. Время жизни ссылки должно задаваться системой и ограничиваться определенным сроком (например, сутки). После истечения этого срока ссылка должна автоматически удаляться.
    public static String getLinkStatus(String link) {
        StringBuilder result = new StringBuilder(link);
        if (linkIsExpired(link)) {
            result.append(" ссылка устарела, удалена");
        } else {
            long usagesLeft = countLinkUsages(link);
            if (usagesLeft <= 0) {
                result.append(" количество использований исчерпано, ссылка заблокирована");
            } else {
                long millis = linkMap.get(link)[1] - System.currentTimeMillis();
                long remainingMinutes = millis / 60 / 1000;
                long remainingSeconds = millis / 1000 % 60;
                result.append(" доступно использований: ").append(usagesLeft).append(". Автоудаление через: ").append(remainingMinutes).append(" мин. ").append(remainingSeconds).append(" сек.");
            }
        }
        return result.toString();
    }

    public static void decrementLinkUsage(String link, String currentUUID) {
        Long[] constraints = linkMap.get(link);
        if (constraints == null || constraints[0] == null) {
            System.out.println("Ссылка " + link + " устарела и была удалена");
            return;
        }

        constraints[0]--;
    }

    public static String generateLink() {
        String newLink = LINK_START + RandomGenerator.generateRandomString(6);
        //Проверка на дублирование link
        if (!DataManager.getLinkUrlMap().containsKey(newLink)) {
            return newLink;
        } else {
            generateLink();
        }
        return newLink;
    }
}
