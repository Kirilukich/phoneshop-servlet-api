<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="history" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <form>
        <input name="query" value="${param.query}">
        <button>Search</button>
    </form>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sortLink sort="description" order="asc"/>
                <tags:sortLink sort="description" order="desc"/>
            </td>
            <td class="price">
                Price
                <tags:sortLink sort="price" order="asc"/>
                <tags:sortLink sort="price" order="desc"/>
            </td>
            <td class="quantity">
                Quantity
            </td>
        </tr>
        </thead>
        <form method="post">
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                            ${product.description}
                    </a>
                </td>
                <td class="price">
                    <div title=${product.getHistories()}>
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </div>
                </td>
                <td>
                    <form method="post" action="${pageContext.servletContext.contextPath}/products">
                        <label>
                            <input name="quantity" class="quantity" value="${not empty param.error and param.productId eq product.id ? param.quantity : 1}"/>
                        </label>
                        <input type="hidden" name="productId" value="${product.id}"/>
                        <c:if test="${param.productId eq product.id}">
                            <div class="error">
                                    ${param.error}
                            </div>
                        </c:if>
                        <button>
                            Add to cart
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </form>
    </table>
    <table>
        <h3>Recently viewed</h3>
        <c:forEach var="product" items="${history}">
            <td>
                <p>
                    <img class="product-tile" src="${product.imageUrl}">
                </p>
                <p>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}"></a>
                        ${product.description}
                </p>
                <p>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}"></a>
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </p>
            </td>
        </c:forEach>
    </table>
    <form id="addCartItem" method="post">

    </form>
</tags:master>
