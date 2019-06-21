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

//                                   item search

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {

            if (req.getParameter("itemcode") != null) {
                resp.setContentType("application/json");

                String code = req.getParameter("itemcode");


                try {
                    Connection connection = ds.getConnection();

                    PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Item WHERE itemcode=?");
                    pstm.setObject(1, code);
                    ResultSet rst = pstm.executeQuery();

                    if (rst.next()) {
                        JsonObjectBuilder ob = Json.createObjectBuilder();
                        ob.add("itemcode", rst.getString(1));
                        ob.add("description", rst.getString(2));
                        ob.add("unitprice", rst.getDouble(3));
                        ob.add("qtyonhand", rst.getInt(4));


                        resp.setContentType("application/json");
                        out.println(ob.build());
                    } else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }


//

//                                     getall item

            } else {

                try {
                    Connection connection = ds.getConnection();

                    Statement stm = connection.createStatement();
                    ResultSet rst = stm.executeQuery("SELECT * FROM Item");

                    JsonArrayBuilder items = Json.createArrayBuilder();

                    while (rst.next()) {
                        String itemcode = rst.getString("itemcode");
                        String description = rst.getString("description");
                        String unitprice = String.valueOf(rst.getDouble("unitprice"));
                        String qtyonhand = String.valueOf((rst.getInt("qtyOnHand")));

                        JsonObject item = Json.createObjectBuilder()
                                .add("itemcode", itemcode)
                                .add("description", description)
                                .add("unitprice", unitprice)
                                .add("qtyonhand", qtyonhand)
                                .build();
                        items.add(item);
                    }

                    out.println(items.build().toString());

                    connection.close();
                } catch (Exception ex) {
                    resp.sendError(500, ex.getMessage());
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
            double unitprice = Double.parseDouble(item.getString("unitprice"));
            int qtyonhand = Integer.parseInt(item.getString("qtyonhand"));

            connection = ds.getConnection();

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Item VALUES (?,?,?,?)");
            pstm.setObject(1, itemcode);
            pstm.setObject(2, description);
            pstm.setObject(3, unitprice);
            pstm.setObject(4, qtyonhand);

            boolean result = pstm.executeUpdate() > 0;

            if (result) {
                out.println("true");
            } else {
                out.println("false");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            out.println("false");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            out.close();
        }
    }


//                               update item


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getParameter("itemcode") != null){

            try {
                JsonReader reader = Json.createReader(req.getReader());
                JsonObject item = reader.readObject();

                String code=item.getString("itemcode");
                String desc=item.getString("description");
                double price=Double.parseDouble(item.getString("unitprice"));
                int qty=Integer.parseInt(item.getString("qtyonhand"));

                if (!code.equals(req.getParameter("itemcode"))){
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                Connection connection = ds.getConnection();
                java.sql.PreparedStatement pstm = connection.prepareStatement("UPDATE Item SET description=?, unitprice=?,qtyonhand=? WHERE itemcode=?");
                pstm.setObject(4,code);
                pstm.setObject(1,desc);
                pstm.setObject(2,price);
                pstm.setObject(3,qty);
                int affectedRows = pstm.executeUpdate();

                if (affectedRows > 0){
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }else{
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }

            }catch (JsonParsingException | NullPointerException  ex){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }catch (Exception ex){
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }


        }else{
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }


//        if (req.getParameter("itemcode") != null) {
//            System.out.println(req.getParameter("itemcode"));
//            try {
//                JsonReader reader = Json.createReader(req.getReader());
//                JsonObject item = reader.readObject();
//
//                System.out.println(item);
//
//                System.out.println(item.getString("itemcode"));
//
//                double d=Double.parseDouble(item.getString())
//
//
//                String itemcode = item.getString("itemcode");
//                String description = item.getString("description");
//
//                double unitprice = Double.parseDouble(item.getString("unitprice"));
//                int qtyonhand = item.getInt("qtyonhand");
//
//                System.out.println("itemcode : " + itemcode);
//                System.out.println("descirption : " + description);
//                System.out.println("unitprice : " + unitprice);
//                System.out.println("qtyonhand : " + qtyonhand);
//
//                if (!itemcode.equals(req.getParameter("itemcode"))) {
//                    System.out.println("for loop");
//                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
//                    return;
//                }
//
//                Connection connection = ds.getConnection();
//                PreparedStatement pstm = connection.prepareStatement("UPDATE Item SET description=?,unitprice=?, qtyonhand=? WHERE itemcode=?");
//                pstm.setObject(4, itemcode);
//                pstm.setObject(1, description);
//                pstm.setObject(2, unitprice);
//                pstm.setObject(3, qtyonhand);
//                int affectedRows = pstm.executeUpdate();
//                System.out.println(affectedRows);
//
//                if (affectedRows > 0) {
//                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
//                } else {
//                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                }
//
//            } catch (JsonParsingException | NullPointerException ex) {
//                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
//            } catch (Exception ex) {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            }
//
//        } else {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
//        }



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
