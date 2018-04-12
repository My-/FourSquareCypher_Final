package ie.gmit.sw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FourSquareCipherTest {

    public static final String enKey1 = "UVXETRLCYBFIOGAHNDS KMWZP";
    public static final String enKey2 = "AXDOMBZGNK SCHLFPVRUEIYWT";
    static FourSquareCipher cipher = FourSquareCipherDefault.of( enKey1, enKey2);

    @Test
    void validateKey() {
        assertAll(
                ()-> assertTrue(cipher.validateKey("AXDOMBZGNK SCHLFPVRUEIYWT")),
                ()-> assertFalse(cipher.validateKey("AXDOMBZGNK SSHLFPVRUEIYWT"))
        );
    }

    @Test
    void encrypt() {
    }

    @Test
    void decrypt() {
    }
}