<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
    <p>
    Cart: ${cart}, total quantity: ${cart.totalQuantity}
    <c:if test="${empty cart.items}">
        <h1>
            Sorry, your cart is empty
        </h1>
    </c:if>
    </p>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">
            There were errors updating cart
        </div>
    </c:if>
    <form method="post" action="${pageContext.servletContext.contextPath}/cart">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td class="quantity">
                    Quantity
                </td>
                <td class="price">
                    Price
                </td>
                <td>

                </td>
            </tr>
            </thead>
            <c:forEach var="item" items="${cart.items}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src="${item.product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                                ${item.product.description}
                        </a>
                    </td>
                    <td class="quantity">
                        <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                        <c:set var="error" value="${errors[item.product.id]}"/>
                        <input name="quantity"
                               value="${not empty error ? paramValues['quantity'][status.index] : item.quantity}"
                               class="quantity"/>
                        <c:if test="${not empty error}">
                            <div class="error">
                                    ${errors[item.product.id]}
                            </div>
                        </c:if>
                        <input type="hidden" name="productId" value="${item.product.id}"/>
                    </td>
                    <td class="price">
                        <fmt:formatNumber value="${item.product.price}" type="currency"
                                          currencySymbol="${item.product.currency.symbol}"/>
                    </td>
                    <td>
                        <button form="deleteCartItem"
                                formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">
                            Delete
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td>Total cost</td>
                <td>${cart.totalCost}</td>
            </tr>
        </table>
        <p>
            <c:if test="${not empty cart.items}">
                <button>Update</button>
            </c:if>
        </p>
    </form>
    <form action="${pageContext.servletContext.contextPath}/checkout">
        <c:if test="${not empty cart.items}">
            <button>Checkout</button>
        </c:if>
    </form>
    <table>
        <h3>Recently viewed</h3>
        <c:forEach var="product" items="${history}">
            <td>
                <p class="info">
                    <img class="product-tile" src="${product.imageUrl}">
                </p>
                <p class="info">
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}"></a>
                        ${product.description}
                </p>
                <p class="info">
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}"></a>
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </p>
            </td>
        </c:forEach>
    </table>
    <form id="deleteCartItem" method="post">

    </form>
</tags:master>
