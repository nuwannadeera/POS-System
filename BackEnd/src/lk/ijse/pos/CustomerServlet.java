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

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource ds;


//                                      customer search

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()) {

            if (req.getParameter("cid") != null) {
                resp.setContentType("application/json");

                String id = req.getParameter("cid");

                try {
                    Connection connection = ds.getConnection();

                    PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Customer WHERE cid=?");
                    pstm.setObject(1, id);
                    ResultSet rst = pstm.executeQuery();

                    if (rst.next()) {
                        JsonObjectBuilder ob = Json.createObjectBuilder();
                        ob.add("cid", rst.getString(1));
                        ob.add("name", rst.getString(2));
                        ob.add("address", rst.getString(3));
                        ob.add("contactno", rst.getDouble(4));

                        resp.setContentType("application/json");
                        out.println(ob.build());
                    } else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }


//                                      customer getall

            } else {
                try {
                    System.out.println("get All");
                    Connection connection = ds.getConnection();

                    Statement stm = connection.createStatement();
                    ResultSet rst = stm.executeQuery("SELECT * FROM Customer");

                    JsonArrayBuilder customers = Json.createArrayBuilder();

                    while (rst.next()) {
                        String cid = rst.getString("cid");
                        String name = rst.getString("name");
                        String address = rst.getString("address");
                        String contactno = rst.getString("contactno");

                        JsonObject customer = Json.createObjectBuilder()
                                .add("cid", cid)
                                .add("name", name)
                                .add("address", address)
                                .add("contactno", contactno)
                                .build();
                        customers.add(customer);
                    }

                    out.println(customers.build().toString());

                    connection.close();
                } catch (Exception ex) {
                    resp.sendError(500, ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }


    }


//                                      save customer

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        Connection connection = null;

        try {
            JsonObject customer = reader.readObject();
            String cid = customer.getString("cid");
            String name = customer.getString("name");
            String address = customer.getString("address");
            String contactno = customer.getString("contactno");

            connection = ds.getConnection();

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Customer VALUES (?,?,?,?)");
            pstm.setObject(1, cid);
            pstm.setObject(2, name);
            pstm.setObject(3, address);
            pstm.setObject(4, contactno);
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


//                                      update customer

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("cid") != null) {

            try {
                JsonReader reader = Json.createReader(req.getReader());
                JsonObject cus = reader.readObject();

                String cid = cus.getString("cid");
                String name = cus.getString("name");
                String address = cus.getString("address");
                String contactno = cus.getString("contactno");

                if (!cid.equals(req.getParameter("cid"))) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                Connection connection = ds.getConnection();
//                Class.forName("com.mysql.jdbc.Driver");
//                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ThogaKade", "root", "1997");
                PreparedStatement pstm = connection.prepareStatement("UPDATE Customer SET name=?,address=?, contactno=? WHERE cid=?");
                pstm.setObject(4, cid);
                pstm.setObject(1, name);
                pstm.setObject(2, address);
                pstm.setObject(3, contactno);
                int affectedRows = pstm.executeUpdate();

//                System.out.println(cid+","+name+","+address+","+contactno);

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


//                                      delete customer

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String cid = req.getParameter("cid");

        if (cid != null) {

            try {
                Connection connection = ds.getConnection();
//                Class.forName("com.mysql.jdbc.Driver");
//                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ThogaKade", "root", "1997");
                PreparedStatement pstm = connection.prepareStatement("DELETE FROM Customer WHERE cid=?");
                pstm.setObject(1, cid);
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
