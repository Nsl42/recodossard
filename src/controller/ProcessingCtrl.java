package controller;

import java.util.ArrayList;
import java.util.UUID;

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
		return ProcessingCtrl.loadedPhotoLists.get(getPlidFromImgid(imgID)).process();
	}
	public String listProcessing(UUID photoListID)
	{
		return ProcessingCtrl.loadedPhotoLists.get(photoListID).process();
	}

}
