package com.forecast.activity;

/*
		*
		* @author Sefik Ilkin Serengil
		* initialization: 2016-11-01
		*
*/

import java.util.ArrayList;
import java.util.List;

import com.forecast.data.Dataset;
import com.forecast.entity.ForecastEntity;
import com.forecast.entity.HistoricalDataEntity;
import com.forecast.metric.Performance;

public class SingleExponentialSmoothing {
	
	public static void main(String[] args) {
		
		String source = System.getProperty("user.dir")+"\\data\\sinus.txt";
		
		applySingleExponentialSmoothing(source, 0);
		
		/*
		//optimizer
		double minScore = 100;
		double minAlpha = 0;
		
		for(int i=0;i<100000;i++){ //alpha
			
			double alpha = (double) i / 100000;
			
			double mae = applySingleExponentialSmoothing(source, alpha);
			
			if(mae < minScore){
				minScore = mae * 1;
				minAlpha = alpha * 1;
			}
			
		}
		
		System.out.println("optimized parameters: alpha: "+minAlpha+", mae: "+minScore);
		*/
	}
	
	public static double applySingleExponentialSmoothing(String source, double alpha){
		
		// read source from text
		List<HistoricalDataEntity> trainset = Dataset.retrieveTrainset(source);
		
		List<ForecastEntity> trends = new ArrayList<ForecastEntity>();
		
		ForecastEntity entity = new ForecastEntity();
		entity.setForecast(trainset.get(0).getActual());
		trends.add(entity);
		
		for(int i=1;i<trainset.size();i++){
			
			double forecast = alpha * trainset.get(i-1).getActual() + (1-alpha) * trends.get(i-1).getForecast();
			
			entity = new ForecastEntity();
			entity.setForecast(forecast);
			trends.add(entity);
			
		}
		
		//---------------------------------
		
		//calculate performance
		
		List<ForecastEntity> forecastList = new ArrayList<ForecastEntity>();
		
		for(int i=0;i<trends.size();i++){
			
			ForecastEntity fentity = new ForecastEntity();
			fentity.setForecast(trends.get(i).getForecast());
			forecastList.add(fentity);
						
		}
		
		return Performance.calculateMetrics(trainset, forecastList);
		
	}

}
