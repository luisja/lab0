package es.unizar.tmdad.lab0.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

@Service
public class TwitterLookupService {
	
	@Value("${twitter.consumerKey}")
	private String consumerKey;
	
	@Value("${twitter.consumerSecret}")
	private String consumerSecret;
	
	@Value("${twitter.accessToken}")
	private String accessToken;
	
	@Value("${twitter.accessTokenSecret}")
	private String accessTokenSecret;
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	private Map<String,Stream> map = new HashMap<>();
	
	public void search(String query) {
		
		if(!map.containsKey(query)) {
			if (map.size() >= 10) {
				Object firstKey = map.keySet().toArray()[0];
				map.get(firstKey).close();
				map.remove(firstKey);
			}
			
	        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
	        List<StreamListener> list = new ArrayList<StreamListener>();
	        list.add(new SimpleStreamListener(messagingTemplate, query));
	        Stream s = twitter.streamingOperations().filter(query, list);
	        map.put(query, s);
		}
    }
}