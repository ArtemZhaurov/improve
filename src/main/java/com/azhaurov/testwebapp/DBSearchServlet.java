package com.azhaurov.testwebapp;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import com.azhaurov.testwebapp.entity.Cat;
import com.azhaurov.testwebapp.entity.Prod;

@WebServlet(name = "DBSearchServlet")
public class DBSearchServlet extends HttpServlet {
    private EntityManager em;

    @Override
    public void init() throws ServletException {

    }

    protected boolean processRequestEntityManager(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String queryString, queryProdCat, queryProdName,queryPriceMin,queryPriceMax;
        byte qStrLen;

        request.setCharacterEncoding("UTF-8");
        request.setAttribute("message", null);

        // Считаем количество заполненных полей
        // Выбираем ВСЕ поля, поскольку в форме mainform.jsp имена заданы только для тех полей, которые влияют на условия запроса
        Enumeration en = request.getParameterNames();
        int param_counter = 0;
        while(en.hasMoreElements()) {
            if (request.getParameter((String)en.nextElement()).length()>0) param_counter++;
        }

        // Если ни одно поле не заполнено, запрос не выполняем и возвращаем пустую начальную форму
        if (param_counter==0) {
            request.setAttribute("message", "Необходимо заполнить хотя бы одно поле!");
            return false;
        }

        // В противном случае строим запрос
        em = Persistence.createEntityManagerFactory("IMPROVE").createEntityManager();
        queryString = "from Prod p where";
        qStrLen = (byte)queryString.length();

        if (request.getParameter("prod_cat").length()>0) {
            queryProdCat = request.getParameter("prod_cat");
            // Выбираем ID категорий по их названиям
            Query cat_names = em.createQuery("from Cat c where c.name like('" + queryProdCat + "%')");
            String catIds = "''";
            if (cat_names.getResultList().size()>0) {
                catIds = "";
                List<Cat> catNames = cat_names.getResultList();
                for (Cat c : catNames) catIds += c.getId() + ",";
                catIds = catIds.substring(0, catIds.length() - 1);
            }
            // Продолжить основной запрос
            if (queryString.length() > qStrLen) queryString += " and";
            queryString += " p.cat in(" + catIds + ")";
            request.setAttribute("prod_cat_value", queryProdCat);
        }

        if (request.getParameter("prod_name").length()>0) {
            queryProdName = request.getParameter("prod_name");
            if (queryString.length() > qStrLen) queryString += " and";
            queryString += " p.name like('" + queryProdName + "%')";
            request.setAttribute("prod_name_value", queryProdName);
        }

        if (request.getParameter("price_min").length()>0) {
            queryPriceMin = request.getParameter("price_min");
            if (queryString.length() > qStrLen) queryString += " and";
            queryString += " p.price >=(" + queryPriceMin + ")";
            request.setAttribute("price_min_value", queryPriceMin);
        }

        if (request.getParameter("price_max").length()>0) {
            queryPriceMax = request.getParameter("price_max");
            if (queryString.length() > qStrLen) queryString += " and";
            queryString += " p.price <=(" + queryPriceMax + ")";
            request.setAttribute("price_max_value", queryPriceMax);
        }

        try {
            Query query = em.createQuery(queryString);
            List<Prod> dataList = query.getResultList();
            if (dataList.size()==0) {
                request.setAttribute("message", "Ничего не найдено!");
                return false;
            }
            request.setAttribute("data", dataList);
            request.setAttribute("query", true);
            return true;
        } catch (Exception e) {
            request.setAttribute("message","Ошибка в запросе: select * " + queryString);
            //e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Признак того, что запрос не выполнялся (либо выполнился с ошибкой) и отображать результаты запроса не нужно
        request.setAttribute("query", false);
        RequestDispatcher dispatcher = request.getRequestDispatcher("mainform.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("query", processRequestEntityManager(request, response));
        RequestDispatcher dispatcher = request.getRequestDispatcher("mainform.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

}
