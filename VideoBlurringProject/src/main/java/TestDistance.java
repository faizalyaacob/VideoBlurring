
import org.datavec.image.loader.NativeImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.nd4j.linalg.ops.transforms.Transforms.cosineSim;
import static org.nd4j.linalg.ops.transforms.Transforms.euclideanDistance;

public class TestDistance {
    public static void main(String[] args) throws IOException {
        VGG16FeatureProvider faceIdentifier = new VGG16FeatureProvider();

        String PATH = "C:\\Users\\Asus\\Desktop\\CDLE project data\\";
        String img1 = "shamala1.jpg";
        String img2 = "zufar1.jpg";
        String img3 = "shamala2.jpg";


        File file1 = new File(PATH + img1);
        File file2 = new File(PATH + img2);
        File file3 = new File(PATH + img3);

        NativeImageLoader nil = new NativeImageLoader(224,224,3);
        INDArray image1 = nil.asMatrix(file1);
        INDArray image2 = nil.asMatrix(file2);
        INDArray image3 = nil.asMatrix(file3);


        INDArray emb1 = faceIdentifier.getEmbeddings(image1);
        INDArray emb2 = faceIdentifier.getEmbeddings(image2);
        INDArray emb3 = faceIdentifier.getEmbeddings(image3);


        System.out.println(Arrays.toString(emb1.shape()));
        System.out.println("Euclidist");
        System.out.println(euclideanDistance(emb1, emb2));
        System.out.println(euclideanDistance(emb1, emb3));
        System.out.println("CosineSim");
        System.out.println(1-cosineSim(emb1, emb2));
        System.out.println(1-cosineSim(emb1, emb3));
    }
}
