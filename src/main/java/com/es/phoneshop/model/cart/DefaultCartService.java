package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Optional;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static class SingletonHelper {
        private static final DefaultCartService INSTANCE = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return DefaultCartService.SingletonHelper.INSTANCE;
    }

    @Override
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }
        return cart;
    }

    @Override
    public synchronized void add(HttpServletRequest request, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        Cart cart = getCart(request);
        if (product.getStock() < quantity || quantity < 0) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }

        for (int i = 0; i < cart.getItems().size(); i++) {

            CartItem cartItem = cart.getItems().get(i);
            if (cartItem.getProduct().getCode().equals(product.getCode()) && product.getStock() >= cartItem.getQuantity() + quantity) {
                changeQuantity(cart, cartItem.getQuantity() + quantity, i, cartItem);
                return;
            } else if (cartItem.getProduct().getCode().equals(product.getCode()) && product.getStock() <= cartItem.getQuantity() + quantity) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
        }
        cart.getItems().add(new CartItem(product, quantity));
        recalculateCart(cart);
    }

    private Optional<Product> findItemToUpdate(Long productId, Cart cart) {
        return cart.getItems().stream()
                .map(CartItem::getProduct)
                .filter(product -> productId.equals(product.getId()))
                .findAny();
    }

    @Override
    public void delete(Cart cart, Long productId) {
        cart.getItems().removeIf(item ->
                productId.equals(item.getProduct().getId())
        );
        recalculateCart(cart);
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity)
                .mapToInt(q -> q)
                .sum()
        );
        BigDecimal sumPrice = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            sumPrice = sumPrice.add(item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        cart.setTotalCost(sumPrice);
    }

    @Override
    public synchronized void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);

        if (product.getStock() < quantity || quantity < 0) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }

        for (int i = 0; i < cart.getItems().size(); i++) {

            CartItem cartItem = cart.getItems().get(i);
            if (cartItem.getProduct().getCode().equals(product.getCode()) && product.getStock() >= cartItem.getQuantity()) {
                changeQuantity(cart, quantity, i, cartItem);
                return;
            } else if (cartItem.getProduct().getCode().equals(product.getCode()) && product.getStock() <= cartItem.getQuantity()) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
        }
        recalculateCart(cart);
    }

    private void changeQuantity(Cart cart, int quantity, int i, CartItem cartItem) {
        cartItem.setQuantity(quantity);
        cart.getItems().set(i, cart.getItems().get(i));
        recalculateCart(cart);
    }

    @Override
    public void clearCart(Cart cart) {
        cart.getItems().removeAll(cart.getItems());
        recalculateCart(cart);
    }
}
