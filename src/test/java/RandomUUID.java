import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RandomUUID {

    public static String create()  {
        return java.util.UUID.randomUUID().toString();
    }

    private static long get64LeastSignificantBitsForVersion1() {
        Random random = new Random();
        long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
        long variant3BitFlag = 0x8000000000000000L;
        return random63BitLong + variant3BitFlag;
    }

    private static long get64MostSignificantBitsForVersion1() {

        Calendar cal = Calendar.getInstance();
        cal.set(1582, Calendar.OCTOBER, 15);

        Date startDate = cal.getTime();


        long milli = new Date().getTime() - startDate.getTime();

        long nanos = TimeUnit.MILLISECONDS.toNanos(milli);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milli);

        long timeForUuidIn100Nanos = seconds * 10000000 + nanos * 100;
        long least12SignificatBitOfTime = (timeForUuidIn100Nanos & 0x000000000000FFFFL) >> 4;
        long version = 1 << 12;
        return (timeForUuidIn100Nanos & 0xFFFFFFFFFFFF0000L) + version + least12SignificatBitOfTime;
    }

    private static UUID generateType1UUID() {

        long most64SigBits = get64MostSignificantBitsForVersion1();
        long least64SigBits = get64LeastSignificantBitsForVersion1();

        return new UUID(most64SigBits, least64SigBits);
    }

    public static String createVersion1()  {
        return generateType1UUID().toString();
    }

    //@Test
    public void testCreateVersion1() {
        System.out.println(createVersion1());
    }
}