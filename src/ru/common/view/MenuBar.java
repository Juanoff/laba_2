package ru.common.view;

import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import ru.common.controller.emitter.ActionController;
import ru.common.controller.emitter.Actions;
import ru.common.controller.emitter.Emitter;

public class MenuBar extends JMenuBar {

    private final Emitter emitter;
    private final JMenuItem start;
    private final JMenuItem stop;
    private final JCheckBoxMenuItem time;
    private final JCheckBoxMenuItem stats;

    public MenuBar(Emitter emitter) {
        this.emitter = emitter;
        emitter.subscribe("Frame:Control", this::triggerAction);
        emitter.subscribe("Habitat", this::triggerAction);

        JMenu actions = new JMenu("Menu");

        start = new JMenuItem("Start");
        start.addActionListener(getStartActionListener());

        stop = new JMenuItem("Stop");
        stop.addActionListener(getStopActionListener());
        stop.setEnabled(false);

        time = new JCheckBoxMenuItem("Show time");
        time.addActionListener(getTimeActionListener());

        stats = new JCheckBoxMenuItem("Show information");
        stats.addActionListener(getStatsActionListener());

        actions.add(start);
        actions.add(stop);
        actions.add(time);
        actions.add(stats);
        add(actions);
    }

    private void triggerAction(ActionController actionControl) {
        switch (actionControl.action) {
            case STOP, START -> switchButton();
            case HIDE_TIME -> time.setSelected(false);
            case SHOW_TIME -> time.setSelected(true);
            case SHOW_STATS -> stats.setSelected(true);
            case HIDE_STATS -> stats.setSelected(false);
            default -> {
            }
        }
    }

    private ActionListener getStartActionListener() {
        return e -> {
            switchButton();
            ActionController action = new ActionController(Actions.START);
            emitter.emit("Frame:Control", action);
            emitter.emit("Habitat", action);
        };
    }

    private ActionListener getStopActionListener() {
        return e -> {
            switchButton();
            emitter.emit("Frame:Menu", new ActionController(Actions.TIMER_CANCEL));
            emitter.emit("Frame:Menu", new ActionController(Actions.STOP));
        };
    }

    private ActionListener getTimeActionListener() {
        return e -> {
            Actions action = time.isSelected() ? Actions.SHOW_TIME : Actions.HIDE_TIME;
            ActionController actionControl = new ActionController(action);
            emitter.emit("Frame:Menu", actionControl);
        };
    }

    private ActionListener getStatsActionListener() {
        return e -> {
            Actions action = stats.isSelected() ? Actions.SHOW_STATS : Actions.HIDE_STATS;
            ActionController actionControl = new ActionController(action);
            emitter.emit("Frame:Menu", actionControl);
        };
    }

    public void switchButton() {
        if (start.isEnabled()) {
            start.setEnabled(false);
            stop.setEnabled(true);
        } else {
            start.setEnabled(true);
            stop.setEnabled(false);
        }
    }
}
