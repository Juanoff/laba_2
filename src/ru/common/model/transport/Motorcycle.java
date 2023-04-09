package ru.common.model.transport;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Motorcycle extends Transport {

    public static Image image;
    public static float frequency = 0.5f;
    public static int generationTime = 1000;
    public static int count = 0;

    public Motorcycle(int x, int y) {
        super(x, y);
        Motorcycle.count++;
    }

    public Image getImage() {
        return Motorcycle.image;
    }

    public static void setImage(String path) {
        try {
            Motorcycle.image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}