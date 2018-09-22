package Task;

/**
 * Created by José Roberto on 09/03/2017.
 */

public abstract class HandlerTaskAdapter implements HandlerTask {
    @Override
    public void onPreHandle() {
    }

    @Override
    public void onSuccess(String valueRead) {
    }

    @Override
    public void onError(Exception erro) {
    }
}
