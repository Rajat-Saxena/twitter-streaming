package com;

public class TwitterClient
{
<<<<<<< HEAD
    static String consumerKey = "";
    static String consumerSecret = "";
    static String accessToken = "";
    static String accessTokenSecret = "";
=======
    public static Client run(StatusesFilterEndpoint endpoint, BlockingQueue<String> queue)
    {
        String consumerKey = "";
        String consumerSecret = "";
        String token = "";
        String secret = "";

        Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);
        Client client = new ClientBuilder().hosts(Constants.STREAM_HOST)
                .endpoint(endpoint)
                .authentication(auth)
                .processor(new StringDelimitedProcessor(queue)).build();

        return client;
    }
>>>>>>> dcd3c75cd87ea6352c4601b2fd7686965d20d33a
}
