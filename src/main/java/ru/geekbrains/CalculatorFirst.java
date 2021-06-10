package ru.geekbrains;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

    public class CalculatorFirst {

        public static class Calculator {

            public static void main(String[] args) {
                EventQueue.invokeLater(() -> {
                    CalculatorFrame frame = new CalculatorFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setBounds(300, 300, 400, 400);
                    frame.setVisible(true);
                });
            }
        }
        static class CalculatorFrame extends JFrame {
            public CalculatorFrame() {
                setTitle("Мой первый калькулятор");
                CalculatorPanel panel = new CalculatorPanel();
                add(panel);
                pack();
                addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.out.println("Окно закрыто");
                    }
                });
            }
        }
        static class CalculatorPanel extends JPanel {
            public CalculatorPanel() {
                setLayout(new BorderLayout());

                result = 0;
                lastCommand = "=";
                start=true;

                display = new JButton("0");
                display.setEnabled(false);
                add(display, BorderLayout.NORTH);

                ActionListener insert = new InsertAction();
                ActionListener command = new CommandAction();

                panel = new JPanel();
                panel.setLayout(new GridLayout(4, 5));//честно говоря не понял для чего
                //в данном случае cols, при смене данного значения ничего не меняется

                addButton("C", command);
                addButton("7", insert);
                addButton("8", insert);
                addButton("9", insert);
                addButton("/", command);

                addButton("Х^Х", command);
                addButton("4", insert);
                addButton("5", insert);
                addButton("6", insert);
                addButton("*", command);

                addButton("", command); //долго думал как можно создать пустую строку без ввода данных
                addButton("1", insert);
                addButton("2", insert);
                addButton("3", insert);
                addButton("-", command);

                addButton("", command);
                addButton(".", insert);
                addButton("0", insert);
                addButton("=", command);
                addButton("+", command);


                add(panel, BorderLayout.CENTER);
            }
            private void addButton(String label, ActionListener listener) {
                JButton button = new JButton(label);
                button.addActionListener(listener);
                panel.add(button);
            }
            private class InsertAction implements ActionListener {

                public void actionPerformed(ActionEvent event) {

                    String input = event.getActionCommand();
                    if(start) {
                        display.setText(""); //поэксперементировал с командой null, не вышло
                        start = false;       //команда setText"" не полностью чистит поле
                    }
                    display.setText(display.getText() + input);
                }
            }
            private class CommandAction implements ActionListener {
                public void actionPerformed(ActionEvent event) {
                    String command = event.getActionCommand();
                    if (start) {
                        if (command.equals("C")) {
                            display.setText(""); //поэксперементировал с командой null, не вышло
                            start = false;       //команда setText"" не полностью чистит поле
                        } else lastCommand = command;
                    }
                    else {
                        calculate(Double.parseDouble(display.getText()));// была идея использовать BigDecimal, но IDEA ругалась
                        lastCommand = command;                           // нагуглил возможность обойти double (ниже)
                        start = true;
                    }
                }
            }
            public void calculate(double x) {

                switch (lastCommand) {
                    case "+" ->  result = Math.rint((result + x) * 100000) / 100000;  //это грубое откругление
                    //до ста тысячных
                    case "-" ->  result = Math.rint((result - x) * 100000) / 100000;  //ибо double выдавал
                    //несусветную чушь,
                    case "*" ->  result = Math.rint((result * x) * 100000) / 100000;  //потратил много нервов
                    //пока искал варианты обхода
                    case "/" ->  result = Math.rint((result / x) * 100000) / 100000;  //глупых ответов double

                    case "Х^Х" ->  result = Math.rint((result * result) * 100000) / 100000;
                    //возвести в степень не удалось, пробовал x * result, Math.exp(2 * Math.log(x)
                    //думал еще как-то через ретурн в отдельном методе
                    case "=" -> result = x;

                }
                display.setText(String.valueOf(result));
            }
            private final JButton display;
            private final JPanel panel;
            private double result;
            private String lastCommand;
            private boolean start;
        }
    }

