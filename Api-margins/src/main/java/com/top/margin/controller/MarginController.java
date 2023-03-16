package com.top.margin.controller;

import com.top.margin.model.Calification;
import com.top.margin.service.CalculateMargin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarginController {

    private final CalculateMargin calculateMargin;

    public MarginController(CalculateMargin calculateMargin) {
        this.calculateMargin = calculateMargin;
    }

    @GetMapping("/calculate/{documentType}/{documentValue}")
    public Calification calculateCalification(@PathVariable String documentType, @PathVariable String documentValue){
        return calculateMargin.calculate(documentType,documentValue);
    }
}
