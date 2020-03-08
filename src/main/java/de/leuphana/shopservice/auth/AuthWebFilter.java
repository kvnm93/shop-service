package de.leuphana.shopservice.auth;

import de.leuphana.shopservice.ShopService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class AuthWebFilter implements Filter {

    @Autowired
    private ShopService shopService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String bearerToken = request.getHeader("Bearer");

        if (request.getRequestURI().equals("/api/v1/auth/login/")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (bearerToken == null || bearerToken.equals("")) {
            response.setStatus(403);
        }

        try {
            String customerId = Jwts.parserBuilder().setSigningKey(shopService.getKey()).build().parseClaimsJws(bearerToken).getBody().getSubject();
            request.setAttribute("customerId", customerId);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(403);
        }
    }
}
