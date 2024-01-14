package ru.clevertec.house.service.impl.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.exception.PassportValidException;
import ru.clevertec.house.mapper.PersonMapper;
import ru.clevertec.house.model.House;
import ru.clevertec.house.model.Person;
import ru.clevertec.house.model.dto.request.RequestPerson;
import ru.clevertec.house.model.dto.response.ResponsePerson;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.repository.PassportRepository;
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.service.impl.PersonServiceImpl;
import ru.clevertec.house.service.util.HouseTest;
import ru.clevertec.house.service.util.PersonTest;
import ru.clevertec.house.service.util.RequestPersonTest;
import ru.clevertec.house.service.util.ResponsePersonTest;
import ru.clevertec.house.service.util.TestConstant;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private PassportRepository passportRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonServiceImpl personService;

    private final HouseTest houseTest = new HouseTest();
    private final PersonTest personTest = new PersonTest();
    private final ResponsePersonTest responsePersonTest = new ResponsePersonTest();
    private final RequestPersonTest requestPersonTest = new RequestPersonTest();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPersonByUuid() {
        Person person = personTest.build();
        when(personRepository.findByUuid(TestConstant.PERSON_UUID)).thenReturn(Optional.of(person));
        when(personMapper.toDto(person)).thenReturn(responsePersonTest.build());

        ResponsePerson result = personService.get(TestConstant.PERSON_UUID);

        assertThat(result).isNotNull();
        verify(personRepository, times(1)).findByUuid(TestConstant.PERSON_UUID);
        verify(personMapper, times(1)).toDto(person);
    }

    @Test
    void testGetAllPersons() {
        List<Person> personList = Arrays.asList(personTest.build());
        when(personRepository.findAll(1,1)).thenReturn(personList);
        when(personMapper.toDtoList(personList)).thenReturn(Arrays.asList(responsePersonTest.build()));

        List<ResponsePerson> result = personService.getAll(1,1);

        assertThat(result).hasSize(1);
        verify(personRepository, times(1)).findAll(1,1);
        verify(personMapper, times(1)).toDtoList(personList);
    }

    @Test
    void testCreatePerson() {
        RequestPerson requestPerson = requestPersonTest.build();
        House house = houseTest.build();
        when(houseRepository.findByUuid(TestConstant.HOISE_UUID)).thenReturn(Optional.of(house));
        when(personMapper.toEntity(requestPerson)).thenReturn(personTest.build());
        when(passportRepository.findBySeriesAndNumberPassport(any(), any())).thenReturn(Optional.empty());
        when(personRepository.save(any())).thenReturn(TestConstant.PERSON_UUID);

        UUID result = personService.create(requestPerson, TestConstant.HOISE_UUID);

        assertThat(result).isNotNull();
        verify(houseRepository, times(1)).findByUuid(TestConstant.HOISE_UUID);
        verify(personMapper, times(1)).toEntity(requestPerson);
        verify(passportRepository, times(1)).findBySeriesAndNumberPassport(any(), any());
        verify(personRepository, times(1)).save(any());
    }

    @Test
    void testUpdatePerson() throws NotFoundException {
        RequestPerson requestPerson = requestPersonTest.build();
        Person existingPerson = personTest.build();
        existingPerson.setName("name");
        when(personRepository.findByUuid(TestConstant.PERSON_UUID)).thenReturn(Optional.of(existingPerson));
        when(personMapper.toEntity(requestPerson)).thenReturn(personTest.build());

        UUID result = personService.update(TestConstant.PERSON_UUID, requestPerson);

        assertThat(result).isEqualTo(TestConstant.PERSON_UUID);
        verify(personRepository, times(1)).findByUuid(TestConstant.PERSON_UUID);
        verify(personMapper, times(1)).toEntity(requestPerson);
        verify(personRepository, times(1)).save(any());
    }

    @Test
    void testDeletePerson() {
        UUID personUuid = UUID.randomUUID();

        personService.delete(personUuid);

        verify(personRepository, times(1)).delete(personUuid);
    }


    @Test
    void testCreatePersonThrowsNotFoundExceptionForMissingHouse() {
        RequestPerson requestPerson = requestPersonTest.build();
        when(houseRepository.findByUuid(TestConstant.HOISE_UUID)).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> personService.create(requestPerson, TestConstant.HOISE_UUID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("ru.clevertec.house.model.House with 5e213358-c398-49be-945b-e2b32a0c4a41 not found");

        verify(houseRepository, times(1)).findByUuid(TestConstant.HOISE_UUID);
        verifyNoMoreInteractions(personMapper, passportRepository, personRepository);
    }

    @Test
    void testCreatePersonThrowsPassportValidExceptionForInvalidPassport() {
        RequestPerson requestPerson = requestPersonTest.build();
        House house = houseTest.build();
        when(houseRepository.findByUuid(TestConstant.HOISE_UUID)).thenReturn(Optional.of(house));
        when(personMapper.toEntity(requestPerson)).thenReturn(personTest.build());
        when(passportRepository.findBySeriesAndNumberPassport(any(), any())).thenReturn(Optional.of(requestPerson.passport()));

        assertThatThrownBy(
                () -> personService.create(requestPerson, TestConstant.HOISE_UUID))
                .isInstanceOf(PassportValidException.class)
                .hasMessage("Passport with this number has already been saved");

        verify(houseRepository, times(1)).findByUuid(TestConstant.HOISE_UUID);
        verify(passportRepository, times(1)).findBySeriesAndNumberPassport(any(), any());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void testUpdatePersonThrowsNotFoundExceptionForMissingPerson() {
        RequestPerson requestPerson = requestPersonTest.build();
        when(personRepository.findByUuid(TestConstant.PERSON_UUID)).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> personService.update(TestConstant.PERSON_UUID, requestPerson))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("ru.clevertec.house.model.Person with 5c3a267c-3175-4826-a7d1-488782a62d98 not found");

        verify(personRepository, times(1)).findByUuid(TestConstant.PERSON_UUID);
        verifyNoMoreInteractions(houseRepository, personMapper, personRepository);
    }
}
