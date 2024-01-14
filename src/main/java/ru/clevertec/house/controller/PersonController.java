package ru.clevertec.house.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.house.model.dto.request.RequestPerson;
import ru.clevertec.house.model.dto.response.ResponsePerson;
import ru.clevertec.house.service.PersonService;
import ru.clevertec.house.util.PaginationResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/persons")
public class PersonController {

    private final PersonService personService;
    private final Gson gson;

    @PostMapping(value = "/")
    public ResponseEntity<String> createPerson(@RequestBody RequestPerson person,
                                                       @RequestBody UUID houseUuid) {
        UUID responseUuid = personService.create(person, houseUuid);
        return new ResponseEntity<>(gson.toJson(personService.get(responseUuid)), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<String> getPersonById(@PathVariable("id") UUID id) {
        ResponsePerson person = personService.get(id);
        return new ResponseEntity<>(gson.toJson(person), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> updatePersonById(@PathVariable("id") UUID id,
                                                   @RequestBody RequestPerson person) {
        UUID responseUuid = personService.update(id, person);
        return new ResponseEntity<>(gson.toJson(personService.get(responseUuid)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePersonById(@PathVariable("id") UUID id) {
        personService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<String> getAllPersons(@RequestParam(defaultValue = "15", name = "pageSize") int pageSize,
                                                @RequestParam(defaultValue = "1", name = "numberPage") int numberPage) {
        List<ResponsePerson> paginatedList = personService.getAll(pageSize, numberPage);
        long countPeople = personService.countPeople();
        int pages = (int) Math.ceil((double) countPeople / pageSize);
        PaginationResponse<ResponsePerson> paginationResponse = new PaginationResponse<>(numberPage, pages, paginatedList);
        return new ResponseEntity<>(gson.toJson(paginationResponse), HttpStatus.OK);
    }
}
