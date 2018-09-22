package Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by José Roberto on 11/03/2017.
 */

public class JsonParser<T> {
    final Class<T> tipoClasse;
    Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

    public JsonParser(Class<T> tipoClasse) {
        this.tipoClasse = tipoClasse;
    }

    public T toObject(String json) {
        return gson.fromJson(json, tipoClasse);
    }

    // pelo fato de T ser genérico, o Type não pode ser recuperado, sendo necessária a referência de vetor da classe
    public List<T> toList(String json, Class<T[]> classe) {
        T[] vetor = gson.fromJson(json, classe);
        if (vetor != null) {
            return Arrays.asList(vetor);
        } else {
            return new ArrayList<>();
        }

    }

    public String fromObject(T object) {
        return gson.toJson(object);
    }


}
