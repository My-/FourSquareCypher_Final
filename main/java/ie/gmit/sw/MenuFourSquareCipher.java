package ie.gmit.sw;


import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Is special class for displaying FourSquareCipher settings and for change them.
 * Uses CipherType for communications between menu and FourSquareCipher instance.
 *
 * @by Mindugas Sharskus
 * @date 27/03/2018
 */
public class MenuFourSquareCipher{

    /**
     * Field which is used for displaying setup menu in Menu class
     */
    public static String setupMenu = ""+
            "[2] - Key 1\n"+
            "[3] - Key 2\n"+
            "[4] - Cipher Type\n";

    // default constructor
    private MenuFourSquareCipher(){ }

    /**
     * Entry point of this class.
     *
     * @param choice user choice.
     * @param menu main menu instance
     */
    static void setupMenu(int choice, Menu menu){
        FourSquareCipher fsCipher = (FourSquareCipher) menu.getCipher();

        switch (choice){
            case 1: break; // NOTE: [1] is file menu, handled by Menu class
            case 2: keySetupMenu(1, fsCipher, menu); break;
            case 3: keySetupMenu(2, fsCipher, menu) ; break;
            case 4: cipherTypeMenu(menu); break;
        }
    }

    /**
     * Method responsible for key setup menu part.
     *
     * @param key key to setup (1 or 2).
     * @param cipher instance of FourSquareCipher.
     * @param menu main menu instance.
     */
    private static void keySetupMenu(int key, FourSquareCipher cipher, Menu menu) {
        String nKey, keyValue = "";
        do{
            switch(key){
                case 1: keyValue = cipher.getEnKey1(); break;
                case 2: keyValue = cipher.getEnKey2(); break;
            }
            System.out.println(menu.bannerSetup +
                    String.format("=   Key %d                      =\n", key)+
                    "===============================\n"+
                    "\n"+
                    "[1] - new random key\n"+
                    "[2] - new key from phrase\n"+
                    "[enter key] -  to change key\n"+
                    "-------------------\n"+
                    "current key: "+ keyValue +"\n"+
                    "-------------------\n"+
                    "[0] - Go back\n"+
                    "\nEnter choice: "
            );
            nKey = Menu.in.next();
            keySetupMenu_Do(key, nKey, cipher, menu.getCipherType().getPrepareOperator());
        }while( !nKey.trim().equals("0") );
    }

    /**
     * Method responsible for key setup menu parts user actions.
     *
     * @param key key to setup (1 or 2).
     * @param nKey user entered string (could be key or menu choice)
     * @param cipher instance of FourSquareCipher.
     * @param prepare used to prepare custom key and key from phrase.
     */
    private static void keySetupMenu_Do(int key, String nKey, FourSquareCipher cipher, UnaryOperator<String> prepare){
        if( nKey.trim().equals("1") ){ cipher.newRandomKey(key); }
        else if( nKey.trim().equals("2") ){
            Menu.in.nextLine();
            System.out.println("Enter phrase: ");
            String phrase = Menu.in.nextLine();
            phrase = prepare.apply(phrase);
            String newKey = generateKeyFromPhrase(phrase, cipher.getAbc());
            cipher.changeKey(key, newKey);
        }
        else if(cipher.changeKey(key, prepare.apply(nKey))) { System.out.println("Key changed!"); }
        else if( !nKey.trim().equals("0")){ System.out.println("Wrong key format! Enter new key: "); }
    }

    /**
     * Method generates key from phrase. If needed adds missing characters from alphabet.
     *
     * @param phrase phrase to use for generating key.
     * @param abc alphabet to be used for adding missing characters.
     * @return generated new key.
     */
    private static String generateKeyFromPhrase(String phrase, String abc){
        IntPredicate filter = it -> abc.contains(((char)it)+"");
        String str = phrase + abc;

        return ( str ).codePoints()
                .filter(filter)
                .distinct()
                .collect(StringBuilder::new,            // https://stackoverflow.com/a/28285243/5322506
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }

    /**
     * Method responsible for changing ciphers.
     *
     * @param menu instance of main menu.
     */
    private static void cipherTypeMenu(Menu menu) {
        int choice;
        do{
            System.out.println(menu.bannerSetup +
                    "=   Cipher Type               =\n"+
                    "===============================\n"+
                    "\n"+
                    Arrays.stream(CipherTypes.values())
                            .filter(it -> it != CipherTypes.UNKNOWN)
                            .map(it-> String.format("[%d] - %s\n", it.ordinal(), it.toString()))
                            .collect(Collectors.joining()) +
                    "-------------------\n"+
                    menu.getCipherType() +"\n"+
                    "-------------------\n"+
                    "[0] - Go back\n"+
                    "\nEnter choice: "
            );
            choice = Menu.in.nextInt();
            if( 0 < choice && choice < CipherTypes.values().length ){
                menu.changeCipher( CipherTypes.values()[choice].getInstance() );
            }
        }while (choice != 0);
    }


}
