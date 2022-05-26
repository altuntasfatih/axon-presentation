package com.demo.wallet.config;

import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotterFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AxonConfig {
    public static final int THRESHOLD = 5;

    @Bean//basically it takes snapshot after every five event.it stores snapshots on SNAPSHOT_EVENT_ENTRY table
    public SnapshotTriggerDefinition walletSnapshotTrigger(Snapshotter snapshotter) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, THRESHOLD);
    }

    //@Bean//default snapshot operations occurs sync. if we configure the AggregateSnapshotter, it can run async.
    public SpringAggregateSnapshotterFactoryBean aggregateSnapshotter() {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        final SpringAggregateSnapshotterFactoryBean bean = new SpringAggregateSnapshotterFactoryBean();
        bean.setExecutor(executor);
        return bean;
    }
}
