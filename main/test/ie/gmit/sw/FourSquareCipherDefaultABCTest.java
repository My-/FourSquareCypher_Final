package ie.gmit.sw;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FourSquareCipherDefaultABCTest {

    public static final String enKey1 = "GIVFWMCYZKUXTEP ABDHLNORS";
    public static final String enKey2 = "TFWXPSKEOULGNH ABCDIMRVYZ";
    static FourSquareCipher cipher = FourSquareCipherDefault.of( enKey1, enKey2);

    @BeforeAll
    static void show(){
        System.out.println(cipher.getCipherSettings());
    }

    @Test
    void changeKey() {
        assertAll(
                () -> cipher = FourSquareCipherDefault.of( enKey1, enKey2),
                () -> assertEquals(enKey1, cipher.getEnKey1()),
                () -> cipher.changeKey(1, enKey2),
//                () -> System.out.println(cipher.getCipherSettings()),
                ()-> assertEquals(enKey2, cipher.getEnKey1())
        );
    }

    @Test
    void encrypt(){
        assertAll(
//                () -> cipher = FourSquareCipherDefaultABC.of( enKey1, enKey2),
////                () -> assertEquals("UVXETRLCYBFIOGAHNDS KMWZP", cipher.encrypt()),
//                () -> cipher.changeKey(1, "AXDOMBZGNK SCHLFPVRUEIYWT"),
////                () -> System.out.println(cipher.getCipherSettings()),
//                ()-> assertEquals("AXDOMBZGNK SCHLFPVRUEIYWT", cipher.getEnKey1())
        );
    }

    @Test
    void encrypt_decrypt_test() {
        cipher = FourSquareCipherDefault.of( enKey1, enKey2);

        assertAll(
                ()-> assertEquals("AB", cipher.decrypt(cipher.encrypt("AB"))),
                ()-> assertEquals("BB", cipher.decrypt(cipher.encrypt("BB"))),
                ()-> assertEquals("GH", cipher.decrypt(cipher.encrypt("GH"))),
                ()-> assertEquals(cipher.decrypt(cipher.encrypt("KI")), cipher.decrypt(cipher.encrypt("QJ"))),
                ()-> assertEquals(cipher.decrypt(cipher.encrypt("KI")), cipher.decrypt(cipher.encrypt("KI"))),
                ()-> assertEquals("KI", cipher.decrypt(cipher.encrypt("QJ"))),
                ()-> assertEquals("CHAPTER I", cipher.decrypt(cipher.encrypt("CHAPTER I")).trim())
        );
    }
}