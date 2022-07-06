/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

/**
 *
 * @author JC
 */
public class MiJRViewer extends JRViewer {
    

    public MiJRViewer(JasperPrint jrPrint) {
        super(jrPrint);
        this.tlbToolBar.setVisible(false);
        this.lblStatus.setVisible(false);
        
    }
}
