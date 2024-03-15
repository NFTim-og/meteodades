package meteodades;

import static meteodades.Variables.fileVariables;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

class Estacions {
    int codi_comarca, codi_provincia;
    String codi_estacio, nom_estacio, nom_municipi, nom_estat, data_alta, data_baixa;
    public static File fileEstacions;

    static {
        try {
            fileEstacions = new File("estacions.csv");

        } catch (InvalidPathException ex) {
            System.out.println("Invalid file path: " + ex.getMessage());
        }
    }

    static Scanner scan = new Scanner(System.in);

    public static void afegirNovaEstacioM() {

        Estacions myNewEstacio = new Estacions();

        System.out.println("Escriu el codi de la nova estació");
        myNewEstacio.codi_estacio = scan.nextLine();
        System.out.println("Escriu el nom de l'estació");
        myNewEstacio.nom_estacio = scan.nextLine();
        System.out.println("Escriu el nom del municipi");
        myNewEstacio.nom_municipi = scan.nextLine();
        System.out.println("Enter codi comarque");
        myNewEstacio.codi_comarca = scan.nextInt();
        System.out.println("Enter codi provincia ");
        myNewEstacio.codi_provincia = scan.nextInt();
        scan.nextLine();
        System.out.println("Enter nom estat ");
        myNewEstacio.nom_estat = scan.nextLine();
        System.out.println("Enter data alta");
        myNewEstacio.data_alta = scan.nextLine();
        System.out.println("Enter data baixa");
        myNewEstacio.data_baixa = scan.nextLine();

        if (!Menu.checkExist(myNewEstacio.codi_estacio)) {
            Menu.addValuesEstacio(myNewEstacio);
            System.out.println("Values added successfully ");
        } else {
            System.out.println("Already exist");
        }

    }

    public static void llistarEstacionsM() {

        try (Scanner lectorFitxer = new Scanner(fileEstacions)) {

            System.out.printf("%-5s %-40s %-20s %-15s %-25s%n", "CODI", "NOM ESTACIÓ", "NOM COMARCA", "NOM PROVINCIA ", "DATA ALTA");
            System.out.println("============================================================================================================================");
            String text, nom_comarque, nom_municipi;

            String[] valors;
            //skip first line 
            lectorFitxer.nextLine();
            while (lectorFitxer.hasNextLine()) {

                text = lectorFitxer.nextLine();
                valors = text.split(";");

                if ("Operativa".equals(valors[5])) {

                    nom_comarque = Comarques.trobar_Nom_comarque(Integer.parseInt(valors[3]));
                    nom_municipi = Provincies.trobar_Nom_provincia(Integer.parseInt(valors[4]));

                    // codi + n estacio + n comarca + n provincia + data alta 
                    System.out.printf("%-5s %-40s  %-20s %-15s %-25s%n", valors[0], valors[1], nom_comarque, nom_municipi, valors[6]);

                }
            }

        } catch (IOException ex) {
            System.out.println("Error " + ex);
        }

    }

    public static void dadesEstacio() {

        String demana, text; // nom_variable, unitat_variable, codi_variable, valor_lectura, text, nom_comarque, data_lectura;
        String[] valors; //data de estacio 
        String[] values = new String[8]; // values to show 
        System.out.println("Escriu el codi de l'estació per veure les seves dades");
        demana = scan.nextLine();
        boolean found = false;
        if (Menu.checkExist(demana)) {
            System.out.println("yes exist ");

            try (Scanner lectorFitxer = new Scanner(fileEstacions)) {

                System.out.printf("%-5s %-40s %-20s %-15s %-25s%n", "CODI", "NOM ESTACIÓ", "NOM COMARCA", "NOM PROVINCIA ", "DATA ALTA");
                System.out.println("============================================================================================================================");
                //skip first line 
                lectorFitxer.nextLine();
                Variable myVariables = new Variable();
                int position = 0;
                while (lectorFitxer.hasNextLine() && !found) {
                    text = lectorFitxer.nextLine();

                    valors = text.split(";");
                    //si es aquest codi d estacio 
                    if (valors[0].equals(demana)) {
                        //codi estacio 
                        values[0] = valors[0];
                        //nom estacio 
                        values[1] = valors[1];
                        //nom_comarque = trobar_Nom_comarque(Integer.parseInt(valors[3]));
                        values[2] = Comarques.trobar_Nom_comarque(Integer.parseInt(valors[3]));
                        //codi + nom + unitat ) var 

                        try (RandomAccessFile raf = new RandomAccessFile(Variables.fileVariables, "r")) {
                            myVariables = Variables.getVariables(raf);

                        } catch (IOException ex) {
                            System.out.println("Error hey" + ex);
                        }

                        values[3] = Integer.toString(myVariables.id) ;
                        values[4] = myVariables.nom;
                        values[6] = myVariables.unitat_de_mesura;
                        //valor_lectura = dades_meteo_find(valors[0], 3);
                        values[5] = MeteoDades.dades_meteo_find(valors[0], 3);
                        //data_lectura = dades_meteo_find(valors[0], 2);
                        values[7] = MeteoDades.dades_meteo_find(valors[0], 2);

                        System.out.printf("%-5s %-20s %-20s %-15s %-15s %-15s %-15s %-15s%n ", values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]);
                        found = true;
                        create_bin_Estacio(values);
                        position++;
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

    private static void create_bin_Estacio(String[] values) {
        String fileName = "dades_estacio_" + values[0] + ".bin";

        try {
            File file = new File(fileName);
      
            file.createNewFile();
                System.out.println("Fitxer creat correctament: " + file.getAbsolutePath());
                Menu.addValues(file, values);
            
        } catch (IOException e) {
            System.err.println("Error de creació del fitxer: " + e.getMessage());
        }
    }
}
