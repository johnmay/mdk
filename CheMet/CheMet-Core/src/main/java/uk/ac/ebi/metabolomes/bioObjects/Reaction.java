package uk.ac.ebi.metabolomes.bioObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reaction {

    private HashMap<String, Float> reactants;
    private List<Citation> cits;
    private boolean reversible;

    public Reaction() {
        reactants = new HashMap<String, Float>();
        this.reversible = false;
        cits = new ArrayList<Citation>();
    }

    public void addReactant(String name, Float stech) {
        if (stech.floatValue() > 0) {
            stech = Float.valueOf(stech * -1);
        }
        //name = removeExtraCharacters(name);
        reactants.put(name, stech);
    }

    public void addProduct(String name, Float stech) {
        //name = removeExtraCharacters(name);
        reactants.put(name, stech);
    }

    public void setReversible() {
        this.reversible = true;
    }

    public boolean isReversible() {
        return this.reversible;
    }

    public HashMap<String, Float> getReactantsProducts() {
        return reactants;
    }

    public ArrayList<String> getMetabolites() {
        ArrayList<String> ans = new ArrayList<String>();
        for (String a : reactants.keySet()) {
            ans.add(a);
        }
        return ans;
    }

    private String removeExtraCharacters(String s) {
        if (s.charAt(s.length() - 1) == ' ') {
            s = s.substring(0, s.length() - 1);
        }
        if (s.charAt(s.length() - 1) == ',') {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public boolean addCitations(List<Citation> cits) {
        this.cits = cits;
        return true;
    }

    public List<Citation> getCitations() {
        return this.cits;
    }

    @Override
    public String toString() {
        String react = "";
        String prod = "";
        for (String metabName : this.getReactantsProducts().keySet()) {
            if (this.getReactantsProducts().get(metabName) < 0) {
                if (react.length() > 0) {
                    react += " + ";
                }
                react += this.getReactantsProducts().get(metabName) + " " + metabName;
            } else {
                if(prod.length() >0) {
                    prod += " + ";
                }
                prod += this.getReactantsProducts().get(metabName) + " " + metabName;
            }
        }

        return react + " = " + prod;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Reaction other = (Reaction) obj;

        for (String otherReactant : other.reactants.keySet()) {
            if(!this.reactants.containsKey(otherReactant))
                return false;
        }
        for (String thisReactant : this.reactants.keySet()) {
            if(!other.reactants.containsKey(thisReactant))
                return false;
        }
        /*if (this.reactants != other.reactants && (this.reactants == null || !this.reactants.equals(other.reactants))) {
            return false;
        }*/
        if (this.reversible != other.reversible) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.reactants != null ? this.reactants.hashCode() : 0);
        hash = 13 * hash + (this.reversible ? 1 : 0);
        return hash;
    }


}
