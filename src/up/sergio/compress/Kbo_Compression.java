package up.sergio.compress;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import java.util.HashMap;

class Kbo_Compression {
    private HashMap<String, String> indexColors = new HashMap<>();
    private Boolean itEnded = false;

    /**
     * Function that handes all the operatios in order to get the
     * compressed image
     * @param image
     * @return
     */

    StringBuilder compressImage(BufferedImage image){
        creatingOriginal(image);
        HashMap<String, String> colors;
        HashSet<Integer> noRepeated = searchCoincidences(image);
        colors = makeColorDictionary(image, noRepeated);
        StringBuilder file = creatingColorFile(colors, image);
        System.out.println(indexColors);
        addColorsToFile(file, colors);
        writingImageSize(file, image);
        return file;
    }

    private void creatingOriginal(BufferedImage image){
        StringBuilder file = new StringBuilder();
        for(int i = 0; i < image.getWidth(); i++){
            for(int j = 0; j < image.getHeight(); j++){
                file.append(image.getRGB(i,j));
            }
        }
        try {
            PrintWriter printWriter = new PrintWriter("original.txt");
            printWriter.print(file);
            printWriter.close();
        }
        catch (IOException io){
            io.printStackTrace();
        }
    }

    /**
     * Function that handles all the operations to descompress the image
     * @param points
     * @param colors
     * @param fileSize
     * @return
     */

    BufferedImage decompressImage(String[] colors, String[] points, String[] fileSize){
        BufferedImage image = new BufferedImage(Integer.parseInt(fileSize[0]), Integer.parseInt(fileSize[1]), BufferedImage.TYPE_INT_RGB);
        coloredAllColors(image, colors, points);
        return image;
    }

    /**
     * Function that colored all the image by the last color saved in the file
     * @param image
     * @param colors
     */



    /**
     * Colored with the points and colors received
     * @param image
     * @param colors
     * @param points
     */

    private void coloredAllColors(BufferedImage image, String [] colors, String [] points){
        int pos = 0;
        for(int i = 0; i < points.length; i++){
            String [] color = points[i].split("/");
            int posArray = Integer.parseInt(color[0]);
            int color_1 = Integer.parseInt(colors[posArray-1]);
            for(int index = 0; index < Integer.parseInt(color[1]); index++){
                int x = pos % image.getWidth();
                int y = pos/image.getWidth();
                image.setRGB(x, y, color_1) ;
                pos++;
            }

        }

    }

    /**
     * Color the image in the desired points and colors
     * @param image
     * @param colors
     * @param points
     * @param index
     */

    private void coloredByPoints(BufferedImage image, String [] colors, String [] points, int index){
        for(int i = 0; i < points.length; i++){
            int large = Integer.parseInt(points[i]);
            for(int j = 0; j < large; j++){
                image.setRGB(j, i, Integer.parseInt(colors[index]));
            }
        }
    }

    /**
     * Saving the file size to the compress file
     * @param file
     * @param image
     */

    private void writingImageSize(StringBuilder file, BufferedImage image){
        file.append("#").append(image.getWidth()).append(":").append(image.getHeight());
    }

    /**
     * Add the value of the colors of the image
     * @param file
     * @param colors
     */

    private void addColorsToFile(StringBuilder file, HashMap<String, String> colors){
        file.append("#");
        for(int index = 0; index < this.indexColors.size(); index++){
            file.append(colors.get(Integer.toString(index+1)));
            if (index + 1 != this.indexColors.size()){
                file.append("&");
            }
        }
    }

    /**
     * Searchs all the colors that are in the image
     * @param image
     * @return
     */

    private HashSet<Integer> searchCoincidences(BufferedImage image){
        HashSet<Integer> colors = new HashSet<>();
        for(int posX = 0; posX < image.getWidth(); posX++){
            for(int poxY = 0; poxY < image.getHeight(); poxY++){
                colors.add(image.getRGB(posX, poxY));
            }
        }
        return colors;
    }

    /**
     * Saves the dictionary which contains the colors of the image
     * @param image
     * @param colors
     * @return
     */

    private HashMap<String, String> makeColorDictionary(BufferedImage image, HashSet<Integer> colors){
        HashMap<String, String> hash =  new HashMap<>();
        HashSet<Integer> foundColors = new HashSet<>();
        int index = 0;
        for(int pixelX = 0; pixelX < image.getWidth(); pixelX++){
            for(int pixelY = 0; pixelY < image.getHeight(); pixelY++){
                if (colors.contains(image.getRGB(pixelX, pixelY)) && !foundColors.contains(image.getRGB(pixelX, pixelY))){
                    index++;
                    hash.put(Integer.toString(index), Integer.toString(image.getRGB(pixelX, pixelY)));
                    foundColors.add(image.getRGB(pixelX, pixelY));
                    this.indexColors.put(Integer.toString(image.getRGB(pixelX, pixelY)), Integer.toString(index));
                }
            }
        }
        return hash;
    }

    /**
     * Saving all the intervals in which the colors are
     * @param hash
     * @param image
     * @return
     */

    private StringBuilder creatingColorFile(HashMap<String, String> hash, BufferedImage image){
        StringBuilder file = new StringBuilder();
        appendingColorsAtStart(file);
        file.append("#");
        int cont = 0;
        int next;
        int actual = Integer.parseInt(this.indexColors.get(Integer.toString(image.getRGB(0, 0))));
        System.out.println();
        for(int colors = 0; colors < this.indexColors.size(); colors++){
            if(colors > 0) file.append("#");
            for (int i = 0; i < image.getHeight(); i++){
                for(int j = 0; j < image.getWidth(); j++){
                    next = Integer.parseInt(this.indexColors.get(Integer.toString(image.getRGB(j, i))));
                    if(actual == next){
                        cont++;
                    }
                    else{
                        file.append(actual + "/" + (cont) + " ");
                        cont = 1;
                        actual = next;
                    }
                }
            }
        }
        return file;
    }

    /**
     * Saving the value of the colors
     * @param file
     */

    private void appendingColorsAtStart(StringBuilder file){
        for(int colors = 0; colors < this.indexColors.size(); colors++){
            file.append(colors+1);
            if(colors+1 != this.indexColors.size()) file.append("%");
        }
    }
}
