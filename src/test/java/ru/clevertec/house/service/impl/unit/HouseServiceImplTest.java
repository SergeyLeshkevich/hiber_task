package ru.clevertec.house.service.impl.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.mapper.HouseMapper;
import ru.clevertec.house.model.House;
import ru.clevertec.house.model.Person;
import ru.clevertec.house.model.dto.request.RequestHouse;
import ru.clevertec.house.model.dto.response.ResponseHouse;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.service.impl.HouseServiceImpl;
import ru.clevertec.house.service.util.HouseTest;
import ru.clevertec.house.service.util.PersonTest;
import ru.clevertec.house.service.util.RequestHouseTest;
import ru.clevertec.house.service.util.ResponseHouseTest;
import ru.clevertec.house.service.util.TestConstant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HouseServiceImplTest {

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private HouseMapper houseMapper;

    @InjectMocks
    private HouseServiceImpl houseService;

    private final HouseTest houseTest = new HouseTest();
    private final PersonTest personTest = new PersonTest();
    private final ResponseHouseTest responseHouseTest=new ResponseHouseTest();
    private final RequestHouseTest requestHouseTest=new RequestHouseTest();

    @Test
    void testGetExistingHouse() throws NotFoundException {
        House house = houseTest.build();

        when(houseRepository.findByUuid(TestConstant.HOISE_UUID)).thenReturn(Optional.of(house));
        when(houseMapper.toDto(house)).thenReturn(responseHouseTest.build());

        ResponseHouse responseHouse = houseService.get(TestConstant.HOISE_UUID);

        assertThat(responseHouse).isNotNull();
        verify(houseRepository, times(1)).findByUuid(TestConstant.HOISE_UUID);
    }

    @Test
    void testGetNonExistingHouse() {
        UUID uuid = UUID.randomUUID();

        when(houseRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> houseService.get(uuid)).isInstanceOf(NotFoundException.class);
        verify(houseRepository, times(1)).findByUuid(uuid);
    }

    @Test
    void testGetAllHouses() {
        List<House> houseList = Collections.singletonList(new House());

        when(houseRepository.findAll(1,1)).thenReturn(houseList);
        when(houseMapper.toDtoList(houseList)).thenReturn(Collections.singletonList(responseHouseTest.build()));

        List<ResponseHouse> allHouses = houseService.getAll(1,1);

        assertThat(allHouses).isNotNull();
        assertThat(allHouses).hasSize(1);
        verify(houseRepository, times(1)).findAll(1,1);
    }


    @Test
    void testCreateHouse() {
        RequestHouse requestHouse = requestHouseTest.build();
        Set<UUID> registeredPeople = Collections.singleton(TestConstant.HOISE_UUID);

        when(houseMapper.toEntity(requestHouse)).thenReturn(houseTest.build());
        when(houseRepository.save(any())).thenReturn(TestConstant.HOISE_UUID);

        UUID createdHouseUuid = houseService.create(requestHouse);

        assertThat(createdHouseUuid).isNotNull();
        verify(houseRepository, times(1)).save(any());
    }

    @Test
    void testUpdateHouse() throws NotFoundException {
        RequestHouse requestHouse = requestHouseTest.build();
        Set<UUID> registeredPeople = Collections.singleton(TestConstant.HOISE_UUID);

        when(houseMapper.toEntity(requestHouse)).thenReturn(houseTest.build());
        when(personRepository.findByUuid(registeredPeople)).thenReturn(List.of(personTest.build()));
        when(houseRepository.save(any())).thenReturn(TestConstant.HOISE_UUID);

        UUID updatedHouseUuid = houseService.update(TestConstant.HOISE_UUID, requestHouse, registeredPeople);

        assertThat(updatedHouseUuid).isEqualTo(TestConstant.HOISE_UUID);
        verify(houseRepository, times(1)).save(any());
    }

    @Test
    void testDeleteHouse() {
        UUID uuid = TestConstant.HOISE_UUID;

        houseService.delete(uuid);

        verify(houseRepository, times(1)).delete(uuid);
    }

    @Test
    void testCheckInputSetPeopleForAvailabilityPeopleExist() {
        Set<UUID> registeredPeople = Set.of(TestConstant.PERSON_UUID);

        List<Person> personsFromRepository = Arrays.asList(personTest.build());

        when(personRepository.findByUuid(anySet())).thenReturn(personsFromRepository);

        ReflectionTestUtils.invokeMethod(houseService, "checkInputSetPeopleForAvailability", registeredPeople);

        verify(personRepository, times(1)).findByUuid(registeredPeople);
    }

    @Test
    void testCheckInputSetPeopleForAvailabilityMissingPerson() {
        Set<UUID> registeredPeople = Set.of(TestConstant.HOISE_UUID);

        when(personRepository.findByUuid(anySet())).thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(houseService, "checkInputSetPeopleForAvailability", registeredPeople))
                .isInstanceOf(NotFoundException.class);

        verify(personRepository, times(1)).findByUuid(registeredPeople);
    }


}
