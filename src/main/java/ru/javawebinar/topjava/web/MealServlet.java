package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.LoggerWrapper;
import ru.javawebinar.topjava.dao.DAO;
import ru.javawebinar.topjava.dao.UserMealRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Created by forza on 06.12.2015.
 */
public class MealServlet extends HttpServlet{
    private static final LoggerWrapper LOG = LoggerWrapper.get(MealServlet.class);
    private DAO dao= new UserMealRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("return mealList");
        LocalDateTime from = LocalDateTime.MIN;
        LocalDateTime till = LocalDateTime.MAX;

        String action = (req.getParameter("action") == null) ? "default" :  req.getParameter("action");

        switch (action){
            case "delete": //удаление еды
                try {
                    dao.delete(Integer.parseInt(req.getParameter("id")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                req.setAttribute("mealList", dao.display(from, till));
                req.getRequestDispatcher("/mealList.jsp").forward(req, resp);
                break;
            case "edit": // запрос на изменение
                req.setAttribute("userMeal", dao.getUserMeal(Integer.parseInt(req.getParameter("id"))));
                req.getRequestDispatcher("editMeal.jsp").forward(req, resp);
                break;
            default:    //дефолт
                req.setAttribute("mealList", dao.display(from, till));
                req.getRequestDispatcher("/mealList.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDateTime from = LocalDateTime.MIN;
        LocalDateTime till = LocalDateTime.MAX;

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        switch (action){
            case "filter": //фильтрация по заданным датам
                try {
                    from = LocalDateTime.parse(req.getParameter("date1"));
                } catch (NullPointerException | DateTimeParseException e){
                    from = LocalDateTime.MIN;
                }
                try {
                    till = LocalDateTime.parse(req.getParameter("date2"));
                } catch (NullPointerException | DateTimeParseException e){
                    till = LocalDateTime.MAX;
                }
                break;
            case "newMeal": //добавление еды
                try {
                    dao.create(LocalDateTime.parse(req.getParameter("date")), req.getParameter("descr"), Integer.parseInt(req.getParameter("calory")));
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case "edit": //изменение
                try {
                    dao.update(Integer.parseInt(req.getParameter("id")), LocalDateTime.parse(req.getParameter("date")), req.getParameter("descr"), Integer.parseInt(req.getParameter("calory")));
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }

        req.setAttribute("mealList", dao.display(from, till));
        req.getRequestDispatcher("/mealList.jsp").forward(req, resp);
    }
}
