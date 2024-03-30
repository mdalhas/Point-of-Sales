
package view;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import static com.pdf.InvoiceGenerator.getAccountsCell;
import static com.pdf.InvoiceGenerator.getIRDCell;
import static com.pdf.InvoiceGenerator.getValidityCell;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.JFileChooser;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author user
 */
public class ItFirm extends javax.swing.JFrame {

    DbCon con = new DbCon();
    String sql;
    PreparedStatement ps;
    ResultSet rs;

    /**
     * Creates new form ItFirm
     */
    public ItFirm() {
        this.sumActualPrice = 0.0f;

        initComponents();

        AutoCompleteDecorator.decorate(comboSalesProductName);
        AutoCompleteDecorator.decorate(comboPurchaseProductName);
        AutoCompleteDecorator.decorate(comboPurchaseProductCode);
        purchaseTbl();
        getComboProductName();
        getComboProductSalesName();
        getComboProductCode();
        productTbl();
        stockTbl();
        setAddToCart();
        tblCustomer();
        
//        System.out.println(" actual price :"+  homeMonthlySales());
         homeTodaysSales();
         
        jLabel42.setText(String.valueOf(homeTodaysSales()));
        jLabel46.setText(String.valueOf(homeTodaysDue()));
        jLabel44.setText(String.valueOf(homeTodaysPurchase()));
        
         jLabel52.setText(String.valueOf(  getLast30DaysTotalSales()));
           jLabel50.setText(String.valueOf( getLast30DaysTotalDue()));
            jLabel48.setText(String.valueOf(  getLast30DaysTotalPurchse()));

//        salesTbl();
    }

    public float getTotalPrice() {
        float unitPrice = Float.parseFloat(txtSalesUnitPrice.getText().trim());
        float quantity = Float.parseFloat(txtSalesQuantity.getText().trim());
        float totalPrice = unitPrice * quantity;
        return totalPrice;
    }

    public float getActualPrice() {
        float discount = Float.parseFloat(txtSalseDiscount.getText().trim());
        float actualPrice = getTotalPrice() - discount;
        return actualPrice;
    }

    public float getDueAmount() {
        float cashReceived = Float.parseFloat(txtSalesCashRecieved.getText().trim());
        float dueAmount = Float.parseFloat(txtSalesCartGrandTotal.getText().trim()) - cashReceived;
        return dueAmount;
    }

    public float getPurchaseTotalPrice() {
        float unitPrice = Float.parseFloat(txtPurchaseUnitPrice.getText().trim());
        float quantity = Float.parseFloat(txtPurchaseQuantity.getText().trim());
        float totalPrice = unitPrice * quantity;
        return totalPrice;
    }

    public java.sql.Date convertUtilDateToSqlDate(java.util.Date utilDate) {
        if (utilDate != null) {
            return new java.sql.Date(utilDate.getTime());
        }

        return null;
    }

    public static java.util.Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
           
            System.err.println("Date parsing failed " + e.getMessage());
            return null;
        }

    }

    String[] purchaseTblColumn = {"Purchase Id", "Product Name", "Product Code", "Unit Price", "Quantity", "Total Price", " Date"};

    public void purchaseTbl() {
        sql = "select * from purchase ";
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(purchaseTblColumn);
        tblPurchase.setModel(model);

        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String code = rs.getString("code");
                float unitPrice = rs.getFloat("unitPrice");
                float quantity = rs.getFloat("quantity");
                float totalPrice = rs.getFloat("totalPrice");
                Date date = rs.getDate("date");

                model.addRow(new Object[]{id, name, code, unitPrice, quantity, totalPrice, date});

            }
            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void resetSales() {
        txtSalesSID.setText("");
        comboSalesProductName.setSelectedItem(null);
        comboSalesProductName.setSelectedItem(null);
        txtSalesUnitPrice.setText("");
        txtSalesQuantity.setText("");
        txtSalesTotalPrice.setText("");
        txtSalseDiscount.setText("");
        txtSalesActualPrice.setText("");
        txtSalesCashRecieved.setText("");
        txtSalesDue.setText("");
        //datePurchase.setDate(null);
    }

    public void resetPurchase() {
        txtPurchaseId.setText("");
        comboPurchaseProductName.setSelectedItem(null);
        comboPurchaseProductCode.setSelectedItem(null);
        txtPurchaseUnitPrice.setText("");
        txtPurchaseQuantity.setText("");
        txtPurchaseTotalPrice.setText("");
        datePurchase.setDate(null);
    }

    public void getComboProductName() {
        sql = "select name from product";
        comboPurchaseProductName.removeAllItems();
        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String ProductNames = rs.getString("name");
                comboPurchaseProductName.addItem(ProductNames);

            }
            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Combo Purchase product Code
    public void getComboProductCode() {
        sql = "select code from product";
        comboPurchaseProductCode.removeAllItems();
        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String ProductCodes = rs.getString("code");
                comboPurchaseProductCode.addItem(ProductCodes);

            }
            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getComboProductSalesName() {
        sql = "select name from product";
        comboSalesProductName.removeAllItems();
        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String ProductNames = rs.getString("name");
                comboSalesProductName.addItem(ProductNames);

            }
            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //tbl Product
    String[] productTblColumn = {" Product Id", "Product Name", "Product Code", "Unit Price"};

    public void productTbl() {
        sql = "select * from  product ";
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(productTblColumn);
        tblProduct.setModel(model);

        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String code = rs.getString("code");
                float unitPrice = rs.getFloat("unitPrice");

                model.addRow(new Object[]{id, name, code, unitPrice});

            }
            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //reset Product
    public void resetProduct() {
        txtProductId.setText("");
        txtProductName.setText("");
        txtProductCode.setText("");
        txtProductUnitPrice.setText("");

    }

    public void getStockProduct() {
        sql = "insert into stock ( name, code, unitPrice, quantity) value (?,?,?,?)";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, txtProductName.getText().trim());
            ps.setString(2, txtProductCode.getText().trim());
            ps.setFloat(3, Float.parseFloat(txtProductUnitPrice.getText().trim()));
            ps.setFloat(4, 0);

            ps.executeUpdate();
            ps.close();
            con.getCon().close();
            // JOptionPane.showMessageDialog(rootPane, "New Product Is Added");
            //  productTbl();

            //   resetProduct();
            getComboProductName();
            getComboProductSalesName();
            getComboProductCode();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "New Product Is Not Added At Stock");
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //  getQunaatity in stock
//   public static float parseFloat(String s) throws NumberFormatException {
//        return FloatingDecimal.parseFloat(s);
//    }
    // Stock 
    public void setQunantityPurchase() {
        sql = "update  stock  set quantity=quantity+? where name=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setFloat(1, Float.parseFloat(txtPurchaseQuantity.getText().trim()));
            ps.setString(2, comboPurchaseProductName.getSelectedItem().toString());

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Quantity does not Updated");
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    //
    
    public void setQunantitySales(String productName, float quantity) {
        sql = "update  stock  set quantity=quantity-? where name=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setFloat(1, quantity );
            ps.setString(2, productName);

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Quantity does not Updated");
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // sales get table
    public void salesTbl() {
        String[] purchaseTblColumn = {"Id", "Product Name", "Unit Price", "Quantity", "Total Price", "Discount", "Actual Price", "Cash Recieve", "Due"};

        sql = "select * from sales ";
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(purchaseTblColumn);
        tblSales.setModel(model);

        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("sid");
                String name = rs.getString("productName");

                float unitPrice = rs.getFloat("unitPrice");
                float quantity = rs.getFloat("quantity");
                float totalPrice = rs.getFloat("totalPrice");
                float discount = rs.getFloat("discount");
                float actualPrice = rs.getFloat("actualPrice");
                float cashreceived = rs.getFloat("cashreceived");
                float dueAmount = rs.getFloat("dueAmount");

                model.addRow(new Object[]{id, name, unitPrice, quantity, totalPrice, discount, actualPrice, cashreceived, dueAmount});

            }
            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // stock Table
    public void stockTbl() {
        String[] purchaseTblColumn = {"Stock Id", "Product Name", "Unit Price", "Quantity"};

        sql = "select * from stock ";
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(purchaseTblColumn);
        tblStock.setModel(model);
        tblSales.setModel(model);

        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                //  String code = rs.getString("code");
                float unitPrice = rs.getFloat("unitPrice");
                float quantity = rs.getFloat("quantity");
//                    float totalPrice= rs.getFloat("totalPrice");
//                    Date date = rs.getDate("date");

                model.addRow(new Object[]{id, name, unitPrice, quantity});

            }
            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setAddToCart() {
        String[] column = {  "productName", "unitPrice", "quantity", "totalPrice", "discount", "actualPrice"};
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.setColumnIdentifiers(column);
        tblCart.setModel(dtm);

    }
    
    
    
    // Customer table
    
 /*   public void tblCustomerDataInsert(){
        sql = "INSERT INTO customer (name, mobile, address, dueAmount, salesDate, receiveDue, actualDue) VALUES ( ?, ?,?, ?, ?,?, ?) ON DUPLICATE KEY UPDATE dueAmount = dueAmount + VALUES(?)";
        try {
            ps =con.getCon().prepareStatement(sql);
            ps.setString(1, txtCustomerName.getText().trim());
            ps.setString(2, txtCustomerMobile.getText().trim());
            ps.setString(3, txtCustomerAddress.getText().trim());
            
            ps.setString(4, txtCustomerDueAmount.getText().trim());
            ps.setString(5, txtcustomerSalesDate.getText().trim());
            ps.setString(1, txtCustomerR.getText().trim());
            
        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }/*
    
   
    
    
    
    */
    
   
   public void inserDatatoCustomer(){
         
          float due = Float.parseFloat(txtSalesDue.getText().trim());
            String cusN = txtSalesCaustomerName.getText().trim();
            String saller = txtSalesSaller.getText().trim();
            String mobile = txtSalesCaustomerMobile.getText().trim();
            String address = txtSalesAddress.getText().trim();
            Date saledate = convertUtilDateToSqlDate(dateSales.getDate());

            sql = "INSERT INTO customer ( name, mobile, address, salesDate , dueAmount,  seller) VALUES (  ?, ?,?, ?, ?,?) ON DUPLICATE KEY UPDATE dueAmount = ? + VALUES(dueAmount)";
            try {
                ps = con.getCon().prepareStatement(sql);          
                ps.setString(1, cusN);
                ps.setString(2, mobile);
                ps.setString(3, address);
                ps.setDate(4, saledate);
                ps.setFloat(5, due);
                ps.setString(6, saller);
                  ps.setFloat(7, due);
                ps.executeUpdate();
                ps.close();
                con.getCon().close();
 
             //   JOptionPane.showMessageDialog(rootPane, "Order complete.");
            } catch (SQLException ex) {
                Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
    

   //
   
     public void tblCustomer() {
        String[] purchaseTblColumn = {"Customer Id", "Customer Name", " Mobile No","Address", "Sales Date", "Due Amount"};

        sql = "select * from customer ";
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(purchaseTblColumn);
        tblCustomer.setModel(model);
       

        try {
            ps = con.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                //  String code = rs.getString("code");
                String mobile =rs.getString("mobile");
                String Address =rs.getString("address");
                Date date = rs.getDate("salesDate");
                float dueAmount= rs.getFloat("dueAmount");
                 

                model.addRow(new Object[]{id, name, mobile,Address, date,dueAmount});

            }
            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
     
     //sid, productName, unitPrice, quantity, totalPrice, discount, actualPrice, groundTotal, cashReceived, dueAmount, castomerName, saller, mobile, Date
     // tblReport
     public void showSalesReport(java.util.Date fromDate, java.util.Date toDate){
         String colunSalesReport []= {"SL","Product Name","Unit Price","Quantity","Discount","Actual Price","Due Amount","Castomer Name" };
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(colunSalesReport);
        tblReport.setModel(model);
         
       //  sql = "select * from sales where date between ( ? and ?)";
         sql = "SELECT * FROM sales WHERE date BETWEEN ? AND ?";
         
        try {
            ps= con.getCon().prepareStatement(sql);
            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));
            
            
            int sl=0;
            rs = ps.executeQuery();
            while (rs.next()){
                 sl+=1 ;
                String productname= rs.getString("productname");
                float unitPrice= rs.getFloat("unitPrice");
                float quantity= rs.getFloat("quantity");
                float discount= rs.getFloat("totalPrice");
                float actualPrice= rs.getFloat("actualPrice");
                float dueAmount= rs.getFloat("dueAmount");
                String castomerName= rs.getString("castomerName");
                
                model.addRow(new Object[]{sl,productname,unitPrice, quantity, discount,actualPrice,castomerName });
                
            }
            
            ps.close();
            rs.close();
            con.getCon().close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }
     
     }
     
     
     
     //     // tblDueReport
     public void showDueReport(java.util.Date fromDate, java.util.Date toDate){
         String colunSalesReport []= {"SL","Name","Mobile Number","Address","Due Amount","Seller Name" };
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(colunSalesReport);
        tblReport.setModel(model);
         
       //  sql = "select * from sales where date between ( ? and ?)";
         sql = "SELECT * FROM customer WHERE salesdate BETWEEN ? AND ?";
         
        try {
            ps= con.getCon().prepareStatement(sql);
            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));
            
            
            int sl=0;
            rs = ps.executeQuery();
            while (rs.next()){
                 sl+=1 ;
                String productname= rs.getString("name");
                String mobile= rs.getString("mobile");
                String address= rs.getString("Address");
                float dueAmount= rs.getFloat("dueAmount");
                String seller= rs.getString("seller");
                
                model.addRow(new Object[]{sl,productname,mobile,address,dueAmount, seller });
                
            }
            
            ps.close();
            rs.close();
            con.getCon().close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     //  Report Purchase
     
          public void showPurchaseReport(java.util.Date fromDate, java.util.Date toDate){
         String colunSalesReport []= {"SL","Product Name","Unit Price","Quantity","Total Price" };
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(colunSalesReport);
        tblReport.setModel(model);
         
                sql = "SELECT * FROM purchase WHERE date BETWEEN ? AND ?";
         
        try {
            ps= con.getCon().prepareStatement(sql);
            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));
              int sl=0;
            rs = ps.executeQuery();
            while (rs.next()){
                 sl+=1 ;
                String productname= rs.getString("name");
                float unitPrice= rs.getFloat("unitPrice");
                float quantity= rs.getFloat("quantity");
                float actualPrice= rs.getFloat("totalprice");
                              model.addRow(new Object[]{sl,productname,unitPrice, quantity, actualPrice });
                      }
            
            ps.close();
            rs.close();
            con.getCon().close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }
     
     }
          
          //   Todays Sales
 public float homeTodaysSales(){

long currentTimeMillis = System.currentTimeMillis();
Timestamp timestamp = new Timestamp(currentTimeMillis);
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Example format
String formattedDate = dateFormat.format(timestamp);


String sql = "SELECT actualprice FROM sales WHERE date = ?";
 float actualPrice=0;
try {
    ps = con.getCon().prepareStatement(sql);
    ps.setDate(1, Date.valueOf(formattedDate));
    rs = ps.executeQuery();
    
    while (rs.next()){
      actualPrice += rs.getFloat("actualprice");
    
    }
    
   
} catch (SQLException e) {
    e.printStackTrace(); // Handle SQLException appropriately
} finally {
    // Close resources (ResultSet, PreparedStatement, Connection) in a finally block
    // This ensures that resources are closed even if an exception occurs
}
return  actualPrice;

          } 
 
 
           //   Todays Sales
 public float homeTodaysDue(){
// Format the current system date as a string
long currentTimeMillis = System.currentTimeMillis();
Timestamp timestamp = new Timestamp(currentTimeMillis);
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Example format
String formattedDate = dateFormat.format(timestamp);

            //  System.out.println("Date "+formattedDate);
            
// Prepare the SQL query and set the date parameter
String sql = "SELECT dueAmount FROM customer WHERE salesdate = ?";
 float dueAmount=0;
try {
    ps = con.getCon().prepareStatement(sql);
    ps.setDate(1, Date.valueOf(formattedDate));
    rs = ps.executeQuery();
    
    while (rs.next()){
      dueAmount += rs.getFloat("dueAmount");
    
    }
    
    // Process the ResultSet as needed
} catch (SQLException e) {
    e.printStackTrace(); // Handle SQLException appropriately
} finally {
    // Close resources (ResultSet, PreparedStatement, Connection) in a finally block
    // This ensures that resources are closed even if an exception occurs
}
return  dueAmount;

          } 
 
 
 //
 
 
 
  public float homeTodaysPurchase(){
// Format the current system date as a string
long currentTimeMillis = System.currentTimeMillis();
Timestamp timestamp = new Timestamp(currentTimeMillis);
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Example format
String formattedDate = dateFormat.format(timestamp);

            //  System.out.println("Date "+formattedDate);
            
// Prepare the SQL query and set the date parameter
String sql = "SELECT totalprice FROM purchase WHERE date = ?";
 float totalprice=0;
try {
    ps = con.getCon().prepareStatement(sql);
    ps.setDate(1, Date.valueOf(formattedDate));
    rs = ps.executeQuery();
    
    while (rs.next()){
      totalprice += rs.getFloat("totalprice");
    
    }
    
    // Process the ResultSet as needed
} catch (SQLException e) {
    e.printStackTrace(); // Handle SQLException appropriately
} finally {
    // Close resources (ResultSet, PreparedStatement, Connection) in a finally block
    // This ensures that resources are closed even if an exception occurs
}
return  totalprice;

          } 
   
  
  // monthly  sales
  
   
   public float getLast30DaysTotalSales() {
    // Get the current timestamp
    long currentTimeMillis = System.currentTimeMillis();
    Timestamp timestamp = new Timestamp(currentTimeMillis);
    
    // Calculate the date 30 days ago from the current date
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(currentTimeMillis);
    calendar.add(Calendar.DAY_OF_MONTH, -30);
    Date thirtyDaysAgo = new Date(calendar.getTimeInMillis());
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Example format
    String formattedDate = dateFormat.format(timestamp);

    String sql = "SELECT actualprice FROM sales WHERE date BETWEEN ? AND ?";
    float totalSales = 0;

    try {
        ps = con.getCon().prepareStatement(sql);
        ps.setDate(1, thirtyDaysAgo);
        ps.setDate(2, Date.valueOf(formattedDate));
        rs = ps.executeQuery();

        while (rs.next()) {
            totalSales += rs.getFloat("actualprice");
        }

    } catch (SQLException e) {
        e.printStackTrace(); // Handle SQLException appropriately
    } finally {
        // Close resources (ResultSet, PreparedStatement, Connection) in a finally block
        // This ensures that resources are closed even if an exception occurs
    }
    return totalSales;
}

   
// monthly due
   
    public float getLast30DaysTotalDue() {
    // Get the current timestamp
    long currentTimeMillis = System.currentTimeMillis();
    Timestamp timestamp = new Timestamp(currentTimeMillis);
    
    // Calculate the date 30 days ago from the current date
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(currentTimeMillis);
    calendar.add(Calendar.DAY_OF_MONTH, -30);
    Date thirtyDaysAgo = new Date(calendar.getTimeInMillis());
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Example format
    String formattedDate = dateFormat.format(timestamp);

    String sql = "SELECT dueamount FROM customer  WHERE salesdate BETWEEN ? AND ?";
    float dueamount = 0;

    try {
        ps = con.getCon().prepareStatement(sql);
        ps.setDate(1, thirtyDaysAgo);
        ps.setDate(2, Date.valueOf(formattedDate));
        rs = ps.executeQuery();

        while (rs.next()) {
            dueamount += rs.getFloat("dueamount");
        }

    } catch (SQLException e) {
        e.printStackTrace(); // Handle SQLException appropriately
    } finally {
        // Close resources (ResultSet, PreparedStatement, Connection) in a finally block
        // This ensures that resources are closed even if an exception occurs
    }
    return dueamount;
}


    
    //monthly purchse
    
    public float getLast30DaysTotalPurchse() {
    // Get the current timestamp
    long currentTimeMillis = System.currentTimeMillis();
    Timestamp timestamp = new Timestamp(currentTimeMillis);
    
    // Calculate the date 30 days ago from the current date
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(currentTimeMillis);
    calendar.add(Calendar.DAY_OF_MONTH, -30);
    Date thirtyDaysAgo = new Date(calendar.getTimeInMillis());
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Example format
    String formattedDate = dateFormat.format(timestamp);

    String sql = "SELECT totalPrice FROM purchase  WHERE date BETWEEN ? AND ?";
    float totalPrice = 0;

    try {
        ps = con.getCon().prepareStatement(sql);
        ps.setDate(1, thirtyDaysAgo);
        ps.setDate(2, Date.valueOf(formattedDate));
        rs = ps.executeQuery();

        while (rs.next()) {
            totalPrice += rs.getFloat("totalPrice");
        }

    } catch (SQLException e) {
        e.printStackTrace(); // Handle SQLException appropriately
    } finally {
        // Close resources (ResultSet, PreparedStatement, Connection) in a finally block
        // This ensures that resources are closed even if an exception occurs
    }
    return totalPrice;
}


   
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnReport = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        btnSalse = new javax.swing.JButton();
        btnPurchase = new javax.swing.JButton();
        btnProduct = new javax.swing.JButton();
        btnCustomer = new javax.swing.JButton();
        btnStock = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        menu = new javax.swing.JTabbedPane();
        home = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        sales = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        txtSalesPruductName = new javax.swing.JLabel();
        lblSalesUnitPrice = new javax.swing.JLabel();
        lblSalesQuality = new javax.swing.JLabel();
        lblRotalPrice = new javax.swing.JLabel();
        txtSalesUnitPrice5 = new javax.swing.JLabel();
        txtSalesActualPrict = new javax.swing.JLabel();
        txtSalesPruductName2 = new javax.swing.JLabel();
        txtSalesSID = new javax.swing.JTextField();
        txtSalesUnitPrice = new javax.swing.JTextField();
        txtSalesQuantity = new javax.swing.JTextField();
        txtSalesTotalPrice = new javax.swing.JTextField();
        txtSalseDiscount = new javax.swing.JTextField();
        txtSalesActualPrice = new javax.swing.JTextField();
        txtSalesCashRecieved = new javax.swing.JTextField();
        txtSalesDue = new javax.swing.JTextField();
        txtSalesCaustomerName = new javax.swing.JTextField();
        btnSalesSubmit = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        comboSalesProductName = new javax.swing.JComboBox<>();
        txtSalesUnitPrice3 = new javax.swing.JLabel();
        txtSalesPruductName1 = new javax.swing.JLabel();
        txtSalesUnitPrice1 = new javax.swing.JLabel();
        txtSalesUnitPrice6 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblSales = new javax.swing.JTable();
        btnAddToCard = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        txtSalesSaller = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblCart = new javax.swing.JTable();
        txtSalesCaustomerMobile = new javax.swing.JTextField();
        dateSales = new com.toedter.calendar.JDateChooser();
        txtSalesActualPrict4 = new javax.swing.JLabel();
        txtSalesCartGrandTotal = new javax.swing.JTextField();
        txtSalesActualPrict2 = new javax.swing.JLabel();
        txtSalesActualPrict3 = new javax.swing.JLabel();
        txtSalesActualPrict1 = new javax.swing.JLabel();
        txtSalesAddress = new javax.swing.JTextField();
        purchase = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        txtPurchaseId = new javax.swing.JTextField();
        txtPurchaseUnitPrice = new javax.swing.JTextField();
        txtPurchaseQuantity = new javax.swing.JTextField();
        txtPurchaseTotalPrice = new javax.swing.JTextField();
        comboPurchaseProductName = new javax.swing.JComboBox<>();
        comboPurchaseProductCode = new javax.swing.JComboBox<>();
        datePurchase = new com.toedter.calendar.JDateChooser();
        btnPurchaseSubmit = new javax.swing.JButton();
        btnPurchaseUpdate = new javax.swing.JButton();
        btnPurchaseReset = new javax.swing.JButton();
        btnPurchaseDelete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPurchase = new javax.swing.JTable();
        product = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        txtProductId = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtProductName = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtProductCode = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtProductUnitPrice = new javax.swing.JTextField();
        btnProductSave = new javax.swing.JButton();
        btnProductUpdate = new javax.swing.JButton();
        btnProductReset = new javax.swing.JButton();
        btnProductDelete = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblProduct = new javax.swing.JTable();
        report = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        radioReportPurchase = new javax.swing.JRadioButton();
        radioReportDue = new javax.swing.JRadioButton();
        radioReportSales = new javax.swing.JRadioButton();
        btnReportView = new javax.swing.JToggleButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblReport = new javax.swing.JTable();
        toDate = new com.toedter.calendar.JDateChooser();
        fromDate = new com.toedter.calendar.JDateChooser();
        Customer = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        txtCustomerId = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtCustomerMobile = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtCustomerAddress = new javax.swing.JTextArea();
        jLabel32 = new javax.swing.JLabel();
        txtCustomerDueAmount = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblCustomer = new javax.swing.JTable();
        btnCustomerReset = new javax.swing.JButton();
        btnCustomerUpdate = new javax.swing.JButton();
        txtcustomerSalesDate = new com.toedter.calendar.JDateChooser();
        jLabel34 = new javax.swing.JLabel();
        txtCustomerReceiveDue = new javax.swing.JTextField();
        stock = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblStock = new javax.swing.JTable();
        jLabel40 = new javax.swing.JLabel();

        jPanel19.setBackground(new java.awt.Color(204, 255, 255));

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Today Purchase");

        jTextField5.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                    .addComponent(jTextField5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Today Purchase");

        jTextField6.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                    .addComponent(jTextField6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Today Purchase");

        jTextField7.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                    .addComponent(jTextField7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel35.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Modern Super Shop");
        jPanel1.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 300, 40));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/HeaderFurniture.jpg"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 80));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 80));

        jPanel2.setBackground(new java.awt.Color(0, 204, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnReport.setBackground(new java.awt.Color(255, 102, 0));
        btnReport.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        btnReport.setText("Report");
        btnReport.setAutoscrolls(true);
        btnReport.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportMouseClicked(evt);
            }
        });
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportActionPerformed(evt);
            }
        });
        jPanel2.add(btnReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 140, 40));

        btnHome.setBackground(new java.awt.Color(204, 102, 0));
        btnHome.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        btnHome.setText("Home");
        btnHome.setAutoscrolls(true);
        btnHome.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeMouseClicked(evt);
            }
        });
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });
        jPanel2.add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 140, 40));

        btnSalse.setBackground(new java.awt.Color(255, 102, 0));
        btnSalse.setFont(new java.awt.Font("Tempus Sans ITC", 1, 17)); // NOI18N
        btnSalse.setText("Sales");
        btnSalse.setAutoscrolls(true);
        btnSalse.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSalse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalseMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalseMouseExited(evt);
            }
        });
        btnSalse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalseActionPerformed(evt);
            }
        });
        jPanel2.add(btnSalse, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 140, 40));

        btnPurchase.setBackground(new java.awt.Color(255, 102, 0));
        btnPurchase.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        btnPurchase.setText("Purchase");
        btnPurchase.setAutoscrolls(true);
        btnPurchase.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPurchase.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPurchase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPurchaseMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPurchaseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPurchaseMouseExited(evt);
            }
        });
        btnPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseActionPerformed(evt);
            }
        });
        jPanel2.add(btnPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 140, 40));

        btnProduct.setBackground(new java.awt.Color(255, 102, 0));
        btnProduct.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        btnProduct.setText("Product");
        btnProduct.setAutoscrolls(true);
        btnProduct.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductMouseClicked(evt);
            }
        });
        btnProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductActionPerformed(evt);
            }
        });
        jPanel2.add(btnProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 140, 40));

        btnCustomer.setBackground(new java.awt.Color(255, 102, 0));
        btnCustomer.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        btnCustomer.setText("Caustomer");
        btnCustomer.setAutoscrolls(true);
        btnCustomer.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCustomerMouseClicked(evt);
            }
        });
        btnCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerActionPerformed(evt);
            }
        });
        jPanel2.add(btnCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 140, 40));

        btnStock.setBackground(new java.awt.Color(255, 102, 0));
        btnStock.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        btnStock.setText("Stock");
        btnStock.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnStock.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnStockMouseClicked(evt);
            }
        });
        btnStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockActionPerformed(evt);
            }
        });
        jPanel2.add(btnStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 140, 40));

        jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/leftBorMen.jpg"))); // NOI18N
        jPanel2.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 140, 600));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 140, 600));

        jPanel3.setBackground(new java.awt.Color(204, 229, 229));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menu.setBackground(new java.awt.Color(255, 255, 204));

        home.setBackground(new java.awt.Color(255, 204, 102));

        jPanel9.setBackground(new java.awt.Color(102, 51, 0));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 204));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Home");
        jPanel9.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1080, 50));

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel51.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setText("Monthly Sales");
        jPanel23.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 200, 30));

        jLabel52.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setText("000000");
        jPanel23.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 80, 130, 30));

        jPanel24.setBackground(new java.awt.Color(153, 153, 0));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel47.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel47.setText("Monthly Purchase");
        jPanel24.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 210, -1));

        jLabel48.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText("000000");
        jPanel24.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 170, 30));

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel49.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("Monthly Due");
        jPanel25.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 180, -1));

        jLabel50.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel50.setText("000000");
        jPanel25.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 160, 30));

        jPanel16.setBackground(new java.awt.Color(153, 193, 0));
        jPanel16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel41.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("Today's Sales ");
        jPanel16.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 250, 40));

        jLabel42.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("00000000");
        jPanel16.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 160, 40));

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel43.setText("Today's Purchase");
        jPanel17.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 33, 200, 40));

        jLabel44.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("00000000");
        jPanel17.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 170, 30));

        jPanel18.setBackground(new java.awt.Color(153, 153, 0));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel45.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("Today's Due");
        jPanel18.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 210, -1));

        jLabel46.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("000000");
        jPanel18.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 200, 30));

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(homeLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(51, 51, 51)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(58, 58, 58)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        homeLayout.setVerticalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(60, 60, 60))
        );

        menu.addTab("tab1", home);

        jPanel10.setBackground(new java.awt.Color(102, 51, 0));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Sales");
        jPanel10.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1080, 40));

        jPanel4.setBackground(new java.awt.Color(255, 204, 102));

        jPanel5.setBackground(new java.awt.Color(255, 204, 102));

        txtSalesPruductName.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        txtSalesPruductName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtSalesPruductName.setText("Product Name");
        txtSalesPruductName.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblSalesUnitPrice.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        lblSalesUnitPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSalesUnitPrice.setText("Unit Price");
        lblSalesUnitPrice.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblSalesQuality.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        lblSalesQuality.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSalesQuality.setText("Quantity");
        lblSalesQuality.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblRotalPrice.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        lblRotalPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRotalPrice.setText("Total Price");
        lblRotalPrice.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtSalesUnitPrice5.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        txtSalesUnitPrice5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtSalesUnitPrice5.setText("Discount");
        txtSalesUnitPrice5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtSalesActualPrict.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        txtSalesActualPrict.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtSalesActualPrict.setText("Actual Price");
        txtSalesActualPrict.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtSalesPruductName2.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        txtSalesPruductName2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtSalesPruductName2.setText("SID");
        txtSalesPruductName2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtSalesPruductName, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
            .addComponent(txtSalesPruductName2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblSalesUnitPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblSalesQuality, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblRotalPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtSalesUnitPrice5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtSalesActualPrict, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSalesPruductName2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSalesPruductName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSalesUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblSalesQuality, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblRotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(txtSalesUnitPrice5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtSalesActualPrict, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtSalesSID.setEditable(false);
        txtSalesSID.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtSalesSID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSalesSIDMouseClicked(evt);
            }
        });

        txtSalesUnitPrice.setBackground(new java.awt.Color(240, 240, 240));
        txtSalesUnitPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesUnitPriceFocusLost(evt);
            }
        });

        txtSalesQuantity.setBackground(new java.awt.Color(240, 240, 240));
        txtSalesQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesQuantityFocusLost(evt);
            }
        });

        txtSalesTotalPrice.setBackground(new java.awt.Color(240, 240, 240));

        txtSalseDiscount.setBackground(new java.awt.Color(240, 240, 240));
        txtSalseDiscount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalseDiscountFocusLost(evt);
            }
        });

        txtSalesActualPrice.setBackground(new java.awt.Color(240, 240, 240));

        txtSalesCashRecieved.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtSalesCashRecieved.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesCashRecievedFocusLost(evt);
            }
        });

        txtSalesDue.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtSalesCaustomerName.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnSalesSubmit.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        btnSalesSubmit.setText("Sale & Print");
        btnSalesSubmit.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSalesSubmit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalesSubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalesSubmitMouseClicked(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jButton2.setText("Reset");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        comboSalesProductName.setBackground(new java.awt.Color(240, 240, 240));
        comboSalesProductName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboSalesProductName.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtSalesUnitPrice3.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        txtSalesUnitPrice3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtSalesUnitPrice3.setText("Availabe Products For Sale");
        txtSalesUnitPrice3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtSalesPruductName1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        txtSalesPruductName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtSalesPruductName1.setText("Caustomer Name");
        txtSalesPruductName1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtSalesUnitPrice1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        txtSalesUnitPrice1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtSalesUnitPrice1.setText("Date");
        txtSalesUnitPrice1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtSalesUnitPrice6.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        txtSalesUnitPrice6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtSalesUnitPrice6.setText("Mobile");
        txtSalesUnitPrice6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblSales.setBackground(new java.awt.Color(255, 255, 204));
        tblSales.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblSales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSalesMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblSales);

        btnAddToCard.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        btnAddToCard.setText("Add To Card");
        btnAddToCard.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAddToCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddToCardMouseClicked(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 204, 102));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel36.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel36.setText("Saller Name");
        jLabel36.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtSalesSaller.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtSalesSaller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSalesSallerActionPerformed(evt);
            }
        });

        tblCart.setBackground(new java.awt.Color(255, 255, 204));
        tblCart.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblCart.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblCart.setSelectionBackground(new java.awt.Color(255, 204, 102));
        tblCart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCartMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblCart);

        txtSalesCaustomerMobile.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtSalesCaustomerMobile.setSelectionColor(new java.awt.Color(255, 204, 102));

        dateSales.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtSalesActualPrict4.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        txtSalesActualPrict4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtSalesActualPrict4.setText("Grand Total");
        txtSalesActualPrict4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtSalesCartGrandTotal.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtSalesCartGrandTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSalesCartGrandTotalActionPerformed(evt);
            }
        });

        txtSalesActualPrict2.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        txtSalesActualPrict2.setText("Cash Rechived");
        txtSalesActualPrict2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtSalesActualPrict3.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        txtSalesActualPrict3.setText("Due Amount");
        txtSalesActualPrict3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtSalesActualPrict1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        txtSalesActualPrict1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtSalesActualPrict1.setText("Address");
        txtSalesActualPrict1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtSalesAddress.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSalesSaller, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtSalesDue, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtSalseDiscount, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSalesTotalPrice, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSalesQuantity, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSalesUnitPrice, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboSalesProductName, javax.swing.GroupLayout.Alignment.LEADING, 0, 224, Short.MAX_VALUE)
                            .addComponent(txtSalesSID, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSalesActualPrice)
                            .addComponent(btnAddToCard, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtSalesUnitPrice3, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtSalesUnitPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(dateSales, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                            .addComponent(txtSalesPruductName1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtSalesCaustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtSalesUnitPrice6, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtSalesCaustomerMobile, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(26, 26, 26)))
                                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(txtSalesActualPrict1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtSalesAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtSalesActualPrict4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(btnSalesSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(txtSalesActualPrict3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txtSalesActualPrict2, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtSalesCashRecieved, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtSalesCartGrandTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                .addGap(53, 53, 53))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 450, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddToCard, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(dateSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtSalesUnitPrice3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSalesUnitPrice1))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSalesPruductName1)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtSalesCaustomerMobile, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtSalesUnitPrice6))
                            .addComponent(txtSalesCaustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtSalesActualPrict4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSalesActualPrict1)
                                    .addComponent(txtSalesAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSalesCartGrandTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSalesCashRecieved, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSalesActualPrict2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSalesSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSalesSaller, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSalesActualPrict3)
                            .addComponent(txtSalesDue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(txtSalesSID, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboSalesProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtSalesUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(txtSalesQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(txtSalesTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(txtSalseDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(txtSalesActualPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout salesLayout = new javax.swing.GroupLayout(sales);
        sales.setLayout(salesLayout);
        salesLayout.setHorizontalGroup(
            salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        salesLayout.setVerticalGroup(
            salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesLayout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menu.addTab("tab2", sales);

        jPanel11.setBackground(new java.awt.Color(102, 51, 0));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Purchase");
        jPanel11.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1080, 50));

        jPanel12.setBackground(new java.awt.Color(255, 204, 102));

        jPanel13.setBackground(new java.awt.Color(255, 204, 102));

        jLabel5.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Product Name");
        jLabel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Purchase ID");
        jLabel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Unit Price");
        jLabel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Total Price");
        jLabel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Quantity");
        jLabel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel10.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Product Code");
        jLabel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel11.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Date");
        jLabel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel14.setBackground(new java.awt.Color(204, 204, 204));

        txtPurchaseId.setEditable(false);
        txtPurchaseId.setBackground(new java.awt.Color(219, 219, 223));
        txtPurchaseId.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtPurchaseUnitPrice.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtPurchaseUnitPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseUnitPriceFocusLost(evt);
            }
        });

        txtPurchaseQuantity.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtPurchaseQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseQuantityFocusLost(evt);
            }
        });

        txtPurchaseTotalPrice.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        comboPurchaseProductName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboPurchaseProductName.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        comboPurchaseProductCode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboPurchaseProductCode.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        datePurchase.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtPurchaseId)
            .addComponent(comboPurchaseProductName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(comboPurchaseProductCode, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtPurchaseTotalPrice)
            .addComponent(datePurchase, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
            .addComponent(txtPurchaseQuantity)
            .addComponent(txtPurchaseUnitPrice)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(txtPurchaseId, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(comboPurchaseProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(comboPurchaseProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtPurchaseUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtPurchaseQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtPurchaseTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(datePurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnPurchaseSubmit.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        btnPurchaseSubmit.setText("Submit");
        btnPurchaseSubmit.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPurchaseSubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPurchaseSubmitMouseClicked(evt);
            }
        });
        btnPurchaseSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseSubmitActionPerformed(evt);
            }
        });

        btnPurchaseUpdate.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        btnPurchaseUpdate.setText("Update");
        btnPurchaseUpdate.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPurchaseUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPurchaseUpdateMouseClicked(evt);
            }
        });

        btnPurchaseReset.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        btnPurchaseReset.setText("Reset");
        btnPurchaseReset.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPurchaseReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPurchaseResetMouseClicked(evt);
            }
        });

        btnPurchaseDelete.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        btnPurchaseDelete.setText("Delete");
        btnPurchaseDelete.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPurchaseDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPurchaseDeleteMouseClicked(evt);
            }
        });

        tblPurchase.setBackground(new java.awt.Color(255, 255, 204));
        tblPurchase.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblPurchase.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblPurchase.setSelectionBackground(new java.awt.Color(255, 204, 102));
        tblPurchase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPurchaseMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPurchase);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnPurchaseSubmit, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                            .addComponent(btnPurchaseReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(97, 97, 97)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnPurchaseUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                            .addComponent(btnPurchaseDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(32, 32, 32)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPurchaseSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPurchaseUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPurchaseReset, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPurchaseDelete))))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout purchaseLayout = new javax.swing.GroupLayout(purchase);
        purchase.setLayout(purchaseLayout);
        purchaseLayout.setHorizontalGroup(
            purchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        purchaseLayout.setVerticalGroup(
            purchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(purchaseLayout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menu.addTab("tab3", purchase);

        jPanel8.setBackground(new java.awt.Color(102, 51, 0));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 204));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Product");
        jPanel8.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1060, 50));

        jPanel27.setBackground(new java.awt.Color(255, 204, 102));
        jPanel27.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel25.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Product ID");
        jLabel25.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtProductId.setEditable(false);
        txtProductId.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtProductId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtProductIdMouseClicked(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("Product Name");
        jLabel26.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtProductName.setBackground(new java.awt.Color(240, 240, 240));
        txtProductName.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel28.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Product Code");
        jLabel28.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtProductCode.setBackground(new java.awt.Color(240, 240, 240));
        txtProductCode.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtProductCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductCodeActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Unit Price");
        jLabel29.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtProductUnitPrice.setBackground(new java.awt.Color(240, 240, 240));
        txtProductUnitPrice.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnProductSave.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        btnProductSave.setText("Save");
        btnProductSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductSaveMouseClicked(evt);
            }
        });

        btnProductUpdate.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        btnProductUpdate.setText("Update");
        btnProductUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductUpdateMouseClicked(evt);
            }
        });

        btnProductReset.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnProductReset.setText("Reset");
        btnProductReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductResetMouseClicked(evt);
            }
        });

        btnProductDelete.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnProductDelete.setText("Delete");
        btnProductDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductDeleteMouseClicked(evt);
            }
        });

        jLabel39.setBackground(new java.awt.Color(255, 153, 0));
        jLabel39.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblProduct);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel27Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtProductId, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtProductUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(btnProductReset, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnProductDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(btnProductSave, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnProductUpdate)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
            .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel27Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 1022, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(39, Short.MAX_VALUE)))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                            .addComponent(txtProductId))
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                            .addComponent(txtProductUnitPrice))
                        .addGap(200, 200, 200)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnProductSave)
                            .addComponent(btnProductUpdate))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnProductReset)
                            .addComponent(btnProductDelete))
                        .addGap(77, 77, 77))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout productLayout = new javax.swing.GroupLayout(product);
        product.setLayout(productLayout);
        productLayout.setHorizontalGroup(
            productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 1075, Short.MAX_VALUE)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        productLayout.setVerticalGroup(
            productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, 538, Short.MAX_VALUE))
        );

        menu.addTab("tab5", product);

        jPanel7.setBackground(new java.awt.Color(102, 51, 0));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 204));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Report");
        jPanel7.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 1040, 50));

        jPanel26.setBackground(new java.awt.Color(255, 204, 102));

        jButton3.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jButton3.setText("From");
        jButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton5.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jButton5.setText("To");

        buttonGroup1.add(radioReportPurchase);
        radioReportPurchase.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        radioReportPurchase.setText("Purchase");
        radioReportPurchase.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        radioReportPurchase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radioReportPurchaseMouseClicked(evt);
            }
        });

        buttonGroup1.add(radioReportDue);
        radioReportDue.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        radioReportDue.setText("Due");
        radioReportDue.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        buttonGroup1.add(radioReportSales);
        radioReportSales.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        radioReportSales.setText("Sales");
        radioReportSales.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnReportView.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        btnReportView.setText("View");
        btnReportView.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnReportView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportViewMouseClicked(evt);
            }
        });

        tblReport.setBackground(new java.awt.Color(255, 255, 204));
        tblReport.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblReport.setSelectionBackground(new java.awt.Color(255, 204, 102));
        jScrollPane2.setViewportView(tblReport);

        toDate.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        fromDate.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addGap(0, 90, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 887, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGap(207, 207, 207)
                        .addComponent(radioReportPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(radioReportDue))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(radioReportSales)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnReportView, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(253, 253, 253))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toDate, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fromDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioReportPurchase)
                    .addComponent(radioReportDue)
                    .addComponent(radioReportSales)
                    .addComponent(btnReportView))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout reportLayout = new javax.swing.GroupLayout(report);
        report.setLayout(reportLayout);
        reportLayout.setHorizontalGroup(
            reportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 1075, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reportLayout.createSequentialGroup()
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        reportLayout.setVerticalGroup(
            reportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menu.addTab("tab4", report);

        Customer.setBackground(new java.awt.Color(255, 204, 102));

        jPanel28.setBackground(new java.awt.Color(102, 51, 0));
        jPanel28.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 204));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Customer");
        jLabel23.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel28.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1060, 50));

        jPanel29.setBackground(new java.awt.Color(204, 204, 0));
        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel24.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Customer SL");
        jLabel24.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel29.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 130, 40));

        txtCustomerId.setEditable(false);
        txtCustomerId.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel29.add(txtCustomerId, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 230, 40));

        jLabel27.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Customer Name");
        jLabel27.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel29.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 130, 40));

        txtCustomerName.setBackground(new java.awt.Color(240, 240, 240));
        txtCustomerName.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtCustomerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerNameActionPerformed(evt);
            }
        });
        jPanel29.add(txtCustomerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, 230, 40));

        jLabel30.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Mobile No");
        jLabel30.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel29.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 130, 40));

        txtCustomerMobile.setBackground(new java.awt.Color(240, 240, 240));
        txtCustomerMobile.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel29.add(txtCustomerMobile, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 230, 40));

        jLabel31.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Address");
        jLabel31.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel29.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 130, 40));

        txtCustomerAddress.setBackground(new java.awt.Color(240, 240, 240));
        txtCustomerAddress.setColumns(20);
        txtCustomerAddress.setRows(5);
        txtCustomerAddress.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jScrollPane4.setViewportView(txtCustomerAddress);

        jPanel29.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 230, 40));

        jLabel32.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Due Amount");
        jLabel32.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel29.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 130, 40));

        txtCustomerDueAmount.setBackground(new java.awt.Color(240, 240, 240));
        txtCustomerDueAmount.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel29.add(txtCustomerDueAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 280, 230, 40));

        jLabel33.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Sales Date");
        jLabel33.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel29.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 130, 40));

        tblCustomer.setBackground(new java.awt.Color(255, 255, 204));
        tblCustomer.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblCustomer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblCustomer.setSelectionBackground(new java.awt.Color(255, 255, 204));
        tblCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCustomerMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblCustomer);

        jPanel29.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, 620, 450));

        btnCustomerReset.setBackground(new java.awt.Color(204, 204, 204));
        btnCustomerReset.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        btnCustomerReset.setText("Reset");
        btnCustomerReset.setBorder(new javax.swing.border.MatteBorder(null));
        btnCustomerReset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCustomerReset.setFocusCycleRoot(true);
        jPanel29.add(btnCustomerReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 490, 90, 30));

        btnCustomerUpdate.setBackground(new java.awt.Color(204, 204, 204));
        btnCustomerUpdate.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        btnCustomerUpdate.setText("Due Update");
        btnCustomerUpdate.setBorder(new javax.swing.border.MatteBorder(null));
        btnCustomerUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCustomerUpdate.setFocusCycleRoot(true);
        btnCustomerUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCustomerUpdateMouseClicked(evt);
            }
        });
        jPanel29.add(btnCustomerUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 490, 100, 30));

        txtcustomerSalesDate.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel29.add(txtcustomerSalesDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 230, 40));

        jLabel34.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("Receive Due");
        jLabel34.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel29.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 130, 40));

        txtCustomerReceiveDue.setBackground(new java.awt.Color(240, 240, 240));
        txtCustomerReceiveDue.setAutoscrolls(false);
        txtCustomerReceiveDue.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtCustomerReceiveDue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerReceiveDueActionPerformed(evt);
            }
        });
        jPanel29.add(txtCustomerReceiveDue, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 330, 230, 40));

        javax.swing.GroupLayout CustomerLayout = new javax.swing.GroupLayout(Customer);
        Customer.setLayout(CustomerLayout);
        CustomerLayout.setHorizontalGroup(
            CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomerLayout.createSequentialGroup()
                .addGroup(CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, 1062, Short.MAX_VALUE)
                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 13, Short.MAX_VALUE))
        );
        CustomerLayout.setVerticalGroup(
            CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomerLayout.createSequentialGroup()
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE))
        );

        menu.addTab("tab6", Customer);

        jPanel30.setBackground(new java.awt.Color(102, 51, 0));
        jPanel30.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel37.setFont(new java.awt.Font("Comic Sans MS", 0, 24)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 204));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Stock");
        jPanel30.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1060, 50));

        jPanel33.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel34.setBackground(new java.awt.Color(255, 255, 255,80));
        jPanel34.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblStock.setBackground(new java.awt.Color(0, 255, 255,80));
        tblStock.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(tblStock);

        jPanel34.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 760, 360));

        jPanel33.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 830, 390));

        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/stock-boards-with-unfocused-window.jpg"))); // NOI18N
        jPanel33.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1060, 533));

        javax.swing.GroupLayout stockLayout = new javax.swing.GroupLayout(stock);
        stock.setLayout(stockLayout);
        stockLayout.setHorizontalGroup(
            stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stockLayout.createSequentialGroup()
                .addGroup(stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, 1065, Short.MAX_VALUE)
                    .addGroup(stockLayout.createSequentialGroup()
                        .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        stockLayout.setVerticalGroup(
            stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stockLayout.createSequentialGroup()
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE))
        );

        menu.addTab("tab7", stock);

        jPanel3.add(menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -30, 1080, 630));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 1060, 600));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(0);
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnSalseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalseActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(1);
    }//GEN-LAST:event_btnSalseActionPerformed

    private void btnPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(2);
        //  getComboProductCode();

    }//GEN-LAST:event_btnPurchaseActionPerformed

    private void btnSalseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalseMouseEntered
        // TODO add your handling code here:
       
              stockTbl();
    }//GEN-LAST:event_btnSalseMouseEntered

    private void btnSalseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalseMouseExited
        // TODO add your handling code here:
//        btnSalse.setBackground(new Color(153, 255, 204));
    }//GEN-LAST:event_btnSalseMouseExited

    private void btnPurchaseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPurchaseMouseEntered

    private void btnPurchaseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseMouseExited
        // TODO add your handling code here:
//        btnHome.setBackground(new Color(150, 180, 50));
//        btnHome.setBackground(getBackground());
    }//GEN-LAST:event_btnPurchaseMouseExited

    private void txtSalesQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesQuantityFocusLost
        // TODO add your handling code here:

        try {
            if (txtSalesUnitPrice.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "cannot be empty");
                txtSalesUnitPrice.requestFocus();
            } else if (!txtSalesQuantity.getText().trim().isEmpty()) {
                txtSalesTotalPrice.setText(getTotalPrice() + " ");

            } else {
                JOptionPane.showMessageDialog(rootPane, "cannot be empty");
                txtSalesQuantity.requestFocus();

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "An error occured" + e);
        }
    }//GEN-LAST:event_txtSalesQuantityFocusLost

    private void txtSalseDiscountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalseDiscountFocusLost
        // TODO add your handling code here:
        try {
            if (!txtSalseDiscount.getText().trim().isEmpty()) {
                txtSalesActualPrice.setText(getActualPrice() + "");
            } else {
                JOptionPane.showMessageDialog(rootPane, "cannot be empty");
                txtSalseDiscount.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "other ocered" + e);
        }

//        try{
//            if(){
//            } else(){
//            
//            }
//        }catch(){
//        
//        }
    }//GEN-LAST:event_txtSalseDiscountFocusLost

    private void txtSalesCashRecievedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesCashRecievedFocusLost
        // TODO add your handling code here:

        try {
            if (!txtSalesCashRecieved.getText().trim().isEmpty()) {
                txtSalesDue.setText(getDueAmount() + "");
            } else {
                JOptionPane.showMessageDialog(rootPane, "cannot be empty");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "cannot be empty" + e);
        }
    }//GEN-LAST:event_txtSalesCashRecievedFocusLost

    private void btnPurchaseSubmitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseSubmitMouseClicked
        // TODO add your handling code here:

        sql = "insert into purchase (name,code,unitPrice,quantity,totalPrice,date)value(?,?,?,?,?,?)";
        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setString(1, comboPurchaseProductName.getSelectedItem().toString());
            ps.setString(2, comboPurchaseProductCode.getSelectedItem().toString());
            ps.setFloat(3, Float.parseFloat(txtPurchaseUnitPrice.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtPurchaseQuantity.getText().trim()));
            ps.setFloat(5, Float.parseFloat(txtPurchaseTotalPrice.getText().trim()));
            ps.setDate(6, convertUtilDateToSqlDate(datePurchase.getDate()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Inserted");

            purchaseTbl();

            setQunantityPurchase();
            resetPurchase();

            //  getQuantityFromStock();
        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPurchaseSubmitMouseClicked

    private void txtPurchaseUnitPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseUnitPriceFocusLost
        // TODO add your handling code here:
        try {
            if (!txtPurchaseUnitPrice.getText().trim().isEmpty()) {
            } else {
                JOptionPane.showMessageDialog(rootPane, "Cannot be empty");
                txtPurchaseUnitPrice.requestFocus();
            }

        } catch (Exception e) {

        }
    }//GEN-LAST:event_txtPurchaseUnitPriceFocusLost

    private void txtPurchaseQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseQuantityFocusLost
        // TODO add your handling code here:
        try {
            if (txtPurchaseUnitPrice.getText().trim().isEmpty()) {
                txtPurchaseUnitPrice.requestFocus();
            } else if (!txtPurchaseQuantity.getText().trim().isEmpty()) {
                txtPurchaseTotalPrice.setText(getPurchaseTotalPrice() + "");

            } else {
                JOptionPane.showMessageDialog(rootPane, "cannot be empty");
                txtPurchaseQuantity.requestFocus();
            }

        } catch (Exception e) {

        }

    }//GEN-LAST:event_txtPurchaseQuantityFocusLost

    private void btnPurchaseResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseResetMouseClicked
        // TODO add your handling code here:
        resetPurchase();
    }//GEN-LAST:event_btnPurchaseResetMouseClicked

    private void btnPurchaseUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseUpdateMouseClicked
        // TODO add your handling code here:

        // int id=Integer.parseInt(txtPurchaseId.getText());
        sql = "update purchase set name = ?, code=? , unitPrice=?, quantity = ? , totalPrice = ?, date=?  where id=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, comboPurchaseProductName.getSelectedItem().toString());
            ps.setString(2, comboPurchaseProductCode.getSelectedItem().toString());
            ps.setFloat(3, Float.parseFloat(txtPurchaseUnitPrice.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtPurchaseQuantity.getText().trim()));
            ps.setFloat(5, Float.parseFloat(txtPurchaseTotalPrice.getText().trim()));
            ps.setDate(6, convertUtilDateToSqlDate(datePurchase.getDate()));
            ps.setInt(7, Integer.parseInt(txtPurchaseId.getText().trim()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();
            JOptionPane.showConfirmDialog(rootPane, "data updated");

            resetPurchase();
            purchaseTbl();

        } catch (SQLException ex) {
            JOptionPane.showConfirmDialog(rootPane, "data not updated");
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnPurchaseUpdateMouseClicked

    private void tblPurchaseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPurchaseMouseClicked
        // TODO add your handling code here:
        int rowIndex = tblPurchase.getSelectedRow();

        String id = tblPurchase.getModel().getValueAt(rowIndex, 0).toString();
        String name = tblPurchase.getModel().getValueAt(rowIndex, 1).toString();
        String code = tblPurchase.getModel().getValueAt(rowIndex, 2).toString();
        String unitPrice = tblPurchase.getModel().getValueAt(rowIndex, 3).toString();
        String quantity = tblPurchase.getModel().getValueAt(rowIndex, 4).toString();
        String totalPrice = tblPurchase.getModel().getValueAt(rowIndex, 5).toString();
        String dateU = tblPurchase.getModel().getValueAt(rowIndex, 6).toString();

        txtPurchaseId.setText(id);
        comboPurchaseProductName.setSelectedItem(name);
        comboPurchaseProductCode.setSelectedItem(code);
        txtPurchaseUnitPrice.setText(unitPrice);
        txtPurchaseQuantity.setText(quantity);
        txtPurchaseTotalPrice.setText(totalPrice);
        datePurchase.setDate(convertStringToDate(dateU));
        //  java.util.Date(datePurchase.setDate(dateU));
        //convertSqlDateToUtilDate(datePurchase.setDate(dateU));
    }//GEN-LAST:event_tblPurchaseMouseClicked

    private void btnPurchaseDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseDeleteMouseClicked
        // TODO add your handling code here:
        sql = "delete  from purchase where id=?";

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(txtPurchaseId.getText()));
            ps.executeUpdate();
            ps.close();
            con.getCon().close();
            JOptionPane.showConfirmDialog(rootPane, "data delated");

            resetPurchase();
            purchaseTbl();

        } catch (SQLException ex) {
            JOptionPane.showConfirmDialog(rootPane, "data not delated");
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnPurchaseDeleteMouseClicked

    private void btnReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(4);
    }//GEN-LAST:event_btnReportActionPerformed

    private void btnProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(3);
    }//GEN-LAST:event_btnProductActionPerformed

    private void btnProductSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductSaveMouseClicked
        // TODO add your handling code here:

        sql = "insert into product (name,code,unitPrice) value (?,?,?)";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, txtProductName.getText().trim());
            ps.setString(2, txtProductCode.getText().trim());
            ps.setFloat(3, Float.parseFloat(txtProductUnitPrice.getText().trim()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();
            JOptionPane.showMessageDialog(rootPane, "New Product Is Added");
            productTbl();
            getStockProduct();

            resetProduct();

            getComboProductName();
            getComboProductSalesName();
            getComboProductCode();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, " Product Name cannot be dublicate .New Product Is not Added");

            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnProductSaveMouseClicked

    private void tblProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductMouseClicked
        // TODO add your handling code here:
        int getTblProSelectedRow = tblProduct.getSelectedRow();
        String id = tblProduct.getModel().getValueAt(getTblProSelectedRow, 0).toString();
        String name = tblProduct.getModel().getValueAt(getTblProSelectedRow, 1).toString();
        String code = tblProduct.getModel().getValueAt(getTblProSelectedRow, 2).toString();
        String unitPrice = tblProduct.getModel().getValueAt(getTblProSelectedRow, 3).toString();

        txtProductId.setText(id);
        txtProductName.setText(name);
        txtProductCode.setText(code);
        txtProductUnitPrice.setText(unitPrice);

    }//GEN-LAST:event_tblProductMouseClicked

    private void btnProductResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductResetMouseClicked
        // TODO add your handling code here:
        resetProduct();
    }//GEN-LAST:event_btnProductResetMouseClicked

    private void btnProductUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductUpdateMouseClicked
        // TODO add your handling code here:

        sql = "update  product set name=? , code=? , unitPrice=? where id=?";

        try {
            ps = con.getCon().prepareStatement(sql);

//            rs= ps.executeQuery();
//            while
            ps.setString(1, txtProductName.getText().trim());
            ps.setString(2, txtProductCode.getText().trim());
            ps.setString(3, txtProductUnitPrice.getText().trim());
            ps.setInt(4, Integer.parseInt(txtProductId.getText().trim()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Updated");

            resetProduct();
            productTbl();

        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnProductUpdateMouseClicked

    private void btnProductDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductDeleteMouseClicked
        // TODO add your handling code here:
        sql = "delete  from product where id= ?";

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(txtProductId.getText()));
            ps.executeUpdate();
            ps.close();
            con.getCon().close();
            JOptionPane.showConfirmDialog(rootPane, "data delated");

            resetProduct();
            productTbl();

        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnProductDeleteMouseClicked

    private void txtProductIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtProductIdMouseClicked
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(rootPane, " Id will be generated automatically");
        txtProductName.requestFocus();
    }//GEN-LAST:event_txtProductIdMouseClicked

    private void txtSalesUnitPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesUnitPriceFocusLost
        // TODO add your handling code here:
        try {
            if (!txtSalesUnitPrice.getText().trim().isEmpty()) {
                //
            } else {
                JOptionPane.showMessageDialog(rootPane, "cannot be empty");
                txtSalesUnitPrice.requestFocus();

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "An error occured" + e);
        }

    }//GEN-LAST:event_txtSalesUnitPriceFocusLost

    private void btnCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(5);
    }//GEN-LAST:event_btnCustomerActionPerformed

    private void txtCustomerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomerNameActionPerformed

    private void txtProductCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductCodeActionPerformed

    private void btnStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(6);
    }//GEN-LAST:event_btnStockActionPerformed

    private void txtSalesSIDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSalesSIDMouseClicked
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(rootPane, "Id genetates automatically");
        comboSalesProductName.requestFocus();

    }//GEN-LAST:event_txtSalesSIDMouseClicked

    private void tblSalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSalesMouseClicked
        // TODO add your handling code here:

        /*  
       int getTblProSelectedRow= tblProduct.getSelectedRow();
        
       String id = tblProduct.getModel().getValueAt(getTblProSelectedRow, 0).toString();
       String name = tblProduct.getModel().getValueAt(getTblProSelectedRow, 1).toString();
       String code = tblProduct.getModel().getValueAt(getTblProSelectedRow, 2).toString();
       String unitPrice = tblProduct.getModel().getValueAt(getTblProSelectedRow, 3).toString();
       
       txtProductId.setText(id);
       txtProductName.setText(name);
       txtProductCode.setText(code);
       txtProductUnitPrice.setText(unitPrice);
         */
        int rowIndex = tblSales.getSelectedRow();
//
        String id = tblSales.getModel().getValueAt(rowIndex, 0).toString();
        String name = tblSales.getModel().getValueAt(rowIndex, 1).toString();
    
        String up = tblSales.getModel().getValueAt(rowIndex, 2).toString();
        float UnitPrice = Float.parseFloat(up);
        String UnitPriceWithTenPer= String.valueOf(UnitPrice+ UnitPrice/10);
          txtSalesUnitPrice.setText(UnitPriceWithTenPer);
//        String qu = tblSales.getModel().getValueAt(rowIndex, 3).toString();
//        String tp = tblSales.getModel().getValueAt(rowIndex, 4).toString();
//        String di = tblSales.getModel().getValueAt(rowIndex, 5).toString();
//        String acp = tblSales.getModel().getValueAt(rowIndex, 6).toString();
//        String cr = tblSales.getModel().getValueAt(rowIndex, 7).toString();
//        String du = tblSales.getModel().getValueAt(rowIndex, 8).toString();
//
        txtSalesSID.setText(id);
        comboSalesProductName.setSelectedItem(name);
     
//        txtSalesQuantity.setText(qu);
//        txtSalesTotalPrice.setText(tp);
//        txtSalseDiscount.setText(di);
//        txtSalesActualPrice.setText(acp);
//        txtSalesCashRecieved.setText(cr);
//        txtSalesDue.setText(du);

        // Handle the case when no row is selected (rowIndex == -1)
        // For example, you can display a message to the user or clear the text fields.
        // JOptionPane.showMessageDialog(this, "Please select a row.");
        // Clear text fields:
        // txtSalesSID.setText("");
        // txtSalesPruductName.setText("");
        // ... (clear other fields)

    }//GEN-LAST:event_tblSalesMouseClicked

    private void btnStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStockMouseClicked
        // TODO add your handling code here:
         btnHome.setBackground(new Color(255, 115,22));
         btnSalse.setBackground(new Color(255, 115,22));
         btnPurchase.setBackground(new Color(255, 115,22));
         btnReport.setBackground(new Color(255, 115,22));
         btnProduct.setBackground(new Color(255, 115,22));
         btnCustomer.setBackground(new Color(255, 115,22));
         btnStock.setBackground(new Color(255, 255, 255));
        stockTbl();
    }//GEN-LAST:event_btnStockMouseClicked

    private void tblCartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCartMouseClicked
        
    }//GEN-LAST:event_tblCartMouseClicked

    private void btnPurchaseSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseSubmitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPurchaseSubmitActionPerformed

    
    public static PdfPCell getIRHCell(String text, int alignment) {
		FontSelector fs = new FontSelector();
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 16);
		/*	font.setColor(BaseColor.GRAY);*/
		fs.addFont(font);
		Phrase phrase = fs.process(text);
		PdfPCell cell = new PdfPCell(phrase);
		cell.setPadding(5);
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(PdfPCell.NO_BORDER);
		return cell;
	}
    
    
    
    
   
	public static PdfPCell getIRDCell(String text) {
		PdfPCell cell = new PdfPCell (new Paragraph (text));
		cell.setHorizontalAlignment (Element.ALIGN_CENTER);
		cell.setPadding (5.0f);
		cell.setBorderColor(BaseColor.LIGHT_GRAY);
		return cell;
	}

	public static PdfPCell getBillHeaderCell(String text) {
		FontSelector fs = new FontSelector();
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 11);
		font.setColor(BaseColor.GRAY);
		fs.addFont(font);
		Phrase phrase = fs.process(text);
		PdfPCell cell = new PdfPCell (phrase);
		cell.setHorizontalAlignment (Element.ALIGN_CENTER);
		cell.setPadding (5.0f);
		return cell;
	}

	public static PdfPCell getBillRowCell(String text) {
		PdfPCell cell = new PdfPCell (new Paragraph (text));
		cell.setHorizontalAlignment (Element.ALIGN_CENTER);
		cell.setPadding (5.0f);
		cell.setBorderWidthBottom(0);
		cell.setBorderWidthTop(0);
		return cell;
	}

	public static PdfPCell getBillFooterCell(String text) {
		PdfPCell cell = new PdfPCell (new Paragraph (text));
		cell.setHorizontalAlignment (Element.ALIGN_CENTER);
		cell.setPadding (5.0f);
		cell.setBorderWidthBottom(0);
		cell.setBorderWidthTop(0);
		return cell;
	}

	public static PdfPCell getValidityCell(String text) {
		FontSelector fs = new FontSelector();
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
		font.setColor(BaseColor.GRAY);
		fs.addFont(font);
		Phrase phrase = fs.process(text);
		PdfPCell cell = new PdfPCell (phrase);		
		cell.setBorder(0);
		return cell;
	}

	public static PdfPCell getAccountsCell(String text) {
		FontSelector fs = new FontSelector();
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
		fs.addFont(font);
		Phrase phrase = fs.process(text);
		PdfPCell cell = new PdfPCell (phrase);		
		cell.setBorderWidthRight(0);
		cell.setBorderWidthTop(0);
		cell.setPadding (5.0f);
		return cell;
	}
	public static PdfPCell getAccountsCellR(String text) {
		FontSelector fs = new FontSelector();
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
		fs.addFont(font);
		Phrase phrase = fs.process(text);
		PdfPCell cell = new PdfPCell (phrase);		
		cell.setBorderWidthLeft(0);
		cell.setBorderWidthTop(0);
		cell.setHorizontalAlignment (Element.ALIGN_RIGHT);
		cell.setPadding (5.0f);
		cell.setPaddingRight(20.0f);
		return cell;
	}

	public static PdfPCell getdescCell(String text) {
		FontSelector fs = new FontSelector();
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
		font.setColor(BaseColor.GRAY);
		fs.addFont(font);
		Phrase phrase = fs.process(text);
		PdfPCell cell = new PdfPCell (phrase);	
		cell.setHorizontalAlignment (Element.ALIGN_CENTER);
		cell.setBorder(0);
		return cell;
	}
    
    
    public void printSale(){
                                               
        // TODO add your handling code here:
        
        
       String path = "";
       JFileChooser j = new JFileChooser();
       j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
       
       
       int x = j.showSaveDialog(btnReport);
//       if(x==JFileChooser.APPROVE_OPTION){
//           path = j.getSelectedFile().getPath();
//       }

    if (x == JFileChooser.APPROVE_OPTION) {
        path = j.getSelectedFile().getPath();
        String fileName = "abc123.pdf";
        File file = new File(path, fileName);
        int count = 1;
        while (file.exists()) {
            fileName = txtSalesCaustomerName.getText().trim()+ count + ".pdf"; // Append a number to the file name
            file = new File(path, fileName);
            count++;
        }

        path = file.getAbsolutePath();
    }

       
        Document doc= new Document();
        try {
            //
           // PdfWriter.getInstance(doc, new FileOutputStream(path+"abc123.pdf"));
                PdfWriter.getInstance(doc, new FileOutputStream(path));
             doc.open();
             //Header img 
             
               Image image = Image.getInstance("src/resources/header.jpg");
               image.scaleAbsolute(540f, 72f);
             
               FontSelector fs = new FontSelector();
               Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
               fs.addFont(font);
            //   long currentTimeMillis = System.currentTimeMillis();
            long currentTimeMillis = System.currentTimeMillis();
Timestamp timestamp = new Timestamp(currentTimeMillis);

// Format the timestamp as a string (you can adjust the format as per your requirements)
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Example format
String formattedTimestamp = dateFormat.format(timestamp);



            
               PdfPTable irdTable = new PdfPTable(1);
			//irdTable.addCell(getIRDCell(""));
			irdTable.addCell(getIRDCell("Invoice Date & Time"));
			//irdTable.addCell(getIRDCell("13-Mar-2016")); // pass invoice number
			irdTable.addCell(getIRDCell(formattedTimestamp)); // pass invoice date				

			PdfPTable irhTable = new PdfPTable(3);
			irhTable.setWidthPercentage(100);

			irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
			irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
			irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
			irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
			irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
			PdfPCell invoiceTable = new PdfPCell (irdTable);
			invoiceTable.setBorder(0);
			irhTable.addCell(invoiceTable);
            
               
             Phrase phrase = fs.process(" Client Details :");
             
             
             
             Paragraph name = new Paragraph(txtSalesCaustomerName.getText().trim());
             name.setIndentationLeft(20);
             
             Paragraph phone = new Paragraph(txtSalesCaustomerMobile.getText().trim());
             phone.setIndentationLeft(20);
             
             Paragraph address = new Paragraph("Dhaka, bangladesh");
             address.setIndentationLeft(20);
             Paragraph blankk = new Paragraph("  ");
             
             
        PdfPTable tbl = new PdfPTable (6);
                
               	tbl.setWidthPercentage(100);
       
        tbl.addCell("Product Name");
        tbl.addCell("Unit Price");
        tbl.addCell("Quantity");
        tbl.addCell("Total Price");
        tbl.addCell("Discount");
        tbl.addCell("Actual Price");
       
        for(int i= 0; i<tblCart.getRowCount(); i++){
            String pName = tblCart.getValueAt(i, 0) !=null ? tblCart.getValueAt(i, 0).toString():"";
            String up = tblCart.getValueAt(i, 1) !=null ? tblCart.getValueAt(i, 1).toString():"";
            String qu = tblCart.getValueAt(i, 2) !=null ? tblCart.getValueAt(i, 2).toString():"";
            String tp = tblCart.getValueAt(i, 3) !=null ? tblCart.getValueAt(i,3).toString():"";
            String dis = tblCart.getValueAt(i, 4) !=null ? tblCart.getValueAt(i, 4).toString():"";
            String ac = tblCart.getValueAt(i, 5) !=null ? tblCart.getValueAt(i, 5).toString():"";
           
                
           
            tbl.addCell(pName);
            tbl.addCell(up);
            tbl.addCell(qu);
            tbl.addCell(tp);
            tbl.addCell(dis);
            tbl.addCell(ac);
            
            
           
            
            
            
//.getValueAt(i,0).toStrig;
        }
        
           tbl.addCell(" ");
            tbl.addCell("");
            tbl.addCell("");
            tbl.addCell("");
            tbl.addCell("");
            tbl.addCell("");
            
            tbl.addCell(" ");
            tbl.addCell("");
            tbl.addCell("");
            tbl.addCell("");
            tbl.addCell("");
            tbl.addCell("");
            
        PdfPTable validity = new PdfPTable(1);
			validity.setWidthPercentage(100);
			validity.addCell(getValidityCell(" "));
			validity.addCell(getValidityCell("Warranty"));
			validity.addCell(getValidityCell(" * Products purchased comes with 1 year warranty"));
			validity.addCell(getValidityCell(" * Should be claimed to respective manufactures"));		    
			PdfPCell summaryL = new PdfPCell (validity);
			summaryL.setColspan (3);
			summaryL.setPadding (1.0f);	                   
			tbl.addCell(summaryL);

			PdfPTable accounts = new PdfPTable(2);
			accounts.setWidthPercentage(100);
			accounts.addCell(getAccountsCell("Subtotal"));
			accounts.addCell(getAccountsCellR(txtSalesCartGrandTotal.getText().trim()));
			accounts.addCell(getAccountsCell("Cash Receive"));
			accounts.addCell(getAccountsCellR(txtSalesCashRecieved.getText().trim()));
			accounts.addCell(getAccountsCell("Due Amount"));
			accounts.addCell(getAccountsCellR(txtSalesDue.getText().trim()));
//			accounts.addCell(getAccountsCell("Total"));
//			accounts.addCell(getAccountsCellR("11673.55"));			
			PdfPCell summaryR = new PdfPCell (accounts);
			summaryR.setColspan (3);         
			tbl.addCell(summaryR);  

			PdfPTable describer = new PdfPTable(1);
			describer.setWidthPercentage(100);
			describer.addCell(getdescCell(" "));
			describer.addCell(getdescCell("Goods once sold will not be taken back or exchanged "));	

			
                        

                 Image imageSign = Image.getInstance("src/resources/hSign.png");
               imageSign.scaleAbsolute(75, 20); 
               imageSign.setIndentationLeft(440);     
                        
       Paragraph seller = new Paragraph(txtSalesSaller.getText().trim());
             seller.setIndentationLeft(460);                 
                        
        
           doc.add(image);
           doc.add(irhTable);
           doc.add(phrase);
           
           doc.add(name);
           doc.add(phone);
           doc.add(address);
           doc.add(blankk);
           
              doc.add(tbl);
             // doc.add(billTable);
			doc.add(describer);
              
              
                   doc.add(blankk);
                   doc.add(blankk);
                   
                   doc.add(imageSign);
                   doc.add(seller);
                        
                   
              
            doc.close();
            
            
            DefaultTableModel model = (DefaultTableModel)tblCart.getModel();
          //  model.setRowCount(0);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }
               
       
 
    }
    
    private void btnSalesSubmitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesSubmitMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)tblCart.getModel();
        int rowcount = model.getRowCount();
        for (int i = 0; i < rowcount; i++) {
            String proName = tblCart.getModel().getValueAt(i, 0).toString();
            float uniP = Float.parseFloat(tblCart.getModel().getValueAt(i, 1).toString());
            float qty = Float.parseFloat(tblCart.getModel().getValueAt(i, 2).toString());
            float toaP = Float.parseFloat(tblCart.getModel().getValueAt(i, 3).toString());
            float dis = Float.parseFloat(tblCart.getModel().getValueAt(i, 4).toString());
            float actP = Float.parseFloat(tblCart.getModel().getValueAt(i, 5).toString());
            float groundTotal = Float.parseFloat(txtSalesCartGrandTotal.getText().trim());
          float cashR = Float.parseFloat(txtSalesCashRecieved.getText().trim());
          float due = Float.parseFloat(txtSalesDue.getText().trim());
            String cusN = txtSalesCaustomerName.getText().trim();
            String saller = txtSalesSaller.getText().trim();
            String mobile = txtSalesCaustomerMobile.getText().trim();
            Date saledate = convertUtilDateToSqlDate(dateSales.getDate());

            sql = "INSERT INTO sales (productName, unitPrice, quantity, totalPrice, discount, actualPrice, groundTotal, cashReceived,dueAmount,  castomerName,mobile,date , saller) VALUES (  ?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            try {
                ps = con.getCon().prepareStatement(sql);
                ps.setString(1, proName);
                ps.setFloat(2, uniP);
                ps.setFloat(3, qty);
                ps.setFloat(4, toaP);
                ps.setFloat(5, dis);
                ps.setFloat(6, actP);
                ps.setFloat(7, groundTotal);
                
            ps.setFloat(8, cashR);
           ps.setFloat(9, due);
                ps.setString(10, cusN);
                ps.setString(11, mobile);
                ps.setDate(12, saledate);
                ps.setString(13, saller);

                ps.executeUpdate();

                ps.close();
                con.getCon().close();

                getComboProductName();
                setQunantitySales( proName, qty);

                // salesTbl();

                JOptionPane.showMessageDialog(rootPane, "Order complete.");
            } catch (SQLException ex) {
                Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        stockTbl();
         inserDatatoCustomer();
         printSale();
        model.setRowCount(0);
        //   System.out.println("rowcount"+ rowcount);
//        if(rowcount>0)
//
//        for (int i = 0; i<rowcount-1 ; i++) {
//            model.removeRow(i);
//        }

           
    }//GEN-LAST:event_btnSalesSubmitMouseClicked
        float sumActualPrice;
    public void getSalesGroundTotal(){
         sumActualPrice = sumActualPrice+ Float.parseFloat(txtSalesActualPrice.getText().trim());
         txtSalesCartGrandTotal. setText(String.valueOf(sumActualPrice));
    
    }
    
    
    
    private void btnAddToCardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddToCardMouseClicked
        String proName = comboSalesProductName.getModel().getSelectedItem().toString();
        float uniP = Float.parseFloat(txtSalesUnitPrice.getText().trim());
        float qty = Float.parseFloat(txtSalesQuantity.getText().trim());
        float toaP = Float.parseFloat(txtSalesTotalPrice.getText().trim());
        float dis = Float.parseFloat(txtSalseDiscount.getText().trim());
        float actP = Float.parseFloat(txtSalesActualPrice.getText().trim());
//        float cashP = Float.parseFloat(txtSalesCashRecieved.getText().trim());
//        float due = Float.parseFloat(txtSalesDue.getText().trim());
//        String cusN = txtSalesCaustomerName.getText().trim();
//        String saller = txtSalesSaller.getText().trim();
//        String mobile = txtSalesCaustomerMobile.getText().trim();
//        Date saledate = convertUtilDateToSqlDate(dateSales.getDate());
getSalesGroundTotal();

        List<Object> productList = new ArrayList();
        productList.add(new Object[]{proName, uniP, qty, toaP, dis, actP});

        DefaultTableModel model = (DefaultTableModel) tblCart.getModel();
        for (Object i : productList) {

            model.addRow((Object[]) i);

        }
        resetSales();

    }//GEN-LAST:event_btnAddToCardMouseClicked
 
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
        resetSales();
    }//GEN-LAST:event_jButton2MouseClicked

    private void txtSalesSallerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSalesSallerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalesSallerActionPerformed

    private void txtSalesCartGrandTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSalesCartGrandTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalesCartGrandTotalActionPerformed

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        // TODO add your handling code here:
        jLabel42.setText(String.valueOf(homeTodaysSales()));
        jLabel46.setText(String.valueOf(homeTodaysDue()));
        jLabel44.setText(String.valueOf(homeTodaysPurchase()));
        
        jLabel52.setText(String.valueOf(  getLast30DaysTotalSales()));
        jLabel50.setText(String.valueOf( getLast30DaysTotalDue()));
        jLabel48.setText(String.valueOf(  getLast30DaysTotalPurchse()));
        
      
        
               btnHome.setBackground(new Color(255, 255, 255));
         btnSalse.setBackground(new Color(255, 115,22));
         btnPurchase.setBackground(new Color(255, 115,22));
         btnReport.setBackground(new Color(255, 115,22));
         btnProduct.setBackground(new Color(255, 115,22));
         btnCustomer.setBackground(new Color(255, 115,22));
         btnStock.setBackground(new Color(255, 115,22));
        
    }//GEN-LAST:event_btnHomeMouseClicked

    private void btnSalseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalseMouseClicked
        // TODO add your handling code here:
               btnHome.setBackground(new Color(255, 115,22));
       btnCustomer.setBackground(new Color(255, 115,22));
         btnPurchase.setBackground(new Color(255, 115,22));
         btnReport.setBackground(new Color(255, 115,22));
         btnProduct.setBackground(new Color(255, 115,22));
        btnSalse .setBackground(new Color(255, 255, 255));
         btnStock.setBackground(new Color(255, 115,22));
    }//GEN-LAST:event_btnSalseMouseClicked

    private void btnPurchaseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseMouseClicked
        // TODO add your handling code here:
              btnHome.setBackground(new Color(255, 115,22));
         btnSalse.setBackground(new Color(255, 115,22));
         btnCustomer.setBackground(new Color(255, 115,22));
         btnReport.setBackground(new Color(255, 115,22));
         btnProduct.setBackground(new Color(255, 115,22));
         btnPurchase.setBackground(new Color(255, 255, 255));
         btnStock.setBackground(new Color(255, 115,22));
    }//GEN-LAST:event_btnPurchaseMouseClicked

    private void btnReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportMouseClicked
        // TODO add your handling code here:
             btnHome.setBackground(new Color(255, 115,22));
         btnSalse.setBackground(new Color(255, 115,22));
         btnPurchase.setBackground(new Color(255, 115,22));
         btnCustomer.setBackground(new Color(255, 115,22));
         btnProduct.setBackground(new Color(255, 115,22));
         btnReport.setBackground(new Color(255, 255, 255));
         btnStock.setBackground(new Color(255, 115,22));
    }//GEN-LAST:event_btnReportMouseClicked

    private void btnProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductMouseClicked
        // TODO add your handling code here:
         btnHome.setBackground(new Color(255, 115,22));
         btnSalse.setBackground(new Color(255, 115,22));
         btnPurchase.setBackground(new Color(255, 115,22));
         btnReport.setBackground(new Color(255, 115,22));
         btnCustomer.setBackground(new Color(255, 115,22));
         btnProduct.setBackground(new Color(255, 255, 255));
         btnStock.setBackground(new Color(255, 115,22));
    }//GEN-LAST:event_btnProductMouseClicked

    private void btnCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomerMouseClicked
        // TODO add your handling code here:
        
        tblCustomer();
        
         btnHome.setBackground(new Color(255, 115,22));
         btnSalse.setBackground(new Color(255, 115,22));
         btnPurchase.setBackground(new Color(255, 115,22));
         btnReport.setBackground(new Color(255, 115,22));
         btnProduct.setBackground(new Color(255, 115,22));
         btnCustomer.setBackground(new Color(255, 255, 255));
         btnStock.setBackground(new Color(255, 115,22));
        
        
       
    }//GEN-LAST:event_btnCustomerMouseClicked

    private void tblCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCustomerMouseClicked
        // TODO add your handling code here:
          int rowIndex = tblCustomer.getSelectedRow();

        String id = tblCustomer.getModel().getValueAt(rowIndex, 0).toString();
        String name = tblCustomer.getModel().getValueAt(rowIndex, 1).toString();
        String mobile = tblCustomer.getModel().getValueAt(rowIndex, 2).toString();
        String address = tblCustomer.getModel().getValueAt(rowIndex, 3).toString();
         String dateU  = tblCustomer.getModel().getValueAt(rowIndex, 4).toString();
        String due = tblCustomer.getModel().getValueAt(rowIndex, 5).toString();
       

        txtCustomerId.setText(id);
        txtCustomerName.setText(name);
        txtCustomerMobile.setText(mobile);
        txtCustomerAddress.setText(address);
          txtcustomerSalesDate.setDate(convertStringToDate(dateU));
           txtCustomerDueAmount .setText(due);
      
        
    }//GEN-LAST:event_tblCustomerMouseClicked

    private void txtCustomerReceiveDueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerReceiveDueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomerReceiveDueActionPerformed

    public float dueUpdate(){
      float dueOld =  Float.parseFloat(txtCustomerDueAmount.getText().trim());
      float dueReceive =  Float.parseFloat(txtCustomerReceiveDue.getText().trim());
      float finalDue =  dueOld - dueReceive;
      
      return finalDue;
    }
    
    private void btnCustomerUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomerUpdateMouseClicked
        // TODO add your handling code here:
        sql = "update  customer set  dueAmount=? where id=? ";
        
        
         
        try {
            ps = con.getCon().prepareStatement(sql);
            
            ps.setFloat(1,dueUpdate());
              //  System.out.println("txtCustomerId.getText().trim()"+txtCustomerId.getText().trim()+"  final due"+dueUpdate());
                
                
            ps.setInt(2, Integer.parseInt(txtCustomerId.getText().trim()));

            
            ps.executeUpdate();
            ps.close();
            con.getCon().close();
            JOptionPane.showMessageDialog(rootPane, "Due updated");
            tblCustomer();
        } catch (SQLException ex) {
            Logger.getLogger(ItFirm.class.getName()).log(Level.SEVERE, null, ex);
        }

          
           
            
    }//GEN-LAST:event_btnCustomerUpdateMouseClicked

    private void btnReportViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportViewMouseClicked
        // TODO add your handling code here:
        if(radioReportSales.isSelected()){
            showSalesReport(fromDate.getDate(), toDate.getDate());
        }
        else if(radioReportPurchase.isSelected()){
             showPurchaseReport(fromDate.getDate(), toDate.getDate());
        }
        else showDueReport(fromDate.getDate(), toDate.getDate());
       
       
       
    }//GEN-LAST:event_btnReportViewMouseClicked

    private void radioReportPurchaseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radioReportPurchaseMouseClicked
        // TODO add your handling code here:
         
    }//GEN-LAST:event_radioReportPurchaseMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ItFirm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ItFirm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ItFirm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ItFirm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ItFirm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Customer;
    private javax.swing.JButton btnAddToCard;
    private javax.swing.JButton btnCustomer;
    private javax.swing.JButton btnCustomerReset;
    private javax.swing.JButton btnCustomerUpdate;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnProduct;
    private javax.swing.JButton btnProductDelete;
    private javax.swing.JButton btnProductReset;
    private javax.swing.JButton btnProductSave;
    private javax.swing.JButton btnProductUpdate;
    private javax.swing.JButton btnPurchase;
    private javax.swing.JButton btnPurchaseDelete;
    private javax.swing.JButton btnPurchaseReset;
    private javax.swing.JButton btnPurchaseSubmit;
    private javax.swing.JButton btnPurchaseUpdate;
    private javax.swing.JButton btnReport;
    private javax.swing.JToggleButton btnReportView;
    private javax.swing.JButton btnSalesSubmit;
    private javax.swing.JButton btnSalse;
    private javax.swing.JButton btnStock;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> comboPurchaseProductCode;
    private javax.swing.JComboBox<String> comboPurchaseProductName;
    private javax.swing.JComboBox<String> comboSalesProductName;
    private com.toedter.calendar.JDateChooser datePurchase;
    private com.toedter.calendar.JDateChooser dateSales;
    private com.toedter.calendar.JDateChooser fromDate;
    private javax.swing.JPanel home;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JLabel lblRotalPrice;
    private javax.swing.JLabel lblSalesQuality;
    private javax.swing.JLabel lblSalesUnitPrice;
    private javax.swing.JTabbedPane menu;
    private javax.swing.JPanel product;
    private javax.swing.JPanel purchase;
    private javax.swing.JRadioButton radioReportDue;
    private javax.swing.JRadioButton radioReportPurchase;
    private javax.swing.JRadioButton radioReportSales;
    private javax.swing.JPanel report;
    private javax.swing.JPanel sales;
    private javax.swing.JPanel stock;
    private javax.swing.JTable tblCart;
    private javax.swing.JTable tblCustomer;
    private javax.swing.JTable tblProduct;
    private javax.swing.JTable tblPurchase;
    private javax.swing.JTable tblReport;
    private javax.swing.JTable tblSales;
    private javax.swing.JTable tblStock;
    private com.toedter.calendar.JDateChooser toDate;
    private javax.swing.JTextArea txtCustomerAddress;
    private javax.swing.JTextField txtCustomerDueAmount;
    private javax.swing.JTextField txtCustomerId;
    private javax.swing.JTextField txtCustomerMobile;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtCustomerReceiveDue;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtProductId;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtProductUnitPrice;
    private javax.swing.JTextField txtPurchaseId;
    private javax.swing.JTextField txtPurchaseQuantity;
    private javax.swing.JTextField txtPurchaseTotalPrice;
    private javax.swing.JTextField txtPurchaseUnitPrice;
    private javax.swing.JTextField txtSalesActualPrice;
    private javax.swing.JLabel txtSalesActualPrict;
    private javax.swing.JLabel txtSalesActualPrict1;
    private javax.swing.JLabel txtSalesActualPrict2;
    private javax.swing.JLabel txtSalesActualPrict3;
    private javax.swing.JLabel txtSalesActualPrict4;
    private javax.swing.JTextField txtSalesAddress;
    private javax.swing.JTextField txtSalesCartGrandTotal;
    private javax.swing.JTextField txtSalesCashRecieved;
    private javax.swing.JTextField txtSalesCaustomerMobile;
    private javax.swing.JTextField txtSalesCaustomerName;
    private javax.swing.JTextField txtSalesDue;
    private javax.swing.JLabel txtSalesPruductName;
    private javax.swing.JLabel txtSalesPruductName1;
    private javax.swing.JLabel txtSalesPruductName2;
    private javax.swing.JTextField txtSalesQuantity;
    private javax.swing.JTextField txtSalesSID;
    private javax.swing.JTextField txtSalesSaller;
    private javax.swing.JTextField txtSalesTotalPrice;
    private javax.swing.JTextField txtSalesUnitPrice;
    private javax.swing.JLabel txtSalesUnitPrice1;
    private javax.swing.JLabel txtSalesUnitPrice3;
    private javax.swing.JLabel txtSalesUnitPrice5;
    private javax.swing.JLabel txtSalesUnitPrice6;
    private javax.swing.JTextField txtSalseDiscount;
    private com.toedter.calendar.JDateChooser txtcustomerSalesDate;
    // End of variables declaration//GEN-END:variables
}


/*
 btnHome.setBackground(new Color(255, 255, 255));
  btnHome.setBackground(new Color(153, 255, 204));
*/