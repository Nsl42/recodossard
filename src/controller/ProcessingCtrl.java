package controller;

import java.util.ArrayList;
import java.util.UUID;
import model.CVOCR;
import model.ImgModel;
import model.PhotoList;

public class ProcessingCtrl extends Controller {
	
	

	public String getName(int bibNumber) {
		return "0";
	}
	
	/*public int getNumber(String name) {
		return PhotoList.
	}*/
	
	public ArrayList<Integer> getBibsNumber(UUID idPhoto) {
		return null;
	}
	public UUID getPlidFromImgid(UUID imgId)
	{
		UUID ret = null;
		for(UUID listID : ProcessingCtrl.loadedPhotoLists.keySet())
		{
			if(ProcessingCtrl.loadedPhotoLists.get(listID).contains(imgId))
			{
				ret = listID;
				break;
			}
		}
		return ret;
	}
	public String imgProcessing(UUID imgID)
	{
		CVOCR cvocr = new CVOCR();
		PhotoList pl = ProcessingCtrl.loadedPhotoLists.get(getPlidFromImgid(imgID));
		ImgModel to_ret = null;
		for(ImgModel im : pl.getPhotolist())
			if(im.getId().equals(imgID))
			{
				to_ret = im;
				cvocr.launchDetection(false, im.getPath());
			}
		return to_ret.toJSON();
	}
	public String listProcessing(UUID photoListID)
	{
		return "i";
	}

}
