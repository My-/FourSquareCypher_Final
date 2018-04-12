package ie.gmit.sw;

/**
 *  <p>Cipher interface has two abstract methods:</p>
 *  <ul>
 *      <li>encrypt</li>
 *      <li>decrypt</li>
 *  </ul>
 *  <p>Thous methods takes and returns <b><i>String</i></b> as parameters.</p>
 *  <br>
 *
 * @by Mindugas Sharskus
 * @date 18/03/2018
 */
public interface Cipher{

    /**
     * Method to be use for encrypting string.
     *
     * @param s string to be encrypted.
     * @return encrypted string.
     */
    String encrypt(String s);

    /**
     * Method to be use for decrypting string.
     *
     * @param s string to be decrypted.
     * @return decrypted string.
     */
    String decrypt(String s);

    /**
     * Gets Cipher settings as String. Override if planing add settings menu.
     *
     * @return Cipher settings as string.
     */
    default String getCipherSettings(){
        return "";
    }

}

