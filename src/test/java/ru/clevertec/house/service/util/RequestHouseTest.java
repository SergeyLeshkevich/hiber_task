package ru.clevertec.house.service.util;

import ru.clevertec.house.model.dto.request.RequestHouse;

import java.time.LocalDateTime;
import java.util.UUID;

public class RequestHouseTest implements TestBuilder{
    private LocalDateTime dateTime = TestConstant.DATE_TIME;
    private UUID houseUuid = TestConstant.HOISE_UUID;
    private String area = TestConstant.AREA;
    private String country = TestConstant.COUNTRY;
    private String city = TestConstant.CITY;
    private String street = TestConstant.STREET;
    private String number = TestConstant.NUMBER;

    @Override
    public RequestHouse build() {
        final var house = new RequestHouse(
                houseUuid,area,country,city,street,number
        );
        return house;
    }
}
