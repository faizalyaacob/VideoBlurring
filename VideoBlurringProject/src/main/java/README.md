# BlurMe Beta v1.0
This programme takes an .mp4 video as an input. The user then selects the faces that they want to keep, then the rest of the faces in the video will be blurred. The video that is blurred will then be saved in the local directory with output.mp4.

<img width="200" src=https://www.facepixelizer.com/facepixelizerHelpImages/LanBlurred.jpg>

---
##Steps
1. Download the whole folder.   
2. Import pom.xml as project in IntelliJ.
3. It will take some time for the dependencies to be resolved (-_- stay patient..)
4. Once the dependencies are resolved, open the Test.java file and modify the vid path to a .mp4 video in your local machine.(Try you video not shorter than 10s)
5. Run the Test.java file. Pick the face to not get blurred and let the programme run.
6. Video with the title "output.mp4" will be generated in the root directory.
---

##Deep Learning Applications
1. Face Detection - Uses a pretrained model (VGG16) to obtain face embeddings.
2. VGG16, ResNet10, Clustering (DBScanner), 
3. 

---
##Limitations
1. Works best for more static video.
2. The longer the video better blurring, but process time longer.