package br.com.app.vinygram.utils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class DateObject implements Serializable {
	
	
	private long hours;
	
	private long minutes;
	
	private long days;
	
	private long months;
	
	private long years;
	
	
	public long getDays() {
		return days;
	}
	
	public long getHours() {
		return hours;
	}
	
	public long getMinutes() {
		return minutes;
	}
	
	public long getMonths() {
		return months;
	}
	
	public long getYears() {
		return years;
	}
	
	public static DateObjectBuilder getBuilder() {
		return new DateObjectBuilder();
	}
	
	public static class DateObjectBuilder  {
		
		
		private DateObject dateObject;
		
		DateObjectBuilder() {
			
			this.dateObject = new DateObject();
		}
		
		
		public DateObjectBuilder prepareAttributtes(LocalDateTime localDateTime) {
			
			LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
			
			this.dateObject.hours = ChronoUnit.HOURS.between( localDateTime,now) ;
			this.dateObject.minutes = ChronoUnit.MINUTES.between(localDateTime,now) ;
			this.dateObject.days = ChronoUnit.DAYS.between(localDateTime,now) ;
			this.dateObject.months = ChronoUnit.MONTHS.between(localDateTime,now) ;
			this.dateObject.years = ChronoUnit.YEARS.between(localDateTime,now) ;
			
			return this;
		}
		
		public DateObject build(){
			
			return this.dateObject;
			
		}
		
		
	}
	
	

}
