/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentePropio;

import javax.swing.JTextField;

/**
 *
 * @author USUARIO
 */
public class SoloNumeros extends JTextField {

    public SoloNumeros() {
        this.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtKeyTyped(evt);
               
                
            }
        });
    }

    private void jtxtKeyTyped(java.awt.event.KeyEvent evt) {
        // TODO add your handling code here:

        char caracter = evt.getKeyChar();

        if (((caracter < '0') || caracter > '9')) {

            evt.consume();
        }
    }
}
