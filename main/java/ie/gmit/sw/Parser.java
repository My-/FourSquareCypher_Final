package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class Parser {




    /**
     *  This method opens input file. On each line apply maps function 'beforeAction'
     *  and then maps function 'action'. And then saves lines to output file.
     *
     * @param inFile    path of input file
     * @param outFile   path of output file
     * @param beforeAction action to be perform before another one.
     * @param action    Action to be performed on each line
     */
    static void fileToFile(String inFile, String outFile,
                           UnaryOperator<String> beforeAction,
                           UnaryOperator<String> action){

        try ( BufferedReader br = Files.newBufferedReader(
                FileSystems.getDefault().getPath(inFile), StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(
                     FileSystems.getDefault().getPath(outFile), StandardCharsets.UTF_8)
        ){
            Consumer<String> writeToFile = it-> {
                try { writer.write(it, 0, it.length()); writer.newLine(); }
                catch (IOException e) { e.printStackTrace(); }
            };

            br.lines()
                    .parallel()
                    .map(beforeAction)
                    .map(action)
//                    .peek(System.out::println)
                    .forEachOrdered(writeToFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  This method opens input file. On each line apply maps function 'beforeAction'
     *  and then maps function 'action'. And then saves lines to output file.
     *
     * @param inFile    path of input file
     * @param beforeAction action to be perform before another one.
     * @param action    Action to be performed on each line
     */
    static void fileToScreen(String inFile,
                           UnaryOperator<String> beforeAction,
                           UnaryOperator<String> action){

        try ( BufferedReader br = Files.newBufferedReader(
                FileSystems.getDefault().getPath(inFile), StandardCharsets.UTF_8)){

            br.lines()
                    .parallel()
                    .map(beforeAction)
                    .map(action)
                    .forEachOrdered(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  This method opens input file. On each line apply maps function 'beforeAction'
     *  and then maps function 'action'. And then saves lines to output file.
     *
     * @param inURL    path of input url
     * @param outFile   path of output file
     * @param beforeAction action to be perform before another one.
     * @param action    Action to be performed on each line
     */
    static void urlToFile(String inURL, String outFile,
                           UnaryOperator<String> beforeAction,
                           UnaryOperator<String> action){

        URL url;
        try {
            url = new URL(inURL);
        } catch (MalformedURLException e) {
            System.out.println("Wrong URL: "+inURL);
            return;
        }

        try ( BufferedReader br = new BufferedReader(
                new InputStreamReader(url.openStream()));
             BufferedWriter writer = Files.newBufferedWriter(
                     FileSystems.getDefault().getPath(outFile), StandardCharsets.UTF_8)
        ){

            Consumer<String> writeToFile = it-> {
                try { writer.write(it, 0, it.length()); writer.newLine(); }
                catch (IOException e) { e.printStackTrace(); }
            };

            br.lines()
                    .parallel()
                    .map(beforeAction)
                    .map(action)
//                    .peek(System.out::println)
                    .forEachOrdered(writeToFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  This method opens input file. On each line apply maps function 'beforeAction'
     *  and then maps function 'action'. And then saves lines to output file.
     *
     * @param inURL    path of input url
     * @param beforeAction action to be perform before another one.
     * @param action    Action to be performed on each line
     */
    static void urlToScreen(String inURL,
                           UnaryOperator<String> beforeAction,
                           UnaryOperator<String> action){

        URL url;
        try {
            url = new URL(inURL);
        } catch (MalformedURLException e) {
            System.out.println("Wrong URL: "+inURL);
            return;
        }

        try ( BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream())) ){

            br.lines()
                    .parallel()
                    .map(beforeAction)
                    .map(action)
                    .forEachOrdered(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
