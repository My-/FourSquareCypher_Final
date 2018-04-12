package ie.gmit.sw;


import java.util.function.UnaryOperator;

/**
 * CipherTypes enum is used by Cipher interface default method <i>getCipherType()</i> to return Cipher type.
 * That default method and CipherTypes enum should be updated accordingly to reflect supported classes implementing Cipher.
 *
 * @by Mindugas Sharskus
 * @date 25/03/2018
 */
enum CipherTypes {
    // https://stackoverflow.com/a/2497532/5322506
    // https://docs.oracle.com/javase/specs/jls/se8/html/jls-8.html#jls-8.9.3

    /**
     * Unknown cypher. To support new ciphers this file should be updated accordingly.
     */
    UNKNOWN("Cipher", "", UnaryOperator.identity()){
        Cipher getInstance(){ return FourSquareCipherDefault.of(); }
    },

    /**
     * Four Square Cipher - 25 uppercase letters(Default)
     */
    FOUR_SQUARE_CIPHER_DEFAULT("Four Square Cipher (Default)",
            MenuFourSquareCipher.setupMenu,
            FourSquareCipherDefault.PREPARE){

        Cipher getInstance(){ return FourSquareCipherDefault.of(); }
    },

    /**
     * Four Square Cipher - 100 characters (32 - 132 ASCII)
     */
    FOUR_SQUARE_CIPHER_ASCII("Four Square Cipher (ASCII)",
            MenuFourSquareCipher.setupMenu,
            FourSquareCipherASCII.PREPARE){

        Cipher getInstance(){ return FourSquareCipherASCII.of(); }
    };


    private String name;        // enum name. will be returned by toString()
    private String setupMenu;   // cipher setup menu. Menu class use it to display specific cyphers menu.
    private UnaryOperator<String> prepare;  // string preparation function. It will be used before encryption AND decryption.

    /**
     * Method to get instance of the cipher.
     * @return instance of the cipher.
     */
    abstract Cipher getInstance();

    // enum constructor are always private. No need to specify.
    CipherTypes(String name, String setupMenu, UnaryOperator<String> prepare){
        this.name = name;
        this.setupMenu = setupMenu;
        this.prepare = prepare;
    }

    /**
     * Checks instance to get type of cipher.
     *
     * @return cipher type as in CipherTypes enum. CipherTypes.UNKNOWN if mach not found.
     */
    public static CipherTypes typeOf(Cipher cipher){
        if(cipher instanceof FourSquareCipherDefault){ return CipherTypes.FOUR_SQUARE_CIPHER_DEFAULT; }
        else if(cipher instanceof FourSquareCipherASCII){ return CipherTypes.FOUR_SQUARE_CIPHER_ASCII; }
        return CipherTypes.UNKNOWN;
    }

    /**
     * Gets UnaryOperator for preparing string.
     *
     * @return UnaryOperator for preparing string.
     */
    public UnaryOperator<String> getPrepareOperator(){
        return this.prepare;
    }

    /**
     * Gets menu supported setting menu view.
     *
     * @return setting menu.
     */
    public String getSetupMenu(){
        return this.setupMenu;
    }

    /**
     * Calls setting menu of appropriate class.
     *
     * @param choice menu choice.
     * @param menu Menu instance (mainly for getting Cipher instance)
     */
    public void setupMenu(int choice, Menu menu){
        MenuFourSquareCipher.setupMenu(choice, menu);
    }



    @Override
    public String toString() {
        return this.name;
    }
}