/* 
 * www.cutebear.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * @author karott (e-mail:chenlin@yiji.com) 2017-04-19 18:48 创建
 *
 */
package com.global.adk.biz.executor.support.dubbo;

import com.alibaba.dubbo.common.bytecode.ClassGenerator;
import com.alibaba.dubbo.common.utils.ClassHelper;
import com.alibaba.dubbo.config.annotation.Service;
import com.global.adk.api.annotation.DubboServiceAPI;
import com.global.adk.api.annotation.InvokeName;
import com.global.adk.common.compiler.Compiler;
import com.global.adk.common.exception.CompilerException;
import com.global.boot.core.Apps;
import com.yjf.common.lang.context.OperationContext;
import com.yjf.common.service.Order;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author karott (e-mail:chenlin@yiji.com)
 */
public class DubboProviderGenerateFactory {

    private static final String IMPL_PATH = Apps.getBasePackage() + ".gen.dubboprovider.";

    /**
     * 根据服务类创建dubbo provider class
     */
    public static <T> Class createImplementionByService(Class<T> apiService, DubboServiceAPI api) {
        Compiler compiler = Compiler.getInstance();

        CtClass implCtClass = newCtClassCompitableWithDubbo(apiService, IMPL_PATH);
        addClassAnno(implCtClass, api);

        createField(compiler, implCtClass);

        Method[] apiMethods = apiService.getDeclaredMethods();
        for (Method method : apiMethods) {
            compiler.methodWeave(implCtClass, apiService,
                    createMethodByApiServiceMethod(method, apiService));
        }

        try {
            return implCtClass.toClass();
        } catch (CannotCompileException e) {
            throw new IllegalStateException("构建dubbo provider实现类过程失败", e);
        }
    }

    private static void addClassAnno(CtClass ctClass, DubboServiceAPI api) {
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        Annotation annotation = new Annotation(Service.class.getCanonicalName(), constPool);
        annotation.addMemberValue("group", new StringMemberValue(api.group(), constPool));
        annotation.addMemberValue("version", new StringMemberValue(api.version(), constPool));
        //annotation.addMemberValue("delay", new IntegerMemberValue(constPool, 1));

        AnnotationsAttribute attributeInfo = new AnnotationsAttribute(constPool,
                AnnotationsAttribute.visibleTag);
        attributeInfo.addAnnotation(annotation);

        ClassFile classFile = ctClass.getClassFile();
        classFile.addAttribute(attributeInfo);
    }

    private static void createField(Compiler compiler, CtClass implCtClass) {
        compiler.filedWeaveWithAnnotation(implCtClass,
                "\n\tprivate com.global.adk.biz.executor.ExecutorContainer container;",
                Autowired.class.getCanonicalName());
        compiler.filedWeave(implCtClass,
                "\n\tprivate com.yjf.common.lang.context.OperationContext context=new com.yjf.common.lang.context.OperationContext();");
    }

    private static String createMethodByApiServiceMethod(Method method, Class apiService) {
        String modifiers = "\tpublic";
        Class<?> returnType = method.getReturnType();
        String methodName = method.getName();
        Parameter[] params = method.getParameters();
        String serviceName = apiService.getSimpleName() + "." + methodName;

        InvokeName invokeName = method.getAnnotation(InvokeName.class);
        if (invokeName != null) {
            serviceName = invokeName.value();
        }

        StringBuilder builder = new StringBuilder();
        builder.append(modifiers).append(" ").append(returnType.getCanonicalName()).append(" ")
                .append(methodName).append("(");

        Parameter orderParam = null;
        Parameter contextParam = null;

        for (Parameter param : params) {
            builder.append(param.toString()).append(",");

            if (Order.class.isAssignableFrom(param.getType())) {
                orderParam = param;
            }
            if (OperationContext.class.isAssignableFrom(param.getType())) {
                contextParam = param;
            }
        }

        if (null == orderParam) {
            throw new IllegalArgumentException("非法api order入参:" + apiService);
        }

        builder.deleteCharAt(builder.length() - 1);

        builder.append("){\n\t")
                .append("return (" + returnType.getCanonicalName() + ")container.accept("
                        + orderParam.getName() + ",")
                .append("\"" + serviceName + "\"")
                .append("," + (null != contextParam ? contextParam.getName() : "context") + ");\n\t}");

        return builder.toString();
    }


    /**
     * 使用dubbo classpool
     */
    private static CtClass newCtClassCompitableWithDubbo(Class supperClass, String path) {

        try {
            ClassLoader cl = ClassHelper.getClassLoader(supperClass);
            ClassGenerator cc = ClassGenerator.newInstance(cl);
            ClassPool classPool = cc.getClassPool();
            classPool.insertClassPath(path);

            String dynamicClassName = Compiler.genClassNameWithPath(supperClass, path);

            CtClass ctClass = classPool.makeClass(dynamicClassName);

            if (supperClass != null & supperClass != Object.class) {
                if (supperClass.isInterface()) {
                    ctClass.setInterfaces(new CtClass[]{classPool.get(supperClass.getName())});
                } else {
                    ctClass.setSuperclass(classPool.get(supperClass.getName()));
                }
            }

            return ctClass;
        } catch (NotFoundException | CannotCompileException e) {
            throw new CompilerException(
                    String.format("创建CtClass过程中出错supperClass = %s", supperClass), e);
        }
    }
}
