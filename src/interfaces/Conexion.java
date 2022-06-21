/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class Conexion {

    Connection con;
    public ResultSet resultado;
    public Statement sentencia;

    public Connection conectar() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/agiles", "root", "");
//            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/agiles", "root", "");
            sentencia = con.createStatement();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return con;

    }

}
