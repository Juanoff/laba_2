package ru.common.model.transport;

import java.util.ArrayList;
import java.util.List;

public class TransportData {

    private static TransportData INSTANCE;
    private final List<Transport> transports;

    private TransportData() {
        transports = new ArrayList<>();
    }

    public static TransportData getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TransportData();
        }
        return INSTANCE;
    }

    public List<Transport> getTransports() {
        return transports;
    }
}