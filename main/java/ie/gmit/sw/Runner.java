package ie.gmit.sw;



public class Runner {

    public static void main(String[] args) {

//        Cipher cipher = FourSquareCipherDefaultABC.of();
        Cipher cipher = FourSquareCipherASCII.of();
        Menu.of(cipher).show();

    }


}
