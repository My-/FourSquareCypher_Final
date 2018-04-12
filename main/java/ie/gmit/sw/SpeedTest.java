package ie.gmit.sw;


import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.UnaryOperator;


/**
 * @by Mindugas Sharskus
 * @date 17/03/2018
 */
public class SpeedTest {

    public static final String FILE_SMALL = "PoblachtNaHEireann.txt";
    public static final String FILE_MEDIUM = "DeBelloGallico.txt";
    public static final String FILE_BIG = "WarAndPeace-LeoTolstoy.txt";
    public static final String FILE_BIG_10 = "WarAndPeace-LeoTolstoy10.txt";
    public static final String FILE_BIG_50 = "WarAndPeace-LeoTolstoy50.txt";
    public static final String[] FILES = new String[]{
                    FILE_SMALL
                    , FILE_MEDIUM
                    , FILE_BIG
                    , FILE_BIG_10
//                    , FILE_BIG_50
            };

    public static final String FILE_ENCRYPTED = "encrypted.txt";
    public static final String FILE_DECRYPTED = "decrypted.txt";

    public static final String enKey1 = "UVXETRLCYBFIOGAHNDS KMWZP";
    public static final String enKey2 = "AXDOMBZGNK SCHLFPVRUEIYWT";

    public static void main(String[] args) {

        String pathFile = Menu.getFilePath("/tmp/ramdisk/text/");
        int pathEnd = pathFile.lastIndexOf(java.io.File.separator) +1;
        String path = pathFile.substring(0, pathEnd);

        String fileSource = path +SpeedTest.FILE_BIG;
        String fileEncrypted = path + SpeedTest.FILE_ENCRYPTED;
        String fileDecrypted = path + SpeedTest.FILE_DECRYPTED;

        final int TESTS = 20;
        long[] enTimes = new long[TESTS];
        long[] deTimes = new long[TESTS];
        long start = System.nanoTime(), end, encryptTime, setup;
        double enAvg, deAvg;

//        Cipher cipher = FourSquareCipherDefault.of(enKey1, enKey2);
        Cipher cipher = FourSquareCipherASCII.of();
        UnaryOperator<String> prepare = CipherTypes.typeOf(cipher).getPrepareOperator();

        Function<long[], Double> average = arr ->
                Arrays.stream(arr)
                        .map(it-> it /1_000)
                        .average()
                        .orElse(-1);

        setup = System.nanoTime();
        double setupTime = (setup -start) /1_000_000f;
        System.out.println(String.format("\nSetup done in: %.3f ms", setupTime));

        // warm up
        Parser.fileToFile(fileSource, fileEncrypted,  prepare, cipher::encrypt );
        Parser.fileToFile(fileEncrypted, fileDecrypted,  prepare, cipher::decrypt );
        System.out.println( String.format("Warm up: %.3f ms", (System.nanoTime() -setup) /1_000_000f) );

        for(String file : SpeedTest.FILES){
            fileSource = path + file;
            System.gc();
            System.out.println("\n\n===========  "+ file +"  ==================\n");

            for(int i = 0; i < TESTS; i++){
                setup = System.nanoTime();      // start
                Parser.fileToFile(fileSource, fileEncrypted,  String::toUpperCase, cipher::encrypt );
                encryptTime = System.nanoTime();// encryption end
                Parser.fileToFile(fileEncrypted, fileDecrypted,  String::toUpperCase, cipher::decrypt );
                end = System.nanoTime();        // decryption end

                enTimes[i] = encryptTime -setup;// log encryption
                deTimes[i] = end -encryptTime;  // log decryption
            }

            System.out.print("Encryption: ");
            Arrays.stream(enTimes).forEach(it->System.out.print(
                    String.format("%8.2f", it /1_000_000f) ));

            System.out.print("\nDecryption: ");
            Arrays.stream(deTimes).forEach(it->System.out.print(
                    String.format("%8.2f", it /1_000_000f) ));

            enAvg = average.apply(enTimes) /1_000f;
            deAvg = average.apply(deTimes) /1_000f;

            System.out.println("\n");
            System.out.println("Encryption average: "+ String.format("%10.3f", enAvg) +" ms");
            System.out.println("Encryption average: "+ String.format("%10.3f", deAvg) +" ms");

            System.out.println(
                    String.format("Total [setup time + encrypt average + decrypt average(Excluding warm up time]: %8.3f ms",
                            setupTime +enAvg +deAvg)
            );

        }

    }




}
