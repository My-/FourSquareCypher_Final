package ie.gmit.sw;

import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

class ParserTest {

    @Test
    void fileToFile() {
    }

    @Test
    void fileToScreen() {
        String file = "/home/mindaugas/Java/hello.java";
        Parser.fileToScreen(file, UnaryOperator.identity(), UnaryOperator.identity());
    }

    @Test
    void urlToFile() {
        String url = "";
         Parser.urlToFile(url, "/home/mindaugas/Java/fromURL", UnaryOperator.identity(), UnaryOperator.identity());
    }

    @Test
    void urlToScreen() {
        String url = "https://raw.githubusercontent.com/My-/FourSquareCypher/master/src/main/java/ie/gmit/sw/FourSquareCipher.java?token=AJ-Nl3lTFexKLmU1t1rj_57bAJG0y6irks5aw59ewA%3D%3D";
        Parser.urlToScreen(url, UnaryOperator.identity(), UnaryOperator.identity());
    }
}