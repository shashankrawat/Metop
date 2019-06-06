package com.elintminds.mac.metatopos;

import java.util.ArrayList;

public class Model {

    public enum STATE {
        CLOSED,
        OPENED
    }

    public String name;
    public int level;
    public STATE state = STATE.CLOSED;
    public String designation;
    public ArrayList<Model> models = new ArrayList<>();

    public Model(String name) {
        this.name = name;

    }

}
