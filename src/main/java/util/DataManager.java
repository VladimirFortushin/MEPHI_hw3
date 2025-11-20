package util;


import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataManager {
    @Getter
    private static final Map<String, String> linkUrlMap = new HashMap<>();
    @Getter
    private static final Map<String, List<String>> uuidLinkMap = new HashMap<>();

    public static void removeLink(String currentUUID, String link) {
        linkUrlMap.remove(link);
        uuidLinkMap.get(currentUUID).remove(link);
    }






}
