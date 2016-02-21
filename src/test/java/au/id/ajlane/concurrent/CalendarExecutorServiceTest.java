/*
 * Copyright 2016 Aaron Lane
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.id.ajlane.concurrent;

import au.id.ajlane.time.TestClock;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class CalendarExecutorServiceTest
{
    @Test
    public void simpleScheduling() throws Exception
    {
        final Duration interval = Duration.ofMinutes(1L);

        final Instant t0 = Instant.now();
        final Instant tN1 = t0.minus(interval);
        final Instant t1 = t0.plus(interval);
        final Instant t2 = t1.plus(interval);
        final Instant t3 = t2.plus(interval);

        final TestClock clock = new TestClock(interval, t0);

        final DelayBasedCalendarExecutorService executor =
            DelayBasedCalendarExecutorService.wrap(clock, Executors.newSingleThreadScheduledExecutor());
        executor.setAdjustmentPeriod(Duration.ofMillis(500L));

        CalendarExecutorService.singleThread();

        final CalendarFuture<Instant> tN1Future = executor.schedule(() -> tN1, tN1);
        final CalendarFuture<Instant> t0Future = executor.schedule(() -> t0, t0);
        final CalendarFuture<Instant> t1Future = executor.schedule(() -> t1, t1);
        final CalendarFuture<Instant> t2Future = executor.schedule(() -> t2, t2);
        final CalendarFuture<Instant> t3Future = executor.schedule(() -> t3, t3);

        Assert.assertEquals(tN1, tN1Future.get(1L, TimeUnit.SECONDS));
        Assert.assertEquals(t0, t0Future.get(1L, TimeUnit.SECONDS));
        Assert.assertFalse(t1Future.isDone());
        Assert.assertFalse(t2Future.isDone());
        Assert.assertFalse(t3Future.isDone());

        clock.tick();

        Assert.assertEquals(t1, t1Future.get(1L, TimeUnit.SECONDS));
        Assert.assertFalse(t2Future.isDone());
        Assert.assertFalse(t3Future.isDone());

        clock.tick(3);

        Assert.assertEquals(t2, t2Future.get(1L, TimeUnit.SECONDS));
        Assert.assertEquals(t3, t3Future.get(1L, TimeUnit.SECONDS));
    }
}
