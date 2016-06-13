package com.minhasKamal.skinDetector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.minhaskamal.egami.matrix.Matrix;

public class SkinDetectorTester {
	public void recogniseImage(String testImagePath, String knowledgeFilePath) throws Exception{
		recogniseImage(testImagePath, knowledgeFilePath, 0.7);
	}
	
	public void recogniseImage(String testImagePath, String knowledgeFilePath, double threshHold) throws Exception{
		recogniseImage(testImagePath, knowledgeFilePath, testImagePath+"_mask.png", threshHold);
	}
	
	public void recogniseImage(String testImagePath, String knowledgeFilePath, String outputFilePath, double threshHold) throws Exception{
		
		double[][][] ratio = getData(knowledgeFilePath);
		
		Matrix matImage = new Matrix(testImagePath, Matrix.RED_GREEN_BLUE);
		Matrix matMask = new Matrix(matImage.getRows(), matImage.getRows(), Matrix.BLACK_WHITE);
		
		System.out.println("\n\n## \t\tProcessing Image...");	//notification
		int rows = matImage.getRows();
		int cols = matImage.getCols();
		for(int row=0, col; row<rows; row++){
			for(col=0; col<cols; col++){
				if(ratio[matImage.pixels[row][col][0]][matImage.pixels[row][col][1]]
						[matImage.pixels[row][col][2]] > threshHold){
					
					matMask.pixels[row][col][0] = 250;
				}else{
					matMask.pixels[row][col][0] = 0;
				}
			}
		}
		
		matMask.write(outputFilePath);
	}
	
	private double[][][] getData(String knowledgePath) throws IOException{
		String string = "";	//for temporary data store
		BufferedReader mainBR = new BufferedReader(new FileReader(knowledgePath));
		double[][][] ratio = new double[256][256][256];
		
		System.out.println("## \t\tReading Knowledge File...");
		for(int i=0; i<256; i++){
			for(int j=0; j<256; j++){
				for(int k=0; k<256; k++){
					string = mainBR.readLine();
					ratio[i][j][k] = Double.parseDouble(string);
				}
			}
			System.out.print(".");	//notification
		}
		
		mainBR.close();
		
		return ratio;
	}
	
	/** MAIN **/
	public static void main(String[] args) {
		
		String testImagePath = "data\\test.jpg";
		String knowledgePath = "data\\image_knowledge.dat";
		
		try {
			new SkinDetectorTester().recogniseImage(testImagePath, knowledgePath);
			System.out.println("\n\n## \t\tSuccessful!!");
		} catch (Exception e) {
			System.out.println("\n\n## \t\t File Path Error!!");
		}
	}
}
