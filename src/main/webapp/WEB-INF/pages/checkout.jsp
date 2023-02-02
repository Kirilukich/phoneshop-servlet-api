<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
    <br>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">
            There were errors placing order
        </div>
    </c:if>
    <br>
    <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
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
            </tr>
            </thead>
            <c:forEach var="item" items="${order.items}" varStatus="status">
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
                            ${item.quantity}
                    </td>
                    <td class="price">
                        <fmt:formatNumber value="${item.product.price}" type="currency"
                                          currencySymbol="${item.product.currency.symbol}"/>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td>Delivery cost:</td>
                <td>${order.deliveryCost}</td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td>Subtotal:</td>
                <td>${order.subtotal}</td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td>Total cost:</td>
                <td>${order.totalCost}</td>
            </tr>
        </table>
        <h2>Your details</h2>
        <table>
            <tags:orderFormRow name="firstName" label="First name" order="${order}" errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="lastName" label="Last name" order="${order}" errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="phone" label="Phone" order="${order}" errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="deliveryDate" label="Delivery date (dd.mm.yyyy)" order="${order}" errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="deliveryAddress" label="Delivery Address" order="${order}" errors="${errors}"></tags:orderFormRow>

            <tr>
                <td>Payment method<span style="color:red"> *</span></td>
                <td>
                    <select name="paymentMethod">
                        <option></option>
                        <c:forEach var="paymentMethod" items="${paymentMethods}">
                            <c:choose>
                                <c:when test="${paymentMethod eq order.paymentMethod}">
                                    <selected>${paymentMethod}</selected>
                                </c:when>
                                <c:otherwise>
                                    <option>${paymentMethod}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${not empty order.paymentMethod}">
                            <option selected>${order.paymentMethod}</option>
                        </c:if>
                    </select>
                    <c:set var="error" value="${errors['paymentMethod']}"/>
                    <c:if test="${not empty error}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <p>
            <button>Place order</button>
        </p>
    </form>

    <form id="deleteCartItem" method="post">

    </form>
</tags:master>

