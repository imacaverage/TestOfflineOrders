package testofflineorders;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Граббер ордеров
 * Created by iMacAverage on 05.02.16.
 */
public class Grabber implements Callable<List<Integer>> {

    private final MCPSDao mcpsDao;

    private final int countGrab;

    private final boolean oldGrab;

    public Grabber(MCPSDao mcpsDao, int countGrab, boolean oldGrab) {
        this.mcpsDao = mcpsDao;
        this.countGrab = countGrab;
        this.oldGrab = oldGrab;
    }

    @Override
    public List<Integer> call() throws Exception {
        List<Integer> list = new ArrayList<>();
        String whoDo = Thread.currentThread().getName();
        while (this.mcpsDao.isExistOrderHeap()) {
            // захватываю ордера
            if (this.oldGrab)
                this.mcpsDao.grabOldOrderHeap(this.countGrab, whoDo);
            else
                this.mcpsDao.grabNewOrderHeap(this.countGrab, whoDo);
            // вычитываю захваченные ордера
            list.addAll(this.mcpsDao.getIdProcessedOrderHeap(whoDo));
            // удаляю вычитанные ордера
            this.mcpsDao.delOrderHeap(whoDo);
        }
        return list;
    }

}
