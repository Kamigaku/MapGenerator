package com.kamigaku.dungeongenerator.dijkstra;

import java.awt.Point;
import java.util.HashMap;

public class Square {
  
    private final HashMap<Integer, Integer> _possibleDestination;
    
    public Square(char[][] map) {
        Dijkstra dijkstra = new Dijkstra(map);
        dijkstra.addRule(new Rule('W', 'W', true, false));
        dijkstra.createNodes(false);
        this._possibleDestination = new HashMap<Integer, Integer>();
        for(int y = 0; y < map.length; y++) {
            for(int x = 0; x < map[y].length; x++) {
                if(map[y][x] == 'W') {
                    this._possibleDestination.put(Dijkstra.XYValue(x, y, map[0].length), 
                            dijkstra.numberOfAccessiblePoint(new Point(x, y)));
                    dijkstra.unfetchNodes();
                }
            }
        }
    }
    
    /***
     * Compare deux carrés pour voir si la modification peut être apportée
     * @param preUpdate : l'origine du carré
     * @param postUpdate : l'après
     * @return si TRUE, la modification n'engendre aucune modification, si FALSE, la modification engendre un changement
     */
    public static boolean compareSquare(Square preUpdate, Square postUpdate) {
        for(Integer key : preUpdate._possibleDestination.keySet()) {
            if(key != 4) {
                int size_pre = preUpdate._possibleDestination.get(key);
                int size_post = postUpdate._possibleDestination.get(key);
                if(Math.abs(size_post - size_pre) > 1)
                    return false;
            }
        }
        return true;
    }
    
}
