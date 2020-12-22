package br.com.app.vinygram.utils;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DateDiferencesPosts implements Serializable{

	private static final long serialVersionUID = 1L;

	private  MessageDateType typeMessage;
	
	private long diferences;
	
	private LocalDateTime datePosTime;
	
	
	public long getDiferences() {
		return diferences;
	}
	
	
	public String toString() {
	
		return  diferences == 0 ? this.typeMessage.toString() : diferences + " "+ this.typeMessage.toString();
	}
	
	public MessageDateType getTypeMessage() {
		return typeMessage;
	}
	
	public static BuilderDate getBuider() {
		return new BuilderDate();
	}
	
	public LocalDateTime getDatePosTime() {
		return datePosTime;
	}
	
	public static class BuilderDate {
		
		private DateDiferencesPosts diferencesDatePosts;
		
		
		 BuilderDate() {
			diferencesDatePosts = new DateDiferencesPosts();
		}
		 
		 
		public BuilderDate addDataDatePostTime(LocalDateTime datePost) {
			diferencesDatePosts.datePosTime = datePost;
			return this;
		}
		
		public BuilderDate verifyDiferencesDate() {
			
			LocalDateTime dtPosTime = diferencesDatePosts.datePosTime;
			
			//logica
			DateObject dateObject = DateObject.getBuilder().prepareAttributtes(dtPosTime).build();
		
			
			if(dateObject.getYears() < 1) {	
				
				if(dateObject.getMonths() < 1) 
				{	
				
					if(dateObject.getDays() < 1) 
					{		
						if(dateObject.getHours() < 1) 
						{			
							if(dateObject.getMinutes() < 1) 
							{
								this.diferencesDatePosts.typeMessage = MessageDateType.NOW;
								this.diferencesDatePosts.diferences = 0;
							}else 
							{
								this.diferencesDatePosts.diferences = dateObject.getMinutes();
								this.diferencesDatePosts.typeMessage = (this.diferencesDatePosts.diferences == 1) ? MessageDateType.DIFERENCES_MINUTE:MessageDateType.DIFERENCES_MINUTES;	
							}	
						}else 
						{
							this.diferencesDatePosts.diferences = dateObject.getHours();
							this.diferencesDatePosts.typeMessage = (this.diferencesDatePosts.diferences == 1) ? MessageDateType.DIFERENCES_HOUR:MessageDateType.DIFERENCES_HOURS;	
						}	
					}else 
					{
						this.diferencesDatePosts.diferences = dateObject.getDays();
						this.diferencesDatePosts.typeMessage = (this.diferencesDatePosts.diferences == 1) ? MessageDateType.DIFERENCES_DAY:MessageDateType.DIFERENCES_DAYS;	
					}
				}else 
				{
					this.diferencesDatePosts.diferences = dateObject.getMonths();
					this.diferencesDatePosts.typeMessage = (this.diferencesDatePosts.diferences == 1) ? MessageDateType.DIFERENCES_MONTH:MessageDateType.DIFERENCES_MONTHS;	
				}
			}else 
			{
				this.diferencesDatePosts.diferences = dateObject.getYears();
				this.diferencesDatePosts.typeMessage = (this.diferencesDatePosts.diferences == 1) ? MessageDateType.DIFERENCES_YEAR:MessageDateType.DIFERENCES_YEARS;	
			}
			
			return this;
		}
		
		 
		public DateDiferencesPosts build() {
			return diferencesDatePosts;
		}
		
	}
	
	 enum MessageDateType {
		
		DIFERENCES_MONTHS("meses atrás."),
		DIFERENCES_MONTH("mês atrás."),
		DIFERENCES_DAYS("dias atrás."), 
		DIFERENCES_DAY("dia atrás."), 
		DIFERENCES_HOURS("horas atrás."), 
		DIFERENCES_HOUR("hora atrás."), 
		DIFERENCES_MINUTE("minuto atrás."),
		DIFERENCES_MINUTES("minutos atrás."),
		NOW("agora mesmo."),
		DIFERENCES_YEARS("anos atrás."),
		DIFERENCES_YEAR("ano atrás.");
		
		private MessageDateType(String value) {
			this.value = value;
		}
		 
		private String value;
		
		
		@Override
		public String toString() {
			return this.value;
		}
		
	}
}
