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
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/agiles", "root", "");
            sentencia = con.createStatement();

            //System.out.println("Conexion Exitosa");
        } catch (Exception ex) {
            //System.out.println(e);
            JOptionPane.showMessageDialog(null, ex);
        }
        return con;

    }

}
