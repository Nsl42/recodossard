package view;

import control.OCREngine;

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
            }

        }
    }
}
