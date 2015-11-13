package view;

import model.CannyClean;
import java.io.File;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.ImageHelper;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Core;


public class Main{
    public static void main(String args[]){
        if (args.length == 0) {
            System.out.println("Usage : java Main <image file>");
        } else {
            String arg = args[0];
            if(!arg.substring(arg.length() - 3, arg.length()).equals("jpg"))
            {
                System.out.println("Argument must be a jpg image file !");
                return;
            }else {
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                CannyClean toto = new CannyClean(arg);
                toto.executeCanny();
                toto.writeImg();
                 File imageFile = new File("/tmp/tutu.jpg");

                 Tesseract instance = Tesseract.getInstance(); 
                    instance.setTessVariable("tessedit_char_whitelist", "0123456789");
//

        try {

            String result = instance.doOCR(ImageHelper.invertImageColor(ImageIO.read(imageFile)));
            System.out.println(result);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
            }

        }
    }
}
