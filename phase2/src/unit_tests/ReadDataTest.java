package unit_tests;

import controller.LogManager;
import controller.RevisionManager;
import controller.TagManager;
import model.Tags;
import model.data.ReadData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.*;

public class ReadDataTest {
    private ReadData rd = new ReadData();

    @BeforeEach
    void init() {
        RevisionManager.setRevisions(new HashMap<>());
    }

    @Test
    void testReadLogs() {
        Date date = new Date();
        String[] expected = {"A", "B", date.toString()};
        LogManager.addLog("A", "B");

        assertEquals(expected[0], rd.readLogs().get(0)[0]);
        assertEquals(expected[1], rd.readLogs().get(0)[1]);
        assertEquals(expected[2], rd.readLogs().get(0)[2]);
    }

    @Test
    void testReadTags() {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("A");
        TagManager.addTag("A");

        HashSet<String> actual = rd.readTags();

        assertEquals(hashSet, actual);
    }

    @Test
    void testReadRevisions() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("A");
        Tags tags = new Tags(new ArrayList<>());
        tags.setTags(stringArrayList);
        ArrayList<Tags> tagsArrayList = new ArrayList<>();
        tagsArrayList.add(tags);
        RevisionManager.updateRevisions("Old", "New", tags);
        rd.readRevisions();
        ArrayList<Tags> actual = RevisionManager.getRevisions().get("New");

        assertEquals(tagsArrayList.toString(), actual.toString());
    }
}
