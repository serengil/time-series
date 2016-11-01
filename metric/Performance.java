package com.forecast.metric;

import java.util.List;
import com.forecast.entity.ForecastEntity;
import com.forecast.entity.HistoricalDataEntity;

public class Performance {
	
	public static double calculateMetrics(List<HistoricalDataEntity> trainset, List<ForecastEntity> forecastList){
		
		double mae = 0, mean = 0, forecastMean = 0;
		int instances = 0;
		
		//divide data set 80% for training, 20% for forecasting
		int trainSetSize = (int) trainset.size()*80/100;
		int predictSetSize = trainset.size() - trainSetSize;
		
		for (int i = 0; i < trainset.size(); i++) {

			if(i>trainSetSize){

				double actual = trainset.get(i).getActual();
				double forecast = forecastList.get(i).getForecast();
				
				System.out.println(actual+"\t"+forecast);

				double absoluteError = Math.abs(forecast - actual);

				mae = mae + absoluteError;
				mean = mean + actual;
				forecastMean = forecastMean + forecast;

				instances++;

			}

		}
		
		mae = mae / instances;
		mean = mean / instances;
		forecastMean = forecastMean / instances;
		
		//calculate correlation
		
		double correlation = 0, dividend = 0, firstDivider = 0, secondDivider = 0;
		
		for (int i = 0; i < trainset.size(); i++) {
			
			if(i > trainSetSize){
				
				double actual = trainset.get(i).getActual();
				double forecast = forecastList.get(i).getForecast();
				
				dividend = dividend + (actual - mean) * (forecast - forecastMean);
				
				firstDivider = firstDivider + (actual - mean) * (actual - mean);
				secondDivider = secondDivider + (forecast - forecastMean) * (forecast - forecastMean);
				
			}
			
		}
		
		correlation = dividend / Math.sqrt(firstDivider * secondDivider);
		if(true){
			System.out.println("mean: "+mean); 
			System.out.println("mae: "+mae);
			System.out.println("instances: "+instances);
			System.out.println("correlation: "+100*correlation);
			System.out.println("mae/mean: "+(100* mae) / mean);
		}
		
		return (100* mae) / mean;
		
	}

}
