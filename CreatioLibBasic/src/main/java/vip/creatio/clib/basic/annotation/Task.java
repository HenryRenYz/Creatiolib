package vip.creatio.clib.basic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method marked with @Task should be static and have no parameter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Task {

    TaskType value() default TaskType.SYNC_TICK;

    /**
     * Ticking interval, in milliseconds
     * default is 50ms.
     *
     * A sync task will convert this to
     * minecraft tick(by dividing this to 50)
     */
    int period() default 50;
}
