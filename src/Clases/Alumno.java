/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrador
 */
public class Alumno {

    public static boolean modoOffline=false;
    public static String cicloEscolar = "";
    public static int No_Matricula = 0;
    public static String macadress;
    public static String Horario_Y_Materia;
    public static String Alumno = "";
    public static String Materia = "";
    public static String Fecha = "";
    public static String Aula = "";
    public static String Duracion = "";
    public static String Comentarios = "";
    public static String Maestro = "";
    StringBuilder sb = new StringBuilder();
    InetAddress ip;
    Conexion a1 = new Conexion();
    Connection conn;

    public Alumno() {
    }

}
