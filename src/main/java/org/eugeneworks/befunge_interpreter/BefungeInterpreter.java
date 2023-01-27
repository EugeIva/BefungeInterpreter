package org.eugeneworks.befunge_interpreter;

import java.util.LinkedList;
import java.util.Random;

public class BefungeInterpreter {


    private static int[] direction = new int[]{0,1};
    private static int[] stack = new int[100];
    private static int x =0;
    private static int y =0;
    private static int pointer = -1;
    private static Random random = new Random();
    private static String directions = "<>^v";

    public static String interpret(String code) {
        x = 0;
        y = 0;
        direction = new int[]{0,1};
        stack = new int[100];
        pointer = -1;
        StringBuilder out = new StringBuilder();
        char[][] chars = decifer(code);
        char c = chars[y][x];
        boolean append = false;
        do {
            if (c == '"') append = !append;
            else if (append) {
                push(c);
            } else {
                if (directions.indexOf(c)!= -1) {
                    changeDirection(c);
                } else if ("1234567890".indexOf(c)!= -1) {
                    push(Integer.parseInt(String.valueOf(c)));
                } else {
                    switch (c) {
                        case '.' -> out.append(pop());
                        case ',' -> out.append((char)pop());
                        case ':' -> push(pointer == -1 ? 0 : stack[pointer]);
                        case '_' -> changeDirection(pop() == 0 ? '>' : '<');
                        case '|' -> changeDirection(pop() == 0 ? 'v' : '^');
                        case '*' -> push(pop() * pop());
                        case '-' -> push(-pop() + pop());
                        case '+' -> push(pop() + pop());
                        case '/' -> {
                            int a = pop();
                            int b = pop();
                            push(a == 0 ? 0: b / a);
                        }
                        case '%' -> {
                            int a = pop();
                            int b = pop();
                            push(a == 0 ? 0: b % a);
                        }
                        case '$' -> pop();
                        case '?' -> changeDirection(directions.charAt(random.nextInt(4)));
                        case '\\' -> {
                            int a = pop();
                            int b = pointer == -1 ? 0 : pop();
                            push(a);
                            push(b);
                        }
                        case '!' -> push(pop() == 0 ? 1 : 0);
                        case '`' -> push(pop() < pop() ? 1 : 0);
                        case '#' -> step(chars);
                        case 'p' -> chars[pop()][pop()] = (char)pop();
                        case 'g' -> push(chars[pop()][pop()]);
                    }
                }
            }

            step(chars);
            c = chars[y][x];
        } while (c != '@');
        return out.toString();
    }

    private static int pop() {
        int res = stack[pointer];
        stack[pointer] = 0;
        pointer--;
        return res;
    }
    private static void push(int val) {
        pointer++;
        stack[pointer] = val;

    }
    private static void step(char[][] input) {
        y += direction[0];
        if (y < 0) y =  input.length-1;
        if (y >= input.length) y = 0;
        x += direction[1];
        if (x < 0) x = input[y].length;
        if (x >= input[y].length) x = 0;
    }

    private static char[][] decifer(String code) {
        String[] lines = code.split("\n");
        char[][] res = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            res[i] = lines[i].toCharArray();
        }
        return res;
    }

    private static void changeDirection(char c) {
        switch (c) {
            case '<':
                direction[0] = 0;
                direction[1] = -1;
                break;
            case '>':
                direction[0] = 0;
                direction[1] = 1;
                break;
            case '^':
                direction[0] = -1;
                direction[1] = 0;
                break;
            case 'v':
                direction[0] = 1;
                direction[1] = 0;
                break;
        }
    }

}
