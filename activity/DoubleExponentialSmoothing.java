package com.forecast.activity;

/*
		*
		* @author Sefik Ilkin Serengil
		* initialization: 2016-11-01
		*
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.forecast.data.Dataset;
import com.forecast.entity.DESEntity;
import com.forecast.entity.ForecastEntity;
import com.forecast.entity.HistoricalDataEntity;
import com.forecast.metric.Performance;

public class DoubleExponentialSmoothing {
	
	public static void main(String[] args) {
		
		String source = System.getProperty("user.dir")+"\\data\\sinus.txt";
		
		applyDoubleExponentialSmoothing(source, 0.06, 0.03);
		
		/*
		//optimizer
		double minScore = 100;
		double minAlpha = 0, minBeta = 0;
		
		for(int i=0;i<100;i++){ //alpha
			for(int j=0;j<100;j++){ //beta
				
				double alpha = (double) i / 100;
				double beta = (double) j / 100;
				
				double mae = applyDoubleExponentialSmoothing(source, alpha, beta);
				
				if(mae < minScore){
					
					minScore = mae * 1;
					minAlpha = alpha * 1;
					minBeta = beta *1;
					
				}
				
			}
		}
		
		System.out.println("optimized parameters: alpha:"+minAlpha+", beta: "+minBeta+", mea:"+minScore);
		*/
	}
	
	public static double applyDoubleExponentialSmoothing(String source, double alpha, double beta) {
		
		List<HistoricalDataEntity> trainset = Dataset.retrieveTrainset(source);
		List<DESEntity> trends = new ArrayList<DESEntity>();
		
		for(int i=0;i<trainset.size();i++){
			
			DESEntity entity = new DESEntity();
			
			double base, trend, forecast;
			
			if(i == 0){
				base = trainset.get(i).getActual();
				trend = 0;
				forecast = 0;
				
				
				
			}
			else{
				
				base = alpha * trainset.get(i-1).getActual() + (1-alpha) * trends.get(i-1).getBase();
				trend = beta * (base - trends.get(i-1).getBase()) + (1-beta) * trends.get(i-1).getTrend();
				forecast = trends.get(i-1).getBase() + trends.get(i-1).getTrend();
				
			}
			
			entity.setBase(base);
			entity.setTrend(trend);
			entity.setForecast(forecast);
			
			trends.add(entity);
			
		}
		
		//calculate performance
		
		List<ForecastEntity> forecastList = new ArrayList<ForecastEntity>();
		
		for(int i=0;i<trends.size();i++){
			
			ForecastEntity entity = new ForecastEntity();
			entity.setForecast(trends.get(i).getForecast());
			forecastList.add(entity);
						
		}
		
		return Performance.calculateMetrics(trainset, forecastList);
		
	}

}
