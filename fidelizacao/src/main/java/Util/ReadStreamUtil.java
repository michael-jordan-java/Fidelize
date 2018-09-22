package Util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by José Roberto on 09/03/2017.
 */

public class ReadStreamUtil {
    public static String readStream(InputStream inputStream) throws Exception {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String linha = null;
            while ((linha = reader.readLine()) != null) {
                builder.append(linha + "\n");
            }
            reader.close();
            return builder.toString();
        } catch (Exception erro) {
            throw erro;
        }
    }
}
