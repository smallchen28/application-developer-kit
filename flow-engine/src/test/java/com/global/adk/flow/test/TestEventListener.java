package com.global.adk.flow.test;

import com.global.adk.flow.annotation.Listen;
import com.global.adk.flow.engine.Execution;

/**
 * @author hasulee
 * @version 1.0.0
 * @see
 * @since 15/11/25
 */
public class TestEventListener{

    @Listen(eventExpression = "xxx,yyy,zzz",priority = Integer.MAX_VALUE)
    public void onChange1(Execution execution , String eventName){
        execution.addAttribute("xxx,yyy,zzz",eventName);
    }

    @Listen(eventExpression = "!xxx,!yyy,!zzz",priority = Integer.MAX_VALUE)
    public void onChange2(Execution execution , String eventName){
        execution.addAttribute("!xxx,!yyy,!zzz",eventName);
    }

    @Listen(eventExpression = "!xxx,yyy,zzz",priority = Integer.MAX_VALUE)
    public void onChange3(Execution execution , String eventName){
        execution.addAttribute("!xxx,yyy,zzz",eventName);
    }

    @Listen(eventExpression = "yyy",priority = Integer.MAX_VALUE)
    public void onChange4(Execution execution , String eventName){
        execution.addAttribute("yyy",eventName);
    }

    @Listen(eventExpression = "*",priority = Integer.MAX_VALUE)
    public void onChange5(Execution execution , String eventName){
        execution.addAttribute("*+2",eventName);
    }

    @Listen(eventExpression = "\\S+",priority = Integer.MAX_VALUE)
    public void onChange6(Execution execution , String eventName){
        execution.addAttribute("\\S+1",eventName);
    }

    @Listen(eventExpression = "\\S+",priority = Integer.MAX_VALUE)
    public void onChange7(Execution execution , String eventName){
        execution.addAttribute("\\S+3",eventName);
    }


    @Listen(eventExpression="assemble_repay_file_success,receive_repay_file_result_success")
    public void onChange8(Execution execution , String eventName){
        System.out.println(eventName);
    }
}