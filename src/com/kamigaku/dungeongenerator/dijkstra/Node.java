/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kamigaku.dungeongenerator.dijkstra;

import java.util.ArrayList;

/**
 *
 * @author Kamigaku
 */
    public class Node {

        public int value;
        public ArrayList<Integer> neighbors;
        public boolean fetched;
        public boolean isCrossable;
        public int shortestDistance;
        public int previous;

        public Node(int value) {
            this.value = value;
            this.neighbors = new ArrayList<Integer>();
            this.fetched = false;
            this.shortestDistance = 0;
            this.previous = -1;
        }

        public void addNeighbors(Integer value) {
            if(!this.neighbors.contains(value))
                this.neighbors.add(value);
        }
        
        public void addNeighbors(int value) {
            if(!this.neighbors.contains(value))
                this.neighbors.add(value);
        }
        
        public static int XYValue(int x, int y, int size) {
            return x + (y * size);
        }

        public static int XValue(int value, int size) {
            return (int)(value % size);
        }

        public static int YValue(int value, int size) {
            return (int)(value / size);
        } 

    }
