/* Copyright (c) 2006-2007 Timothy Wall, All Rights Reserved
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.  
 */
package furbelow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class WaitIndicatorDemo {

    private static boolean fancy = true;
    
    private static WaitIndicator createWaiter(JComponent c) {
        return fancy ? new SpinningDialWaitIndicator(c) : new WaitIndicator(c);
    }
    
    private static WaitIndicator createWaiter(JFrame f) {
        return fancy ? new SpinningDialWaitIndicator(f) : new WaitIndicator(f);
    }
    
    private static void setWaiting(JComponent c, boolean on) {
        WaitIndicator w = (WaitIndicator)c.getClientProperty("waiter");
        if (w == null) {
            if (on) {
                w = createWaiter(c);
            }
        }
        else if (!on) {
            w.dispose();
            w = null;
        }
        c.putClientProperty("waiter", w);
    }
    
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Wait Indicator Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final Set components = new HashSet();
        JPanel address = new JPanel(new BorderLayout());
        address.add(new JLabel("Location"), BorderLayout.WEST);
        address.add(new JTextField(), BorderLayout.CENTER);
        components.add(address);
        
        URL url = WaitIndicatorDemo.class.getResource("winter.jpg");
        URL url2 = WaitIndicatorDemo.class.getResource("lilies.jpg");
        JLabel image = new JLabel(new ImageIcon(url));
        JLabel image2 = new JLabel(new ImageIcon(url2));
        components.add(image);
        components.add(image2);
        
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                                          new JScrollPane(image), 
                                          new JScrollPane(image2));
        
        JPanel buttons = new JPanel();
        final JButton start = new JButton("Start All");
        final JButton stop = new JButton("Stop All");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Iterator i=components.iterator();i.hasNext();) {
                    JComponent c = (JComponent)i.next();
                    setWaiting(c, true);
                }
                start.setEnabled(false);
                stop.setEnabled(true);
            }
        });
        stop.setEnabled(false);
        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Iterator i=components.iterator();i.hasNext();) {
                    JComponent c = (JComponent)i.next();
                    setWaiting(c, false);
                }
                start.setEnabled(true);
                stop.setEnabled(false);
            }
        });
        final JButton mix = new JButton("Mix It Up");
        mix.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int count = 0;
                Random r = new Random(System.currentTimeMillis());
                for (Iterator i=components.iterator();i.hasNext();) {
                    JComponent c = (JComponent)i.next();
                    boolean on = r.nextBoolean();
                    setWaiting(c, on);
                    if (on) ++count;
                }
                start.setEnabled(count < components.size());
                stop.setEnabled(count > 0);
            }
        });
        final JButton full = new JButton("Frame");
        full.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final WaitIndicator waiter = createWaiter(frame);
                new Thread() {
                    public void run() {
                        try { sleep(5000); }
                        catch(Exception e) { }
                        SwingUtilities.invokeLater(new Runnable() { 
                            public void run() { waiter.dispose(); }
                        });
                    }
                }.start();
            }
        });
        JCheckBox fancyCheck = new JCheckBox("Show Spinner", fancy);
        fancyCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fancy = !fancy;
                stop.doClick();
            }
        });
        buttons.add(start);
        buttons.add(stop);
        buttons.add(mix);
        buttons.add(full);
        buttons.add(fancyCheck);
        frame.getContentPane().add(address, BorderLayout.NORTH);
        frame.getContentPane().add(split, BorderLayout.CENTER);
        frame.getContentPane().add(buttons, BorderLayout.SOUTH);
        
        JMenuBar mb = new JMenuBar();
        mb.add(new JMenu("File"));
        mb.add(new JMenu("Edit"));
        frame.setJMenuBar(mb);
        
        frame.pack();
        frame.setSize(new Dimension(500, 300));
        frame.setVisible(true);
        split.setDividerLocation(0.5);
    }
}
