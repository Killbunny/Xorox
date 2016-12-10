/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import javax.swing.JLabel;

/**
 *
 * @author Hp Pavilion g4
 */
public class Cronometro extends Thread {

    public static int minuto;
    public static int hora;
    public static int segundos;
    JLabel tiempo;

    public Cronometro(JLabel tiempo) {
        minuto = 0;
        hora = 0;
        segundos = 0;
        this.tiempo = tiempo;
    }

    public void run() {
        while (true) {
            try {
                sleep(1000);
                segundos++;

                if (segundos > 59) {
                    segundos = 0;
                    minuto++;
                }
                if (minuto > 59) {
                    minuto = 0;
                    hora++;
                }

                String cadena = Formatear_Tiempo(hora, minuto, segundos);
                tiempo.setText(cadena);
            } catch (Exception e) {

            }
        }

    }

    public String Formatear_Tiempo(int hora, int minuto, int segundos) {
        String cadena = "";
        String hor = "";
        String min = "";
        String seg = "";

        //esto se hace debido al formato de las fechas
        if (hora <= 9) {
            hor = "0" + hora;
        } else {
            hor = "" + hora;
        }

        if (minuto <= 9) {
            min = "0" + minuto;
        } else {
            min = "" + minuto;
        }

        if (segundos <= 9) {
            seg = "0" + segundos;
        } else {
            seg = "" + segundos;
        }
        return cadena = hor + ":" + min + ":" + seg;
    }
}
