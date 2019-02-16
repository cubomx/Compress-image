package up.sergio.compress;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main (String [] args) throws IOException {
        String nameFile = "compres.kbo";
        String outputFileExtension = "png";
        try {
            BufferedImage image = ImageIO.read(new File("image.png"));
            Kbo_Compression kbo =  new Kbo_Compression();
            try {
                PrintWriter printWriter = new PrintWriter(nameFile);
                printWriter.print(kbo.compressImage(image));
                printWriter.close();
            }
            catch (IOException io){
                io.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String data = readFile(nameFile);
        String [] info = data.split("#");
        String [] colors = divideArray(info, info.length - 2, "&");
        String [] fileSize = divideArray(info, info.length - 1, ":");
        String [] points = divideArray(info, info.length - 3, " ");
        BufferedImage image;
        File file = new File("smaller.png");
        try{
            Kbo_Compression kbo =  new Kbo_Compression();
            image = kbo.decompressImage(colors, points, fileSize);
            ImageIO.write(image, outputFileExtension, file);
        }
        catch (IOException IO){
            IO.printStackTrace();
        }
    }

    /**
     * function divides all the points
     * @param info
     * @param index
     * @param charToSplit
     * @return
     */
    private static String [] findingAllPoints(String [] info, int index, String charToSplit){
        return divideArray(info, index, charToSplit);
    }

    /**
     * Gets the information from the file
     * @param fileName
     * @return
     * @throws IOException
     */

    private static String readFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    /**
     * Prints all the elements in an array
     * @param data
     */
    private static void printProperties(String [] data){
        for (String item: data) {
            System.out.println(item);
        }
    }

    /**
     * Gets a delimiter which determines where to split it
     * @param info
     * @param index
     * @param charToSplit
     * @return
     */

    private static String [] divideArray(String [] info, int index, String charToSplit){
        return info[index].split(charToSplit);
    }

}
