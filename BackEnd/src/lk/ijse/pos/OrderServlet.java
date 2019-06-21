package lk.ijse.pos;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();



        Connection connection = null;
        try {

            connection .setAutoCommit(false);
            JsonObject orders = reader.readObject();


            String oid = orders.getString("oid");
            String date = orders.getString("date");
            String cid = orders.getString("cid");

            JsonObject itemdetail = reader.readObject();
            String odid = itemdetail.getString("odid");
            String itemcode = itemdetail.getString("itemcode");
            Integer qty =Integer.parseInt(itemdetail.getString("qty"));
            String unitprice = itemdetail.getString("unitprice");


            Statement stm = connection.createStatement();
            stm.executeUpdate(
                    "UPDATE Item SET qtyonhand =(qtyonhand - qty WHERE itemcode =?)"
            );

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Orders VALUES (?,?,?)");
            pstm.setObject(1,oid);
            pstm.setObject(2,date);
            pstm.setObject(3,cid);
            PreparedStatement psst = connection.prepareStatement("INSERT INTO ItemDetail VALUES (?,?,?,?)");
            psst.setObject(1,odid);
            psst.setObject(2,itemcode);
            psst.setObject(3,qty);
            psst.setObject(4,unitprice);

            boolean result = pstm.executeUpdate()>0;
            boolean resultnew = psst.executeUpdate()>0;



            if(result && resultnew){
                out.println("true");
            }else {
                out.println("false");
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
