package ie.gmit.sw;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.function.UnaryOperator;


/**
 *  This is main menu class. It use enum CipherTypes to communicate with Cipher instances.
 *
 *
 * @by Mindugas Sharskus
 * @date 18/03/2018
 */
public class Menu {

    static Scanner in = new Scanner(System.in);

    private Cipher cipher;
    private CipherTypes cipherType;
    private String inFile = "";
    private String enFile = "";
    private String deFile = "";
    private String inURL = "";

    private String banner;
    String bannerSetup;

    /**
     * Private constructor used for factory method.
     *
     * @param cipher instance of cipher to be used in program.
     */
    private Menu(Cipher cipher){
        this.cipher = cipher;
        this.cipherType = CipherTypes.typeOf(cipher);
        this.banner = "\n" +
                "===============================\n" +
                String.format("=   %-26s=\n", this.cipherType.toString()) +
                "===============================\n";

        this.bannerSetup = this.banner +
                "=   SETUP                     =\n"+
                "===============================\n";
    }

    /**
     * Factory method for Menu class.
     *
     * @param cipher is used to encrypt/decrypt and setup of cipher (mutates).
     * @return Menu class instance.
     */
    public static Menu of(Cipher cipher){
        return new Menu(cipher);
    }

    /**
     * Starting point of Menu class. Shows Main menu.
     */
    public void show(){
        Menu.mainMenu(this);
    }

    /**
     * This method handles Main menu.
     *
     * @param menu Menu instance (dependency injection).
     */
    static void mainMenu(Menu menu){
        int choice;

        do{
            System.out.println(menu.banner +
                    "\n"+
                    "[1] - Setup\n"+
                    "[2] - Encrypt\n"+
                    "[3] - Decrypt\n"+
                    "-------------------\n"+
                    "File: "+ menu.inFile +" \n"+
                    "Cipher: "+ menu.cipherType +"\n"+
                    "-------------------\n"+
                    "[0] - Exit\n"+
                    "\nEnter your choice: "
            );
            choice = in.nextInt();
            mainMenu_Do(choice, menu);
        }while(choice != 0);
    }

    /**
     * This method is "mainMenu()" method logic
     *
     * @param choice menu choice.
     * @param menu Menu instance (dependency injection).
     */
    private static void mainMenu_Do(int choice, Menu menu) {
        switch (choice){
            case 1: setupMainMenu(menu); break;
            case 2: xCryptionMainMenu(CipherAction.ENCRYPT, menu); break;
            case 3: xCryptionMainMenu(CipherAction.DECRYPT, menu); break;
            case 0: System.out.println("Bye bye!"); break;
            default:
                System.out.println("Unknown choice: "+ choice);
        }
    }

    /**
     * This method handles Setup menu.
     *
     * @param menu Menu instance (dependency injection).
     */
    private static void setupMainMenu(Menu menu){
        int choice;
        do{
            System.out.println( menu.bannerSetup +
                    "\n"+
                    "[1] - Source(file/url):\n" +
                    "-------------------\n"+
                    "PLEASE NOTE:\n" +
                    "Encryption output file is same as Decryption input file.\n" +
                    "Decryption input file WILL BE OVERWRITTEN then encrypting file.\n"+
                    "\n\t - Input: "+ menu.inFile +
                    "\n\t - Encrypted: "+ menu.enFile +
                    "\n\t - Decrypted: "+menu.deFile+
                    "\n\t - URL: "+ menu.inURL +"\n"+
                    menu.cipherType.getSetupMenu() +
                    "-------------------\n"+
                    "[0] - Go back\n\n"
            );
            choice = in.nextInt();
            setupMainMenu_Do(choice, menu);
        }while (choice != 0);
    }

    /**
     * This method is "setupMainMenu()" method logic.
     *
     * @param choice menu choice.
     * @param menu Menu instance (dependency injection).
     */
    private static void setupMainMenu_Do(int choice, Menu menu) {
        if( choice == 1 ){ sourceMenu(menu); }
        else if( choice != 0 ){ menu.cipherType.setupMenu(choice, menu); }
    }

    private static void sourceMenu(Menu menu) {
        int choice;
        do{
            System.out.println( menu.bannerSetup +
                    "=   Source                    =\n" +
                    "===============================\n" +
                    "\n"+
                    "[1] - Pick input file.\n" +
                    "[2] - Pick output/input file (Encrypted out/Decrypted in).\n" +
                    "[3] - Pick output file (Decryption out).\n"+
                    "[4] - Pick URL.\n" +
                    "-------------------\n"+
                    "NOTE:\n" +
                    "Encryption output file is same as Decryption input file.\n" +
                    "Decryption input file WILL BE OVERWRITTEN then encrypting file.\n"+
                    "\n\t - Input file: "+ menu.inFile +
                    "\n\t - Encrypted file: "+ menu.enFile +
                    "\n\t - Decrypted file: "+menu.deFile +
                    "\n\t - URL: "+ menu.inURL +"\n"+
                    "-------------------\n"+
                    "[0] - Go back\n\n"
            );
            choice = in.nextInt();
            sourceMenu_Do(choice, menu);
        }while (choice != 0);

    }

    private static void sourceMenu_Do(int choice, Menu menu) {
        switch (choice){
            case 1:
                menu.inFile = getFilePath(menu.inFile);
                String separator = java.io.File.separator;
                int pathEnd = menu.inFile.lastIndexOf(separator) +1;
                String file = menu.inFile.substring(pathEnd);
                String path = menu.inFile.substring(0, pathEnd);
                menu.enFile = path + "encrypted_" + file;
                menu.deFile = path + "decrypted_" + file;
                break;
            case 2: menu.enFile = getFilePath(menu.enFile); break;
            case 3: menu.deFile = getFilePath(menu.deFile); break;
            case 4: menu.inURL = getURL(menu.inURL); break;
            default:
                System.out.println("Wrong choice: "+ choice);
        }
    }

    private static String getURL(String inURL) {
        String url;
        do{
            System.out.println("Enter URL ([0] - to Cancel): ");
            url = Menu.in.next();
            if( url.trim().equals("0")){ return inURL; }
        }while(invalidURL(url));

        return url;
    }


    private static boolean invalidURL(String url) {
        try { new URL(url); }
        catch (MalformedURLException e) {
            System.out.println("Wrong url: "+ url);
            return true;
        }
        return false;
    }


    /**
     * This method sets all file paths.
     *
     * @param menu holding all file paths.
     */
    static void setFiles(Menu menu){
        System.out.println("Encryption input file: "+ menu.inFile);
        menu.inFile = getFilePath(menu.inFile);

        int pathEnd = menu.inFile.lastIndexOf(java.io.File.separator) +1;
        String file = menu.inFile.substring(pathEnd);
        String path = menu.inFile.substring(0, pathEnd);
        menu.enFile = path + "encrypted_" + file;
        menu.deFile = path + "decrypted_" + file;

        System.out.println("Decryption input file: "+ menu.enFile);
        menu.enFile = getFilePath(menu.enFile);
    }

    /**
     * Method opens file chooser.
     *
     * @param defaultPath default file path if file wont be selected
     * @return selected file path or default file path.
     */
    public static String getFilePath(String defaultPath){
        // got code from Cormac McHale, changed to meet my needs.
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("text Files", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);

        switch (returnVal){
            case JFileChooser.APPROVE_OPTION : return chooser.getSelectedFile().getPath();
        }
        return defaultPath;
    }

    /**
     * This method handles Encryption and Decryption menus.
     *
     * @param cipherAction action (encrypt/decrypt).
     * @param menu Menu instance (dependency injection).
     */
    private static void xCryptionMainMenu(CipherAction cipherAction, Menu menu){
        String inputFile, outputFile;

        switch (cipherAction){
            case ENCRYPT:
                inputFile = menu.inFile;
                outputFile = menu.enFile;
                break;
            case DECRYPT:
                inputFile = menu.enFile;
                outputFile = menu.deFile;
                break;
            default:
                inputFile = "";
                outputFile = "";
        }
        int choice;
        System.out.println(menu.banner +
                String.format("=   %-26s=\n", cipherAction)+
                "===============================\n"+
                "\n"+
                " Cipher: "+ menu.cipherType +"\n"+
                menu.cipher.getCipherSettings() +"\n"+
                "-------------------\n"+
                (menu.inFile.trim().isEmpty()
                        ? "You need Pick File/URL first in \"Setup\"\n"
                        :
                          String.format("[1] - File to File. \n\t%s: %s\n\tTo: %s\n", cipherAction, inputFile, outputFile) +
                          String.format("[2] - File to Screen. \n\t%s: %s\n", cipherAction, inputFile)+
                          String.format("[3] - URL to File. \n\t%s: %s\n\tTo: %s\n", cipherAction, menu.inURL, outputFile) +
                          String.format("[4] - URL to Screen. \n\t%s: %s\n", cipherAction, menu.inURL))+
                "-------------------\n"+
                "[0] - Go back\n"
        );

        do{
            choice = in.nextInt();
            xCrypticMenu_Do(choice, cipherAction, menu, inputFile, outputFile);
        }while(choice != 0);
    }

    /**
     * This method is "xCrypticMenu()" method logic.
     *
     * @param choice menu choice.
     * @param cipherAction action (encrypt/decrypt).
     * @param menu Menu instance (dependency injection).
     */
    private static void xCrypticMenu_Do(int choice, CipherAction cipherAction, Menu menu, String inputFile, String outputFile) {
        UnaryOperator<String> prepare = menu.cipherType.getPrepareOperator();
        UnaryOperator<String> action;

        switch (cipherAction){
            case ENCRYPT: action = menu.cipher::encrypt; break;
            case DECRYPT: action = menu.cipher::decrypt; break;
            default:
                action = UnaryOperator.identity();
        }

        System.out.print(cipherAction +" started... ");
        long start = System.nanoTime();

        switch (choice){
            case 1: Parser.fileToFile(inputFile, outputFile, prepare, action); break;
            case 2: Parser.fileToScreen(inputFile, prepare, action); break;
            case 3: Parser.urlToFile(menu.inURL, outputFile, prepare, action); break;
            case 4: Parser.urlToScreen(menu.inURL, prepare, action); break;
            default:
                System.out.println("Unknown choice: "+ choice);
        }

        System.out.println( String.format("done (%.3f ms).", (System.nanoTime() -start) /1_000_000f) );
    }

    /**
     * Get Cipher instance
     * @return Cipher instance.
     */
    public Cipher getCipher() {
        return cipher;
    }

    /**
     * Change cipher
     * @param cipher cipher to change to.
     */
    public void changeCipher(Cipher cipher) {
        this.cipher = cipher;
        this.cipherType = CipherTypes.typeOf(this.cipher);
    }

    /**
     * Get CipherType instance.
     * @return CipherType instance.
     */
    public CipherTypes getCipherType() {
        return cipherType;
    }
}


/**
 * Enumeration for choosing cipher action.
 */
enum CipherAction{
    // https://stackoverflow.com/a/2497532/5322506
    ENCRYPT("Encrypt"),
    DECRYPT("Decrypt");

    private String s;

    CipherAction(String s){
        this.s = s;
    }

    @Override
    public String toString(){
        return this.s;
    }
}
