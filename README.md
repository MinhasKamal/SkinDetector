# :dancer: Skin Detector
#### Detects Human Skin From Image

[This program](http://minhaskamal.github.io/SkinDetector) is a very simple machine learning implementation for image region segmentation. Only by altering training data it can detect any type of region based on pixel value.

### How to Run?
1. For training the system run **SkinDetectorTrainer.java**. After training a knowledge file is created.

  <div align="center">
    <img src="https://cloud.githubusercontent.com/assets/5456665/20896606/cbaa3d56-bb48-11e6-9277-c236eaf23b9b.png" alt="training" width="400" height=auto/>
  </div>

2. Then run **SkinDetectorTester.java** or **SkinDetectorTester2.java** for getting output (change file-paths in the main method according to the need).

   input-
     <div align="center">
       <img src="https://cloud.githubusercontent.com/assets/5456665/20897840/33f1084a-bb4e-11e6-90d0-3751ed9574e1.jpg" alt="input image" width="150" height=auto/>
     </div>
  
   output-
     <div align="center">
       <img src="https://cloud.githubusercontent.com/assets/5456665/20897841/3427d640-bb4e-11e6-8163-230587d5bb71.png" alt="outout image" width="150" height=auto/>
       <img src="https://cloud.githubusercontent.com/assets/5456665/20898055/0e565fd0-bb4f-11e6-9bea-963a333794d5.png" alt="outout image" width="150" height=auto/>
     </div>
  
### How It Works?
We have used [naive Bayes](https://en.wikipedia.org/wiki/Naive_Bayes_classifier) here for classification (skin or non-skin pixel). As it is a colour image there are 256\*256\*256 types of pixels. 

In the training phase, pixel frequencies of being skin or non-skin is calculated. We take every pixel of the image and see if it is a pixel of skin by using the mask. If the pixel is on skin, we increase its skin-frequency. Else we increase the non-skin-frequency. After processing all images, probability of a skin-pixels is calculated from the frequency using Bayes Theorem. We store this data in a file.

During testing, we simply map each pixel with the probability we calculated in training phase. If the probability is greater than a certain threshold, we mark that pixel as skin.

### License
<a rel="license" href="https://opensource.org/licenses/MIT"><img alt="MIT License" src="https://cloud.githubusercontent.com/assets/5456665/18950087/fbe0681a-865f-11e6-9552-e59d038d5913.png" width="60em" height=auto/></a><br/><a href="https://github.com/MinhasKamal/SkinDetector">SkinDetector</a> is licensed under <a rel="license" href="https://opensource.org/licenses/MIT">MIT License</a>.
