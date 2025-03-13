package org.example.populatedb;

import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.logging.Logger;

public class PopulateXX {
    private static final Logger logger = Logger.getLogger(PopulateXX.class.getName());
    private static final Pattern MONGO_ID_PATTERN = Pattern.compile("^[a-fA-F0-9]{24}$");

    public static void populateXX() {
        String logId = "logID";
        Context ctx = new Context(logId);

        long count = 0;

        // try-with-resources 语法允许在 try() 里声明 stream，在 try 代码块执行完后自动关闭 stream
        try (Stream<MockData> stream = new ScanApprovalService().scanApproval()) {
            count = stream.filter(mockData -> {
                if (!isValidMongoID(mockData.getBusinessId())) {
                    logger.warning("approval is not procurement demand, mockData ID: " + mockData.getId());
                    return false;
                }
                return true; // 让该数据继续进入流
            }).peek(mockData -> {
                try {
                    fillMockData(ctx, mockData);
                    logger.info("mockData ID: " + mockData.getId() + ", businessID: " + mockData.getBusinessId() + ", fillApproval succeed");
                } catch (Exception e) {
                    logger.warning("mockData ID: " + mockData.getId() + ", businessID: " + mockData.getBusinessId() + ", fillApproval err: " + e.getMessage());
                }
            }).count();
            // Stream 是惰性求值的（Lazy Evaluation），如果没有 count() 这样的终端操作，filter() 和 peek() 代码不会执行！
        }

        logger.info("populateApprovalName count: " + count);
    }

    private static boolean isValidMongoID(String id) {
        return MONGO_ID_PATTERN.matcher(id).matches();
    }

    private static void fillMockData(Context ctx, MockData mockData) throws Exception {
        logger.info("mockData ID: " + mockData.getId());
        // get other data corresponding to mockData
        // update other data
        // store other data
    }

    public static void main(String[] args) {
        populateXX();
    }
}

class Context {
    private String logId;

    public Context(String logId) {
        this.logId = logId;
    }

    public String getLogId() {
        return logId;
    }
}
