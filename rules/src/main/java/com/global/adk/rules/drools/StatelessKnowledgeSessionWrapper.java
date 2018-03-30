package com.global.adk.rules.drools;

import com.global.adk.common.exception.RuleException;
import com.global.adk.rules.drools.module.Description;
import org.drools.command.Command;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.impl.StatelessKnowledgeSessionImpl;
import org.drools.runtime.Globals;
import org.drools.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class StatelessKnowledgeSessionWrapper implements StatelessKnowledgeSession {

    private static final Logger logger = LoggerFactory.getLogger(StatelessKnowledgeSessionWrapper.class);

    private StatelessKnowledgeSessionImpl session;

    private DroolsEngine.DroolsElement ruleElement;

    private String description;

    public StatelessKnowledgeSessionWrapper(StatelessKnowledgeSessionImpl session, DroolsEngine.DroolsElement ruleElement, String description) {
        this.ruleElement = ruleElement;
        this.description = description;
        this.session = session;
    }

    @Override
    public void execute(final Object object) {

        new ExecuteInterceptor<Void>() {

            @Override
            Void doExecute(StatelessKnowledgeSession session, DroolsExecuteContext context) {

                List<Object> objects = new LinkedList<>();

                objects.add(object);
                objects.add(context);

                session.execute(objects);

                return null;
            }
        }.execute(session);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void execute(Iterable objects) {

        try {
            final Collection<Object> parameters = (Collection<Object>) objects;
            new ExecuteInterceptor<Void>() {

                @Override
                Void doExecute(StatelessKnowledgeSession session, DroolsExecuteContext context) {

                    parameters.add(context);
                    session.execute(parameters);

                    return null;
                }
            }.execute(session);
        } catch (ClassCastException e) {
            throw new RuleException(String.format("规则参数不接受collection以外的类型 objects type =  %s", objects.getClass()
                    .getName()));
        }
    }

    @Override
    public <T> T execute(final Command<T> command) {

        throw new RuleException("不支持的调用方式（Command），请使用其他execute重载.");
        //        return new ExecuteInterceptor<T>() {
        //
        //            @SuppressWarnings("unchecked")
        //            @Override
        //            T doExecute(StatelessKnowledgeSession session, RuleExecuteContext context) {
        //
        //                if (command instanceof InsertElementsCommand) {
        //                    InsertElementsCommand insertElementsCommand = (InsertElementsCommand) command;
        //                    context.setTargetObject(insertElementsCommand.getObjects());
        //                } else if (command instanceof InsertObjectCommand) {
        //                    InsertObjectCommand objectCommand = (InsertObjectCommand) command;
        //                    context.setTargetObject(objectCommand.getObject());
        //                    objectCommand.setObject(context);
        //
        //                } else {
        //                    throw new RuleException(String.format("系统只InsertElementCommand&InsertObjectCommand,commandType=%s", command.getClass().getName()));
        //                }
        //                return (T) session.execute(CommandFactory.newInsert(context));
        //            }
        //
        //
        //        }.execute(session);

    }

    private abstract class ExecuteInterceptor<T> {

        T execute(StatelessKnowledgeSession session) {

            DroolsExecuteContext context = new DroolsExecuteContext();
            context.setDescription(StatelessKnowledgeSessionWrapper.this.description);
            context.setRequest(StatelessKnowledgeSessionWrapper.this.ruleElement.getRequest());

            long begin = System.currentTimeMillis();
            context.setStartTime(new Timestamp(begin));

            Exception ex = null;

            T target = null;
            try {

                target = doExecute(session, context);

            } catch (Exception e) {
                ex = e;
                throw e;
            } finally {
                long end = System.currentTimeMillis();
                context.setEndTime(new Timestamp(end));
                context.setElapse(end - begin);
                context.setError(ex);
                context.setExecuted(context.getError() == null);
                logger.info("执行规则=>{},knowledgebase({})", context.toString(),((StatelessKnowledgeSessionImpl)session).getRuleBase());
            }
            return target;
        }

        abstract T doExecute(StatelessKnowledgeSession session, DroolsExecuteContext context);
    }

    //--------------------------
    //- 以下忽略
    //--------------------------
    @Override
    public void addEventListener(WorkingMemoryEventListener listener) {

        session.addEventListener(listener);
    }

    @Override
    public void removeEventListener(WorkingMemoryEventListener listener) {

        session.removeEventListener(listener);
    }

    @Override
    public Collection<WorkingMemoryEventListener> getWorkingMemoryEventListeners() {

        return session.getWorkingMemoryEventListeners();
    }

    @Override
    public void addEventListener(AgendaEventListener listener) {

        session.addEventListener(listener);
    }

    @Override
    public void removeEventListener(AgendaEventListener listener) {

        session.removeEventListener(listener);
    }

    @Override
    public Collection<AgendaEventListener> getAgendaEventListeners() {

        return session.getAgendaEventListeners();
    }

    @Override
    public void addEventListener(ProcessEventListener listener) {

        session.addEventListener(listener);
    }

    @Override
    public void removeEventListener(ProcessEventListener listener) {

        session.removeEventListener(listener);
    }

    @Override
    public Collection<ProcessEventListener> getProcessEventListeners() {

        return session.getProcessEventListeners();
    }

    @Override
    public Globals getGlobals() {

        return session.getGlobals();
    }

    @Override
    public void setGlobal(String identifer, Object value) {
        if (!ruleElement.getDescription().getEventRuleType().equals(Description.EventRuleType.DEFAULT)) {
            session.setGlobal(identifer, value);
        }

    }

}
