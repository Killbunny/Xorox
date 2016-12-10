/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Hp Pavilion g4
 */
public class Servidor extends Thread{
     ServerSocket servidor;
     Socket cliente;
    
    public Servidor()
    {
        
    }
    
    public void run()
    {
     try {
                    //Abro el puerto 9090
                    servidor=new ServerSocket(9090);                  
                    JOptionPane.showMessageDialog(null, "Se abrio el Puerto");                            
                    while(true)
                    {
                    //Permito aceptar a quien sea
                    cliente=servidor.accept();  
                    DataInputStream flujo=new DataInputStream(cliente.getInputStream());
                    //Almaceno el mensaje que fue enviado a este a cliente en la siguiente variable
                    String mensaje=flujo.readUTF();
                    //Muestra los mensajes recibidos por parte del Servidor.
                    JOptionPane.showMessageDialog(null, mensaje);
                    }
                    
                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
    }
}
