package com.asignore.springmvc.controller;

import com.asignore.springmvc.dto.StatDTO;
import com.asignore.springmvc.dto.ValueDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.0")
public class RiskRestController {

    @RequestMapping(value = "/risk", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatDTO> risk(@RequestBody ValueDTO value) {

        if (ObjectUtils.isEmpty(value))
            return new ResponseEntity<StatDTO>(HttpStatus.BAD_REQUEST);

        StatDTO stat = new StatDTO();
        stat.setValue(value.getValue());
        stat.setStat(value.getValue() + 1); // cool stat

        return new ResponseEntity<StatDTO>(stat, HttpStatus.OK);
    }

    @RequestMapping(value = "/proxy", method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> permitted() {
        return new ResponseEntity<String>("pippo", HttpStatus.OK);
    }
}