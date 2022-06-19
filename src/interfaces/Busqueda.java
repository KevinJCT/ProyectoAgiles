/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerListModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author byron
 */
public class Busqueda extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    DefaultTableModel modeloT;
    DefaultComboBoxModel modeloProfesional;
    DefaultComboBoxModel modeloCiudad;
    SpinnerListModel modeloSpinnerCantidad;

    public Busqueda() {
        initComponents();
        bloquearBusqueda();
        cargarSpinner();

    }

    public boolean cargarProfesional() {

        try {
            ArrayList<String> habilidades = new ArrayList<>();
            modeloProfesional = new DefaultComboBoxModel();
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();

            String sql = "Select HAB_TRA from habilidades";
            Statement psd = cn.createStatement();

            ResultSet rs = psd.executeQuery(sql);

            while (rs.next()) {
                habilidades.add(rs.getString("HAB_TRA"));
            }

            Set<String> hashSet = new HashSet<String>(habilidades);
            habilidades.clear();
            habilidades.addAll(hashSet);
//            System.out.println(habilidades.size());
            for (String habilidad : habilidades) {
                System.out.println("Paso");
                modeloProfesional.addElement(habilidad);
            }
            jcbxProfesional.setModel(modeloProfesional);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Busqueda.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public boolean cargarTabla() {
        String[] titulos = {"Nombre", "Telefono", "# Servicios", "Calificación"};
        modeloT = new DefaultTableModel(null, titulos);
        jtbl1.setModel(modeloT);
        for (int i = 0; i < titulos.length; i++) {
            jtbl1.getColumnModel().getColumn(i).setResizable(false);
        }

        try {

            Conexion cn = new Conexion();
            Connection cc = cn.conectar();

            String[] registro = new String[modeloT.getColumnCount()];

            String sqlSelect;
            sqlSelect = "SELECT * FROM trabajador";
            Statement psd = cc.createStatement();
            ResultSet rs = psd.executeQuery(sqlSelect);
            while (rs.next()) {
                registro[0] = rs.getString("NOM_TRA");
                registro[1] = rs.getString("TEL1_TRA");
                registro[2] = rs.getString("CAN_TRA");
                registro[3] = rs.getString("CAL_TRA");
                modeloT.addRow(registro);
            }
            jtbl1.setModel(modeloT);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return true;
    }
    

    public void cargarBusqueda(String habilidad, String ciudad) {
        try {

            Conexion cn = new Conexion();
            Connection cc = cn.conectar();

            String[] registro = new String[modeloT.getColumnCount()];
            for(int i =0;i<modeloT.getRowCount();i++){
                modeloT.removeRow(i);
            }
            

            String sqlSelect;
            sqlSelect = "SELECT * FROM trabajador WHERE CED_TRA IN (SELECT CED_TRA_PER FROM habilidades WHERE HAB_TRA = '" + habilidad + "') AND NAC_TRA = (SELECT IDE_CIU FROM ciudad WHERE NOM_CIU = '" + ciudad + "')";
            Statement psd = cc.createStatement();
            ResultSet rs = psd.executeQuery(sqlSelect);
            while (rs.next()) {
                registro[0] = rs.getString("NOM_TRA");
                registro[1] = rs.getString("TEL1_TRA");
                registro[2] = rs.getString("CAN_TRA");
                registro[3] = rs.getString("CAL_TRA");
                modeloT.addRow(registro);
            }
            jtbl1.setModel(modeloT);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void CargarCuidad() {
        try {

            ArrayList<String> ciudades = new ArrayList<>();
            modeloCiudad = new DefaultComboBoxModel();
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            String sql = "";
            sql = "select NOM_CIU from ciudad";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                ciudades.add(rs.getString("NOM_CIU"));
            }
            System.out.println(ciudades.size());
            for (String i : ciudades) {
                modeloCiudad.addElement(i);

            }
            //ciudad.addElement(ciudades);

            jcbxCiudad.setModel(modeloCiudad);

        } catch (Exception ex) {
            Logger.getLogger(Busqueda.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
     private void cargarSpinner() {
        String[] cantidad = new String[10];
        Integer x = 1;
        for (int i = 0; i < 10; i++) {
            cantidad[i] = x.toString();
            x += 1;
        }
        modeloSpinnerCantidad = new SpinnerListModel(cantidad);
        jSpinner1.setModel(modeloSpinnerCantidad);
    }

    public boolean bloquearBusqueda() {
        jcbxProfesional.setEnabled(false);
        jcbxCiudad.setEnabled(false);
        jbtnBuscar.setEnabled(false);
        jbtnCancelar.setEnabled(false);
        jbtnNuevo.setEnabled(true);
        jbtnResumen.setEnabled(false);
        jbtnCalificar.setEnabled(false);
        return true;
    }

    public boolean desbloquearBusqueda() {
        jcbxProfesional.setEnabled(true);
        jcbxCiudad.setEnabled(true);
        jbtnBuscar.setEnabled(true);
        jbtnCancelar.setEnabled(true);
        jbtnNuevo.setEnabled(false);
        jbtnResumen.setEnabled(true);
        jbtnCalificar.setEnabled(false);
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jcbxProfesional = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jcbxCiudad = new javax.swing.JComboBox<>();
        jbtnBuscar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jbtnResumen = new javax.swing.JButton();
        jbtnNuevo = new javax.swing.JButton();
        jbtnCancelar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbl1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jlbUsuario = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jbtnCalificar = new javax.swing.JButton();
        jSpinner1 = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Profesional");

        jLabel2.setText("Ciudad");

        jbtnBuscar.setText("Buscar");
        jbtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbxProfesional, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbxCiudad, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(24, 24, 24)
                .addComponent(jbtnBuscar)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jcbxProfesional, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbxCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnBuscar)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jbtnResumen.setText("Resumen");

        jbtnNuevo.setText("Nuevo Contrato");
        jbtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNuevoActionPerformed(evt);
            }
        });

        jbtnCancelar.setText("Cancelar");
        jbtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtnResumen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnCancelar)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnResumen)
                    .addComponent(jbtnNuevo)
                    .addComponent(jbtnCancelar))
                .addGap(36, 36, 36))
        );

        jtbl1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbl1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtbl1);

        jLabel3.setText("USUARIO: ");

        jbtnCalificar.setText("Calificar");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbtnCalificar)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnCalificar)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlbUsuario)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jlbUsuario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNuevoActionPerformed
        // TODO add your handling code here:
        desbloquearBusqueda();
        cargarProfesional();
        CargarCuidad();
        cargarTabla();
    }//GEN-LAST:event_jbtnNuevoActionPerformed

    private void jbtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelarActionPerformed
        // TODO add your handling code here:
        bloquearBusqueda();
    }//GEN-LAST:event_jbtnCancelarActionPerformed

    private void jtbl1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbl1MouseClicked
        // TODO add your handling code here:
        jbtnCalificar.setEnabled(true);
    }//GEN-LAST:event_jtbl1MouseClicked

    private void jbtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBuscarActionPerformed
        // TODO add your handling code here:
        cargarBusqueda(this.modeloProfesional.getElementAt(this.jcbxProfesional.getSelectedIndex()).toString(), this.modeloCiudad.getElementAt(this.jcbxCiudad.getSelectedIndex()).toString());
    }//GEN-LAST:event_jbtnBuscarActionPerformed

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
            java.util.logging.Logger.getLogger(Busqueda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Busqueda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Busqueda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Busqueda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Busqueda().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JButton jbtnBuscar;
    private javax.swing.JButton jbtnCalificar;
    private javax.swing.JButton jbtnCancelar;
    private javax.swing.JButton jbtnNuevo;
    private javax.swing.JButton jbtnResumen;
    private javax.swing.JComboBox<String> jcbxCiudad;
    private javax.swing.JComboBox<String> jcbxProfesional;
    private javax.swing.JLabel jlbUsuario;
    private javax.swing.JTable jtbl1;
    // End of variables declaration//GEN-END:variables
}
