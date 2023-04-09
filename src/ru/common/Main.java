package ru.common;

import ru.common.controller.HabitatController;
import ru.common.model.Habitat;

public class Main {

    public static void main(String[] args) {
        new HabitatController(new Habitat(1400, 700));
    }
}