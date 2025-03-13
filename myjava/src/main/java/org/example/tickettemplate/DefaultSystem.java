package org.example.tickettemplate;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class DefaultSystem implements System {
    private static final Logger logger = Logger.getLogger(DefaultSystem.class.getName());
    private String name = "default";
    private final String createTicketLockPrefix = "default_create_ticket_";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TicketResponse createTicket(String context, Ticket ticketCfg) throws Exception {
        // 1. 构建请求内容
        Content content = null; // 这里应当构建有效的请求内容

        // 2. 发送请求并获取响应
        TicketResponse bizEntity = postRequest(context, content, ticketCfg, "userID", "messageID", "ticketSystemName");

        if (bizEntity == null) {
            throw new Exception("Ticket creation failed");
        }

        // 3. 异步保存响应到数据库
        CompletableFuture.runAsync(() -> saveResponse(context, bizEntity, "messageID"));

        return bizEntity;
    }

    @Override
    public TicketResponse getTicket(String context, String messageID, String ticketSystemName) {
        // 查询数据库以获取已创建的票据
        // 将数据库对象转换为 TicketResponse
        return new TicketResponse();
    }

    private TicketResponse postRequest(String context, Content req, Ticket cfg, String user, String messageID, String name) throws Exception {
        // 1. 获取分布式锁

        // 2. 发送 HTTP 请求到第三方系统
        CreateTicketResp res = createDefaultTicket(context, req, user);
        if (res == null) {
            logger.warning(String.format("[PostRequest] system name: %s, postFunc error: request failed, user: %s", name, user));
            throw new Exception("Request failed");
        }

        return new TicketResponse(
                messageID,
                user,
                name,
                res.getTicketLink(),
                "",
                cfg,
                new OptionFromThirdParty(res.getChatGroupId(), res.isSendCardToChatGroup())
        );
    }

    private void saveResponse(String context, TicketResponse result, String messageID) {
        // 1. 存储结果到数据库
        logger.info(String.format("[SaveResponse] Saving ticket response for messageID: %s", messageID));
    }

    private CreateTicketResp createDefaultTicket(String context, Content req, String user) {
        // 获取 Ticket.SiteKeyAndEndpoints 的 endpoint
        // 调用第三方 API
        return new CreateTicketResp();
    }
}

