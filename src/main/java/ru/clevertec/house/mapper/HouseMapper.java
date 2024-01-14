package ru.clevertec.house.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.house.model.House;
import ru.clevertec.house.model.dto.request.RequestHouse;
import ru.clevertec.house.model.dto.response.ResponseHouse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HouseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredPeople", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    House toEntity(RequestHouse requestHouse);
    ResponseHouse toDto(House house);
    List<ResponseHouse> toDtoList(List<House> houses);

}
