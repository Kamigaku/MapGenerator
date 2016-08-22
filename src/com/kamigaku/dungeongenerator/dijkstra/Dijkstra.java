package com.kamigaku.dungeongenerator.dijkstra;

import com.kamigaku.dungeongenerator.utility.Utility;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;

public class Dijkstra {

    private final char[][] _map;
    private final ArrayList<Rule> _rules; 
    private final Node[] _nodes;
    
    public Dijkstra(char[][] theMap) {
        this._map = theMap;
        this._rules = new ArrayList<>();
        this._nodes = new Node[this._map[0].length * this._map.length];
    }
        
    /**
     * Créer les noeuds pour le tableau passé
     * @param doAngles (boolean) : si TRUE, l'algorithme passera aussi dans les angles haut gauche, haut droite, bas gauche et bas droite.
     */
    public void createNodes(boolean doAngles) {
        for(int y = 0; y < this._map.length; y++) {
            for(int x = 0; x < this._map[y].length; x++) {
                if(isValidObject(this._map[y][x])) {
                    int value = XYValue(x, y, this._map[y].length);
                    _nodes[value] = new Node(value);
                    if(x + 1 < this._map[y].length && this.validateARule(this._map[y][x], this._map[y][x + 1], true)) // Droite
                        _nodes[value].addNeighbors(XYValue(x + 1, y, this._map[y].length));

                    if(x - 1 >= 0 && this.validateARule(this._map[y][x], this._map[y][x - 1], true)) // Gauche
                        _nodes[value].addNeighbors(XYValue(x - 1, y, this._map[y].length));

                    if(y + 1 < this._map.length && this.validateARule(this._map[y][x], this._map[y + 1][x], true)) // Haut
                        _nodes[value].addNeighbors(XYValue(x, y + 1, this._map[y].length));

                    if(y - 1 >= 0 && this.validateARule(this._map[y][x], this._map[y - 1][x], true)) // Bas
                        _nodes[value].addNeighbors(XYValue(x, y - 1, this._map[y].length));

                    if(doAngles) {
                        if(y + 1 < this._map.length && x - 1 >= 0 && 
                                this.validateARule(this._map[y][x], this._map[y + 1][x - 1], false)) // Haut gauche
                            _nodes[value].addNeighbors(XYValue(x - 1, y + 1, this._map[y].length));

                        if(y + 1 < this._map.length && x + 1 < this._map[y].length && 
                                this.validateARule(this._map[y][x], this._map[y + 1][x + 1], false)) // Haut droite
                            _nodes[value].addNeighbors(XYValue(x + 1, y + 1, this._map[y].length));

                        if(y - 1 >= 0 && x - 1 >= 0 && 
                                this.validateARule(this._map[y][x], this._map[y - 1][x - 1], false)) // Bas gauche
                            _nodes[value].addNeighbors(XYValue(x - 1, y - 1, this._map[y].length));

                        if(y - 1 >= 0 && x + 1 < this._map[y].length && 
                                this.validateARule(this._map[y][x], this._map[y - 1][x + 1], false)) // Bas droite
                            _nodes[value].addNeighbors(XYValue(x + 1, y - 1, this._map[y].length));
                    }
                }
            }
        }
    }
    
    public ArrayList<Point> shortestPathFromTo(Point from, Point to) {
        int valueOrigine = Dijkstra.XYValue(from.x, from.y, this._map[0].length);
        int valueTarget = Dijkstra.XYValue(to.x, to.y, this._map[0].length);

        resetDistance();

        Node currentNode = this._nodes[valueOrigine];
        currentNode.previous = valueOrigine;
        boolean found = false;

        ArrayList<Node> ordereredDistance = new ArrayList<>();
        while (!found) {
            int shortDistance = currentNode.shortestDistance + 1; // La valeur la plus petite possible
            for (int i = 0; i < currentNode.neighbors.size(); i++) { // Je parcours tous mes voisins
                Node neighbor = this._nodes[currentNode.neighbors.get(i)];
                if (!neighbor.fetched) { // Est-ce qu'il n'a jamais été parcouru ?
                    if (neighbor.shortestDistance > 0) { // Est-ce que le noeud voisin a déjà été parcouru ?
                                                    if (neighbor.shortestDistance > shortDistance) { // Le noeud voisin possède un parcours qui était plus long
                            neighbor.shortestDistance = shortDistance;
                            neighbor.previous = currentNode.value;
                        }
                    }
                    else { // Le noeud voisin n'a jamais été parcouru, j'initialise sa valeur et son précédent
                                                    neighbor.shortestDistance = shortDistance;
                        neighbor.previous = currentNode.value;
                        ordereredDistance.add(neighbor);
                    }
                }
            }

            this._nodes[currentNode.value].fetched = true;
            ordereredDistance.sort(new Comparator<Node>() {
                @Override
                public int compare(Node n1, Node n2) {
                    return n1.shortestDistance - n2.shortestDistance;
                }
            });
            if(ordereredDistance.isEmpty())
                return null;
            currentNode = ordereredDistance.get(0);
            ordereredDistance.remove(0);
            if (currentNode.value == valueTarget) {
                found = true;
            }
        }

        // Parcours inversé
        ArrayList<Point> reversePath = new ArrayList<Point>();
        while (currentNode.value != valueOrigine) {
            int yPrevious = Dijkstra.YValue(currentNode.value, this._map[0].length);
            int xPrevious = Dijkstra.XValue(currentNode.value, this._map[0].length);
            reversePath.add(new Point(xPrevious, yPrevious));
            currentNode = this._nodes[currentNode.previous];
        }
        return reversePath;
    }
    
    /**
     * Retourne le nombre de points accessibles à partir d'un noeud d'origine
     * @param origin (Point) Les coordonnées du point d'origine
     * @return Le nombre de point accessible
     */
    public int numberOfAccessiblePoint(Point origin) {
        return allPossiblePath(origin).size();
    }
    
    /**
     * Retourne la position du premier élément correspondant à la recherche par rapport à la position
     * @param origin (Point) Les coordonnées du point d'origine
     * @param toFind Le caractère recherché
     * @return Le premier point trouvé
     */
    public Point findFirst(Point origin, char toFind) {
        int valueOrigine = Dijkstra.XYValue(origin.x, origin.y, this._map[0].length);
        resetDistance();

        Node currentNode = this._nodes[valueOrigine];
        currentNode.previous = valueOrigine;
        boolean found = false;

        ArrayList<Node> ordereredDistance = new ArrayList<>();
        while (!found) {
            int shortDistance = currentNode.shortestDistance + 1; // La valeur la plus petite possible
            for (int i = 0; i < currentNode.neighbors.size(); i++) { // Je parcours tous mes voisins
            Node neighbor = this._nodes[currentNode.neighbors.get(i)];
                if (neighbor != null && !neighbor.fetched) { // Est-ce qu'il n'a jamais été parcouru ?
                    if (neighbor.shortestDistance > 0) { // Est-ce que le noeud voisin a déjà été parcouru ?
                                                    if (neighbor.shortestDistance > shortDistance) { // Le noeud voisin possède un parcours qui était plus long
                            neighbor.shortestDistance = shortDistance;
                            neighbor.previous = currentNode.value;
                        }
                    }
                    else { // Le noeud voisin n'a jamais été parcouru, j'initialise sa valeur et son précédent
                        neighbor.shortestDistance = shortDistance;
                        neighbor.previous = currentNode.value;
                        ordereredDistance.add(neighbor);
                    }
                }
            }

            this._nodes[currentNode.value].fetched = true;
            ordereredDistance.sort(new Comparator<Node>() {
                @Override
                public int compare(Node n1, Node n2) {
                    return n1.shortestDistance - n2.shortestDistance;
                }
            });
            if(ordereredDistance.isEmpty())
                return new Point(-1, -1);
            currentNode = ordereredDistance.get(0);
            ordereredDistance.remove(0);
            int xMap = Dijkstra.XValue(currentNode.value, this._map[0].length);
            int yMap = Dijkstra.YValue(currentNode.value, this._map[0].length);
            if (this._map[yMap][xMap] == toFind) {
                return new Point(xMap, yMap);
            }
        }
        return null;
    }
    
    /** 
     * Retourne tous les points accessibles à partir d'un noeud d'origine.
     * @param origin (Point) Le noeud d'origine
     * @return Tous les points accessibles
     */
    public ArrayList<Point> allPossiblePath(Point origin) {
        ArrayList<Point> allPath = new ArrayList<Point>();
        Node originNode = this._nodes[XYValue(origin.x, origin.y, this._map[origin.y].length)];
        allPossiblePath_fetch(originNode, allPath);
        return allPath;
    }
    
    private void allPossiblePath_fetch(Node origin, ArrayList<Point> path) {
        origin.fetched = true;
        for(int i = 0; i < origin.neighbors.size(); i++) {
            if(!this._nodes[origin.neighbors.get(i)].fetched) {
                path.add(new Point(XValue(origin.neighbors.get(i), this._map[0].length),
                        YValue(origin.neighbors.get(i), this._map[0].length)));
                allPossiblePath_fetch(this._nodes[origin.neighbors.get(i)], path);
            }
        }
    }
    
    public void unfetchNodes() {
        for(int j = 0; j < this._nodes.length; j++)
            if(this._nodes[j] != null) this._nodes[j].fetched = false;
    }
    
    public void unfetchNode(int x, int y) {
        if(this._nodes[XYValue(x, y, this._map[y].length)] != null)
            this._nodes[XYValue(x, y, this._map[y].length)].fetched = false;
    }
    
    public boolean validateARule(Object o1, Object o2, boolean line) {
        for(int i = 0; i < this._rules.size(); i++) {
            if(this._rules.get(i).isValid(o1, o2, line))
                return true;
        }
        return false;
    }
    
    public boolean isValidObject(Object o1) {
        for(int i = 0; i < this._rules.size(); i++) {
            if(this._rules.get(i).getSource() == o1)
                return true;
        }
        return false;
    }
    
    public void resetDistance() {
        for(int i = 0; i < this._nodes.length; i++)
            if(this._nodes[i] != null) {
                this._nodes[i].shortestDistance = 0;
                this._nodes[i].previous = -1;
            }
    }
    
    public void addRule(Rule r) {
        this._rules.add(r);
    }
    
    public Node[] getNodes() {
        return this._nodes;
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