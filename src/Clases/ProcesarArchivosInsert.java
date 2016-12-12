/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ControlWifi
 */
public class ProcesarArchivosInsert {
    public static int procesar(){
        BufferedReader leer=null;
        //Creamops el objeto del archivo de registrospendientes.dat exista
        File f=new File(System.getProperty("java.io.tmpdir") + "registrospendientes.dat");
        int erroresDeInsert=0;
        try {
            //Leemos del archivo
            //Si no hay archivo se lanzará a exepcion FileNotFoundException
            leer = new BufferedReader(new FileReader(f));
            String stm;
            try {
                //Abrimos conexion
                Conexion miconexion = new Conexion();
                Connection conn = miconexion.getConnection();
                //Cargamos la linea leída del archivo a un a string
                stm = leer.readLine();
                //Mientras la cadena no sea nula significa que leyó algo
                while (stm!=null)
                {
                    //Intenta crear un statement con la cadena extraída del archivo
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.executeUpdate(stm);
                    } catch (SQLException ex) {
                        erroresDeInsert++;//La cadena leída no pudo ser insertada en la DB
                    }
                    //Lee la siguiente linea en el archivo
                    stm=leer.readLine();
                }
                leer.close();
                //Mensaje de notificación de errores de insert
                if(erroresDeInsert>0){
                    JOptionPane.showMessageDialog(null, "Hubo "+erroresDeInsert+" errores de insercion a la DB \n Revise las matriculas en el archivo residual");
                }
                Date hoy=new Date();
                DateFormat datef= new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
                
                //Preparamos el objeto del archivo donde se va a copiar para mantener un registro
                File fCopia=new File (System.getProperty("java.io.tmpdir") + "registrospendientes_"+datef.format(hoy)+".dat");               
                //System.out.println(""+f.getAbsoluteFile().toPath()+"|"+ fCopia.getAbsoluteFile().toPath()+"|"+StandardCopyOption.REPLACE_EXISTING);
                //Copiamos el archivo 'registrospendientes.dat' a 'resgistrospendientes_fecha hora.dat'  y enseguida eliminamos el original
                Files.copy(f.getAbsoluteFile().toPath(), fCopia.getAbsoluteFile().toPath(),StandardCopyOption.REPLACE_EXISTING);
                f.delete();
                return 1;//Encontró el archivo. Ejecucion normal
                
            } catch (IOException ex) {
                Logger.getLogger(ProcesarArchivosInsert.class.getName()).log(Level.SEVERE, null, ex);
                return -1;//Error al leer el archivo
            }
        } catch (FileNotFoundException ex) {
            return 0; //No encontró el archivo. Ejecucion normal
        }
    }
}
