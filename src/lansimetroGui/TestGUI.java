package lansimetroGui;

import lansimetroModel.Hairio;
import lansimetroModel.KokoHairio;
import lansimetroModel.Asema;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import lansimetroController.Kontrolleri;

/**
 *
 * @author Antti Nieminen, Antti Käyhkö, Anssi Chamorro, Heikki Tanttu
 */
public class TestGUI {

    private Kontrolleri kontrolleri;

    public TestGUI() {
    }

    public void rekisteröiKontrolleri(Kontrolleri kontrolleri) {
        this.kontrolleri = kontrolleri;
    }

    public String listaaAsematJaHäiriöt() {
        StringBuilder sb = new StringBuilder();
        for (Asema asema : kontrolleri.getAsemat()) {
            int onkoAsemallaHäiriöitä = kontrolleri.tarkastaOnkoAsemallaHäiriöitä(asema);
            sb.append("<html>");

            switch (onkoAsemallaHäiriöitä) {
                case 0:
                    sb.append("<span style=\"color:green\">");
                    break;
                case 1:
                    sb.append("<span style=\"color:yellow\">");
                    break;
                case 2:
                    sb.append("<span style=\"color:red\">");
                    break;
            }

            sb.append(asema.getNimi());
            sb.append("</span>");
            sb.append("</html>");
            sb.append("\n");

            if (onkoAsemallaHäiriöitä == 0) {
                sb.append("     Ei häiriöitä!");
                sb.append("\n");
            } else {
                for (KokoHairio kokohäiriö : asema.getHäiriöt()) {
                    sb.append("     Häiriö: ").append(kokohäiriö.getNimi());

                    int montakoPäivääHoidettavana = kontrolleri.tarkistaOnkoKokoHäiriöHoidossa(kokohäiriö);
                    if (montakoPäivääHoidettavana > 0) {
                        sb.append(" (Hoidossa: " + montakoPäivääHoidettavana + " pv jäljellä)");
                    }

                    sb.append("\n");
                }
            }

        }
        return sb.toString();
    }

    public void pääIkkuna() {

        Object[] vaihtikset = {"Odota päivä", "Odota viikko", "Lopeta", "Lisää Tapiolaan häiriö"};

        int n;
        OUTER:
        while (true) {
            n = JOptionPane.showOptionDialog(null,
                    kontrolleri.tulostaPäivä() + "\nKäytetyt rahat: " + kontrolleri.tulostaRahat() + " €\n\n" + listaaAsematJaHäiriöt(),
                    "Länsimetrosimulaattori",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    vaihtikset,
                    vaihtikset[vaihtikset.length - 1]);
            switch (n) {
                case 0:
                    kontrolleri.etenePäivä();
                    break;
                case 1:
                    kontrolleri.eteneviikko();
                    break;
                case 2:
                    break OUTER;
                case 3:
                    
                    break;
                default:
                    break;
            }
        }
        System.exit(0);
    }

    public void tulostaRatkaisu(Asema asema, Hairio häiriö) {
        JOptionPane.showMessageDialog(null, häiriö.getRatkaisuKuvaus());
    }

    public void tulostaUusiHäiriö(Asema asema, KokoHairio kokohäiriö) {
        ArrayList<Hairio> häiriöt = kokohäiriö.getKokoHäiriölista();

        Object[] vaihtikset = new Object[häiriöt.size()];

        for (int i = 0; i < häiriöt.size(); i++) {
            vaihtikset[i] = häiriöt.get(i).getHäiriöNimi();
        }

        int n;

        OUTER:
        while (true) {
            n = JOptionPane.showOptionDialog(null,
                    kokohäiriö.getKuvaus() + "\n" + listaaHäiriönRatkaisuVaihtoehdot(kokohäiriö),
                    "Uusi häiriö asemalla " + asema,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    vaihtikset,
                    vaihtikset[vaihtikset.length - 1]);
            if (häiriöt.get(n).getPäivätHoitamiseen() <= 0) {
                tulostaRatkaisu(asema, häiriöt.get(n));
                if (häiriöt.get(n).isPoistaaKokoHäiriön()) {
                    kontrolleri.poistaAsemaltaHäiriö(asema, kokohäiriö);
                }
            } else {
                kontrolleri.teeHoidettavaksi(häiriöt.get(n));
            }
            kontrolleri.lisääRaha(häiriöt.get(n).getHinta());
            break;
        }
        // System.exit(0);
    }

    

    public String listaaHäiriönRatkaisuVaihtoehdot(KokoHairio kokohäiriö) {
        StringBuilder sb = new StringBuilder();
        sb.append("Vaihtoehdot:").append("\n\n");
        for (Hairio häiriö : kokohäiriö.getKokoHäiriölista()) {
            sb.append(häiriö.getHäiriöNimi()).append("\n");
            sb.append("Maksaa: ").append(häiriö.getHinta()).append("\n");
            sb.append("Keskeyttää koeajon: ").append(häiriö.isKeskeyttääKoeajon()).append("\n");
            if (häiriö.getPäivätHoitamiseen() == -1) {
                sb.append("Häiriö jatkuu toistaiseksi.").append("\n");
            } else if (häiriö.getPäivätHoitamiseen() == 0) {
                sb.append("Häiriö poistuu samantien.").append("\n");
            } else {
                sb.append("Hoitaminen kestää: ").append(häiriö.getPäivätHoitamiseen()).append(" päivää").append("\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
