package ru.common.model.transport;

public class TransportFactory {

    public interface ITransportFactory {

        Transport createTransport(int x, int y);
    }

    public static class CarFactory implements ITransportFactory {

        @Override
        public Transport createTransport(int x, int y) {
            return new Car(x, y);
        }
    }

    public static class MotorcycleFactory implements ITransportFactory {

        @Override
        public Transport createTransport(int x, int y) {
            return new Motorcycle(x, y);
        }
    }
}