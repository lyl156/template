package org.example.populatedb;

import java.util.concurrent.*;
import java.util.*;
import java.util.stream.Stream;

class MockData {
    private long id;

    private String businessId;

    public MockData(long id, String businessId) {
        this.id = id;
        this.businessId = businessId;
    }

    public long getId() {
        return id;
    }

    public String getBusinessId() {
        return businessId;
    }

}

class DB {
    public List<MockData> findMockData(long offset, int limit) {
        // 模拟数据库查询
        return new ArrayList<>();
    }
}

public class ScanApprovalService {
    private static final int LIMIT = 500;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public Stream<MockData> scanApproval() {
        // 链表实现，默认无界（可设最大值）
        BlockingQueue<MockData> queue = new LinkedBlockingQueue<>();

        executor.submit(() -> {
            try {
                long offset = 0;
                DB db = new DB();
                while (true) {
                    List<MockData> mockDataList = db.findMockData(offset, LIMIT);
                    for (MockData mockData : mockDataList) {
                        queue.put(mockData);
                    }

                    if (mockDataList.size() < LIMIT) {
                        break;
                    }
                    offset = mockDataList.get(mockDataList.size() - 1).getId();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                queue.add(null); // 结束标志
            }
        });

        return Stream.generate(() -> {
            try {
                MockData data = queue.take(); // 若 queue 为空，则 block
                return data == null ? null : data;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }).takeWhile(Objects::nonNull);
    }
}
