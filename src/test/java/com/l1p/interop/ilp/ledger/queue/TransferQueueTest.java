package com.l1p.interop.ilp.ledger.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l1p.interop.ilp.ledger.domain.Transfer;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;

public class TransferQueueTest {
    ObjectMapper mapper = new ObjectMapper();
    
    @Test
    public void test() throws IOException {
        TransferQueue underTest = new TransferQueue(10, 10);

        Calendar cal = Calendar.getInstance();

        Transfer p1 = new Transfer();
        p1.setId("p1");
        p1.setExpiresAt(cal.getTime());

        Transfer p2 = new Transfer();
        p2.setId("p2");
        cal.add(Calendar.SECOND, 2);
        p2.setExpiresAt(cal.getTime());

        underTest.addPrepare(mapper.writeValueAsString(p2));
        underTest.addPrepare(mapper.writeValueAsString(p1));
        underTest.addFufill("p2", "f2");

        System.out.println(underTest.takeNext());
        System.out.println(underTest.takeNext());
        System.out.println(underTest.takeNext());
        System.out.println(underTest.takeNext());

    }
}
