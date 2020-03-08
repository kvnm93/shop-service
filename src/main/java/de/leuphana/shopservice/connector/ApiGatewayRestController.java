package de.leuphana.shopservice.connector;

import de.leuphana.articleservice.component.structure.Article;
import de.leuphana.customerservice.component.structure.Customer;
import de.leuphana.orderservice.component.structure.Order;
import de.leuphana.shopservice.ShopService;
import de.leuphana.shopservice.component.structure.LoginDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    public List<Article> getAllArticles() {
        return this.shopService.getAllArticles();
    }

    @PostMapping("/api/v1/article/{articleId}/")
    public Article updateArticle(@PathVariable("articleId") UUID articleId, @RequestBody Article article) {
        return this.shopService.updateArticleByArticleId(articleId, article);
    }

    @PostMapping("/api/v1/article/")
    public Article updateArticle(@RequestBody Article article) {
        return this.shopService.upsertArticle(article);
    }

    @PostMapping("/order/")
    public Order createOrder(@RequestBody Order order) {
        return shopService.createOrder(order);
    }

    @GetMapping("/order/")
    public List<Order> getOrdersByCustomerId(@RequestParam UUID customerId) {
        return shopService.getOrdersByCustomerId(customerId);
    }



}
