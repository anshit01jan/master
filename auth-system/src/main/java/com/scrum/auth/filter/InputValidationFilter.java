package com.scrum.auth.filter;

import com.scrum.auth.security.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class InputValidationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(InputValidationFilter.class);
    private final InputValidator validator;

    public InputValidationFilter(InputValidator validator) {
        this.validator = validator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {
        validateHeaders(req);
        chain.doFilter(req, res);
    }

    private void validateHeaders(HttpServletRequest req) {
        Enumeration<String> headers = req.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            String value = req.getHeader(header);
            if (value != null && value.contains("<?")) {
                logger.warn("Potential injection detected in header: {}", header);
            }
        }
    }
}
