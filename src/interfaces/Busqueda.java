/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
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
    int fila1;
    static String usuario;
    static String cedula;
    String ide_con;
    int xMouse, yMouse;

    public Busqueda() {
        initComponents();
        setLocationRelativeTo(this);
        this.jlblUsuario.setText(usuario);
        bloquearBusqueda();
        cargarSpinner();
        cargarTabla(cedula);
        desbloquearCalificar();
        jpnlCerrar.setOpaque(false);
        jtbl1.setOpaque(false);
        jpnlCabecera.setOpaque(false);
        //((DefaultTableCellRenderer) jtbl1.getDefaultRenderer(Object.class)).setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);
    }

    public boolean desbloquearCalificar() {
        jtbl1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (jtbl1.getSelectedRow() != -1) {
                    fila1 = jtbl1.getSelectedRow();
                    if (jtbl1.getValueAt(fila1, 1).toString().equals("Finalizado")) {
                        jpnlCalificar.setEnabled(true);
                        jlblCalificar.setEnabled(true);
                        //System.out.println(jtbl1.getValueAt(fila1, 0).toString());
                        ide_con = jtbl1.getValueAt(fila1, 0).toString();
//                        System.out.println(jtbl1.getValueAt(fila1, 1).toString());
                    }
                    if (jtbl1.getValueAt(fila1, 1).toString().equals("Iniciado")) {
                        jpnlCalificar.setEnabled(false);
                        jlblCalificar.setEnabled(false);
                    }
                    if (jtbl1.getValueAt(fila1, 1).toString().equals("Pendiente")) {
                        jpnlCalificar.setEnabled(false);
                        jlblCalificar.setEnabled(false);
                    }

                }
            }
        });
        return true;
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
                //System.out.println("Paso");
                modeloProfesional.addElement(habilidad);
            }
            jcbxProfesional.setModel(modeloProfesional);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Busqueda.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public boolean calificarServicio() {

        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();

            //System.out.println(jSpinner1.getValue().toString());
            String sql = "update registro_contrato set CAL_CON='" + jSpinner1.getValue().toString() + "' where IDE_CON='" + ide_con + "'";
            PreparedStatement psd = cn.prepareStatement(sql);

            int n = psd.executeUpdate();

            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Calificación registrada");
                return true;
            }

            return false;
        } catch (SQLException ex) {
            Logger.getLogger(Busqueda.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public boolean cargarTabla(String cedula) {
        String[] titulos = {"N° Contrato", "Estado", "Cedula Trabajador"};
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

            // Falta poner la cedula del usuario
            sqlSelect = "SELECT * FROM registro_contrato WHERE CED_USU_CON='" + cedula + "' AND CAL_CON=0";
            Statement psd = cc.createStatement();
            ResultSet rs = psd.executeQuery(sqlSelect);
            while (rs.next()) {
                registro[0] = rs.getString("IDE_CON");
                registro[1] = rs.getString("EST_CON");
                registro[2] = rs.getString("CED_TRA_CON");
//                registro[3] = String.valueOf(rs);
//                registro[4] = rs.getString("CAL_TRA");
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
            //System.out.println("entro cargar busqueda");
            Conexion cn = new Conexion();
            Connection cc = cn.conectar();

            String[] titulos = {"Cedula", "Nombre", "Telefono", "# Servicios", "Calificación"};
            modeloT = new DefaultTableModel(null, titulos);
            jtbl1.setModel(modeloT);
            for (int i = 0; i < titulos.length; i++) {
                jtbl1.getColumnModel().getColumn(i).setResizable(false);
            }
            //System.out.println(modeloT.getColumnCount());
            String[] registros = new String[modeloT.getColumnCount()];

            String sqlSelect;
            sqlSelect = "SELECT * FROM trabajador WHERE CED_TRA IN (SELECT CED_TRA_PER FROM habilidades WHERE HAB_TRA = '" + habilidad + "') AND NAC_TRA = (SELECT IDE_CIU FROM ciudad WHERE NOM_CIU = '" + ciudad + "')";

            Statement psd = cc.createStatement();
            ResultSet rs = psd.executeQuery(sqlSelect);
            while (rs.next()) {
                registros[0] = rs.getString("CED_TRA");
                registros[1] = rs.getString("NOM_TRA");
                registros[2] = rs.getString("TEL1_TRA");
                registros[3] = rs.getString("CAN_TRA");
                registros[4] = rs.getString("CAL_TRA");
                modeloT.addRow(registros);
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
            //System.out.println(ciudades.size());
            for (String i : ciudades) {
                modeloCiudad.addElement(i);

            }
            //ciudad.addElement(ciudades);

            jcbxCiudad.setModel(modeloCiudad);

        } catch (Exception ex) {
            Logger.getLogger(Busqueda.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void nuevaTabla() {
        int fila = jtbl1.getSelectedRow();
        jlblFila.setText(String.valueOf(fila));
        jlblFila.setVisible(false);
        if (fila >= 0) {
            Resumen s;
            Resumen.cedula = cedula;
            s = new Resumen();
            s.setVisible(true);

            String[] info = new String[5];

            info[0] = jtbl1.getValueAt(fila, 0).toString();
            info[1] = jtbl1.getValueAt(fila, 1).toString();

            Resumen.jlblDato.setText(info[1].toString().toUpperCase());
            //resumen.modelo.addRow(info);          
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
        //Buscar
        jpnlBuscar.setEnabled(false);
        jlblBuscar.setEnabled(false);
        //Cancelar
        jpnlCancelar.setEnabled(false);
        jlblCancelar.setEnabled(false);
        //Nuevo
        jpnlNuevo.setEnabled(true);
        jlblNuevo.setEnabled(true);
        //Resumen
        jpnlResumen.setEnabled(false);
        jlblResumen.setEnabled(false);
        //Calificar
        jpnlCalificar.setEnabled(false);
        jlblCalificar.setEnabled(false);
        return true;
    }

    public boolean desbloquearBusqueda() {
        jcbxProfesional.setEnabled(true);
        jcbxCiudad.setEnabled(true);
        //Buscar
        jpnlBuscar.setEnabled(true);
        jlblBuscar.setEnabled(true);
        //Cancelar
        jpnlCancelar.setEnabled(true);
        jlblCancelar.setEnabled(true);
        //Nuevo
        jpnlNuevo.setEnabled(false);
        jlblNuevo.setEnabled(false);
        //Calificar
        jpnlCalificar.setEnabled(true);
        jlblCalificar.setEnabled(true);
        return true;
    }

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("imagenes/trab.jpg"));
        return retValue;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jpnlBackGround = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbl1 = new javax.swing.JTable();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jcbxProfesional = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jcbxCiudad = new javax.swing.JComboBox<>();
        jpnlBuscar = new javax.swing.JPanel();
        jlblBuscar = new javax.swing.JLabel();
        jpnlCalificar = new javax.swing.JPanel();
        jlblCalificar = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jpnlNuevo = new javax.swing.JPanel();
        jlblNuevo = new javax.swing.JLabel();
        jpnlResumen = new javax.swing.JPanel();
        jlblResumen = new javax.swing.JLabel();
        jpnlCancelar = new javax.swing.JPanel();
        jlblCancelar = new javax.swing.JLabel();
        jpnlCerrar = new javax.swing.JPanel();
        jlblCerrar = new javax.swing.JLabel();
        jpnlCabecera = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jlblFila = new javax.swing.JLabel();
        jlblUsuario = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());
        setLocationByPlatform(true);
        setUndecorated(true);
        setResizable(false);

        jpnlBackGround.setBackground(new java.awt.Color(255, 255, 255));
        jpnlBackGround.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setBorder(null);
        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));

        jtbl1.setBackground(new java.awt.Color(204, 204, 204));
        jtbl1.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jtbl1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbl1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtbl1);

        jpnlBackGround.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 620, 169));

        jSpinner1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jpnlBackGround.add(jSpinner1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 280, 47, 30));

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel1.setText("PROFESIONAL:");
        jpnlBackGround.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, 20));
        jpnlBackGround.add(jcbxProfesional, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 140, -1));

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel2.setText("CIUDAD:");
        jpnlBackGround.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 60, -1, 20));
        jpnlBackGround.add(jcbxCiudad, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, 130, -1));

        jpnlBuscar.setBackground(new java.awt.Color(3, 40, 24));
        jpnlBuscar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnlBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnlBuscarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jpnlBuscarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jpnlBuscarMouseExited(evt);
            }
        });

        jlblBuscar.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jlblBuscar.setForeground(new java.awt.Color(255, 255, 255));
        jlblBuscar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblBuscar.setText("BUSCAR");

        javax.swing.GroupLayout jpnlBuscarLayout = new javax.swing.GroupLayout(jpnlBuscar);
        jpnlBuscar.setLayout(jpnlBuscarLayout);
        jpnlBuscarLayout.setHorizontalGroup(
            jpnlBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlblBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
        );
        jpnlBuscarLayout.setVerticalGroup(
            jpnlBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlblBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jpnlBackGround.add(jpnlBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 60, 80, 30));

        jpnlCalificar.setBackground(new java.awt.Color(3, 40, 24));
        jpnlCalificar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnlCalificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnlCalificarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jpnlCalificarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jpnlCalificarMouseExited(evt);
            }
        });

        jlblCalificar.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jlblCalificar.setForeground(new java.awt.Color(255, 255, 255));
        jlblCalificar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblCalificar.setText("CALIFICAR");

        javax.swing.GroupLayout jpnlCalificarLayout = new javax.swing.GroupLayout(jpnlCalificar);
        jpnlCalificar.setLayout(jpnlCalificarLayout);
        jpnlCalificarLayout.setHorizontalGroup(
            jpnlCalificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlblCalificar, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
        );
        jpnlCalificarLayout.setVerticalGroup(
            jpnlCalificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlblCalificar, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
        );

        jpnlBackGround.add(jpnlCalificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 280, 80, -1));

        jPanel1.setBackground(new java.awt.Color(20, 120, 50));

        jpnlNuevo.setBackground(new java.awt.Color(3, 40, 24));
        jpnlNuevo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnlNuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnlNuevoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jpnlNuevoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jpnlNuevoMouseExited(evt);
            }
        });

        jlblNuevo.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jlblNuevo.setForeground(new java.awt.Color(255, 255, 255));
        jlblNuevo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblNuevo.setText("NUEVO CONTRATO");

        javax.swing.GroupLayout jpnlNuevoLayout = new javax.swing.GroupLayout(jpnlNuevo);
        jpnlNuevo.setLayout(jpnlNuevoLayout);
        jpnlNuevoLayout.setHorizontalGroup(
            jpnlNuevoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlblNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
        );
        jpnlNuevoLayout.setVerticalGroup(
            jpnlNuevoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlblNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jpnlResumen.setBackground(new java.awt.Color(3, 40, 24));
        jpnlResumen.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnlResumen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnlResumenMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jpnlResumenMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jpnlResumenMouseExited(evt);
            }
        });

        jlblResumen.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jlblResumen.setForeground(new java.awt.Color(255, 255, 255));
        jlblResumen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblResumen.setText("RESUMEN");

        javax.swing.GroupLayout jpnlResumenLayout = new javax.swing.GroupLayout(jpnlResumen);
        jpnlResumen.setLayout(jpnlResumenLayout);
        jpnlResumenLayout.setHorizontalGroup(
            jpnlResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlblResumen, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
        );
        jpnlResumenLayout.setVerticalGroup(
            jpnlResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlResumenLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jlblResumen, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jpnlCancelar.setBackground(new java.awt.Color(3, 40, 24));
        jpnlCancelar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnlCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnlCancelarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jpnlCancelarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jpnlCancelarMouseExited(evt);
            }
        });

        jlblCancelar.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jlblCancelar.setForeground(new java.awt.Color(255, 255, 255));
        jlblCancelar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblCancelar.setText("CANCELAR");

        javax.swing.GroupLayout jpnlCancelarLayout = new javax.swing.GroupLayout(jpnlCancelar);
        jpnlCancelar.setLayout(jpnlCancelarLayout);
        jpnlCancelarLayout.setHorizontalGroup(
            jpnlCancelarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlblCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
        );
        jpnlCancelarLayout.setVerticalGroup(
            jpnlCancelarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlblCancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpnlNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 332, Short.MAX_VALUE)
                .addComponent(jpnlResumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnlCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jpnlResumen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnlCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnlNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnlBackGround.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 317, 640, -1));

        jpnlCerrar.setBackground(new java.awt.Color(255, 255, 255));

        jlblCerrar.setBackground(new java.awt.Color(255, 255, 255));
        jlblCerrar.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jlblCerrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblCerrar.setText("X");
        jlblCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlblCerrarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jlblCerrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jlblCerrarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jpnlCerrarLayout = new javax.swing.GroupLayout(jpnlCerrar);
        jpnlCerrar.setLayout(jpnlCerrarLayout);
        jpnlCerrarLayout.setHorizontalGroup(
            jpnlCerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlblCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jpnlCerrarLayout.setVerticalGroup(
            jpnlCerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlblCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jpnlBackGround.add(jpnlCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 0, 60, 40));

        jpnlCabecera.setBackground(new java.awt.Color(255, 255, 255));
        jpnlCabecera.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jpnlCabeceraMouseDragged(evt);
            }
        });
        jpnlCabecera.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jpnlCabeceraMousePressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel3.setText("BIENVENIDO: ");

        jlblUsuario.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N

        javax.swing.GroupLayout jpnlCabeceraLayout = new javax.swing.GroupLayout(jpnlCabecera);
        jpnlCabecera.setLayout(jpnlCabeceraLayout);
        jpnlCabeceraLayout.setHorizontalGroup(
            jpnlCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnlCabeceraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(147, 147, 147)
                .addComponent(jlblFila, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(146, Short.MAX_VALUE))
        );
        jpnlCabeceraLayout.setVerticalGroup(
            jpnlCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlCabeceraLayout.createSequentialGroup()
                .addGap(0, 9, Short.MAX_VALUE)
                .addGroup(jpnlCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jlblFila, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
        );

        jpnlBackGround.add(jpnlCabecera, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 640, 40));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/5.jpeg"))); // NOI18N
        jpnlBackGround.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 640, 320));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnlBackGround, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnlBackGround, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtbl1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbl1MouseClicked
        // TODO add your handling code here:
//        jbtnCalificar.setEnabled(true);

    }//GEN-LAST:event_jtbl1MouseClicked

    private void jpnlBuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlBuscarMouseClicked
        if (jpnlBuscar.isEnabled() && jlblBuscar.isEnabled()) {
            jpnlResumen.setEnabled(true);
            jlblResumen.setEnabled(true);
            cargarBusqueda(this.modeloProfesional.getElementAt(this.jcbxProfesional.getSelectedIndex()).toString(), this.modeloCiudad.getElementAt(this.jcbxCiudad.getSelectedIndex()).toString());
        }
    }//GEN-LAST:event_jpnlBuscarMouseClicked

    private void jpnlBuscarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlBuscarMouseEntered
        if (jpnlBuscar.isEnabled()) {
            jpnlBuscar.setBackground(new Color(3, 80, 24));
        }
    }//GEN-LAST:event_jpnlBuscarMouseEntered

    private void jpnlBuscarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlBuscarMouseExited
        if (jpnlBuscar.isEnabled()) {
            jpnlBuscar.setBackground(new Color(3, 40, 24));
        }
    }//GEN-LAST:event_jpnlBuscarMouseExited

    private void jpnlCancelarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlCancelarMouseClicked
        if (jpnlCancelar.isEnabled() && jlblCancelar.isEnabled()) {
            bloquearBusqueda();
            cargarTabla(cedula);
        }
    }//GEN-LAST:event_jpnlCancelarMouseClicked

    private void jpnlCancelarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlCancelarMouseEntered
        if (jpnlCancelar.isEnabled()) {
            jpnlCancelar.setBackground(new Color(3, 80, 24));
        }
    }//GEN-LAST:event_jpnlCancelarMouseEntered

    private void jpnlCancelarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlCancelarMouseExited
        if (jpnlCancelar.isEnabled()) {
            jpnlCancelar.setBackground(new Color(3, 40, 24));
        } else {
            jpnlCancelar.setBackground(new Color(3, 40, 24));
        }
    }//GEN-LAST:event_jpnlCancelarMouseExited

    private void jpnlResumenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlResumenMouseClicked
        if (jpnlResumen.isEnabled() && jlblResumen.isEnabled()) {
            nuevaTabla();
        }
    }//GEN-LAST:event_jpnlResumenMouseClicked

    private void jpnlResumenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlResumenMouseEntered
        if (jpnlResumen.isEnabled()) {
            jpnlResumen.setBackground(new Color(3, 80, 24));
        }
    }//GEN-LAST:event_jpnlResumenMouseEntered

    private void jpnlResumenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlResumenMouseExited
        if (jpnlResumen.isEnabled()) {
            jpnlResumen.setBackground(new Color(3, 40, 24));
        }
    }//GEN-LAST:event_jpnlResumenMouseExited

    private void jpnlCalificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlCalificarMouseClicked
        if (jpnlCalificar.isEnabled() && jlblCalificar.isEnabled()) {
            calificarServicio();
            cargarTabla(cedula);
        }
    }//GEN-LAST:event_jpnlCalificarMouseClicked

    private void jpnlCalificarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlCalificarMouseEntered
        if (jpnlCalificar.isEnabled()) {
            jpnlCalificar.setBackground(new Color(3, 80, 24));
        }
    }//GEN-LAST:event_jpnlCalificarMouseEntered

    private void jpnlCalificarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlCalificarMouseExited
        if (jpnlCalificar.isEnabled()) {
            jpnlCalificar.setBackground(new Color(3, 40, 24));
        }
    }//GEN-LAST:event_jpnlCalificarMouseExited

    private void jpnlNuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlNuevoMouseClicked
        if (jpnlNuevo.isEnabled() && jlblNuevo.isEnabled()) {
            desbloquearBusqueda();
            cargarProfesional();
            CargarCuidad();
        }
    }//GEN-LAST:event_jpnlNuevoMouseClicked

    private void jpnlNuevoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlNuevoMouseEntered
        if (jpnlNuevo.isEnabled()) {
            jpnlNuevo.setBackground(new Color(3, 80, 24));
        }
    }//GEN-LAST:event_jpnlNuevoMouseEntered

    private void jpnlNuevoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlNuevoMouseExited
        if (jpnlNuevo.isEnabled()) {
            jpnlNuevo.setBackground(new Color(3, 40, 24));
        } else {
            jpnlNuevo.setBackground(new Color(3, 40, 24));
        }
    }//GEN-LAST:event_jpnlNuevoMouseExited

    private void jpnlCabeceraMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlCabeceraMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_jpnlCabeceraMouseDragged

    private void jpnlCabeceraMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnlCabeceraMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_jpnlCabeceraMousePressed

    private void jlblCerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlblCerrarMouseClicked
        Login l = new Login();
        l.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jlblCerrarMouseClicked

    private void jlblCerrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlblCerrarMouseEntered
        jpnlCerrar.setOpaque(true);
        jpnlCerrar.setBackground(Color.red);
        jlblCerrar.setForeground(Color.white);
    }//GEN-LAST:event_jlblCerrarMouseEntered

    private void jlblCerrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlblCerrarMouseExited
        jpnlCerrar.setOpaque(false);
        jlblCerrar.setForeground(Color.black);
    }//GEN-LAST:event_jlblCerrarMouseExited

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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JComboBox<String> jcbxCiudad;
    private javax.swing.JComboBox<String> jcbxProfesional;
    private javax.swing.JLabel jlblBuscar;
    private javax.swing.JLabel jlblCalificar;
    private javax.swing.JLabel jlblCancelar;
    private javax.swing.JLabel jlblCerrar;
    public static javax.swing.JLabel jlblFila;
    private javax.swing.JLabel jlblNuevo;
    private javax.swing.JLabel jlblResumen;
    private javax.swing.JLabel jlblUsuario;
    private javax.swing.JPanel jpnlBackGround;
    private javax.swing.JPanel jpnlBuscar;
    private javax.swing.JPanel jpnlCabecera;
    private javax.swing.JPanel jpnlCalificar;
    private javax.swing.JPanel jpnlCancelar;
    private javax.swing.JPanel jpnlCerrar;
    private javax.swing.JPanel jpnlNuevo;
    private javax.swing.JPanel jpnlResumen;
    public static javax.swing.JTable jtbl1;
    // End of variables declaration//GEN-END:variables
}
