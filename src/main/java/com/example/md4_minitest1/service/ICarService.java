package com.example.md4_minitest1.service;

import com.example.md4_minitest1.model.Car;

import java.util.List;

public interface ICarService {
    List<Car> findAll();
    Car findByID(int id);
    void save(Car car);

    void remove(int id);

}
