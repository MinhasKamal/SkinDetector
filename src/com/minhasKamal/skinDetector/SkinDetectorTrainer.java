/***********************************************************
* Developer: Minhas Kamal (minhaskamal024@gmail.com)       *
* Date: May-2015                                           *
* License: MIT License                                     *
***********************************************************/

package com.minhasKamal.skinDetector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SkinDetectorTrainer {
	public SkinDetectorTrainer() {

	}
	
	
	public void train(String imageFolderPath, String maskFolderPath) throws Exception{
		train(imageFolderPath, maskFolderPath, imageFolderPath+"_knowledge.dat");
	}
	
	public void train(String imageFolderPath, String maskFolderPath, String outputFilePath) throws Exception{
		String[] imageFilePaths = getAllFiles(imageFolderPath);
		String[] maskFilePaths = getAllFiles(maskFolderPath);
		
		Matrix matImageTemp;
		Matrix matMaskTemp;
		int[][][] skinPixelNumber = new int[256][256][256];
		int[][][] nonskinPixelNumber = new int[256][256][256];
		
		System.out.println("\n\n### \t\tProcessing image...");	//notification
		for(int i=0; i<imageFilePaths.length; i++){
			matImageTemp = new Matrix(imageFilePaths[i], Matrix.RED_GREEN_BLUE);
			matMaskTemp = new Matrix(maskFilePaths[i], Matrix.RED_GREEN_BLUE);
			
			readSkinColor(matImageTemp, matMaskTemp, skinPixelNumber, nonskinPixelNumber);
			
			System.out.println("Image Processed: " + i);	//notification
		}
		
		try {
			System.out.println("\n\n### \t\tProcessing data...");	//notification
			caculteProbability(skinPixelNumber, nonskinPixelNumber, outputFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	///////////////////////////////////////
	
	/**
	 * returns all file path in the root folder
	 */
	private String[] getAllFiles(String folderPath) {
		File[] files = new File(folderPath).listFiles();
		ArrayList<String> filePathList = new ArrayList<String>();
		
		for(File file: files){
			filePathList.add(file.getAbsolutePath());
		}
		
		String[] filePaths = new String[filePathList.size()];
		filePathList.toArray(filePaths);
		
		return filePaths;
	}
	
	/////////////////////////////////////////
	
	/**
	 * counts pixels of skin and non-skin in a image
	 */
	private void readSkinColor(Matrix matImage, Matrix matMask, int[][][] skinPixelNumber, int[][][] nonskinPixelNumber){
		int rows = matImage.getRows(),
			cols = matImage.getCols();
		
		int red=0, green=0, blue=0;
		for(int row=0, col; row<rows; row++){
			for(col=0; col<cols; col++){
				red = (int) matImage.pixels[row][col][0];
				green = (int) matImage.pixels[row][col][1];
				blue = (int) matImage.pixels[row][col][2];
				
				if(doesShowSkin(matMask.getPixel(row, col))){
					skinPixelNumber[red][green][blue] ++;
				}else{
					nonskinPixelNumber[red][green][blue] ++;
				}
			}
		}
	}
	
	private boolean doesShowSkin(int[] value){
		if(value[0]>250 && value[1]>250 && value[2]>250){
			return true;
		}else{
			return false;
		}
	}
	
	///////////////////////////////////////////
	
	/**
	 * calculates probability of a pixel resembling skin.
	 */
	private void caculteProbability(int[][][] skinPixelNumber, int[][][] nonskinPixelNumber,
			String outputFilePath) throws IOException {
		
		int totalSkinPixelNumber=0;
		int totalNonskinPixelNumber=0;
		for(int i=0; i<256; i++){
			for(int j=0; j<256; j++){
				for(int k=0; k<256; k++){
					skinPixelNumber[i][j][k] ++;
					totalSkinPixelNumber += skinPixelNumber[i][j][k];
					
					nonskinPixelNumber[i][j][k] ++;
					totalNonskinPixelNumber += nonskinPixelNumber[i][j][k];
				}
			}
		}
		
		double probabilityOfSkin = (double) totalSkinPixelNumber/(totalNonskinPixelNumber+totalSkinPixelNumber);
		double probability=0;
		BufferedWriter mainBW = new BufferedWriter(new FileWriter(outputFilePath));
		mainBW.write("");
		for(int i=0; i<256; i++){
			for(int j=0; j<256; j++){
				for(int k=0; k<256; k++){
										
					probability = skinPixelNumber[i][j][k]*probabilityOfSkin
							/(skinPixelNumber[i][j][k]+nonskinPixelNumber[i][j][k]);
					mainBW.append(String.format("%.3f\n", probability));
				}
			}
			
			System.out.print(".");	//notification
		}
		mainBW.close();
		
		return ;
	}
	
	//////////////////////////////////////////////
	
	/** MAIN **/
	public static void main(String[] args) {
		String imageFolderPath = "data\\image";
		String maskFolderPath = "data\\mask";
		
		try {
			new SkinDetectorTrainer().train(imageFolderPath, maskFolderPath);
			System.out.println("\n\n## \t\t Successful!!!");
		} catch (Exception e) {
			System.out.println("\n\n## \t\t File Path Error!!");
		}
	}
}
