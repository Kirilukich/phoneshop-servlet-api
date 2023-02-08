<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="searchMethod" type="com.es.phoneshop.model.product.SearchMethod" scope="request"/>
<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Advanced search">
    <h1>
        Advanced search
    </h1>
    <c:if test="${not message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">
            There was an error with the search
        </div>
    </c:if>
    </p>

    <form method="get" action="${pageContext.servletContext.contextPath}/advanced-search">
        <table>
            <tr>
                <td>
                    Description
                </td>
                <td>
                    <input name="query" value="${param.query}">

                    <select name="searchMethod">
                    </select>

                </td>
            <tr>
            </tr>
            </tr>
            <tr>
                <td>
                    Min Price
                </td>
                <td>
                    <c:set var="error" value="${errors['minPriceError']}"/>
                    <input name="minPrice"
                           value="${param.minPrice}">
                    <c:if test="${not empty error}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td>
                    Max Price
                </td>
                <td>
                    <c:set var="error" value="${errors['maxPriceError']}"/>
                    <input name="maxPrice" value="${param.maxPrice}">
                    <c:if test="${not empty error}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <br>
        <button>Search</button>
    </form>
    <c:if test="${not empty products and empty errors}">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>Description</td>
                <td class="price">Price</td>
            </tr>
            </thead>
            <c:forEach var="product" items="${products}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile"
                             src="${product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                                ${product.description}
                        </a>
                    </td>
                    <td class="price">
                        <a href="${pageContext.servletContext.contextPath}/products/priceHistory?productId=${product.id}">
                            <fmt:formatNumber value="${product.price}" type="currency"
                                              currencySymbol="${product.currency.symbol}"/>
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</tags:master>

