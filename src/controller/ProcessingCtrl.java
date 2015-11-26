package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import model.CVOCR;
import model.ImgModel;
import model.PhotoList;
import model.Settings;

public class ProcessingCtrl extends Controller {
	
	CVOCR engine;
	
	/**
	 * Default and only constructor you ever need
	 */
	public ProcessingCtrl() {
	this.engine = new CVOCR();
	}

	/**
	 * setProcessSettings : Sets the global settings for cv/ocr execution
	 * @param exif boolean true if an exif analysis is expected
	 * @param data boolean true if an data analysis is expected
	 * @param debug boolean true if a debug output is expected
	 */
	public void setProcessSettings(boolean exif, boolean data, boolean bench, boolean debug) {
		Settings.setSettings(exif, data, bench, debug, null);
	}
	
	/**
	 * get PhotoListIDfromImgID : Returns the PhotoList UID, given 
	 * an existing Img UID. Returns null if the img is not found
	 * @param imgId : the UID object of the Img you  want to find
	 * @return  UUID ret : The UUID of the gallery you were looking for
	 */
	public UUID getPlidFromImgid(UUID imgId)
	{
		UUID ret = null;
		for(UUID listID : ProcessingCtrl.loadedPhotoLists.keySet())
			if(ProcessingCtrl.loadedPhotoLists.get(listID).contains(imgId))
			{
				ret = listID;
				break;
			}
		return ret;
	}
	
	/**
	 * processing given a existing photolist or imgmodel uid, executes the 
	 * cv/ocr and returns the JSON object string
	 * @param id PhotoList or ImageModel Id
	 * @return String JSON representation of the given object
	 */
	public String processing(UUID id)
	{
		UUID gallery = this.getPlidFromImgid(id);
		if(gallery != null) {
			return imgProcessing(loadedPhotoLists.get(gallery).getPhoto(id));
		}
		
		else
		{
			return listProcessing(loadedPhotoLists.get(id));
		}
	}

	/**
	 * imgProcessing Takes an ImgModel object and launches the detection.
	 * Returns the JSON String, answer to the detection.
	 * @param im the ImgModel object you want to run detection on
	 * @return ret String JSON format, the Image with the detection done
	 */
	public String imgProcessing(ImgModel im)
	{
		im.setProcessed(true);
		im.getResult().addAll(engine.launchDetection(im));
		return im.toJSON();

	}
	
	/**
	 * listProcessing Takes a PhotoList object and launches the detection.
	 * Returns the JSON String, answer of the detection.
	 * @param pl the PhotoList object you want to run detection on
	 * @return String JSON format, the photolist with the detection done
	 */
	public String listProcessing(PhotoList pl)
	{
		ImgModel to_ret = null;
		for(ImgModel im : pl.getPhotolist())
		{
				to_ret = im;
				to_ret.setProcessed(true);
				to_ret.getResult().addAll(engine.launchDetection(im));
		}
			
		return pl.toJSON();
	}

	/**
	 * acknowledge Given a File f, returns the UUID of a constructed object
	 * not yet treated
	 * @param f File the file to load in the system
	 * @return UUID The uuid of the created object, either a PhotoList, or 
	 * a ImgModel, whether the file was a image or a directory
	 */
	public UUID acknowledge(File f)
	{
		if(!f.isDirectory())
		{
			PhotoList pl = new PhotoList(f.getParentFile());
			ImgModel im = pl.addPhoto(f);
			this.loadedPhotoLists.put(pl.getId(), pl);
			return(im.getId());
		}
		else
		{
			PhotoList pl = new PhotoList(f);
			for(File iff : f.listFiles())
				if(iff.isFile())
					pl.addPhoto(iff);
				else
					pl.getSublists().add(this.dir_ack(iff));
			this.loadedPhotoLists.put(pl.getId(), pl);
			return(pl.getId());
		}
	}

	/**
	 * Permits to recursively acknowledge directories
	 * Obviously private since you're not supposed to use it.
	 * @param f The File pointing to the directory to load
	 * @return Photolist representing the object for the directory.
	 */
	private PhotoList dir_ack(File f)
	{
		PhotoList pl = new PhotoList(f);
		for(File iff : f.listFiles())
			if(iff.isFile())
				pl.addPhoto(iff);
			else
				pl.getSublists().add(dir_ack(iff));
		return pl;
	}
}

