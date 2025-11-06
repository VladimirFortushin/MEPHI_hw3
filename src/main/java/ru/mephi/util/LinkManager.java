package ru.mephi.util;

import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LinkManager {
    private static final String LINK_CLK = "clck.ru/";
    @Getter
    private static final Map<String, String[]> uuidLinkMap = new HashMap<>();
    static {
        //запуск уборщика со стартом приложения
        startCleanupTask();
    }

    private static void startCleanupTask() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                    ZonedDateTime now = ZonedDateTime.now();

                    // Удаление просроченных записей
                    uuidLinkMap.entrySet().removeIf(entry ->
                            DateTimeUtil.isExpired(entry.getValue()[1])
                    );
                    //проверка раз в сутки

                }, 1, 1, TimeUnit.DAYS);
    }

    public static String getLink(String uuid, String url){
        String result = null;
        if(!uuidLinkMap.containsKey(uuid)){
            String link = LINK_CLK + RandomGenerator.generateRandomString(6);
            //ссылка живет 24 часа
            ZonedDateTime expiresAt = ZonedDateTime.now(ZoneId.systemDefault()).plusHours(24);
            String repeats = "5";
            String[] record = new String[3];
            record[0] = link;
            record[1] = expiresAt.toString();
            record[2] = repeats;
            uuidLinkMap.put(uuid, record);
            decrementRepeats(uuid);
            result = link;
        }else{
            if(getRepeats(uuid) == 0){
                result = "Error: no repeats left, the link is unavailable";
            }else{
                decrementRepeats(uuid);
                result = uuidLinkMap.get(uuid)[0];
            }
        }
        return result;
    }

    public static int getRepeats(String uuid) {
        return Integer.parseInt(uuidLinkMap.get(uuid)[2]);
    }

    public static void decrementRepeats(String uuid) {
        uuidLinkMap.get(uuid)[2] = String.valueOf(Integer.parseInt(uuidLinkMap.get(uuid)[2]) - 1);
    }


}
