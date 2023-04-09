package ru.common.view;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import ru.common.model.transport.Transport;

public class TransportLabel extends JLabel {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 80;
    public TransportLabel(Transport transport) {
        super();
        Image image = transport.getImage()
            .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_AREA_AVERAGING);
        setIcon(new ImageIcon(image));
        setBounds(transport.getX(), transport.getY(), WIDTH, HEIGHT);
    }
}