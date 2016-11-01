package com.forecast.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.forecast.data.Dataset;
import com.forecast.entity.ForecastEntity;
import com.forecast.entity.HistoricalDataEntity;
import com.forecast.entity.TESEntity;
import com.forecast.metric.Performance;

public class TripleExponentialSmoothing {

	public static void main(String[] args) throws Exception {
		
		System.out.println("program begins..."+new Date());
		
		String source = System.getProperty("user.dir")+"\\data\\sinus.txt";

		// define variables
		
		double ls = 0.5, ts = 0.9, ss = 0.1;
		int blockSize = 3;
		System.out.println("mae/mean: "+applyTripleExponentialSmoothing(source, blockSize, ls, ts, ss));
		
		/*
		//optimizer
		double minScore = 100;
		double minLS = 0, minTS = 0, minSS = 0, minBlock = 0;
		
		for(int i=0;i<10;i++){ //ls
			for(int j=0;j<10;j++){ //ts
				for(int k=0;k<10;k++){ //ss
					for(int s=2;s<15;s++){ //block size
						double ls = (double) i / 10;
						double ts = (double) j / 10;
						double ss = (double) k / 10;
						
						double mae = applyTripleExponentialSmoothing(source, s, ls, ts, ss);
											
						if(mae < minScore){
							
							minScore = mae * 1;
							minLS = ls * 1;
							minTS = ts * 1;
							minSS = ss * 1;
							minBlock = s * 1;
						
						}
						
					}
				}
			}
		}
		
		System.out.println("optimized parameters: block size: "+minBlock+", ls: "+minLS+", ts: "+minTS+", ss: "+minSS+", mae: "+minScore);
		*/
		// ------------------------
		
		System.out.println("program ends..."+new Date());

	}

	public static double applyTripleExponentialSmoothing(String source, int blockSize, double ls, double ts, double ss) {
		
		boolean dump = false;
		
		// read source from text
		List<HistoricalDataEntity> trainset = Dataset.retrieveTrainset(source);
		//System.out.println(trainset.size()+" items retrieved...");
		
		// initializtion

		List<TESEntity> trends = new ArrayList<TESEntity>();

		// level
		double level = 0;

		for (int i = 0; i < blockSize; i++) {

			level = level + trainset.get(i).getActual();

		}

		level = level / blockSize;

		// trend
		double trend = (trainset.get(0).getActual() - trainset.get(blockSize).getActual()) / blockSize;

		// seasonal

		double seasonal = 0;

		for (int i = 0; i < blockSize; i++) {

			seasonal = trainset.get(i).getActual() / level;

			TESEntity tesentity = new TESEntity();

			if (i != blockSize - 1) {

				tesentity.setLevel(0);
				tesentity.setTrend(0);

			} else {

				tesentity.setLevel(level);
				tesentity.setTrend(trend);

			}

			tesentity.setSeasonal(seasonal);

			trends.add(tesentity);

			if(dump)
				System.out.println(trainset.get(i).getActual()+"\t"+tesentity.getLevel()+"\t"+tesentity.getTrend()+"\t"+tesentity.getSeasonal());

		}

		if(dump)
			System.out.println("---------------------");

		for (int i = blockSize; i < trainset.size(); i++) {

			TESEntity tesentity = new TESEntity();

			level = ls * (trainset.get(i).getActual() / trends.get(i - blockSize).getSeasonal())
					+ (1 - ls) * (trends.get(i - 1).getLevel() + trends.get(i - 1).getTrend());

			trend = ts * (level - trends.get(i - 1).getLevel()) + (1 - ts) * trends.get(i - 1).getTrend();

			seasonal = ss * (trainset.get(i).getActual() / level)
					+ (1 - ss) * (trends.get(i - blockSize).getSeasonal());

			double forecast = (trends.get(i - 1).getLevel() + trends.get(i - 1).getTrend())
					* trends.get(i - blockSize).getSeasonal();

			tesentity.setLevel(level);
			tesentity.setTrend(trend);
			tesentity.setSeasonal(seasonal);
			tesentity.setForecast(forecast);

			trends.add(tesentity);

			if(dump)
				System.out.println(trainset.get(i).getActual()+"\t"+tesentity.getLevel()+"\t"+tesentity.getTrend()+"\t"+tesentity.getSeasonal()+" |\t"+tesentity.getForecast());
			 
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
