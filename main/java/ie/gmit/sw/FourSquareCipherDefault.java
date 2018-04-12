package ie.gmit.sw;

import java.util.function.UnaryOperator;

/**
 * Four Square Cipher - 25 uppercase letter version.
 * Letters from A to Z excluding Q and J. And add ' ' (space).
 *
 * @by Mindugas Sharskus
 * @date 20/03/2018
 */
public class FourSquareCipherDefault extends FourSquareCipher {

    /**
     * Prepare function. Should be used before encryption with this cipher.
     */
    public static final UnaryOperator<String> PREPARE = String::toUpperCase;

    // constructor for custom keys cypher.
    private FourSquareCipherDefault(String enKey1, String enKey2){
        super(FourSquareCipher.ALPHABET, enKey1, enKey2);
    }

    // constructor for random keys
    private FourSquareCipherDefault(){
        super(FourSquareCipher.ALPHABET);
    }

    /**
     * Factory method for creating cipher with random keys.
     *
     * @return instance of this class.
     */
    public static FourSquareCipherDefault of(){
        return new FourSquareCipherDefault();
    }

    /**
     * Factory method for creating cipher with provided keys.
     *
     * @return instance of this class.
     */
    public static FourSquareCipherDefault of(String enKey1, String enKey2){
        return new FourSquareCipherDefault(enKey1, enKey2);
    }

    @Override
    public int getIndex(int ch) {
        if( 'A' <= ch && ch < 'J'){ return ch -64; }
        if( 'J' < ch && ch < 'Q'){ return ch -65; }
        if( 'Q' < ch && ch <= 'Z'){ return ch -66; }
        if( ch == 'J' ){ return 'I' -64; }
        if( ch == 'Q' ){ return 'K' -65; }

        return 0; // ' ' (space)
    }


}
