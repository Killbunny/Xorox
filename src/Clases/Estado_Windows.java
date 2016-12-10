/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import javax.swing.JOptionPane;

/**
 *
 * @author Hp Pavilion g4
 */
public class Estado_Windows extends Thread {

    public Estado_Windows() {

    }

    public void run() {
        while (true) {
            if (System.getProperties().equals("shutdown")) {
                JOptionPane.showMessageDialog(null, "Esta Cerrando Windows");
            }
        }
    }
}
