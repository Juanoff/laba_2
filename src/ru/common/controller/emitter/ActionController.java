package ru.common.controller.emitter;

public class ActionController {

    public int state = 0;

    public Actions action;

    public ActionController(Actions action, int state) {
        this.state = state;
        this.action = action;
    }

    public ActionController(Actions action) {
        this.action = action;
    }
}