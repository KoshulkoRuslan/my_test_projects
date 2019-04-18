//package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrameLauncher {
    public static void main(String[]args)
    {
        JFrame f = new testCalc("testCalc");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

    }
}
class testCalc extends JFrame
{
    //Создание экземпляров элементов интерфейса
    private JTextField calcInput = new JTextField("Input ", 150);
    private JButton bttnResult = new JButton("Start");
    private JTextField calcResult = new JTextField("Result",150);

     testCalc(String title)
    {
        super (title);
        //Присваивание элементов слою
        getContentPane().add(calcInput, BorderLayout.NORTH);
        getContentPane().add(calcResult, BorderLayout.SOUTH);
        getContentPane().add(bttnResult,BorderLayout.CENTER);
        setSize (300, 150);
        setVisible (true);
        bttnResult.addActionListener(new bttnAction());
    }

    public class bttnAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String inputString = calcInput.getText();
            char[] inputChar_ = inputString.toCharArray();

            //проверка на недопустимые символы
            if(!inputString.matches("[0-9\\-\\+\\*\\(\\)\\/]*")) {
                calcResult.setText("Недопустимые символы");
                return;
            }
            //Преобразуем унарный минус в бинарный, добавляя перед ним 0
            if (inputChar_[0] == '-'){
                inputString = "0" + inputString;
            }
            char min = '-';
            int openBracket = 0;
            int closeBracket = 0;
            for(int i=1 ; i<inputChar_.length; i++){
                if(inputChar_[i] == min && inputChar_[i-1] == '('){
                        inputString = inputString.replace("(-","(0-");
                    }

            }
            for (int i=0 ; i<inputChar_.length; i++){
                if(inputChar_[i] == '(') openBracket++;
                if (inputChar_[i] == ')') closeBracket++;
            }
            if (!Integer.valueOf(openBracket).equals(closeBracket)){
                calcResult.setText("Скобки не согласованы");
                return;
            }
            char[] inputChar = inputString.toCharArray();

            //Выражение к виду обратной польской записи
            ArrayList<String> outArray = new ArrayList<>();
            ArrayList<String> operatArray = new ArrayList<>();


            for (char c1 : inputChar) {
                if (c1 == '0' ||
                        c1 == '1' ||
                        c1 == '2' ||
                        c1 == '3' ||
                        c1 == '4' ||
                        c1 == '5' ||
                        c1 == '6' ||
                        c1 == '7' ||
                        c1 == '8' ||
                        c1 == '9') {
                    outArray.add(Character.toString(c1));
                    System.out.println("Обрабатываем цифру");
                }
                if (c1 == '+' ||
                        c1 == '-' ||
                        c1 == '*' ||
                        c1 == '/') {
                    outArray.add(",");
                    System.out.println("Обрабатываем +-/*");
                    if ((c1 == '+' || c1 == '-') &&
                            (operatArray.isEmpty() || operatArray.get(operatArray.size() - 1).equals("("))) {
                        operatArray.add(Character.toString(c1));
                        System.out.println("Обрабатываем + и - перед ( или пустым массивом");
                    } else if ((c1 == '+' || c1 == '-')&&
                            (operatArray.get(operatArray.size() - 1).equals("+") ||
                            operatArray.get(operatArray.size() - 1).equals("-") ||
                            operatArray.get(operatArray.size() - 1).equals("*") ||
                            operatArray.get(operatArray.size() - 1).equals("/"))) {
                        outArray.add(operatArray.get(operatArray.size() - 1));
                        outArray.add(",");
                        operatArray.remove(operatArray.size() - 1);
                        operatArray.add(Character.toString(c1));
                        System.out.println("Обрабатываем + и - перед другим оператором");
                    }
                    if ((c1 == '/' ||
                            c1 == '*') && (operatArray.isEmpty() ||
                            operatArray.get(operatArray.size() - 1).equals("(") ||
                            operatArray.get(operatArray.size() - 1).equals("+") ||
                            operatArray.get(operatArray.size() - 1).equals("-"))) {
                        operatArray.add(Character.toString(c1));
                        System.out.println("Обрабатываем * и / с добавлением без переноса");
                    } else if ((c1 == '/' ||
                            c1 == '*') &&
                            (operatArray.get(operatArray.size() - 1).equals("/") ||
                                    operatArray.get(operatArray.size() - 1).equals("*"))) {
                        outArray.add(operatArray.get(operatArray.size() - 1));
                        outArray.add(",");
                        operatArray.remove(operatArray.size() - 1);
                        operatArray.add(Character.toString(c1));
                        System.out.println("Обрабатываем * и / перед равнозначными");
                    }

                }

                if (c1 == '(' ||
                        c1 == ')') {
                    System.out.println("Обрабатываем скобки");
                    if (c1 == '(') {
                        operatArray.add(Character.toString(c1));
                        System.out.println("Обрабатываем (");

                    }
                    if (c1 == ')') {
                        System.out.println("Обрабатываем )");

                        while (!operatArray.get(operatArray.size() - 1).equals("(")) {
                            System.out.println("запускаем while");
                            int c = operatArray.size() - 1;
                            outArray.add(",");
                            outArray.add(operatArray.get(c));
                            operatArray.remove(c);
                            System.out.println("заканчиваем цикл");

                        }
                        if (operatArray.get(operatArray.size() - 1).equals("("))
                            operatArray.remove(operatArray.size() - 1);
                    }

                }

            }

            while(!operatArray.isEmpty()) {
                System.out.println("Добиваем операторы");
                int c = operatArray.size()-1;
                outArray.add(",");
                outArray.add(operatArray.get(c));
                operatArray.remove(c);
            }

            //Рассчет

            String listString = "";
            //Переводим outArray в строку
            for (String s : outArray)
            {
                listString = listString + s;
            }

            //Разбиваем строку разделителями и записываем в новый ArrayList
            ArrayList<String> calculateArray = new ArrayList<String>();
            ArrayList<Object> bufferArray = new ArrayList<>();

            for (String retval : listString.split(",")) {
                calculateArray.add(retval);
                System.out.println(retval);
            }


            for (String st :calculateArray){
                if (st.matches("[0-9]*")){
                    float i = Float.valueOf(st +".0");
                    bufferArray.add(i);
                    System.out.println("Добавили цифру " +i);
                }
                else if(st.matches("[\\*\\/\\+\\-]")){
                    float result;
                    switch (st){
                        case "+":
                            result =(Float) bufferArray.get(bufferArray.size()-2) +
                                    (Float) bufferArray.get(bufferArray.size()-1);
                            bufferArray.remove(bufferArray.size()-1);
                            bufferArray.remove(bufferArray.size()-1);
                            bufferArray.add(result);
                            System.out.println("Выполнили + и записали result" + result);
                            break;
                        case "-":
                            result =(Float) bufferArray.get(bufferArray.size()-2) -
                                    (Float) bufferArray.get(bufferArray.size()-1);
                            bufferArray.remove(bufferArray.size()-1);
                            bufferArray.remove(bufferArray.size()-1);
                            bufferArray.add(result);
                            System.out.println("Выполнили - и записали result" + result);
                            break;
                        case "*":
                            result =(Float) bufferArray.get(bufferArray.size()-2) *
                                    (Float) bufferArray.get(bufferArray.size()-1);
                            bufferArray.remove(bufferArray.size()-1);
                            bufferArray.remove(bufferArray.size()-1);
                            bufferArray.add(result);
                            System.out.println("Выполнили * и записали result" + result);
                            break;
                        case "/":
                            result =(Float) bufferArray.get(bufferArray.size()-2) /
                                    (Float) bufferArray.get(bufferArray.size()-1);
                            bufferArray.remove(bufferArray.size()-1);
                            bufferArray.remove(bufferArray.size()-1);
                            bufferArray.add(result);
                            System.out.println("Выполнили / и записали result" + result);
                            break;
                    }
                }

            }



            calcResult.setText(Float.toString((Float) bufferArray.get(bufferArray.size()-1)));

            }
     }
}




