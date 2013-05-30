
package serwer;

/**
 *
 * @author Olek
 */
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import serwer.Network.*;

public class przycisk2 extends JPanel implements ActionListener {
    protected JTextField send;
    protected JTextArea log;
    protected JButton przycisk;
    private final static String newline = "\n";
    private Serwer serwer;

    public przycisk2() {
        super(new GridBagLayout());

        send = new JTextField(30);
        send.addActionListener(this);
        send.setEditable(false);
        
        log = new JTextArea(30, 30);
        log.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(log);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        add(send, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
        przycisk = new JButton();
        przycisk.setText("Run server");
        add(przycisk);        
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

    public void actionPerformed(ActionEvent evt) {
        
        
        SendChat info = new SendChat();
        if(send.getText().startsWith(".")){
            commands(send.getText());
            send.setText("");

            return;
        }


        info.napis = "Serwer: "+send.getText();
        serwer.server.sendToAllTCP(info);
                            
       
        log.append(info.napis + newline);
        send.setText("");

       log.setCaretPosition(log.getDocument().getLength());
        
        
        
    }

    /**
     * 
     */
    private static void createAndShowGUI() {
        
        JFrame frame = new JFrame("Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new przycisk2());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
       javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    
    private void przyciskActionPerformed(ActionEvent evt) throws IOException {
         serwer = new Serwer(this);
                
         przycisk.setEnabled(false);
         send.setEditable(true);
        
    }
    
             
private void commands(String napis) {

        if (napis.contains(".setMap")) {
            napis = napis.replace(".setMap ", "");

            int i;
            i = Integer.decode(napis);
            SetMap setMap = new SetMap();
            setMap.id = i;

            serwer.server.sendToAllTCP(setMap);
            return;
        }
        if (napis.contains(".kick")){
            napis = napis.replace(".kick ", "");
            Kick kick = new Kick();
            kick.name = napis;
            serwer.server.sendToAllTCP(kick);
            return;
        }

    }
}
