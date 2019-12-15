package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("rest")
public class ShipController {

    @Autowired
    ShipService shipService;

    @GetMapping("ships")
    public List<Ship> getAllShips(@RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "planet", required = false) String planet,
                                  @RequestParam(name = "shipType", required = false) ShipType shipType,
                                  @RequestParam(name = "after", required = false) Long after,
                                  @RequestParam(name = "before", required = false) Long before,
                                  @RequestParam(name = "isUsed", required = false) Boolean isUsed,
                                  @RequestParam(name = "minSpeed", required = false) Double minSpeed,
                                  @RequestParam(name = "maxSpeed", required = false) Double maxSpeed,
                                  @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
                                  @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
                                  @RequestParam(name = "minRating", required = false) Double minRating,
                                  @RequestParam(name = "maxRating", required = false) Double maxRating,
                                  @RequestParam(name = "order", required = false, defaultValue = "ID") ShipOrder order,
                                  @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                  @RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize) {
        List<Ship> ships = shipService.getAllShips();
        if (name != null) {
            ships = ships.stream().filter(s -> s.getName().contains(name)).collect(Collectors.toList());
        }
        if (planet != null) {
            ships = ships.stream().filter(s -> s.getPlanet().contains(planet)).collect(Collectors.toList());
        }
        if (shipType != null) {
            ships = ships.stream().filter(s -> s.getShipType().equals(shipType)).collect(Collectors.toList());
        }
        if (after != null) {
            ships = ships.stream().filter(s -> s.getProdDate().after(new Date(after))).collect(Collectors.toList());
        }
        if (before != null) {
            ships = ships.stream().filter(s -> s.getProdDate().before(new Date(before))).collect(Collectors.toList());
        }
        if (isUsed != null) {
            ships = ships.stream().filter(s -> s.isUsed().equals(isUsed)).collect(Collectors.toList());
        }
        if (minSpeed != null) {
            ships = ships.stream().filter(s -> s.getSpeed() >= minSpeed).collect(Collectors.toList());
        }
        if (maxSpeed != null) {
            ships = ships.stream().filter(s -> s.getSpeed() <= maxSpeed).collect(Collectors.toList());
        }
        if (minCrewSize != null) {
            ships = ships.stream().filter(s -> s.getCrewSize() >= minCrewSize).collect(Collectors.toList());
        }
        if (maxCrewSize != null) {
            ships = ships.stream().filter(s -> s.getCrewSize() <= maxCrewSize).collect(Collectors.toList());
        }
        if (minRating != null) {
            ships = ships.stream().filter(s -> s.getRating() >= minRating).collect(Collectors.toList());
        }
        if (maxRating != null) {
            ships = ships.stream().filter(s -> s.getRating() <= maxRating).collect(Collectors.toList());
        }

        int tmpPageNumber = pageNumber * pageSize;
        int tmpPageSize = (pageNumber + 1) * pageSize;
        if (tmpPageSize > ships.size()) {
            tmpPageSize = ships.size();
        }
        switch (order.getFieldName()) {
            case "id":
                Comparator<Ship> byId = (s1, s2) -> s1.getId().compareTo(s2.getId());
                Collections.sort(ships, byId);
                break;
            case "speed":
                Comparator<Ship> bySpeed = (s1, s2) -> s1.getSpeed().compareTo(s2.getSpeed());
                Collections.sort(ships, bySpeed);
                break;
            case "prodDate":
                Comparator<Ship> byDate = (s1, s2) -> s1.getProdDate().compareTo(s2.getProdDate());
                Collections.sort(ships, byDate);
                break;
            case "rating":
                Comparator<Ship> byRating = (s1, s2) -> s1.getRating().compareTo(s2.getRating());
                Collections.sort(ships, byRating);
                break;
            default:
                break;
        }
        return ships.subList(tmpPageNumber, tmpPageSize);
    }

    @GetMapping("ships/count")
    public Integer getShipsCount(@RequestParam(name = "name", required = false) String name,
                                 @RequestParam(name = "planet", required = false) String planet,
                                 @RequestParam(name = "shipType", required = false) ShipType shipType,
                                 @RequestParam(name = "after", required = false) Long after,
                                 @RequestParam(name = "before", required = false) Long before,
                                 @RequestParam(name = "isUsed", required = false) Boolean isUsed,
                                 @RequestParam(name = "minSpeed", required = false) Double minSpeed,
                                 @RequestParam(name = "maxSpeed", required = false) Double maxSpeed,
                                 @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
                                 @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
                                 @RequestParam(name = "minRating", required = false) Double minRating,
                                 @RequestParam(name = "maxRating", required = false) Double maxRating) {
        List<Ship> ships = shipService.getAllShips();
        if (name != null) {
            ships = ships.stream().filter(s -> s.getName().contains(name)).collect(Collectors.toList());
        }
        if (planet != null) {
            ships = ships.stream().filter(s -> s.getPlanet().contains(planet)).collect(Collectors.toList());
        }
        if (shipType != null) {
            ships = ships.stream().filter(s -> s.getShipType().equals(shipType)).collect(Collectors.toList());
        }
        if (after != null) {
            ships = ships.stream().filter(s -> s.getProdDate().after(new Date(after))).collect(Collectors.toList());
        }
        if (before != null) {
            ships = ships.stream().filter(s -> s.getProdDate().before(new Date(before))).collect(Collectors.toList());
        }
        if (isUsed != null) {
            ships = ships.stream().filter(s -> s.isUsed().equals(isUsed)).collect(Collectors.toList());
        }
        if (minSpeed != null) {
            ships = ships.stream().filter(s -> s.getSpeed() >= minSpeed).collect(Collectors.toList());
        }
        if (maxSpeed != null) {
            ships = ships.stream().filter(s -> s.getSpeed() <= maxSpeed).collect(Collectors.toList());
        }
        if (minCrewSize != null) {
            ships = ships.stream().filter(s -> s.getCrewSize() >= minCrewSize).collect(Collectors.toList());
        }
        if (maxCrewSize != null) {
            ships = ships.stream().filter(s -> s.getCrewSize() <= maxCrewSize).collect(Collectors.toList());
        }
        if (minRating != null) {
            ships = ships.stream().filter(s -> s.getRating() >= minRating).collect(Collectors.toList());
        }
        if (maxRating != null) {
            ships = ships.stream().filter(s -> s.getRating() <= maxRating).collect(Collectors.toList());
        }
        return ships.size();
    }

    @PostMapping("ships")
    public ResponseEntity<?> createShip(@RequestBody Ship ship) {
        return shipService.addShip(ship);
    }

    @PostMapping("ships/{id}")
    public ResponseEntity<?> updateShip(@RequestBody Ship ship, @PathVariable(name = "id") long id) {
        return shipService.updateShip(ship, id);
    }

    @DeleteMapping("ships/{id}")
    public ResponseEntity<?> deleteShip(@PathVariable(name = "id") long id) {
        return shipService.deleteShip(id);
    }

    @GetMapping("ships/{id}")
    public ResponseEntity<?> getShipByID(@PathVariable(name = "id") long id) {
        return shipService.getShipByID(id);
    }

}
