package lk.ijse.pos;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
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


//                                     getAll

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()) {

            resp.setContentType("application/json");

            try {
                Connection connection = ds.getConnection();

                Statement stm = connection.createStatement();
                ResultSet rst = stm.executeQuery("SELECT * FROM Customer");

                JsonArrayBuilder customers = Json.createArrayBuilder();

                while (rst.next()){
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

}
