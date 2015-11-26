package com.minhasKamal.skinDetector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class SkinDetectorTest {
	private static void recogniseImage(String testImagePath, String knowledgePath, double threshHold) throws IOException{
		
		double[][][] ratio = getData(knowledgePath);
		
		Mat matImage = Highgui.imread(testImagePath, Highgui.CV_LOAD_IMAGE_UNCHANGED);
		Mat matMask = new Mat(matImage.rows(), matImage.cols(), CvType.CV_8UC1);
		
		System.out.println("\n\n## \t\tProcessing Image...");	//notification
		int rows = matImage.rows();
		int cols = matImage.cols();
		for(int row=0, col; row<rows; row++){
			for(col=0; col<cols; col++){
				if(ratio[(int) matImage.get(row, col)[0]][(int) matImage.get(row, col)[1]]
						[(int) matImage.get(row, col)[2]] > threshHold){
					
					matMask.put(row, col, 250);
				}else{
					matMask.put(row, col, 0);
				}
			}
		}
		
		Highgui.imwrite(testImagePath+"_mask.jpg", matMask);
	}
	
	private static double[][][] getData(String knowledgePath) throws IOException{
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
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		String testImagePath = "data\\test.jpg";
		String knowledgePath = "data\\image_knowledge.dat";
		double threshHold = 0.7;
		
		try {
			recogniseImage(testImagePath, knowledgePath, threshHold);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("\n\n## \t\tSuccessful!!");
	}
}
