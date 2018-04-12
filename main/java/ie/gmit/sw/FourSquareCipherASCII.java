package ie.gmit.sw;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * Four Square Cipher - ASCII version.
 *
 * @by Mindugas Sharskus
 * @date 26/03/2018
 */
public class FourSquareCipherASCII extends FourSquareCipher{

    private static final int ASCII_START_INCLUSIVE = 32; // 6
    private static final int ASCII_END_EXCLUSIVE = 132; // 127
    private static final int ASCII_ESCAPE = 27 -ASCII_START_INCLUSIVE;
    private static final int ASCII_SPACE = 32 -ASCII_START_INCLUSIVE;
    // Alphabet supplier.
    private static final Supplier<String> ABC_SUPPLIER = () ->
            IntStream.range(ASCII_START_INCLUSIVE, ASCII_END_EXCLUSIVE)
                    .collect(StringBuilder::new,
                            StringBuilder::appendCodePoint,
                            StringBuilder::append)
                    .toString();
    /**
     * Prepare function. Should be used before encryption with this cipher.
     * In this class it's an identity function (returns what it takes).
     */
    public static final UnaryOperator<String> PREPARE = UnaryOperator.identity();

    // Constructor for creating custom key cipher
    private FourSquareCipherASCII(String alphabet, String enKey1, String enKey2) {
        super(alphabet, enKey1, enKey2);
    }
    // Constructor for creating random key cipher
    private FourSquareCipherASCII(String alphabet) {
        super(alphabet);
    }

    /**
     * Factory method for custom key cipher.
     *
     * @param enKey1 custom key one
     * @param enKey2 custom key two
     * @return instance of this class
     */
    public static FourSquareCipherASCII of(String enKey1, String enKey2){
        return new FourSquareCipherASCII(ABC_SUPPLIER.get(), enKey1, enKey2);
    }

    /**
     * Factory method for random cipher.
     * @return instance of this class.
     */
    public static FourSquareCipherASCII of(){
        return new FourSquareCipherASCII(String.valueOf(ABC_SUPPLIER.get()));
    }

    @Override
    int getIndex(int ch) {
        if(ASCII_START_INCLUSIVE <= ch && ch < ASCII_END_EXCLUSIVE){ return ch -ASCII_START_INCLUSIVE; }
        System.out.println(">\tOut of range!! Character ascii("+ ch +")");
        return ASCII_SPACE; // Don't want to throw exception
    }
}
