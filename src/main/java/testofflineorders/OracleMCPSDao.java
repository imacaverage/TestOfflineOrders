package testofflineorders;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Класс работы с базой MCPS на Oracle
 * Created by iMacAverage on 06.02.16.
 */

public class OracleMCPSDao implements MCPSDao {

    private final JdbcTemplate jdbcTemplate;

    public OracleMCPSDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void fillOrderHeap(int count) {
        String sql = "call mcps.fill_data_orders_heap(?)";
        this.jdbcTemplate.update(sql, count);
    }

    @Override
    public void grabOldOrderHeap(int count, String whoDo) {
        String sql = "call mcps.without_plate_old(?,?)";
        this.jdbcTemplate.update(sql, count, whoDo);
    }

    @Override
    public void grabNewOrderHeap(int count, String whoDo) {
        String sql = "call mcps.without_plate_new(?,?)";
        this.jdbcTemplate.update(sql, count, whoDo);
    }

    @Override
    public boolean isExistOrderHeap() {
        String sql = "select count(*) as count from mcps.data_orders_heap where who_do is null";
        return this.jdbcTemplate.queryForLong(sql) > 0;
    }

    @Override
    public List<Integer> getIdProcessedOrderHeap(String whoDo) {
        String sql = "select id from mcps.data_orders_heap where who_do = ?";
        return this.jdbcTemplate.queryForList(sql, new Object[]{whoDo}, Integer.class);
    }

    @Override
    public void delOrderHeap(String whoDo) {
        String sql = "delete from mcps.data_orders_heap where who_do = ?";
        this.jdbcTemplate.update(sql, whoDo);
    }

}
