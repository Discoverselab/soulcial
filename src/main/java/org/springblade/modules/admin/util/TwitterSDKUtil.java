package org.springblade.modules.admin.util;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.Get2TweetsIdResponse;
import com.twitter.clientlib.model.ResourceUnauthorizedProblem;

import java.util.HashSet;
import java.util.Set;

public class TwitterSDKUtil {

	public static void main(String[] args) {

		/**
		 * Set the credentials for the required APIs.
		 * The Java SDK supports TwitterCredentialsOAuth2 & TwitterCredentialsBearer.
		 * Check the 'security' tag of the required APIs in https://api.twitter.com/2/openapi.json in order
		 * to use the right credential object.
		 */
		System.setProperty("http.proxyHost", "localhost");
		System.setProperty("http.proxyPort", "7890");
		TwitterCredentialsOAuth2 twitterCredentialsOAuth2 = new TwitterCredentialsOAuth2(
			"X05OQUhkbjNxTVVOUFJqMnI0c0g6MTpjaQ",
			"hlzjkrw2D3KxF4_4PCmEMwVH2iJpiFpPSWjeRh5yrBX8zGCKM7",
			"1665007675040735232-e5dvPpxgwSbY44YwPxLzMYy8WQUoc7",
			"AAAAAAAAAAAAAAAAAAAAAK7PnwEAAAAAglWKci0fqZ0e%2BsHtu5gUITmDzwA%3DudMEhPCyJvieWY4eVzI6ONrldAUe9JT9KpXVzSPiFgYrOLAIx8");
		TwitterApi apiInstance = new TwitterApi(twitterCredentialsOAuth2);

		Set<String> tweetFields = new HashSet<>();
		tweetFields.add("author_id");
		tweetFields.add("id");
		tweetFields.add("created_at");

		try {
			// findTweetById
			Get2TweetsIdResponse result = apiInstance.tweets().findTweetById("1665044573339676672")
				.tweetFields(tweetFields)
				.execute();
			if(result.getErrors() != null && result.getErrors().size() > 0) {
				System.out.println("Error:");

				result.getErrors().forEach(e -> {
					System.out.println(e.toString());
					if (e instanceof ResourceUnauthorizedProblem) {
						System.out.println(((ResourceUnauthorizedProblem) e).getTitle() + " " + ((ResourceUnauthorizedProblem) e).getDetail());
					}
				});
			} else {
				System.out.println("findTweetById - Tweet Text: " + result.toString());
			}
		} catch (ApiException e) {
			System.err.println("Status code: " + e.getCode());
			System.err.println("Reason: " + e.getResponseBody());
			System.err.println("Response headers: " + e.getResponseHeaders());
			e.printStackTrace();
		}

	}

}
