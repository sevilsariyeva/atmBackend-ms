package com.example.atmbackend_ms.controller;

import com.example.atmbackend_ms.context.AtmContext;
import com.example.atmbackend_ms.model.enums.AtmState;
import com.example.atmbackend_ms.service.AtmService;
import com.example.atmbackend_ms.util.HttpResponseConstants;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/atm")
@RequiredArgsConstructor
public class AtmController {
    private final AtmService atmService;

    private final AtmContext atmContext;

    @PostMapping("/insertCard")
    public ResponseEntity<String> insertCard(@RequestParam String cardNumber) {
        return Optional.ofNullable(atmService.insertCard(cardNumber))
                .filter(response-> HttpResponseConstants.INSERT_SUCCESS.equals(response))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpResponseConstants.INSERT_EX));

    }
    @PostMapping("/pin")
    public String enterPin(@RequestParam String cardNumber, @RequestParam Integer pin){
        atmContext.setAtmState(AtmState.SELECT_TRANSACTION);
        return atmService.validatePin(atmContext.getCardNumber(), pin);
    }

    @GetMapping("/balance")
    public String checkBalance(@RequestParam String cardNumber){
        return atmService.checkBalance(cardNumber);
    }
}
