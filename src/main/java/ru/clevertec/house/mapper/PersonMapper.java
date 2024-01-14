package ru.clevertec.house.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.house.model.Person;
import ru.clevertec.house.model.dto.request.RequestPerson;
import ru.clevertec.house.model.dto.response.ResponsePerson;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "house", ignore = true)
    @Mapping(target = "houses", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Person toEntity(RequestPerson personDto);
    ResponsePerson toDto(Person person);
    List<ResponsePerson> toDtoList(List<Person> persons);
}
