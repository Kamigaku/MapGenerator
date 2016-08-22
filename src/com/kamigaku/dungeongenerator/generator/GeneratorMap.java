package com.kamigaku.dungeongenerator.generator;

import com.kamigaku.dungeongenerator.Map;
import com.kamigaku.dungeongenerator.Room;
import com.kamigaku.dungeongenerator.dijkstra.*;
import com.kamigaku.dungeongenerator.utility.Utility;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class GeneratorMap {
  
    // [TEMPORARY VARIABLES]
    private char[][] _map;
    private ArrayList<GeneratorRoom> _rooms;
    
    
    // [MAP PROPERTIES]
    private int _numberRoom;
    private long _seed;
    private Random _random;
    private Point _propWidthRoom;
    private Point _propHeightRoom;
    private String _stringMapSeed;
    
    // [RESULTS]
    private Map m;
    private ArrayList<Room> tRoom;
    
    public GeneratorMap(long seed) {
        this.init(seed);
        int minNumberRoom = Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 1)) + 
                Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 2)) * 10 + 10;
        int maxNumberRoom = (Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 3)) + 
                Integer.parseInt("" + _stringMapSeed.charAt(_stringMapSeed.length() - 4)) * 10) + minNumberRoom + 10;
        //System.out.println(_stringMapSeed + " / " + minNumberRoom + " / " + maxNumberRoom);
        this._numberRoom = Utility.nextInt(this._random, minNumberRoom, maxNumberRoom);
        this.executeProcess();
    }
    
    public GeneratorMap(long seed, int minNumberRoom, int maxNumberRoom) {
        this.init(seed);
        this._numberRoom = Utility.nextInt(this._random, minNumberRoom, maxNumberRoom);
        this.executeProcess();
    }
    
    public GeneratorMap(long seed, int minSizeRoomWidth, int maxSizeRoomWidth, 
                        int minSizeRoomHeight, int maxSizeRoomHeight) {
        this.init(seed);
        this._propHeightRoom = new Point(minSizeRoomHeight, maxSizeRoomHeight);
        this._propWidthRoom = new Point(minSizeRoomWidth, maxSizeRoomHeight);
        this.executeProcess();
    }
    
    public GeneratorMap(long seed, int minNumberRoom, int maxNumberRoom, 
                        int minSizeRoomWidth, int maxSizeRoomWidth, 
                        int minSizeRoomHeight, int maxSizeRoomHeight) {
        this.init(seed);
        this._numberRoom = Utility.nextInt(this._random, minNumberRoom, maxNumberRoom);
        this._propHeightRoom = new Point(minSizeRoomHeight, maxSizeRoomHeight);
        this._propWidthRoom = new Point(minSizeRoomWidth, maxSizeRoomHeight);
        this.executeProcess();
    }
    
    private void init(long seed) {
        this._seed = seed;
        this._random = new Random(seed);
        this._rooms = new ArrayList<>();
        /* UNCOMMENT IF NEEDED TO DO SOMETHING WITH THE FIRST RANDOM LIKE MAP TYPE, ETC... */
        this._stringMapSeed = "" + this._random.nextLong();
        //if(infoMapSeed.charAt(0) == '-') {
        //    infoMapSeed = infoMapSeed.substring(1);
        //}
        //int type = Integer.parseInt(
        //        "" + infoMapSeed.charAt(infoMapSeed.length() - 1));             // Map type
    }
    
    private void executeProcess() {
        this.generateRooms();                                                   // Générer les rooms
        Point sizeMap = sumSizeRooms(_rooms);
        char[][] map = new char[sizeMap.y][sizeMap.x];
        Utility.fillArray(map, '#');
        ArrayList<Point> borders = new ArrayList<>();
        GeneratorRoom r1 = this._rooms.get(0);
        int x = (map[0].length / 2) - (r1._width / 2);
        int y = (map.length / 2) - (r1._height / 2);
        for(int i = 0; i < this._rooms.size(); i++) {
            this.placeRoom(map, this._rooms.get(i), x, y);
            borders.addAll(this._rooms.get(i).getWorldBorders());
            for(int j = 0; j < borders.size(); j++) {
                if(Utility.numberAllAroundSurrondings(map, borders.get(i).x, borders.get(i).y, '#') < 2) {
                    borders.remove(j);
                    j--;
                }
                
            }
            Point p = borders.get(Utility.nextInt(_random, 0, borders.size()));
            x = p.x;
            y = p.y;
        }
        borders.clear();
        this.reduceMap(map);                                                    // Réduit la taille de la carte
        this.cleanMap();                                                        // Retire les murs qui ne sont pas nécessaires
        this.createRooms();                                                     // Créer les vraies salles
        this.connectRooms();                                                    // Connecte les salles entres elles
        this.createEntrance();                                                  // Créer l'entrée du donjon
        //Utility.displayEntityReverseY(this._map);
        this.boundRooms();                                                      // Connecte les salles avec leurs voisines
        //this.entryToExit();                                                   // Détermine le chemin entre l'entrée et la salle finale
        this.m = new Map(this._map, this.tRoom);
    }
    
    private void generateRooms() {
        if(this._propHeightRoom == null && this._propWidthRoom == null) {
            for(int i = 0; i < this._numberRoom; i++) {
                //System.out.println("[Generating room " + (i+1) + " / " + this._numberRoom + "]");
                GeneratorRoom r = new GeneratorRoom(this._random.nextLong());
                if(r.isValid) {
                    this._rooms.add(r);
                }
                else
                    i--;
            }
        }
        else {
            for(int i = 0; i < this._numberRoom; i++) {
                GeneratorRoom r = new GeneratorRoom(this._random.nextLong(), 
                        this._propWidthRoom.x, this._propWidthRoom.y, 
                        this._propHeightRoom.x, this._propHeightRoom.y);
                if(r.isValid) {
                    this._rooms.add(r);
                }
                else
                    i--;
            }
        }
    }
        
    private Point sumSizeRooms(ArrayList<GeneratorRoom> rooms) {
        int height = 0;
        int width = 0;
        for(int i = 0; i < rooms.size(); i++) {
            height += rooms.get(i)._height;
            width += rooms.get(i)._width;
        }
        return new Point(width, height);
    }
    
    private void placeRoom(char[][] map, GeneratorRoom r, int x_ori, int y_ori) {
        for(int y = 0; y < r._map.length; y++) {
            for(int x = 0; x < r._map[y].length; x++) {
                if(r._map[y][x] != '#') {
                    map[y + y_ori][x + x_ori] = r._map[y][x];
                }
            }
        }
        r.setOriginXY(x_ori, y_ori);
    }
    
    private void reduceMap(char[][] map) {     
        int heightMinRoom = -1;
        int heightMaxRoom = map.length;
        int widthMinRoom = -1;
        int widthMaxRoom = -1;
        
        for(int y = 0; y < map.length; y++) {
            for(int x = 0; x < map[y].length; x++) {
                if(map[y][x] == 'W') {
                    if(heightMinRoom == -1)
                        heightMinRoom = y;
                    if(widthMinRoom == -1)
                        widthMinRoom = x;
                    heightMaxRoom = y;
                    if(x < widthMinRoom) widthMinRoom = x;
                    if(x > widthMaxRoom) widthMaxRoom = x;
                }
            }
        }
        heightMinRoom = heightMinRoom == 0 ? 0 : heightMinRoom - 1;
        widthMinRoom = widthMinRoom == 0 ? 0 : widthMinRoom - 1;
        heightMaxRoom = heightMaxRoom >= (map.length - 1) ? map.length - 1 : heightMaxRoom + 1;
        widthMaxRoom = widthMaxRoom >= (map[0].length - 1) ? map[0].length - 1 : widthMaxRoom + 1;
        this._map = new char[heightMaxRoom - heightMinRoom + 1][widthMaxRoom - widthMinRoom + 1];
        for(int y = heightMinRoom; y <= heightMaxRoom; y++) {
            for(int x = widthMinRoom; x <= widthMaxRoom; x++) {
                this._map[y - heightMinRoom][x - widthMinRoom] = map[y][x];
            }
        }
    }
    
    private void cleanMap() {
        // Je retire les murs qui n'ont pas lieu d'être et si un sol est mal placé
        for(int y = 1; y < this._map.length - 1; y++) {
            for(int x = 1; x < this._map[y].length - 1; x++) {
                if(this._map[y][x] == 'W') {
                    char[][] square = new char[3][3];
                    for(int i = 0; i < 3; i++)
                        for(int j = 0; j < 3; j++)
                            square[i][j] = this._map[y + (-1 + i)][x + (-1 + j)];
                    Square preUpdate = new Square(square);
                    square[1][1] = ' ';
                    Square postUpdate = new Square(square);
                    if(Square.compareSquare(preUpdate, postUpdate)) {
                        if(Utility.checkXorYSurrondings(this._map, x, y, ' '))
                            this._map[y][x] = ' ';
                        else
                            this._map[y][x] = '#';
                    }
                }
            }
        }
    }
    
    private void createRooms() {        
        Dijkstra d = new Dijkstra(this._map);
        d.addRule(new Rule(' ', 'W', true, true));
        d.addRule(new Rule(' ', ' ', true, false));
        d.createNodes(true);
        Node[] nodes = d.getNodes();
        this.tRoom = new ArrayList<>();
        for(int i = 0; i < nodes.length; i++) {
            if(nodes[i] != null && !nodes[i].fetched) {
                int xPos = Dijkstra.XValue(i, this._map[0].length);
                int yPos = Dijkstra.YValue(i, this._map[0].length);
                ArrayList<Point> wall = new ArrayList<>();
                ArrayList<Point> ground = new ArrayList<>();
                ground.add(new Point(xPos, yPos));
                fetchNode(nodes[i], nodes, ground, wall);

                this.tRoom.add(new Room(this._map, ground, wall, this._random));
            }
        }
    }
    
    private void fetchNode(Node origin, Node[] nodes, ArrayList<Point> ground, ArrayList<Point> wall) {
        origin.fetched = true;
        for(int i = 0; i < origin.neighbors.size(); i++) {
            Point p = new Point(Dijkstra.XValue(origin.neighbors.get(i), this._map[0].length), 
                                Dijkstra.YValue(origin.neighbors.get(i), this._map[0].length));
            if(this._map[p.y][p.x] == 'W') {
                wall.add(p);                
            }
            else if(this._map[p.y][p.x] == ' ' && !nodes[origin.neighbors.get(i)].fetched) {
                ground.add(p);
                fetchNode(nodes[origin.neighbors.get(i)], nodes, ground, wall);
            }
        }
    }
    
    private void connectRooms() {
        for(int y = 0; y < this._map.length; y++) {
            for(int x = 0; x < this._map[y].length; x++) {
                if(this._map[y][x] == 'W') {
                    int result = Utility.checkDiagonalsSurrondings(this._map, x, y, ' ');
                    switch(result) {
                        case 1:
                        case -1:
                            this._map[y][x] = this._map[y][x - 1] = this._map[y][x + 1] = ' ';
                            this._map[y + 1][x + (1 * result)] = this._map[y + 1][x + (2 * result)] = 'W';
                            this._map[y - 1][x + (-1 * result)] = this._map[y - 1][x + (-2 * result)] = 'W';
                            break;
                        /*case 2:
                            System.out.println("Implement 2 here");
                            this._map[y][x] = 'G';
                            Utility.displayEntity(this._map);
                            System.out.println("");
                            //throw new UnsupportedOperationException();
                            break;*/
                    }
                }
            }
        }
    }
    
    private void createEntrance() {
        Point indexRooms = new Point();
        int valueDistance = 0;
        for(int i = 0; i < this.tRoom.size(); i++) {
            for(int j = i + 1; j < this.tRoom.size(); j++) {
                Point p1 = this.tRoom.get(i).getFirstFloorTilesPosition();
                Point p2 = this.tRoom.get(j).getFirstFloorTilesPosition();
                if(p1.distance(p2.x, p2.y) > valueDistance) {
                    indexRooms.x = i;
                    indexRooms.y = j;
                }
            }
        }
        this.tRoom.get(indexRooms.x).isEntry = true;
        this.tRoom.get(indexRooms.x).isExit = true;
    }

    private void boundRooms() {
        for(int i = 0; i < this.tRoom.size(); i++) {
            Room r1 = this.tRoom.get(i);
            for(int j = i + 1; j < this.tRoom.size(); j++) {
                Room r2 = this.tRoom.get(j);
                ArrayList<Point> commonCoords = Utility.commonCoords(r1.getBordersWithoutAngles(),
                                                                       r2.getBordersWithoutAngles());
                if(commonCoords.size() > 0) {
                    r1.addNeighboorRoom(r2);
                    r2.addNeighboorRoom(r1);
                }
            }
            // @TODO
            // Faire en sorte que les deux mêmes salles ne soient pas connectés, pour cela il suffira
            // de regarder deux choses au début : si la salle courante ne possède qu'une connection, il faut voir
            // si les connection de la connection possède plus de 2 connexions. Si non créer un chemin vers la room la plus
            // proche. Néanmoins il sera possible par la suite que trois salles soient isolés dans un coin
            // et possèdent donc chacune 2 connections sans jamais rejoindre le chemin principale
            // il faudra donc vérifier une fois le chemin principale fait si des salles ne sont pas isolés 
            // dans l'arbre de parcours finale
            if(r1.getConnections().isEmpty()) { // Si une salle ne possède pas de voisin direct
                char[][] copyMap = new char[this._map.length][this._map[0].length];
                Utility.fillArray(copyMap, '#');
                for(int j = 0; j < this.tRoom.size(); j++) {
                    if(this.tRoom.get(j) != r1) {
                        ArrayList<Point> points = this.tRoom.get(j).getBordersWithoutAngles();
                        for(int k = 0; k < points.size(); k++) {
                            copyMap[points.get(k).y][points.get(k).x] = 'W';
                        }
                    }
                }
                copyMap[r1.origin.y][r1.origin.x] = 'O';
                Dijkstra dis = new Dijkstra(copyMap);
                dis.addRule(new Rule('O', '#', true, false));
                dis.addRule(new Rule('O', 'W', true, false));
                dis.addRule(new Rule('#', '#', true, false));
                dis.addRule(new Rule('#', 'W', true, false));
                dis.addRule(new Rule('W', '#', true, false));
                dis.createNodes(false);
                Point destination = dis.findFirst(r1.origin, 'W');
                Room r2 = null;
                for(int j = 0; j < this.tRoom.size(); j++) {
                    if(this.tRoom.get(j).getAllPoints().contains(destination)) {
                        r2 = this.tRoom.get(j);
                        break;
                    }
                }
                if(r2 != null)
                    connectTwoRooms(this.tRoom.get(i), r2, destination);
            }
        }
    }
    
    private void connectTwoRooms(Room r1, Room r2, Point destination) {
        char[][] tempMap = new char[this._map.length][this._map[0].length];
        Utility.fillArray(tempMap, '#');
        Dijkstra d = new Dijkstra(tempMap);
        d.addRule(new Rule('#', '#', true, false));
        d.createNodes(false);
        ArrayList<Point> corridor = d.shortestPathFromTo(r1.origin, destination);
        if(corridor != null) {
            for(int k = 0; k < corridor.size(); k++) {
                Point aCor = corridor.get(k);
                this._map[aCor.y][aCor.x] = ' ';
                if((aCor.x - 1) >= 0 && this._map[aCor.y][aCor.x - 1] != ' ') 
                    this._map[aCor.y][aCor.x - 1] = 'W';
                if((aCor.x + 1) < this._map[aCor.y].length && this._map[aCor.y][aCor.x + 1] != ' ') 
                    this._map[aCor.y][aCor.x + 1] = 'W';
                if((aCor.y - 1) >= 0 && this._map[aCor.y - 1][aCor.x] != ' ') 
                    this._map[aCor.y - 1][aCor.x] = 'W';
                if((aCor.y + 1) < this._map.length && this._map[aCor.y + 1][aCor.x] != ' ') 
                    this._map[aCor.y + 1][aCor.x] = 'W'; 
            }
            // @TODO : A optimiser ci-dessous, voir cas génération avec 1, une porte se retrouve au milieu de tout
            this._map[corridor.get(corridor.size() - 1).y][corridor.get(corridor.size() - 1).x] = 'D';
            r1.addNeighboorRoom(r2);
            r2.addNeighboorRoom(r1);
        }
    }
    
    private void entryToExit() {
        
    }
    
    public Map getMap() {
        return this.m;
    }
}
