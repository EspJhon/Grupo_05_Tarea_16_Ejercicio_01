package com.example.grupo_05_tarea_16_ejercicio_01.adapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IPUtilizada {

    private static IPUtilizada instance;
    private List<String> ips = Arrays.asList("192.168.100.15", "192.168.10.106", "192.168.1.6", "192.168.1.2","192.168.50.90");

    private IPUtilizada() {
    }

    public static IPUtilizada getInstance() {
        if (instance == null) {
            instance = new IPUtilizada();
        }
        return instance;
    }

    public String getSelectedIP(String username) {
        Map<String, String> userIpMap = new HashMap<>();
        userIpMap.put("jhon", ips.get(0));
        userIpMap.put("chagua", ips.get(1));
        userIpMap.put("matias", ips.get(2));
        userIpMap.put("calixto", ips.get(3));
        userIpMap.put("admin", ips.get(4));

        return userIpMap.get(username);
    }

}

