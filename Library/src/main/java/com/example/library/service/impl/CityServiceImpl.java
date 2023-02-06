package com.example.library.service.impl;

import com.example.library.model.City;
import com.example.library.repository.CityRepository;
import com.example.library.service.CityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    private CityRepository cityRepository;

    @Override
    public List<City> getAll() {
        return cityRepository.findAll();
    }
}
