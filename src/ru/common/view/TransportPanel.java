package ru.common.view;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TransportPanel extends JPanel {

    private final List<TransportLabel> transportsLabels;

    public TransportPanel(int width, int height) {
        transportsLabels = new ArrayList<>();
        setPreferredSize(new Dimension(width, height));
        setLayout(null);

        URL imageURL = getClass().getResource("/ru/common/view/images/road.png");
        assert imageURL != null;
        Image backgroundImage = new ImageIcon(imageURL).getImage()
            .getScaledInstance(width, height, Image.SCALE_SMOOTH);

        JLabel roadLabel = new JLabel(new ImageIcon(backgroundImage));
        roadLabel.setBounds(0, 0, width, height);
        add(roadLabel);
    }

    public void addTransport(TransportLabel transport) {
        add(transport);
        setComponentZOrder(transport, 0);
        transportsLabels.add(transport);
    }

    public void removeTransports() {
        transportsLabels.forEach(this::remove);
        transportsLabels.clear();
    }
}

//        for (TransportLabel transportsLabel : transportsLabels) {
//            remove(transportsLabel);
//        }