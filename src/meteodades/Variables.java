package meteodades;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.InvalidPathException;

class Variable {
    int id;
    String nom, unitat_de_mesura;
}

public class Variables {

    public static File fileVariables;

    static {
        fileVariables = new File("variables.bin");
    }

    public static void llistarVariables() {

        try (RandomAccessFile raf = new RandomAccessFile(fileVariables, "r")) {
            String nom, unitat_de_mesura;
            int id;
            long mida;
            System.out.printf("%-5s %-40s %-20s%n", "ID", "NOM ", "UNITAT DE MESURA ");
            System.out.println("==========================================================================================");
            mida = raf.length();
            while (raf.getFilePointer() < mida) {

                id = raf.readInt();

                nom = Menu.llegirCaracters(raf, 50);
                unitat_de_mesura = Menu.llegirCaracters(raf, 5);

                System.out.printf("%-5s %-40s %-20s%n", id, nom, unitat_de_mesura);
            }

        } catch (IOException ex) {
            System.out.println("Error hey" + ex);
        }
    }

    public static Variable getVariables(RandomAccessFile raf) throws IOException {
        Variable myVar = new Variable();

        myVar.id = raf.readInt();

        myVar.nom = Menu.llegirCaracters(raf, 50);
        myVar.unitat_de_mesura = Menu.llegirCaracters(raf, 5);

        myVar.id = raf.readInt();

        return myVar;
    }
}
