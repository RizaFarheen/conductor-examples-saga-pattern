package io.orkes.example.saga.dao;

import io.orkes.example.saga.pojos.Driver;

public class DriverDAO extends DBAccessor {

    public DriverDAO(String url) {
        super(url);
    }

    public void readDriver(int driverId, Driver driver) {

    }
}
