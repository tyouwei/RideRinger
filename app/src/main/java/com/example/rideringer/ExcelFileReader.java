package com.example.rideringer;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Pair;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class ExcelFileReader {
    InputStream myInput;
    Workbook workbook;
    Sheet sheet;

    public Sheet readExcelFromStorage(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();
        try {
            myInput = assetManager.open(fileName);
            workbook = new HSSFWorkbook(myInput);
            sheet = workbook.getSheetAt(0);
            return sheet;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Pair<String, String>> populateOnLineCode(Sheet sheet, String inputLineCode) {
        ArrayList<Pair<String, String>> stations = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        for (Row row : sheet) {
            Iterator<Cell> cellIterator = row.cellIterator();
            Cell idCell = cellIterator.next();
            Cell lineCodeCell = cellIterator.next();
            Cell stationCodeCell = cellIterator.next();
            Cell stationNameCell = cellIterator.next();

            String lineCode = formatter.formatCellValue(lineCodeCell);

            if (lineCode.equals(inputLineCode)) {
                String stationCode = formatter.formatCellValue(stationCodeCell);
                String stationName = formatter.formatCellValue(stationNameCell);
                if (!stationName.equals("NIL")) {
                    stations.add(new Pair(stationCode,stationName));
                }
            }
        }
        return stations;
    }
}
