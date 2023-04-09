package ru.common.view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import ru.common.controller.emitter.Emitter;

public class Frame extends JFrame {

    private final TransportPanel transportPanel;
    private final ControlPanel controlPanel;
    private final MenuBar menuBar;
    private final int width;
    private final int height;

    public Frame(int width, int height, Emitter emitter) {
        super("Transport generation");
        this.width = width;
        this.height = height;

        setLayout(new BorderLayout());
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuBar = new MenuBar(emitter);
        setJMenuBar(menuBar);
        setLocationRelativeTo(null);

        transportPanel = new TransportPanel(getWidthTransportsPanel() - 15,
            getHeightTransportsPanel());
        add(transportPanel, BorderLayout.WEST);

        controlPanel = new ControlPanel((int) (width * (1 / 3f)), height, emitter);
        add(controlPanel, BorderLayout.EAST);

        setVisible(true);
    }

    public void switchButton() {
        controlPanel.switchButton();
        menuBar.switchButton();
    }

    public void removeTransports() {
        transportPanel.removeTransports();
        repaint();
    }

    public void addToTransportsPanel(TransportLabel transport) {
        transportPanel.addTransport(transport);
        repaint();
    }

    public int getWidthTransportsPanel() {
        float coefWidthTransports = 2 / 3f;
        return (int) (width * coefWidthTransports);
    }

    public int getHeightTransportsPanel() {
        return height;
    }
}