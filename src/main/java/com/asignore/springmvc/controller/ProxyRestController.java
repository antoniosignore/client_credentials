package com.asignore.springmvc.controller;

import com.asignore.springmvc.dto.StatDTO;
import com.asignore.springmvc.dto.ValueDTO;
import com.asignore.springmvc.model.AuthTokenInfo;
import com.asignore.springmvc.service.RestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProxyRestController {

    @Autowired
    RestService restService;

    @RequestMapping(value = "/proxy", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatDTO> proxy(@RequestBody ValueDTO value) throws JsonProcessingException {

        if (ObjectUtils.isEmpty(value))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        AuthTokenInfo authTokenInfo = restService.sendTokenRequest();
        StatDTO risk = restService.risk(value, authTokenInfo.getAccess_token());
        return new ResponseEntity<>(risk, HttpStatus.OK);
    }
}