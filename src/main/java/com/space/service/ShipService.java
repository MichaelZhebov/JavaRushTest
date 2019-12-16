package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ShipService {

    @Autowired
    ShipRepository repo;

    public List<Ship> getAllShips() {
        return repo.findAll();
    }

    public ResponseEntity<?> addShip(Ship ship) {
        if (ship.getName() == null || ship.getPlanet() == null || ship.getShipType() == null || ship.getProdDate() == null ||
                ship.getSpeed() == null || ship.getCrewSize() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ship.getName().length() >= 50 || ship.getPlanet().length() >= 50) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ship.getName().isEmpty() || ship.getPlanet().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        double scale = Math.pow(10, 2);
        double speed = Math.round(ship.getSpeed() * scale) / scale;
        if (speed < 0.01 || speed > 0.99 || ship.getCrewSize() <= 0 || ship.getCrewSize() > 9999) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ship.getProdDate().getTime() < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        LocalDate date = ship.getProdDate().toLocalDate();
        int prodYear = date.getYear();
        if (prodYear < 2800 || prodYear > 3019) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Ship existShip = repo.findByName(ship.getName());
        if (existShip != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        double k = ship.isUsed() ? 0.5 : 1;
        double rating = Math.round(((80 * speed * k) / (3019 - date.getYear() + 1)) * scale) / scale;
        Ship newShip = new Ship();
        newShip.setName(ship.getName());
        newShip.setPlanet(ship.getPlanet());
        newShip.setShipType(ship.getShipType());
        newShip.setProdDate(ship.getProdDate());
        newShip.setUsed(ship.isUsed());
        newShip.setSpeed(ship.getSpeed());
        newShip.setCrewSize(ship.getCrewSize());
        newShip.setRating(rating);
        repo.save(newShip);
        return new ResponseEntity<>(newShip, HttpStatus.OK);
    }

    public ResponseEntity<?> updateShip(Ship newShip, long updateShipID) {
        if (updateShipID == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Ship existShip = repo.findById(updateShipID);
        if (existShip == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (newShip.getName() == null && newShip.getPlanet() == null && newShip.getShipType() == null && newShip.getProdDate() == null &&
                newShip.getSpeed() == null && newShip.getCrewSize() == null) {
            return new ResponseEntity<>(existShip, HttpStatus.OK);
        }
        if (newShip.getName() != null) {
            if (newShip.getName().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        String valid = String.valueOf(updateShipID);
        if (!valid.matches("\\d+")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (newShip.getName() != null) {
            if (newShip.getName().length() >= 50) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                existShip.setName(newShip.getName());
            }
        }
        if (newShip.getPlanet() != null) {
            if (newShip.getPlanet().length() >= 50) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                existShip.setPlanet(newShip.getPlanet());
            }
        }
        if (newShip.getShipType() != null) {
            existShip.setShipType(newShip.getShipType());
        }
        existShip.setUsed(newShip.isUsed());
        if (newShip.getProdDate() != null) {
            existShip.setProdDate(newShip.getProdDate());
        }
        double scale = Math.pow(10, 2);
        if (newShip.getSpeed() != null) {
            double speed = Math.round(newShip.getSpeed() * scale) / scale;
            if (speed < 0.01 || speed > 0.99) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                existShip.setSpeed(speed);
            }
        }
        if (newShip.getCrewSize() != null) {
            if (newShip.getCrewSize() <= 0 || newShip.getCrewSize() > 9999) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                existShip.setCrewSize(newShip.getCrewSize());
            }
        }
        if (newShip.getProdDate() != null) {
            LocalDate date = newShip.getProdDate().toLocalDate();
            int prodYear = date.getYear();
            if (prodYear < 2800 || prodYear > 3019) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        LocalDate localDatedate = existShip.getProdDate().toLocalDate();
        double k = existShip.isUsed() ? 0.5 : 1;
        double rating = Math.round(((80 * existShip.getSpeed() * k) / (3019 - localDatedate.getYear() + 1)) * scale) / scale;
        existShip.setRating(rating);
        repo.save(existShip);
        return new ResponseEntity<>(existShip, HttpStatus.OK);

    }

    public ResponseEntity<String> deleteShip(long deleteShipID) {
        if (deleteShipID == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String valid = String.valueOf(deleteShipID);
        if (!valid.matches("\\d+")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Ship existShip = repo.findById(deleteShipID);
        if (existShip == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repo.delete(existShip);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> getShipByID(long shipID) {
        String valid = String.valueOf(shipID);
        if (shipID == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!valid.matches("[0-9]+")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Ship existShip = repo.findById(shipID);
        if (existShip == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(existShip, HttpStatus.OK);
    }


}
