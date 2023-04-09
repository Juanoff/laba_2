package ru.common.controller.emitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class Emitter {

    private final HashMap<String, List<Consumer<ActionController>>> listeners;

    public Emitter() {
        listeners = new HashMap<>();
    }

    public void emit(String event, ActionController arg) {
        listeners.get(event).forEach(fn -> fn.accept(arg));
    }

    public void subscribe(String event, Consumer<ActionController> fn) {
        if (!listeners.containsKey(event)) {
            listeners.put(event, new ArrayList<>());
        }
        listeners.get(event).add(fn);
    }
}