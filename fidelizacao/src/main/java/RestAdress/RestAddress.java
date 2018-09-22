package RestAdress;

/**
 * Created by Stefanini on 25/04/2017.
 */

public class RestAddress {
    //Endereco base da API
    public static final String URL = "http://172.18.36.28:8080/Fidelizacao";

    //Endereços de recursos da API
    public static final String LOGIN = URL + "/rest/cliente/logar";
    public static final String CADASTRAR_CLIENTE = URL + "/rest/cliente";
}
