
package länsimetroModel;

/**
 *
 * @author Hodor
 */
public class Koeajo {
    
    private boolean käynnissä = false;
    private int päiviäJäljellä = 14;

    public Koeajo() {
    }
    
    public void käynnistä() {
        käynnissä = true;        
    }
    
    public void keskeytä() {
        käynnissä = false;
        päiviäJäljellä = 14;
    }
    
    public boolean isKäynnissä() {
        return käynnissä;
    }
    
    public int getPäiviäJäljellä() {
        return päiviäJäljellä;
    }
    
    public void etenePäivä() {
        päiviäJäljellä--;
    }
    
}
