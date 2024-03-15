package meteodades;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

class Provincies {

    int codi_provincie;
    String nom_provincie;
}

public class Provincies {
    
    public static File fileProvincies = new File("provincies.csv");
    
     public static String trobar_Nom_provincia(int id) {

        try (Scanner lectorFitxer = new Scanner(fileProvincies)) {
            String text;
            String[] valors;
            boolean check = false;
            lectorFitxer.nextLine();
            while (lectorFitxer.hasNextLine() && !check) {
                text = lectorFitxer.nextLine();
                valors = text.split(";");

                if (Integer.parseInt(valors[0]) == id) {
                    check = true;
                    return valors[1];
                }
            }
        } catch (IOException ex) {
            System.out.println("Error " + ex);
        }
        return null;
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
