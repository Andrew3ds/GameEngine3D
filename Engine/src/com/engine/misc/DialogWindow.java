package com.engine.misc;

import com.engine.core.Engine;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew on 1/5/2017.
 */
public class DialogWindow {
    private static Thread thread;
    private static List<JDialog> dialogs = new ArrayList<>();

    public static void init() {
        thread = new Thread(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            while(true) {
                if(dialogs.isEmpty() && !Engine.isRunning()) {
                    break;
                }

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Dialog Thread");

        thread.start();
    }

    public static void newDialog(String title, String text, int width, int height, Map<String, ButtonAction> buttons) {
        JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        dialog.setTitle(title);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dialogs.remove(dialog);
            }
        });
        dialog.setSize(Math.min(width, 640), Math.min(height, 480));
        dialog.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        dialog.setContentPane(panel);

        JTextArea jta = new JTextArea(text);
        jta.setEditable(false);
        JScrollPane scrollpane = new JScrollPane(jta);
        scrollpane.setSize(width-6, height-60);
        dialog.getContentPane().add(scrollpane);

        dialog.setVisible(true);
        dialogs.add(dialog);
    }

    public static void infoDialog(String title, String text) {
        int minWidth = 320;
        int minHeight = 120;
        int newWidth = 0;
        int newHeight;

        String[] lines = text.split("\n");
        for (String line : lines) {
            int width = line.length() * 8 * 2;
            if(width > minWidth) {
                newWidth = width;
            } else {
                newWidth = minWidth;
            }
        }
        int height = lines.length * 12 * 2;
        if(height > minHeight-60) {
            newHeight = height;
        } else {
            newHeight = minHeight;
        }

        Map<String, ButtonAction> buttons = new HashMap<>();
        buttons.put("OK", () -> {});

        System.out.println(newHeight);

        newDialog(title, text, newWidth, newHeight+60, buttons);
    }

    public static void errorDialog(Exception e) {
        String message = e.getMessage() + "\n";
        for(StackTraceElement stackTraceElement : e.getStackTrace()) {
            message = message.concat(stackTraceElement.toString()).concat("\n");
        }
        infoDialog("Error", message);
        e.printStackTrace();
    }

    public interface ButtonAction {
        void invoke();
    }
}
