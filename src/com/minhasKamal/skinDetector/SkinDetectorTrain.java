package com.minhasKamal.skinDetector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class SkinDetectorTrain {
	public static void train(String imageFolderPath, String maskFolderPath){
		String[] imageFilePaths = getAllFiles(imageFolderPath);
		String[] maskFilePaths = getAllFiles(maskFolderPath);
		
		Mat matImageTemp;
		Mat matMaskTemp;
		int[][][] skinPixelNumber = new int[256][256][256];
		int[][][] nonskinPixelNumber = new int[256][256][256];
		
		System.out.println("\n\n### \t\tProcessing image...");	//notification
		for(int i=0; i<imageFilePaths.length; i++){
			matImageTemp = Highgui.imread(imageFilePaths[i], Highgui.CV_LOAD_IMAGE_UNCHANGED);
			matMaskTemp = Highgui.imread(maskFilePaths[i], Highgui.CV_LOAD_IMAGE_UNCHANGED);
			
//			System.out.println(imageFilePaths[i]);
//			System.out.println(maskFilePaths[i]);
			readSkinColor(matImageTemp, matMaskTemp, skinPixelNumber, nonskinPixelNumber);
			
			System.out.println("Image Processed: " + i);	//notification
		}
		
		try {
			System.out.println("\n\n### \t\tProcessing data...");	//notification
			getMainRation(skinPixelNumber, nonskinPixelNumber, imageFolderPath+"_knowledge.dat");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String[] getAllFiles(String folderPath) {
		File[] files = new File(folderPath).listFiles();
		ArrayList<String> filePathList = new ArrayList<String>();
		
		for(File file: files){
			filePathList.add(file.getAbsolutePath());
		}
		
		String[] filePaths = new String[filePathList.size()];
		filePathList.toArray(filePaths);
		
		return filePaths;
	}
	
	private static void readSkinColor(Mat matImage, Mat matMask, int[][][] skinPixelNumber, int[][][] nonskinPixelNumber){
		int rows = matImage.rows(),
			cols = matImage.cols();
		
		int red=0, green=0, blue=0;
		for(int row=0, col; row<rows; row++){
			for(col=0; col<cols; col++){
				red = (int) matImage.get(row, col)[0];
				green = (int) matImage.get(row, col)[1];
				blue = (int) matImage.get(row, col)[2];
				
				if(doesShowSkin(matMask.get(row, col))){
					skinPixelNumber[red][green][blue] ++;
				}else{
					nonskinPixelNumber[red][green][blue] ++;
				}
			}
		}
	}
	
	private static boolean doesShowSkin(double[] value){
		if(value[0]>250 && value[1]>250 && value[2]>250){
			return true;
		}else{
			return false;
		}
	}
	
	private static void getMainRation(int[][][] skinPixelNumber, int[][][] nonskinPixelNumber,
			String outputFilePath) throws IOException {
		
		int totalSkinPixelNumber=0;
		int totalNonskinPixelNumber=0;
		
		for(int i=0; i<256; i++){
			for(int j=0; j<256; j++){
				for(int k=0; k<256; k++){
					totalSkinPixelNumber += skinPixelNumber[i][j][k];
					totalNonskinPixelNumber += nonskinPixelNumber[i][j][k];
				}
			}
		}
		double totalNonskinSkinPixelRatio = totalNonskinPixelNumber / totalSkinPixelNumber;
		double ratio=0;
		double doubleTemp1, doubleTemp2;
		BufferedWriter mainBW = new BufferedWriter(new FileWriter(outputFilePath));
		mainBW.write("");
		for(int i=0; i<256; i++){
			for(int j=0; j<256; j++){
				for(int k=0; k<256; k++){
					doubleTemp1 = skinPixelNumber[i][j][k] * totalNonskinSkinPixelRatio;
					doubleTemp2 = nonskinPixelNumber[i][j][k];
					
					if(doubleTemp2==0){
						ratio=1;
					}else{
						ratio=doubleTemp1/doubleTemp2;
					}
					
					mainBW.append(ratio+"\n");
				}
			}
			
			System.out.print(".");	//notification
		}
		
		mainBW.close();
		
		return ;
	}
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		String imageFolderPath = "data\\image";
		String maskFolderPath = "data\\mask";
		
		train(imageFolderPath, maskFolderPath);
		
		System.out.println("\n\n## \t\tSuccessful!!!");
	}
}
