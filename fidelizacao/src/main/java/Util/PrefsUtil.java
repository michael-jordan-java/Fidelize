package Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import Model.Cliente;

/**
 * Created by Stefanini on 25/04/2017.
 */

public class PrefsUtil {
    public static void salvarLogin(Context context, String cliente) {
        //Pegando a preferencia default
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        //Editando a preferencia default
        SharedPreferences.Editor editor = preferences.edit();
        //Adicionando as informações do login na preferencia
        editor.putString("login", cliente);
        editor.commit();
    }

    public static Cliente getLogin(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (preferences.getString("login", "").isEmpty()) {
            return null;
        } else {
            String preferencesStringAdm = preferences.getString("login", "");

            if(preferencesStringAdm.isEmpty()) {
                return null;
            }else{
                Cliente cliente = new JsonParser<Cliente>(Cliente.class).toObject(preferencesStringAdm);
                return cliente;
            }

        }
    }
}
