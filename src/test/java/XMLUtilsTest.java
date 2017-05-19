import difflib.DiffHTMLGenerator;
import difflib.DiffUtils;
import difflib.Patch;
import difflib.myers.Equalizer;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;


/**
 * Created by alekbaga on 06.04.2017.
 */
public class XMLUtilsTest {
    public static void main(String[] args) throws Exception{
        List<String> original = XMLUtils.fileToLines("src/main/resources/control2.xml");
        List<String> revised  = XMLUtils.fileToLines("src/main/resources/test2.xml");
        original.removeAll(Arrays.asList("", null));
        revised.removeAll(Arrays.asList("", null));

        //Custom equalizer that will take into account masked values ("###") and
        Equalizer equalizer = new Equalizer() {
            @Override
            public boolean equals(@Nullable Object original, @Nullable Object revised) {
                String[] splitOrig = original.toString().replaceAll("<","").replaceAll(">","").split("\\s");
                String[] splitRev = revised.toString().replaceAll("<","").replaceAll(">","").split("\\s");
                splitOrig=XMLUtils.removeNullValue(splitOrig);
                splitRev=XMLUtils.removeNullValue(splitRev);
                Arrays.sort(splitOrig);
                Arrays.sort(splitRev);
                if (original == null && revised == null) {
                    return true;
                } else if (Arrays.equals(splitOrig,splitRev)){
                    return true;
                } else if (original != null){
                    return original.toString().trim().equals(revised.toString().trim());
                }
                else {
                    return false;
                }
            }

            @Override
            public boolean skip(@Nullable Object original) {
                if (original.toString().contains("###")) {
                    return true;
                } else return false;
            }
        };

        //Use custom equalizer instead of the default one
        DiffHTMLGenerator html = new DiffHTMLGenerator(equalizer);
        // Compute diff. Get the Patch object. Patch is the container for computed deltas.
        Patch<String> patch = DiffUtils.diff(original, revised, equalizer);

        //Build HTML page with highlighted side-by-side diffs
        System.out.println(html.buildSideBySideHTML(original,revised,patch));


    }
}
