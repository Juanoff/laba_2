package ru.common.view;

import java.awt.event.ActionListener;
import javax.swing.JButton;

public class Button extends JButton {

    public Button(String text, int x, int y, ActionListener actionListener) {
        super(text);
        setBounds(x, y, 100, 50);
        setFocusPainted(false);
        setContentAreaFilled(false);
        addActionListener(actionListener);
    }
}