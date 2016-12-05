/***********************************************************
* Developer: Minhas Kamal (minhaskamal024@gmail.com)       *
* Date: 10-Mar-2015                                        *
* Modification Date: 30-Dec-2015                           *
* Modification Date: 22-Jan-2016                           *
* Modification Date: 03-Oct-2016                           *
* License: MIT License                                     *
***********************************************************/

package com.minhasKamal.skinDetector;


import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Matrix {
	/**
	 * Matrix types- <br/>
	 * 1. <code>BLACK_WHITE</code> -> grey-level image <br/>
	 * 2. <code>BLACK_WHITE_ALPHA</code> -> grey-level image with opacity <br/>
	 * 3. <code>RED_GREEN_BLUE</code> -> color image <br/>
	 * 4. <code>RED_GREEN_BLUE_ALPHA</code> color image with opacity <br/>
	 */
	public static final int BLACK_WHITE = 1,
			BLACK_WHITE_ALPHA = 2,
			RED_GREEN_BLUE = 3,
			RED_GREEN_BLUE_ALPHA = 4;
	
	/**
	 * <code>WHITE_PIXEL = 255</code>, highest value of a <code>pixel</code> <br/>
	 * <code>BLACK_PIXEL = 0</code>, lowest value of a <code>pixel</code>
	 */
	public static final int MAX_PIXEL = 255,
			MIN_PIXEL = 0;
	
	/**
	 * first dimension represents row or height from top to bottom ranging from 0 to height-1,
	 * second dimension represents column or width from left to right ranging from 0 to width-1,
	 * third dimension represents intensity value.
	 */
	public int[][][] pixels;
	
	
////----constructors----////
	
	
	public Matrix(int[][][] pixels){
		this.pixels = pixels;
	}
	
	/**
	 * When invalid info is provided Matrix is created with minimal size.
	 * @param row height of the image
	 * @param col width of the image
	 * @param type <code>BLACK_WHITE</code>, <code>BLACK_WHITE_ALPHA</code>, 
	 * <code>RED_GREEN_BLUE</code>, <code>RED_GREEN_BLUE_ALPHA</code>
	 */
	public Matrix( int row, int col, int type){
		if(row<1){
			row=1;
		}if(col<1){
			col=1;
		}if(type<Matrix.BLACK_WHITE || type>Matrix.RED_GREEN_BLUE_ALPHA){
			type = Matrix.BLACK_WHITE;
		}
		
		this.pixels = new int[row][col][type];
	}
	
	public Matrix(String imageFilePath, int type){
		BufferedImage bufferedImage = null;
		
		try{
			bufferedImage = ImageIO.read(new File(imageFilePath));
		}catch(Exception e){e.printStackTrace();}
		
		this.pixels = bufferedImageToMatrix(bufferedImage, type).pixels;
	}
	
	public Matrix(BufferedImage bufferedImage, int type){
		this.pixels = bufferedImageToMatrix(bufferedImage, type).pixels;
	}
	
	
////----getter setters----////
	
	
	/**
	 * @return height of image
	 */
	public int getRows(){
		return this.pixels.length;
	}
	
	/**
	 * @return width of image
	 */
	public int getCols(){
		return this.pixels[0].length;
	}
	
	/**
	 * @return 1 -> <code>BLACK_WHITE</code>,<br/>
	 * 2 -> <code>BLACK_WHITE_ALPHA</code>,<br/>
	 * 3 -> <code>RED_GREEN_BLUE</code>,<br/>
	 * 4 -> <code>RED_GREEN_BLUE_ALPHA</code>
	 */
	public int getType(){
		return this.pixels[0][0].length;
	}
	
	/**
	 * @param rowNo 0 =< rowNo < number of rows
	 * @param colNo 0 =< colNo < number of columns
	 */
	public int[] getPixel(int rowNo, int colNo){
		return this.pixels[rowNo][colNo].clone();
	}
	
	/**
	 * @param rowNo 0 =< rowNo < number of rows
	 * @param colNo 0 =< colNo < number of columns
	 */
	public void setPixel(int rowNo, int colNo, int[] value){
		this.pixels[rowNo][colNo] = value.clone();
	}

	
////----matrix-bufferedimage methods----////
	
	
	/**
	 * Creates <code>BufferedImage</code> from Matrix.
	 * @param matrix input matrix
	 * @return
	 */
	public static BufferedImage matrixToBufferedImage(Matrix matrix){
		BufferedImage bufferedImage = new BufferedImage(matrix.pixels[0].length, matrix.pixels.length,
				BufferedImage.TYPE_4BYTE_ABGR);
		
		int type = matrix.getType();
		if(type==Matrix.BLACK_WHITE){
			for(int i=0, j; i<matrix.pixels.length; i++){
				for(j=0; j<matrix.pixels[0].length; j++){
					int[] rgb = matrix.pixels[i][j];
					int color = (Matrix.MAX_PIXEL<<24) | (rgb[0]<<16) | (rgb[0]<<8) | rgb[0];
					bufferedImage.setRGB(j, i, color);
				}
			}
		}else if(type==Matrix.BLACK_WHITE_ALPHA){
			for(int i=0, j; i<matrix.pixels.length; i++){
				for(j=0; j<matrix.pixels[0].length; j++){
					int[] rgb = matrix.pixels[i][j];
					int color = (rgb[1]<<24) | (rgb[0]<<16) | (rgb[0]<<8) | rgb[0];
					bufferedImage.setRGB(j, i, color);
				}
			}
		}else if(type==Matrix.RED_GREEN_BLUE){
			for(int i=0, j; i<matrix.pixels.length; i++){
				for(j=0; j<matrix.pixels[0].length; j++){
					int[] rgb = matrix.pixels[i][j];
					int color = (Matrix.MAX_PIXEL<<24) | (rgb[0]<<16) | (rgb[1]<<8) | rgb[2];
					bufferedImage.setRGB(j, i, color);
				}
			}
		}else{
			for(int i=0, j; i<matrix.pixels.length; i++){
				for(j=0; j<matrix.pixels[0].length; j++){
					int[] rgb = matrix.pixels[i][j];
					int color = (rgb[3]<<24) | (rgb[0]<<16) | (rgb[1]<<8) | rgb[2];
					bufferedImage.setRGB(j, i, color);
				}
			}
		}
		
		return bufferedImage;
	}
	
	/**
	 * Creates Matrix from <code>BufferedImage</code>
	 * @param bufferedImage
	 * @param type type of the output Matrix
	 * @return
	 */
	public static Matrix bufferedImageToMatrix(BufferedImage bufferedImage, int type){
		int row = bufferedImage.getHeight();
		int col = bufferedImage.getWidth();
		
		if(type<Matrix.BLACK_WHITE || type>Matrix.RED_GREEN_BLUE_ALPHA){
			type = Matrix.BLACK_WHITE;
		}
		
		Matrix matrix = new Matrix(row, col, type);
		
		if(type==BLACK_WHITE){
			for(int i=0, j; i<row; i++){
				for(j=0; j<col; j++){
					int rgb = bufferedImage.getRGB(j, i);
					matrix.pixels[i][j] = new int[]{rgb>>8 & 0xFF};
				}
			}
		}else if(type==BLACK_WHITE_ALPHA){
			for(int i=0, j; i<row; i++){
				for(j=0; j<col; j++){
					int rgb = bufferedImage.getRGB(j, i);
					matrix.pixels[i][j] = new int[]{rgb>>8 & 0xFF, rgb>>24 & 0xFF};
				}
			}
		}else if(type==RED_GREEN_BLUE){
			for(int i=0, j; i<row; i++){
				for(j=0; j<col; j++){
					int rgb = bufferedImage.getRGB(j, i);
					matrix.pixels[i][j] = new int[]{rgb>>16 & 0xFF, rgb>>8 & 0xFF, rgb>>0 & 0xFF};
				}
			}
		}else{
			for(int i=0, j; i<row; i++){
				for(j=0; j<col; j++){
					int rgb = bufferedImage.getRGB(j, i);
					matrix.pixels[i][j] = new int[]{rgb>>16 & 0xFF, rgb>>8 & 0xFF, rgb>>0 & 0xFF, rgb>>24 & 0xFF};
				}
			}
		}
		
		return matrix;
	}
	
////----methods----////

	/**
	 * Crops a section out of the Matrix.
	 * @param rowStart 0 =< rowStart < rowEnd
	 * @param rowEnd rowStart < rowEnd <= numberOfRows-1
	 * @param colStart 0 =< colStart < colEnd
	 * @param colEnd colStart < colEnd <= numberOfCols-1
	 * @return a new Matrix
	 */
	public Matrix subMatrix(int rowStart, int rowEnd, int colStart, int colEnd){
		if(rowStart>=0 && colStart>=0 && rowEnd<=getRows() && colEnd<=getCols() &&
			rowEnd>rowStart && colEnd>colStart){
			
			int row = rowEnd-rowStart;
			int col = colEnd-colStart;
			
			Matrix matrix = new Matrix(row, col, this.getType());
			
			for(int i=0; i<row; i++){
				for(int j=0; j<col; j++){
					matrix.pixels[i][j] = this.pixels[rowStart+i][colStart+j].clone();
				}
			}
			
			return matrix;
			
		}else{
			return null;
		}
	}

	/**
	 * Clones the entire Matrix into a new instance.
	 */
	public Matrix clone(){
		return subMatrix(0, getRows(), 0, getCols());
	}
	
////----io operation----////
	
	/**
	 * Writes image in the hard-disk.
	 * @param filePath
	 * @throws IOException
	 */
	public void write(String filePath){
		BufferedImage bufferedImage = matrixToBufferedImage(this);
		try{
			ImageIO.write(bufferedImage, filePath.substring(filePath.lastIndexOf('.')+1), new File(filePath));
		}catch(Exception e){e.printStackTrace();}
	}
	
	/**
	 * @return Matrix as text
	 */
	public String dump(){
		int row = this.pixels.length;
		int col = this.pixels[0].length;
		int type = this.pixels[0][0].length;
		
		String string = "(" + row + "," + col + "," + type + ")";
		string += "[";
		
		String pixelString, rowPixelsString;
		for(int i=0, j; i<row; i++){
			
			rowPixelsString = "";
			for(j=0; j<col; j++){
				
				pixelString = "";
				for(int k=0; k<type; k++){
					pixelString += this.pixels[i][j][k] + ",";
				}
				
				rowPixelsString += pixelString;
			}
			
			string += rowPixelsString;
		}
		
		string += "]";
		
		return string;
	}
	
	/**
	 * Creates Matrix from text.
	 * @param string output of <code>dump()</code>
	 */
	public static Matrix load(String string){
		int startIndex=0, stopIndex=0;
		int row=1, col=1, type=1;
		
		startIndex = 1;
		stopIndex = string.indexOf(',', startIndex);
		row = Integer.valueOf(string.substring(startIndex, stopIndex));
		
		startIndex = stopIndex+1;
		stopIndex = string.indexOf(',', startIndex);
		col = Integer.valueOf(string.substring(startIndex, stopIndex));
		
		startIndex = stopIndex+1;
		stopIndex = string.indexOf(')', startIndex);
		type = Integer.valueOf(string.substring(startIndex, stopIndex));
		
		
		
		Matrix matrix = new Matrix(row, col, type);
		
		stopIndex = string.indexOf('[', stopIndex);
		for(int i=0; i<row; i++){
			for(int j=0; j<col; j++){
				
				for(int k=0; k<type; k++){
					startIndex = stopIndex+1;
					stopIndex = string.indexOf(',', startIndex);
					
					matrix.pixels[i][j][k] = Integer.valueOf(string.substring(startIndex, stopIndex));
				}
			}
		}
		
		return matrix;
	}
	
	/**
	 * Visualizes image as text.
	 */
	public String toString(){
		int row = this.pixels.length;
		int col = this.pixels[0].length;
		int type = this.pixels[0][0].length;
		
		String string = "(" + row + "," + col + "," + type + ")\n";
		string += "[\n";
		
		String pixelString, rowPixelsString;
		for(int i=0, j, k; i<row; i++){
	
			rowPixelsString = "[";
			for(j=0; j<col; j++){
				
				pixelString = "("+this.pixels[i][j][0];
				for(k=1; k<type; k++){
					pixelString += "." + this.pixels[i][j][k];
				}
				
				rowPixelsString += pixelString + ") ";
			}
			
			string += rowPixelsString+"]\n";
		}
		
		string += "]";
		
		return string;
	}
	
	
}
