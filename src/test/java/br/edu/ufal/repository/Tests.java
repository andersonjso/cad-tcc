package br.edu.ufal.repository;

import br.edu.ufal.AttributesNodule;
import br.edu.ufal.cad.cbir.isa.ISATexture;
import br.edu.ufal.cad.cbir.isa.NoduleRetrievalPerformanceEvaluation;
import br.edu.ufal.cad.cbir.isa.NoduleRetrievalPrecisionEvaluation;
import br.edu.ufal.cad.cbir.isa.SimilarNodule;
import org.dcm4che2.tool.dcm2jpg.Dcm2Jpg;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static javax.imageio.ImageIO.*;

/**
 * Created by andersonjso on 2/18/16.
 */
public class Tests {


    @Test
    public void aff() throws IOException {
        File img = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule0.png");
        File img2 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule1.png");
        File img3 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule2.png");
        File img4 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule3.png");
        File img5 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule4.png");
        File img6 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule5.png");
        File img7 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule6.png");
        File img8 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule7.png");
        BufferedImage[] images = {read(img), read(img2), read(img3), read(img4), read(img5), read(img6), read(img7),
                read(img8)};

        AttributesNodule attributesNodule = new AttributesNodule();

        double[] attribute = attributesNodule.getImageTextureAttributes(images);

        NoduleRetrievalPrecisionEvaluation noduleRetrievalPerformanceEvaluation =
                new NoduleRetrievalPrecisionEvaluation ();
//        List<SimilarNodule> nodules = noduleRetrievalPerformanceEvaluation.retrieveSimilarNodules(images);

        System.out.println();
    }

    @Test
    public void coverter() throws IOException {
        Dcm2Jpg dcm2Jpg = new Dcm2Jpg();

        dcm2Jpg.convert(new File("/Users/andersonjso/Downloads/teste.dcm"),
                new File("/Users/andersonjso/Downloads/anderson2015.png"));
    }
}
