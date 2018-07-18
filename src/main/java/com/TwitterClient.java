package com;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import java.util.concurrent.BlockingQueue;

public class TwitterClient
{
    public static Client run(StatusesFilterEndpoint endpoint, BlockingQueue<String> queue)
    {
        String consumerKey = "AcfTlGEf4Oo8z0toYrLvXxkvT";
        String consumerSecret = "WrxCrKnJv36wPo0iQBgcO6s5BvIyi81GViKJ39r1QH5mar5ofG";
        String token = "88861115-bIdAIbTr6fptyLQY4GPbRZDoKhN0eGbjU9pLL9EXw";
        String secret = "l1MjJhrkStvtdnYh0tuTds5VszG7p3UfTGvEy5E6Rv7rk";

        Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);
        Client client = new ClientBuilder().hosts(Constants.STREAM_HOST)
                .endpoint(endpoint)
                .authentication(auth)
                .processor(new StringDelimitedProcessor(queue)).build();

        return client;
    }
}
