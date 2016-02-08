<%@ page import="com.azhaurov.testwebapp.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Прайс-лист</title>
</head>
<body>
<p><h2>Прайс-лист</h2></p>

<form action="DBSearchServlet" value="find" method="POST">

    <% String prod_cat_value = (String)request.getAttribute("prod_cat_value") == null ? "" : (String)request.getAttribute("prod_cat_value");%>
    <% String prod_name_value = (String)request.getAttribute("prod_name_value") == null ? "" : (String)request.getAttribute("prod_name_value");%>
    <% String price_min_value = (String)request.getAttribute("price_min_value") == null ? "" : (String)request.getAttribute("price_min_value");%>
    <% String price_max_value = (String)request.getAttribute("price_max_value") == null ? "" : (String)request.getAttribute("price_max_value");%>

    <table width="100%" border="0" cellpadding="1" cellspacing="5">
        <tr border="0">
            <td><b>Категория:</b></td>
            <td><b>Наименование:</b></td>
            <td><b>Цена от:</b></td>
            <td><b>Цена до:</b></td>
            <td>&nbsp;</td>
        </tr>
        <tr  border="1">
            <!-- шаблон специально не задан, можно использовать символ % для получения полного списка товаров или категорий -->
            <td><input type = "text" name = "prod_cat" value="<%=prod_cat_value%>"/></td>
            <td><input type = "text" name = "prod_name" value="<%=prod_name_value%>"/></td>
            <!-- целое число либо число с плавающей точкой (разделитель точка) -->
            <td><input type = "text" pattern="\-?\d+(\.\d{0,})?" name = "price_min" value="<%=price_min_value%>"/></td>
            <td><input type = "text" pattern="\-?\d+(\.\d{0,})?" name = "price_max" value="<%=price_max_value%>"/></td>
            <td>&nbsp;&nbsp;<input type="submit" value="Найти" /></td>
        </tr>
    </table>

    <% String msg = (String)request.getAttribute("message"); if (msg!=null) { %>
    <script type="text/javascript">
        alert('<%=msg%>');
    </script>
    <%}%>
</form>

<% boolean qr = (Boolean)request.getAttribute("query"); if (qr) { %>

<table width="100%" border="0" cellpadding="1" cellspacing="5">
    <tr bgcolor="#87ceeb">
        <td><b>Категория</b></td>
        <td><b>Наименование</b></td>
        <td><b>Цена</b></td>
      </tr>

    <%
        List<Prod> prods = ((List)request.getAttribute("data"));
        for(Prod pr : prods)  {
    %>
    <tr><td><%=pr.getCat().getName()%></td><td><%=pr.getName()%></td><td><%=pr.getPrice()%></td></tr>
    <%
        }
    %>
</table>

<%}%>

</body>
</html>
