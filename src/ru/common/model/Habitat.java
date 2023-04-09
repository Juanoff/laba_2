package ru.common.model;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import ru.common.controller.emitter.ActionController;
import ru.common.controller.emitter.Actions;
import ru.common.controller.emitter.Emitter;
import ru.common.model.transport.Car;
import ru.common.model.transport.Motorcycle;
import ru.common.model.transport.Transport;
import ru.common.model.transport.TransportData;
import ru.common.model.transport.TransportFactory.CarFactory;
import ru.common.model.transport.TransportFactory.ITransportFactory;
import ru.common.model.transport.TransportFactory.MotorcycleFactory;
import ru.common.view.Frame;
import ru.common.view.StatsDialog;
import ru.common.view.TransportLabel;

public class Habitat {

    private final List<Transport> transports;
    private final Frame frame;
    private Timer timer;
    private final Random random;
    private long simulationTime;
    private boolean isVisibleTime;
    private boolean isVisibleStats;
    private final Emitter emitter;

    public Habitat(int width, int height) {
        emitter = new Emitter();
        emitter.subscribe("Frame:Control", this::triggerAction);
        emitter.subscribe("Frame:Menu", this::triggerAction);

        frame = new Frame(width, height, emitter);
        random = new Random();
        transports = TransportData.getInstance().getTransports();

        isVisibleTime = false;
        isVisibleStats = false;
        simulationTime = 0L;

        Car.setImage("src/ru/common/view/images/car.png");
        Motorcycle.setImage("src/ru/common/view/images/motorcycle.png");
    }

    private void triggerAction(ActionController actionControl) {
        switch (actionControl.action) {
            case START -> startSimulation();
            case STOP -> stopSimulation();
            case CONTINUE -> continueSimulation();
            case DESTROY -> destroySimulation();
            case HIDE_TIME -> isVisibleTime = false;
            case SHOW_TIME -> isVisibleTime = true;
            case SHOW_STATS -> isVisibleStats = true;
            case HIDE_STATS -> isVisibleStats = false;
            case SWITCH_BUTTON -> frame.switchButton();
            case TIMER_CANCEL -> timer.cancel();
            default -> {
            }
        }
    }

    public Timer getTimer() {
        return timer;
    }

    public Emitter getEmitter() {
        return emitter;
    }

    public long getSimulationTime() {
        return simulationTime;
    }

    public boolean isVisibleTime() {
        return isVisibleTime;
    }

    public void setVisibleTime(boolean isVisibleTime) {
        this.isVisibleTime = isVisibleTime;
    }

    public void startSimulation() {
        if (timer != null) {
            destroySimulation();
        }
        timer = new Timer();
        long period = 100L;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                simulationTime += period;
                updateFrame(simulationTime);
            }
        }, 0, period);
    }

    private void continueSimulation() {
        timer = new Timer();
        long period = 100L;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                simulationTime += period;
                updateFrame(simulationTime);
            }
        }, 0, period);
    }

    private void updateFrame(long currentTime) {
        emitter.emit("Habitat", new ActionController(Actions.UPDATE_TIME, (int) currentTime));

        int[] generationTimes = {Car.generationTime, Motorcycle.generationTime};
        float[] frequencies = {Car.frequency, Motorcycle.frequency};

        ITransportFactory[] transportFactories = {
            new CarFactory(),
            new MotorcycleFactory()
        };

        int width = frame.getWidthTransportsPanel();
        int height = frame.getHeightTransportsPanel();

        int minX = 175;
        int minY = 175;

        for (int i = 0; i < generationTimes.length; ++i) {
            if (isBorn(frequencies[i], currentTime, generationTimes[i])) {
                int x = random.nextInt(width - 2 * minX) + minX;
                int y = random.nextInt(height - 2 * minY) + minY;

                transports.add(transportFactories[i].createTransport(x, y));
                frame.addToTransportsPanel(
                    new TransportLabel(transports.get(transports.size() - 1)));
            }
        }
    }

    private boolean isBorn(float probability, long currentTime, int generationTime) {
        float randomProbability = random.nextFloat();
        return probability >= randomProbability && currentTime != 0
            && currentTime % generationTime == 0;
    }

    public void stopSimulation() {
        emitter.emit("Habitat", new ActionController(Actions.SHOW_TIME));
        if (isVisibleStats) {
            new StatsDialog(frame, "Statistics", createStatistics(), emitter);
        } else {
            timer.cancel();
        }
    }

    private String createStatistics() {
        int seconds = (int) (simulationTime / 1000);
        int ms = (int) (simulationTime % 1000);
        String[] stats = {
            "Time: " + seconds + "." + ms / 100 + " sec",
            "Count of Cars: " + Car.count,
            "Count of Motorcycles: " + Motorcycle.count
        };

        StringBuilder result = new StringBuilder();
        for (String stat : stats) {
            result.append(stat).append("\n");
        }

        return result.toString();
    }

    private void destroySimulation() {
        emitter.emit("Habitat", new ActionController(Actions.UPDATE_TIME, 0));
        timer.cancel();
        transports.clear();
        Motorcycle.count = 0;
        Car.count = 0;
        simulationTime = 0L;
        frame.removeTransports();
    }
}