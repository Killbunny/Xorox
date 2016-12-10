/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 *
 * @author Jesus
 */
public class fondo_imagen extends javax.swing.JPanel {

    //   Toolkit tk=Toolkit.getDefaultToolkit();
    String nombreimagen;

    public fondo_imagen(int x, int y, String nombreimagen) {
        initComponents();
        this.setSize(x, y);
        this.nombreimagen = nombreimagen;
    }

    @Override
    public void paintComponent(Graphics g) {
        Dimension t = getSize();
        ImageIcon imagen = new ImageIcon(new ImageIcon(getClass().getResource(nombreimagen)).getImage());
        g.drawImage(imagen.getImage(), 0, 0, t.width, t.height, null);
        setOpaque(false);
        super.paintComponent(g);

        //   setOpaque(false);
    }

    private void initComponents() {

    }
}
