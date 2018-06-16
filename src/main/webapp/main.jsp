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

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
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

        <form:form modelAttribute="conversionForm" method="POST" action="${contextPath}/convert">
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
                    <label>Date</label>
                    <spring:bind path="timestamp">
                        <form:input path="timestamp" class="form-control"/>
                    </spring:bind>
                </div>
                <div class="col-xs-3 form-group">
                    <label>&nbsp;</label>
                    <input type="submit" value="Convert" class="btn btn-primary form-control"/>
                </div>
            </div>
            <c:if test="${not empty conversionRate}">
                <div class="row col-md-12">
                    <h3>1 <c:out value="${conversionRate.exchangeFrom}"/> = <c:out value="${conversionRate.rate}"/> <c:out value="${conversionRate.exchangeTo}"/></h3>
                </div>
            </c:if>
        </form:form>


        <c:if test="${not empty historicalConversions}">
            <div class="row col-md-12">
                <h2>Last 10 conversions</h2>
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
                                <td class="col-md-3"><c:out value="${conversion.rate}"/></td>
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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
