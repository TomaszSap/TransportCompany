package com.example.TransportCompany.controller;

import com.example.TransportCompany.model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
//@RequestMapping("forwarded")
public class ForwarderController extends RestEndpoint {
    @PatchMapping("/user")
    public ResponseEntity<String> updateUser(@RequestParam int id, @Valid @RequestBody Employee employee)
    {  return super.updateUser(id,employee);
    }
}
