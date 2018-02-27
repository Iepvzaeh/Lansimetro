/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lansimetroModel;

/**
 *
 * @author Antti Käyhkö
 */
import java.util.ArrayList;

public class Asema {

    private final String nimi;
    private ArrayList<KokoHairio> häiriöt;

    public String HäiriötToString() {
        String s = "";
        for (KokoHairio h : häiriöt) {
            s = s + h.getNimi() + "\n";
        }
        return s;
    }

    public KokoHairio getViimeisinHäiriö() {
        if (!häiriöt.isEmpty()) {
            return häiriöt.get(0);
        } else {
            return null;
        }
    }

    public boolean onkoAsemallaHäiriö(String häiriönNimi) {
        if (!häiriöt.isEmpty()) {
            for (KokoHairio h : this.häiriöt) {
                if (h.getNimi().equals(häiriönNimi)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<KokoHairio> getHäiriöt() {
        return häiriöt;
    }

    public Asema(String nimi) {
        this.nimi = nimi;
        häiriöt = new ArrayList<>();
    }

    public void lisääHäiriö(KokoHairio häiriö) {
        häiriöt.add(häiriö);
    }

    public boolean poistaHäiriö(KokoHairio häiriö) {
        return häiriöt.remove(häiriö);
    }

    public String getNimi() {
        return nimi;
    }

    @Override
    public String toString() {
        return nimi;
    }

}
