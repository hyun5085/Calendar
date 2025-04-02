package com.example.calendar.repository;

import com.example.calendar.entity.Consummer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface ConsummerRepository extends JpaRepository<Consummer, Long> {

    Optional<Consummer> findConsummerByConsummerName(String consummerName);

    default Consummer findConsummerByConsummerNameOrelseThrow(String consummerName){
        return findConsummerByConsummerName(consummerName).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Does not exist username = " + consummerName));


    }

    default Consummer findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(()->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }

}

