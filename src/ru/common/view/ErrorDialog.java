package ru.common.view;

import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JTextArea;

public class ErrorDialog extends JDialog {

    public ErrorDialog(String textError) {
        setTitle("Error");
        setSize(350, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        JTextArea error = new JTextArea(textError);
        error.setBounds((350 - textError.length() * 7 + 5) / 2, 40, textError.length() * 7 + 5, 40);
        error.setEditable(false);
        error.setBackground(null);
        error.setFont(new Font("serif", Font.PLAIN, 17));

        add(error);
        add(new Button("OK", 125, 100, getOkActionListener()));

        setVisible(true);
    }

    private ActionListener getOkActionListener() {
        return e -> {
            setVisible(false);
            dispose();
        };
    }
}