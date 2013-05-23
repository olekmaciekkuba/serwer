/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serwer;

import com.esotericsoftware.minlog.Log;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Olek
 */
public class przycisk {
    
    JButton przycisk;
    JTextArea log;
    JTextField send;
    JFrame okno,okno2;
   
    JScrollPane scroll;
    przycisk(){
        
                
                okno = new JFrame();
                okno.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                
                
                przycisk = new JButton();
                przycisk.setText("Run server");
                okno.setSize(317, 320);
            
                okno.setLayout(null);
                log = new JTextArea();
                //log.setText(null);
                log.setBounds(0,0,300,200);
                scroll = new JScrollPane(log);
                send = new JTextField();
                send.setText(null);
                send.setBounds(0, 200, 300, 30);
                okno.add(log);
                okno.add(send);
                
                log.setEditable(false);
                okno.add(scroll);
                okno.add(przycisk);
                przycisk.setBounds(0,230 , 300, 50);
               
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
                
                send.addKeyListener(new java.awt.event.KeyListener() {

                    @Override
                    public void keyTyped(KeyEvent e) {
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if(e.getKeyCode() == KeyEvent.VK_ENTER){
                            log.setText(log.getText()+"\n"+send.getText());
                           
                            send.setText("");
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
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
