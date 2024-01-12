/*
 * Copyright (c) 2024. Intel
 *
 * This file is part of LuminaryOS
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.luminary.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Window {
    private JFrame frame;
    private JPanel panel;


    public Window(String title, int width, int height) {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //
        frame.addMouseListener(new DragMouseListener());
        frame.addMouseMotionListener(new DragMouseListener());
        //

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        //
        panel.addMouseListener(new DragMouseListener());
        panel.addMouseMotionListener(new DragMouseListener());
        //
        frame.add(panel);

    }

    public void addComponent(Component component, String constraints) {
        frame.add(component, constraints);
        panel.add(component, constraints);
    }

    public void show() {
        frame.setVisible(true);
    }

    private class DragMouseListener extends MouseAdapter {
        private Point offset = new Point(); // Initialize offset to avoid NullPointerException
        @Override
        public void mouseEntered(MouseEvent e) {
            Component comp = (Component) e.getSource();
            comp.setBackground(Color.CYAN);
        }
        @Override
        public void mousePressed(MouseEvent e) {
            offset = e.getPoint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point current = e.getPoint();
            System.out.println(e.getSource());
            Point location = frame.getLocation();
            frame.setLocation((int) (location.getX() + current.getX() - offset.getX()),
                    (int) (location.getY() + current.getY() - offset.getY()));
        }
    }

    public static void main(String[] args) {
        Window rootWindow = new Window("Simple Wrapper", 400, 300);
        JPanel subWindow1 = new JPanel();
        subWindow1.setSize(50, 50);
        subWindow1.setBackground(Color.BLUE);
        rootWindow.addComponent(subWindow1, BorderLayout.NORTH);

        JPanel subWindow2 = new JPanel();
        subWindow2.setSize(50, 50);
        subWindow2.setBackground(Color.GREEN);
        rootWindow.addComponent(subWindow2, BorderLayout.SOUTH);

        JButton button = new JButton("Click me!");
        subWindow2.add(button);

        rootWindow.show();
    }
}
