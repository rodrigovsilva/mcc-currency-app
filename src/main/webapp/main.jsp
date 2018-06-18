<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Convert a currency</title>

    <jsp:include page="header.jsp"/>

</head>
<body>
    <div class="container">

        <!-- this is header -->
        <nav class="navbar navbar-inverse">
            <div class="container">
                <div class="navbar-header">
                    <span class="navbar-brand">My Currency Converter</span>
                </div>
                <div id="navbar" class="collapse navbar-collapse">
                    <!--<ul class="nav navbar-nav">
                        <li class="active"><a th:href="@{/}">Home</a></li>
                    </ul>-->
                </div>
            </div>
        </nav>

        <form:form method="POST" action="${contextPath}/convert" modelAttribute="conversionFormData">
            <div class="row">
                <div class="col-xs-3 form-group">
                    <label>Exchange</label>
                    <spring:bind path="exchangeFrom">
                        <form:select path="exchangeFrom" class="form-control">
                            <form:option value="" label="Select an exchange"/>
                            <form:options items = "${availableCurrencies}" />
                        </form:select>
                    </spring:bind>
                </div>
                <div class="col-xs-3 form-group">
                    <label>Exchange</label>
                    <spring:bind path="exchangeTo">
                        <form:select path="exchangeTo" class="form-control">
                            <form:option value="" label="Select an exchange"/>
                            <form:options items = "${availableCurrencies}" />
                        </form:select>
                    </spring:bind>
                </div>
                <div class="col-xs-3 form-group">
                    <label class="control-label" for="date">Date</label>
                    <spring:bind path="timestamp">
                        <form:input class="form-control" id="timestamp" path="timestamp" placeholder="MM/DD/YYY" type="text"/>
                    </spring:bind>
                </div>
                <div class="col-xs-3 form-group">
                    <label>&nbsp;</label>
                    <input type="submit" value="Convert" class="btn btn-primary form-control"/>
                </div>
            </div>
            <c:if test="${not empty conversionRate}">
                <div class="row col-md-12">
                    <h3>1 <c:out value="${conversionRate.exchangeFrom}"/> = <fmt:formatNumber value="${conversionRate.rate}" minFractionDigits="2" maxFractionDigits="2"/><c:out value="${conversionRate.exchangeTo}"/></h3>
                </div>
            </c:if>
        </form:form>

        <c:if test="${not empty historicalConversions}">
            <div class="row col-md-12">
                <h4>Last 10 conversions</h4>
                <table id="tableClient" class="table table-bordered table-striped col-md-12">
                    <thead>
                        <tr>
                            <th class="col-md-3">Date</th>
                            <th class="col-md-3">From</th>
                            <th class="col-md-3">To</th>
                            <th class="col-md-3">Rate</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="conversion" items="${historicalConversions}">
                            <tr>
                                <td class="col-md-3"><fmt:formatDate type="both" value="${conversion.createdAt.time}"/></td>
                                <td class="col-md-3"><c:out value="${conversion.exchangeFrom}"/></td>
                                <td class="col-md-3"><c:out value="${conversion.exchangeTo}"/></td>
                                <td class="col-md-3"><fmt:formatNumber value="${conversion.rate}" minFractionDigits="2" maxFractionDigits="2"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <footer>
            <c:if test="${pageContext.request.userPrincipal.name != null}">
                <form id="logoutForm" method="POST" action="${contextPath}/logout">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
                <span>Welcome ${pageContext.request.userPrincipal.name} | <a onclick="document.forms['logoutForm'].submit()" class="cursor-pointer">Logout</a></span>
            </c:if>
        </footer>
    </div>
    <!-- /container -->
    <jsp:include page="scripts.jsp"/>
    <script type="text/javascript">
        $(function () {
            $('#timestamp').datetimepicker();
        });
    </script>
</body>
</html>
