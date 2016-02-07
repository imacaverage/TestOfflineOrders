package testofflineorders;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Класс для тестирования устранения проблемы дублей при обработке оффлайн ордеров
 * Created by iMacAverage on 04.02.16.
 */
public class TestOfflineOrders {

    public final static int COUNT_ORDERS = 10000;

    public final static int COUNT_GRAB_ORDERS = 100;

    public final static int COUNT_THREAD = 10;

    private final AnnotationConfigApplicationContext context;

    private final ArrayList<Integer> listId;

    public TestOfflineOrders(AnnotationConfigApplicationContext context) {
        this.context = context;
        this.listId = new ArrayList<>();
    }

    public boolean testGrabber(boolean oldGrabber) throws ExecutionException, InterruptedException {
        Grabber grabber;
        List<Future<List<Integer>>> listFuture = new ArrayList<>();
        // создаю ордера для теста
        OracleMCPSDao oracleMCPSDao = (OracleMCPSDao) this.context.getBean("oracleMCPSDao");
        oracleMCPSDao.fillOrderHeap(TestOfflineOrders.COUNT_ORDERS);
        // получаю объект граббера
        if (oldGrabber)
            grabber = (Grabber) this.context.getBean("oldGrabber");
        else
            grabber =  (Grabber) this.context.getBean("newGrabber");
        // запускаю потоки
        ExecutorService executor = Executors.newFixedThreadPool(TestOfflineOrders.COUNT_THREAD);
        for (int i = 0; i < TestOfflineOrders.COUNT_THREAD; i++) {
            Future<List<Integer>> future = executor.submit(grabber);
            listFuture.add(future);
        }
        this.listId.clear();
        // получаю id обработанных ордеров от task-ов
        for (Future<List<Integer>> future : listFuture)
            this.listId.addAll(future.get());
        executor.shutdown();
        return this.listId.stream().allMatch(new HashSet<>()::add);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Context.class);
        TestOfflineOrders testOfflineOrders = new TestOfflineOrders(context);
        try {
            System.out.printf("Test old Grabber: %b\n", testOfflineOrders.testGrabber(true));
            System.out.printf("Test new Grabber: %b\n", testOfflineOrders.testGrabber(false));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
