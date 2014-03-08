/*
 * Copyright 2014 OW2 Chameleon
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ow2.chameleon.testing.helpers;

import java.lang.management.*;
import java.util.Map;

/**
 * Dump utilities.
 */
public class ThreadDumpHelper {

    public static void threadDumps() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] ids = bean.getAllThreadIds();
        StringBuilder builder = new StringBuilder();

        printTitle(builder);

        printDeadlockInfo(builder, bean);

        for (long id : ids) {
            ThreadInfo info = bean.getThreadInfo(id);
            printThreadInfo(info, builder);
            printThreadSeparator(builder);
        }

        System.out.println(builder.toString());
    }

    private static void printTitle(StringBuilder builder) {
        builder.append("============ THREAD DUMP ============\n");
    }

    private static void printThreadSeparator(StringBuilder builder) {
        builder.append("-------------------------------------\n");
    }

    private static void printDeadlockInfo(StringBuilder builder, ThreadMXBean bean) {
        long[] ids = bean.getAllThreadIds();
        long[] dds = bean.findDeadlockedThreads();
        if (dds != null) {
            ThreadInfo[] info = bean.getThreadInfo(ids, true, true);
            builder.append("The following threads are deadlocked:");
            for (ThreadInfo ti : info) {
                builder.append("\t").append(ti);
            }
        }
    }

    public static void printThreadInfo(ThreadInfo info, StringBuilder builder) {
        builder.append("Thread ").append(info.getThreadName()).append("\" (").append(info.getThreadId()).append(") -- ");
        formatState(builder, info);
        if (info.isSuspended()) {
            builder.append(" (suspended)");
        }
        if (info.isInNative()) {
            builder.append(" (in native)");
        }
        builder.append("\n");

        // The waited lock.
        LockInfo li = info.getLockInfo();
        if (li != null) {
            builder.append("Waiting for ");
            formatLock(builder, li);
            if (info.getLockOwnerName() != null) {
                builder.append(" owned by ").append(info.getLockOwnerName()).append("(").append(info.getLockOwnerId()).append(")");
            }
            builder.append("\n");
        }

        // The stack
        builder.append("Stack:\n");
        printStack(info, builder, getStackForThread(info.getThreadId()));
    }

    private static StackTraceElement[] getStackForThread(long id) {
        for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            if (entry.getKey().getId() == id) {
                return entry.getValue();
            }
        }
        return new StackTraceElement[0];
    }

    public static void printStack(ThreadInfo info, final StringBuilder sb, final StackTraceElement[] stack) {
        int i = 0;
        for (final StackTraceElement element : stack) {
            sb.append("\tat ").append(element.toString());
            sb.append('\n');
            if (i == 0 && info.getLockInfo() != null) {
                final Thread.State ts = info.getThreadState();
                switch (ts) {
                    case BLOCKED:
                        sb.append("\t-  blocked on ");
                        formatLock(sb, info.getLockInfo());
                        sb.append('\n');
                        break;
                    case WAITING:
                        sb.append("\t-  waiting on ");
                        formatLock(sb, info.getLockInfo());
                        sb.append('\n');
                        break;
                    case TIMED_WAITING:
                        sb.append("\t-  waiting on ");
                        formatLock(sb, info.getLockInfo());
                        sb.append('\n');
                        break;
                    default:
                }
            }

            for (final MonitorInfo mi : info.getLockedMonitors()) {
                if (mi.getLockedStackDepth() == i) {
                    sb.append("\t-  locked ");
                    formatLock(sb, mi);
                    sb.append('\n');
                }
            }
            ++i;
        }

        final LockInfo[] locks = info.getLockedSynchronizers();
        if (locks.length > 0) {
            sb.append("\n\tNumber of locked synchronizers = ").append(locks.length).append('\n');
            for (final LockInfo li : locks) {
                sb.append("\t- ");
                formatLock(sb, li);
                sb.append('\n');
            }
        }
    }

    private static void formatLock(final StringBuilder sb, final LockInfo lock) {
        sb.append("<").append(lock.getIdentityHashCode()).append("> (a ");
        sb.append(lock.getClassName()).append(")");
    }

    private static void formatState(final StringBuilder sb, final ThreadInfo info) {
        final Thread.State state = info.getThreadState();
        sb.append(state);
        switch (state) {
            case BLOCKED: {
                sb.append(" (on object monitor owned by \"");
                sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId()).append(")");
                break;
            }
            case WAITING: {
                final StackTraceElement element = getStackForThread(info.getThreadId())[0];
                final String className = element.getClassName();
                final String method = element.getMethodName();
                if (className.equals("java.lang.Object") && method.equals("wait")) {
                    sb.append(" (on object monitor");
                    if (info.getLockOwnerName() != null) {
                        sb.append(" owned by \"");
                        sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
                    }
                    sb.append(")");
                } else if (className.equals("java.lang.Thread") && method.equals("join")) {
                    sb.append(" (on completion of thread ").append(info.getLockOwnerId()).append(")");
                } else {
                    sb.append(" (parking for lock");
                    if (info.getLockOwnerName() != null) {
                        sb.append(" owned by \"");
                        sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
                    }
                    sb.append(")");
                }
                break;
            }
            case TIMED_WAITING: {
                final StackTraceElement element = info.getStackTrace()[0];
                final String className = element.getClassName();
                final String method = element.getMethodName();
                if (className.equals("java.lang.Object") && method.equals("wait")) {
                    sb.append(" (on object monitor");
                    if (info.getLockOwnerName() != null) {
                        sb.append(" owned by \"");
                        sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
                    }
                    sb.append(")");
                } else if (className.equals("java.lang.Thread") && method.equals("sleep")) {
                    sb.append(" (sleeping)");
                } else if (className.equals("java.lang.Thread") && method.equals("join")) {
                    sb.append(" (on completion of thread ").append(info.getLockOwnerId()).append(")");
                } else {
                    sb.append(" (parking for lock");
                    if (info.getLockOwnerName() != null) {
                        sb.append(" owned by \"");
                        sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
                    }
                    sb.append(")");
                }
                break;
            }
            default:
                break;
        }
    }

}
