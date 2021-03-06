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

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjuster;

import org.junit.Assert;
import org.junit.Test;

public final class SchedulingAdjustersTest
{
    @Test
    public void testChain()
    {
        final OffsetDateTime jan1 = OffsetDateTime.of(2000, 1, 1, 6, 0, 0, 0, ZoneOffset.ofHours(0));
        final OffsetDateTime jan17 = OffsetDateTime.of(2000, 1, 17, 17, 0, 0, 0, ZoneOffset.ofHours(1));
        final OffsetDateTime feb15 = OffsetDateTime.of(2000, 2, 16, 10, 0, 0, 0, ZoneOffset.ofHours(18));

        final TemporalAdjuster adjuster = SchedulingAdjusters.chain(
            SchedulingAdjusters.nextOrSameTime(OffsetTime.of(12, 0, 0, 0, ZoneOffset.ofHours(-4))),
            SchedulingAdjusters.nextOrSameDayOfMonth(15),
            SchedulingAdjusters.nextOrSameWeekday()
        );

        Assert.assertEquals(
            jan17.toInstant(),
            jan1.with(adjuster)
                .toInstant()
        );
        Assert.assertEquals(
            feb15.toInstant(),
            jan17.with(adjuster)
                .toInstant()
        );
    }

    @Test
    public void testNextDay()
    {
        final OffsetDateTime dec31 = OffsetDateTime.of(2000, 12, 31, 16, 0, 0, 0, ZoneOffset.ofHours(0));
        final OffsetDateTime jan1 = OffsetDateTime.of(2001, 1, 1, 17, 0, 0, 0, ZoneOffset.ofHours(1));

        final TemporalAdjuster adjuster = SchedulingAdjusters.nextDay();

        Assert.assertEquals(
            jan1.toInstant(),
            dec31.with(adjuster)
                .toInstant()
        );
    }

    @Test
    public void testNextDayOfMonth()
    {
        final OffsetDateTime jan1 = OffsetDateTime.of(2000, 1, 1, 3, 0, 0, 0, ZoneOffset.ofHours(0));
        final OffsetDateTime jan31 = OffsetDateTime.of(2000, 1, 31, 4, 0, 0, 0, ZoneOffset.ofHours(1));
        final OffsetDateTime feb3 = OffsetDateTime.of(2000, 2, 3, 5, 0, 0, 0, ZoneOffset.ofHours(2));
        final OffsetDateTime feb15 = OffsetDateTime.of(2000, 2, 15, 6, 0, 0, 0, ZoneOffset.ofHours(3));
        final OffsetDateTime feb29 = OffsetDateTime.of(2000, 2, 29, 7, 0, 0, 0, ZoneOffset.ofHours(4));

        final TemporalAdjuster to15th = SchedulingAdjusters.nextDayOfMonth(15);

        Assert.assertEquals(
            feb15.toInstant(),
            jan31.with(to15th)
                .toInstant()
        );
        Assert.assertEquals(
            feb15.toInstant(),
            feb3.with(to15th)
                .toInstant()
        );

        final TemporalAdjuster to31st = SchedulingAdjusters.nextDayOfMonth(31);

        Assert.assertEquals(
            jan31.toInstant(),
            jan1.with(to31st)
                .toInstant()
        );
        Assert.assertEquals(
            feb29.toInstant(),
            jan31.with(to31st)
                .toInstant()
        );
        Assert.assertEquals(
            feb29.toInstant(),
            feb15.with(to31st)
                .toInstant()
        );
    }

    @Test
    public void testNextMonth()
    {
        final OffsetDateTime jan = OffsetDateTime.of(2000, 1, 31, 3, 0, 0, 0, ZoneOffset.ofHours(0));
        final OffsetDateTime feb = OffsetDateTime.of(2000, 2, 29, 4, 0, 0, 0, ZoneOffset.ofHours(1));
        final OffsetDateTime mar = OffsetDateTime.of(2000, 3, 31, 5, 0, 0, 0, ZoneOffset.ofHours(2));
        final OffsetDateTime apr = OffsetDateTime.of(2000, 4, 30, 6, 0, 0, 0, ZoneOffset.ofHours(3));
        final OffsetDateTime may = OffsetDateTime.of(2000, 5, 31, 7, 0, 0, 0, ZoneOffset.ofHours(4));
        final OffsetDateTime jun = OffsetDateTime.of(2000, 6, 30, 8, 0, 0, 0, ZoneOffset.ofHours(5));
        final OffsetDateTime jul = OffsetDateTime.of(2000, 7, 31, 9, 0, 0, 0, ZoneOffset.ofHours(6));
        final OffsetDateTime aug = OffsetDateTime.of(2000, 8, 31, 10, 0, 0, 0, ZoneOffset.ofHours(7));
        final OffsetDateTime sep = OffsetDateTime.of(2000, 9, 30, 11, 0, 0, 0, ZoneOffset.ofHours(8));
        final OffsetDateTime oct = OffsetDateTime.of(2000, 10, 31, 12, 0, 0, 0, ZoneOffset.ofHours(9));
        final OffsetDateTime nov = OffsetDateTime.of(2000, 11, 30, 13, 0, 0, 0, ZoneOffset.ofHours(10));
        final OffsetDateTime dec = OffsetDateTime.of(2000, 12, 31, 14, 0, 0, 0, ZoneOffset.ofHours(11));
        final OffsetDateTime jan2 = OffsetDateTime.of(2001, 1, 31, 15, 0, 0, 0, ZoneOffset.ofHours(12));
        final OffsetDateTime feb2 = OffsetDateTime.of(2001, 2, 28, 16, 0, 0, 0, ZoneOffset.ofHours(13));

        final TemporalAdjuster adjuster = SchedulingAdjusters.nextMonth();

        OffsetDateTime current = jan;
        Assert.assertEquals(jan.toInstant(), current.toInstant());
        current = current.with(adjuster);
        Assert.assertEquals(feb.toInstant(), current.toInstant());
        current = current.with(adjuster);
        Assert.assertEquals(mar.toInstant(), current.toInstant());
        current = current.with(adjuster);
        Assert.assertEquals(apr.toInstant(), current.toInstant());
        current = current.with(adjuster);
        Assert.assertEquals(may.toInstant(), current.toInstant());
        current = current.with(adjuster);
        Assert.assertEquals(jun.toInstant(), current.toInstant());
        current = current.with(adjuster);
        Assert.assertEquals(jul.toInstant(), current.toInstant());
        current = current.with(adjuster);
        Assert.assertEquals(aug.toInstant(), current.toInstant());
        current = current.with(adjuster);
        Assert.assertEquals(sep.toInstant(), current.toInstant());
        current = current.with(adjuster);
        Assert.assertEquals(oct.toInstant(), current.toInstant());
        current = current.with(adjuster);
        Assert.assertEquals(nov.toInstant(), current.toInstant());
        current = current.with(adjuster);
        Assert.assertEquals(dec.toInstant(), current.toInstant());
        current = current.with(adjuster);
        Assert.assertEquals(jan2.toInstant(), current.toInstant());
        current = current.with(adjuster);
        Assert.assertEquals(feb2.toInstant(), current.toInstant());
    }

    @Test
    public void testNextTime()
    {
        final OffsetDateTime jan1at4pm = OffsetDateTime.of(2000, 1, 1, 16, 0, 0, 0, ZoneOffset.ofHours(0));
        final OffsetDateTime jan1at6pm = OffsetDateTime.of(2000, 1, 2, 6, 0, 0, 0, ZoneOffset.ofHours(12));
        final OffsetDateTime jan1at8pm = OffsetDateTime.of(2000, 1, 1, 20, 0, 0, 0, ZoneOffset.ofHours(0));
        final OffsetDateTime jan2at6pm = OffsetDateTime.of(2000, 1, 3, 12, 0, 0, 0, ZoneOffset.ofHours(18));

        final TemporalAdjuster adjuster = SchedulingAdjusters.nextTime(
            OffsetTime.of(
                14,
                0,
                0,
                0,
                ZoneOffset.ofHours(-4)
            )
        );

        Assert.assertEquals(
            jan1at6pm.toInstant(),
            jan1at4pm.with(adjuster)
                .toInstant()
        );
        Assert.assertEquals(
            jan2at6pm.toInstant(),
            jan1at6pm.with(adjuster)
                .toInstant()
        );
        Assert.assertEquals(
            jan2at6pm.toInstant(),
            jan1at8pm.with(adjuster)
                .toInstant()
        );
    }

    @Test
    public void testNextWeekday()
    {
        final OffsetDateTime fri = OffsetDateTime.of(1999, 12, 31, 16, 0, 0, 0, ZoneOffset.ofHours(0));
        final OffsetDateTime sat = OffsetDateTime.of(2000, 1, 1, 17, 0, 0, 0, ZoneOffset.ofHours(1));
        final OffsetDateTime sun = OffsetDateTime.of(2000, 1, 2, 18, 0, 0, 0, ZoneOffset.ofHours(2));
        final OffsetDateTime mon = OffsetDateTime.of(2000, 1, 3, 19, 0, 0, 0, ZoneOffset.ofHours(3));
        final OffsetDateTime tue = OffsetDateTime.of(2000, 1, 4, 20, 0, 0, 0, ZoneOffset.ofHours(4));
        final OffsetDateTime wed = OffsetDateTime.of(2000, 1, 5, 21, 0, 0, 0, ZoneOffset.ofHours(5));

        final TemporalAdjuster adjuster = SchedulingAdjusters.nextWeekday();

        Assert.assertEquals(
            mon.toInstant(),
            fri.with(adjuster)
                .toInstant()
        );
        Assert.assertEquals(
            mon.toInstant(),
            sat.with(adjuster)
                .toInstant()
        );
        Assert.assertEquals(
            mon.toInstant(),
            sun.with(adjuster)
                .toInstant()
        );
        Assert.assertEquals(
            tue.toInstant(),
            mon.with(adjuster)
                .toInstant()
        );
        Assert.assertEquals(
            wed.toInstant(),
            tue.with(adjuster)
                .toInstant()
        );
    }
}

