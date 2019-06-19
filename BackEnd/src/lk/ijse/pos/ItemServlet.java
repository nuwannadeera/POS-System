package lk.ijse.pos;

import javax.annotation.Resource;
import javax.json.*;
import javax.json.stream.JsonParsingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource ds;

//                                   getAll

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {

            if (req.getParameter("itemcode") != null) {

                resp.setContentType("application/json");

                try {
                    Connection connection = ds.getConnection();

                    Statement stm = connection.createStatement();
                    ResultSet rst = stm.executeQuery("SELECT * FROM Item");

                    JsonArrayBuilder items = Json.createArrayBuilder();

                    while (rst.next()) {
                        String itemcode = rst.getString("itemcode");
                        String description = rst.getString("description");
                        int qtyonhand = rst.getInt("qtyonhand");
                        double unitprice = rst.getDouble("unitprice");

                        JsonObject item = Json.createObjectBuilder()
                                .add("itemcode", itemcode)
                                .add("description", description)
                                .add("qtyonhand", qtyonhand)
                                .add("unitprice", unitprice)
                                .build();
                        items.add(item);
                    }

                    out.println(items.build().toString());

                    connection.close();
                } catch (Exception ex) {
                    resp.sendError(500, ex.getMessage());
                    ex.printStackTrace();
                }

//                                     search item

            } else {
                try {
                    Connection connection = ds.getConnection();
//                    Class.forName("com.mysql.jdbc.Driver");
//                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ThogaKade", "root", "1997");
                    Statement stm = connection.createStatement();
                    ResultSet rst = stm.executeQuery("SELECT * FROM Item");

                    resp.setContentType("application/json");

                    JsonArrayBuilder ab = Json.createArrayBuilder();

                    while (rst.next()) {
                        JsonObjectBuilder ob = Json.createObjectBuilder();
                        ob.add("code", rst.getString("code"));
                        ob.add("description", rst.getString("description"));
                        ob.add("price", rst.getDouble(3));
                        ob.add("qty", rst.getInt(4));
                        ab.add(ob.build());
                    }
                    out.println(ab.build());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }

    }

//                                   save item

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        Connection connection = null;

        try {
            JsonObject item = reader.readObject();
            String itemcode = item.getString("itemcode");
            String description = item.getString("description");
            int qtyonhand = Integer.parseInt(item.getString("qtyonhand"));
            double unitprice = Double.parseDouble(item.getString("unitprice"));
            connection = ds.getConnection();

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Item VALUES (?,?,?,?)");
            pstm.setObject(1, itemcode);
            pstm.setObject(2, description);
            pstm.setObject(3, qtyonhand);
            pstm.setObject(4, unitprice);
            boolean result = pstm.executeUpdate()>0;

            if (result){
                out.println("true");
            }else{
                out.println("false");
            }

        }catch (Exception ex){
            ex.printStackTrace();
            out.println("false");
        }finally {
            try {
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
            out.close();
        }
    }



//                               update item


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("itemcode") != null) {

            try {
                JsonReader reader = Json.createReader(req.getReader());
                JsonObject item = reader.readObject();

                String itemcode = item.getString("itemcode");
                String description = item.getString("description");
                int qtyonhand = Integer.parseInt(item.getString("qtyonhand"));
                double unitprice = Double.parseDouble(item.getString("unitprice"));

                if (!itemcode.equals(req.getParameter("itemcode"))) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                Connection connection = ds.getConnection();
//                Class.forName("com.mysql.jdbc.Driver");
//                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ThogaKade", "root", "1997");
                PreparedStatement pstm = connection.prepareStatement("UPDATE Item SET description=?,qtyonhand=?, unitprice=? WHERE itemcode=?");
                pstm.setObject(1, itemcode);
                pstm.setObject(2, description);
                pstm.setObject(3, qtyonhand);
                pstm.setObject(4, unitprice);
                int affectedRows = pstm.executeUpdate();

                if (affectedRows > 0) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }

            } catch (JsonParsingException | NullPointerException ex) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } catch (Exception ex) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }



//                               delete item

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String code = req.getParameter("itemcode");

        if (code != null) {

            try {
                Connection connection = ds.getConnection();
//                Class.forName("com.mysql.jdbc.Driver");
//                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ThogaKade", "root", "1997");
                PreparedStatement pstm = connection.prepareStatement("DELETE FROM Item WHERE itemcode=?");
                pstm.setObject(1, code);
                int affectedRows = pstm.executeUpdate();
                if (affectedRows > 0) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (Exception ex) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                ex.printStackTrace();
            }

        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }



}
