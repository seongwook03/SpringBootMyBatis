package kopo.poly.service;

import kopo.poly.dto.FoodDTO;

import java.util.List;

public interface IFoodService {

    List<FoodDTO> toDayFood() throws Exception;
}
