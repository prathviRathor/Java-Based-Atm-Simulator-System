package ASimulatorSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class MiniStatement extends JFrame implements ActionListener{
 
    JButton b1, b2;
    JLabel l1, l5;
    MiniStatement(String pin){
        super("Mini Statement");
        getContentPane().setBackground(Color.WHITE);
        setSize(500,650);
        setLocation(20,20);
        
        l1 = new JLabel();
        add(l1);
        
        JLabel l2 = new JLabel("Indian Bank");
        l2.setBounds(150, 20, 100, 20);
        add(l2);
        
        JLabel l3 = new JLabel();
        l3.setBounds(20, 80, 300, 20);
        add(l3);
        
        JLabel l4 = new JLabel();
        l4.setBounds(20, 400, 300, 20);
        add(l4);

        l5 = new JLabel();
        l5.setBounds(20, 440, 450, 110);
        add(l5);
        
        try{
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from login where pin = '"+pin+"'");
            while(rs.next()){
                String cardno = rs.getString("cardno");
                if (cardno != null && cardno.length() >= 16) {
                    l3.setText("Card Number:    " + cardno.substring(0, 4) + "XXXXXXXX" + cardno.substring(12));
                } else {
                    l3.setText("Card Number:    " + cardno);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to fetch card details");
        }
        	 
        try{
            int balance = 0;
            StringBuilder statement = new StringBuilder("<html>");
            Conn c1  = new Conn();
            ResultSet rs = c1.s.executeQuery("SELECT * FROM bank where pin = '"+pin+"'");
            while(rs.next()){
                statement.append(rs.getString("date"))
                        .append("&nbsp;&nbsp;&nbsp;")
                        .append(rs.getString("mode"))
                        .append("&nbsp;&nbsp;&nbsp;")
                        .append(rs.getString("amount"))
                        .append("<br><br>");
                if(rs.getString("mode").equals("Deposit")){
                    balance += Integer.parseInt(rs.getString("amount"));
                }else{
                    balance -= Integer.parseInt(rs.getString("amount"));
                }
            }
            statement.append("</html>");
            l1.setText(statement.toString());
            l4.setText("Your total Balance is Rs "+balance);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            StringBuilder sessions = new StringBuilder("<html><b>ATM Use Timing</b><br>");
            Conn c2 = new Conn();
            ResultSet rs = c2.s.executeQuery(
                    "select login_time, logout_time from atm_session where pin = '"+pin+"' order by id desc limit 5");
            boolean hasSession = false;
            while(rs.next()){
                hasSession = true;
                String logoutTime = rs.getString("logout_time");
                if (logoutTime == null) {
                    logoutTime = "Still logged in";
                }
                sessions.append("In: ")
                        .append(rs.getString("login_time"))
                        .append("&nbsp;&nbsp; Out: ")
                        .append(logoutTime)
                        .append("<br>");
            }
            if(!hasSession){
                sessions.append("No login/logout record found");
            }
            sessions.append("</html>");
            l5.setText(sessions.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        
        setLayout(null);
        b1 = new JButton("Exit");
        add(b1);
        
        b1.addActionListener(this);
        
        l1.setBounds(20, 140, 400, 200);
        b1.setBounds(20, 570, 100, 25);
    }
    public void actionPerformed(ActionEvent ae){
        this.setVisible(false);
    }
    
    public static void main(String[] args){
        new MiniStatement("").setVisible(true);
    }
    
}
