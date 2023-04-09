package ru.common.controller;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import ru.common.controller.emitter.ActionController;
import ru.common.controller.emitter.Actions;
import ru.common.model.Habitat;


public class HabitatController implements KeyEventDispatcher {

    private final Habitat habitat;

    public HabitatController(Habitat habitat) {
        this.habitat = habitat;
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            if (e.getKeyCode() == KeyEvent.VK_B) {
                habitat.getEmitter().emit("Habitat", new ActionController(Actions.START));
                habitat.startSimulation();
            } else if (e.getKeyCode() == KeyEvent.VK_E && habitat.getSimulationTime() != 0L) {
                habitat.getEmitter().emit("Habitat", new ActionController(Actions.TIMER_CANCEL));
                habitat.getTimer().cancel();
                habitat.getEmitter().emit("Habitat", new ActionController(Actions.STOP));
                habitat.stopSimulation();
            } else if (e.getKeyCode() == KeyEvent.VK_T) {
                Actions action = habitat.isVisibleTime() ? Actions.HIDE_TIME : Actions.SHOW_TIME;
                habitat.getEmitter().emit("Habitat", new ActionController(action));
                habitat.setVisibleTime(!habitat.isVisibleTime());
            }
        }
        return false;
    }
}