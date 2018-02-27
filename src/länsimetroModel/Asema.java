/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package länsimetroModel;

/**
 *
 * @author Antti Käyhkö
 */
import java.util.ArrayList;

public class Asema {

    private final String nimi;
    private ArrayList<KokoHäiriö> häiriöt;

    public String HäiriötToString() {
        String s = "";
        for (KokoHäiriö h : häiriöt) {
            s = s + h.getNimi() + "\n";
        }
        return s;
    }

    public KokoHäiriö getViimeisinHäiriö() {
        if (!häiriöt.isEmpty()) {
            return häiriöt.get(0);
        } else {
            return null;
        }
    }

    public boolean onkoAsemallaHäiriö(String häiriönNimi) {
        if (!häiriöt.isEmpty()) {
            for (KokoHäiriö h : this.häiriöt) {
                if (h.getNimi().equals(häiriönNimi)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<KokoHäiriö> getHäiriöt() {
        return häiriöt;
    }

    public Asema(String nimi) {
        this.nimi = nimi;
        häiriöt = new ArrayList<>();
    }

    public void lisääHäiriö(KokoHäiriö häiriö) {
        häiriöt.add(häiriö);
    }

    public boolean poistaHäiriö(KokoHäiriö häiriö) {
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
