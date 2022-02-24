package back_end;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Tags implements Serializable{
    private Object[] tagsList;
    private ArrayList<String> tags;

    public Tags(Object[] tagsList) {
        this.tagsList = tagsList;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(String[] newTags) {
        tags = new ArrayList<String>(Arrays.asList(newTags));
    }

    public String toString() {
        return tags.toString();
    }
}
