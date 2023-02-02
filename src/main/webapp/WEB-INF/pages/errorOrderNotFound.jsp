<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="exception" value="${requestScope['javax.servlet.error.exception']}"/>
<tags:master pageTitle="Order not found">
  <h1>Order not found</h1>
</tags:master>