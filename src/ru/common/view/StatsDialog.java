package ru.common.view;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionListener;
import java.io.InputStream;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import ru.common.controller.emitter.ActionController;
import ru.common.controller.emitter.Actions;
import ru.common.controller.emitter.Emitter;

public class StatsDialog extends JDialog {

    private final Emitter emitter;
    private Font customFont;

    public StatsDialog(JFrame owner, String title, String text, Emitter emitter) {
        super(owner, title, true);
        this.emitter = emitter;

        setLayout(null);
        setSize(400, 400);
        setResizable(false);
        setLocationRelativeTo(owner);

        try {
            InputStream is = getClass().getResourceAsStream("/ru/common/view/fonts/Bayon-Regular.ttf");
            assert is != null;
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JTextArea statistics = new JTextArea(text);
        statistics.setBounds(20, 10, 400, 200);
        statistics.setEditable(false);
        statistics.setBackground(null);
        statistics.setFont(customFont);

        add(statistics);
        add(new Button("OK", 50, 300, getOkActionListener()));
        add(new Button("Cancel", 250, 300, getCancelActionListener()));

        setVisible(true);
    }

    private ActionListener getCancelActionListener() {
        return e -> {
            emitter.emit("Frame:Control", new ActionController(Actions.CONTINUE));
            emitter.emit("Frame:Control", new ActionController(Actions.SWITCH_BUTTON));
            destroy();
        };
    }

    private ActionListener getOkActionListener() {
        return e -> {
            emitter.emit("Frame:Control", new ActionController(Actions.DESTROY));
            destroy();
        };
    }

    private void destroy() {
        setVisible(false);
        dispose();
    }
}