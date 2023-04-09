package ru.common.view;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import ru.common.model.transport.Transport;

public class TransportLabel extends JLabel {

    public TransportLabel(Transport transport) {
        super();
        int WIDTH = 80;
        int HEIGHT = 80;
        Image image = transport.getImage()
            .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_AREA_AVERAGING);
        setIcon(new ImageIcon(image));
        setBounds(transport.getX(), transport.getY(), WIDTH, HEIGHT);
    }
}