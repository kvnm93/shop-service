package de.leuphana.shopservice.connector;

import de.leuphana.customerservice.component.structure.Customer;
import de.leuphana.shopservice.ShopService;
import de.leuphana.shopservice.component.structure.LoginDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Key;

@RestController
public class ApiGatewayRestController {

    @Autowired
    private ShopService shopService;

    public String toJWT(Customer customer) {
        return Jwts.builder().setSubject(customer.getCustomerId().toString()).signWith(shopService.getKey()).compact();
    }

    @PostMapping("/api/v1/auth/login")
    public String loginUser (@RequestBody LoginDetails loginDetails) {
        return this.toJWT(shopService.loginUser(loginDetails));
    }

    @GetMapping("/api/v1/article")
    public String getAllArticles() {
        return "Test";
    }





}
