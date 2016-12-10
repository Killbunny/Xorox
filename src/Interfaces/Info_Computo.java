/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Clases.Alumno;
import Clases.Conexion;
import Clases.Cronometro;
import Clases.Estado_Windows;
import Clases.Servidor;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TitledPane;
import javax.swing.JOptionPane;

/**
 *
 * @author Hp Pavilion g4
 */
public class Info_Computo extends javax.swing.JFrame {

    //Atributos
    int x = 0;
    int y = 0;
    public String horaactual = "";
    String horaTermino;
    Interfaces.Info_Computo a1;
    Conexion b1 = new Conexion();
//      String horaTermino="";
    //Hilos
    public static Cronometro cron;
    //Socket que recibe el mensaje del administrador para despues proyectarlo en la ventana
    Servidor ser;
    Estado_Windows estado;

    //Clase inet address
    InetAddress ip;
    String nombre = "";
    StringBuilder sb = new StringBuilder();
    boolean fecha_es_correcta = false;  //estado para saber si la fecha actual
    //esta dentro del rango del ciclo escolar actual.

    public Info_Computo() {
        initComponents();
        //Fecha Actual del Equipo
        Calendar fecha = Calendar.getInstance();
        int dia = fecha.get(fecha.DAY_OF_MONTH);
        int año = fecha.get(fecha.YEAR);
        int mes = fecha.get(fecha.MONTH);
        String fecha_Actual = "";
        String mes1 = formato_mes(mes);
        if (dia <= 9) {
            fecha_Actual = "0" + dia + "/" + mes1 + "/" + año;
        } //Se le da formato al numero del mes    
        else {
            fecha_Actual = dia + "/" + mes1 + "/" + año;
        }
        //comprobaremos si la fecha actual esta  dentro del rango del ciclo escolar
        fecha_es_correcta = Comprobar_Fecha_del_Ciclo_E(fecha_Actual);
        if (!fecha_es_correcta) {
            JOptionPane.showMessageDialog(null, "Fecha fuera del ciclo escolar actual");
        }
        //Variables donde se guarda la informacion   
        String alumno = Alumno.Alumno;
        String a = Alumno.Comentarios;
        String M = Alumno.Maestro;
        String Aula = Alumno.Aula;
        String mat = Alumno.Horario_Y_Materia;
        //Carga la informacion del alumno a la ventana
        Object das[] = Cargar_Info_Alumno(alumno);
        lblNo_Matricula.setText(das[0].toString());
        int matricula = Integer.parseInt(lblNo_Matricula.getText());
        lblAlumno.setText(das[1].toString());
        lblCarrera.setText(das[2].toString());
        lblAula.setText(Alumno.Aula);
        estado = new Estado_Windows();
        estado.start();
        //Inicio el hilo del Cronometro
        cron = new Cronometro(lblCronometro);
        cron.start();

        //Se necesita usar un try y esas excepciones 
        //para poder usar la clase InetAddress
        try {
            //Se  obtiene la direccion ip de la computadora local   
            ip = InetAddress.getLocalHost();
            //Se crea una interfaz de red apartir de la direccion ip actual
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            //La direccion Mac se descompone en bytes
            byte[] mac = network.getHardwareAddress();
            //Se crea el objeto StringBuilder para traducir los bytes a caracteres

            if (mac != null) {
                //Si no es nula procede a agregar la MAC al stringbuilder
                for (int j = 0; j < mac.length; j++) {

                    //Se van agregando los bytes con el formato especificado en el objeto StringBuilder  
                    sb.append(String.format("%02X%s", mac[j], (j < mac.length - 1) ? "-" : ""));
                }
            } else {
                //Si es nula crea una Exception de tipo NoNetworkException
                throw new Login.NoNetworkException("No hay direccion MAC en el dispositivo");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Login.NoNetworkException ex) {

        }

        Object infoMac[] = Comprobar_MaC(sb.toString());

        if (infoMac[0] != null) {
            lblMAC_ADDRESS.setText(infoMac[0].toString());
            lblUbicacion.setText(infoMac[1].toString());
            lblFecha.setText(fecha_Actual);
        } else {
            JOptionPane.showMessageDialog(null, "infoMac es null");
        }

//      comprobar si existe mensaje del dia en base a la fecha del sistema
        Object Msn_dia[] = Comprobar_Mensaje_del_Dia(fecha_Actual);
        String motd = "" + Msn_dia[1].toString();
        JOptionPane.showMessageDialog(null, "Mensaje del Dia" + motd);
        //En caso que exista mensaje del dia
        int idmensaje_del_dia = Integer.parseInt(Msn_dia[0].toString());

        if (idmensaje_del_dia != 0) {
            //lo muestro

            //comprueba si ya lo he mostrado con anterioridad
            Object msnComprobado[] = Comprobar_si_se_leyo_el_Mensaje_del_Dia(idmensaje_del_dia);
            JOptionPane.showMessageDialog(null, msnComprobado[0].toString());
            //vacia en esta variable el id del msn a leer
            int idmensaje_leido_del_dia = Integer.parseInt(msnComprobado[0].toString());
            //vacia en esta variable el numero de veces que se ha leido el mensaje.
            int cont = Integer.parseInt(msnComprobado[2].toString());
            //Si no entonces registro que lo mostre por primera vez
            if (idmensaje_leido_del_dia == 0) {
                if (cont == 0) {
                    //se muestra por primera vez
                    JOptionPane.showMessageDialog(null, "Mensaje del dia: \n " + Msn_dia[1].toString());

                    Insertar_Lectura_del_mensaje_del_dia(matricula, idmensaje_del_dia);

                }
                if (cont == 1) {
                    //se muestra por segunda vez    
                    JOptionPane.showMessageDialog(null, "Mensaje del dia: \n " + Msn_dia[1].toString());
                    Actualizar(idmensaje_leido_del_dia, idmensaje_del_dia, matricula);
                }
            }

        }
        //si existe
        //comprobar si hay registro del lectura de ese mensaje del dia tomando como referencia el No de Matricula ingresado.
        //si no hay registro se crea uno en base al No de Matricula (operacion insert)
        //si hay, simplemente se incrementa en 1 el valor de la columna cont si es menor que 2(operacion UPDATE) 
    }

    public Object[] Comprobar_Mensaje_del_Dia(String Fecha) {
        Object msninfo[] = new Object[2];
        msninfo[0] = 0;
        msninfo[1] = "No hay Mensaje";
        try//       try
        {
            JOptionPane.showMessageDialog(null, "Fecha:" + Fecha);
            Conexion b1 = new Conexion();
            Connection conn = b1.getConnection();
            String consultar = "SELECT * FROM xorox.mensaje_del_dia where Fecha='" + Fecha + "';";
            Conexion miconexion = new Conexion();
            Statement pst2 = conn.createStatement();
            ResultSet rs = pst2.executeQuery(consultar);
            if (rs.next()) {
                do {
                    msninfo[0] = rs.getInt("idmensaje_del_dia");
                    msninfo[1] = rs.getString("mensaje");
                } while (rs.next());

            } else {
                msninfo[0] = 0;
                msninfo[1] = "No hay Mensaje";
            }
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            msninfo[0] = -1;
            msninfo[1] = "SQL Exception";
        }
        return msninfo;
    }

    public Object[] Comprobar_si_se_leyo_el_Mensaje_del_Dia(int idMensaje_del_dia) {
        Object msn_diario[] = new Object[3];
        msn_diario[0] = 0;
        msn_diario[1] = "Matricula";
        msn_diario[2] = "-1";
        try {
            String consultar = "SELECT a.id_mensaje_diario_leido,a.NO_MATRICULA,b.mensaje,a.cont  "
                    + "FROM mensaje_diario_leido a, mensaje_del_dia b "
                    + "where a.id_mensaje_del_dia=" + idMensaje_del_dia + " and cont<3 group by NO_MATRICULA;";
            Connection conn = b1.getConnection();
            Statement pst2 = conn.createStatement();
            ResultSet rs = pst2.executeQuery(consultar);
//        Object datos[]=new Object[2];        
            JOptionPane.showMessageDialog(null, "id del mensaje: " + idMensaje_del_dia);
            if (rs.next()) {
                do {
                    msn_diario[0] = rs.getInt("id_mensaje_diario_leido");
                    msn_diario[1] = rs.getString("No_Matricula");
                    msn_diario[2] = rs.getString("cont");
                } while (rs.next());
            } else {
                msn_diario[0] = 0;
                msn_diario[1] = "Matricula";
                msn_diario[2] = "-1";
            }
            conn.close();
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return msn_diario;
    }

    public void Actualizar(int id_mensaje_diario_leido, int idmensaje, int No_matricula) {
        try {
            Conexion miconexion = new Conexion();
            Connection conn = miconexion.getConnection();

            //Segun la columna que haya seleccionado sera el valor que se modique     
            String actualizar = "UPDATE `xorox`.`mensaje_diario_leido` SET `No_Matricula`=?, `cont`=`cont`+1 WHERE `id_mensaje_diario_leido`=" + id_mensaje_diario_leido + ";";
            PreparedStatement pst = conn.prepareStatement(actualizar);
            pst.setInt(1, No_matricula);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cambio realizado con exito");
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //en caso que se haya leido el mensaje del dia 
    public boolean Insertar_Lectura_del_mensaje_del_dia(int matricula, int idmensaje_diario) {
        boolean exito = false;
        try {
            String registrar = "INSERT INTO `xorox`.`mensaje_diario_leido` (`No_Matricula`, `id_mensaje_del_dia`, `cont`) VALUES (?,?, 1);";
            Conexion miconexion = new Conexion();
            Connection conn = miconexion.getConnection();
            PreparedStatement pst = conn.prepareStatement(registrar);
            pst.setInt(1, matricula);
            pst.setInt(2, idmensaje_diario);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Registro Exitoso");
            conn.close();
            exito = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return exito;
    }

    public Object[] Comprobar_MaC(String MAC) {
        String info[] = new String[2];
        try {
            String consultar = "SELECT * FROM xorox.equipo where MAC_ADDRESS='" + MAC + "';";
            Conexion b1 = new Conexion();
            Connection conn = b1.getConnection();
            Statement pst2 = conn.createStatement();
            ResultSet rs = pst2.executeQuery(consultar);
            while (rs.next()) {
                info[0] = rs.getString("MAC_ADDRESS");
                info[1] = rs.getString("ubicacion");

            }
            conn.close();
            return info;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erorrorororororo" + e.getMessage());
            return null;
        }

    }

    public Object[] Cargar_Info_Alumno(String No_Matricula) {
        Object datos[] = new Object[3];
        try {
            Conexion b2 = new Conexion();
            String consultar = "SELECT * FROM xorox.alumnos where NO_MATRICULA=" + No_Matricula + ";";
            Conexion miconexion = new Conexion();
            Connection conn = miconexion.getConnection();
            Statement pst2 = conn.createStatement();
            ResultSet rs = pst2.executeQuery(consultar);
            while (rs.next()) {
                datos[0] = (rs.getInt("NO_MATRICULA"));
                datos[1] = (rs.getString("Alumno"));
                datos[2] = (rs.getString("Carrera"));
            }
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return datos;
    }

    public void Registrar_Sesion(String cicloEscolar, int matricula, String MAC, String Maestro, String Materia, String Aula, String Fecha, String Duracion, String Comentarios) {
//        String registrar = "INSERT INTO `xorox`.`uso_del_equipo`"
//                + " (`NO_MATRICULA`, `MAC_ADDRESS`, `Maestro`, `Aula`, `Fecha`, "
//                + "`Duracion`, `Comentarios`) VALUES (?,?,?,?,?,?,?);";
//        
        String registrar = "INSERT INTO `xorox`.`uso_del_equipo` "
                + "(`Ciclo_Escolar`, `NO_MATRICULA`, `MAC_ADDRESS`, `Maestro`, `Materia`, `Aula`, `Fecha`, `Duracion`, `Comentarios`)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {

            Conexion miconexion = new Conexion();
            Connection conn = miconexion.getConnection();
            PreparedStatement pst = conn.prepareStatement(registrar);
            pst.setString(1, cicloEscolar);
            pst.setInt(2, matricula);
            pst.setString(3, MAC);
            pst.setString(4, Maestro);
            pst.setString(5, Materia);
            pst.setString(6, Aula);
            pst.setString(7, Fecha);
            pst.setString(8, Duracion);
            pst.setString(9, Comentarios);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Registro Exitoso");
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    public void deterner_Reloj() {
        a1.cron.stop();
    }

    public String Hora_Termino() {
        //Obtiene la hora actual   
        Date hoy = new Date();
        //Formato de Hora
        SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss");
        //Paso la hora con el formato
        horaTermino = s.format(hoy);
        return horaTermino;
    }

    public boolean Comprobar_Fecha_del_Ciclo_E(String Fecha_Actual) {
        boolean fecha_correcta = false;
        int id_Ciclo_esc = 0;
        try {
            //comprueba si la fecha actual se encuentra dentro del rango de algun ciclo escolar siempre y cuando este activo     
            String consultar = "SELECT id_Ciclo_Escolar FROM xorox.ciclo_escolar where str_to_date('" + Fecha_Actual + "', '%d/%m/%Y') between str_to_date(desde, '%d/%m/%Y') and  str_to_date(hasta, '%d/%m/%Y') and estado='activo'; ";
            Conexion miconexion = new Conexion();
            Connection conn = miconexion.getConnection();
            Statement pst2 = conn.createStatement();
            ResultSet rs = pst2.executeQuery(consultar);
            while (rs.next()) {
                //Guarda el numero de ID del ciclo   
                id_Ciclo_esc = (rs.getInt("id_Ciclo_Escolar"));
            }
            //si la fecha actual esta en el rango volvera la variable boolean verdadera
            if (id_Ciclo_esc != 0) {
                fecha_correcta = true;
            }
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        //regresa un falso o un verdadero de acuerdo a la comprobacion de la fecha
        return fecha_correcta;
    }

    public void Apagar_Windows() {
        try {
            String[] cmd = {"shutdown", "-s", "-t", "10"}; //Comando de apagado en windows
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblMAC_ADDRESS = new javax.swing.JLabel();
        lblUbicacion = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnCerrarSesion = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblAula = new javax.swing.JLabel();
        lblNo_Matricula = new javax.swing.JLabel();
        lblAlumno = new javax.swing.JLabel();
        lblCarrera = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblCronometro = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Equipo");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Informacion del Equipo");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 30, -1, -1));

        jLabel2.setText("MAC Address:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 70, -1, -1));

        jLabel3.setText("Ubicacion:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, -1, -1));
        jPanel1.add(lblMAC_ADDRESS, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 70, 252, 14));
        jPanel1.add(lblUbicacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 100, 252, 14));

        jLabel7.setText("Fecha:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 130, -1, -1));
        jPanel1.add(lblFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, 252, 14));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/desktop-pc-vector.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel6)
                .addContainerGap(106, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 24, -1, 140));

        btnCerrarSesion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/image4144.png"))); // NOI18N
        btnCerrarSesion.setText("Cerrar Sesion");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });
        jPanel1.add(btnCerrarSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 470, 200, 90));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setText("Informacion del Alumno");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 180, -1, -1));

        jLabel9.setText("Alumno:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 230, -1, -1));

        jLabel10.setText("No. Matricula:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 210, -1, -1));

        jLabel11.setText("Carrera:");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 250, -1, -1));

        jLabel12.setText("Aula:");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 270, -1, -1));
        jPanel1.add(lblAula, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 270, 250, 20));
        jPanel1.add(lblNo_Matricula, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 210, 250, 20));
        jPanel1.add(lblAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 230, 250, 20));
        jPanel1.add(lblCarrera, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 250, 250, 20));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Silueta.jpg"))); // NOI18N
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 200, 360));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));

        lblCronometro.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblCronometro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCronometro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblCronometro.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel5.setFont(new java.awt.Font("Segoe UI Light", 1, 18)); // NOI18N
        jLabel5.setText("Uso del Equipo");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/timeclock_tiemp_3924.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCronometro, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jLabel5)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(lblCronometro, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 19, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 300, 450, 180));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        // TODO add your handling code here:
        deterner_Reloj();
        Hora_Termino();
        Alumno.Duracion = Info_Computo.lblCronometro.getText();

        //Se registra la sesion
        Registrar_Sesion(Alumno.cicloEscolar, Alumno.No_Matricula, Alumno.macadress, Alumno.Maestro, Alumno.Materia, Alumno.Aula, Alumno.Fecha, Alumno.Duracion, Alumno.Comentarios);
        //Se apaga el Equipo
        //Apagar_Windows();
        btnCerrarSesion.setEnabled(false);
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    /**
     * @param args the command line arguments
     */
    public String nombre(int mes) {
        switch (mes) {
            case 0:
                nombre = "Enero";
                break;

            case 1:
                nombre = "Febrero";
                break;

            case 2:
                nombre = "Marzo";
                break;

            case 3:
                nombre = "Abril";
                break;

            case 4:
                nombre = "Mayo";
                break;

            case 5:
                nombre = "Junio";
                break;

            case 6:
                nombre = "Julio";
                break;

            case 7:
                nombre = "Agosto";
                break;

            case 8:
                nombre = "Septiembre";
                break;

            case 9:
                nombre = "Octubre";
                break;

            case 10:
                nombre = "Noviembre";
                break;

            case 11:
                nombre = "Diciembre";
                break;

        }
        return nombre;
    }

    public String formato_mes(int mes) {
        //debido a que los enteros de los meses del jcalendar 
        //empiesan en 0 se le aumenta uno para poder regresar el entero correcto
        String mes1 = "";
        mes = mes + 1;

        //esto se hace debido al formato de las fecha
        if (mes <= 9) {
            mes1 = "0" + mes;
        } else {
            mes1 = "" + mes;
        }
        return mes1;
    }

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
            java.util.logging.Logger.getLogger(Info_Computo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Info_Computo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Info_Computo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Info_Computo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Info_Computo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblAlumno;
    private javax.swing.JLabel lblAula;
    private javax.swing.JLabel lblCarrera;
    public static javax.swing.JLabel lblCronometro;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblMAC_ADDRESS;
    private javax.swing.JLabel lblNo_Matricula;
    private javax.swing.JLabel lblUbicacion;
    // End of variables declaration//GEN-END:variables

}
