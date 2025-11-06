package ru.mephi.util;

import lombok.Getter;
import ru.mephi.entity.User;

import java.util.HashSet;
import java.util.Set;

public class DataStorageUtil {
    @Getter
    private static final Set<User> userSet = new HashSet<>();
}
