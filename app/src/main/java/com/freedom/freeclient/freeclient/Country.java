package com.freedom.freeclient.freeclient;

/**
 * Created by kemihambolu on 1/23/15.
 */
public class Country {
    private String name;
    private String id;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return 47 * name.hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if(!(o instanceof Country)){
            return false;
        }
        Country other = (Country) o;
        return this.id == other.id && this.name.equalsIgnoreCase(other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
