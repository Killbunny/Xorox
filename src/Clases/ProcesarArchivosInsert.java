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
        File f=new File(System.getProperty("java.io.tmpdir") + "registrospendientes.dat");
        int erroresDeInsert=0;
        try {
            leer = new BufferedReader(new FileReader(f));
            String stm;
            try {
                Conexion miconexion = new Conexion();
                Connection conn = miconexion.getConnection();
                stm = leer.readLine();
                
                while (stm!=null)
                {
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.executeUpdate(stm);
                    } catch (SQLException ex) {
                        erroresDeInsert++;
                    }
                    System.out.println(stm);
                    stm=leer.readLine();
                }
                leer.close();
                //TODO: hacer que renombre el archivo una vez leído a
                //El nombreDelArchivo_fecha
                if(erroresDeInsert>0){
                    JOptionPane.showMessageDialog(null, "Hubo "+erroresDeInsert+" errores de insercion a la DB \n Revise las matriculas en el archivo residual");
                }
                Date hoy=new Date();
                DateFormat datef= new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                File fCopia=new File (System.getProperty("java.io.tmpdir") + "registrospendientes_"+datef.format(hoy)+".dat");               
                Files.copy(f.getAbsoluteFile().toPath(), fCopia.getAbsoluteFile().toPath(),StandardCopyOption.REPLACE_EXISTING);
                
                return 1;//Encontró el archivo. Ejecucion normal
                
            } catch (IOException ex) {
                Logger.getLogger(ProcesarArchivosInsert.class.getName()).log(Level.SEVERE, null, ex);
                return -1;//Error al leer el archivo
            }
        } catch (FileNotFoundException ex) {
            return 0; //No encontró el archivo. Ejecucion normal
        } finally{
        }
    }
}
