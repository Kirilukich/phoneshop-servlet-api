package com.es.phoneshop.model.order;

public class OrderNotFoundException extends RuntimeException{
    private Long id;

    public OrderNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
