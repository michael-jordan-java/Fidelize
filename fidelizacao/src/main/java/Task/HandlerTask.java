package Task;

/**
 * Created by Stefanini on 25/04/2017.
 */

public interface HandlerTask {
    void onPreHandle();

    void onSuccess(String valueRead);

    void onError(Exception erro);
}
