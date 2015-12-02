package model;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Contains the information of a race: bibs number, ranking, 
 * contestant's names, contestant's time.
 */
public class RaceData {
	
	/** 
	 * HashMap of the list of contestant.
	 * Key: bibs number.
	 * Value: Contestname.
	 */
	private HashMap<Integer, ContestantName<String, String>> contestantList = new HashMap<Integer, ContestantName<String,String>>();
	
	/**
	 * HashMap of the ranking of the race.
	 * Key: bibs number
	 * Value: rank of the contestant.
	 */
	private HashMap<Integer, Integer> ranking = new HashMap<Integer, Integer>();
	
	/**
	 * Total race time of each contestant.
	 * Key: bibs number.
	 * Value: date object of the contestant time (format: HH:mm:ss).
	 */
	private HashMap<Integer, Date> totalTime = new HashMap<Integer, Date>();
	
	/**
	 * 10 km time of each contestant.
	 * Key: bibs number.
	 * Value: date object of the contestant time (format: HH:mm:ss).
	 */
	private HashMap<Integer, Date> km10Time = new HashMap<Integer, Date>();
	
	/**
	 * Semi marathon time of each contestant.
	 * Key: bibs number.
	 * Value: date object of the contestant time (format: HH:mm:ss).
	 */
	private HashMap<Integer, Date> semiTime = new HashMap<Integer, Date>();
	
	/**
	 * 30 km time of each contestant.
	 * Key: bibs number.
	 * Value: date object of the contestant time (format: HH:mm:ss).
	 */
	private HashMap<Integer, Date> km30Time = new HashMap<Integer, Date>();

	/**
	 * Get the contestant list.
	 * @return a hashmap of the contestant list. (Key: bibs number, Value: ContestName)
	 */
	public HashMap<Integer, ContestantName<String, String>> getContestantList() {
		return this.contestantList;
	}
	
	/**
	 * Get the ranking list.
	 * @return a hashmap of the ranking. (Key: bibs number, Value: rank).
	 */
	public HashMap<Integer, Integer> getRanking() {
		return this.ranking;
	}
	
	/**
	 * Get the total time of each concurent.
	 * @return a hashmap of the total time. 
	 * (Key: bibs number, Value: Date (format HH:mm:ss)
	 */
	public HashMap<Integer,Date> getTotalTime() {
		return this.totalTime;
	}
	
	/**
	 * Get the 10 km time of each concurent.
	 * @return a hashmap of the 10 km time. 
	 * (Key: bibs number, Value: Date (format HH:mm:ss)
	 *
	 */
	public HashMap<Integer, Date> getKm10Time() {
		return this.km10Time;
	}
	
	/**
	 * Get the semi time of each concurent.
	 * @return a hashmap of the semi time. 
	 * (Key: bibs number, Value: Date (format HH:mm:ss)
	 */
	public HashMap<Integer, Date> getSemiTime() {
		return this.semiTime;
	}
	
	/**
	 * Get the 30 km time of each concurent.
	 * @return a hashmap of the 30 km time. 
	 * (Key: bibs number, Value: Date (format HH:mm:ss)
	 */
	public HashMap<Integer, Date> getKm30Time() {
		return this.km30Time;
	}

	/**
	 * Analyses a data file of a race and set the HashMap of the class.
	 * @param file - file to parse.
	 */
	public void parseDataRaceFile(File file) {
		FileInputStream xlsFile;
		Workbook workbook;
		try {
			xlsFile = new FileInputStream(file);
			workbook = new HSSFWorkbook(xlsFile);
			Sheet sheet = workbook.getSheetAt(0);

			int bibCol = 0, rankCol = 0, nameCol = 0, firstNameCol = 0, timeCol = 0, km10Col = 0, semiCol = 0, km30Col = 0;

			Row firstRow = sheet.getRow(sheet.getFirstRowNum());

			for (int colNum = 0 ; colNum < firstRow.getLastCellNum() ; colNum++) {
				String cellContent = firstRow.getCell(colNum, Row.RETURN_BLANK_AS_NULL).getStringCellValue();

				switch (cellContent.toUpperCase()) {
				case "PLACE":
					rankCol = colNum;
					break;
				case "DOSS":
					bibCol = colNum;
					break;
				case "NOM":
					nameCol = colNum;
					break;
				case "PRENOM":
					firstNameCol = colNum;
					break;
				case "TPS REEL":
					timeCol = colNum;
					break;
				case "KM10":
					km10Col = colNum;
					break;
				case "SEMI":
					semiCol = colNum;
					break;
				case "KM30":
					km30Col = colNum;
					break;
				}
			}

			for (int rowNum = sheet.getFirstRowNum() +1  ; rowNum <= sheet.getLastRowNum() ; rowNum++) {
				Row r = sheet.getRow(rowNum);
				// Get cells
				Cell bibCell = r.getCell(bibCol);
				Cell rankingCell = r.getCell(rankCol);
				Cell firstNameCell = r.getCell(firstNameCol);
				Cell nameCell = r.getCell(nameCol);
				Cell totalTimeCell = r.getCell(timeCol);
				Cell km10TimeCell = r.getCell(km10Col);
				Cell km30TimeCell = r.getCell(km30Col);
				Cell semiTimeCell = r.getCell(semiCol);

				// Get bib
				if (bibCell != null && bibCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					int bibNumber = (int) bibCell.getNumericCellValue();

					// Get rank
					if (rankingCell != null && rankingCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						// Add to ranking HashMap
						this.ranking.put(bibNumber, (int) rankingCell.getNumericCellValue());
					}

					// Get the name
					String strName = "";
					String strFirstName = "";
					if(firstNameCell != null && firstNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
						strFirstName = firstNameCell.getStringCellValue();	
					}
					if(nameCell != null && nameCell.getCellType() == Cell.CELL_TYPE_STRING) {
						strName = nameCell.getStringCellValue();
					}

					// Add to contesterList
					this.contestantList.put(bibNumber, new ContestantName<String, String>(strFirstName, strName));

					// Get the time
					if(totalTime != null && totalTimeCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						this.totalTime.put(bibNumber, totalTimeCell.getDateCellValue());
					}
					if (km10TimeCell != null && km10TimeCell.getCellType() == Cell.CELL_TYPE_STRING) {
						this.km10Time.put(bibNumber, km10TimeCell.getDateCellValue());
					}
					if (km30TimeCell != null && km30TimeCell.getCellType() == Cell.CELL_TYPE_STRING) {
						this.km30Time.put(bibNumber,  km30TimeCell.getDateCellValue());
					}
					if (semiTimeCell != null && semiTimeCell.getCellType() == Cell.CELL_TYPE_STRING) {				
						this.semiTime.put(bibNumber, semiTimeCell.getDateCellValue());						
					}

				}
			}
			xlsFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Name of a contestant.
	 * @author Xavier Leblond
	 *
	 * @param <F> firstname of a contestant.
	 * @param <L> lastname of a contestant.
	 */
	public class ContestantName<F, L> {
		/** Firstname of a contestant. */
		private final F firstName;
		
		/** Lastname of a contestant. */
		private final L lastName;

		/**
		 * Constructs a contestant name.
		 * @param firstName
		 * @param lastName
		 */
		public ContestantName(F firstName, L lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

		/**
		 * Get the firstname of a contestant.
		 * @return the firstname.
		 */
		public F getFirstName() {
			return this.firstName;
		}

		/**
		 * Get the lastname of a contestant.
		 * @return the lastname.
		 */
		public L getLastName() {
			return this.lastName;
		}
	}

}
