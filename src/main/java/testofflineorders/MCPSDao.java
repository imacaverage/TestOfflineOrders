package testofflineorders;

import java.util.List;

/**
 * Интерфейс для работы с базой MCPS
 * Created by iMacAverage on 06.02.16.
 */
public interface MCPSDao {

    void fillOrderHeap(int count);

    void grabOldOrderHeap(int count, String whoDo);

    void grabNewOrderHeap(int count, String whoDo);

    boolean isExistOrderHeap();

    List<Integer> getIdProcessedOrderHeap(String whoDo);

    void delOrderHeap(String whoDo);

}
