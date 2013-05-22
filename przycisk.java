/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serwer;

import com.esotericsoftware.minlog.Log;
import java.awt.Dimension;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author Olek
 */
public class przycisk {
    
    JButton przycisk;
    JTextField log;
    JTextField send;
    JFrame okno,okno2;
    przycisk(){
        
                okno = new JFrame();
                okno.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                
                przycisk = new JButton();
                przycisk.setText("Run server");
//               okno.setBounds(0,0,300,300);
//                
//                log = new JTextField();
//                log.setText(null);
//                log.setBounds(0,0,300,200);
//
//                send = new JTextField();
//                send.setText(null);
//                send.setBounds(0, 200, 300, 30);
//                okno.add(log);
//                okno.add(send);
                
                 okno.add(przycisk);
                 okno.pack();
                
                okno.setVisible(true);
                przycisk.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        try {
                            przyciskActionPerformed(evt);
                        } catch (IOException ex) {
                            Logger.getLogger(Serwer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });  
    }
    
    
          private void przyciskActionPerformed(java.awt.event.ActionEvent evt) throws IOException {  
              new Serwer();
                //okno.setVisible(false);
                 przycisk.setEnabled(false);
//                okno2 = new JFrame();
//                
//                okno2.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
//                log = new JTextField();
//                log.setText(null);
//                log.setBounds(0,0,300,200);
//
//                send = new JTextField();
//                send.setText(null);
//                send.setBounds(0, 200, 300, 30);
//                okno2.add(log);
//                okno2.add(send);
//                okno2.setVisible(true);
//                okno2.pack();
              
          }

	public static void main (String[] args) throws IOException {
                Log.set(Log.LEVEL_DEBUG);
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new przycisk();
                    }
                });
                
		
	}
    
}
