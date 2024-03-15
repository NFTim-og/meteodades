package meteodades;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Scanner;

public class MeteoDades {

    static Scanner scan = new Scanner(System.in);
    public static File fileDades;

    static {
        try {
            fileDades = new File("dades_meteo.csv");

        } catch (InvalidPathException ex) {
            System.out.println("Invalid file path: " + ex.getMessage());
        }
    }
    
    public static String dades_meteo_find(String codi, int posicio) {

        try (Scanner lectorFitxer = new Scanner(fileDades)) {

            boolean findit = false;

            String text, data_lectura = null;

            String[] valors;
            //skip first line 
            lectorFitxer.nextLine();
            while (lectorFitxer.hasNextLine() && !findit) {
                text = lectorFitxer.nextLine();
                valors = text.split(";");

                if (valors[0].equals(codi)) {
                    data_lectura = valors[posicio];
                    findit = true;
                }
            }
            return data_lectura;
        } catch (IOException ex) {
            System.out.println("Error " + ex);
        }
        return null;
    }
    
}
