/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kamigaku.dungeongenerator.dijkstra;

/**
 *
 * @author stagiaire
 */
public class Rule {
    
    private final Object _source;
    private final Object _neighbor;
    private final boolean _line;
    private final boolean _diagonal;
    
    public Rule(Object source, Object neighbor, boolean line, boolean diagonal) {
        this._line = line;
        this._diagonal = diagonal;
        this._source = source;
        this._neighbor = neighbor;
    }
    
    public boolean isValid(Object source, Object neighbor, boolean line) {
        if(source == this._source && neighbor == this._neighbor) {
            if(line)
                return _line;
            else
                return _diagonal;
        }
        return false;
    }
    
    public Object getSource() {
        return this._source;
    }
    
}
