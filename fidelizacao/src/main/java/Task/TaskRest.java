package Task;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Util.ReadStreamUtil;

/**
 * Created by Stefanini on 25/04/2017.
 */

public class TaskRest extends AsyncTask<String, Void, String> {

    // enumeração para indicar o tipo de método da requisição
    public enum RequestMethod {
        GET, POST, PUT, DELETE
    }

    // variável para controlar possíveis exceptions que possam ocorrer
    private Exception exception;
    // variável para indicar qual tipo de método a ser chamado
    private RequestMethod method;
    // handler para tratar dos eventos relacionados à task
    private HandlerTask handlerTask;
    // variável para armazenar o token, caso exista
    private String token;


    /**
     * @param method      Tipo de Requisição REST a ser realizada
     * @param handlerTask Handler com as ações a serem realizadas em cada etapa da task
     */
    public TaskRest(RequestMethod method, HandlerTask handlerTask) {
        this.method = method;
        this.handlerTask = handlerTask;
    }

    /**
     * @param method      Tipo de Requisição REST a ser realizada
     * @param handlerTask Handler com as ações a serem realizadas em cada etapa da task
     * @param token       Token Authorization para realizar a requisição REST
     */
    public TaskRest(RequestMethod method, HandlerTask handlerTask, String token) {
        this(method, handlerTask);
        this.token = token;
    }

    @Override
    protected void onPreExecute() {
        handlerTask.onPreHandle();
    }

    @Override
    protected String doInBackground(String... params) {
        String retorno = null;
        try {
            // O endereço será passado como a primeira String dos parâmetros
            String endereco = params[0];
            // Cria um objeto URL a partir do endereço
            URL url = new URL(endereco);
            // Extrai da URL uma conexão HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // tempo máximo de espera para conexão
            connection.setConnectTimeout(15000);
            // tempo máximo de espera para leitura
            connection.setReadTimeout(15000);
            // caso exista token, acrescenta-o na requisição
            if (token != null) {
                connection.setRequestProperty("Authorization", token);
            }
            // define o tipo de requisição de acordo com o método recebido via construtor
            connection.setRequestMethod(method.toString());
            // caso seja um POST ou PUT, deverá haver um segundo parâmetro que corresponde ao json que deve ser enviado
            // no corpo da requisição
            if (method == RequestMethod.POST || method == RequestMethod.PUT) {
                String json = params[1];
                // define o ContentType da requisição
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                // habilita a escrita na requisição
                connection.setDoOutput(true);
                // obtem um objeto OutputStream para gravar dados na requisição
                OutputStream outputStream = connection.getOutputStream();
                // escreve o JSON no corpo da requisição
                outputStream.write(json.getBytes("UTF-8"));
                // libera o output e fecha o recurso
                outputStream.flush();
                outputStream.close();
            }
            // obtém o http status da requisição
            int responseCode = connection.getResponseCode();
            switch (method) {
                case GET:
                case POST:
                case PUT:
                    if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                        // obtém um inputStream para ler o corpo da resposta
                        InputStream inputStream = connection.getInputStream();
                        // coloca na variável retorno a String lida no corpo da resposta
                        retorno = ReadStreamUtil.readStream(inputStream);
                        inputStream.close();
                    } else {
                        // dispara uma exceção com o método e o código da resposta
                        throw new Exception("Erro: " + responseCode);
                    }
                    break;
                case DELETE:
                    // a String vazia será usada para indicar no handler do postExecute que a requisição foi feita com sucesso
                    if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                        retorno = "";
                    } else {
                        throw new Exception("Erro: " + responseCode);
                    }


            }
            connection.disconnect();
        } catch (IndexOutOfBoundsException erro) {
            // uma IndexOuOfBounds ocorrerá caso não seja passado o segundo parâmetro quando a requisição for POST ou PUT
            exception = new Exception("Está faltando um parâmetro para a execução do método");
            erro.printStackTrace();
        } catch (Exception erro) {
            // coloca na variável exception o erro para ser tratado pelo método no handler invocado no post execute
            exception = erro;
            erro.printStackTrace();
        }
        return retorno;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            handlerTask.onSuccess(s);
        } else {
            handlerTask.onError(exception);
        }
    }
}
