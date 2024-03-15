package meteodades;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.InvalidPathException;
import java.util.Scanner;

public class Comarques {

    public static File fileComarques;
    static Scanner scan = new Scanner(System.in);

    static {
        try {
            fileComarques = new File("comarques.bin");

        } catch (InvalidPathException ex) {
            System.out.println("Invalid file path: " + ex.getMessage());
        }
    }

    public static String trobar_Nom_comarque(int id) {

        int comarques_size = 24; //integer 4 bytes and 20 character 20 bytes 
        int position = (id - 1) * comarques_size;

        String nom_comarque;
        try (RandomAccessFile raf = new RandomAccessFile(Comarques.fileComarques, "r")) {
            raf.seek(position);
            raf.readInt();
            nom_comarque = raf.readUTF();
            return nom_comarque;
        } catch (IOException ex) {
            System.out.println("Error " + ex);
        }
        return null;
    }

    public static void llistarComarques() {

        try (RandomAccessFile raf = new RandomAccessFile(fileComarques, "r")) {
            String nom;
            int id;

            System.out.printf("%-5s %-40s%n", "ID", "NOM ");
            System.out.println("====================================================");

            int i = 1, Mida = 24, position;

            do {
                position = (i - 1) * Mida;

                raf.seek(position);
                id = raf.readInt();
                nom = raf.readUTF();
                i++;
                System.out.printf("%-5s %-40s%n", id, nom);

            } while (raf.getFilePointer() < raf.length());

        } catch (IOException ex) {
            System.out.println("Error " + ex);
        }

    }

    public static void dadesComarca() {

        String demana;
        String text; // nom_variable, unitat_variable, codi_variable, valor_lectura, text, nom_comarque, data_lectura;
        String[] valors; //data de estacio 
        String[] values = new String[8]; // values to show 

        System.out.println("Entrar un codi de comarca ");

        demana = scan.nextLine();
        if (Comarques.checkComarca(demana)) {
            System.out.println("Exist");

            File file = Comarques.create_bin_Comarque(values, demana);
            try (Scanner lectorFitxer = new Scanner(Estacions.fileEstacions)) {

                //skip first line 
                lectorFitxer.nextLine();
                while (lectorFitxer.hasNextLine()) {
                    text = lectorFitxer.nextLine();
                    valors = text.split(";");
                    //si es d aquest codi comarca
                    if (valors[3].equals(demana)) {
                        //codi estacio 
                        values[0] = valors[0];
                        //nom estacio 
                        values[1] = valors[1];
                        //nom_comarque = trobar_Nom_comarque(Integer.parseInt(valors[3]));
                        values[2] = trobar_Nom_comarque(Integer.parseInt(valors[3]));
                        //codi + nom + unitat ) var 
                        values[3] = "c_var";
                        values[4] = "nom_var";
                        values[6] = "uni_var";
                        //valor_lectura = dades_meteo_find(valors[0], 3);
                        values[5] = MeteoDades.dades_meteo_find(valors[0], 3);
                        //data_lectura = dades_meteo_find(valors[0], 2);
                        values[7] = MeteoDades.dades_meteo_find(valors[0], 2);
                        System.out.printf("%-5s %-20s %-20s %-15s %-15s %-15s %-15s %-15s%n ", values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
                        Menu.addValues(file, values);
                    }

                }

            } catch (IOException ex) {
                System.out.println("Error " + ex);
            }

        } else {
            System.out.println("Not found");
            Menu.menu();
        }
    }

    private static boolean checkComarca(String demana) {

        File fitxer = new File("comarques.bin");

        try (RandomAccessFile raf = new RandomAccessFile(fitxer, "r")) {
            System.out.println(raf.length());
            if (raf.length() / 24 < Integer.parseInt(demana)) {
                return false;
            } else {
                return true;
            }

        } catch (IOException ex) {
            System.out.println("Error " + ex);
        }

        return false;

    }

    private static File create_bin_Comarque(String[] values, String codi_comarque) {
        String fileName = "dades_comarque_" + codi_comarque + ".bin";
        int question;

        File file = new File(fileName);
        try {
            //check if we can create it 
            if (file.createNewFile()) {
                System.out.println("File created successfully: " + file.getAbsolutePath());
            } else {
                File exist = new File(fileName);
                System.out.println("File already exists.Wanna Replace it ? (0/1) true/false");
                question = scan.nextInt();
                //if there is another one ,want u change it 
                if (question == 0) {
                    if (exist.delete()) {
                        System.out.println("Replaced  ");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(" error a la creacio del file : " + e.getMessage());
        }
        return file;
    }

}
