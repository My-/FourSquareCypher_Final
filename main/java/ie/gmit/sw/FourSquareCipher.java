package ie.gmit.sw;

import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * <p> n = alphabet length</p>
 * <p> gaps = unused characters between first alphabet character and last alphabet character. E.G. ' ' to 'Z' >>  32 to 90 >> 90 -32 >> 58 (instead of 25 needed)</p>
 * <p> lookup table = (n +total gaps)^2 *2</p>
 * <p> Space: <b> ~O(n^2)</b> - it's more. Because: 2 * Lookup tables + 2 * key + alphabet = 4(n +gaps)^2 +3n. I'm might be wrong...</p>
 *
 * <p> Time: check concrete classes, but for two implemented is O(n) were n is text to encrypt length. And it is independent on alphabet size.</p>
 * <p> Space complexity is independent on text to encrypt length. But depends on on alphabet size ~O(n^2)</p>
 *
 * @by Mindugas Sharskus
 * @date 17/03/2018
 */
public abstract class FourSquareCipher implements Cipher{

    public final static String ALPHABET = " ABCDEFGHIKLMNOPRSTUVWXYZ";

    // Checks if given string is made from unique characters.
    // Time: O(n) - worst case (checks for first mach and exits)
    // Space: O(n) - worst case (distinct() needs to store previous values)
    private static final Predicate<String> IS_UNIQUE_CHARS =
            it-> it.length() == it.codePoints().distinct().count();
    // Checks if given string length is a square number.
    private static final Predicate<String> IS_LENGTH_SQUARE_NUMBER =
            it -> it.length() ==  Math.pow(Math.floor(Math.sqrt(it.length())), 2);
    // checks if given string is unique character one and length is square number.
    // Time & Space: O(n) - same as IS_UNIQUE_CHARS
    private static final Predicate<String> IS_VALID_ALPHABET =
            IS_UNIQUE_CHARS.and(IS_LENGTH_SQUARE_NUMBER);

    private char[] abc;             // Alphabet
    private int abcSquareSize;      // square root of alphabet length
    private int[] enKey1, enKey2;   // encryption keys
    private char[][][] deBigrams;   // look up table for decryption
    private char[][][] enBigrams;   // look up table for encryption

    /**
     * Method for getting index of given character from ciphers alphabet.
     * @param ch character to look for (ascii code).
     * @return index or that character in alphabet.
     */
    abstract int getIndex(int ch);

    /**
     * Constructor for creating custom cipher.
     *
     * @param alphabet is used in this cipher.
     * @param enKey1 custom key one.
     * @param enKey2 custom key two.
     */
    FourSquareCipher(String alphabet, String enKey1, String enKey2){
        this.createAlphabet(alphabet);

        Function<String, int[]> createKey = it -> it.codePoints().map(this::getIndex).toArray();
        this.enKey1 = createKey.apply(enKey1);  //
        this.enKey2 = createKey.apply(enKey2);

        this.createLookups(this.abc);
    }

    /**
     * Constructor for creating random key cipher.
     *
     * @param alphabet is used in this cipher.
     */
    FourSquareCipher(String alphabet){
        this.createAlphabet(alphabet);

        this.enKey1 = generateRandomKey(this.abc.length);
        this.enKey2 = generateRandomKey(this.abc.length);

        this.createLookups(this.abc);
    }

    // Creates and sets cipher alphabet. For constructor use only.
    // Time & Space: O(n log(n)) more precise O(3n log(n))
    // because IS_UNIQUE_CHARS called twice (once inside IS_VALID_ALPHABET)
    // and array is sorted (O(n log(n)).
    private void createAlphabet(String alphabet){

        if( IS_VALID_ALPHABET.test(alphabet) ){
            this.abc = alphabet.toCharArray();
            Arrays.sort(this.abc);
        }
        else if(IS_UNIQUE_CHARS.negate().test(alphabet)){
            throw new IllegalArgumentException("Alphabet is NOT unique string: "+ alphabet);
        }
        else if(IS_LENGTH_SQUARE_NUMBER.negate().test(alphabet)){
            throw new IllegalArgumentException("Alphabet is NOT right length: "+ alphabet.length());
        }

        this.abcSquareSize = (int)Math.sqrt(this.abc.length);
    }

    // Creates (initializes) bigram lookup tables. For constructor use only.
    private void createLookups(char[] abc){
        int limit = abc[abc.length -1] -abc[0] +1;  // ascii value of last alphabet char - first alphabet character +1
        this.deBigrams = new char[limit][limit][];  // .. ' ' to 'Z' >>  32 to 90 >> 90 -32 +1 >> 59 (instead of 25 needed)
        this.enBigrams = new char[limit][limit][];  // Space: (n +total gaps)^2 ~ O(n^2)
    }

    /**
     * Generates unique int array of given length.
     * <hr>
     *     Time: <b>O(n)</b> best case scenario for this implementation. It's bad implementation. But I want to keep it for reference reasons. <br>
     *     Space: <b>O(n)</b> it needs to keep track of previous values.
     * <hr>
     * @param limit limit of the key.
     * @return randomly populated int array.
     */
    static int[] generateRandomKey(int limit){
        return new Random(System.nanoTime())
                .ints(0, limit)     // take random number between 0 and limit..
                .distinct()             // .. check if it's unique. No: go back and take new number. Yes: continue..
                .limit(limit)           // .. count values and check if limit reached. Yes: close stream. No: continue ..
                .toArray();             // .. add to array. Most likely is stored to some sort collection and then stream is closed converted to array.
    }


    /**
     * Generate new random key.
     * <hr>
     *     Time & Space: <b>O(n)</b> same as <i>generateRandomKey()</i>.
     * <hr>
     * @param keyNo key to change.
     */
    public void newRandomKey(int keyNo){
        switch (keyNo){
            case 1: this.enKey1 = generateRandomKey(this.abc.length); break;
            case 2: this.enKey2 = generateRandomKey(this.abc.length); break;
        }
    }

    /**
     * Changes key to a given key.
     * <hr>
     *     n = key length
     *     Time: <b>O(n)</b> same as <i>validateKey()</i>.
     * <hr>
     * @param keyNo key to change.
     * @param newKey key to change to.
     * @return true if key was changed. Otherwise false.
     */
    public boolean changeKey(int keyNo, String newKey){
        // TODO: how? lock it if cryption in progress to avoid change key in a middle of cryption process?
        if( validateKey(newKey) ){
            IntUnaryOperator toInt = this::getIndex;

            switch ((keyNo)){
                case 1:
                    this.enKey1 = newKey.codePoints()
                            .map(toInt)
                            .toArray();
                    return true;
                case 2:
                    this.enKey2 = newKey.codePoints()
                            .map(toInt)
                            .toArray();
                    return true;
            }
        }
        return false;
    }

    /**
     * Checks if given string can be valid key.
     * <hr>
     *     <p>n = key length</p>
     *     <p>hasChar: O(n) - worst case do not exist</p>
     *     <p>isKeyHasAllAlphabetValues: ~O(n^2) - because for each value we call <i>hasChar</i>. Is actually less then n^2 because in worst
     *     case scenario on a valid key (if not valid key operation is canceled) we check up to each element (including it) once so t is: <b>(n(n +1))/2</b></p>
     *     <p>Time: <b>~O(n^2)</b>.</p>
     * <hr>
     * @param key key to validate
     * @return true if key is valid.
     */
    boolean validateKey(final String key){
        IntPredicate hasChar = it -> key.contains(((char)it)+"");
        Predicate<String> isKeyHasAllAlphabetChars = abc -> abc.codePoints().allMatch(hasChar);

        return  FourSquareCipher.IS_UNIQUE_CHARS.test(String.valueOf(this.abc))
                &&
                isKeyHasAllAlphabetChars.test(String.valueOf(this.abc));
    }

    /**
     * Alphabet getter.
     * @return alphabet as string.
     */
    public String getAbc() {
        return String.valueOf(abc);
    }

    /**
     * Key one getter.
     * <hr>
     *     <p>n = key length.</p>
     *     <p>Time: <b>O(n)</b></p>
     * <hr>
     * @return key one as string.
     */
    public String getEnKey1() {
        IntUnaryOperator mapToChar = it -> this.abc[it]; // O(1)

        return Arrays.stream(this.enKey1)
//                .peek(it-> System.out.print(it +"\t"))    // for debugging
                .map(mapToChar)
                .mapToObj(it-> ((char)it)+"")
//                .peek(it-> System.out.println(it))        // for debugging
                .collect(Collectors.joining());
    }

    /**
     * Key two getter.
     * <hr>
     *     <p>n = key length.</p>
     *     <p>Time: <b>O(n)</b></p>
     * <hr>
     * @return key two as string.
     */
    public String getEnKey2() {
        IntUnaryOperator mapToChar = it -> this.abc[it];

        return Arrays.stream(this.enKey2)
//                .peek(it-> System.out.print(it +"\t"))    // for debugging
                .map(mapToChar)
                .mapToObj(it-> ((char)it)+"")
//                .peek(it-> System.out.println(it))        // for debugging
                .collect(Collectors.joining());
    }

    /**
     * Encrypts given bigram.
     * <hr>
     *     <p>n = alphabet length.</p>
     *     <p>Time: <b>O(1)</b> - constant. NOTE: it depends on <i>getIndex()</i> implementation. But in my two overridden methods is O(1)</p>
     * <hr>
     * @param ch1 bigrams first character.
     * @param ch2 bigrams second character.
     * @return encrypted bigram.
     */
    char[] encrypt(char ch1, char ch2) {
        int i1 = getIndex(ch1), i2 = getIndex(ch2);     // get index of each letter inside alphabet

        if( this.enBigrams[i1][i2] == null ){           // if look up table has no record..
            ch1 = this.abc[i1];
            ch2 = this.abc[i2];
            int n = this.abcSquareSize;                 // ..
            int c1 = enKey1[(i1 / n) * n + i2 % n];     // ..calculate index and get value from encryption key
            int c2 = enKey2[(i2 / n) * n + i1 % n];     //   (value is index of character inside alphabet (abc array)..
            this.enBigrams[i1][i2] = new char[]{abc[c1], abc[c2]};  // ..save newly calculated bigram to look up table..
            this.deBigrams[c1][c2] = new char[]{ch1, ch2};          // .. and save given bigram to decryption look up table
        }                                                           // for feature decryption.

        return this.enBigrams[i1][i2];
    }

    /**
     * Decrypts given bigram.
     * <hr>
     *     <p>n = alphabet length.</p>
     *     <p>Time: <b>O(1)</b> - constant. NOTE: it depends on <i>getIndex()</i> implementation. But in my two overridden methods is O(1)</p>
     * <hr>
     * @param ch1 bigrams first character.
     * @param ch2 bigrams second character.
     * @return decrypted bigram.
     */
    char[] decrypt(char ch1, char ch2) {
        int i1 = getIndex(ch1), i2 = getIndex(ch2);

        if( this.deBigrams[i1][i2] == null ){

            int n = this.abcSquareSize;
            int c1 = enKey1[(i1 / n) * n + i2 % n];
            int c2 = enKey2[(i2 / n) * n + i1 % n];
            this.deBigrams[i1][i2] = new char[]{abc[c1], abc[c2]};
        }
        return this.deBigrams[i1][i2];
    }

    @Override
    // Time: O(n)
    public String encrypt(String s){
        StringBuilder sb = new StringBuilder();

        final int limit = s.length();
        for(int i = 1; i < limit; i+=2){
            char ch1 = s.charAt(i -1), ch2 = s.charAt(i);
            sb.append(encrypt(ch1, ch2));
        }

        if(limit % 2 != 0){
            sb.append(encrypt(s.charAt(s.length() -1), ' ')); // append encrypted (last character + ' ' (space))
        }

        return sb.toString();
    }

    @Override
    // Time: O(n)
    public String decrypt(String s){
        StringBuilder sb = new StringBuilder();

        final int limit = s.length();
        for(int i = 1; i < limit; i+=2){
            char ch1 = s.charAt(i -1), ch2 = s.charAt(i);
            char[] chArr = decrypt(ch1, ch2);

            if(chArr == null ){ System.out.println("null: "+ ch1+":"+ch2); }
            else{ sb.append(chArr); }
        }
//        System.out.println(sb.toString());    // for debugging
        return sb.toString();
    }

    @Override
    // Time: O(n)
    public String getCipherSettings() {
        return    "\tAlphabet: " + this.getAbc() +
                "\n\t   Key 1: " + this.getEnKey1() +
                "\n\t   Key 2: " + this.getEnKey2();
    }

    @Override
    // Time: O(n)
    public String toString() {
        return "\tAlphabet: " + this.getAbcIndexed() +
                ", \n\tsqr size: "+ this.abcSquareSize +
                ", \n\tKey 1: " + Arrays.toString(enKey1) +
                ", \n\tKey 2: " + Arrays.toString(enKey2) +
                ", \n\tdeBigrams=" + Arrays.deepToString(deBigrams) +
                ", \n\tenBigrams=" + Arrays.deepToString(enBigrams) +
                '}';
    }

    // adds index to each alphabet character.
    // time: O(n) - one loop.
    private String getAbcIndexed() {
        StringJoiner sj = new StringJoiner(" ");
        for(int i = 0; i< this.abc.length; i++){
            sj.add("[" + i + "]: " + this.abc[i]);
        }
        return sj.toString();
    }
}

