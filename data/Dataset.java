package com.forecast.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.forecast.entity.HistoricalDataEntity;

public class Dataset {
	
	public static List<HistoricalDataEntity> retrieveTrainset(String source) {

		List<HistoricalDataEntity> trainset = new ArrayList<HistoricalDataEntity>();

		try {

			BufferedReader br = new BufferedReader(new FileReader(source));

			String line = br.readLine();

			int i = 0;

			while (line != null) {

				// System.out.println(line);

				if (i > 0) {

					String[] currentLine = line.split(",");

					HistoricalDataEntity trainitem = new HistoricalDataEntity();
					
					String value = currentLine[0];
					
					trainitem.setActual(Double.parseDouble(value));
					trainitem.setIndex(i);

					trainset.add(trainitem);
					
				}

				i++;

				line = br.readLine();

			}

			br.close();

		} catch (Exception ex) {
			System.out.println(ex);
		}

		return trainset;

	}

}
