package com.mfundOptimizer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App extends JFrame{
    private JTextField amtTxtFld;
    private JButton runBtn;
    private JPanel rootPanel;


    public App(){
        add(rootPanel);
        setTitle("Mutual Fund Portfolio Optimizer");
        setSize(400,200);


        //when text is entered into the amt text field...
//        amtTxtFld.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                long amount = Long.parseLong(e.getActionCommand());  //get user input from the txtFld...
//                JOptionPane.showMessageDialog(null,amount);
//            }
//        });

        //when the run button is clicked
        runBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //We'll display the results of the program run
                Population pop = new Population(Long.parseLong(amtTxtFld.getText()) );
                pop.run();
                JOptionPane.showMessageDialog(null,pop.display(),"Results",JOptionPane.PLAIN_MESSAGE);
            }
        });

    }
}
