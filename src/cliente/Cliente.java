/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import Interfaces.Login;
import Clases.ProcesarArchivosInsert;
import javax.swing.JOptionPane;
/**
 *
 * @author Hp Pavilion g4
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Login L1 = new Login();
        int p=ProcesarArchivosInsert.procesar();
        if(p>0){
            JOptionPane.showMessageDialog(null, "Inserciones pendientes realizadas");
        }else if (p<0){
            JOptionPane.showMessageDialog(null, "Error en el archivo!");
        }
//        L1.setVisible(true);
    }

}
