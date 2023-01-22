package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;

    private CartService cartService;

    private ProductsHistory productsHistory;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        productsHistory = ProductsHistory.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        request.setAttribute("history", productsHistory.getProducts(request));
        request.setAttribute("products", productDao.findProducts(query,
                Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)
        ));
                request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long productId = Long.parseLong(request.getParameter("productId"));
            try {
                NumberFormat format = NumberFormat.getInstance(request.getLocale());
                int quantity = format.parse(request.getParameter("quantity")).intValue();
                cartService.add(cartService.getCart(request), productId, quantity);
                response.sendRedirect(request.getContextPath() + "/products?message=Product added to cart!");
                return;
            } catch (NumberFormatException | ParseException e) {
                request.setAttribute("error", "Invalid quantity");
            } catch (OutOfStockException e) {
                request.setAttribute("error", "not enough stock available");
            }
            doGet(request, response);
        } catch (Exception e) {
            doGet(request, response);
        }
    }

}