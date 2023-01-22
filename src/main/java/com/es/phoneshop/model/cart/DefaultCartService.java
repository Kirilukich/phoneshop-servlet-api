package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

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
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);

        if (product.getStock() < quantity || quantity < 0) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }

        for (int i = 0; i < cart.getItems().size(); i++) {

            if (cart.getItems().get(i).getProduct().getCode().equals(product.getCode()) && product.getStock() >= cart.getItems().get(i).getQuantity() + quantity) {
                cart.getItems().get(i).setQuantity(cart.getItems().get(i).getQuantity() + quantity);
                cart.getItems().set(i, cart.getItems().get(i));
                return;
            } else if (product.getStock() <= cart.getItems().get(i).getQuantity() + quantity) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
        }
        cart.getItems().add(new CartItem(product, quantity));
        recalculateCart(cart);
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

            if (cart.getItems().get(i).getProduct().getCode().equals(product.getCode()) && product.getStock() >= cart.getItems().get(i).getQuantity() + quantity) {
                cart.getItems().get(i).setQuantity(quantity);
                cart.getItems().set(i, cart.getItems().get(i));
                recalculateCart(cart);
                return;
            } else if (product.getStock() <= cart.getItems().get(i).getQuantity() + quantity) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
        }
        cart.getItems().add(new CartItem(product, quantity));
        recalculateCart(cart);
    }
}
