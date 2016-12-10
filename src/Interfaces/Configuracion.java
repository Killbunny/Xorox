/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Clases.Alumno;
import Clases.Conexion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Hp Pavilion g4
 */
public class Configuracion extends javax.swing.JFrame {

    /**
     * Creates new form Configuracion
     */
    Connection conn;
    Conexion a1 = new Conexion();

    public Configuracion() {
        initComponents();
        //Desactivar jcombobox
        jcb_Maestro.setEnabled(false);
        jcb_razones.setEnabled(false);
        jcb_Hora_Clase.setEnabled(false);
        btnEntrar.setEnabled(false);
        //Cargar Informacion a los jcombobox
        String d = Alumno.Alumno;
        Consultar_Aulas();
        
        //Agrega la informacion al combobox según el index del combobox actividad
        jcb_Actividad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Si jcb_actividad=x
                //Activa/desactiva Y comboboxes
                if (jcb_Actividad.getSelectedIndex() == 1) {
                    jcb_Maestro.setEnabled(true);
                    jcb_Hora_Clase.setEnabled(true);
                    jcb_razones.setEnabled(false);
                    Limpiar_Maestros();
                    Consultar_Maestros();

                }
                if (jcb_Actividad.getSelectedIndex() == 0) {
                    jcb_Maestro.setEnabled(false);
                    jcb_Hora_Clase.setEnabled(false);
                    jcb_razones.setEnabled(false);
                    Limpiar_Maestros();
                    Limpiar_Aulas_Y_Materias();
                }
                if (jcb_Actividad.getSelectedIndex() == 2) {

                    jcb_Maestro.setEnabled(false);
                    jcb_Hora_Clase.setEnabled(false);
                    Limpiar_Maestros();
                    Limpiar_Aulas_Y_Materias();
                    Alumno.Comentarios = jcb_Actividad.getSelectedItem().toString();
                    btnEntrar.setEnabled(true);
                }
                if (jcb_Actividad.getSelectedIndex() == 3) {

                    jcb_Maestro.setEnabled(false);
                    jcb_Hora_Clase.setEnabled(false);
                    Limpiar_Maestros();
                    Limpiar_Aulas_Y_Materias();
                    Alumno.Comentarios = jcb_Actividad.getSelectedItem().toString();
                    btnEntrar.setEnabled(true);
                }

                if (jcb_Actividad.getSelectedIndex() == 4) {
                    jcb_razones.setEnabled(true);
                    jcb_Maestro.setEnabled(false);
                    jcb_Hora_Clase.setEnabled(false);
                    Limpiar_Maestros();
                    Limpiar_Aulas_Y_Materias();
//                  btnEntrar.setEnabled(true);
                }
            }
        });

        jcb_razones.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jcb_razones.getSelectedIndex() == 0) {
                    btnEntrar.setEnabled(false);
                } else {
                    btnEntrar.setEnabled(true);
                    Alumno.Comentarios = jcb_razones.getSelectedItem().toString();
                }
            }
        });

//        String Mensaje_Del_Dia=Obtener_Mensaje_del_Dia();
//        JOptionPane.showMessageDialog(null, Mensaje_Del_Dia);
        jcbAula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jcbAula.getSelectedIndex() == 0) {
                    btnEntrar.setEnabled(false);
                } else {
                    String Aula = jcbAula.getSelectedItem().toString();
                    Alumno.Aula = Aula;
                }
            }
        });

        jcb_Maestro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jcb_Maestro.getSelectedIndex() == 0) {
                    btnEntrar.setEnabled(false);

                } else if (jcb_Maestro.getItemCount() > 0) {
                    btnEntrar.setEnabled(true);
                    String Maestro = jcb_Maestro.getSelectedItem().toString();
                    Alumno.Maestro = Maestro;
                    Limpiar_Aulas_Y_Materias();
                    Consultar_Horarios_Y_Materias(Maestro);
                }
            }
        });

        jcb_razones.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jcb_razones.getSelectedIndex() > 0) {
                    btnEntrar.setEnabled(true);
                }
            }
        });

        jcb_Hora_Clase.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jcb_Hora_Clase.getSelectedIndex() > 0) {
                    String Horario_y_Materia = jcb_Hora_Clase.getSelectedItem().toString();
                    Alumno.Horario_Y_Materia = Horario_y_Materia;
                }
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jcbAula = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jcb_Actividad = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jcb_Maestro = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnEntrar = new javax.swing.JButton();
        jcb_razones = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jcb_Hora_Clase = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jcbAula.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione" }));
        jPanel1.add(jcbAula, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 90, 230, -1));

        jLabel1.setText("¿En que Aula estas?");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        jcb_Actividad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "Clase", "Trabajo", "Investigacion", "Otros" }));
        jPanel1.add(jcb_Actividad, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 230, -1));

        jLabel2.setText("¿Que Actividad estas realizando?");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        jcb_Maestro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione" }));
        jPanel1.add(jcb_Maestro, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 180, 230, -1));

        jLabel3.setText("¿Con que maestro estas?");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));

        jLabel4.setFont(new java.awt.Font("Myanmar Text", 1, 18)); // NOI18N
        jLabel4.setText("Xorox version 1.0.0");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, -1, -1));

        btnEntrar.setText("Entrar");
        btnEntrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntrarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEntrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 260, 90, -1));

        jcb_razones.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "Diversion", "Redes Sociales", "Correo Electronico" }));
        jPanel1.add(jcb_razones, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 150, 230, -1));

        jLabel5.setText("Razon");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, -1, -1));

        jcb_Hora_Clase.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione" }));
        jPanel1.add(jcb_Hora_Clase, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 210, 230, -1));

        jLabel6.setText("Selecciona tu hora de Clase");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

        jButton1.setText("Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 260, 70, -1));

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void Control_de_Entrada() {

    }

    public void Limpiar_Maestros() {
        jcb_Maestro.removeAllItems();;
        jcb_Maestro.addItem("Seleccione");
    }

    public void Limpiar_Aulas_Y_Materias() {
        jcb_Hora_Clase.removeAllItems();;
        jcb_Hora_Clase.addItem("Seleccione");
    }

    public void Consultar_Aulas() {
        try {
            conn = a1.getConnection();
            String consultar = "SELECT * FROM xorox.aulas;";
            Conexion miconexion = new Conexion();
            Connection conn = miconexion.getConnection();
            Statement pst2 = conn.createStatement();
            ResultSet rs = pst2.executeQuery(consultar);
            Object datos[] = new Object[2];
            while (rs.next()) {
                datos[0] = rs.getString("Aula");
                jcbAula.addItem("" + datos[0]);
            }
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void Consultar_Maestros() {
        try {
            conn = a1.getConnection();
            String consultar = "SELECT * FROM xorox.maestros;";
            Conexion miconexion = new Conexion();
            Connection conn = miconexion.getConnection();
            Statement pst2 = conn.createStatement();
            ResultSet rs = pst2.executeQuery(consultar);
            Object datos[] = new Object[2];
            while (rs.next()) {
                datos[0] = rs.getString("Maestro");
                jcb_Maestro.addItem("" + datos[0]);
            }
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void Consultar_Horarios_Y_Materias(String Maestro) {
        try {
            conn = a1.getConnection();
            String consultar = "SELECT Dia_Hora_Uso FROM xorox.horario_separado where Maestro='" + Maestro + "';";
            Conexion miconexion = new Conexion();
            Connection conn = miconexion.getConnection();
            Statement pst2 = conn.createStatement();
            ResultSet rs = pst2.executeQuery(consultar);
            Object datos[] = new Object[2];
            while (rs.next()) {
                datos[0] = rs.getString("Dia_Hora_Uso");
                jcb_Hora_Clase.addItem("" + datos[0]);
            }
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void btnEntrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrarActionPerformed


        // TODO add your handling code here:
        dispose();
        Alumno.Aula = jcbAula.getSelectedItem().toString();
        Alumno.Materia = "Prueba";
        Info_Computo a1 = new Info_Computo();
        a1.setVisible(true);
    }//GEN-LAST:event_btnEntrarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    public String Obtener_Mensaje_del_Dia() {
        String mensaje = "";
        return mensaje;
    }

    public void Obtener_Aulas() {

    }

    public void Obtener_Maestros() {

    }

    public void Obtener_Hora_Clase() {

    }

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
            java.util.logging.Logger.getLogger(Configuracion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Configuracion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Configuracion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Configuracion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Configuracion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntrar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> jcbAula;
    private javax.swing.JComboBox<String> jcb_Actividad;
    private javax.swing.JComboBox<String> jcb_Hora_Clase;
    private javax.swing.JComboBox<String> jcb_Maestro;
    private javax.swing.JComboBox<String> jcb_razones;
    // End of variables declaration//GEN-END:variables
}
