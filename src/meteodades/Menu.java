package meteodades;

import java.io.File;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;

public class Menu {

    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        menu();
    }

    public static void menu() {
        int value;

        System.out.println("1.Afegir nova estació meteorològica.");
        System.out.println("2.Llistar estacions meteorològiques");
        System.out.println("3.Llistar comarques.");
        System.out.println("4.Llistar variables.");
        System.out.println("5.Dades d’una estació");
        System.out.println("6.Dades d’una comarca.");
        System.out.println("7.Sortir.");

        value = scan.nextInt();
        scan.nextLine();

        switch (value) {
            case 1 ->
                Estacions.afegirNovaEstacioM();
            case 2 ->
                Estacions.llistarEstacionsM();
            case 3 ->
                Comarques.llistarComarques();
            case 4 ->
                Variables.llistarVariables();
            case 5 ->
                Estacions.dadesEstacio();
            case 6 ->
                Comarques.dadesComarca();
            default -> {
                if (value != 7) {
                    System.out.println("Valor not valid");
                    menu();
                }
            }

        }

    }

    public static boolean checkExist(String demana) {
        try (Scanner lectorFitxer = new Scanner(Estacions.fileEstacions)) {
            String text;
            String[] valors;
            boolean foundit = false;
            lectorFitxer.nextLine();
            while (lectorFitxer.hasNextLine() && foundit == false) {
                text = lectorFitxer.nextLine();
                valors = text.split(";");
                if (valors[0].equals(demana)) {
                    foundit = true;
                    return true;
                }
            }
        } catch (IOException ex) {
            System.out.println("Error in check " + ex);
        }
        return false;
    }

    
    public static void addValuesEstacio(Estacions myNewEstacio) {
        File fitxer1 = new File("estacions.csv");

        try (Scanner lectorFitxer = new Scanner(Estacions.fileEstacions)) {
            PrintStream escriptor = new PrintStream(new FileOutputStream(fitxer1, true));

            escriptor.print(myNewEstacio.codi_estacio + ";");
            escriptor.print(myNewEstacio.nom_estacio + ";");
            escriptor.print(myNewEstacio.nom_municipi + ";");
            escriptor.print(myNewEstacio.codi_comarca + ";");
            escriptor.print(myNewEstacio.codi_provincia + ";");
            escriptor.print(myNewEstacio.nom_estat + ";");
            escriptor.print(myNewEstacio.data_alta + ";");
            escriptor.print(myNewEstacio.data_baixa + ";");
            escriptor.println();

        } catch (IOException ex) {
            System.out.println("Error in adding values " + ex);
        }

    }

    public static String trobar_Nom_provincia(int id) {

        try (Scanner lectorFitxer = new Scanner(Provincies.fileProvincies)) {
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

    public static String llegirCaracters(RandomAccessFile raf, int i) throws IOException {

        String text = "";

        for (int j = 0; j < i; j++) {
            text += raf.readChar();
        }
        return text;
    }

    public static void addValues(File file, String[] values) {

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {

            raf.seek(raf.length());

            for (String value : values) {
                raf.writeUTF(value);
            }

        } catch (IOException ex) {
            System.out.println("Error " + ex);
        }

    }

}
