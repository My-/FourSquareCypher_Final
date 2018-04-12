package ie.gmit.sw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FourSquareCipherASCIITest {

    static String key1 = "2\\FOa[1>9-.4M Jz=_I,pxf(g/~\u0082w&N%B\u0081lY\u0080j]8Pb}:<\u0083QmKki\"XnWcE\u007FGodSC?U`r|yR5DT;*tqu{AVHLh$6Z#!7se)^3'v@+0";
    static String key2 = "fJvqnwu(*`@U?&c\u00814/>y\\_\u0083dK.NpV-Im\u008072$A{G0x9SEh b!16eTXY|\"s[%jo=Ft8L~Z,}^)#OP3\u007FlR<gz'+Wr\u00825]CB:iMHDa;Qk";
    static FourSquareCipher cipher = FourSquareCipherASCII.of(key1, key2);
    String abc = cipher.getAbc();

//    @BeforeAll
//    static void show(){
//        System.out.println(cipher.getCipherSettings());
//    }

    @Test
    void getIndex() {
        assertAll(
               ()-> assertEquals("2f", cipher.encrypt("  ")),
                ()-> assertEquals("\\f", cipher.encrypt(" !")),
                ()-> assertEquals("2B", cipher.encrypt(" z")),
                ()-> assertEquals("2B2f", cipher.encrypt(" z  "))
        );
    }

    @Test
    void testKeys() {
        assertAll(
                ()-> assertEquals(key1, cipher.getEnKey1()),
                ()-> assertEquals(key2, cipher.getEnKey2())
        );
    }

    @Test
    void encrypt_decrypt_test() {
        cipher = FourSquareCipherASCII.of();
        String s1 = "\"Well, Prince, so Genoa and Lucca are now just family estates of the\n" +
                "Buonapartes. But I warn you, if you don't tell me that this means war,\n" +
                "if you still try to defend the infamies and horrors perpetrated by that\n" +
                "Antichrist--I really believe he is Antichrist--I will have nothing more\n" +
                "to do with you and you are no longer my friend, no longer my 'faithful\n" +
                "slave,' as you call yourself! But how do you do? I see I have frightened\n" +
                "you--sit down and tell me all the news.\"";

        String s2 = "Poblacht na hÉireann.\n" +
                "The Provisional Government\n" +
                "of the\n" +
                "Irish Republic\n" +
                "To the people of Ireland.\n" +
                "IRISHMEN AND IRISHWOMEN: In the name of God and of the dead generations from which she receives her old tradition of nationhood, Ireland, through us, summons her children to her flag and strikes for her freedom.";

        assertAll(
//                ()-> System.out.println(cipher.getCipherSettings()),
                ()-> assertEquals("AB", cipher.decrypt(cipher.encrypt("AB"))),
                ()-> assertEquals("BB", cipher.decrypt(cipher.encrypt("BB"))),
                ()-> assertEquals("GH", cipher.decrypt(cipher.encrypt("GH"))),
                ()-> assertEquals("CHAPTER I", cipher.decrypt(cipher.encrypt("CHAPTER I")).trim()),
                ()-> assertEquals(s1, cipher.decrypt(cipher.encrypt(s1))),
                ()-> assertEquals(s2, cipher.decrypt(cipher.encrypt(s2)))
        );
        System.out.println(cipher.decrypt(cipher.encrypt(s2)));
    }
}