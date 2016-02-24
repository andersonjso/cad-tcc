package br.edu.ufal;

import br.edu.ufal.cad.cbir.texture.coocurrencematrix.CMTextureAttributes;

import java.awt.image.BufferedImage;

/**
 * Created by andersonjso on 2/23/16.
 */
public class AttributesNodule {
    private final int SIZEARRAY = 36;

    public double[] getImageTextureAttributes(BufferedImage[] imagesReference) {
        CMTextureAttributes att = new CMTextureAttributes(imagesReference);
        double[] arrayReference;
        double[] atts;

        arrayReference = new double[SIZEARRAY];
        int p = 0;

        atts = att.get0to0DegreeFeaturesVector();
        for (double d : atts) {
            arrayReference[p] = d;
            p++;
        }

        atts = att.get0to135DegreeFeaturesVector();
        for (double d : atts) {
            arrayReference[p] = d;
            p++;
        }

        atts = att.get0to45DegreeFeaturesVector();
        for (double d : atts) {
            arrayReference[p] = d;
            p++;
        }

        atts = att.get0to90DegreeFeaturesVector();
        for (double d : atts) {
            arrayReference[p] = d;
            p++;
        }

        return arrayReference;
    }
}
