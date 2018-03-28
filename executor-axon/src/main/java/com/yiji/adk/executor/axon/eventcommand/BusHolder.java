/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-10-25 20:54 创建
 *
 */
package com.yiji.adk.executor.axon.eventcommand;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.GenericDomainEventMessage;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class BusHolder {

    public static CommandBus commandBus;

    public static EventBus eventBus;


    public static void dispatch(Object command) {
        commandBus.dispatch(GenericCommandMessage.asCommandMessage(command));
    }

    public static void publish(Object event) {
        eventBus.publish(GenericDomainEventMessage.asEventMessage(event));
    }
}
