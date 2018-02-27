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
public class Hairio {

    private final int hinta;
    private final String häiriöNimi;
    private final String kuvaus;
    private int päivätHoitamiseen;
    private final String ratkaisuKuvaus;
    private final boolean keskeyttääKoeajon;
    private final boolean poistaaKokoHäiriön;
    private boolean hoidettavana;
    private boolean valittu;

    public Hairio(int hinta, String häiriöNimi, String kuvaus, int päivätHoitamiseen, String ratkaisuKuvaus, boolean keskeyttääKoeajon, boolean poistaaKokoHäiriön) {
        this.hinta = hinta;
        this.häiriöNimi = häiriöNimi;
        this.kuvaus = kuvaus;
        this.päivätHoitamiseen = päivätHoitamiseen;
        this.ratkaisuKuvaus = ratkaisuKuvaus;
        this.keskeyttääKoeajon = keskeyttääKoeajon;
        this.poistaaKokoHäiriön = poistaaKokoHäiriön;
        this.hoidettavana = false;
        this.valittu = false;
    }

    public Hairio(Hairio h) { // luodaan kopio häiriöstä
        this.hinta = h.hinta;
        this.häiriöNimi = h.häiriöNimi;
        this.kuvaus = h.kuvaus;
        this.päivätHoitamiseen = h.päivätHoitamiseen;
        this.ratkaisuKuvaus = h.ratkaisuKuvaus;
        this.keskeyttääKoeajon = h.keskeyttääKoeajon;
        this.poistaaKokoHäiriön = h.poistaaKokoHäiriön;
        this.hoidettavana = false;
        this.valittu = false;
    }

    public boolean isPoistaaKokoHäiriön() {
        return poistaaKokoHäiriön;
    }

    public boolean isValittu() {
        return valittu;
    }

    public void setValittu(boolean valittu) {
        this.valittu = valittu;
    }

    public boolean isKeskeyttääKoeajon() {
        return keskeyttääKoeajon;
    }

    public boolean isHoidettavana() {
        return hoidettavana;
    }

    public void setHoidettavana(boolean hoidettavana) {
        this.hoidettavana = hoidettavana;
    }

    public void vähennäPäiviä() {
        this.päivätHoitamiseen = this.päivätHoitamiseen - 1;
    }

    public String getRatkaisuKuvaus() {
        return ratkaisuKuvaus;
    }

    public int getPäivätHoitamiseen() {
        return päivätHoitamiseen;
    }

    public void setPäiväthoitamiseen(int päiväthoitamiseen) {
        this.päivätHoitamiseen = päiväthoitamiseen;
    }

    public int getHinta() {
        return hinta;
    }

    public String getHäiriöNimi() {
        return häiriöNimi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

}
