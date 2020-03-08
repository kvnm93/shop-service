package de.leuphana.shopservice;

import de.leuphana.customerservice.component.structure.Customer;
import de.leuphana.shopservice.component.structure.LoginDetails;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.security.Key;
import java.util.Collections;


@SpringBootApplication
@ComponentScan(basePackages = {"de.leuphana.shopservice"})
@ServletComponentScan
public class ShopService {

    private WebClient webClient;
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public Key getKey() {
        return key;
    }

    @PostConstruct
    public void init() {
        this.webClient = WebClient
                .builder()
                .baseUrl("http://localhost:8050/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

    }

    public static void main(String[] args) {
        SpringApplication.run(ShopService.class);
    }

    public Customer loginUser(LoginDetails loginDetails) {

        WebClient.RequestBodySpec request = this.webClient
                .method(HttpMethod.GET)
                .uri("/customer/?username=" + loginDetails.getUsername());

        Customer customer = request.retrieve().bodyToMono(Customer.class).block();

        if (customer != null && loginDetails.getUsername().equals(customer.getUserName()) && loginDetails.getPassword().equals(customer.getPassword())) {
            return customer;
        } else {
            throw new IllegalArgumentException();
        }

    }
}
