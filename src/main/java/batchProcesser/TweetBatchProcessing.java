package batchProcesser;

import kwetter.domain.Tweet;
import kwetter.domain.User;
import kwetter.service.KwetterService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.batch.api.Batchlet;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Niek on 16-3-14.
 */
@Dependent
@Named("TweetBatchProcessing")
public class TweetBatchProcessing implements Batchlet {
    private BufferedReader breader;
    private String filename;

    @Inject
    JobContext jobCtx;

    @Inject
    KwetterService service;

    @Override
    public String process() throws Exception {
        this.filename = jobCtx.getProperties().getProperty("input_file");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        /* Parse JSON */
        JSONParser parser = new JSONParser();
        try {
            JSONObject obj = (JSONObject) parser.parse(readUrl(classLoader.getResource(filename)));
            JSONArray arr = (JSONArray) obj.get("Tweets");
            Iterator itr = arr.iterator();
            while (itr.hasNext()) {
                JSONObject element = (JSONObject) itr.next();
                String name = (String) element.get("screenName");

                String tweetContent = (String) element.get("tweet");
                String postedFrom = (String) element.get("postedFrom");

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = formatter.parse((String) element.get("postDate"));

                User user = service.find(name);
                if (user == null) {
                    user = new User(name, "http", "New user");
                    service.create(user);
                    user = service.find(name);
                }

                Tweet newTweet = new Tweet(user, tweetContent, date, postedFrom);
                service.postNewTweet(newTweet);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "COMPLETED";
    }

    @Override
    public void stop() throws Exception {
        this.breader.close();
    }

    private static String readUrl(URL url) throws Exception {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }

            return buffer.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

    }
}
