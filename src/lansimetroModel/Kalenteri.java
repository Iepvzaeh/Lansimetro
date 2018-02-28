package lansimetroModel;

import java.util.Calendar;

/**
 *
 * @author Antti Nieminen, Antti Käyhkö, Anssi Chamorro, Heikki Tanttu
 */
public class Kalenteri {

    private Calendar kalenteri = Calendar.getInstance();

    public Kalenteri() {
        this.kalenteri.set(2017, 9, 10);
    }
    
    public void etenePäivä() {
        kalenteri.set(Calendar.DAY_OF_YEAR, kalenteri.get(Calendar.DAY_OF_YEAR) + 1);
    }

    public String tulostaPäivä() {
        String kuukausi = "";
        int päivä = kalenteri.get(Calendar.DAY_OF_MONTH);
        int vuosi = kalenteri.get(Calendar.YEAR);

        switch (kalenteri.get(Calendar.MONTH)) {
            case 0:
                kuukausi = "Tammikuu";
                break;
            case 1:
                kuukausi = "Helmikuu";
                break;
            case 2:
                kuukausi = "Maaliskuu";
                break;
            case 3:
                kuukausi = "Huhtikuu";
                break;
            case 4:
                kuukausi = "Toukokuu";
                break;
            case 5:
                kuukausi = "Kesäkuu";
                break;
            case 6:
                kuukausi = "Heinäkuu";
                break;
            case 7:
                kuukausi = "Elokuu";
                break;
            case 8:
                kuukausi = "Syyskuu";
                break;
            case 9:
                kuukausi = "Lokakuu";
                break;
            case 10:
                kuukausi = "Marraskuu";
                break;
            case 11:
                kuukausi = "Joulukuu";
                break;
        };
        return päivä + " " + kuukausi + " " + vuosi;
    }

}
