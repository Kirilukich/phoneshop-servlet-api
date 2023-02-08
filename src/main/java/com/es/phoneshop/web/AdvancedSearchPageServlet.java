package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SearchMethod;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedSearchPageServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SearchMethod searchMethod = SearchMethod.all;
        String query = request.getParameter("query");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");

        Map<String, String> errors = new HashMap<>();

        BigDecimal minPrice = parseBigDecimal(minPriceStr, errors, "errorMinPrice");
        BigDecimal maxPrice = parseBigDecimal(maxPriceStr, errors, "errorMaxPrice");

        List<Product> findProducts = new ArrayList<>();

        if (errors.isEmpty()) {
            findProducts = productDao.findProducts(query, minPrice, maxPrice, searchMethod);
            request.setAttribute("searchMethod", searchMethod);
        } else if (query != null || minPrice != null || maxPrice != null) {
            request.setAttribute("errors", errors);
        }
        request.setAttribute("products",findProducts);
        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
    }

    private BigDecimal parseBigDecimal(String value, Map<String, String> errors, String errorName) {
        if (value != null && !value.isEmpty()) {
            try {
                return BigDecimal.valueOf(Long.parseLong(value));
            } catch (NumberFormatException ex) {
                errors.put(errorName, "It's not a number");
            }
        }
        return null;
    }
}
