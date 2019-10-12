package com.jlchn.demoservice1.hystrix;

import java.util.concurrent.Callable;

import com.jlchn.demoservice1.context.UserContext;
import com.jlchn.demoservice1.context.UserContextHolder;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;

public class ThreadLocalCopyStrategy extends HystrixConcurrencyStrategy {

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {

        return super.wrapCallable(new DelegatingUserContextCallable<T>(callable, UserContextHolder.getContext()));
    }

    private class DelegatingUserContextCallable<T> implements Callable<T> {
        private final Callable<T> delegate;
        private UserContext userInfo;
        public DelegatingUserContextCallable(Callable<T> callable, UserContext userContext) {
            this.delegate = callable;
            this.userInfo = userContext;
        }

        public T call() throws Exception {

           UserContextHolder.setContext(this.userInfo);

            try {
                return delegate.call();
            }
            finally {
                this.userInfo = null; //reset
            }
        }
    }
}
