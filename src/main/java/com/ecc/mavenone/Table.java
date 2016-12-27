package com.ecc.mavenone;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

public class Table {
	
	private int row;
	private int col;
	private Map<String,String> rowTable;
	private List<Map<String,String>> table;
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
	
	public void edit(String editStr, int nRow, int nCol, int keyOrValue){
		rowTable = table.get(nRow);
		if(checkDuplicate(editStr,rowTable)==true){
			System.out.println(editStr+" already found in one of the keys in row "+nRow+". Please try another string");
		}
		else{
			int iCol=0;
			String newKey= "";
			String newValue="";
			Map<String , String> newRowTable = new LinkedHashMap<String, String>();
			for(Map.Entry<String, String> map : rowTable.entrySet()){
				if(iCol==nCol){
					if(keyOrValue==1){
						newKey = editStr;
						newValue = map.getValue();
					}
					else{
						newKey= map.getKey();
						newValue = editStr;
					}
					newRowTable.put(newKey, newValue);
				}
				else{
					newRowTable.put(map.getKey(), map.getValue());
				}
				iCol++;
			}
			table.set(nRow, newRowTable);
		}
	}
	
	public boolean checkDuplicate(String editStr, Map<String,String> map){
		for(Map.Entry<String, String> cell : map.entrySet()){
			if(cell.getKey().equals(editStr)){
				return true;
			}
		}
		return false;
	}
	
	public void reset(int nRow, int nCol){
		row = nRow;
		col = nCol;
		table = new ArrayList<Map<String,String>>();
		for(int counterA=0;counterA<row;counterA++){
			rowTable = new HashMap<String,String>();
			for(int counterB=0;counterB<col;counterB++){
				rowTable.put(generateAscii(), generateAscii());
			}
			table.add(rowTable);
		}
		printToText();
	}
	
	public boolean readFromText(){
		try{
			Scanner x = new Scanner(new FileReader("fileto.txt"));
			table = new ArrayList<Map<String,String>>();
			int nRow=0;
			int totalCell=0;
			int nCol=0;
			while(x.hasNext()){
				rowTable = new HashMap<String,String>();
				String [] rows = x.nextLine().split("\t");
				for(String y : rows){
					rowTable.put(y.substring(0,3), y.substring(y.length()-3,y.length()));
					totalCell++;
				}
				table.add(rowTable);
				nRow++;
			}
			row=nRow;
			col=totalCell/row;
			System.out.println("ariel");
			System.out.println(row+" "+col);
			x.close();
			return true;
		}
		catch(Exception e){
			System.out.println("No file found");
			return false;
		}
	}
	
	public void printToText(){
		try {
			PrintWriter x = new PrintWriter("fileto.txt");
			int iRow=0;
			for(Map<String,String> rowTable : table){
				int iCol=0;
				StringBuilder sb = new StringBuilder();
				for(String key : rowTable.keySet()){
					sb.append(key+"  "+rowTable.get(key));
					if(iCol<col-1){
						sb.append("\t");
					}
					iCol++;
				}
				if(iRow==row-1)
					x.print(sb);
				else
					x.println(sb);
				iRow++;
			}
			x.close();
		} catch (FileNotFoundException e) {
			System.out.println("error");
		}
	}
	
	
	
	public void addRow(int addNumRow){
		for(int counterA=0;counterA<addNumRow;counterA++){
			rowTable = new HashMap<>();
			for(int counterB=0;counterB<col;counterB++){
				rowTable.put(generateAscii(), generateAscii());
			}
			table.add(rowTable);
		}
		row+=addNumRow;
	}
	
	public void sort(){
		for(int counter=0;counter<row;counter++){
			Map<String, String> sortedMap = new TreeMap<>();
			sortedMap.putAll(table.get(counter));
			table.set(counter, sortedMap);
		}		
		Collections.sort(table, new Comparator<Map<String,String>>(){
			public int compare(Map<String, String> o1, Map<String, String> o2) {
				String s1 = o1.keySet().iterator().next();
				String s2 = o2.keySet().iterator().next();
				return s1.compareTo(s2);
			}
		});
	}
	
	public void search(String searchStr){
		int sum=0;
		int iRow=0;
		for(Map<String,String> rowTable : table){
			int iCol=0;
			for(String key: rowTable.keySet()){
				int keyOccur= checkOccurence(searchStr, key);
				int valueOccur=checkOccurence(searchStr,rowTable.get(key));
				if(keyOccur!=0){
					System.out.println("String "+searchStr+" found in coordinate ["+iRow+"] ["+iCol+"] in key "+keyOccur+" time/s");
				}
			if(valueOccur!=0){
					System.out.println("String "+searchStr+" found in coordinate ["+iRow+"] ["+iCol+"] in value "+valueOccur+" time/s");
				}
				iCol++;
				sum+=keyOccur+valueOccur;
			}
			iRow++;
		}
		if(sum!=0){
			System.out.println("String "+searchStr+" found "+sum+" time/s");
		}
		else{
			System.out.println("String "+searchStr+" not found. ");
		}
	}
	
	public int checkOccurence(String searchStr, String arrayStr){
		if(arrayStr.length()<searchStr.length()){
			return 0;
		}
		else{
			if(arrayStr.substring(0,searchStr.length()).equals(searchStr))
				return 1+checkOccurence(searchStr,arrayStr.substring(1,arrayStr.length()));
			else
				return checkOccurence(searchStr,arrayStr.substring(1,arrayStr.length()));
		}
	}
	
	public void print(){
		for(Map<String, String> rowTable : table){
			int iCol=0;
			StringBuilder sb = new StringBuilder();
			for(String key: rowTable.keySet()){
				sb.append(key+"  "+rowTable.get(key));
				if(iCol!=col-1){
					sb.append("\t");
				}
				iCol++;
			}
			System.out.println(sb);
		}
	}
	
	public String generateAscii(){
		Random ran = new Random();
		String ascii="";
		for(int counter=0;counter<3;counter++){
			ascii+=(char)(ran.nextInt(89)+33);
		}
		return ascii;	
	}
	
}
