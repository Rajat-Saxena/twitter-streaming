package com.twitter_streaming;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        System.out.println("Starting program.");

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("producer"))
                TwitterProducer.main(args);
            else if (args[0].equalsIgnoreCase("consumer"))
                TwitterConsumer.main(args);
            else
                System.out.println("Invalid parameter: " + args[0] + ".");
        } else
            System.out.println("A parameter needs to be specified. Valid parameters - producer, consumer");
    }
}
