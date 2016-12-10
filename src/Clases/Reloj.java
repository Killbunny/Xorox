/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;

/**
 *
 * @author Hp Pavilion g4
 */
public class Reloj extends Thread {

    JLabel lbl;

    public Reloj(JLabel lbl) {
        this.lbl = lbl;
    }

    public void run() {
        while (true) {
            Date hoy = new Date();
            SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss");
            lbl.setText(s.format(hoy));
            try {
                sleep(1000);

            } catch (Exception ex) {

            }
        }
    }
}
