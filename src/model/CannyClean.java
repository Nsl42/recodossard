package model;

import java.io.*;
import org.opencv.core.*;
import org.opencv.imgproc.*;
import org.opencv.imgcodecs.*;

public class CannyClean{

    Mat img;
    Mat sortie;
    String filename;

    public CannyClean(String filename){
        this.filename = filename;
        this.sortie = new Mat();
        this.img = Imgcodecs.imread(filename);
    }

    public Mat getImg()
    {
        return this.img; 
    }
    public void setImg(Mat img)
    {
        this.img = img;
    }
    public void executeCanny()
    {
        Imgproc.Canny(this.img, sortie, 300, 600, 5, true);
    }
    public boolean writeImg()
    {
        return Imgcodecs.imwrite("/tmp/toto.jpg", sortie);
    }
}
