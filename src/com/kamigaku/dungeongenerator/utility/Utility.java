package com.kamigaku.dungeongenerator.utility;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public abstract class Utility {
        
    public static void fillArray(char[][] obj, char filler) {
        for(int i = 0; i < obj.length; i++) {
            for(int j = 0; j < obj[i].length; j++) {
                obj[i][j] = filler;
            }
        }
    }

    public static ArrayList<Point> commonCoords(ArrayList<Point> v1, ArrayList<Point> v2) {
        ArrayList<Point> v3 = new ArrayList<Point>();
        for(Point v : v1) {
            if(v2.contains(v))
                v3.add(v);
        }
        return v3;
    }
    
    /***
     * Permet de vérifier si les cases proches (haut, bas, gauche et droite) possède l'élément recherché.
     * @param map La carte
     * @param x_origin L'origine X
     * @param y_origin L'origine Y
     * @param search L'élément recherché
     * @return TRUE si l'élément est présent, FALSE si non.
     */
    public static boolean checkXorYSurrondings(char[][] map, int x_origin, 
                                            int y_origin, char search) {
        boolean xAxis = checkXSurrondings(map, x_origin, y_origin, search);
        boolean yAxis = checkYSurrondings(map, x_origin, y_origin, search);
        return (xAxis || yAxis);
    }
    
    public static boolean checkXandYSurrondings(char[][] map, int x_origin, 
                                            int y_origin, char search) {
        boolean xAxis = checkXSurrondings(map, x_origin, y_origin, search);
        boolean yAxis = checkYSurrondings(map, x_origin, y_origin, search);
        return (xAxis && yAxis);
    }
    
    public static boolean checkYSurrondings(char[][] map, int x_origin, int y_origin,
                                            char search) {
        return (y_origin - 1 >= 0 && map[y_origin - 1][x_origin] == search) || 
               (y_origin + 1 < map.length && map[y_origin + 1][x_origin] == search);
    }
    
    public static boolean checkXSurrondings(char[][] map, int x_origin, int y_origin,
                                            char search) {
        return (x_origin - 1 >= 0 && map[y_origin][x_origin - 1] == search) || 
               (x_origin + 1 < map[y_origin].length && map[y_origin][x_origin + 1] == search);
    }
    
    public static boolean checkSquareSurrondings(char[][] map, int x_origin,
                                                 int y_origin, char search) {
        if(x_origin - 1 >= 0) {
            if(y_origin - 1 >= 0 && map[y_origin - 1][x_origin - 1] == search)
                return true;
            if(y_origin + 1 < map.length && map[y_origin + 1][x_origin - 1] == search)
                return true;
        }
        if(x_origin + 1 < map[y_origin].length) {
            if(y_origin - 1 >= 0 && map[y_origin - 1][x_origin + 1] == search)
                return true;
            if(y_origin + 1 < map.length && map[y_origin + 1][x_origin + 1] == search)
                return true;
        }
        return Utility.checkXorYSurrondings(map, x_origin, y_origin, search);
    }
    
    public static int checkDiagonalsSurrondings(char[][] map, int x_origin,
                                                int y_origin, char search) {
        boolean left_top_2_right_bot = false;
        boolean right_top_2_left_bot = false;
        
        if(x_origin - 1 >= 0 && y_origin + 1 < map.length &&
           y_origin - 1 >= 0 && x_origin + 1 < map[y_origin - 1].length) { // top left 2 bottom right
            if(map[y_origin + 1][x_origin - 1] == search && 
               map[y_origin - 1][x_origin + 1] == search &&
               !Utility.checkXorYSurrondings(map, x_origin, y_origin, ' '))
                left_top_2_right_bot = true;
            if(map[y_origin + 1][x_origin + 1] == search && 
               map[y_origin - 1][x_origin - 1] == search &&
               !Utility.checkXorYSurrondings(map, x_origin, y_origin, ' '))
                right_top_2_left_bot = true;
        }
        if(left_top_2_right_bot && right_top_2_left_bot) return 2;
        else if(left_top_2_right_bot) return 1;
        else if(right_top_2_left_bot) return -1;
        return 0;
    }
    
    public static int numberSquareSurrondings(char[][] map, int x_origin, int y_origin,
                                               char search, int direction_x, int direction_y)  {
        int numberSurronding = 0;
        if(x_origin - 1 >= 0 && y_origin + 1 < map.length &&
           y_origin - 1 >= 0 && x_origin + 1 < map[y_origin].length) {
            if(map[y_origin + (1 * direction_y)][x_origin] == search)
                numberSurronding++;
            if(map[y_origin + (1 * direction_y)][x_origin + (1 * direction_x)] == search)
                numberSurronding++;
            if(map[y_origin][x_origin + (1 * direction_x)] == search)
                numberSurronding++;
            if(map[y_origin][x_origin] == search)
                numberSurronding++;
        }
        return numberSurronding;
    }
    
    public static int numberAllAroundSurrondings(char[][] map, int x_origin, int y_origin,
                                               char search)  {
        int numberSurronding = 0;
        if(x_origin - 1 >= 0 && y_origin + 1 < map.length &&
           y_origin - 1 >= 0 && x_origin + 1 < map[y_origin - 1].length) {
            if(map[y_origin + 1][x_origin + 1] == search)
                numberSurronding++;
            if(map[y_origin + 1][x_origin - 1] == search)
                numberSurronding++;
            if(map[y_origin + 1][x_origin] == search)
                numberSurronding++;
            if(map[y_origin - 1][x_origin + 1] == search)
                numberSurronding++;
            if(map[y_origin - 1][x_origin - 1] == search)
                numberSurronding++;
            if(map[y_origin - 1][x_origin] == search)
                numberSurronding++;
            if(map[y_origin][x_origin + 1] == search)
                numberSurronding++;
            if(map[y_origin][x_origin - 1] == search)
                numberSurronding++;
        }
        return numberSurronding;
    }
        
    public static int nextInt(Random random, int min, int max) {
        return random.nextInt(max-min) + min;
    }
    
    public static void displayEntity(String[][] map) {
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }        
    }
    
    public static void displayEntity(char[][] map) {
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
    
    public static void displayEntityReverseY(char[][] map) {
        for(int i = map.length - 1; i >= 0; i--) {
            for(int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
    
}
