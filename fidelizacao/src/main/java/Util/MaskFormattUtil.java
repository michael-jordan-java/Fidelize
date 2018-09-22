package Util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public abstract class MaskFormattUtil {

    private static final String maskCPF = "###.###.###-##";
    private static final String maskCelular = "#####-####";


    public static String unmask(String s) {
        return s.replaceAll("[^0-9]*", "");
    }

    public static TextWatcher insert(final EditText editText, final Context context) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = MaskFormattUtil.unmask(s.toString());
                String maskAux = null;
                String mask = "";
                switch (str.length()) {
                    case 11:
                        maskAux = maskCPF;
                        break;
                    case 9:
                        maskAux = maskCelular;
                        break;
                    default:
                        maskAux = maskCPF;
                        break;
                }

                mask = maskAux;
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if ((m != '#' && str.length() > old.length()) || (m != '#' && str.length() < old.length() && str.length() != i)) {
                        mascara += m;
                        continue;
                    }

                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                editText.setText(mascara);
                editText.setSelection(mascara.length());
            }


            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
                Log.e("LENGHT", "" + s.length());
            }
        };
    }

}

