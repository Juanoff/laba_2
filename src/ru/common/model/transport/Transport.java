package ru.common.model.transport;

import java.awt.Image;

public abstract class Transport implements IBehaviour {

    private int x;
    private int y;

    public Transport(int x, int y) {
        setPosition(x, y);
    }

    public abstract Image getImage();

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }
}
