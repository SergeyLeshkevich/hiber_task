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
import ru.clevertec.house.model.dto.request.RequestHouse;
import ru.clevertec.house.model.dto.response.ResponseHouse;
import ru.clevertec.house.service.HouseService;
import ru.clevertec.house.util.PaginationResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/houses")
public class HouseController {

    private final HouseService houseService;
    private final Gson gson;

    @PostMapping
    public ResponseEntity<String> createHouse(@RequestBody RequestHouse house) {
        UUID responseUuid = houseService.create(house);
        return new ResponseEntity<>(gson.toJson(houseService.get(responseUuid)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getHouseById(@PathVariable("id") UUID id) {
        ResponseHouse house = houseService.get(id);
        return new ResponseEntity<>(gson.toJson(house), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateHouseById(@PathVariable("id") UUID id,
                                                  @RequestBody RequestHouse person,
                                                  @RequestBody Set<UUID> registeredPeople) {
        UUID responseUuid = houseService.update(id, person, registeredPeople);
        return new ResponseEntity<>(gson.toJson(houseService.get(responseUuid)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonById(@PathVariable("id") UUID id) {
        houseService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAllPersons(@RequestParam(defaultValue = "15", name = "pageSize") int pageSize,
                                                @RequestParam(defaultValue = "1", name = "numberPage") int numberPage) {
        List<ResponseHouse> paginatedList = houseService.getAll(pageSize, numberPage);
        long countPeople = houseService.countPeople();
        int pages = (int) Math.ceil((double) countPeople / pageSize);
        PaginationResponse<ResponseHouse> paginationResponse = new PaginationResponse<>(numberPage, pages, paginatedList);
        return new ResponseEntity<>(gson.toJson(paginationResponse), HttpStatus.OK);
    }
}