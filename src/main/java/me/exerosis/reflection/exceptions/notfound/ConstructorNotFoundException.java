package me.exerosis.reflection.exceptions.notfound;

import me.exerosis.reflection.exceptions.ReflectException;
import me.exerosis.reflection.exceptions.node.ExceptionBuilder;
import me.exerosis.reflection.exceptions.node.ExceptionNode;

public class ConstructorNotFoundException extends ReflectException {

    public ConstructorNotFoundException(Class<?> clazz, Class<?>... params) {
        super(new ExceptionBuilder("Constructor not found!", getExceptionNodes(clazz, params)));
    }

    private static ExceptionNode[] getExceptionNodes(Class<?> clazz, Class<?>... params) {
        ExceptionNode[] nodes = new ExceptionNode[2];
        if (clazz != null)
            nodes[0] = new ExceptionNode("ReflectClass", clazz.getSimpleName());

        if (params.length == 0)
            return nodes;
        StringBuilder builder = new StringBuilder("\n");
        for (Class<?> param : params)
            builder.append('-').append(param.getSimpleName()).append("\n\t");

        nodes[1] = new ExceptionNode("Params", builder.toString());
        return nodes;
    }
}