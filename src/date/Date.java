package date;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Date class calculates the date and time
 * @author Vachia Thoj
 *
 */
public class Date implements Serializable
{
	//For serialization
	private static final long serialVersionUID = 1L;
	
	private int month;
	private int day;
	private int year;
	
	private int hour;
	private int minute;
	
	/**
	 * Constructor
	 */
	public Date()
	{
		this.month = -1;
		this.day = -1;
		this.year = -1;
		this.hour = -1;
		this.minute = -1;
	}
	
	//Getter Methods
	public int getMonth() {return month;}
	public int getDay() {return day;}
	public int getYear() {return year;}
	public int getHour() {return hour;}
	public int getMinute() {return minute;}
	
	/**
	 * Method that calculates the date and time right now
	 */
	public void calculateDate()
	{
		month = LocalDateTime.now().getMonthValue();
		day = LocalDateTime.now().getDayOfMonth();
		year = LocalDateTime.now().getYear();
		
		hour = LocalDateTime.now().getHour();
		minute = LocalDateTime.now().getMinute();
	}
	
	/**
	 * Method that obtains the date as a String
	 * @return (String) returns the date as a String
	 */
	public String toString()
	{
		if(minute < 10)
		{
			return (month + "/" + day + "/" + year + " " + hour + ":" + "0" + minute);
		}
		else
		{
			return (month + "/" + day + "/" + year + " " + hour + ":" + minute);
		}
	}
}
