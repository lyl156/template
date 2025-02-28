package org.example.multithread;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

class ItemType {
    String id;
    ItemType(String id) { this.id = id; }
}

enum BusinessType {
    EXAMPLE
}

class Breakdown {
    String id;
    Breakdown(String id) { this.id = id; }
    String getId() { return id; }
}

class OaAccrual {
    String id;
    OaAccrual(String id) { this.id = id; }
}

class RequestByStatement {
    BusinessType businessType;
    String partnerID, month, oaContractID, ourEntity, mediaSource, region, appID;
    RequestByStatement(BusinessType businessType, String month) {
        this.businessType = businessType;
        this.partnerID = "partnerID";
        this.month = month;
        this.oaContractID = "oaContractID";
        this.ourEntity = "ourEntity";
        this.mediaSource = "mediaSource";
        this.region = "region";
        this.appID = "appID";
    }
}

class Service {
    List<Breakdown> findBreakdownsByAggregateID(String id, BusinessType businessType) {
        return Collections.singletonList(new Breakdown("1"));
    }
    List<OaAccrual> queryAccrualsByStatementParams(RequestByStatement req) {
        return Collections.singletonList(new OaAccrual("1"));
    }
}

public class MultiThread {
    public static void main(String[] args) {
        Service srv = new Service();
        List<ItemType> items = Arrays.asList(new ItemType("item1"), new ItemType("item2"));
        BusinessType businessType = BusinessType.EXAMPLE;
        String month = "2025-02";

        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<CompletableFuture<List<OaAccrual>>> futures = new ArrayList<>();

        for (ItemType item : items) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                List<OaAccrual> accruals = new ArrayList<>();
                if (businessType == BusinessType.EXAMPLE) {
                    List<Breakdown> breakdowns = srv.findBreakdownsByAggregateID(item.id, businessType);
                    for (Breakdown brk : breakdowns) {
                        System.out.println("brk: " + brk.getId());
                        RequestByStatement req = new RequestByStatement(businessType, month);
                        accruals.addAll(srv.queryAccrualsByStatementParams(req));
                    }
                }
                return accruals;
            }, executor));
        }

        List<OaAccrual> accrualList = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        executor.shutdown();
        System.out.println("Accrual List: " + accrualList);
    }
}
