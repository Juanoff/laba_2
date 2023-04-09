package ru.common.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionListener;
import ru.common.controller.emitter.ActionController;
import ru.common.controller.emitter.Actions;
import ru.common.controller.emitter.Emitter;
import ru.common.model.transport.Car;
import ru.common.model.transport.Motorcycle;
import ru.common.utils.MainUtils;

public class ControlPanel extends JPanel {

    private final Emitter emitter;
    private final Button start;
    private final Button stop;
    private final JLabel timeLabel;
    private final JRadioButton showTime;
    private final JRadioButton hideTime;
    private final JCheckBox stats;
    private final JTextField timeSpawnCar;
    private final JTextField timeSpawnMotorcycle;
    private final String DEFAULT_TIME = "100";
    private final JComboBox<String> frequencyCar;
    private final JList<String> frequencyMotorcycle;

    public ControlPanel(int width, int height, Emitter emitter) {
        setPreferredSize(new Dimension(width, height));
        setLayout(null);

        this.emitter = emitter;
        emitter.subscribe("Frame:Menu", this::triggerAction);
        emitter.subscribe("Habitat", this::triggerAction);

        start = new Button("Start", 20, 20, getStartActionListener());
        add(start);
        stop = new Button("Stop", 140, 20, getStopActionListener());
        stop.setEnabled(false);
        add(stop);

        ButtonGroup bgTime = new ButtonGroup();
        showTime = new JRadioButton("Show time");
        customizeRadioAndAddToPanel(bgTime, showTime, 100, getShowActionListener());
        hideTime = new JRadioButton("Hide time");
        customizeRadioAndAddToPanel(bgTime, hideTime, 130, getHideActionListener());
        hideTime.setSelected(true);

        timeLabel = new JLabel("Time: 0.0 seconds");
        timeLabel.setBounds(200, 115, 200, 20);
        timeLabel.setVisible(false);
        add(timeLabel);

        stats = new JCheckBox("Show information");
        stats.setBounds(20, 170, 200, 20);
        stats.setFocusPainted(false);
        stats.addActionListener(getStatsActionListener());
        add(stats);

        timeSpawnCar = new JTextField("1000");
        customizeTextFieldAndAddToPanel("Car generation time", 20,
            timeSpawnCar, 75, getTimeSpawnCarActionListener());

        timeSpawnMotorcycle = new JTextField("1000");
        customizeTextFieldAndAddToPanel("Motorcycle generation time", 250,
            timeSpawnMotorcycle, 305, getTimeSpawnMotorcycleActionListener());

        JLabel infoFrequencyCar = new JLabel("Car chance of generation");
        infoFrequencyCar.setBounds(20, 280, 200, 20);
        add(infoFrequencyCar);

        Vector<String> percentages = MainUtils.generatePercentages(10, 100, 10);
        frequencyCar = new JComboBox<>(percentages);
        frequencyCar.setMaximumRowCount(5);
        frequencyCar.setBounds(75, 310, 70, 20);
        frequencyCar.setSelectedIndex(9);
        frequencyCar.addActionListener(getFrequencyCarActionListener());
        add(frequencyCar);

        JLabel infoFrequencyMotorcycle = new JLabel("Motorcycle chance of generation");
        infoFrequencyMotorcycle.setBounds(250, 280, 200, 20);
        add(infoFrequencyMotorcycle);

        frequencyMotorcycle = new JList<>(percentages);
        frequencyMotorcycle.setBounds(305, 310, 40, 180);
        frequencyMotorcycle.setSelectedIndex(4);
        frequencyMotorcycle.addListSelectionListener(getFrequencyMotorcycleSelectionListener());
        add(frequencyMotorcycle);
    }

    private void triggerAction(ActionController actionControl) {
        switch (actionControl.action) {
            case STOP, START -> switchButton();
            case UPDATE_TIME -> updateTime(actionControl.state);
            case HIDE_TIME -> {
                timeLabel.setVisible(false);
                hideTime.setSelected(true);
            }
            case SHOW_TIME -> {
                timeLabel.setVisible(true);
                showTime.setSelected(true);
            }
            case SHOW_STATS -> stats.setSelected(true);
            case HIDE_STATS -> stats.setSelected(false);
            default -> {
            }
        }
    }

    private ActionListener getStartActionListener() {
        return e -> {
            switchButton();
            emitter.emit("Frame:Control", new ActionController(Actions.START));
        };
    }

    private ActionListener getStopActionListener() {
        return e -> {
            switchButton();
            emitter.emit("Frame:Control", new ActionController(Actions.TIMER_CANCEL));
            emitter.emit("Frame:Control", new ActionController(Actions.STOP));
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

    private void customizeRadioAndAddToPanel(ButtonGroup bg, JRadioButton rb, int y,
        ActionListener actionListener) {
        rb.setBounds(20, y, 150, 20);
        rb.setFocusPainted(false);
        rb.setContentAreaFilled(false);
        rb.addActionListener(actionListener);
        bg.add(rb);
        add(rb);
    }

    private ActionListener getShowActionListener() {
        return e -> {
            timeLabel.setVisible(true);
            emitter.emit("Frame:Control", new ActionController(Actions.SHOW_TIME));
        };
    }

    private ActionListener getHideActionListener() {
        return e -> {
            timeLabel.setVisible(false);
            emitter.emit("Frame:Control", new ActionController(Actions.HIDE_TIME));
        };
    }

    private void updateTime(int time) {
        int seconds = time / 1000;
        int ms = time % 1000;
        timeLabel.setText("Time: " + seconds + "." + ms / 100 + " seconds");
    }

    private ActionListener getStatsActionListener() {
        return e -> {
            Actions action = stats.isSelected() ? Actions.SHOW_STATS : Actions.HIDE_STATS;
            emitter.emit("Frame:Control", new ActionController(action));
        };
    }

    private void customizeTextFieldAndAddToPanel(String description, int xDesc,
        JTextField field, int xField, ActionListener actionList) {

        JLabel desc = new JLabel(description);
        desc.setBounds(xDesc, 220, 200, 20);

        field.setBounds(xField, 250, 50, 20);
        field.addActionListener(actionList);

        add(desc);
        add(field);
    }

    private ActionListener getTimeSpawnCarActionListener() {
        return e -> {
            if (checkErrorFromSpawnField(timeSpawnCar)) {
                Car.generationTime = Integer.parseInt(timeSpawnCar.getText());
            } else {
                timeSpawnCar.setText(DEFAULT_TIME);
            }
        };
    }

    private ActionListener getTimeSpawnMotorcycleActionListener() {
        return e -> {
            if (checkErrorFromSpawnField(timeSpawnMotorcycle)) {
                Motorcycle.generationTime = Integer.parseInt(timeSpawnMotorcycle.getText());
            } else {
                timeSpawnMotorcycle.setText(DEFAULT_TIME);
            }
        };
    }

    private boolean checkErrorFromSpawnField(JTextField field) {
        try {
            Integer.parseInt(field.getText());
        } catch (NumberFormatException ex) {
            new ErrorDialog("You can't enter a character");
            return false;
        }

        if (field.getText().length() < 3) {
            new ErrorDialog("You can't enter less than 3 digits");
            return false;
        }

        return true;
    }

    private ActionListener getFrequencyCarActionListener() {
        return e -> Car.frequency = Float.parseFloat(((String) Objects.requireNonNull(
            frequencyCar.getSelectedItem())).replace("%", "")) / 100;
    }

    private ListSelectionListener getFrequencyMotorcycleSelectionListener() {
        return e -> {
            if (!e.getValueIsAdjusting()) {
                Motorcycle.frequency =
                    Float.parseFloat(frequencyMotorcycle.getSelectedValue().replace("%", "")) / 100;
            }
        };
    }
}